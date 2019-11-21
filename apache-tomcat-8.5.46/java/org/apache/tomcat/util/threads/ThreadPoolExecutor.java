/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.tomcat.util.threads;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.tomcat.util.res.StringManager;

/**
 * Same as a java.util.concurrent.ThreadPoolExecutor but implements a much more efficient
 * {@link #getSubmittedCount()} method, to be used to properly handle the work queue.
 * If a RejectedExecutionHandler is not specified a default one will be configured
 * and that one will always throw a RejectedExecutionException
 *
 */
public class ThreadPoolExecutor extends java.util.concurrent.ThreadPoolExecutor {
    /**
     * The string manager for this package.
     */
    protected static final StringManager sm = StringManager
            .getManager("org.apache.tomcat.util.threads.res");

    /**
     * The number of tasks submitted but not yet finished. This includes tasks
     * in the queue and tasks that have been handed to a worker thread but the
     * latter did not start executing the task yet.
     * This number is always greater or equal to {@link #getActiveCount()}.
     */
    // 任务提交数目
    private final AtomicInteger submittedCount = new AtomicInteger(0);

    // 这个参数：是为了防止内存泄漏设置的？
    private final AtomicLong lastContextStoppedTime = new AtomicLong(0L);

    /**
     * Most recent time in ms when a thread decided to kill itself to avoid
     * potential memory leaks. Useful to throttle the rate of renewals of
     * threads.
     */
    private final AtomicLong lastTimeThreadKilledItself = new AtomicLong(0L);

    /**
     * Delay in ms between 2 threads being renewed. If negative, do not renew threads.
     *
     * 更新线程的时间，当获取任务的时候，会判断当前线程创建时间是否在lastContextStoppedTime之前，是的话则终止当前线程
     * 下次如果线程不够，会创建新的线程
     *
     */
    private long threadRenewalDelay = Constants.DEFAULT_THREAD_RENEWAL_DELAY;

    public ThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
        prestartAllCoreThreads();
    }

    public ThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory,
            RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
        prestartAllCoreThreads();
    }

    public ThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        // tomcat将JDK的ThreadPoolExecutor进行了包装，默认用的抛异常的拒绝策略
        // threadFactory(DefaultThreadFactory)负责创建最终的线程；创建的线程默认名是：pool-num-thread
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, new RejectHandler());
        // 提前初始化好核心数目的线程，而不是等待任务来了再建线程
        prestartAllCoreThreads();
    }

    public ThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, new RejectHandler());
        prestartAllCoreThreads();
    }

    public long getThreadRenewalDelay() {
        return threadRenewalDelay;
    }

    public void setThreadRenewalDelay(long threadRenewalDelay) {
        this.threadRenewalDelay = threadRenewalDelay;
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        submittedCount.decrementAndGet();

        if (t == null) {
            stopCurrentThreadIfNeeded();
        }
    }

    /**
     * If the current thread was started before the last time when a context was
     * stopped, an exception is thrown so that the current thread is stopped.
     * 通过抛异常方式，终止当前线程
     */
    protected void stopCurrentThreadIfNeeded() {
        if (currentThreadShouldBeStopped()) {
            long lastTime = lastTimeThreadKilledItself.longValue();
            // 如果最后一次自杀时间加上线程更新时延小于当前系统时间，说明可以自杀了，两次自杀时间超过了threadRenewalDelay
            if (lastTime + threadRenewalDelay < System.currentTimeMillis()) {
                if (lastTimeThreadKilledItself.compareAndSet(lastTime,
                        System.currentTimeMillis() + 1)) {
                    // OK, it's really time to dispose of this thread

                    final String msg = sm.getString(
                                    "threadPoolExecutor.threadStoppedToAvoidPotentialLeak",
                                    Thread.currentThread().getName());

                    throw new StopPooledThreadException(msg);
                }
            }
        }
    }

    // 判断当前线程是否应该终止
    protected boolean currentThreadShouldBeStopped() {
        if (threadRenewalDelay >= 0
            && Thread.currentThread() instanceof TaskThread) {
            TaskThread currentTaskThread = (TaskThread) Thread.currentThread();
            // 当前线程创建时间小于lastContextStoppedTime时间，说明需要终止了
            if (currentTaskThread.getCreationTime() <
                    this.lastContextStoppedTime.longValue()) {
                return true;
            }
        }
        return false;
    }

    public int getSubmittedCount() {
        return submittedCount.get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(Runnable command) {
        // 这里覆盖父类的execute
        execute(command,0,TimeUnit.MILLISECONDS);
    }

    /**
     * Executes the given command at some time in the future.  The command
     * may execute in a new thread, in a pooled thread, or in the calling
     * thread, at the discretion of the <code>Executor</code> implementation.
     * If no threads are available, it will be added to the work queue.
     * If the work queue is full, the system will wait for the specified
     * time and it throw a RejectedExecutionException if the queue is still
     * full after that.
     *
     * @param command the runnable task
     * @param timeout A timeout for the completion of the task
     * @param unit The timeout time unit
     * @throws RejectedExecutionException if this task cannot be
     * accepted for execution - the queue is full
     * @throws NullPointerException if command or unit is null
     */
    public void execute(Runnable command, long timeout, TimeUnit unit) {
        submittedCount.incrementAndGet();
        try {
            super.execute(command);
            // 调用父类的execute方法，如果拒绝执行，则强制放入任务队列
            // 父类execute方法逻辑：
            // （1）小于核心线程，创建新线程执行任务
            // （2）任务入队成功：这里入队成功也被TaskQueue重写了，见上面的分析offer(Runnable o)
            // （3）添加新线程执行任务（如果线程数超了，则不成功），没有成功则reject(command)，下面就可以捕获RejectedExecutionException
        } catch (RejectedExecutionException rx) {
            // 如果tomcat自己的TaskQueue，提供了强制入队方法
            if (super.getQueue() instanceof TaskQueue) {
                final TaskQueue queue = (TaskQueue)super.getQueue();
                try {
                    if (!queue.force(command, timeout, unit)) {
                        submittedCount.decrementAndGet();
                        throw new RejectedExecutionException("Queue capacity is full.");
                    }
                    // 可以看到强制入队时候，是可以响应中断的（捕获了InterruptedException）
                } catch (InterruptedException x) {
                    submittedCount.decrementAndGet();
                    throw new RejectedExecutionException(x);
                }
            } else {
                submittedCount.decrementAndGet();
                throw rx;
            }

        }
    }
    // 设置lastContextStoppedTime的时间，更改核心线程
    public void contextStopping() {
        // 更新lastContextStoppedTime时间，比这个早创建的线程可以自杀啦
        this.lastContextStoppedTime.set(System.currentTimeMillis());

        // save the current pool parameters to restore them later
        int savedCorePoolSize = this.getCorePoolSize();
        TaskQueue taskQueue =
                getQueue() instanceof TaskQueue ? (TaskQueue) getQueue() : null;
        if (taskQueue != null) {
            // note by slaurent : quite oddly threadPoolExecutor.setCorePoolSize
            // checks that queue.remainingCapacity()==0. I did not understand
            // why, but to get the intended effect of waking up idle threads, I
            // temporarily fake this condition.

            // threadPoolExecutor.setCorePoolSize会检查remainingCapacity是否为0；作者在这里伪造了0；
            /**
             * public void setCorePoolSize(int corePoolSize) {
             *     if (corePoolSize < 0)
             *         throw new IllegalArgumentException();
             *     delta不可能大于0哦，也就不会去检查workQueue.size()；而且就算检查也只是检查size，而不是remainingCapacity
             *     总算明白了，原来是JDK版本不一样，Java8去掉了remainingCapacity检查，下面贴上Java6 ThreadPoolExecutor#setCorePoolSizea()源码
             *     int delta = corePoolSize - this.corePoolSize;
             *     this.corePoolSize = corePoolSize;
             *     if (workerCountOf(ctl.get()) > corePoolSize)
             *          代码会执行到这，空闲线程会被中断吧
             *         interruptIdleWorkers();
             *     else if (delta > 0) {
             *         // We don't really know how many new threads are "needed".
             *         // As a heuristic, prestart enough new workers (up to new
             *         // core size) to handle the current number of tasks in
             *         // queue, but stop if queue becomes empty while doing so.
             *         int k = Math.min(delta, workQueue.size());
             *         while (k-- > 0 && addWorker(null, true)) {
             *             if (workQueue.isEmpty())
             *                 break;
             *         }
             *     }
             * }
             */

            /**
             * JDK1.6 ThreadPoolExecutor#setCorePoolSize
             * public void setCorePoolSize(int corePoolSize) {
             *         if (corePoolSize < 0)
             *             throw new IllegalArgumentException();
             *         final ReentrantLock mainLock = this.mainLock;
             *         mainLock.lock();
             *         try {
             *             int extra = this.corePoolSize - corePoolSize;
             *             this.corePoolSize = corePoolSize;
             *             if (extra < 0) {
             *                 //如果核心线程数量扩充，且任务队列中有足够的任务，那么增加工作线程数量(到核心线程数量)。
             *                 int n = workQueue.size(); // don't add more threads than tasks
             *                 while (extra++ < 0 && n-- > 0 && poolSize < corePoolSize) {
             *                     Thread t = addThread(null);
             *                     if (t == null)
             *                         break;
             *                 }
             *             }
             *             else if (extra > 0 && poolSize > corePoolSize) {
             *                 //如果核心线程数量缩减，且任务队列中没有任务，且当前线程数量大于核心线程数量，那么移除一些工作线程(到核心线程数量)。
             *                 try {
             *                     Iterator<Worker> it = workers.iterator();
             *                     while (it.hasNext() &&
             *                            extra-- > 0 &&
             *                            poolSize > corePoolSize &&
             *                            workQueue.remainingCapacity() == 0)
             *                         it.next().interruptIfIdle();
             *                 } catch (SecurityException ignore) {
             *                     // Not an error; it is OK if the threads stay live
             *                 }
             *             }
             *         } finally {
             *             mainLock.unlock();
             *         }
             *     }
             *
             * ————————————————
             * 版权声明：本文为CSDN博主「iteye_11160」的原创文章，遵循 CC 4.0 BY-SA 版权协议，转载请附上原文出处链接及本声明。
             * 原文链接：https://blog.csdn.net/iteye_11160/article/details/82642575
             */
            taskQueue.setForcedRemainingCapacity(Integer.valueOf(0));
        }

        // setCorePoolSize(0) wakes idle threads
        this.setCorePoolSize(0);

        // setCorePoolSize(0)，会调用interruptIdleWorkers()；最终会循环所有works，调用t.interrupt();
        // TODO 调用当前线程t.interrupt()作用；
        // 调用interrupt方法后，要看看线程的run方法中有没有处理中断的代码，可以是Thread.isInterrupted判断，也可以是捕获InterruptedException
        // 查看Worker的代码，可以看到并没有这些处理，但是代码中有task.run(),因此要去看task.run()，这里的task按照之前的分析，是SocketProcessorBase；
        // 见代码：AbstractEndpoint#processSocket；接着去找SocketProcessorBase实现类：NioEndpoint内部类SocketProcessor#doRun，确实捕获了Throwable

        /**
         * JDK，Thread#interrupt()注释，可以看到如果线程阻塞在IO上是会抛异常的
         * If this thread is blocked in an I/O operation upon an {@link
         *  java.nio.channels.InterruptibleChannel InterruptibleChannel}
         *  then the channel will be closed, the thread's interrupt
         *  status will be set, and the thread will receive a {@link
         *  java.nio.channels.ClosedByInterruptException}.
         *
         */

        // TaskQueue.take() takes care of timing out, so that we are sure that
        // all threads of the pool are renewed in a limited time, something like
        // (threadKeepAlive + longest request time)

        if (taskQueue != null) {
            // ok, restore the state of the queue and pool
            taskQueue.setForcedRemainingCapacity(null);
        }
        this.setCorePoolSize(savedCorePoolSize);
    }
    // 拒绝策略，直接抛异常
    private static class RejectHandler implements RejectedExecutionHandler {
        @Override
        public void rejectedExecution(Runnable r,
                java.util.concurrent.ThreadPoolExecutor executor) {
            throw new RejectedExecutionException();
        }

    }


}

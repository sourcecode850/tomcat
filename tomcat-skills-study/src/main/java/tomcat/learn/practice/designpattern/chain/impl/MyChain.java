package tomcat.learn.practice.designpattern.chain.impl;

import tomcat.learn.practice.designpattern.chain.IMyChain;
import tomcat.learn.practice.designpattern.chain.IMyChainNode;

/**
 * @date:2019/10/26 22:16
 **/
public class MyChain implements IMyChain {

    private int pos = 0;
    private int n = 0;
    public static final int INCREMENT = 10;

    private MyChainNodeTail myChainTail;

    public void setMyChainTail(MyChainNodeTail myChainTail) {
        this.myChainTail = myChainTail;
    }

    private IMyChainNode[] chainNodes = new IMyChainNode[0];

    public void addMyChainNode(IMyChainNode iMyChainNode) {
        // Prevent the same node being added multiple times
        for (IMyChainNode node : chainNodes) {
            if (iMyChainNode == node) {
                return;
            }
        }
        //不要一直arraycopy，没加10个node之后copy一次；自动扩容还可以这么写了
        if (n == chainNodes.length) {
            IMyChainNode[] newNodes =
                    new IMyChainNode[n + INCREMENT];
            System.arraycopy(chainNodes, 0, newNodes, 0, n);
            chainNodes = newNodes;
        }
        chainNodes[n++] = iMyChainNode;
    }

    @Override
    public void doChain() {
        if (pos < n) {
            IMyChainNode node = chainNodes[pos++];
            node.doChainNode(this);
            //记得退出，不然myChainTail.doTail()会被执行多次
            return;
        }
        myChainTail.doTail();
    }
}

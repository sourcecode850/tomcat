package tomcat.learn.practice.designpattern.chain.impl;

import lombok.extern.slf4j.Slf4j;
import tomcat.learn.practice.designpattern.chain.IMyChain;
import tomcat.learn.practice.designpattern.chain.IMyChainNode;

/**
 * @date:2019/10/26 22:52
 **/
@Slf4j
public class MyChainNode2 implements IMyChainNode {
    @Override
    public void doChainNode(IMyChain iMyChain) {
        log.info("MyChainNode2222==============doChainNode=============before");
        // 这里记得doChain()否则，链为不会被执行，也就是链到当前节点就退出了
        iMyChain.doChain();
        log.info("MyChainNode2222==============doChainNode=============after");
    }
}

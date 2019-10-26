package tomcat.learn.practice.designpattern.chain.impl;

import lombok.extern.slf4j.Slf4j;
import tomcat.learn.practice.designpattern.chain.IMyChain;
import tomcat.learn.practice.designpattern.chain.IMyChainNode;

/**
 * @date:2019/10/26 22:52
 **/
@Slf4j
public class MyChainNode1 implements IMyChainNode {
    @Override
    public void doChainNode(IMyChain iMyChain) {
        log.info("MyChainNode1111==============doChainNode=============before");
        iMyChain.doChain();
        log.info("MyChainNode1111==============doChainNode=============after");
    }
}

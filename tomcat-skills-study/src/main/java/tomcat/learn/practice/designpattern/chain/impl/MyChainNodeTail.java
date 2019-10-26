package tomcat.learn.practice.designpattern.chain.impl;

import lombok.extern.slf4j.Slf4j;
import tomcat.learn.practice.designpattern.chain.IMyChainNodeTail;

/**
 * @date:2019/10/26 22:18
 **/
@Slf4j
public class MyChainNodeTail implements IMyChainNodeTail {

    @Override
    public Object doTail() {
        log.info("===========tail()" + this.getClass());
        return null;
    }
}

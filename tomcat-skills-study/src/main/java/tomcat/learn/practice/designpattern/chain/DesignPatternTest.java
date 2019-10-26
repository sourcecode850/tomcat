
package tomcat.learn.practice.designpattern.chain;

import org.junit.Test;
import tomcat.learn.practice.designpattern.chain.impl.MyChain;
import tomcat.learn.practice.designpattern.chain.impl.MyChainNode1;
import tomcat.learn.practice.designpattern.chain.impl.MyChainNode2;
import tomcat.learn.practice.designpattern.chain.impl.MyChainNodeTail;

/**
 * @date:2019/10/26 22:51
 **/
public class DesignPatternTest {

    @Test
    public void testChain() {

        IMyChainNode myChainNode1 = new MyChainNode1();
        IMyChainNode myChainNode2 = new MyChainNode2();

        MyChain myChain = new MyChain();
        myChain.addMyChainNode(myChainNode1);
        myChain.addMyChainNode(myChainNode2);
        myChain.setMyChainTail(new MyChainNodeTail());
        myChain.doChain();
    }
}

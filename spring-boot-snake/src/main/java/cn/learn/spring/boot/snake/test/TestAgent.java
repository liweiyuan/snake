package cn.learn.spring.boot.snake.test;

/**
 * @Author :lwy
 * @Date : 2018/10/25 18:24
 * @Description :
 */
public class TestAgent {

    public static void main(String[] args) throws InterruptedException {
        Thread.sleep(1000 * 10);
        A a = new A();

        TestAgent agent = new TestAgent();
        String result = agent.say(a);
        System.out.println(result);

        //Thread.sleep(1000 * 20);
        System.exit(1);
    }

    private String say(A a) {
        String say = a.say();
        return say;
    }

}

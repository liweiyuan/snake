package cn.learn.spring.boot.snake.test;

/**
 * @Author :lwy
 * @Date : 2018/10/25 18:24
 * @Description :
 */
public class TestAgent {

    public static void main(String[] args) throws InterruptedException {
        Thread.sleep(1000 * 20);
        A a = new A();
        String result = a.say();
        System.out.println(result);

        Thread.sleep(1000 * 20);
        System.exit(0);
    }

    private static class A {

        public String say() {
            return "A";
        }
    }
}

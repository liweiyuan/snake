package com.learn.snake.plugin.interceptor;

import com.learn.snake.plugin.handler.HandlerLoader;
import com.learn.snake.plugin.handler.IHandler;
import net.bytebuddy.asm.Advice;

/**
 * @Author :lwy
 * @Date : 2018/10/24 16:05
 * @Description :
 * <p>
 * 注意：实例方法使用@Advice.This注解，静态方法使用@Advice.Origin 两者不能混用
 */
public class ProcessAdvice {

    private static final String PROCESS_HANDLER = "com.learn.snake.plugin.handler.ProcessHandler";

    @Advice.OnMethodEnter
    public static void enter(@Advice.Local("handler") IHandler handler,
                             @Advice.Origin("#t") String className,
                             @Advice.Origin("#m") String methName,
                             @Advice.AllArguments Object[] arguments) {
        //构建handler
        handler = HandlerLoader.loadHandler(PROCESS_HANDLER);
        handler.before(className, methName, arguments, null);
    }

    /**
     * 如果需要返回值，在方法里添加注解和参数@Advice.Return(readOnly = false) Object result,result的类型要和实际返回值类型一致,需要修改参数readOnly置为false
     */
    @Advice.OnMethodExit(onThrowable = Throwable.class)
    public static void exit(@Advice.Local("handler") IHandler handler,
                            @Advice.Origin("#t") String className,
                            @Advice.Origin("#m") String methName,
                            @Advice.AllArguments Object[] arguments,
                            @Advice.Thrown Throwable throwable){
        handler.after(className,methName,arguments,null,throwable,null);
    }
}

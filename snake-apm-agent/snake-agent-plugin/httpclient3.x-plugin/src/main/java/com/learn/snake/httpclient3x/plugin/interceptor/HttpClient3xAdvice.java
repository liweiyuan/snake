package com.learn.snake.httpclient3x.plugin.interceptor;

import com.learn.snake.plugin.handler.HandlerLoader;
import com.learn.snake.plugin.handler.IHandler;
import net.bytebuddy.asm.Advice;

/**
 * @Author :lwy
 * @Date : 2018/10/29 15:46
 * @Description :
 */
public class HttpClient3xAdvice {

    @Advice.OnMethodEnter
    public static void enter(@Advice.Local("handler") IHandler handler,
                             @Advice.Origin("#t") String className,
                             @Advice.Origin("#m") String methodName,
                             @Advice.AllArguments Object[] arguments) {
        handler = HandlerLoader.loadHandler("com.learn.snake.httpclient3x.plugin.handler.HttpClient3xHandler");
        handler.before(className, methodName, arguments, null);
    }
}

package com.learn.snake.servlet.plugin.interceptor;

import com.learn.snake.model.Span;
import com.learn.snake.plugin.handler.HandlerLoader;
import com.learn.snake.plugin.handler.IHandler;
import net.bytebuddy.asm.Advice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author :lwy
 * @Date : 2018/10/30 14:32
 * @Description :
 */
public class ServletAdvice {

    @Advice.OnMethodEnter
    public static void enter(@Advice.Local("handler") IHandler handler,
                             @Advice.Origin("#t") String className,
                             @Advice.Origin("#m") String methodName,
                             @Advice.Argument(value = 0) HttpServletRequest request,
                             @Advice.Argument(value = 1, readOnly = false) HttpServletResponse response) {
        handler = HandlerLoader.loadHandler("com.learn.snake.servlet.plugin.handler.ServletHandler");
        Span span = handler.before(className, methodName, new Object[]{request, response}, null);
    }

}

package com.learn.snake.common;

import com.learn.snake.model.Span;

import java.util.Stack;

/**
 * @Author :lwy
 * @Date : 2018/10/25 11:10
 * @Description : 请求上下文保存
 */
public class SpanManager {

    private static final ThreadLocal<Stack<Span>> threadLocalSpan = new ThreadLocal<>();


    /**
     * 创建Span
     *
     * @param spanType
     * @return
     */
    private static Span createSpan(String spanType) {

        Stack<Span> spanStack = threadLocalSpan.get();
        if (spanStack == null) {
            spanStack = new Stack<>();
            //不存在就添加一个空的
            threadLocalSpan.set(spanStack);
        }
        String pId, gId;
        if (spanStack.empty()) {

            //获取pid
            pId = SnakeTraceContext.getPId();
            if (pId == null) {
                pId = "nvl";
                SnakeTraceContext.setPId(pId);
            }
            //gid
            gId = SnakeTraceContext.getGId();
            if (gId == null) {
                gId = IdHelper.getId();
                SnakeTraceContext.setGid(gId);
            }
        } else {
            Span span = spanStack.peek();
            pId = span.getId();
            gId = span.getGid();
            SnakeTraceContext.setPId(pId);
        }

        Span resultSpan = new Span(spanType);
        resultSpan.setId(IdHelper.getId()).setPid(pId).setGid(gId);
        return resultSpan;
    }


    //1.
    /**
     * 创建制定类型的埋点
     *
     * @param spanType
     * @return 创建入口span，并放入堆栈中，用于Advice::enter或Handler::before开始的span创建，需要在Advice::exit或Handler::after处调用getExitSpan
     * </br> createEntrySpan和getExitSpan它们是一对的，要配合一起使用，谁也不能缺了谁，不能单独使用。
     */
    public static Span createEntrySpan(String spanType) {
        Span span = createSpan(spanType);
        Stack<Span> spanStack = threadLocalSpan.get();
        spanStack.push(span);
        return span;
    }


    //2.
    /**
     * 获取当前的span，必须在createEntrySpan之后getExitSpan之前。
     *
     * @return
     */
    public static Span getCurrentSpan() {
        Stack<Span> stack = threadLocalSpan.get();
        if (stack == null || stack.isEmpty()) {
            return null;
        }
        return stack.peek();
    }

    //3.
    /**
     * 获取出口span，并出堆栈，用于Advice::exit或Handler::after处，事先需要在Advice::enter或Handler::before开始时调用createEntrySpan创建入口span
     * </br> createEntrySpan和getExitSpan它们是一对的，要配合一起使用，谁也不能缺了谁，不能单独使用。
     * @param
     * @return
     */
    public static Span getExitSpan() {

        Stack<Span> spanStack=threadLocalSpan.get();
        if(spanStack==null|| spanStack.empty()){
            //清出所有的当前线程的数据
            SnakeTraceContext.clearAll();
            return null;
        }
        return  spanStack.pop();

    }
}

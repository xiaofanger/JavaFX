package com.fanger.spring.scope

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.springframework.beans.factory.ObjectFactory
import org.springframework.beans.factory.config.Scope
import org.springframework.context.support.SimpleThreadScope

/**
 * 自定义Scope的实现
 */
class ProjectScope implements Scope {

    private static final Log logger = LogFactory.getLog(ProjectScope.class);

    /**
     * 从基础范围返回对象。
     * 例如，会话范围实现返回会话范围的bean（如果它不存在，则该方法在将其绑定到会话以供将来参考之后返回该bean的新实例）。
     *
     * @param name
     * @param objectFactory
     * @return
     */
    @Override
    Object get(String name, ObjectFactory<?> objectFactory) {
        new SimpleThreadScope()
        return null
    }

    /**
     * 从基础范围中删除对象。
     * 例如，会话范围实现从基础会话中删除会话范围的bean。应返回该对象，但如果找不到具有指定名称的对象，则可以返回null。
     *
     * @param name
     * @return
     */
    @Override
    Object remove(String name) {
        return null
    }

    /**
     * 注册范围应在销毁时或在范围中指定的对象被销毁时应执行的回调。
     * 有关销毁回调的更多信息，请参阅javadocs或Spring作用域实现。
     *
     * @param name
     * @param callback
     */
    @Override
    void registerDestructionCallback(String name, Runnable callback) {

    }

    /**
     * 解析给定键的上下文对象（如果有）。
     * *例如 key “request”的HttpServletRequest对象。
     *
     * @param key
     * @return
     */
    @Override
    Object resolveContextualObject(String key) {
        return null
    }

    /**
     * 以下方法获取基础范围的对话标识符。每个范围的标识符都不同。
     * 对于会话范围实现，此标识符可以是会话标识符。
     *
     * @return
     */
    @Override
    String getConversationId() {
        return null
    }
}

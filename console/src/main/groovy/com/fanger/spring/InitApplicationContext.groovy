package com.fanger.spring

import com.fanger.config.AppConfig
import org.springframework.context.annotation.AnnotationConfigApplicationContext

/**
 * 使用单例模式。注入Spring容器
 */
class InitApplicationContext {

    private volatile static AnnotationConfigApplicationContext ctx

    private InitApplicationContext() {}

    static AnnotationConfigApplicationContext createInstance() {
        if (ctx == null) {
            synchronized (AnnotationConfigApplicationContext.class) {
                if (ctx == null) {
                    ctx = new AnnotationConfigApplicationContext(AppConfig.class)
                }
            }
        }
        return ctx
    }

}

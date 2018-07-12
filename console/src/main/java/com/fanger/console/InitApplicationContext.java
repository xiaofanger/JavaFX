package com.fanger.console;

import com.fanger.config.AppConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * 注入Spring容器
 */
public class InitApplicationContext {

    private static AnnotationConfigApplicationContext ctx =
            new AnnotationConfigApplicationContext(AppConfig.class);

    private InitApplicationContext() {}

    public static AnnotationConfigApplicationContext createInstance() {
        return ctx;
    }

}

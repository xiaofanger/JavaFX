package com.fanger.config;

import com.fanger.console.AppStart;
import com.fanger.console.MainController;
import com.fanger.service.ConsoleService;
import com.google.common.eventbus.EventBus;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public AppStart main() {
        return new AppStart();
    }

    @Bean
    public MainController mainController() {
        return new MainController();
    }

    @Bean
    public ConsoleService consoleService() {
        return new ConsoleService();
    }

//    @Bean
//    public DefaultListableBeanFactory beanFactory() {
//        return new DefaultListableBeanFactory();
//    }

    @Bean
    public EventBus eventBus() {
        return new EventBus();
    }

    class BeanFactory implements BeanFactoryAware {
        @Override
        public void setBeanFactory(org.springframework.beans.factory.BeanFactory beanFactory) throws BeansException {

        }
    }
}

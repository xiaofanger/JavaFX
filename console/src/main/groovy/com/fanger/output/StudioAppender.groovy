package com.fanger.output

import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.AppenderBase
import com.fanger.event.LogMessageEvent
import com.fanger.spring.InitApplicationContext
import com.google.common.eventbus.EventBus
import org.springframework.context.annotation.AnnotationConfigApplicationContext

class StudioAppender extends AppenderBase<ILoggingEvent> {
    @Override
    protected void append(ILoggingEvent eventObject) {
        def msg = "${eventObject.level} ${eventObject.timeStamp} " +
                "${eventObject.threadName} ${eventObject.formattedMessage}"
        AnnotationConfigApplicationContext ac2 =  InitApplicationContext.createInstance()
        EventBus eventBus = ac2.getBean(EventBus.class)
        eventBus.post(new LogMessageEvent(msg.toString()))
    }
}

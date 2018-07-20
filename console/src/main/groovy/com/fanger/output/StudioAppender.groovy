package com.fanger.output

import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.AppenderBase
import com.fanger.jms.LogMessageProducer

class StudioAppender extends AppenderBase<ILoggingEvent> {
    @Override
    protected void append(ILoggingEvent eventObject) {
        LogMessageProducer.send(
                "${eventObject.level} ${eventObject.timeStamp} ${eventObject.threadName} ${eventObject.formattedMessage}"
        )
    }
}

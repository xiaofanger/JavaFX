package com.fanger.event;

/**
 * 日志消息事件
 */
public class LogMessageEvent extends AbstractApplicationEvent {

    private String message;

    LogMessageEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

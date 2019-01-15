package org.han.ica.asd.c.agent;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

class TestExceptionLogHandler extends Handler {
    Level level;
    String message;

    @Override
    public void publish(LogRecord record) {
        level = record.getLevel();
        message = record.getMessage();
    }

    @Override
    public void flush() {
    }

    @Override
    public void close() throws SecurityException {
    }
}

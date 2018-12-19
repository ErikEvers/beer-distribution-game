package org.han.ica.asd.c.messagehandler.messagetypes;

import java.io.Serializable;

public class ResponseMessage implements Serializable {
    private Exception exception;
    private boolean isSuccess;
    private Object responseObject;

    public ResponseMessage(boolean isSuccess, Exception exception) {
        this.exception = exception;
        this.isSuccess = isSuccess;
    }

    public ResponseMessage(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public ResponseMessage(Object object) {
        this.responseObject = object;
        this.isSuccess = true;
    }

    public boolean getIsSuccess() {
        return isSuccess;
    }

    public Exception getException() {
        return exception;
    }
}

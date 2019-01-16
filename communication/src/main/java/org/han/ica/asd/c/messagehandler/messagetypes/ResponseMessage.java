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

    /**
     * Gets responseObject.
     *
     * @return Value of responseObject.
     */
    public Object getResponseObject() {
        return responseObject;
    }

    /**
     * Gets exception.
     *
     * @return Value of exception.
     */
    public Exception getException() {
        return exception;
    }

    /**
     * Gets isSuccess.
     *
     * @return Value of isSuccess.
     */
    public boolean getIsSuccess() {
        return isSuccess;
    }
}

package com.helpapplication.events;

import com.helpapplication.http.model.Result;

/**
 * 업로드 데이터.
 */

public class UploadCompletedEvent {
    private boolean isSuccessful;
    private int code;
    private String errorMessage;

    public UploadCompletedEvent(boolean isSuccessful, int code, Result result) {
        this.isSuccessful = isSuccessful;
        this.code = code;
        errorMessage = result != null ? result.getMessage() : null;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public void setSuccessful(boolean successful) {
        isSuccessful = successful;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "UploadCompletedEvent{" +
                "isSuccessful=" + isSuccessful +
                ", code=" + code +
                '}';
    }
}

package com.helpapplication.http.model;

/**
 * Created by massivcode on 2017-02-25.
 */

public class Result {
    private String message;

    public Result(String message) {
        this.message = message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "Result{" +
                "message='" + message + '\'' +
                '}';
    }
}

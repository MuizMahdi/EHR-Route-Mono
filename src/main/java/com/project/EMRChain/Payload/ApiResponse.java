package com.project.EMRChain.Payload;

public class ApiResponse
{
    private Boolean success;
    private String message;

    public ApiResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
    public Boolean getSuccess() {
        return success;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public void setSuccess(Boolean success) {
        this.success = success;
    }
}

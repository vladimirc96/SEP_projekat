package com.sep.bankservice.dto;

public class ApproveDTO {

    private long id;
    private String password;
    private long methodId;

    public ApproveDTO() {
    }

    public ApproveDTO(long id, String password, long methodId) {
        this.id = id;
        this.password = password;
        this.methodId = methodId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getMethodId() {
        return methodId;
    }

    public void setMethodId(long methodId) {
        this.methodId = methodId;
    }
}

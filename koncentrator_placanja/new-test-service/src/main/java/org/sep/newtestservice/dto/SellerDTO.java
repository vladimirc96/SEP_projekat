package org.sep.newtestservice.dto;


import org.sep.newtestservice.model.Seller;

public class SellerDTO {

    private long id;
    private String password;
    private String someToken;

    public SellerDTO() {
    }

    public static SellerDTO formDto(Seller s) {
        return new SellerDTO(s.getId(), "", "");
    }

    public SellerDTO(long id, String password, String someToken) {
        this.id = id;
        this.password = password;
        this.someToken = someToken;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSomeToken() {
        return someToken;
    }

    public void setSomeToken(String someToken) {
        this.someToken = someToken;
    }


}
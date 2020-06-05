package com.example.qa_hub;

public class Users {
  public   String name,mail,phone,field;

    public Users(){

    }
    public Users(String name, String mail, String phone, String field) {
        this.name = name;
        this.mail = mail;
        this.phone = phone;
        this.field = field;
    }

    public String getField() {
        return field;
    }
}

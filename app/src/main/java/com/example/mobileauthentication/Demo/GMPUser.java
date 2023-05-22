package com.example.mobileauthentication.Demo;

public class GMPUser {
    private String Name;
    private String Mobile;
    private String Picture;
    private String Role;
    private String BirthDate;
    private String Uid;

    public GMPUser(String name, String mobile, String role, String uid) {
        Name = name;
        Mobile = mobile;
        Role = role;
        Uid = uid;
    }

    public GMPUser(){}

    public GMPUser(String Name, String Mobile, String Picture, String Role, String BirthDate, String Uid) {
        this.Name = Name;
        this.Mobile = Mobile;
        this.Picture = Picture;
        this.Role = Role;
        this.BirthDate = BirthDate;
        this.Uid = Uid;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String Mobile) {
        this.Mobile = Mobile;
    }

    public String getPicture() {
        return Picture;
    }

    public void setPicture(String image) {
        this.Picture = Picture;
    }

    public String getRole() {
        return Role;
    }

    public void setRole(String role) {
        this.Role = Role;
    }

    public String getBirthDate() {
        return BirthDate;
    }

    public void setBirthDate(String birth) {
        this.BirthDate = BirthDate;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }
}

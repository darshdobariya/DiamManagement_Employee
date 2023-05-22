package com.example.mobileauthentication.Demo;

public class GMP {
    private String endDate;
    private String less;
    private String ready;
    private String startDate;
    private String status;

    public GMP(String endDate, String less, String ready, String startDate, String status) {
        this.endDate = endDate;
        this.less = less;
        this.ready = ready;
        this.startDate = startDate;
        this.status = status;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getLess() {
        return less;
    }

    public void setLess(String less) {
        this.less = less;
    }

    public String getReady() {
        return ready;
    }

    public void setReady(String ready) {
        this.ready = ready;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

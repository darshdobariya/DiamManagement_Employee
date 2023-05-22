package com.example.mobileauthentication.Demo;

public class DaimProcess {
    private String DaimNo;
    private String EndDate;
    private String ExpectedWeight;
    private String Kapan;
    private String manuEndDate;
    private String manuLess;
    private String manuReady;
    private String manuStartDate;
    private String manuStatus;
    private String manuMobile;
    private String manuName;
    private String manuUid;
    private String Ready;
    private String ReadyWeight;
    private String RuffWeight;
    private String StartDate;
    private String Status;
    private String Total;
    private String SendTo;

    public DaimProcess(String DaimNo, String StartDate, String RuffWeight, String ReadyWeight, String Status, String SendTo){
        this.DaimNo = DaimNo;
        this.StartDate = StartDate;
        this.RuffWeight = RuffWeight;
        this.ReadyWeight = ReadyWeight;
        this.Status = Status;
        this.SendTo = SendTo;
    }

    public DaimProcess(String endDate, String expectedWeight, String ready, String readyWeight, String ruffWeight, String startDate, String status, String total, String kapan, String daimNo){
        EndDate = endDate;
        ExpectedWeight = expectedWeight;
        Ready = ready;
        ReadyWeight = readyWeight;
        RuffWeight = ruffWeight;
        StartDate = startDate;
        Status = status;
        Total = total;
        Kapan = kapan;
        DaimNo = daimNo;
    }

    public DaimProcess() {
    }

    public DaimProcess(String kapan, String ready, String totaldaim) {
        this.Kapan = kapan;
        this.Ready = ready;
        this.totaldaim = totaldaim;
    }

    public String getSendTo() {
        return SendTo;
    }

    public void setSendTo(String sendTo) {
        SendTo = sendTo;
    }

    public String getManuLess() {
        return manuLess;
    }

    public void setManuLess(String manuLess) {
        this.manuLess = manuLess;
    }

    public String getManuReady() {
        return manuReady;
    }

    public void setManuReady(String manuReady) {
        this.manuReady = manuReady;
    }

    public String getManuStartDate() {
        return manuStartDate;
    }

    public void setManuStartDate(String manuStartDate) {
        this.manuStartDate = manuStartDate;
    }

    public String getManuStatus() {
        return manuStatus;
    }

    public void setManuStatus(String manuStatus) {
        this.manuStatus = manuStatus;
    }

    public String getManuMobile() {
        return manuMobile;
    }

    public void setManuMobile(String manuMobile) {
        this.manuMobile = manuMobile;
    }

    public String getManuName() {
        return manuName;
    }

    public void setManuName(String manuName) {
        this.manuName = manuName;
    }

    public String getManuUid() {
        return manuUid;
    }

    public void setManuUid(String manuUid) {
        this.manuUid = manuUid;
    }

    public String getTotaldaim() {
        return totaldaim;
    }

    public void setTotaldaim(String totaldaim) {
        this.totaldaim = totaldaim;
    }

    private String totaldaim;

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

    public String getExpectedWeight() {
        return ExpectedWeight;
    }

    public void setExpectedWeight(String expectedWeight) {
        ExpectedWeight = expectedWeight;
    }

    public String getReady() {
        return Ready;
    }

    public void setReady(String ready) {
        Ready = ready;
    }

    public String getReadyWeight() {
        return ReadyWeight;
    }

    public void setReadyWeight(String readyWeight) {
        ReadyWeight = readyWeight;
    }

    public String getRuffWeight() {
        return RuffWeight;
    }

    public void setRuffWeight(String ruffWeight) {
        RuffWeight = ruffWeight;
    }

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getTotal() {
        return Total;
    }

    public void setTotal(String total) {
        Total = total;
    }

    public String getKapan() {
        return Kapan;
    }

    public void setKapan(String kapan) {
        Kapan = kapan;
    }

    public String getDaimNo() {
        return DaimNo;
    }

    public void setDaimNo(String daimNo) {
        DaimNo = daimNo;
    }

    public String getManuEndDate() {
        return manuEndDate;
    }

    public void setManuEndDate(String manuEndDate) {
        this.manuEndDate = manuEndDate;
    }
}

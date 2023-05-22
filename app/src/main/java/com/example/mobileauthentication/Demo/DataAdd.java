package com.example.mobileauthentication.Demo;

public class DataAdd {

    private String expanseName;
    private String expansePrice;
    private String datePicker;
    private String expanseNote;

    public DataAdd(String expanseName1, String expansePrice1, String datePicker1, String expanseNote1){

    }

    public DataAdd(String expanseName, String expansePrice, String datePicker) {
        this.expanseName = expanseName;
        this.expansePrice = expansePrice;
        this.datePicker = datePicker;
        this.expanseNote = expanseNote;
    }

    public String getExpanseName() {
        return expanseName;
    }

    public void setExpanseName(String expanseName) {
        this.expanseName = expanseName;
    }

    public String getExpansePrice() {
        return expansePrice;
    }

    public void setExpansePrice(String expansePrice) {
        this.expansePrice = expansePrice;
    }

    public String getDatePicker() {
        return datePicker;
    }

    public void setDatePicker(String datePicker) {
        this.datePicker = datePicker;
    }

    public String getExpanseNote() {
        return expanseNote;
    }

    public void setExpanseNote(String expanseNote) {
        this.expanseNote = expanseNote;
    }
}

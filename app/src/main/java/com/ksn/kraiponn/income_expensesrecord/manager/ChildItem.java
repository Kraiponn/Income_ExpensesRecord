package com.ksn.kraiponn.income_expensesrecord.manager;

public class ChildItem {
    private int _id;
    private int iconID;
    private String title;
    private String amount;

    public ChildItem(int _id, int iconID, String title, String amount) {
        this._id = _id;
        this.iconID = iconID;
        this.title = title;
        this.amount = amount;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getIconID() {
        return iconID;
    }

    public void setIconID(int iconID) {
        this.iconID = iconID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

}

package com.stack.wotr.model;

public class Item {
    String mTitle;
    boolean isCheck;

    public Item(String mTitle, boolean isCheck) {
        this.mTitle = mTitle;
        this.isCheck = isCheck;
    }

    public Item(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}

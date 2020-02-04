package com.stack.wotr.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Data implements Parcelable {
    int mDate;
    String mMonth;
    int mYear;
    String mPeriod;
    String mArea;

    public Data(int mDate, String mMonth, int mYear, String mPeriod) {
        this.mDate = mDate;
        this.mMonth = mMonth;
        this.mYear = mYear;
        this.mPeriod = mPeriod;
    }

    public int getDate() {
        return mDate;
    }

    public void setDate(int mDate) {
        this.mDate = mDate;
    }

    public String getMonth() {
        return mMonth;
    }

    public void setMonth(String mMonth) {
        this.mMonth = mMonth;
    }

    public int getYear() {
        return mYear;
    }

    public void setYear(int mYear) {
        this.mYear = mYear;
    }

    public String getPeriod() {
        return mPeriod;
    }

    public void setPeriod(String mPeriod) {
        this.mPeriod = mPeriod;
    }

    public String getArea() {
        return mArea;
    }

    public void setArea(String mArea) {
        this.mArea = mArea;
    }

    public static Creator<Data> getCREATOR() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mDate);
        dest.writeString(this.mMonth);
        dest.writeInt(this.mYear);
        dest.writeString(this.mPeriod);
        dest.writeString(this.mArea);
    }

    public Data() {
    }

    protected Data(Parcel in) {
        this.mDate = in.readInt();
        this.mMonth = in.readString();
        this.mYear = in.readInt();
        this.mPeriod = in.readString();
        this.mArea = in.readString();
    }

    public static final Creator<Data> CREATOR = new Creator<Data>() {
        @Override
        public Data createFromParcel(Parcel source) {
            return new Data(source);
        }

        @Override
        public Data[] newArray(int size) {
            return new Data[size];
        }
    };
}

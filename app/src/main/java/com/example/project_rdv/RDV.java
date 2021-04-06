package com.example.project_rdv;

import android.os.Parcel;
import android.os.Parcelable;

public class RDV implements Parcelable {
    private Long id;
    private String title;
    private String description;
    private String date;
    private String time;
    private String address;
    private String phone;
    private Boolean state;


    public RDV(){}

    public RDV(long id){
        this.setId(id);
    }

    public RDV(String title,String description,String date,String time,String address, String phone, Boolean state){
        this.setTitle(title);
        this.setDescription(description);
        this.setDate(date);
        this.setTime(time);
        this.setAddress(address);
        this.setPhone(phone);
        this.setState(state);
    }

    public RDV(long id,String title,String description,String date,String time,String address, String phone, Boolean state){
        this.setId(id);
        this.setTitle(title);
        this.setDescription(description);
        this.setDate(date);
        this.setTime(time);
        this.setAddress(address);
        this.setPhone(phone);
        this.setState(state);
    }

    public RDV(Parcel in) {
        id = in.readLong();
        title = in.readString();
        date = in.readString();
        time = in.readString();
        address = in.readString();
        phone = in.readString();
        description = in.readString();
        state = in.readBoolean();
    }


    public static final Creator<RDV> CREATOR = new Creator<RDV>() {
        @Override
        public RDV createFromParcel(Parcel in) {
            return new RDV(in);
        }

        @Override
        public RDV[] newArray(int size) {
            return new RDV[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(getId());
        dest.writeString(getTitle());
        dest.writeString(getDate());
        dest.writeString(getTime());
        dest.writeString(getAddress());
        dest.writeString(getPhone());
        dest.writeString(getDescription());
        dest.writeBoolean(getState());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }
}

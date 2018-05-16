package com.diplomski.data.api.models.response;


import android.os.Parcel;
import android.os.Parcelable;

public class LoginApiResponse implements Parcelable {

    public int id;
    public String ime;
    public String prezime;
    public String adresa;
    public String username;
    public String password;
    public int isAdmin;

    public LoginApiResponse(int id, String ime, String prezime, String adresa, String username, String password, int isAdmin) {
        this.id = id;
        this.ime = ime;
        this.prezime = prezime;
        this.adresa = adresa;
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.ime);
        dest.writeString(this.prezime);
        dest.writeString(this.adresa);
        dest.writeString(this.username);
        dest.writeString(this.password);
        dest.writeInt(this.isAdmin);
    }

    protected LoginApiResponse(Parcel in) {
        this.id = in.readInt();
        this.ime = in.readString();
        this.prezime = in.readString();
        this.adresa = in.readString();
        this.username = in.readString();
        this.password = in.readString();
        this.isAdmin = in.readInt();
    }

    public static final Creator<LoginApiResponse> CREATOR = new Creator<LoginApiResponse>() {
        @Override
        public LoginApiResponse createFromParcel(Parcel source) {
            return new LoginApiResponse(source);
        }

        @Override
        public LoginApiResponse[] newArray(int size) {
            return new LoginApiResponse[size];
        }
    };
}

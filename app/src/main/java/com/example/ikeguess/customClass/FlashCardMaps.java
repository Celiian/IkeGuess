package com.example.ikeguess.customClass;

import android.os.Parcel;
import android.os.Parcelable;

public class FlashCardMaps implements Parcelable {

    public Double latAnswer;

    public Double lgtAnswer;

    public String imageUrl;

    public String answerName;

    public String type;

    public int radius;

    public FlashCardMaps(Double latAnswer, Double lgtAnswer, String imageUrl, String answerName, String type, int radius) {
        this.latAnswer = latAnswer;
        this.lgtAnswer = lgtAnswer;
        this.imageUrl = imageUrl;
        this.answerName = answerName;
        this.type = type;
        this.radius = radius;
    }

    protected FlashCardMaps(Parcel in) {
        if (in.readByte() == 0) {
            latAnswer = null;
        } else {
            latAnswer = in.readDouble();
        }
        if (in.readByte() == 0) {
            lgtAnswer = null;
        } else {
            lgtAnswer = in.readDouble();
        }
        imageUrl = in.readString();
        answerName = in.readString();
        radius = in.readInt();
        type = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (latAnswer == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(latAnswer);
        }
        if (lgtAnswer == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(lgtAnswer);
        }
        dest.writeString(imageUrl);
        dest.writeString(answerName);
        dest.writeInt(radius);
        dest.writeString(type);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FlashCardMaps> CREATOR = new Creator<FlashCardMaps>() {
        @Override
        public FlashCardMaps createFromParcel(Parcel in) {
            return new FlashCardMaps(in);
        }

        @Override
        public FlashCardMaps[] newArray(int size) {
            return new FlashCardMaps[size];
        }
    };
}

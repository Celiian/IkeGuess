package com.example.ikeguess.customClass;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class FlashCard implements Parcelable {
    public String question;
    public String mediaUrl;
    public String mediaType;
    public String goodAnswer;
    public ArrayList<String> answers;
    public int numberOfQuestions;

    public FlashCard(String question, String imageUrl, String goodAnswer, ArrayList<String> answers, int numberOfQuestions, String mediaType) {
        this.question = question;
        this.mediaUrl = imageUrl;
        this.goodAnswer = goodAnswer;
        this.answers = answers;
        this.numberOfQuestions = numberOfQuestions;
        this.mediaType = mediaType;
    }

    protected FlashCard(Parcel in) {
        question = in.readString();
        mediaUrl = in.readString();
        mediaType = in.readString();
        goodAnswer = in.readString();
        answers = in.createStringArrayList();
        numberOfQuestions = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(question);
        dest.writeString(mediaUrl);
        dest.writeString(mediaType);
        dest.writeString(goodAnswer);
        dest.writeStringList(answers);
        dest.writeInt(numberOfQuestions);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FlashCard> CREATOR = new Creator<FlashCard>() {
        @Override
        public FlashCard createFromParcel(Parcel in) {
            return new FlashCard(in);
        }

        @Override
        public FlashCard[] newArray(int size) {
            return new FlashCard[size];
        }
    };
}

package com.example.abhisheknair.hchat;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by abhisheknair on 21/05/16.
 */
public class ChatResponse implements Parcelable{

    @SerializedName("count")
    public int count = 0;
    @SerializedName("messages")
    public ArrayList<ChatMessage> messages = new ArrayList<>();

    public ChatResponse() {
    }

    protected ChatResponse(Parcel in) {
        count = in.readInt();
        messages = in.createTypedArrayList(ChatMessage.CREATOR);
    }

    public static final Creator<ChatResponse> CREATOR = new Creator<ChatResponse>() {
        @Override
        public ChatResponse createFromParcel(Parcel in) {
            return new ChatResponse(in);
        }

        @Override
        public ChatResponse[] newArray(int size) {
            return new ChatResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(count);
        dest.writeTypedList(messages);
    }

    public static class ChatMessage implements Parcelable {
        @SerializedName("body")
        public String body = "";
        @SerializedName("username")
        public String username = "";
        @SerializedName("Name")
        public String Name = "";
        @SerializedName("image-url")
        public String imageUrl = "";
        @SerializedName("message-time")
        public String messageTime = "";

        protected ChatMessage(Parcel in) {
            body = in.readString();
            username = in.readString();
            Name = in.readString();
            imageUrl = in.readString();
            messageTime = in.readString();
        }

        public static final Creator<ChatMessage> CREATOR = new Creator<ChatMessage>() {
            @Override
            public ChatMessage createFromParcel(Parcel in) {
                return new ChatMessage(in);
            }

            @Override
            public ChatMessage[] newArray(int size) {
                return new ChatMessage[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(body);
            dest.writeString(username);
            dest.writeString(Name);
            dest.writeString(imageUrl);
            dest.writeString(messageTime);
        }
    }
}

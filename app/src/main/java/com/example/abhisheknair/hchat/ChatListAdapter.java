package com.example.abhisheknair.hchat;

/**
 * Created by abhisheknair on 21/05/16.
 */

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abhisheknair.hchat.ChatResponse.ChatMessage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * Created by madhur on 17/01/15.
 */
public class ChatListAdapter extends BaseAdapter {

    private ArrayList<ChatMessage> chatMessages;
    private Context context;
    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    public ChatListAdapter(Context context) {
        this.context = context;

    }

    public void setData(ArrayList<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        if (chatMessages != null)
            return chatMessages.size();
        else return 0;
    }

    @Override
    public Object getItem(int position) {
        return chatMessages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = null;
        ChatMessage message = chatMessages.get(position);
        RightViewHolder rightViewHolder;
        LeftViewHolder leftViewHolder;

        if (position % 2 == 0) {
            if (convertView == null) {
                v = LayoutInflater.from(context).inflate(R.layout.chat_head_left, null, false);
                rightViewHolder = new RightViewHolder();
                rightViewHolder.messageTextView = (TextView) v.findViewById(R.id.user_message_tv);
                rightViewHolder.timeTextView = (TextView) v.findViewById(R.id.time_tv);
                rightViewHolder.userNameTv = (TextView) v.findViewById(R.id.user_name_tv);
                v.setTag(rightViewHolder);
            } else {
                v = convertView;
                rightViewHolder = (RightViewHolder) v.getTag();
            }

            rightViewHolder.messageTextView.setText(message.body);
            rightViewHolder.userNameTv.setText(message.Name);
            String dateString = "";
            try {
                Date date = SIMPLE_DATE_FORMAT.parse(message.messageTime);
                dateString = String.valueOf(date.getMinutes()) + "." + String.valueOf(date.getSeconds());
            } catch (ParseException e) {
                Log.d("ADAPTER", "ParseException");
            }
            if (!TextUtils.isEmpty(dateString)) {
                rightViewHolder.timeTextView.setText(dateString);
            }

        } else {

            if (convertView == null) {
                v = LayoutInflater.from(context).inflate(R.layout.chat_head_right, null, false);
                leftViewHolder = new LeftViewHolder();
                leftViewHolder.messageTextView = (TextView) v.findViewById(R.id.other_message_text);
                leftViewHolder.timeTextView = (TextView) v.findViewById(R.id.time_text);
                leftViewHolder.messageStatus = (ImageView) v.findViewById(R.id.other_image_iv);
                leftViewHolder.userNameTv = (TextView) v.findViewById(R.id.other_name_tv);
                v.setTag(leftViewHolder);

            } else {
                v = convertView;
                leftViewHolder = (LeftViewHolder) v.getTag();
            }

            leftViewHolder.messageTextView.setText(message.body);
            leftViewHolder.userNameTv.setText(message.Name);
            String dateString = "";
            try {
                Date date = SIMPLE_DATE_FORMAT.parse(message.messageTime);
                dateString = String.valueOf(date.getMinutes()) +"." + String.valueOf(date.getSeconds());
            } catch (ParseException e) {
                Log.d("ADAPTER", "ParseException");
            }
            if (!TextUtils.isEmpty(dateString)) {
                leftViewHolder.timeTextView.setText(dateString);
            }
        }
        return v;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage message = chatMessages.get(position);
        return position % 2;
    }

    private class RightViewHolder {
        public TextView messageTextView;
        public TextView timeTextView;
        public TextView userNameTv;

    }

    private class LeftViewHolder {
        public ImageView messageStatus;
        public TextView messageTextView;
        public TextView timeTextView;
        public TextView userNameTv;

    }
}
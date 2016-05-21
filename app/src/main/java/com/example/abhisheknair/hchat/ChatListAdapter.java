package com.example.abhisheknair.hchat;

/**
 * Created by abhisheknair on 21/05/16.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abhisheknair.hchat.ChatResponse.ChatMessage;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
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

    public ChatMessage getData(int position) {
        return chatMessages.get(position);
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
        ChatMessage prevMessage = null;
        RightViewHolder rightViewHolder;
        LeftViewHolder leftViewHolder;

        if (position % 2 == 0) {
            if (convertView == null) {
                v = LayoutInflater.from(context).inflate(R.layout.chat_head_right, null, false);
                leftViewHolder = new LeftViewHolder();
                leftViewHolder.messageTextView = (TextView) v.findViewById(R.id.other_message_text);
                leftViewHolder.timeTextView = (TextView) v.findViewById(R.id.time_text);
                leftViewHolder.userImageView = (ImageView) v.findViewById(R.id.other_image_iv);
                leftViewHolder.userNameTv = (TextView) v.findViewById(R.id.other_name_tv);
                v.setTag(leftViewHolder);

            } else {
                v = convertView;
                leftViewHolder = (LeftViewHolder) v.getTag();
            }

            leftViewHolder.messageTextView.setText(message.body);
            leftViewHolder.userNameTv.setText(message.Name);
            String dateString = "";
            if (leftViewHolder.userImageView != null) {
                new ImageDownloaderTask(leftViewHolder.userImageView).execute(message.imageUrl);
            }
            try {
                Date date = SIMPLE_DATE_FORMAT.parse(message.messageTime);
                dateString = String.valueOf(date.getHours()) +":" + String.valueOf(date.getMinutes());
            } catch (ParseException e) {
                Log.d("ADAPTER", "ParseException");
            }
            if (!TextUtils.isEmpty(dateString)) {
                leftViewHolder.timeTextView.setText(dateString);
            }
        } else {
            if (convertView == null) {
                v = LayoutInflater.from(context).inflate(R.layout.chat_head_left, null, false);
                rightViewHolder = new RightViewHolder();
                rightViewHolder.messageTextView = (TextView) v.findViewById(R.id.user_message_tv);
                rightViewHolder.userImageView = (ImageView) v.findViewById(R.id.user_image_iv);
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
            if (rightViewHolder.userImageView != null) {
                new ImageDownloaderTask(rightViewHolder.userImageView).execute(message.imageUrl);
            }
            try {
                Date date = SIMPLE_DATE_FORMAT.parse(message.messageTime);
                dateString = String.valueOf(date.getHours()) + ":" + String.valueOf(date.getMinutes());
            } catch (ParseException e) {
                Log.d("ADAPTER", "ParseException");
            }
            if (!TextUtils.isEmpty(dateString)) {
                rightViewHolder.timeTextView.setText(dateString);
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
        return position % 2;
    }

    private class RightViewHolder {
        public TextView messageTextView;
        public TextView timeTextView;
        public TextView userNameTv;
        public ImageView userImageView;
    }

    private class LeftViewHolder {
        public ImageView userImageView;
        public TextView messageTextView;
        public TextView timeTextView;
        public TextView userNameTv;

    }

    class ImageDownloaderTask extends AsyncTask<String, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;

        public ImageDownloaderTask(ImageView imageView) {
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            return downloadBitmap(params[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                bitmap = null;
            }

            if (imageViewReference != null) {
                ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    if (bitmap != null) {
                        imageView.setImageBitmap(bitmap);
                    } else {
                        Drawable placeholder = imageView.getContext().getResources().getDrawable(R.drawable.ic_chat);
                        imageView.setImageDrawable(placeholder);
                    }
                }
            }
        }
    }

    private Bitmap downloadBitmap(String url) {
        HttpURLConnection urlConnection = null;
        try {
            URL uri = new URL(url);
            urlConnection = (HttpURLConnection) uri.openConnection();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != 200) {
                return null;
            }

            InputStream inputStream = urlConnection.getInputStream();
            if (inputStream != null) {
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            }
        } catch (Exception e) {
            if (urlConnection != null)
                urlConnection.disconnect();
            Log.w("ImageDownloader", "Error downloading image from " + url);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }
}
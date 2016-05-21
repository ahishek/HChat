package com.example.abhisheknair.hchat;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONObject;

public class ChatActivity extends AppCompatActivity {

    private static final String HAPTIC_URL = "http://haptik.co/android/test_data/";
    private ChatResponse chatAPIresponse;
    private Gson gson;
    private ListView mListView;
    private ChatListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initToolbar();
        initControls();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!= null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
    }

    private void initControls() {
        chatAPIresponse = new ChatResponse();
        gson = new Gson();
        mListView = (ListView) findViewById(R.id.chat_list_view);
        mAdapter = new ChatListAdapter(this);
        mListView.setAdapter(mAdapter);
        hitAPIURL();
    }

    private void hitAPIURL() {
        JsonObjectRequest request = new JsonObjectRequest(HAPTIC_URL, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        chatAPIresponse = gson.fromJson(String.valueOf(response), ChatResponse.class);
                        if (chatAPIresponse.count == 0) {
                            View emptyView = getLayoutInflater().inflate(R.layout.error_view, null);
                            TextView errorTv = (TextView) emptyView.findViewById(R.id.error_tv);
                            errorTv.setText(getString(R.string.empty_chats));
                            mListView.setEmptyView(emptyView);
                        } else {
                            populateListView();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mListView.setEmptyView(getLayoutInflater().inflate(R.layout.error_view, null));
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void populateListView() {
        mAdapter.setData(chatAPIresponse.messages);
    }

}

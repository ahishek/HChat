package com.example.abhisheknair.hchat;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONObject;

public class ChatActivity extends AppCompatActivity implements ListView.OnItemClickListener {

    private static final String HAPTIC_URL = "http://haptik.co/android/test_data/";
    private ChatResponse chatAPIresponse;
    private Gson gson;
    private ListView mListView;
    private ChatListAdapter mAdapter;
    private SparseIntArray countHashMap;

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
        countHashMap = new SparseIntArray();
        mListView = (ListView) findViewById(R.id.chat_list_view);
        mListView.setOnItemClickListener(this);
        mAdapter = new ChatListAdapter(this);
        mListView.setAdapter(mAdapter);
        EditText chatEditText = (EditText) findViewById(R.id.chat_text);
        chatEditText.setEnabled(false);
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
        mListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mListView.setSelection(mAdapter.getCount() - 1);
            }
        }, 500);
        getCountOfUserMessages();
    }

    private void getCountOfUserMessages() {
        for (int i = 0; i <chatAPIresponse.count; i++) {
            String username = chatAPIresponse.messages.get(i).username;
            int hash = getHash(username);
            if (countHashMap.get(hash, -1) == -1) {
                countHashMap.put(hash, 1);
            } else {
                int count = countHashMap.get(hash);
                countHashMap.put(hash, count + 1);
            }
        }
    }

    private int getHash(String x) {
        char ch[];
        ch = x.toCharArray();
        int i, sum;
        for (sum=0, i=0; i < x.length(); i++)
            sum += ch[i];
        return sum % 27;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_count, null);
        TextView tv = (TextView) dialogView.findViewById(R.id.dialog_error_tv);
        ChatResponse.ChatMessage message = mAdapter.getData(position);
        int count = countHashMap.get(getHash(message.username));
        tv.setText(getString(R.string.number_string_1) + " " + message.Name + " " + getString(R.string.string_is) + " " + String.valueOf(count));
        alertDialog.setView(dialogView);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }
}

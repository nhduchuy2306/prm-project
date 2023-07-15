package com.example.happygear.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.happygear.R;
import com.example.happygear.adapters.ChatAdapter;
import com.example.happygear.interfaces.ChatListener;
import com.example.happygear.models.ChatMessage;
import com.example.happygear.models.User;
import com.example.happygear.services.UserService;
import com.example.happygear.utils.Constants;
import com.example.happygear.utils.SerializableObject;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity implements ChatListener {

    //    private ActivityChatBinding binding;
    private User user;
    private User receiver;
    private List<ChatMessage> mChatMessages;
    private ChatAdapter chatAdapter;
    private RecyclerView chatRecyclerView;
    private SharedPreferences sharedPreferences;
    private FirebaseFirestore database;

    private EditText inputMessage;
    private View layoutSend;

    private ProgressBar progressBar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        init();
        listenMessage();
        layoutSend.setOnClickListener(v -> sendMessage());


        AppCompatImageView back = findViewById(R.id.imgBack);
        back.setOnClickListener(v -> onBackPressed());
    }

    private void init() {
        try {
            inputMessage = findViewById(R.id.inputMessage);
            layoutSend = findViewById(R.id.layoutSend);
            progressBar = findViewById(R.id.progressBar);
            chatRecyclerView = findViewById(R.id.chatRecyclerView);
            database = FirebaseFirestore.getInstance();

            //get sender
            sharedPreferences = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            String userSerialized = sharedPreferences.getString("user", null);
            if (userSerialized != null) {
                user = (User) SerializableObject.deserializeObject(userSerialized);
                if (!user.getUsername().equals("admin")) {
                    receiver = new User();
                    receiver.setUsername("admin");
                    receiver.setFullName("Admin");
                    receiver.setPassword("admin");
                    receiver.setEmail("admin@gmail.com");
                    receiver.setStatus(true);
                    receiver.setRoleId(1);
                }
            }


            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
            chatRecyclerView.setLayoutManager(linearLayoutManager);
            mChatMessages = new ArrayList<>();
            chatAdapter = new ChatAdapter(this, user.getUsername());
            chatAdapter.setData(mChatMessages);


            //set adapter
            chatRecyclerView.setAdapter(chatAdapter);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void loadAdmin() {
        UserService.userService.getUserByUsername("admin").enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    receiver = response.body();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    private void sendMessage() {
        HashMap<String, Object> message = new HashMap<>();
        message.put(Constants.KEY_SENDER_ID, user.getUsername());
        message.put(Constants.KEY_RECEIVER_ID, receiver.getUsername());
        message.put(Constants.KEY_MESSAGE, inputMessage.getText().toString());
        message.put(Constants.KEY_TIMESTAMP, new Date());
        database.collection(Constants.KEY_COLLECTION_CHAT).add(message);

        inputMessage.setText(null);
    }

    private void listenMessage() {
        database.collection(Constants.KEY_COLLECTION_CHAT)
                .whereEqualTo(Constants.KEY_SENDER_ID, user.getUsername())
                .whereEqualTo(Constants.KEY_RECEIVER_ID, receiver.getUsername())
                .addSnapshotListener(eventListener);

        database.collection(Constants.KEY_COLLECTION_CHAT)
                .whereEqualTo(Constants.KEY_SENDER_ID, receiver.getUsername())
                .whereEqualTo(Constants.KEY_RECEIVER_ID, user.getUsername())
                .addSnapshotListener(eventListener);
    }

    private final EventListener<QuerySnapshot> eventListener = ((value, error) -> {
        if (error != null) {
            return;
        }
        if (value != null) {
            int count = mChatMessages.size();
            for (DocumentChange documentChange : value.getDocumentChanges()) {
                if (documentChange.getType() == DocumentChange.Type.ADDED) {
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                    chatMessage.receiverId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                    chatMessage.message = documentChange.getDocument().getString(Constants.KEY_MESSAGE);
                    chatMessage.dateTime = getReadableDateTime(documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP));
                    chatMessage.dateObject = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                    mChatMessages.add(chatMessage);
                }
            }
//            Collections.sort(mChatMessages, (obj1, obj2) -> obj1.dateObject.compareTo(obj2.dateObject));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Collections.sort(mChatMessages, Comparator.comparing(obj -> obj.dateObject));
            }
            if (count == 0) {
                chatAdapter.notifyDataSetChanged();
            } else {
                chatAdapter.notifyItemRangeChanged(mChatMessages.size(), mChatMessages.size());
                chatRecyclerView.smoothScrollToPosition(mChatMessages.size() - 1);
            }
            chatRecyclerView.setVisibility(View.VISIBLE);
        }
        progressBar.setVisibility(View.GONE);
    });

    private String getReadableDateTime(Date date) {
        return new SimpleDateFormat("MMMM dd, yyyy - hh:mm a", Locale.getDefault()).format(date);
    }


}

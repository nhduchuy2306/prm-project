package com.example.happygear.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.happygear.R;
import com.example.happygear.adapters.RecentConversationsAdapter;
import com.example.happygear.interfaces.ConversionListener;
import com.example.happygear.models.ChatMessage;
import com.example.happygear.models.User;
import com.example.happygear.services.UserService;
import com.example.happygear.utils.Constants;
import com.example.happygear.utils.SerializableObject;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatManagementActivity extends AppCompatActivity implements ConversionListener {

    private List<ChatMessage> conversations;
    private RecyclerView conversationsRecyclerView;

    private SharedPreferences sharedPreferences;

    private User receiver;
    private ProgressBar progressBar;

    private User user;
    private RecentConversationsAdapter conversationsAdapter;
    private FirebaseFirestore database;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chatmanagement);
        init();
        listenConversations();

        backToPreviousScreen();
    }

    private void init() {
        try {
            database = FirebaseFirestore.getInstance();
            progressBar = findViewById(R.id.progressBar_Management);
            conversationsRecyclerView = findViewById(R.id.conversationsRecyclerView);

            //get user
            sharedPreferences = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            String userSerialized = sharedPreferences.getString("user", null);
            if (userSerialized != null) {
                user = (User) SerializableObject.deserializeObject(userSerialized);
            }

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
            conversationsRecyclerView.setLayoutManager(linearLayoutManager);
            conversations = new ArrayList<>();
            conversationsAdapter = new RecentConversationsAdapter(this);
            conversationsAdapter.setData(conversations);

            //set adapter
            conversationsRecyclerView.setAdapter(conversationsAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void listenConversations() {
        database.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
                .whereEqualTo(Constants.KEY_SENDER_ID, user.getUsername())
                .addSnapshotListener(eventListener);
        database.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
                .whereEqualTo(Constants.KEY_RECEIVER_ID, user.getUsername())
                .addSnapshotListener(eventListener);
    }

    private final EventListener<QuerySnapshot> eventListener = (value, error) -> {
        if (error != null) {
            return;
        }
        if (value != null) {
            for (DocumentChange documentChange : value.getDocumentChanges()) {
                if (documentChange.getType() == DocumentChange.Type.ADDED) {
                    String senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                    String receiverId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.senderId = senderId;
                    chatMessage.receiverId = receiverId;
                    if (user.getUsername().equals(senderId)) {
                        chatMessage.conversionId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                        chatMessage.conversionName = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                        chatMessage.conversionId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                    } else {
                        chatMessage.conversionId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                        chatMessage.conversionName = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                        chatMessage.conversionId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                    }
                    chatMessage.message = documentChange.getDocument().getString(Constants.KEY_LAST_MESSAGE);
                    chatMessage.dateObject = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                    conversations.add(chatMessage);
                } else if (documentChange.getType() == DocumentChange.Type.MODIFIED) {
                    for (int i = 0; i < conversations.size(); i++) {
                        String senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                        String receiverId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);

                        if (conversations.get(i).senderId.equals(senderId) && conversations.get(i).receiverId.equals(receiverId)) {
                            conversations.get(i).message = documentChange.getDocument().getString(Constants.KEY_LAST_MESSAGE);
                            conversations.get(i).dateObject = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                            break;
                        }
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Collections.sort(conversations, Comparator.comparing(obj -> obj.dateObject));
                    }

                }
            }
            conversationsAdapter.notifyDataSetChanged();
            conversationsRecyclerView.smoothScrollToPosition(0);
            conversationsRecyclerView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    };

    private void backToPreviousScreen() {
        Toolbar toolbar = findViewById(R.id.toolbar_chatmanagement);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }


    @Override
    public void onConversionClick(User userTest, ChatMessage chatMessage) {
       try {
           userTest.setUsername(chatMessage.conversionId);
           userTest.setFullName(chatMessage.conversionName);
           UserService.userService.getUserByUsername(userTest.getUsername()).enqueue(new Callback<User>() {
               @Override
               public void onResponse(Call<User> call, Response<User> response) {
                try{
                    if (response.isSuccessful()) {
                        receiver = response.body();

                        String receiverSerialized = SerializableObject.serializeObject(receiver);
                        SharedPreferences sharedPreferences = getSharedPreferences("Receiver", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("receiver", receiverSerialized);
                        editor.apply();


                        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                        startActivity(intent);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
               }
               @Override
               public void onFailure(Call<User> call, Throwable t) {

               }
           });

       }
       catch (Exception e){
           e.printStackTrace();
       }
    }
}

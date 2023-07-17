package com.example.happygear.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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

    private GoogleSignInAccount account;
    private List<ChatMessage> mChatMessages;
    private ChatAdapter chatAdapter;
    private RecyclerView chatRecyclerView;
    private SharedPreferences sharedPreferences;
    private FirebaseFirestore database;

    private EditText inputMessage;
    private View layoutSend;

    private ProgressBar progressBar;

    private TextView textNameChat;

    private String conversionId = null;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        init();
        listenMessage();
        layoutSend.setOnClickListener(v -> sendMessage());

        checkKeyboard();
        AppCompatImageView back = findViewById(R.id.imgBack);
        back.setOnClickListener(v -> onBackPressed());
    }

    private void init() {
        try {
            inputMessage = findViewById(R.id.inputMessage);
            layoutSend = findViewById(R.id.layoutSend);
            progressBar = findViewById(R.id.progressBar);
            chatRecyclerView = findViewById(R.id.chatRecyclerView);
            textNameChat = findViewById(R.id.textName_Chat);
            database = FirebaseFirestore.getInstance();

            //get sender
            account = GoogleSignIn.getLastSignedInAccount(this);

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
                }else{
                    sharedPreferences = this.getSharedPreferences("Receiver", Context.MODE_PRIVATE);
                    String receiverSerialized = sharedPreferences.getString("receiver", null);
                    if(receiverSerialized != null){
                        receiver = (User) SerializableObject.deserializeObject(receiverSerialized);
                        textNameChat.setText(receiver.getFullName());
                    }
                }
            }
            else if (account != null){
                user = new User();
                user.setUsername(account.getId());
                user.setFullName(account.getDisplayName());
                user.setEmail(account.getEmail());
                if (!user.getUsername().equals("admin")) {
                    receiver = new User();
                    receiver.setUsername("admin");
                    receiver.setFullName("Admin");
                    receiver.setPassword("admin");
                    receiver.setEmail("admin@gmail.com");
                    receiver.setStatus(true);
                    receiver.setRoleId(1);
                }else{
                    sharedPreferences = this.getSharedPreferences("Receiver", Context.MODE_PRIVATE);
                    String receiverSerialized = sharedPreferences.getString("receiver", null);
                    if(receiverSerialized != null){
                        receiver = (User) SerializableObject.deserializeObject(receiverSerialized);
                        textNameChat.setText(receiver.getFullName());
                    }
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


    private void sendMessage() {
        HashMap<String, Object> message = new HashMap<>();

        message.put(Constants.KEY_SENDER_ID, user.getUsername());
        message.put(Constants.KEY_SENDER_NAME, user.getFullName());
        message.put(Constants.KEY_RECEIVER_ID, receiver.getUsername());
        message.put(Constants.KEY_RECEIVER_NAME, receiver.getFullName());
        message.put(Constants.KEY_MESSAGE, inputMessage.getText().toString());
        message.put(Constants.KEY_TIMESTAMP, new Date());
        database.collection(Constants.KEY_COLLECTION_CHAT).add(message);
        if (conversionId != null){
            updateConversion(inputMessage.getText().toString());
        }
        else{
            HashMap<String, Object> conversion = new HashMap<>();
            conversion.put(Constants.KEY_SENDER_ID, user.getUsername());
            conversion.put(Constants.KEY_SENDER_NAME, user.getFullName());
            conversion.put(Constants.KEY_RECEIVER_ID, receiver.getUsername());
            conversion.put(Constants.KEY_RECEIVER_NAME, receiver.getFullName());
            conversion.put(Constants.KEY_SENDER_ID, user.getUsername());
            conversion.put(Constants.KEY_LAST_MESSAGE, inputMessage.getText().toString());
            conversion.put(Constants.KEY_TIMESTAMP, new Date());
            addConversion(conversion);
        }
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
                    chatMessage.senderName = documentChange.getDocument().getString(Constants.KEY_SENDER_NAME);
                    chatMessage.receiverId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                    chatMessage.receiverName = documentChange.getDocument().getString(Constants.KEY_RECEIVER_NAME);
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

        if(conversionId == null){
            checkForConversion();
        }
    });


    private String getReadableDateTime(Date date) {
        return new SimpleDateFormat("MMMM dd, yyyy - hh:mm a", Locale.getDefault()).format(date);
    }


    private void addConversion(HashMap<String, Object> conversion){
        database.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
                .add(conversion)
                .addOnSuccessListener(documentReference -> conversionId = documentReference.getId());
    }


    private void updateConversion(String message){
        DocumentReference documentReference = database.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
                .document(conversionId);
        documentReference.update(Constants.KEY_LAST_MESSAGE, message, Constants.KEY_TIMESTAMP, new Date());
    }


    private void checkForConversion(){
        if(mChatMessages.size() != 0){
            checkForConversionRemotely( user.getUsername(), receiver.getUsername() );
            checkForConversionRemotely(receiver.getUsername(), user.getUsername());
        }
    }


    private void checkForConversionRemotely(String senderId, String receiverId){
        database.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
                .whereEqualTo(Constants.KEY_SENDER_ID, senderId)
                .whereEqualTo(Constants.KEY_RECEIVER_ID, receiverId)
                .get()
                .addOnCompleteListener(conversionOnCompleteListener);
    }

    private final OnCompleteListener<QuerySnapshot> conversionOnCompleteListener = task -> {
    if(task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0){
        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
        conversionId = documentSnapshot.getId();
    }
    };


    private void checkKeyboard(){
        final View chatRoot = findViewById(R.id.chatRoot);
        chatRoot.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();

                //r will be populated with the coordinates of your view that area still visible
                chatRoot.getWindowVisibleDisplayFrame(r);

                int heightDiff = chatRoot.getRootView().getHeight() - r.height();
                if(heightDiff > 0.25*chatRoot.getRootView().getHeight()){
                    if (mChatMessages.size() > 0){
                        chatRecyclerView.scrollToPosition(mChatMessages.size() - 1);
                        chatRoot.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                }
            }
        });
    }
}

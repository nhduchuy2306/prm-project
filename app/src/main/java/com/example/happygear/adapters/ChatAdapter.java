package com.example.happygear.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.happygear.R;
import com.example.happygear.databinding.ItemContainerSentMessageBinding;
import com.example.happygear.interfaces.ChatListener;
import com.example.happygear.models.ChatMessage;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private  List<ChatMessage> mChatMessages;
    private  String senderId;

    public static final int VIEW_TYPE_SENT = 1;
    public static final int VIEW_TYPE_RECEIVED = 2;
    private ChatListener mChatListener;

    public ChatAdapter(ChatListener mChatListener, List<ChatMessage> mChatMessages){
        this.mChatMessages = mChatMessages;
        this.mChatListener = mChatListener;
    }

    public void setData(List<ChatMessage> mChatMessages){
        this.mChatMessages = mChatMessages;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return 0;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_SENT){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_sent_message, parent, false);
            return new SentMessageViewHolder(view);
        }
        else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_received_message, parent, false);
            return new ReceivedMessageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == VIEW_TYPE_SENT){
            ChatMessage chatMessage = mChatMessages.get(position);
            if(chatMessage == null){
                return;
            }
            ((SentMessageViewHolder) holder).tvMessage.setText(chatMessage.getMessage());
            ((SentMessageViewHolder) holder).tvDateTime.setText(chatMessage.getDateTime());
        }else{
            ChatMessage chatMessage = mChatMessages.get(position);
            if(chatMessage == null){
                return;
            }
            ((ReceivedMessageViewHolder) holder).tvMessage.setText(chatMessage.getMessage());
            ((ReceivedMessageViewHolder) holder).tvDateTime.setText(chatMessage.getDateTime());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(mChatMessages.get(position).senderId.equals(senderId)){
            return VIEW_TYPE_SENT;
        }
        else{
            return VIEW_TYPE_RECEIVED;
        }
    }

    public static class SentMessageViewHolder extends  RecyclerView.ViewHolder{
       private TextView tvMessage;
       private TextView tvDateTime;


       public SentMessageViewHolder(@NonNull View itemView) {
           super(itemView);

           tvMessage = itemView.findViewById(R.id.textMessage);
           tvDateTime = itemView.findViewById(R.id.textDateTime);
       }
   }

   public static class ReceivedMessageViewHolder extends  RecyclerView.ViewHolder{
       private TextView tvMessage;
       private TextView tvDateTime;

       public ReceivedMessageViewHolder(@NonNull View itemView) {
           super(itemView);
           tvMessage = itemView.findViewById(R.id.textMessage);
           tvDateTime = itemView.findViewById(R.id.textDateTime);
       }
   }
}

package com.example.happygear.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.happygear.R;
import com.example.happygear.interfaces.ConversionListener;
import com.example.happygear.models.ChatMessage;
import com.example.happygear.models.User;

import java.util.List;

public class RecentConversationsAdapter extends RecyclerView.Adapter<RecentConversationsAdapter.ConversionViewHolder> {

    private List<ChatMessage> mChatMessages;
    private ConversionListener mConversionListener;

    public RecentConversationsAdapter(ConversionListener mConversionListener){
        this.mConversionListener = mConversionListener;
    }

    public void setData(List<ChatMessage> mChatMessages){
        this.mChatMessages = mChatMessages;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ConversionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_recent_conversation, parent, false);
        return new ConversionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConversionViewHolder holder, int position) {
        ChatMessage chatMessage = mChatMessages.get(position);
        if(chatMessage == null){
            return;
        }
        holder.tvUserName.setText(chatMessage.getSenderId());
        holder.tvRecentMessage.setText(chatMessage.getMessage());
        holder.itemRecentConversation.setOnClickListener(v -> mConversionListener.onConversionClick(new User(), chatMessage));
    }

    @Override
    public int getItemCount() {
        if(mChatMessages != null){
            return mChatMessages.size();
        }
        return 0;
    }


    public class ConversionViewHolder extends RecyclerView.ViewHolder{

        private TextView tvUserName;
        private TextView tvRecentMessage;
        private ConstraintLayout itemRecentConversation;

        public ConversionViewHolder(@NonNull View itemView) {
            super(itemView);

            tvUserName = itemView.findViewById(R.id.textUserName);
            tvRecentMessage = itemView.findViewById(R.id.textRecentMessage);
            itemRecentConversation = itemView.findViewById(R.id.item_recent_conversation);

        }
    }
}

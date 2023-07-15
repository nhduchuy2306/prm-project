package com.example.happygear.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.happygear.R;
import com.example.happygear.models.ChatMessage;

import java.util.List;

public class RecentConversationsAdapter extends RecyclerView.Adapter<RecentConversationsAdapter.ConversionViewHolder> {

    private List<ChatMessage> mChatMessages;

    public RecentConversationsAdapter(){

    }

    public void setData(List<ChatMessage> mChatMessages){
        this.mChatMessages = mChatMessages;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ConversionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.)
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ConversionViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        if(mChatMessages != null){
            return mChatMessages.size();
        }
        return 0;
    }


    public class ConversionViewHolder extends RecyclerView.ViewHolder{

        public ConversionViewHolder(@NonNull View itemView) {
            super(itemView);


        }
    }
}

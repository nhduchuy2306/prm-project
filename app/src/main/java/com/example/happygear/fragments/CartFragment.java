package com.example.happygear.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.happygear.R;
import com.example.happygear.adapters.CartAdapter;
import com.example.happygear.databases.AppDatabase;
import com.example.happygear.dto.CartDto;
import com.example.happygear.interfaces.CartItemListener;
import com.example.happygear.utils.SwipeUtil;

import java.util.List;

public class CartFragment extends Fragment implements CartItemListener {

    private RecyclerView cartRecyclerView;
    private CartAdapter cartAdapter;
    private List<CartDto> cartDtoList;
    private AppDatabase db;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = Room.databaseBuilder(requireContext(), AppDatabase.class, "cart.db").allowMainThreadQueries().build();
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        cartRecyclerView = view.findViewById(R.id.cart_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        cartRecyclerView.setLayoutManager(linearLayoutManager);

        cartAdapter = new CartAdapter(this);
        cartDtoList = getCartDtoList();
        cartAdapter.setCartDtoList(cartDtoList);
        cartRecyclerView.setAdapter(cartAdapter);

        SwipeUtil swipeUtil = new SwipeUtil(cartAdapter, requireContext(), cartDtoList, this);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeUtil);
        itemTouchHelper.attachToRecyclerView(cartRecyclerView);

        return view;
    }

    private List<CartDto> getCartDtoList() {
        return db.cartDao().getCartItems();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onRemove(CartDto cartDto) {
        cartDtoList.remove(cartDto);
        new Thread(() -> {
            db.cartDao().delete(cartDto.getProductId());
        }).start();
        cartAdapter.notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onIncrease(CartDto cartDto) {
        cartDto.setQuantity(cartDto.getQuantity() + 1);
        new Thread(() -> {
            db.cartDao().update(cartDto.getProductId(), cartDto.getQuantity());
        }).start();
        cartAdapter.notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onDecrease(CartDto cartDto) {
        if (cartDto.getQuantity() == 1) {
            return;
        }
        cartDto.setQuantity(cartDto.getQuantity() - 1);
        new Thread(() -> {
            db.cartDao().update(cartDto.getProductId(), cartDto.getQuantity());
        }).start();
        cartAdapter.notifyDataSetChanged();
    }
}

package com.example.happygear.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.happygear.MainActivity;
import com.example.happygear.R;
import com.example.happygear.activities.CheckoutActivity;
import com.example.happygear.adapters.CartAdapter;
import com.example.happygear.databases.AppDatabase;
import com.example.happygear.dto.CartDto;
import com.example.happygear.interfaces.CartItemListener;
import com.example.happygear.models.utils.SwipeUtil;
import com.example.happygear.models.User;
import com.example.happygear.services.OrderService;
import com.example.happygear.models.utils.SerializableObject;
import com.example.happygear.models.utils.SwipeUtil;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;

public class CartFragment extends Fragment implements CartItemListener {

    private RecyclerView cartRecyclerView;
    private CartAdapter cartAdapter;
    private List<CartDto> cartDtoList;
    private TextView cartEmptyTextView;
    private Button checkoutButton;
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

        cartEmptyTextView = view.findViewById(R.id.cart_empty_text);
        checkoutButton = view.findViewById(R.id.cart_checkout_button);

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

        if (cartDtoList.size() == 0) {
            cartEmptyTextView.setVisibility(View.VISIBLE);
            checkoutButton.setVisibility(View.GONE);
        } else {
            cartEmptyTextView.setVisibility(View.GONE);
            checkoutButton.setVisibility(View.VISIBLE);
        }

        checkoutButton.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), CheckoutActivity.class);
            startActivity(intent);
        });

        return view;
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onResume() {
        super.onResume();

        List<CartDto> cartDtoList = db.cartDao().getCartItems();
        if (cartDtoList.size() == 0) {
            cartEmptyTextView.setVisibility(View.VISIBLE);
            checkoutButton.setVisibility(View.GONE);
            cartAdapter.setCartDtoList(cartDtoList);
            cartAdapter.notifyDataSetChanged();
        } else {
            cartEmptyTextView.setVisibility(View.GONE);
            checkoutButton.setVisibility(View.VISIBLE);
        }
    }

    private List<CartDto> getCartDtoList() {
        return db.cartDao().getCartItems();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onRemove(CartDto cartDto) {
        cartDtoList.remove(cartDto);
        if (cartDtoList.size() == 0) {
            cartEmptyTextView.setVisibility(View.VISIBLE);
            checkoutButton.setVisibility(View.GONE);
        } else {
            cartEmptyTextView.setVisibility(View.GONE);
            checkoutButton.setVisibility(View.VISIBLE);
        }
        new Thread(() -> {
            db.cartDao().delete(cartDto.getProductId());
            int size = db.cartDao().getCartCount();
            MainActivity activity = (MainActivity) getActivity();
            activity.updateCartBadge(size);
        }).start();
        cartAdapter.notifyDataSetChanged();

        Toast.makeText(getContext(), "Remove from cart", Toast.LENGTH_SHORT).show();
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

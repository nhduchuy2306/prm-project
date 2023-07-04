package com.example.happygear.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.happygear.R;
import com.example.happygear.adapters.CartAdapter;
import com.example.happygear.dto.CartDto;
import com.example.happygear.interfaces.CartItemListener;

import java.util.List;

public class SwipeUtil extends ItemTouchHelper.SimpleCallback {
    private final Context context;
    private final CartAdapter cartAdapter;
    private final List<CartDto> cartDtoList;
    private final CartItemListener cartItemListener;
    private final ColorDrawable background;
    private final Drawable deleteIcon;

    public SwipeUtil(CartAdapter cartAdapter, Context context, List<CartDto> cartDtoList, CartItemListener cartItemListener) {
        super(0, ItemTouchHelper.LEFT);
        this.cartAdapter = cartAdapter;
        this.context = context;
        this.cartDtoList = cartDtoList;
        this.cartItemListener = cartItemListener;
        this.background = new ColorDrawable(Color.RED);
        this.deleteIcon = ContextCompat.getDrawable(this.context, R.drawable.ic_close);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        if (direction == ItemTouchHelper.LEFT) {
            cartItemListener.onRemove(cartDtoList.get(position));
            cartAdapter.notifyItemRemoved(position);
        }
    }

    @Override
    public void onChildDrawOver(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        View itemView = viewHolder.itemView;

        if (deleteIcon != null) {
            deleteIcon.setColorFilter(Color.WHITE, android.graphics.PorterDuff.Mode.SRC_IN);
        }

        int iconMargin = (itemView.getHeight() - deleteIcon.getIntrinsicHeight()) / 2;
        int iconTop = itemView.getTop() + (itemView.getHeight() - deleteIcon.getIntrinsicHeight()) / 2;
        int iconBottom = iconTop + deleteIcon.getIntrinsicHeight();

        if (dX < 0) {
            int iconRight = itemView.getRight() - iconMargin;
            int iconLeft = itemView.getRight() - iconMargin - deleteIcon.getIntrinsicWidth();
            deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

            background.setBounds(itemView.getRight() + ((int) dX), itemView.getTop(), itemView.getRight(), itemView.getBottom());
        }

        background.draw(c);
        deleteIcon.draw(c);
    }
}

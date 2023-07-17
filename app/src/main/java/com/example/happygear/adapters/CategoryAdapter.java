package com.example.happygear.adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.happygear.R;
import com.example.happygear.fragments.ExploreFragment;
import com.example.happygear.models.Category;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Random;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategroyViewHolder> {

    private List<Category> mCategoryList;
    private Context mContext;

    public CategoryAdapter(Context context) {
        mContext = context;
    }

    public void setCategoryList(List<Category> categoryList) {
        mCategoryList = categoryList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategroyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new CategroyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategroyViewHolder holder, int position) {
        Category category = mCategoryList.get(position);
        if (category == null)
            return;
        Picasso.get().load(category.getCategoryPicture()).placeholder(androidx.recyclerview.selection.R.drawable.selection_band_overlay).into(holder.categoryImage);
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        int whiteColor = Color.argb(255, 255, 255, 255);

        holder.categoryName.setTextColor(whiteColor);
        holder.categoryName.setText(category.getCategoryName());
        holder.categoryItemLayout.setBackgroundColor(color);
        holder.categoryItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CategoryAdapter", "onClick: " + category.getCategoryName());
                Bundle bundle = new Bundle();
                bundle.putInt("categoryId", category.getCategoryId());
                Fragment fragment = new ExploreFragment();
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = ((FragmentActivity) mContext).getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.nav_main_fragment, fragment).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mCategoryList != null)
            return mCategoryList.size();
        return 0;
    }

    public class CategroyViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout categoryItemLayout;
        private ImageView categoryImage;
        private TextView categoryName;

        public CategroyViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryItemLayout = itemView.findViewById(R.id.category_item_layout);
            categoryImage = itemView.findViewById(R.id.category_image);
            categoryName = itemView.findViewById(R.id.category_name);
        }
    }
}

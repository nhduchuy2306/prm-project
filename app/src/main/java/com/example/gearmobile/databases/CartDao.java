package com.example.gearmobile.databases;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.gearmobile.dto.CartDto;

import java.util.List;

@Dao
public interface CartDao {
    @Query("SELECT COUNT(*) FROM cart")
    int getCartCount();

    @Query("SELECT * FROM cart")
    List<CartDto> getCartItems();

    @Insert
    void insert(CartDto cartDto);

    @Query("DELETE FROM cart WHERE productId = :productId")
    void delete(int productId);
}

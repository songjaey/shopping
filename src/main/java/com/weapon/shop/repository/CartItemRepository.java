package com.weapon.shop.repository;

import com.weapon.shop.entity.Cart;
import com.weapon.shop.entity.CartItem;
import com.weapon.shop.entity.Item;
import com.weapon.shop.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findByCartAndItem(Cart cart, Item item);

}

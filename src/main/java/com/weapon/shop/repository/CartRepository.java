package com.weapon.shop.repository;

import com.weapon.shop.entity.Cart;
import com.weapon.shop.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    // 특정 사용자의 장바구니 가져오기

    List<Cart> findByMember(Member member);

    Optional<Cart> findByMemberAndCartItems_ItemId(Member member, Long itemId);

}

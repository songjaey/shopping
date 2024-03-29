package com.weapon.shop.repository;

import com.weapon.shop.dto.CartDetailDto;
import com.weapon.shop.entity.Cart;
import com.weapon.shop.entity.CartItem;
import com.weapon.shop.entity.Item;
import com.weapon.shop.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findByCartAndItem(Cart cart, Item item);

    CartItem findByItem(Item item);

    CartItem findByCartIdAndItemId(Long cartId, Long itemId);

    void deleteByItem(CartItem cartItem);

    @Query("select new com.weapon.shop.dto.CartDetailDto(ci.id, i.itemNm,"+
            "i.price, ci.count, im.imgUrl) " +
            "from CartItem ci, ItemImg im join ci.item i " +
            "where  ci.cart.id = :cartId "+
            "and im.item.id = ci.item.id " +
            "and im.repImgYn = 'Y' " +
            "order by ci.regTime desc ")
    List<CartDetailDto> findCartDetailDtoList(@Param("cartId") Long cartId);


}

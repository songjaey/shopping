package com.weapon.shop.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
public class Cart extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "cart_member_id")
    private Member member;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    private List<CartItem> cartItems;

    public static Cart createCart(Member member) {
        Cart cart = new Cart();
        cart.setMember(member);
        return cart;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public CartItem getCartItem(Long cartItemId) {
        for (CartItem cartItem : cartItems) {
            if (cartItem.getId().equals(cartItemId)) {
                return cartItem;
            }
        }
        return null;
    }

    public void deleteCartItem(Long cartItemId) {
        // 장바구니에서 해당 상품 제거
        CartItem cartItemToRemove = null;
        for (CartItem cartItem : cartItems) {
            if (cartItem.getId().equals(cartItemId)) {
                cartItemToRemove = cartItem;
                break;
            }
        }

        if (cartItemToRemove != null) {
            cartItems.remove(cartItemToRemove);
        }
    }
}
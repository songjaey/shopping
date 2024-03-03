package com.weapon.shop.service;

import com.weapon.shop.dto.CartDetailDto;
import com.weapon.shop.dto.CartItemDto;
import com.weapon.shop.dto.CartOrderDto;
import com.weapon.shop.entity.*;
import com.weapon.shop.repository.*;
//import com.weapon.shop.repository.CartRepository;
//import com.weapon.shop.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {
    private final ItemRepository itemRepository;
    private final ItemImgRepository itemImgRepository;
    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderService orderService;
    @Transactional
    public Long addCart(CartItemDto cartItemDto, String email) {
        Item item = findItemById(cartItemDto.getItemId());
        Member member = memberRepository.findByEmail(email);
        List<Cart> carts = cartRepository.findByMember(member);
        Cart cart = carts.isEmpty() ? Cart.createCart(member) : carts.get(0);

        // Cart를 먼저 저장하고 CartItem을 만들기 전에 저장된 것을 확인합니다.
        cartRepository.save(cart);

        // 이제 아이템이 장바구니에 이미 있는지 또는 새로운 카트 아이템을 만들기 전에 확인합니다.
        CartItem existingCartItem = cartItemRepository.findByCartAndItem(cart, item);

        if (existingCartItem != null) {
            // 이미 있는 경우 수량을 업데이트합니다.
            existingCartItem.addCount(cartItemDto.getCount());
            cartItemRepository.save(existingCartItem);
        } else {
            // 없는 경우 새로운 카트 아이템을 생성합니다.
            CartItem newCartItem = CartItem.createCartItem(cart, item, cartItemDto.getCount());
            cartItemRepository.save(newCartItem);
        }

        // 카트 아이템을 확인하거나 업데이트한 후에는 변경 내용을 유지하기 위해 cartRepository.save(cart)를 다시 호출합니다.
        // 여기서는 카트 아이템이 이미 저장된 상태이므로 refresh 대신에 다시 저장해도 됩니다.
        cartRepository.save(cart);

        return cart.getId();
    }
    private Item findItemById(Long itemId) {
        return itemRepository.findById(itemId).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<CartDetailDto> getCartList(String email) {
        // 장바구니 메뉴 클릭시
        Member member = memberRepository.findByEmail(email);

        List<Cart> carts = cartRepository.findByMember(member);
        if (carts == null || carts.isEmpty()) {
            return List.of(); // 장바구니가 비어있는 경우 빈 리스트 반환
        }

        List<CartDetailDto> cartDetailDtos = new ArrayList<>();
        for (Cart cart : carts) {
            List<CartItem> cartItems = cart.getCartItems();
            for (CartItem cartItem : cartItems) {
                ItemImg itemImg = itemImgRepository.findByItemIdAndRepImgYn(
                        cartItem.getItem().getId() , "Y");

                CartDetailDto cartDetailDto = new CartDetailDto(
                        //cartItem.getId(),
                        cartItem.getItem().getId(),
                        cartItem.getItem().getItemNm(),
                        cartItem.getItem().getPrice(),
                        cartItem.getCount(),
                        itemImg.getImgUrl()
                );
                cartDetailDtos.add(cartDetailDto);
            }
        }

        return cartDetailDtos;
    }

    public Long orderCartItem(List<CartOrderDto> cartOrderDtoList, String email) {
        // 회원 정보 조회
        Member member = memberRepository.findByEmail(email);

        // 회원의 장바구니 정보 조회
        List<Cart> carts = cartRepository.findByMember(member);
        if (carts == null || carts.isEmpty()) {
            throw new RuntimeException("장바구니가 비어있습니다.");
        }

        // 주문(cart) 생성 및 회원과 연결
        Order order = new Order();
        order.setMember(member);
        orderRepository.save(order);

        // 카트 생성 (하나 이상의 장바구니가 있을 경우를 대비하여 List에서 하나 선택)
        Cart cart = carts.get(0);

        // cartOrderDtoList를 이용해 주문 상품(orderItems) 생성 및 주문(cart)에 연결
        for (CartOrderDto cartOrderDto : cartOrderDtoList) {
            // 해당 상품의 장바구니에서 정보 조회
            CartItem cartItem = cart.getCartItem(cartOrderDto.getCartItemId());
            if (cartItem == null) {
                throw new RuntimeException("장바구니에 해당 상품이 없습니다.");
            }

            // 주문 상품(orderItem) 생성 및 주문(cart)에 연결
            OrderItem orderItem = new OrderItem();
            orderItem.setItem(cartItem.getItem());
            orderItem.setCount(cartOrderDto.getCount());
            orderItem.setOrderPrice(cartItem.getItem().getPrice());
            orderItem.setOrder(order);
            orderItemRepository.save(orderItem);

            // 장바구니에서 해당 상품 제거
            cart.deleteCartItem(cartItem.getId());

        }

        // 주문이 완료되면 장바구니 저장
        cartRepository.save(cart);

        return order.getId();


    }


}
//
//    public void updateCartItemCount(Long cartItemId, int count){
//        //장바구니 상품 수량 변경
//    }
//
//    public void deleteCartItem(CartItem cartItems, Long cartItemId) {
//    // 장바구니에서 해당 상품 제거
//
//
//    }


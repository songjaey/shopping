package com.weapon.shop.service;

import com.weapon.shop.dto.CartDetailDto;
import com.weapon.shop.dto.CartItemDto;
import com.weapon.shop.dto.CartOrderDto;
import com.weapon.shop.dto.OrderDto;
import com.weapon.shop.entity.*;
import com.weapon.shop.repository.*;
//import com.weapon.shop.repository.CartRepository;
//import com.weapon.shop.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

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
        Cart cart = cartRepository.findByMemberId( member.getId() );

        if( cart==null) {
            cart = Cart.createCart(member);
            cartRepository.save(cart); // Cart를 먼저 저장하고 CartItem을 만들기 전에 저장된 것을 확인합니다.
        }

        CartItem cartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), item.getId());

        if( cartItem != null){
            cartItem.addCount(cartItemDto.getCount());
            return cartItem.getId();
        }else{
            // 없는 경우 새로운 카트 아이템을 생성합니다.
            CartItem newCartItem = CartItem.createCartItem(cart, item, cartItemDto.getCount());
            cartItemRepository.save(newCartItem);
            return newCartItem.getId();
        }

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

        List<OrderDto> orderDtos = new ArrayList<>();

        for( CartOrderDto cartOrderDto : cartOrderDtoList){
            CartItem cartItem = cartItemRepository.findById(cartOrderDto.getCartItemId()).orElseThrow(EntityNotFoundException::new);

            OrderDto orderDto = new OrderDto();
            orderDto.setItemId(cartItem.getItem().getId());
            orderDto.setCount(cartItem.getCount());
            orderDtos.add(orderDto);
        }

        Long orderId = orderService.orders(orderDtos, email);

        for (CartOrderDto cartOrderDto : cartOrderDtoList) {
            CartItem cartItem = cartItemRepository.findById(cartOrderDto.getCartItemId()).get();
            cartItemRepository.delete( cartItem);
        }

        return orderId;
    }

    public void deleteCartItem( Long cartItemId) {
        // 장바구니에서 해당 상품 제거
        CartItem cartItem = cartItemRepository.findById(cartItemId).get();
        cartItemRepository.delete(cartItem);
    }
}
//
//    public void updateCartItemCount(Long cartItemId, int count){
//        //장바구니 상품 수량 변경
//    }
//



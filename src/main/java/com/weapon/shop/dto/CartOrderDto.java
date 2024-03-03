package com.weapon.shop.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CartOrderDto {

    private Long cartItemId;

    private int count;

    private List<CartOrderDto> cartOrderDtoList;

//    public int getCount() {
//        return cartOrderDtoList.size(); // 예시로 장바구니 아이템 수를 반환하도록 설정함
//    }
}
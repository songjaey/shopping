package com.weapon.shop.control;

import com.weapon.shop.dto.CartDetailDto;
import com.weapon.shop.dto.CartItemDto;
import com.weapon.shop.dto.CartOrderDto;
import com.weapon.shop.dto.OrderDto;
import com.weapon.shop.entity.CartItem;
import com.weapon.shop.service.CartService;
import com.weapon.shop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/cart")
    public @ResponseBody ResponseEntity cartPut(@RequestBody @Valid CartItemDto cartItemDto,
                                                BindingResult bindingResult, Principal principal) {
    //장바구니 버튼클릭시
        if(bindingResult.hasErrors()){
            StringBuilder sb = new StringBuilder(); //json 응답은 문자열로 보내야해서
            List<FieldError> fieldErrors = bindingResult.getFieldErrors(); //유효성검사 오류 내용

            for( FieldError fieldError : fieldErrors){
                sb.append(fieldError.getDefaultMessage()); //json응답을 보내기위해 StringBuilder에저장
            }
            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }
        String email = principal.getName();
        Long cartId;
        try{
            cartId = cartService.addCart(cartItemDto, email);
        }catch(Exception e){
            System.out.println("카트 데이터베이스 저장 실패");
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        //return new ResponseEntity<Long>(cartId, HttpStatus.OK);
        return new ResponseEntity<Long>(1L, HttpStatus.OK);
    }

    @GetMapping(value = "/cart")
    public String orderHist(Principal principal, Model model) {
        //장바구니 메뉴 클릭시

        // 여기서 장바구니 아이템 정보를 가져와서 모델에 추가하는 작업을 수행하세요.
        // 예를 들어, CartService나 CartRepository 등을 사용하여 데이터를 조회할 수 있습니다.

        // 임시로 CartItem 목록을 만들어 모델에 추가하는 예시
        //List<CartItem> cartItems = // 장바구니 아이템 정보를 가져오는 로직 필요;
        // model.addAttribute("cartItems", cartItems);
        List<CartDetailDto> cartDetailDtos = cartService.getCartList(principal.getName());;
        model.addAttribute("cartItems", cartDetailDtos);


        return "cart/cartList";

    }

    @PatchMapping(value = "/cartItem/{cartItemId}")
    public @ResponseBody ResponseEntity updateCartItem(@PathVariable("cartItemId") Long cartItemId, int count, Principal principal) {
        // 상품 수량 변경
        return null;
    }
    @DeleteMapping(value = "/cartItem/{cartItemId}")
    public @ResponseBody ResponseEntity deleteCartItem(@PathVariable("cartItemId") Long cartItemId, Principal principal) {
        //장바구니 상품 삭제
        return null;
    }

    @PostMapping(value = "/cart/orders")
    public @ResponseBody ResponseEntity<String> orderCartItem(@RequestBody List<CartOrderDto> cartOrderDtoList, Principal principal) {
        // 장바구니에서 상품 주문
        String email = principal.getName();
        try {
            Long orderId = cartService.orderCartItem(cartOrderDtoList, email);
            System.out.println("ok");
            return new ResponseEntity<>("success", HttpStatus.OK);
        } catch (RuntimeException e) {
            e.printStackTrace(); // 예외 메시지 및 스택 트레이스 출력
            System.out.println("save failure");
            return new ResponseEntity<>("error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

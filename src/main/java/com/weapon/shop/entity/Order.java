package com.weapon.shop.entity;

import com.weapon.shop.constant.OrderStatus;
import lombok.Getter;
import lombok.Setter;
import org.aspectj.weaver.ast.Or;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@Table(name="orders")
public class Order extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="order_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL
            , orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems =new ArrayList<>();

    @Column
    private LocalDateTime orderDate; //주문일
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; //주문 상태

    public void addOrderItem(OrderItem orderItem){
        this.orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public static Order createOrder(Member member, List<OrderItem> orderItems){
        Order order = new Order();
        order.setMember(member);
        for(OrderItem orderItem : orderItems){
            order.addOrderItem(orderItem);
        }
        order.setOrderStatus(OrderStatus.ORDER);
        order.setOrderDate( LocalDateTime.now());
        return order;
    }
    public int getTotalPrice(){  //여러상품 구매시 전체 총 결제금액
        int total =0;
        for(OrderItem orderItem : orderItems){
            total += orderItem.getTotalPrice();
        }
        return total;
    }

    // 구매한 상품  구매취소
    public void cancelOrder(){
        this.orderStatus = OrderStatus.CANCEL;
        for(OrderItem orderItem:orderItems){ // 아이템 수량 원복
            orderItem.cancel();
        }
    }

}





/*
    1 정규화
     - 1. 각각의 컬럼이 하나의 속성만 가져야한다.
           num int default 23 ,
       2.  하나의 컬럼은 같은 종류나 타입을 가져야만한다.
       3. 각 컬럼이 유일한 이름을 가져야 한다.
       컬럼 순서는 무관하다.

       김민수   하나은행     111
       김민수   하나은행     333
       김민수   하나은행     444
       노재홍   신한은행    455
       인호성   농협      444
       인호성  농협       999


    1   김민수   하나은행
    2   노재홍   신한은행
    3   김민수   신한은행
    4   인호성   농협


       1   111
       1   333
       3   444
       2   455
       4   444
       4   999
 */
//package com.weapon.shop.repository;
//
//import com.querydsl.jpa.impl.JPAQuery;
//import com.querydsl.jpa.impl.JPAQueryFactory;
//import com.weapon.shop.constant.ItemSellStatus;
//import com.weapon.shop.entity.Item;
//import com.weapon.shop.entity.QItem;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.TestPropertySource;
//
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//
//@SpringBootTest
//@TestPropertySource(locations="classpath:application-test.properties")
//class ItemRepositoryTest {
//
//    @Autowired
//    ItemRepository itemRepository;
//
//    @PersistenceContext
//    EntityManager em;
//
//    @Test
//    @DisplayName("querydsl 테스트")
//    public void querydslTest(){
//        this.createItemList();
//        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
////        QItem qItem = QItem.item;
////        JPAQuery<Item> query = queryFactory.selectFrom(qItem)
////                .where(qItem.itemNm.eq("테스트 상품2"))
////                .orderBy(qItem.price.desc());
////        List<Item> itemList = query.fetch();
////
////        for(Item item : itemList)
////            System.out.println( item );
//
//        // select * from item where item_nm = '테스트 상품2' order by price desc
//        //  findByItemNmOrderByPriceDesc(String itemNm);
//    }
//
//
//
//    @Test
//    @DisplayName("================상품 한개 저장 테스트================")
//    public void createTest(){
////        Item item = new Item();
////        item.setItemNm("테스트 상품");
////        item.setPrice(10000);
////        item.setItemDetail("상품 상세 설명");
////        item.setItemSellStatus( ItemSellStatus.SELL);
////        item.setStockNumber(100);
////        item.setRegTime( LocalDateTime.now());
////        item.setUpdateTime( LocalDateTime.now());
////
////        Item saveItem = itemRepository.save( item );
////        System.out.println(  saveItem.toString() );
//    }
//
//
//    public void createItemList(){
//        for( int i=1; i<=10; i++) {
//            int a = (int)Math.floor(Math.random() * 5 )+1;
//            int b = (int)Math.floor(Math.random() * 5 )+1;
//
////            Item item = new Item();
////            item.setItemNm("테스트 상품"+a);
////            item.setPrice(10000*a);
////            item.setItemDetail("상품 상세 설명"+b);
////            item.setItemSellStatus(ItemSellStatus.SELL);
////            item.setStockNumber(100);
////            item.setRegTime(LocalDateTime.now());
////            item.setUpdateTime(LocalDateTime.now());
//
////            Item saveItem = itemRepository.save(item);
//        }
//    }
//
//
//    @Test
//    @DisplayName("상품명 조회 테스트")
//    public void findItemNm(){
//        this.createItemList();
//        List<Item> itemList = itemRepository.findByItemNm("테스트 상품3");
//        for(Item item : itemList)
//            System.out.println(item);
//        if( itemList.isEmpty() )
//            System.out.println("테스트 상품3 은 없습니다.");
//    }
//
//    @Test
//    @DisplayName("상품명 또는 상품상세 설명으로 검색")
//    public void itemNmOritemDetail(){
//        this.createItemList(); //10개 상품 등록
//        List<Item> itemList = itemRepository.findByItemNmOrItemDetail("테스트 상품2", "상품 상세 설명4");
//        for(Item item : itemList)
//            System.out.println( item );
//
//        if( itemList.isEmpty() )
//            System.out.println("결과 없음");
//
//    }
//
//    @Test
//    @DisplayName("상품가격 이하 조회")
//    public void lessPrice(){
//        this.createItemList();
//        List<Item> itemList = itemRepository.findByPriceLessThanEqualOrderByPriceDesc(30000);
//
//        for( Item item: itemList)
//            System.out.println( item );
//
//        if(itemList.isEmpty())
//            System.out.println("결과 없다");
//    }
//
//
//    @Test
//    @DisplayName("상품가격15000~35000 조회")
//    public void lessBetweenPrice(){
//        this.createItemList();
//        List<Item> itemList = itemRepository.findByPriceBetweenOrderByPriceDesc(15000,35000);
//
//        for( Item item: itemList)
//            System.out.println( item );
//
//        if(itemList.isEmpty())
//            System.out.println("결과 없다");
//    }
//
//
//    @Test
//    @DisplayName("상품상세 설명 JPQL 테스트")
//    public void itemDetailTest(){
//        this.createItemList();
//        List<Item> itemList = itemRepository.findByItemDetail("설명3");
//        for(Item item: itemList)
//            System.out.println( item );
//
//    }
//}
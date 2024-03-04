package com.weapon.shop.repository;

import com.weapon.shop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item,Long>,
        QuerydslPredicateExecutor<Item>, ItemRepositoryCustom {

    // 상품 이름으로 조회
    List<Item> findByItemNm(String itemNm);

    // 상품명, 상품상세설명 으로 검색
    List<Item> findByItemNmOrItemDetail(String itemNm, String itemDetail);

    List<Item> findByPriceLessThanEqualOrderByPriceDesc(Integer price);

    List<Item> findByPriceBetweenOrderByPriceDesc(Integer price1, Integer price2);

    // findByItemDetailLikeOrderByPriceDesc(String itemDetail)
    @Query("select i from Item i where i.itemDetail like %:itemDetail% order by i.price desc")
    List<Item> findByItemDetail(@Param("itemDetail") String itemDetail);

    @Query(value="select * from item where item_detail like %:itemDetail% order by price desc",
            nativeQuery = true)
    List<Item> findByItemDetailNative(@Param("itemDetail") String itemDetail);
}
/*
        JPA 와 JPQL의 단점은  에러 발견이 안된다.
        문자열로 쿼리를 입력하기 때문에 잘못입력하여도  컴파일 시점에서는 에러를 발견할 수 없다.
        그래서  단점을 보완 하고자 만들어진 녀석이   Querydsl 이다.

        Querydsl의 장점
        - 고정된 SQL문이 아닌 조건에 맞게 동적으로 쿼리를 생성할 수 있다.
        - 비슷한 쿼리를 재사용할 수 있으며 제약 조건 조립 및 가독성을 향상 시킬수 있다.
        - 문자열이 아닌 자바 소스코드로 작성하기 때문에 컴파일 시점에 오류를 발견 할 수 있다.
        - IDE의 도움으로 자동완성 기능 사용해서 생산성 향상 시킬 수 있다.




        @Query 애너테이션을 이용하여 sql과 유사한 JPQL이라는 객체지향 쿼리 언어를 통해
        복잡한 쿼리도 처리가 가능하다.
        파라미터(:itemDetail) 에  @Param 애너테이션을 이용하여 파라미터로 넘어온 값을 JPQL에
        들어갈 변수로 지정할 수 있다.
          : 는 변수앞에 붙여서 사용한다.
        @Query("select * from Item where price < :price")
        List<Item> priceThan(@Param("price") Integer price);
 */


// jpa의 네이밍규칙을 이용하여 메서드를 작성하면 원하는 쿼리를 실행 할수 있다.
// 데이터베이스에서 조회할 메서드 규치   find +By +변수이름
//    findByItemNm - 상품 이름으로 조회 하는 메서드

// 상품의 가격이  15000원에서  35000원 사이의 상품들만 조회 해서  상품가격 내림차순으로 가져오기


// 상품가격이 30000인 상품 조회
//  List<Item> findByPrice(int price);

/*
    keyword        sample                      query

    And            findByItemNmAndPrice        where item_nm='a' and price=1000
    Or             findByItemOrPrice           where item_nm='a' or price=1000
    Is, Equals     findByItemNm
                   findByItemNmIs              where item_nm='a'
                   findByItemNmEquals
    Between        findByRegDateBetween        where reg_date between 2024-01-01 and 2024-02-03
    LessThan       findByPriceLessThan         where price < 50000
    LessThanEqual  findByPriceLessThanEqual    where price <= 50000
    GreaterThan    findByPriceGreaterThan      where price > 20000
    GreaterThanEqual   findByPriceGreaterEqual  where price >= 20000
    After          findByRegDateAfter          where reg_date > 2024-01-01
    before         findByRegDateBefore         where reg_dae < 2024-02-15
    IsNull,Null    findByItemNmNull            where item_nm is null
    NotNull        findByItemNmNotNull         where item_nm not null
    Like           findByItemNmLike            where item_nm like '가방'
    StartingWith   findByItemNmStartingWith    where item_nm like '가방%'
    EndingWith     findByItemNmEndingWith      where item_nm like '%가방'
                   findByItemNmStartingWithIgnoreCase
    OrderBy        findByPriceOrderByRegDateDesc   where  price=3000 order by reg_date desc

 */
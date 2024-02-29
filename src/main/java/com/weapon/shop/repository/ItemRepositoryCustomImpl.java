package com.weapon.shop.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.weapon.shop.dto.ItemSearchDto;
import com.weapon.shop.dto.MainItemDto;
import com.weapon.shop.dto.QMainItemDto;
import com.weapon.shop.entity.Item;
import com.weapon.shop.entity.QItem;
import com.weapon.shop.entity.QItemImg;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;


public class ItemRepositoryCustomImpl implements ItemRepositoryCustom{
    private JPAQueryFactory jpaQueryFactory;

    public ItemRepositoryCustomImpl(EntityManager em){
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<MainItemDto> getMainItem(ItemSearchDto itemSearchDto, Pageable pageable){
        QItem item = QItem.item; //
        QItemImg itemImg = QItemImg.itemImg;

        List<MainItemDto> cont = jpaQueryFactory
                .select(
                        new QMainItemDto(item.id, item.itemNm,
                                item.itemDetail, itemImg.imgUrl,
                                item.price)
                )
                .from(itemImg)
                .join(itemImg.item, item)
                .where(itemImg.repImgYn.eq("Y"))
                .where(itemNmLike(itemSearchDto.getSearchQuery()))
                .orderBy(item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch(); //쿼리문 실행, 다수의 데이터 조회

        long total = jpaQueryFactory
                .select(Wildcard.count)
                .from(itemImg)
                .join(itemImg.item, item)
                .where(itemImg.repImgYn.eq("Y"))
                .where(itemNmLike(itemSearchDto.getSearchQuery()))
                .fetchOne(); // 단일 데이터 조회


        return new PageImpl<>(cont,pageable,total);
    }

    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        QItem item = QItem.item;

        // 페이지네이션 적용
        List<Item> cont = jpaQueryFactory
                .selectFrom(item)
                .orderBy(item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch(); // 쿼리 실행 및 페이징된 결과 집합 가져오기

        // 전체 아이템 수 카운트
        long total = jpaQueryFactory.select(Wildcard.count).from(item)
                .fetchOne();

        return new PageImpl<>(cont, pageable, total);
    }


    private BooleanExpression itemNmLike(String searchQuery){
        return StringUtils.isEmpty(searchQuery) ? null : QItem.item.itemNm.like("%"+searchQuery+"%");
    }

}

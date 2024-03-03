package com.weapon.shop.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.weapon.shop.constant.ItemSellStatus;
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
import java.time.LocalDateTime;
import java.util.List;

public class ItemRepositoryCustomImpl implements ItemRepositoryCustom{

    private JPAQueryFactory jpaQueryFactory;

    public ItemRepositoryCustomImpl(EntityManager em){
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }
    // 위의 두가지는querydsl 사용시 꼭 필요하다
    private BooleanExpression searchSellStatusEq(ItemSellStatus searchSellStatus){
        return searchSellStatus == null ? null : QItem.item.itemSellStatus.eq(searchSellStatus);
    }
    private BooleanExpression regDtsAfter(String searchDateType){

        LocalDateTime dateTime = LocalDateTime.now();

        if(StringUtils.equals("all", searchDateType) || searchDateType == null){
            return null;
        } else if(StringUtils.equals("1d", searchDateType)){
            dateTime = dateTime.minusDays(1);
        } else if(StringUtils.equals("1w", searchDateType)){
            dateTime = dateTime.minusWeeks(1);
        } else if(StringUtils.equals("1m", searchDateType)){
            dateTime = dateTime.minusMonths(1);
        } else if(StringUtils.equals("6m", searchDateType)){
            dateTime = dateTime.minusMonths(6);
        }

        return QItem.item.regTime.after(dateTime);
    }

    private BooleanExpression searchByLike(String searchBy, String searchQuery){
        if(StringUtils.equals("itemNm", searchBy)){
            return QItem.item.itemNm.like("%" + searchQuery + "%");
        } else if(StringUtils.equals("createdBy", searchBy)){
            return QItem.item.createdBy.like("%" + searchQuery + "%");
        }

        return null;
    }

    @Override
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {

        List<Item> content = jpaQueryFactory
                .selectFrom(QItem.item)
                .where(regDtsAfter(itemSearchDto.getSearchDateType()),
                        searchSellStatusEq(itemSearchDto.getItemSellStatus()),
                        searchByLike(itemSearchDto.getSearchBy(),
                                itemSearchDto.getSearchQuery()))
                .orderBy(QItem.item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = jpaQueryFactory.select(Wildcard.count).from(QItem.item)
                .where(regDtsAfter(itemSearchDto.getSearchDateType()),
                        searchSellStatusEq(itemSearchDto.getItemSellStatus()),
                        searchByLike(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery()))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }




    @Override
    public Page<MainItemDto> getMainItem(ItemSearchDto itemSearchDto, Pageable pageable) {

        QItem item = QItem.item; // querydsl작업위해 entity객체 필요
        QItemImg itemImg = QItemImg.itemImg;

        List<MainItemDto> cont = jpaQueryFactory  //데이터베이스 조회해서 MainItemDto객체에 저장하여 가져오기
                .select(
                        new QMainItemDto( item.id, item.itemNm,
                                item.itemDetail, itemImg.imgUrl,
                                item.price)
                ) // sql문의  select item.id, item.item_nm , item.item_detail, item_img.img_url, item.price
                .from(itemImg) // from은 조회할 테이블 지정 하는 부분
                .join(itemImg.item, item)// 테이블 관계에 따라 지정 되는 테이블이 다르다. join은 주테이블, from은 종테이블
                .where(itemImg.repImgYn.eq("Y")) //item_img테이블에서 대표이미지만 가져오기 이라는 조건
                .where(itemNmLike(itemSearchDto.getSearchQuery()))//검색어를 포함한 상품명 전부 조회
                .orderBy(item.id.desc())// 상품번호기준으로 내림차순 정렬
                .offset(pageable.getOffset())//페이징 시에 시작번호 지정, 한페이지에 5개씩이라면 두번째 페이지 조회시 offset은 5가된다.
                .limit(pageable.getPageSize())// 한페이지에 표시할 데이터 갯수
                .fetch(); // 쿼리문 실행 해라 , 다수의 데이터 조회시

        long total = jpaQueryFactory
                .select(Wildcard.count) // select COUNT( ) -> 조회한 데이터 총갯수 구하는함수
                .from(itemImg)
                .join(itemImg.item, item)
                .where(itemImg.repImgYn.eq("Y"))
                .where(itemNmLike(itemSearchDto.getSearchQuery()))
                .fetchOne(); // 단일 데이터 조회시

        return new PageImpl<>(cont, pageable, total);
    }

    private BooleanExpression itemNmLike(String searchQuery){
        return StringUtils.isEmpty(searchQuery) ? null : QItem.item.itemNm.like("%"+searchQuery+"%");
    }


}
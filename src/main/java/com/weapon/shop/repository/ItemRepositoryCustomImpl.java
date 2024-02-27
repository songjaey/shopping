package com.weapon.shop.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.weapon.shop.dto.ItemSearchDto;
import com.weapon.shop.dto.MainItemDto;
import com.weapon.shop.entity.QItem;
import com.weapon.shop.entity.QItemImg;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
                .where()
        return null;
    }

}

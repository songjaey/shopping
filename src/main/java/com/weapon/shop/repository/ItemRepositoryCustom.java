package com.weapon.shop.repository;

import com.weapon.shop.dto.ItemSearchDto;
import com.weapon.shop.dto.MainItemDto;
import com.weapon.shop.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepositoryCustom extends JpaRepository<Item, Long> {
    Page<MainItemDto> getMainItem(ItemSearchDto itemSearchDto, Pageable pageable);
}

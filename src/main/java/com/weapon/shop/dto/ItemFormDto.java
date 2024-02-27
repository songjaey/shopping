package com.weapon.shop.dto;

import com.weapon.shop.entity.Item;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ItemFormDto {
    private Long id;
    @NotBlank(message = "상품명은 필수 입력 값입니다.")
    private String itemNm;

    @NotNull(message="가격은 필수 입력 값이다.")
    private Integer price;

    @NotBlank(message = "상품 상세는 필수 입력값이다.")
    private String itemDetail;

    @NotNull(message = "가격은 필수 입력값이다.")
    private Integer stockNumber;

    private String itemSellStatus;

    private List<ItemImgDto> itemImgDtoList = new ArrayList<>();

    private List<Long> itemImgIds = new ArrayList<>();

    private LocalDateTime regTime;
    private LocalDateTime updateTime;

    private static ModelMapper mapper = new ModelMapper();
    public Item createItem(){   //ItemFormDto 객체 데이터를 -> Item 객체에 저장
        return mapper.map(this, Item.class);
    }

    public static ItemFormDto of(Item item){ //Item객체의 데이터를 -> ItemFormDto 객체에 저장
        return mapper.map(item, ItemFormDto.class);
    }
}

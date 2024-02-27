package com.weapon.shop.service;

import com.weapon.shop.dto.ItemFormDto;
import com.weapon.shop.dto.ItemSearchDto;
import com.weapon.shop.dto.MainItemDto;
import com.weapon.shop.entity.Item;
import com.weapon.shop.entity.ItemImg;
import com.weapon.shop.repository.ItemImgRepository;
import com.weapon.shop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final ItemImgRepository itemImgRepository;
    private final ItemImgService itemImgService;

    // 메인페이지에 출력할 상품들 가져오기( 페이징 - 5개씩 보여주기)
    @Transactional( readOnly = true)
    public Page<MainItemDto> getMainItem(ItemSearchDto itemSearchDto, Pageable pageable){
        return itemRepository.getMainItem(itemSearchDto, pageable);
    }






    public Long saveItem(ItemFormDto itemFormDto,
                         List<MultipartFile> multipartFileList) throws Exception{
        Item item = itemFormDto.createItem();
        itemRepository.save(item);
        for(int i=0; i<multipartFileList.size(); i++){
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);

            if(i==0)
                itemImg.setRepImgYn("Y");
            else
                itemImg.setRepImgYn("N");
            itemImgService.saveItemImg(itemImg, multipartFileList.get(i));
        }
        return item.getId();
    }

}

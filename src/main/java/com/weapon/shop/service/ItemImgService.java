package com.weapon.shop.service;

import com.weapon.shop.entity.ItemImg;
import com.weapon.shop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemImgService {

    @Value("${itemImgLocation}")
    private String imgLocation;

    private final ItemRepository itemRepository;

    private final FileService  fileService;

    public void saveItemImg(ItemImg itemImg, MultipartFile multipartFile) throws Exception{
        String oriName = multipartFile.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";

        if( !StringUtils.isEmpty(oriName)){

        }
    }
}

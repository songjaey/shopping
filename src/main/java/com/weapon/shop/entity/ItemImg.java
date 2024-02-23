package com.weapon.shop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
public class ItemImg {
    @Id
    @Column(name="item_img_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String imgName;

    @Column
    private String oriImgName;

    @Column
    private String imgUrl;

    @Column
    private String repImgYn;

    @Column
    private LocalDateTime regDate;

    @Column
    private LocalDateTime updateDate;

    @ManyToOne
    @JoinColumn(name="item_id")
    private Item item;

}

package com.weapon.shop.entity;

import com.weapon.shop.constant.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.bytebuddy.asm.Advice;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@ToString
public class Item extends BaseEntity{

    @Id
    @Column(name="item_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable= false, length = 50)
    private String itemNm;

    @Column(nullable= false)
    private int price;

    @Column(nullable= false)
    private int stockNumber;

    @Type( type = "org.hibernate.type.TextType")
    @Column(nullable = false)
    private String itemDetail;

    @Enumerated( EnumType.STRING )
    private ItemSellStatus itemSellStatus;
}

/*
    @Entity - 클래스를 엔티티로 선언하겠다.
    @Table - 엔티티와 매핑할 테이블 이름 설정 @Table(name="item_s")

    @Id = 테이블의 기본키(primary key) 지정
    @GeneratedValue = 기본키의 값을 생성하는 방법 명시
                      GenerationType.IDENTITY : 기본키 생성(기본값 생성)
                      GenerationType.AUTO : 자동증가
                      GenerationType.SEQUENCE : 데이터 베이스 시퀀스 오브젝트를 이용한 값생성
    @Column - 필드와 테이블의 컬럼 매핑
            @Column(속성 = )
            속성들
            name : 필드와 매핑할 컬럼의 이름 설정
            unique : 유니크 제약 조건 설정
            insertable : insert 가능 여부
            updateable : update 가능 여부
            nullable : null값 허용 여부
            columnDefinition : 데이터베이스 컬럼 정보 직접 기술
                @Column(columnDefinition = "name varchar(30) not null ")
            percision, scale : BigDecimal 타입에서 사용, percision은 소수점 포함 전체 자리수,
                               scale은 소수점 자리수

    @Lob - BLOB, CLOB
           BLOB : 인제이터를 저장, 이미지나 파일 저장
           CLOB : 문자열이나 텍스트 데이터 저장

    @CreationTimestamp - insert 시 시간 자동 저장
    @UpdateTImestamp - update 시 시간 자동저장
    @Enumverated - enum 타입 매핑
    @Transient - 해당 필드 데이터베스 매핑 무시
    @Temporal - 날짜 타입 매핑
    @CreateDate -엔티티가 생성되어 저장될 대 시간 자동저장
    @LastModifiedDate - 조회한 엔티티의 값을 변경할 대 시간 자동 저장

 */

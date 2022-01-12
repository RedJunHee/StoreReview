package com.review.storereview.common.enumerate;


import lombok.Getter;

/** Class       : Authority (Enum)
 *  Author      : 조 준 희
 *  Description : 유저의 권한 관리
 *  History     : [2022-01-11] - TEMP
 */
@Getter
public enum Authority {
    USER("USER","ROLE_USER"),ADMIN("ADMIN","ROLE_ADMIN"), TESTER("TESTER","ROLE_TESTER");

    //Enum 필드
    private String name;
    private String fullName;

    //Enum 생성자
    Authority( String name, String fullName) {
        this.name = name;
        this.fullName = fullName;
    }


}

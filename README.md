# StoreReview
리뷰 서비스를 제공하는 웹 사이트 개발 프로젝트입니다. 😊

[여기 모아](https://store-review.nextwing.me/)

## 팀원 🧑🏻‍🤝‍🧑🏻
- Front-End : [이수경](https://github.com/ssssksss), [박재현](https://github.com/ghtea)
- Back-End : [문윤지](https://github.com/BananMoon), [조준희](https://github.com/RedJunHee)

## 주요 기능 💃
- 이미지 업로드, 별점, 댓글, 로그인, 회원가입, 검색, 가게 정보 조회

## 기술 스택🔧

### Front-End
- **Library**
- react
  - redux
  - redux-saga
  - react-router-dom
- HTML5
- CSS, styled-component
- Javascript, Typescript

### Back-End
- **Server**
    - Ubuntu 20.x
    - Docker Container ( CentOS 7 )
    - Ley's Encrypt SSL Protocol
- **FrameWork**
    - Java 8
    - Spring Boot 2.5.x
    - Spring Data JPA
- **Build Tool**
    - Gradle
- **DataBase**
    - MySQL
- **Infra**
    - AWS EC2
    - AWS RDS
    - AWS S3
    
### Cooperation Tool
- **버전 관리**
    - Git(Pull Request Feedback Cycle)
- **의견 공유 및 정리**
    - Discord
    - Notion
    - Figma (프로토타입)

## 해결 과제 (Back-End) 🤹
- [X] Server 세팅
- [X] Cors 정책 허용
- [X] SSL Protocol 세팅
- [X] [api 규약 정의](https://docs.google.com/document/d/1JvENVWph2QBL9mxwRsd1sTEhiNriD3MX/edit?usp=sharing&ouid=116446426306038263641&rtpof=true&sd=true)
- [X] DB 설계 & ERD 작성
- [X] 프로젝트 세팅
- [X] model 정의 및 MySQL 연동
- [ ] 기능 구현
    - [X] Spring Security JWT 인증/인가 기능 구현
    - [X] AES256, BCrypt, Base64 Tool 개발
    - [ ] 회원가입 
    - [X] 로그인 (Authenticate) JWT 발급 
    - [X] 리뷰 코멘트 조회 (리뷰에 달린 모든 코멘트 + Paging) 
    - [X] 리뷰 코멘트 작성 
    - [X] 리뷰 코멘트 수정 (리뷰 사용자 체크 후 수정 기능 수행)
    - [X] 리뷰 코멘트 삭제 (리뷰 사용자 체크 후 삭제 기능 수행)

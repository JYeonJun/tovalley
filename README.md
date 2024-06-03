<span style="margin-left: 10px;"><h1><img src="https://github.com/illdang100/tovalley/assets/97449471/0df8fe51-b4b5-49d7-b0f0-3568156184a1" width="30" height="auto" align="left" />투계곡(ToValley) 프로젝트</h1></span>

<p align="center"><img src="https://github.com/illdang100/molly/assets/77067383/a005dbdd-79ac-4bdc-b94c-2ecec6fa4400" style="width:70%;"/></p>

## 💡 프로젝트 소개
투계곡 프로젝트는 물놀이 장소에서 발생할 수 있는 인명사고를 예방하는 것을 목표로 합니다.<br/>
사용자들은 물놀이 장소의 안전 정보를 확인할 수 있으며, 또한 리뷰, 커뮤니티, 채팅 기능을 통해 다른 사용자들과 자신의 경험을 공유할 수 있습니다.<br/>
이를 통해 사용자들은 보다 안전한 물놀이 환경을 조성하고, 위험을 사전에 예방할 수 있습니다.

<ul>
   <li>프로젝트 기간: 2023.09 ~ 2024.03</li>
   <li>웹사이트 방문: <a href="https://tovalley.site/">바로가기</a></li>
   <li>데모 영상: <a href="https://www.youtube.com/watch?v=qlYggbJ5q3I">링크</a></li>
</ul>

## 🚀 시스템 구조
<p align="center"><img src="https://github.com/illdang100/tovalley/assets/97449471/cdd7512c-031a-495c-b7cf-24ccaf2e9941" width=65% /></p>

- **Nginx**: 정적 자원을 서비스하고 클라이언트의 요청을 적절한 서버로 전달하는 역할을 담당합니다.
- **MariaDB**: 사용자 데이터 및 트랜잭션 데이터와 같은 구조화된 데이터의 관리를 담당합니다.
- **Redis**: 사용자 인증 과정에서 발급되는 리프래시(refresh) 토큰을 저장합니다.
- **MongoDB**: 채팅 메시지 정보와 같은 비구조화된 데이터를 저장하는 데 사용됩니다.
- **Kafka**: 실시간 채팅 메시지의 비동기 처리 및 분선 저장을 담당합니다.

### 개발 환경
- 프로그래밍 언어: Java 11
- 빌드 툴: Gradle
- 프레임워크: SpringBoot 2.7.14

### 기술 스택

**프레임워크 및 라이브러리**
<div>
  <span><img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=flat-square&logo=springboot&logoColor=white"/></span>
  <span><img src="https://img.shields.io/badge/Spring Security-6DB33F?style=flat-square&logo=springsecurity&logoColor=white"/></span>
  <span><img src="https://img.shields.io/badge/JPA-007396?style=flat-square&logo=java&logoColor=white"/></span>
  <span><img src="https://img.shields.io/badge/QueryDSL-007396?style=flat-square&logo=java&logoColor=white"/></span>
  <span><img src="https://img.shields.io/badge/React-61DAFB?style=flat-square&logo=react&logoColor=black"/></span>
  <span><img src="https://img.shields.io/badge/TypeScript-3178C6?style=flat-square&logo=typescript&logoColor=white"/></span>
  <span><img src="https://img.shields.io/badge/CSS Module-000000?style=flat-square&logo=css3&logoColor=white"/></span>
  <span><img src="https://img.shields.io/badge/Styled--Component-DB7093?style=flat-square&logo=styled-components&logoColor=white"/></span>
</div>

**데이터베이스 및 캐싱**
<div>
  <span><img src="https://img.shields.io/badge/MariaDB-003545?style=flat-square&logo=mariadb&logoColor=white"/></span>
  <span><img src="https://img.shields.io/badge/MongoDB-47A248?style=flat-square&logo=mongodb&logoColor=white"/></span>
  <span><img src="https://img.shields.io/badge/Redis-DC382D?style=flat-square&logo=redis&logoColor=white"/></span>
</div>

**인프라 및 배포**
<div>
  <span><img src="https://img.shields.io/badge/Docker-2496ED?style=flat-square&logo=docker&logoColor=white"/></span>
  <span><img src="https://img.shields.io/badge/Github Actions-2088FF?style=flat-square&logo=githubactions&logoColor=white"/></span>
  <span><img src="https://img.shields.io/badge/AWS EC2-FF9900?style=flat-square&logo=amazonec2&logoColor=white"/></span>
  <span><img src="https://img.shields.io/badge/AWS RDS-527FFF?style=flat-square&logo=amazonrds&logoColor=white"/></span>
  <span><img src="https://img.shields.io/badge/AWS S3-569A31?style=flat-square&logo=amazons3&logoColor=white"/></span>
  <span><img src="https://img.shields.io/badge/Nginx-009639?style=flat-square&logo=nginx&logoColor=white"/></span>
</div>

**템플릿 엔진 및 API 도구**
<div>
  <span><img src="https://img.shields.io/badge/Thymeleaf-005F0F?style=flat-square&logo=thymeleaf&logoColor=white"/></span>
  <span><img src="https://img.shields.io/badge/Swagger-85EA2D?style=flat-square&logo=swagger&logoColor=black"/></span>
</div>

**테스트 도구**
<div>
  <span><img src="https://img.shields.io/badge/JUnit-25A162?style=flat-square&logo=junit5&logoColor=white"/></span>
</div>

**메시징 시스템**
<div>
  <span><img src="https://img.shields.io/badge/Kafka-231F20?style=flat-square&logo=apachekafka&logoColor=white"/></span>
</div>

## 🌤️ ERD
<details>
<summary>ERD 이미지</summary>
<div markdown="1">

<p align="center"><img src="https://github.com/illdang100/tovalley/assets/97449471/0f0ad225-0c7c-4cf0-b0f5-319791817328" width=95% /></p>

</div>
</details>

다음과 같이 아이콘을 추가하고, 소제목과 내용을 적절히 수정하였습니다:

## 📌 주요 기능

### 🔐 회원가입 및 로그인
- 사용자 편의성 증대를 위한 다양한 로그인 (이메일, 카카오, 구글, 네이버) 지원

<details>
<summary>회원가입 및 로그인 GIF 보기</summary>
<div markdown="1">
<p align="center"><img src="https://github.com/illdang100/tovalley/assets/97449471/eb7e543a-ac36-43a7-8a2b-e934b7f47f92" width=90%></p>
<p align="center"><img src="https://github.com/illdang100/tovalley/assets/97449471/48b25f4d-daa0-43ab-bfe4-63aee84d9a35" width=90%></p>
<p align="center"><img src="https://github.com/illdang100/tovalley/assets/97449471/fb8357a5-ef0f-46ad-b6bf-066bd48fc187" width=90%></p>

</div>
</details>

### 🏠 홈페이지
- 전국 날씨 지도, 특보 정보, 물놀이 사고 발생 수 그래프, 인기 물놀이 장소 목록 등의 정보 제공

<details>
<summary>홈페이지 GIF 보기</summary>
<div markdown="1">
<p align="center"><img src="https://github.com/illdang100/tovalley/assets/97449471/762fb2a3-1f3a-4553-9999-921b67a87980" width=90% /></p>
</div>
</details>

### 🏊 물놀이 장소 상세 정보
- 물놀이 장소, 주변 의료시설의 위치, 날씨 정보, 수질 정보, 혼잡도 정보 및 사용자 리뷰 제공

<details>
<summary>물놀이 장소 상세 정보 페이지 GIF 보기</summary>
<div markdown="1">
<p align="center"><img src="https://github.com/illdang100/tovalley/assets/97449471/9ea1b3ad-9322-4473-886f-14a0014336b9" width=90% /></p>
</div>
</details>

### 🔍 분실물 커뮤니티
- 분실물과 관련된 글을 카테고리별로 등록하고 조회할 수 있는 커뮤니티 페이지 제공

<details>
<summary>분실물 커뮤니티 페이지 GIF 보기</summary>
<div markdown="1">
<p align="center"><img src="https://github.com/illdang100/tovalley/assets/97449471/2d239399-92e4-40ee-805e-88d151282f5b" width=90% /></p>
<p align="center"><img src="https://github.com/illdang100/tovalley/assets/97449471/321047f0-8161-4834-9a72-82c50f417d96" width=90% /></p>
</div>
</details>

### 💬 실시간 채팅 및 알림
- WebSocket, STOMP, Kafka, MongoDB, Redis, MySQL를 사용한 실시간 채팅 및 알림 기능을 제공
  - **Kafka**: 메시지 브로커로 사용하여 채팅 및 알림 기능의 성능을 개선
  - **MongoDB**: 채팅 메시지 저장, 유연한 데이터 저장 및 빠른 데이터 조회
  - **Redis**: 채팅방 입장 상태와 같이 빈번하게 조회되는 데이터의 캐싱을 위해 사용
  - **MySQL**: 채팅방 목록이나 채팅 알림 정보와 함께 사용자 정보를 한 번에 조회하기 위해 사용

<details>
<summary>실시간 채팅 GIF 보기</summary>
<div markdown="1">
<p align="center"><img src="https://github.com/illdang100/tovalley/assets/97449471/e48ce36c-d404-4010-8839-8c3b9fd8ec94" width=90% /></p>
</div>
</details>

### 📊 관리자 페이지(Thymeleaf)
- 물놀이 장소 등록 및 정보 수정, 사고 내역 관리 등의 기능 제공

<details>
<summary>관리자 페이지 GIF 보기</summary>
<div markdown="1">

<p align="center"><img src="https://github.com/illdang100/tovalley/assets/97449471/4f1e713f-bd3b-4a00-b780-aa47ecc17e8a" width=90%></p>
<p align="center"><img src="https://github.com/illdang100/tovalley/assets/97449471/83dfe899-1df9-4c33-99db-899db4561419" width=90%></p>
<p align="center"><img src="https://github.com/illdang100/tovalley/assets/97449471/21f53df1-225b-4ba5-b6d6-06080726348b" width=90%></p>
<p align="center"><img src="https://github.com/illdang100/tovalley/assets/97449471/b2b63344-b26f-4b03-91b7-a05f45911471" width="90%"></p>

</div>
</details>

## 🖨️ Api 명세서
<details>
<summary>API 명세서 이미지</summary>
<div markdown="1">

<p align="center"><img src="https://github.com/illdang100/tovalley/assets/97449471/a6608f7c-37ba-4a43-82bd-b3b6f277fe18" width=90% /></p>

</div>
</details>

## 📈**성능 최적화 및 트러블 슈팅**
프로젝트 진행 과정에서 발생한 성능 최적화 및 트러블 슈팅 사례를 공유합니다.<br/>
### 채팅 및 알림 기능 성능 개선 ([자세히 보기](https://yenjjun187.tistory.com/1003))
- **String** : 시간대 변환 후 저장 -> **LocalDateTime**: 저장 후 시간대 변환
- **Kafka 사용**
  - 채팅 및 알림 토픽 각각을 **2개의 파티션**으로 분할하고 **2개의 컨슈머** 할당
    - **fetch.min.bytes**와 **fetch.max.wait.ms** 설정 최적화
    - TPS: **144.7** -> **638.4** (**약 341.2% 성능 개선**)

### 채팅 메시지 순서 불일치 문제 해결
- 원인: 채팅 토픽의 **파티션 분할**로 인한 메시지 순서 불일치 문제
- 해결: **키 기반 파티셔닝** 도입(채팅방 ID를 키로하여 동일 채팅방의 메시지를 같은 파티션으로 전송되도록 구현)

### 웹소켓 통신 과정에서 사용자 토큰 검증
- 원인: http-only 쿠키에 저장된 사용자 토큰 검증 필요
- 해결: **HttpHandshakeInterceptor**와 **웹소켓 세션**을 통한 사용자 토큰 검증 및 정보 관리

### 커서 기반 페이지네이션을 사용한 중복 데이터 조회 문제 해결
- 원인: 오프셋 기반 실시간 채팅 메시지 목록 조회 -> 중복 데이터 조회
- 해결: **커서(채팅 메시지 ID) 기반 페이지네이션** 방식 적용

## 👍기술적 의사 결정
### 채팅 기능 3개의 데이터베이스 선택 및 사용 이유 ([자세히 보기](https://yenjjun187.tistory.com/996))
- **MongoDB**: 채팅 메시지의 데이터 구조 변화의 유연성, 빠른 데이터 조회
- **Redis**: 채팅방 입장 상태의 빈번한 데이터 조회
- **MySQL**: 채팅방 및 채팅 알림 조회 시 사용자 정보도 함께 조회 가능

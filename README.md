# Reservation API Project

# 개발환경 :heavy_check_mark:
- 운영체제: window
- 통합개발환경(IDE) : IntelliJ
- JAVA Version : JDK 21
- SpringBoot Version : 3.2.3
- Data base : MYSQL
- build tool : Gradle


# 프로젝트 설명 :heavy_check_mark:
- 스프링 부트를 이용한 API 입니다
- JWT 토큰과 Spring-security를 이용하여 인증과 권한에 대해 다뤘습니다
- JWT 토큰은 누구나 디코딩이 가능하기때문에 
- swagger를 활용해 보다 더 쉽게 API를 볼 수 있게 했습니다
- JPA를 활용한 CRUD에 대해 다뤘습니다
- feign을 활용하여 mailgun을 호출해, 이메일에 인증메일을 받을 수 있게 했습니다

# 비즈니스 구현 모델 사용도

![Untitled](https://github.com/karpei-taemukan/Reservation/assets/91212680/200ad2c7-ba92-4b1c-bde6-cafc7c299c3a)

 
# 주의사항
:triangular_flag_on_post: 만약 한글로 json 요청을 준다면 DB 콘솔창에 다음과 같이 한글을 인식할 수 있도록 utf8 설정을 합니다 <br>
alter table reservation.customer convert to charset utf8;<br>
alter table reservation.seller convert to charset utf8;<br>
alter table reservation.store convert to charset utf8;<br>
alter table reservation.review convert to charset utf8;<br>
alter table reservation.admin convert to charset utf8;<br>


# API 기능 :heavy_check_mark:

  :white_check_mark: 기본 권한 및 인증 처리 필수 구현(로그인, 권한 체크)
  <br>
:white_check_mark: 시나리오 기능 구현 평가
 <br>
:white_check_mark: 회원 가입 기능 구현 여부(매장 점장 및 사용자)
 <br>
 :white_check_mark: 매장 등록/수정/삭제 기능 구현 여부
  <br>
:white_check_mark: 예약 진행(예약 가능 여부 확인 후 예약 진행)
 <br>
:white_check_mark:  도착 확인 기능 구현(유효성 필수 체크)
 <br>
:white_check_mark:  예약 이용 후 리뷰 작성 기능 구현(예약자인지 확인 및 작성, 수정, 삭제 모두 구현, 작성,수정은 작성자만 삭제는 작성자와 관리자 가능

# ERD
![Reservation-ERD](https://github.com/karpei-taemukan/Reservation/assets/91212680/8112189a-eaab-4619-abaa-4b5ffa668f25)

# Swagger 페이지 
:arrow_forward: (spring boot 3 버전 이후는 swagger를 지원하던 springfox가 더이상 동작하지않아, springdoc으로 대체되었습니다)
<br>
:arrow_forward:  ~~ /swagger-ui/index.html/ 로 접근해야 swagger 페이지가 렌더링됩니다 

![sw](https://github.com/karpei-taemukan/Reservation/assets/91212680/d1ded97d-53ab-40fa-981e-7f78cc6b0b76)


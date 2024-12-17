# 소셜 로그인 예제 프로젝트 사용 방법

1. 프로젝트에서 사용 중인 스키마와 테이블 구조를 위해 아래 쿼리를 실행하여 스키마 하나와 테이블 두 개를 생성합니다.
   > ```mariadb
   > CREATE SCHEMA `example`;
   > 
   > CREATE TABLE `example`.`social_types`
   > (
   >   `code` VARCHAR(10) NOT NULL,
   >   `text` VARCHAR(50) NOT NULL,
   >   CONSTRAINT PRIMARY KEY (`code`)
   > );
   > 
   > INSERT INTO `example`.`social_types` (`code`, `text`)
   > VALUES ('KAKAO', '카카오'),
   >        ('NAVER', '네이버'); 
   > 
   > CREATE TABLE `example`.`users`
   > (
   >   `email`            VARCHAR(50)  NOT NULL,
   >   `password`         VARCHAR(250) NOT NULL,
   >   `nickname`         VARCHAR(10)  NOT NULL,
   >   `created_at`       DATETIME     NOT NULL DEFAULT NOW(),
   >   `social_type_code` VARCHAR(10)  NULL     DEFAULT NULL,
   >   `social_id`        VARCHAR(50)  NULL     DEFAULT NULL,
   >   CONSTRAINT PRIMARY KEY (`email`),
   >   CONSTRAINT FOREIGN KEY (`social_type_code`) REFERENCES `example`.`social_types` (`code`)
   >       ON DELETE SET NULL
   >       ON UPDATE CASCADE,
   >   CONSTRAINT UNIQUE (`social_type_code`, `social_id`)
   > );
   > ```

2. 카카오 개발자 페이지(`developers.kakao.com`)에 접속하여 _카카오 로그인_ 기능을 활성화 하고, `Rest API 키`를 확보하고, `Redirect URI`를 아래와 같이 등록합니다.
   > ```http://localhost:8080/user/login/kakao```
   > - 위 주소는 프로젝트의 매핑에 따라 달라지나 예제 프로젝트는 위 주소로 등록되어 있음으로 일단은 동일하게 설정합니다.

3. 네이버 개발자 페이지(`developers.naver.com`)에 접속하여 _네이버 로그인_ 기능을 신청하고, `Client ID`, `Client Secret`을 확보하고, `Redirect URI`를 아래와 같이 등록합니다.
   > ```http://localhost:8080/user/login/naver```
   > - 위 주소는 프로젝트의 매핑에 따라 달라지나 예제 프로젝트는 위 주소로 등록되어 있음으로 일단은 동일하게 설정합니다.

4. 내려 받은 프로젝트의 `application.properties` 파일을 열어 아래 명시된 항목을 개인의 상황에 맞게 수정합니다.
   - `spring.datasource.driver-class-name`
   - `spring.datasource.url`
   - `spring.datasource.username`
   - `spring.datasource.password`
   - `custom.property.kakao-client-id`: 위 `2`번에서 확보한 `Rest API 키`입니다.
   - `custom.property.kakao-redirect-uri`: 위 `2`번에서 설정한 `Redirect URI`입니다.
   - `custom.property.naver-client-id`: 위 `3`번에서 확보한 `Client Key`입니다.
   - `custom.property.naver-client-secret`: 위 `3`번에서 확보한 `Client Secret`입니다.
   - `custom.property.naver-redirect-uri`: 위 `3`번에서 설정한 `Redirect URI`입니다.
   - 위 설정 중 `custom`으로 시작하는 설정들은 실제로 존재하는 설정들은 아니며 `CustomPropertyConfig` 클래스에서 직접 등록한 설정입니다.

# 소셜 로그인의 흐름

1. 사용자가 로컬 호스트의 로그인 페이지에서 각 소셜 로그인 링크를 클릭하면 각 소셜 로그인을 제공하는 사이트(카카오나 네이버 등)으로 이동합니다.
2. 각 소셜 로그인을 제공하는 사이트에서 로그인이 되어 있지 않다면 로그인한 뒤, 해당 사이트의 개인 정보를 우리(로컬 호스트)에게 제공하는 부분에 대해 동의하는가에 대해 물어본 뒤, 동의 한다면 우리가 넘겨준 리다이렉트 주소(`Redirect URI`)로 이동시켜 줍니다.
3. 리디렉션이 발생할 때, 사용자가 소셜 로그인을 시도했음을 증명하는 임시 코드를 발급하는데, 해당 코드를 이용하여 소셜 로그인을 제공하는 서버에 로컬 호스트에서 해당 코드와 함께 접근 토큰(Access Token) 발급 요청을 전송합니다.
4. 접근 토큰이 발급되면, 접근 토큰과 함께 해당 사용자의 고유 아이디를 응답으로 반환 받을 수 있는 요청을 재전송하여, 해당 사용자의 고유 식별자를 응답으로 반환 받습니다.
5. 해당 사용자의 고유 식별자를 반환 받아, 해당 사용자가 우리 서비스에 가입한 적이 있는가의 여부를 판단하여, 가입한 적이 없다면 가입을 진행 시키고, 가입한 적이 있다면, 별도의 이메일이나 비밀번호를 입력 받을 필요 없이 로그인 처리시켜 주는 절차를 밟게 됩니다.
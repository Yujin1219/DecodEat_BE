# DecodEat - Backend Server

[![cicd](https://github.com/DecodEat/BE-SpringBoot/actions/workflows/workflow.yml/badge.svg)](https://github.com/DecodEat/BE-SpringBoot/actions/workflows/workflow.yml)

DecodEat은 식품 정보를 분석하고 사용자 맞춤 추천을 제공하는 서비스입니다. 이 프로젝트는 해당 서비스의 백엔드 서버입니다.

## 🛠️ 주요 기술 스택

| 구분 | 기술 |
| --- | --- |
| **Framework** | Spring Boot 3.2.5 |
| **Language** | Java 17 |
| **Build Tool** | Gradle 8.7 |
| **Database** | Spring Data JPA, MYSQL |
| **Authentication** | Spring Security, JWT, OAuth 2.0 |
| **API Documentation** | Swagger (Springdoc OpenAPI) |
| **Cloud Service** | AWS S3 |
| **Containerization** | Docker, Docker Compose |
| **CI/CD** | GitHub Actions |

## ✨ 주요 기능

### 1. **사용자 관리 및 인증**
- OAuth 2.0을 연동하여 Kakao 소셜 로그인을 지원합니다.
- Refresh Token을 이용한 토큰 재발급 로직을 구현하여 사용자 편의성을 높였습니다.
<img width="2940" height="1602" alt="image" src="https://github.com/user-attachments/assets/37822d0d-264e-43de-a8b1-e64b596e9dc4" />

### 2. **상품(식품) 정보 관리**
- 사용자가 직접 상품 정보를 등록하고, 이미지(원재료, 영양정보표)를 S3에 업로드할 수 있습니다.
- 상품명, 카테고리 등 다양한 조건으로 상품을 검색하고 필터링하는 기능을 제공합니다.
- 상품 상세 정보 조회, 좋아요 기능을 제공합니다.
<img width="2940" height="2115" alt="image" src="https://github.com/user-attachments/assets/84d55561-40c4-4c8f-9749-feddb53ece51" />
<img width="1066" height="683" alt="image" src="https://github.com/user-attachments/assets/aef297ca-d1bf-45ba-9740-88c2949f6948" />

### 3. **영양 정보 분석 및 추천**
- 외부 Python 분석 서버와 비동기 통신(WebClient)하여 상품의 영양 정보를 분석합니다.
- 사용자 기반 및 상품 기반의 추천 알고리즘을 통해 개인화된 상품 추천 목록을 제공합니다.
<img width="1019" height="704" alt="image" src="https://github.com/user-attachments/assets/c85aefa1-f484-48ac-8915-50372f80886b" />

### 4. **오류 제보 및 관리**
- 사용자는 등록된 상품의 영양 정보나 이미지에 대한 오류를 제보할 수 있습니다.
- 관리자는 제보된 내용을 확인하고, 상품 정보를 수정하거나 제보를 처리할 수 있습니다.
<img width="784" height="579" alt="image" src="https://github.com/user-attachments/assets/60d0c0a9-5440-4814-9e5e-86982c19b51a" />

### 5. **API 및 예외 처리**
- 표준화된 API 응답 형식을(`ApiResponse`) 사용하여 클라이언트와의 통신 효율성을 높였습니다.
- `ErrorStatus`를 통해 예외 상황을 체계적으로 관리하고, `ExceptionAdvice`에서 공통으로 처리합니다.

## 🚀 시작하기

### **Prerequisites**
- Java 17
- Gradle 8.7
- Docker (선택 사항)

### **Installation & Run**
1. **Git Clone**
   ```bash
   git clone https://github.com/DecodEat/BE-SpringBoot.git
   cd BE-SpringBoot
   ```

2. **`application.yml` 설정**
   `src/main/resources/` 경로에 `application.yml` 파일을 생성하고 데이터베이스, JWT, OAuth, AWS S3 관련 설정값을 입력해야 합니다.

3. **애플리케이션 실행**
   ```bash
   ./gradlew bootRun
   ```

## 📝 API 문서

서버 실행 후, 아래 URL을 통해 API 명세를 확인할 수 있습니다.
- **Swagger UI:** [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
<img width="1434" height="287" alt="image" src="https://github.com/user-attachments/assets/8af2a117-f8f7-4710-8245-2f757e7d9641" />
<img width="1434" height="657" alt="image" src="https://github.com/user-attachments/assets/7da3b2ca-9296-4fd9-893f-e91cc14df378" />
<img width="1434" height="432" alt="image" src="https://github.com/user-attachments/assets/4e53f711-42e9-4cac-a325-1ead3ccb9dcd" />  


## 📁 프로젝트 구조

```
.
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── DecodEat
│   │   │           ├── DecodEatApplication.java
│   │   │           ├── domain                # 비즈니스 로직 (하위 도메인으로 분리)
│   │   │           │   ├── products          # 상품
│   │   │           │   ├── users             # 사용자
│   │   │           │   ├── report            # 신고
│   │   │           │   └── refreshToken      # 리프레시 토큰
│   │   │           └── global                # 공통 모듈
│   │   │               ├── apiPayload        # 공통 응답/예외 처리
│   │   │               ├── aws               # AWS 관련 유틸
│   │   │               ├── config            # Spring 설정 (Security, Swagger 등)
│   │   │               └── exception         # 전역 예외 핸들링
│   │   └── resources
│   │       ├── application.yml
│   └── test
...
```

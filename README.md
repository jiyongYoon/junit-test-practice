## 1. Spring MVC에서 Junit Test의 순서
`Repository` -> `Service` -> `Controller`

## 2. 각 레이어 별 Test의 목적(대상)
### 1) Repository
- DB쪽 관련 테스트

### 2) Service
- 기능들이 Transaction 순서에 맞게 잘 타는지

### 3) Controller
- 클라이언트와의 테스트 (ex, Null 체크)

## 3. Junit 테스트 동작
- 프로젝트를 실행하면 Springboot가 실행되는 것과 다르게, Test를 실행하면 Test 환경이 실행됨.
- 따라서 필요한 클래스만 메모리에 로드하여 실행함.
  - Repository : `@DataJpaTest` 를 붙이면 DB와 관련된 컴포넌트만 메모리에 로딩함.

## 1. Unit Test의 목적
- `Spring MVC` 패턴에서는 각 `Layer` 별 책임(기능)이 다르다.
- 결과물이 잘못 되었을 때, 어느 부분이 잘못 되었는지를 확인하기 위해서는 각 `Layer` 별로 동작이 잘 하는지 확인이 필요하며, 이 때 `Unit Test`가 필요하다.
- 때문에 `Unit Test`를 진행하다 보면 반대로 각 `Layer` 혹은 `Method` 별로 하나의 책임(기능)을 가지도록 잘 설계 되었는지 검토가 가능하다. 
- 또, `Layer` 혹은 `Method` 별로 검증하는 기능을 가졌기 때문에 한 기능을 수정했을 때 생기는 부작용을 미리 체크할 수 있다.
- 혹자는 프로그램의 동작원리를 작성한 공식 `Docs`를 작성하기에 시간적 여유가 없는 경우, `Test Code`가 이를 대용할 수 있다고 한다. (`Test Code`는 이 메서드가 무슨 역할을 하고 어떻게 동작하는지를 명시해주기 때문이다.)
- 프로그램이 커질수록 필요한 여러 클래스, 메서드 등을 매번 테스트하기 어렵다. `Unit Test`는 프로그램에 필요한 모든 내용을 메모리에 올리지 않고, 필요한 부분만 올려서 테스트가 가능해져 시간 단축에도 유리하다.

### Q. 그렇다면 통합 테스트는 필요한가?
Yes. 각 `Layer` 별로 동작을 잘 한다고 해서 모든 `Process`가 잘 작동할지는 미확실하다. 따라서 최종적으로는 통합테스트를 통해 검토가 필요하다.

## 2. Spring MVC에서 Junit Test의 순서
`Repository` -> `Service` -> `Controller`

## 3. 각 Layer 별 Test의 목적(대상)
### 1) Repository
- DB쪽 관련 테스트

### 2) Service
- 기능들이 Transaction 순서에 맞게 잘 타는지

### 3) Controller
- 클라이언트와의 테스트 (ex, Null 체크)

## 4. Junit 테스트 동작
- 프로젝트를 실행하면 Springboot가 실행되는 것과 다르게, Test를 실행하면 Test 환경이 실행됨.
- 따라서 필요한 클래스만 메모리에 로드하여 실행함.
- 클래스 내의 테스트 메서드는 순서보장이 안됨.
- 테스트 클래스에 `@TestMethodOrder(MethodOrderer.OrderAnnotation.class)`
- 테스트 메서드에
  `@Order`를 사용하면 순서를 지정할 수 있음.

- JUnit은 기본적으로 메서드가 실행되면 `Transaction`시작 -> 메서드가 종료되면 `Transaction`종료 -> `RollBack`됨.

## 5. Junit 검증 메서드 라이브러리
### 1) 기본 메서드
Junit 의존성에 있는 `org.junit.jupiter.api.Assertions` 메서드.
- Test 검증 메서드 예시
```java
    assertEquals(savedBook.getTitle(), bookReqDto.getTitle());
```
왼쪽과 오른쪽 값이 동일해야 한다는 것은 알지만, 어떤 것이 실행값이고, 어떤 값을 기대하는지 알기가 어렵다.

### 2) assertj
가독성이 더 좋은 검증 메서드 라이브러리
- build.gradle
```groovy
    testImplementation("org.assertj:assertj-core:3.24.2")
```
- import static
```java
import static org.assertj.core.api.Assertions.*;
```
- Test 검증 메서드 예시
```java
    assertThat(savedBook.getTitle()).isEqualTo(bookReqDto.getTitle());
```
왼쪽 값이 우리가 `검증하고 싶은` 값이고, 오른쪽 `.isEqualTo`가 어떤 값과 동일해야 하는지를 명시함.

### [assertj 공식 설명 사이트](https://assertj.github.io/doc/)

## 6. SpringMVC 패턴에서 각 Layer 별 테스트 코드

### 1) Repository
- `@DataJpaTest` 를 붙이면 DB와 관련된 컴포넌트만 메모리에 로딩함.
- `@DataJpaTest` 안에  `@Transactional`가 있기 때문에 메서드별로 데이터가 초기화됨. (RollBack)
  - 데이터는 초기화 되는데, primary_key(auto_increment) 값이 초기화가 안됨.

### 2) Service
- Service Layer는 순수한 비즈니스 로직만을 가지고 있게 됨. 따라서, 가짜 메모리 환경에서 가짜 객체들을 주입해주고,
  그 객체들의 리턴값은 stub 구역에서 정의해줌.
- `@ExtendWith(MockitoExtension.class)` 를 붙여 가짜 메모리 환경을 구성해줌.
- Service 객체에 주입할 가짜 객체들을 필드에 `@Mock`과 함께 선언함.
- Service 객체에 `@InjectMocks` 를 붙여 가짜 객체들을 Service 객체에 주입해줌.
- 예시 코드
  ```java
  @ExtendWith(MockitoExtension.class)
  class BookServiceTest {
      
    @InjectMocks
    private BookService bookService;
    
    @Mock
    private BookRepository bookRepository;
    
    @Mock
    private MailSender mailSender;
    
  }
  ```
- Service 테스트를 위한 `given` 데이터 및 `stub` 작성
  - `given` 데이터는 파라미터로 받게 될 값을 의미함.
  - `stub`은 `given().willReturn()` 등을 통해 Service에서 주입받게 되는 가짜 객체가 해야할 일과 리턴값을 정의함.
  - 예시 코드
    ```java
    @Test
    void getBook() {
    
    // given
    Long id = 1L;
    
    // stub
    Book book = Book.builder()
                    .id(id)
                    .author("작가1")
                    .title("제목1")
                    .build();

    given(bookRepository.findById(anyLong()))
            .willReturn(Optional.ofNullable(book));

    // when
    BookResDto findBook = bookService.getBook(id);

    // then
    assertThat(findBook.getAuthor()).isEqualTo(book.getAuthor());
    assertThat(findBook.getTitle()).isEqualTo(book.getTitle());
    
    }
  ```



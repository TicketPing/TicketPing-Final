## 🎫 캐치! 티켓핑 : **대규모 트래픽 처리 공연 예매 서비스**

![티켓핑 배경 이미지](https://github.com/user-attachments/assets/57572f9b-6c7c-4ffd-aa50-e32c2056341f)

## 👋🏻 프로젝트 소개

<aside>

대규모 트래픽에도 안정적인 예매가 가능한 서비스를 제공하는 MSA 기반의 공연 예매 시스템입니다.

</aside>

## 🎯 프로젝트 목표

- **대규모 트래픽 처리를 위한 대기열 시스템**
    
    서버의 과부하 방지를 위해 예매 단계에서의 인원을 제한하는 `대기열` 시스템 구축
    
    대규모 트래픽을 안정적으로 처리할 수 있는 시스템 구축
  
- **DB 부하 및 조회 속도 개선**
    
    트래픽이 많은 좌석 조회 및 인증 정보 `Redis` 캐싱 도입
    
- **실시간 중복 예매 방지**
    
    `실시간`으로 좌석을 예매할 때 중복 예매가 발생하지 않도록 좌석 선점 시스템 도입
    
- **MSA 서비스 간 느슨한 결합**
    
    `Kafka`를 이용한 서비스 간 비동기 통신 도입
    
- **사용자 경험 향상**
    
    불필요한 대기 상황을 막기 위한 대기열 요청 선처리 필터 도입
    
- **코드 중복 최소화 및 일관성 유지**
    
    `멀티 모듈` 구조에서 공통 모듈 도입
    
- **배포 및 운영**
    
    효율성과 일관성을 위해 `Docker` 및 Docker Compose 사용
    
<br>

## 🛠️ 인프라 설계도

![ticketping_sa-최종](https://github.com/user-attachments/assets/a0352dd0-5074-49bc-9ebb-458396f03726)

<br>

## 💻 주요 기능

<aside>

### 👨‍👩‍👧‍👦 대기열 서비스
<details>
  <summary> 대기열 시퀀스 다이어그램</summary> 
  
![ticketping_sa-페이지-8 drawio](https://github.com/user-attachments/assets/1442b0f7-35dc-448a-a051-33d648bb9236)

</details>

<details>
  <summary> 대기열 진입 </summary> 
  
    작업열 인원 여유 상태에 따라 
    
    - 대기열 Sorted Set에 토큰 저장 (Member: 토큰 value, Score: 진입 시간)
    - 작업열 Key-Value 저장 (Key: 토큰 value, Value: null, TTL: 만료 시간)

</details>  

<details>
  <summary> 대기열 상태 조회 </summary> 
  
    사용자 Id로 토큰 조회
    
    - 대기열 Sorted Set에서 조회
    - 작업열 Key-Value 조회

</details>  

<details>
  <summary> 결제 이벤트 처리 </summary> 
  
    1. Kafka Consumer로 예매 완료 메시지 소비
    2. 사용자 Id로 작업열 토큰 조회 및 삭제
    3. 대기열 Sorted Set의 첫 번째 토큰 작업열로 이동

</details>  


<details>
  <summary> 작업열 토큰 만료 이벤트 처리 </summary> 
  
    1. Redis MessageListener로 작업열 토큰 만료 이벤트 수신
    2. 대기열 Sorted Set의 첫 번째 토큰 작업열로 이동
    3. 분산 환경에서 구독 중인 인스턴스가 이벤트를 수신하는 것을 방지하기 위해 Redisson의 분산 락 사용.

</details>  

<details>
  <summary> 대기열 요청 선처리 (API SERVER) </summary> 
  
    - 목적: 불필요한 대기 인원  및 서버 과부하 방지
    - 대기열 진입: 공연의 현재 잔여 좌석 수와 대기 인원 수를 토대로 처리
    - 대기열 상태 조회:  공연 매진 여부로 처리
    - 예매 API (좌석 선점, 결제): 작업열 토큰 조회 여부로 처리

</details>  

</aside>

<aside>

### 👥 회원 서비스

- 회원가입
- 로그인 (JWT 토큰 생성)
- 인가 (JWT 토큰 검증)
</aside>

<aside>

### 🎤 공연 서비스

- 공연, 공연장, 공연 일정, 일정 별 좌석 → 더미 데이터 생성
- 공연 조회
    - 등록된 모든 공연은 공연 정보를 조회 가능
- 공연 일정 조회
    - 공연 예매 기간인 공연만 공연 일정 조회 가능
- 일정 별 전체 좌석 정보 조회
</aside>

<aside>
  
### 🎫 예매 서비스

- 좌석 예매
    - 예매 진행 시 좌석을 먼저 선택한 사람에게 좌석이 선점되고 일정 시간 이후 결제까지 진행하지 않으면 좌석 선점이 풀림
- 예매 정보 조회

</aside>

<aside>

### 💸 결제 서비스

  <details>
    <summary> Stripe PG사 연동 플로우 (API SERVER) </summary> 
    
  ![image.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/83c75a39-3aba-4ba4-a792-7aefe4b07895/ef8fe8f6-2128-4162-a860-171911c0fab2/image.png)
  
  </details>  

- Stripe PG사 연동
- 결제 화면 구현
- 카드 결제 요청 및 승인 확인
</aside>

<br>

## ⚙️ 적용 기술

<aside>

**Backend**

- Java 17
- Spring boot

**DB**

- PostgreSQL
- Redis

**Messaging**

- Kafka

**Deploy**

- AWS EC2, S3
- Docker, Docker Compose

**monitoring**

- Prometheus
- Grafana

**Test**

- Jmeter
</aside>

<br>

## 🔧 기술적 의사결정


| 서비스 | 요구사항 | 기술 | 선택 이유 |
| --- | --- | --- | --- |
| 대기열 서비스 | 대기열 진입  요청 | Redis Sorted Set | 빠른 연산 속도와 사용자 요청을 Atomic하게 순서대로 처리 가능하기 때문에  |
| 대기열 서비스 | 작업열 토큰  만료 이벤트 처리 | Redis Keyspace Notifications | 잡 스케줄러의 불필요한 자원 소모를 막기 위하여 |
| 공연 서비스 | 공연 전체 좌석 조회 | Redis String | 전체 좌석 상태 조회의 경우 트래픽이 몰릴 경우 DB에 부하를 줄 수 있음 → redis에 캐싱해 DB 부하 방지 및 조회 속도 개선 |
| 예매 서비스 | 좌석 결제 과정 동안 좌석 선점  | Redis String | 좌석을 선택하고 결제하기 창에 들어가면 Redis에 캐싱된 공연 좌석을 예매 불가 상태로 변경 |
| 예매 서비스 | 좌석 선점 후 일정 기간 내 결제하지 않으면 선점 만료 | Redis String (TTL) <br> Redis Keyspace Notifications | Redis는 TTL이 있는 String을 만들고 Redis Keyspace Notifications를 통해 String이 만료되면 알림을 받을 수 있음 |
| 결제 서비스 | 결제 데이터 처리 | Stripe (PG) 연동 | 개인의 민감 정보는 DB에서 취급하지 않고 Stripe에 역할을 이전하여 결제에 필요한 최소한의 데이터만 저장하여 민감 정보 유출의 위험도를 낮춤. |
| 결제 서비스 | 결제 성공 시 예매 서비스에서 예매 상태를 `예매 완료`로 변경. | Kafka | - 비동기 통신 <br> 대규모 트래픽 상황의 경우 동기로 처리할 경우 처리 속도가 느려질 수 밖에 없기 때문에 kafka로 이벤트 핸들링하여 처리 속도 향상 |

<br>

## ⚽️ 트러블슈팅

  <details>
    <summary> [대기열] 대기열 진입 동시성 문제 </summary> 
    
  - 문제: 작업열 최대 크기 이상의 토큰이 저장되는 문제
        
        (기댓값: 100, 결과값: 208)
        
    <img width="422" alt="대기열트러블슈팅1" src="https://github.com/user-attachments/assets/0a0fd99c-2c40-4003-bcbd-3428bd346fde">
        
    
- 원인:  작업열의 토큰 카운터 값을 기반으로 분기 처리하는 부분에서, 여러 스레드가 동시에 조건을 만족하게 되어 max size이상의 토큰이 저장됨.
    
    
        public GeneralQueueTokenResponse enterWaitingQueue(String userId, String performanceId) {
                // 작업열 인원 여유 확인
                AvailableSlots availableSlots = workingQueueRepository.countAvailableSlots(CountAvailableSlotsCommand.create(performanceId));
                if (availableSlots.isLimited()) {
                    return getWaitingTokenResponse(userId, performanceId);
                }
                return getWorkingTokenResponse(userId, performanceId);
            }
        
    

- 해결: Redis의 연산들을 하나의 트랙잭션으로 묶어서 수행 가능한 Lua Script로 변경하여 해결
    
    <img width="424" alt="대기열트러블슈팅2" src="https://github.com/user-attachments/assets/d6fddd5e-fc1f-47ba-89c1-d6cc5ad66921">
        
  
  </details>  

  <details>
    <summary>[좌석 캐싱] Redis @class로 인해 다른 서버에서 캐시를 읽지 못하는 문제 해결</summary>
    
  - 문제
        - Redis에 좌석 정보 값을 저장할 때 @class가 함께 저장되어 다른 서비스에서 읽어오지 못하는 문제가 발생함
  - 원인
        - RedisConfiguration을 만들 때, `GenericJackson2JsonRedisSerializer()` 를 사용하면 @class 정보가 함께 저장
        
    ![좌석캐싱트러블슈팅1](https://github.com/user-attachments/assets/763ec8ae-5e17-4838-af19-2109a9890871)
        
        
        @Bean
        public RedisTemplate<String, Object> redisTemplate() {
            RedisTemplate<String, Object> template = new RedisTemplate<>();
            template.setConnectionFactory(redisConnectionFactory());
        
            template.setKeySerializer(new GenericJackson2JsonRedisSerializer());
            template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        
            return template;
        }
        
        
  - 해결
        - RedisConfiguration 생성시 Serializer에 @class를 저장하지 않도록 설정하여 해결
        
    ![좌석캐싱트러블슈팅2](https://github.com/user-attachments/assets/f6633e12-cea9-4ffb-8e01-a554c3f53440)
        
        
        @Bean
        public RedisTemplate<String, Object> redisTemplate() {
            RedisTemplate<String, Object> template = new RedisTemplate<>();
            template.setConnectionFactory(redisConnectionFactory());
        
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.deactivateDefaultTyping(); // @class 제거
        
            // Jackson2JsonRedisSerializer 설정
            Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(objectMapper, Object.class);
        
            template.setKeySerializer(new StringRedisSerializer());
            template.setValueSerializer(serializer);
            template.setHashKeySerializer(new StringRedisSerializer());
            template.setHashValueSerializer(serializer);
        
            return template;
        }
        
  </details>

  <details>
    <summary>
      [결제] CORS 에러
    </summary>
    
  - 문제
          - CORS 설정을 했음에도 배포된 클라이언트가 로컬의 서버와 통신하려고 할 때 CORS 에러 발생
          
          
          has been blocked by CORS policy
          : Response to preflight request doesn't pass access control check
          : No 'Access-Control-Allow-Origin' header is present on the requested resource. 
          If an opaque response serves your needs, set the request's mode to 'no-cors' to fetch the resource with CORS disabled.
          
          
  - 원인
          - 로컬의 서버가 프라이빗 서버로 인식되기 때문에 배포된 퍼블릭 클라이언트와 통신이 막힘.
  - 해결
          - ngrox으로 localhost를 퍼블릭으로 포워딩해서 클라이언트와 통신.
          
    ![cors트러블슈팅1](https://github.com/user-attachments/assets/0d8e3d2a-3863-4c58-b41f-e232080a9b2c)
  </details>
    

  <br>
  
## 🙋🏻 CONTRIBUTORS
| 팀원명 | 포지션 | 담당(개인별 기여점) | 깃허브 링크 |
| --- | --- | --- | --- |
| 김지희 | `팀장`<br>인증/인가<br> 회원 가입, 로그인 | ▶ **사용자 관리** <br>- 회원 가입, 로그인 <br> - gateway 회원 인가 필터 구현 <br> ▶ **공연 서비스** <br> - 공연, 공연장, 공연 일정, 좌석 더미 데이터 생성 <br> - 공연, 공연 일정 조회 구현 <br> - 공연 좌석 정보 캐싱 |[ 지희's Github ](https://github.com/mii2026/ ) |
| 강태원 | `부팀장` <br> - 대기열 서비스 <br> - 대기열 요청 선처리 필터| **▶ 대기열 관리**  <br>  - 대기열 진입, 대기열 상태, 작업열 토큰 조회 구현 <br> - 결제 및 작업열 토큰 만료 이벤트 시 토큰 이동 <br> **▶ 대기열 요청 선처리 필터 (게이트웨이)** <br> - 대기열 및 예매 API 요청 선처리 필터 구현 | [ 태원's Github ](https://github.com/rivertw777) |
| 김례화 | `팀원` <br>  - 결제 서비스|  ▶ **결제 서비스 화면 구현** <br>  - 결제 플로우 React로 화면 구현 <br> ▶ **PG사(Stripe) 연동** <br> - 결제 요청 <br> - 결제 승인 확인 | [ 례화's Github ](https://github.com/ryehwa) |
| 장숭혁 | `팀원` <br> - 예매 서비스 | ▶ **예매 서비스** <br> - 좌석 선점 <br> - 예매 정보 조회 | [ 숭혁's Github ](https://github.com/Mcgeolypazun)|

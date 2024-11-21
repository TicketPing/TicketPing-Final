## 🎫 캐치! 티켓핑 : **대규모 트래픽 처리 공연 예매 서비스**
<p align="center">
  <img src="https://github.com/user-attachments/assets/57572f9b-6c7c-4ffd-aa50-e32c2056341f" width="80%" alt="티켓핑 배경 이미지">
</p>
<br>

## 👋🏻 프로젝트 소개

<aside>
티켓핑은 대규모 트래픽에도 안정적인 예매가 가능한 서비스를 제공하는 MSA 기반의 공연 예매 시스템입니다.
</aside>

<br>
<br>

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

## 🛠️ 시스템 아키텍처

<p align="center">
  <img src="https://github.com/user-attachments/assets/e2c614e9-28b3-4d37-9e2c-8fabeeb2864d" height="90%" width="90%" alt="sa">
</p>

<details>
    <summary> 기술 스택 </summary> 
<br>  
<br>  
<div align="center">
<table style="width: 100%; margin: auto; text-align: center;">
    <tr>
        <th style="text-align: center;">카테고리</th>
        <th style="text-align: center;">기술/도구</th>
    </tr>
    <tr>
        <td style="text-align: center;"><strong>Backend</strong></td>
        <td style="text-align: center;">Java 17</td>
    </tr>
    <tr>
        <td style="text-align: center;"></td>
        <td style="text-align: center;">Spring Boot 3.3.4</td>
    </tr>
    <tr>
        <td style="text-align: center;"></td>
        <td style="text-align: center;">MVC, WebFlux</td>
    </tr>
    <tr>
        <td style="text-align: center;"></td>
        <td style="text-align: center;">Cloud, Security</td>
    </tr>
    <tr>
        <td style="text-align: center;"></td>
        <td style="text-align: center;">Spring Data JPA</td>
    </tr>
    <tr>
        <td style="text-align: center;"><strong>DB</strong></td>
        <td style="text-align: center;">PostgreSQL</td>
    </tr>
    <tr>
        <td style="text-align: center;"></td>
        <td style="text-align: center;">Redis</td>
    </tr>
    <tr>
        <td style="text-align: center;"><strong>Messaging</strong></td>
        <td style="text-align: center;">Kafka</td>
    </tr>
    <tr>
        <td style="text-align: center;"><strong>Monitoring</strong></td>
        <td style="text-align: center;">Prometheus</td>
    </tr>
    <tr>
        <td style="text-align: center;"></td>
        <td style="text-align: center;">Grafana</td>
    </tr>
    <tr>
        <td style="text-align: center;"></td>
        <td style="text-align: center;">Loki</td>
    </tr>
    <tr>
        <td style="text-align: center;"></td>
        <td style="text-align: center;">Zipkin</td>
    </tr>
    <tr>
        <td style="text-align: center;"><strong>Infra</strong></td>
        <td style="text-align: center;">Docker</td>
    </tr>
    <tr>
        <td style="text-align: center;"><strong>Test</strong></td>
        <td style="text-align: center;">JMeter</td>
    </tr>
    <tr>
        <td style="text-align: center;"><strong>Channel</strong></td>
        <td style="text-align: center;">Discord</td>
    </tr>
</table>
</div>
</details>

<br>

## 💻 시퀀스 다이어그램

<aside>
  
### 👨‍👩‍👧‍👦 대기열

<details>
    <summary> View  </summary> 

![ticketping_sa-페이지-8 drawio](https://github.com/user-attachments/assets/a2f5074b-441b-457f-b674-38419f20d2d3)

</details>

</aside>

<aside>

### 🎫 예매 

<details>
    <summary> View  </summary> 

![ticketping_sa-예매,결제 시퀀스 drawio](https://github.com/user-attachments/assets/a91e25c2-7191-4fb1-b94d-cacdb7074e39)

</details>

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

  <br>
  
## 🙋🏻 CONTRIBUTORS
<div align="center">
<table style="width: 100%">
    <tr>
        <th style="text-align: center;">팀원</th>
        <th style="text-align: center;">담당</th>
        <th style="text-align: center;">깃허브 링크</th>
    </tr>
    <tr>
        <td align="center"><img src="https://avatars.githubusercontent.com/rivertw777" width="100px;" alt=""/><br /><sub><b>강태원</b></sub></a></td>
        <td>
          - 대기열 서비스 개발 <br>
          - 결제 서비스 개발 <br>
          - 게이트웨이 예매 API 선처리 필터 개발 <br>
          - 공통 모듈 관리 <br>
          - Redis 설정, 클러스터 구축 <br>
          - Kafka 설정 <br>
        </td>
        <td align="center"><a href="https://github.com/rivertw777">GitHub</a></td>
    </tr>
    <tr>
        <td align="center"><img src="https://avatars.githubusercontent.com/mii2026" width="100px;" alt=""/><br /><sub><b>김지희</b></sub></a></td>
        <td>
          - 회원, 인증 서비스 개발 <br>
          - 공연 서비스 개발 <br>
          - 주문 서비스 개발 <br>
          - 게이트웨이 JWT 인증 필터 개발 <br>
          - 모니터링 시스템 구축 <br>
        </td>
        <td align="center"><a href="https://github.com/mii2026">GitHub</a></td>
    </tr>
</table>
</div>

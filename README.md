## 🎫 캐치! 티켓핑 : **대규모 트래픽 처리 공연 예매 서비스**
<p align="center">
  <img src="https://github.com/user-attachments/assets/57572f9b-6c7c-4ffd-aa50-e32c2056341f" width="80%" alt="티켓핑 배경 이미지">
</p>
<br>

## 👋🏻 프로젝트 소개

티켓핑은 **대규모 트래픽** 상황에도 안정적으로 사용자들이 예매할 수 있도록
하는 공연 예매 서비스입니다.

인기 공연의 티켓팅 시에 순간 트래픽으로 인한 서버 과부하 상황을 방지하기 위해 예매 인원을 제한하는 

**대기열 시스템**을 구축하였습니다.

<br>

## 🎯 프로젝트 목표

- **MSA**: 특정 서비스에 대한 부하가 증가할 때 해당 서비스만 독립적으로 스케일 아웃할 수 있는 MSA 적용

- **비동기 통신**: 서비스 간 느슨한 결합 위해 메시지 큐를 사용하여 비동기 통신 구현

- **고가용성**: 서버 장애 시에도 서비스가 지속적으로 운영될 수 있도록 고가용성 보장

- **동시성 처리**: 대규모 트래픽과 분산 환경에서도 안정적이고 신뢰성 있는 서비스를 제공하기 위한 동시성 처리

- **모니터링**: 서버의 과부하 상황 시 즉각적으로 대응할 수 있도록 알림을 보내는 모니터링 시스템 구축

- **공통 모듈**: 코드 중복을 최소화하고 일관성을 유지하기 위해 공통 모듈 사용
    
<br>

## 🛠️ 시스템 아키텍처

<p align="center">
  <img src="https://github.com/user-attachments/assets/e2c614e9-28b3-4d37-9e2c-8fabeeb2864d" height="90%" width="90%" alt="sa">
</p>

<details>
    <summary><h3>⚙️ 기술 스택</h3></summary> 
  
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

## 📃 다이어그램

### 🧑 유저 플로우

<div align="center">
<br>  
  
![ticketping_sa-유저 플로우 drawio](https://github.com/user-attachments/assets/56de8102-d730-48a0-ae61-de2c844f807d)
</div>

<aside>
  
<details>
    <summary><h3>👨‍👩‍👧‍👦 대기열 시퀀스 다이어그램</h3></summary> 
<br>  
  
![ticketping_sa-페이지-8 drawio](https://github.com/user-attachments/assets/a2f5074b-441b-457f-b674-38419f20d2d3)

</details>

</aside>

<aside>

<details>
    <summary><h3>🎫 예매 시퀀스 다이어그램</h3></summary> 
<br>  

![ticketping_sa-예매,결제 시퀀스 drawio](https://github.com/user-attachments/assets/a91e25c2-7191-4fb1-b94d-cacdb7074e39)

</details>

</aside>

<aside>

<details>
    <summary><h3>✏️ ERD</h3></summary> 
<br>  

![ticketping_sa-예매,결제 시퀀스 drawio](https://github.com/user-attachments/assets/a91e25c2-7191-4fb1-b94d-cacdb7074e39)

</details>

</aside>

<br>

## 🔧 기술적 의사결정

- [🎟️ 대기열 시스템 구상하기](https://github.com/TicketPing/TicketPing-Final/wiki/%F0%9F%8E%9F%EF%B8%8F-%EB%8C%80%EA%B8%B0%EC%97%B4-%EC%8B%9C%EC%8A%A4%ED%85%9C-%EA%B5%AC%EC%83%81%ED%95%98%EA%B8%B0)

- [🎬 스케줄러로 작업열 토큰의 만료 이벤트 처리하기](https://github.com/TicketPing/TicketPing-Final/wiki/%F0%9F%8E%AC-%EC%8A%A4%EC%BC%80%EC%A4%84%EB%9F%AC%EB%A1%9C-%EC%9E%91%EC%97%85%EC%97%B4-%ED%86%A0%ED%81%B0%EC%9D%98-%EB%A7%8C%EB%A3%8C-%EC%9D%B4%EB%B2%A4%ED%8A%B8-%EC%B2%98%EB%A6%AC%ED%95%98%EA%B8%B0)

- [👁️‍🗨️ Redis Keyspace Notifications로 작업열 토큰의 만료 이벤트 처리하기](https://github.com/TicketPing/TicketPing-Final/wiki/%F0%9F%91%81%EF%B8%8F%E2%80%8D%F0%9F%97%A8%EF%B8%8F-Redis-Keyspace-Notifications%EB%A1%9C-%EC%9E%91%EC%97%85%EC%97%B4-%ED%86%A0%ED%81%B0%EC%9D%98-%EB%A7%8C%EB%A3%8C-%EC%9D%B4%EB%B2%A4%ED%8A%B8-%EC%B2%98%EB%A6%AC%ED%95%98%EA%B8%B0)  
  
- [💬 Redis Cluster 적용하기](https://github.com/TicketPing/TicketPing-Final/wiki/%F0%9F%92%AC-Redis-Cluster-%EC%A0%81%EC%9A%A9%ED%95%98%EA%B8%B0)
  
<br>

## ⚽️ 트러블슈팅

- [🎁 Lua Script를 활용한 대기열 진입 동시성 문제 해결](https://github.com/TicketPing/TicketPing-Final/wiki/%F0%9F%8E%81-Lua-Script%EB%A5%BC-%ED%99%9C%EC%9A%A9%ED%95%9C-%EB%8C%80%EA%B8%B0%EC%97%B4-%EC%A7%84%EC%9E%85-%EB%8F%99%EC%8B%9C%EC%84%B1-%EB%AC%B8%EC%A0%9C-%ED%95%B4%EA%B2%B0)

- [🖍️ Redis @class로 인해 다른 서버에서 캐시를 읽지 못하는 문제 해결](https://github.com/TicketPing/TicketPing-Final/wiki/%F0%9F%96%8D%EF%B8%8F-Redis-@class%EB%A1%9C-%EC%9D%B8%ED%95%B4-%EB%8B%A4%EB%A5%B8-%EC%84%9C%EB%B2%84%EC%97%90%EC%84%9C-%EC%BA%90%EC%8B%9C%EB%A5%BC-%EC%9D%BD%EC%A7%80-%EB%AA%BB%ED%95%98%EB%8A%94-%EB%AC%B8%EC%A0%9C-%ED%95%B4%EA%B2%B0)  

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

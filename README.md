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

- **느슨한 결합**: 메시지 큐를 활용한 비동기 처리로 각 서비스 간의 의존성을 최소화

- **고가용성**: 서버 장애 시에도 서비스가 지속적으로 운영될 수 있도록 고가용성 보장

- **동시성 처리**: 대규모 트래픽과 분산 환경에서도 안정적이고 신뢰성 있는 서비스를 제공하기 위한 동시성 처리

- **모니터링**: 서버의 과부하 상황 시 즉각적으로 대응할 수 있도록 알림을 보내는 모니터링 시스템 구축

- **공통 모듈**: 코드 중복을 최소화하고 일관성을 유지하기 위해 공통 모듈 사용
    
<br>

## 🛠️ 시스템 아키텍처

<p align="center">
  <img src="https://github.com/user-attachments/assets/02e9519b-1488-4aa7-ad06-036e99700c2e" height="90%" width="90%" alt="sa">
</p>

## ⚙️ 기술 스택
<div align="center"> 
<table style="width: 100%; margin: auto; text-align: center;">
    <tr>
        <th style="text-align: center;">카테고리</th>
        <th style="text-align: center;">기술/도구</th>
    </tr>
    <tr>
        <td style="text-align: center;"><strong>Backend</strong></td>
        <td style="text-align: center;"> <img src="https://img.shields.io/badge/java 17-007396"/> <img src="https://img.shields.io/badge/Spring Boot 3.3.4-6DB33F?logo=spring-boot&logoColor=white"/> </td>
    </tr>
    <tr>
        <td style="text-align: center;"><strong>Database</strong></td>
        <td style="text-align: center;"> <img src="https://img.shields.io/badge/PostgreSQL-316192?logo=postgresql&logoColor=white"/> <img src="https://img.shields.io/badge/redis-%23DD0031.svg?logo=redis&logoColor=white"/> </td>
    </tr>
    <tr>
        <td style="text-align: center;"><strong>Messaging</strong></td>
        <td style="text-align: center;"> <img src="https://img.shields.io/badge/Apache_Kafka-231F20?logo=apache-kafka&logoColor=white"/> </td>
    </tr>
    <tr>
        <td style="text-align: center;"><strong>Monitoring</strong></td>
        <td style="text-align: center;"><img src="https://img.shields.io/badge/Grafana-F2F4F9?logo=grafana&logoColor=orange&labelColor=F2F4F9"/> <img src="https://img.shields.io/badge/Prometheus-000000?logo=prometheus&labelColor=000000"/> <img src="https://img.shields.io/badge/Loki-F44B21"/> <img src="https://img.shields.io/badge/Zipkin-FE7A16"/> </td>
    </tr>
    <tr>
        <td style="text-align: center;"><strong>Infra</strong></td>
        <td style="text-align: center;"> <img src="https://img.shields.io/badge/docker-%230db7ed.svg?logo=docker&logoColor=white"/> </td>
    </tr>
    <tr>
        <td style="text-align: center;"><strong>Test</strong></td>
        <td style="text-align: center;"> <img src="https://img.shields.io/badge/Jmeter-543DE0"/> </td>
    </tr>
    <tr>
      <td style="text-align: center;"><strong>Channel</strong></td>
      <td style="text-align: center;"> <img src="https://img.shields.io/badge/Discord-%235865F2.svg?logo=discord&logoColor=white"/> </td>
    </tr>
</table>
</div>

<br>

## 👨‍👩‍👧‍👦 10,000명을 안정적으로 수용할 수 있는 대기열 시스템 검증

### 테스트 환경
<br>

<div align="center">
  <img src="https://github.com/user-attachments/assets/dd8d2714-52cb-4bf0-92c4-c0ee104470e6" height="70%" width="70%" alt="test">
</div>

<details>
    <summary><h4>인스턴스 스펙</h4></summary> 
    <div align="center">
    <table style="width: 100%; margin: auto; text-align: center; border-collapse: collapse;">
    <tr>
        <th style="text-align: center; border: 1px solid black;">인스턴스</th>
        <th style="text-align: center; border: 1px solid black;">스펙</th>
    </tr>
    <tr>
        <td style="text-align: center; border: 1px solid black;"><strong>Server</strong></td>
        <td style="text-align: center; border: 1px solid black;"><strong>t2.2xlarge (8 vCPU, 32 GB memory)</strong></td>
    </tr>
    <tr>
        <td style="text-align: center; border: 1px solid black;"><strong>Test</strong></td>
        <td style="text-align: center; border: 1px solid black;"><strong>t2.medium (2 vCPU, 4 GB memory)</strong></td>
    </tr>
    <tr>
        <td style="text-align: center; border: 1px solid black;"><strong>Monitoring</strong></td>
        <td style="text-align: center; border: 1px solid black;"><strong>t2.medium (2 vCPU, 4 GB memory)</strong></td>
    </tr>
    <tr>
        <td style="text-align: center; border: 1px solid black;"><strong>Redis OSS</strong></td>
        <td style="text-align: center; border: 1px solid black;"><strong>cache.r7g.large (13.07 GB memory)</strong></td>
    </tr>
    </table>
    </div>
</details>

### MVC VS WebFlux
`10,000`명의 동시 대기열 진입 이후 15분간 3초 주기의 Polling을 통해 대기열 상태 조회를 실시하였습니다.

<br>

<div align="center">

![image](https://github.com/user-attachments/assets/68c0e381-f238-43f8-ae24-158382a91e8c)
![image](https://github.com/user-attachments/assets/552153ed-6b1a-423d-9d2f-36f53759fa4b)
</div>

동일 환경에서 테스트한 결과 WebFlux가 MVC에 비해 약 10% 정도 짧은 응답 시간을 갖고, 

CPU 사용량과 Load Average가 상대적으로 낮은 것을 확인할 수 있었습니다.

이미 Lua Script를 통한 성능 최적화가 이루어진 상태에서도 `WebFlux`가 `MVC`에 비해 더 적은 리소스를 사용하여도

대량의 트래픽을 빠르게 처리함을 확인할 수 있었습니다.
<br> 

<details>
    <summary><h4>결과 상세</h4></summary> 
    <ul>
        <li>
            <h2>MVC</h2>
            <img src="https://github.com/user-attachments/assets/318073b3-cb86-4871-bc21-ea7b4e319cf8" alt="mvc result" style="max-width: 100%; height: auto;">
            <img src="https://github.com/user-attachments/assets/d63be685-863c-451c-bb73-f9ecc86b24a5" alt="mvc cpu" style="max-width: 100%; height: auto;">
            <img src="https://github.com/user-attachments/assets/5c6a90cc-18ba-4cdc-8d07-b15c43bd9b7e" alt="mvc load" style="max-width: 100%; height: auto;">
        </li>
        <li>
            <h2>WebFlux</h2>
            <img src="https://github.com/user-attachments/assets/6c93c553-9789-49ac-ac7e-c374bf524842" alt="wf result" style="max-width: 100%; height: auto;">
            <img src="https://github.com/user-attachments/assets/0ee46ed3-8400-49c2-ad17-466cd7b1bb91" alt="wf cpu" style="max-width: 100%; height: auto;">
            <img src="https://github.com/user-attachments/assets/361909fe-4c7c-48d0-b11c-b9b57d1a74db" alt="wf load" style="max-width: 100%; height: auto;">
        </li>
    </ul>
</details>

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
  
![ticketping_sa-페이지-8 drawio](https://github.com/user-attachments/assets/9ddde4a6-3d2c-48f4-b08c-536a1d6df269)

</details>

</aside>

<aside>

<details>
    <summary><h3>🎫 예매 시퀀스 다이어그램</h3></summary> 
<br>  

![ticketping_sa-예매 시퀀스 drawio (1)](https://github.com/user-attachments/assets/f1396b43-d583-4066-b288-e66796622e66)


</details>

</aside>

<aside>

<details>
    <summary><h3>✏️ ERD</h3></summary> 
<br>  

![ticketping-erd](https://github.com/user-attachments/assets/4604f251-4e39-4856-8d67-853d29622a86)

</details>

</aside>

<br>

## 🔧 기술적 의사결정

- [🎟️ 대기열 시스템 구상하기](https://github.com/TicketPing/TicketPing-Final/wiki/%F0%9F%8E%9F%EF%B8%8F-%EB%8C%80%EA%B8%B0%EC%97%B4-%EC%8B%9C%EC%8A%A4%ED%85%9C-%EA%B5%AC%EC%83%81%ED%95%98%EA%B8%B0)

- [🎬 스케줄러로 작업열 토큰의 만료 이벤트 처리하기](https://github.com/TicketPing/TicketPing-Final/wiki/%F0%9F%8E%AC-%EC%8A%A4%EC%BC%80%EC%A4%84%EB%9F%AC%EB%A1%9C-%EC%9E%91%EC%97%85%EC%97%B4-%ED%86%A0%ED%81%B0%EC%9D%98-%EB%A7%8C%EB%A3%8C-%EC%9D%B4%EB%B2%A4%ED%8A%B8-%EC%B2%98%EB%A6%AC%ED%95%98%EA%B8%B0)

- [👁️‍🗨️ Redis Keyspace Notifications로 작업열 토큰의 만료 이벤트 처리하기](https://github.com/TicketPing/TicketPing-Final/wiki/%F0%9F%91%81%EF%B8%8F%E2%80%8D%F0%9F%97%A8%EF%B8%8F-Redis-Keyspace-Notifications%EB%A1%9C-%EC%9E%91%EC%97%85%EC%97%B4-%ED%86%A0%ED%81%B0%EC%9D%98-%EB%A7%8C%EB%A3%8C-%EC%9D%B4%EB%B2%A4%ED%8A%B8-%EC%B2%98%EB%A6%AC%ED%95%98%EA%B8%B0)  
  
- [💬 Redis Cluster 적용하기](https://github.com/TicketPing/TicketPing-Final/wiki/%F0%9F%92%AC-Redis-Cluster-%EC%A0%81%EC%9A%A9%ED%95%98%EA%B8%B0)

- [😏 Kafka Producer 설정하기](https://github.com/TicketPing/TicketPing-Final/wiki/%F0%9F%98%8F-Kafka-Producer-%EC%84%A4%EC%A0%95%ED%95%98%EA%B8%B0)

- [🤥 Kafka Consumer 설정하기](https://github.com/TicketPing/TicketPing-Final/wiki/%F0%9F%A4%A5-Kafka-Consumer-%EC%84%A4%EC%A0%95%ED%95%98%EA%B8%B0)

- [🥶 Kafka Cluster 적용하기](https://github.com/TicketPing/TicketPing-Final/wiki/%F0%9F%A5%B6-Kafka-Cluster-%EC%A0%81%EC%9A%A9%ED%95%98%EA%B8%B0)

- [✅ 인증 인가 구상하기](https://github.com/TicketPing/TicketPing-Final/wiki/%E2%9C%85-%EC%9D%B8%EC%A6%9D-%EC%9D%B8%EA%B0%80-%EA%B5%AC%EC%83%81%ED%95%98%EA%B8%B0)

- [🎫 좌석 예매 흐름 구상하기](https://github.com/TicketPing/TicketPing-Final/wiki/%F0%9F%8E%AB-%EC%A2%8C%EC%84%9D-%EC%98%88%EB%A7%A4-%ED%9D%90%EB%A6%84-%EA%B5%AC%EC%83%81%ED%95%98%EA%B8%B0)

- [💺 좌석 데이터 캐싱 구상하기](https://github.com/TicketPing/TicketPing-Final/wiki/%F0%9F%92%BA-%EC%A2%8C%EC%84%9D-%EB%8D%B0%EC%9D%B4%ED%84%B0-%EC%BA%90%EC%8B%B1-%EA%B5%AC%EC%83%81%ED%95%98%EA%B8%B0)

- [↩️ Redis Keyspace Notifications을 이용한 좌석 선점 만료 구현](https://github.com/TicketPing/TicketPing-Final/wiki/%E2%86%A9%EF%B8%8F-Redis-Key-Expiration%EC%9D%84-%EC%9D%B4%EC%9A%A9%ED%95%9C-%EC%A2%8C%EC%84%9D-%EC%84%A0%EC%A0%90-%EB%A7%8C%EB%A3%8C-%EA%B5%AC%ED%98%84)

- [⏰ Grafana를 이용한 통합 모니터링 및 알람 구축](https://github.com/TicketPing/TicketPing-Final/wiki/%E2%8F%B0-Grafana%EB%A5%BC-%EC%9D%B4%EC%9A%A9%ED%95%9C-%ED%86%B5%ED%95%A9-%EB%AA%A8%EB%8B%88%ED%84%B0%EB%A7%81-%EB%B0%8F-%EC%95%8C%EB%9E%8C-%EA%B5%AC%EC%B6%95)

- [⚡ 게이트웨이에 서킷브레이커 구축](https://github.com/TicketPing/TicketPing-Final/wiki/%E2%9A%A1-%EA%B2%8C%EC%9D%B4%ED%8A%B8%EC%9B%A8%EC%9D%B4%EC%97%90-%EC%84%9C%ED%82%B7%EB%B8%8C%EB%A0%88%EC%9D%B4%EC%BB%A4-%EA%B5%AC%EC%B6%95)

<br>

## ⚽️ 트러블슈팅

- [🎁 Lua Script를 활용한 대기열 진입 동시성 문제 해결](https://github.com/TicketPing/TicketPing-Final/wiki/%F0%9F%8E%81-Lua-Script%EB%A5%BC-%ED%99%9C%EC%9A%A9%ED%95%9C-%EB%8C%80%EA%B8%B0%EC%97%B4-%EC%A7%84%EC%9E%85-%EB%8F%99%EC%8B%9C%EC%84%B1-%EB%AC%B8%EC%A0%9C-%ED%95%B4%EA%B2%B0)

- [🗣️ Redis Cluster 적용 이후 Lua Script 실행 오류 문제 해결](https://github.com/TicketPing/TicketPing-Final/wiki/%F0%9F%97%A3%EF%B8%8F-Redis-Cluster-%EC%A0%81%EC%9A%A9-%EC%9D%B4%ED%9B%84-Lua-Script-%EC%8B%A4%ED%96%89-%EC%98%A4%EB%A5%98-%EB%AC%B8%EC%A0%9C-%ED%95%B4%EA%B2%B0)

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
          - Redis 클러스터 구축 <br>
          - Kafka 설정, 클러스터 구축 <br>
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

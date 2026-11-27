# Fink! AWS 기본 네트워크 구축 기록

## 구축 범위

- 작업일 2026-11-27
- AWS 서울 리전 `ap-northeast-2`
- 사용자가 팀 공용 AWS 계정에서 콘솔로 직접 구성
- 최초 확인한 크레딧 $100, 실제 사용 비용과 잔액은 미확인

아래는 콘솔에 적용된 구성이다. 승인된 설계와의 최종 대조 및 팀 합의는 검토 대기다.

## VPC와 서브넷

`fink-vpc`의 IPv4 CIDR은 `10.0.0.0/16`이다. IPv6는 사용하지 않으며 DNS 확인과 DNS 호스트 이름은 활성화했다.

| 서브넷 | IPv4 CIDR | 가용 영역 | 구분 |
| --- | --- | --- | --- |
| `fink-public-a` | `10.0.10.0/24` | `ap-northeast-2a` | 퍼블릭 |
| `fink-public-b` | `10.0.11.0/24` | `ap-northeast-2c` | 퍼블릭 |
| `fink-external` | `10.0.20.0/24` | `ap-northeast-2a` | 프라이빗 |
| `fink-internal` | `10.0.30.0/24` | `ap-northeast-2a` | 프라이빗 |
| `fink-external-db` | `10.0.40.0/24` | `ap-northeast-2a` | 프라이빗 |
| `fink-internal-db` | `10.0.50.0/24` | `ap-northeast-2a` | 프라이빗 |

서브넷 6개 모두 `Available` 상태이며 퍼블릭 IPv4 자동 할당은 모두 비활성화되어 있다.

## 인터넷 게이트웨이와 라우팅

`fink-igw`는 `fink-vpc`에 연결되어 있으며 콘솔에서 `Attached` 상태를 확인했다.

| Route Table | 대상 CIDR과 다음 홉 | 명시적으로 연결한 서브넷 |
| --- | --- | --- |
| `fink-public-rt` | `10.0.0.0/16` → `local`, `0.0.0.0/0` → `fink-igw` | `fink-public-a`, `fink-public-b` |
| `fink-private-rt` | `10.0.0.0/16` → `local` | `fink-external`, `fink-internal`, `fink-external-db`, `fink-internal-db` |
| 기본 Route Table | `10.0.0.0/16` → `local` | 없음 |

퍼블릭 Route Table의 기본 경로는 인터넷 게이트웨이로 향한다. 프라이빗 Route Table은 VPC 내부의 `local` 경로만 갖고 있으며, 외부 송신 경로는 미구성 상태다. 서브넷 간 접근 허용과 차단은 서비스 배포 및 Security Group 적용 후 실제 통신 테스트로 검증한다.

## 태그 관리 규칙

| 키 | 값 |
| --- | --- |
| `Name` | 리소스별 이름 |
| `Project` | `Fink` |
| `Environment` | `dev` |

## 검증 결과

사용자와 GPT가 AWS 콘솔 캡처 및 리소스 맵을 대조한 결과를 기록했다.

| 확인 항목 | 결과 |
| --- | --- |
| VPC | 1개 |
| 서브넷 | 6개 |
| Route Table | 3개 |
| Internet Gateway | 1개 |
| 퍼블릭 서브넷 명시적 연결 | 2개 |
| 프라이빗 서브넷 명시적 연결 | 4개 |
| 명시적 연결이 없는 서브넷 | 0개 |

기본 네트워크 설정 검증을 완료했다. [ROADMAP의 INFRA-01](../ROADMAP.md#infra-01-aws-vpc-및-subnet-구축) 전체 완료에 필요한 승인 설계와 실제 구성의 일치 여부는 검토 대기다.

## 미구현 및 미확인 사항

- EC2 및 DB 배포, Security Group 접근제어, VPN, 메시지 큐와 IAM 연동
- 프라이빗 영역의 외부 송신 경로와 실제 패킷·서버 통신 검증
- 팀 전체 설계 합의, ALB 및 RDS 도입 여부, 실제 AWS 비용

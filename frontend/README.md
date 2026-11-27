# frontend

직원용 React 웹을 둘 자리입니다.
고객용 화면은 이 폴더가 아니라 `customer-app` (Flutter) 에 있습니다.

## 호출하는 서버

직원 웹은 internal-server (8081) 만 호출합니다.

| API | 설명 |
|---|---|
| `POST /staff/auth/login` | 직원 로그인 → 토큰 |
| `GET /staff/customers/{id}/accounts` | 고객 계좌 조회 |
| `GET /staff/audit-logs` | 감사 로그 조회 |

요청 헤더에 `Authorization: Bearer <토큰>` 을 붙입니다.

## 배포 위치

직원 화면은 인터넷에 공개하지 않습니다.
빌드 결과물을 내부망 안(internal-server 가 있는 쪽)에 두어, VPN 에 접속한 직원만 열 수 있게 합니다.

## 로컬 개발 시

React 개발 서버(예: `localhost:5173`)와 internal-server(`localhost:8081`)의 주소가 달라서,
internal-server 에 개발용 CORS 허용 설정이 필요합니다.

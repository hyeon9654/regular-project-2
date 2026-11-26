# regular-project-2

은행기업 사내 통합 업무 시스템의 기본 틀입니다.
서버가 뜨고 서로 통신하는 것까지만 들어 있습니다.

## 구조

| 폴더 | 포트 | 역할 | 배포 위치 |
|---|---|---|---|
| external-server | 8080 | 외부망 Spring. 고객 로그인, 고객 요청을 내부로 전달 | EC2-1 (퍼블릭) |
| internal-server | 8081 | 내부망 Spring. 직원 로그인, 계좌 조회, 감사 로그 | EC2-2 (프라이빗) |
| ai-server | 8000 | Python FastAPI. 모델 서빙 자리 | EC2-3 (프라이빗) |
| frontend | - | 직원용 React 웹 자리 | 내부망 안 |
| customer-app | - | 고객용 Flutter 앱 자리 | 고객 폰 |
| docs | - | 기획서, ERD, API 명세 | - |

```
고객 (Flutter 앱) ──▶ external-server ──▶ internal-server ──▶ (DB)
                                               ▲
직원 (React 웹) ───────────────────────────────┘   (나중에 VPN으로만 접근)
```

## 지금 들어 있는 것

- JWT 로그인과 역할 구분 (CUSTOMER / STAFF)
- 외부 서버 → 내부 서버 호출, 내부 서버의 토큰 재검증
- 고객은 본인 계좌만 조회
- 직원 로그인과 직원용 조회는 내부 서버에만 존재
- 내부 서버의 모든 요청을 감사 로그로 기록 (AOP)

## 아직 없는 것

- 실제 DB (지금은 메모리의 가짜 데이터, 감사 로그만 H2에 저장)
- 입금·출금·이체, 이상거래 탐지, 소명, 증시 예측 등 업무 기능
- 서버 간 mTLS, 인증서
- VPC, VPN (AWS 설정)

## 연습용 계정

| 아이디 | 비밀번호 | 역할 | 번호 | 로그인하는 서버 |
|---|---|---|---|---|
| kim | 1234 | CUSTOMER | 7 | external-server (8080) |
| park | 1234 | CUSTOMER | 8 | external-server (8080) |
| lee | 1234 | STAFF | 9 | internal-server (8081) |

## 실행

JDK 17이 필요합니다. 터미널을 서버마다 하나씩 엽니다.

```bash
# 터미널 1
cd internal-server
./gradlew bootRun          # 윈도우: gradlew.bat bootRun

# 터미널 2
cd external-server
./gradlew bootRun

# 터미널 3
cd ai-server
python -m venv venv
source venv/bin/activate   # 윈도우: venv\Scripts\activate
pip install -r requirements.txt
uvicorn main:app --port 8000
```

IntelliJ에서는 `external-server`와 `internal-server` 폴더를 각각 다른 창으로 엽니다.

## 확인 순서

Git Bash, macOS, Linux 터미널 기준입니다.

```bash
# 1. 고객 로그인 (외부 서버) → 응답의 token 을 CUSTOMER_TOKEN 자리에 넣는다
curl -s -X POST localhost:8080/api/auth/login \
  -H "Content-Type: application/json" -d '{"loginId":"kim","password":"1234"}'

# 2. 고객이 본인 계좌 조회 (외부 → 내부)                     → 200, 계좌 2개 기대
curl -i localhost:8080/api/customer/accounts \
  -H "Authorization: Bearer CUSTOMER_TOKEN"

# 3. 직원 로그인 (내부 서버) → 응답의 token 을 STAFF_TOKEN 자리에 넣는다
curl -s -X POST localhost:8081/staff/auth/login \
  -H "Content-Type: application/json" -d '{"loginId":"lee","password":"1234"}'

# 4. 직원이 고객 7번 계좌 조회 (내부 서버)                    → 200 기대
curl -i localhost:8081/staff/customers/7/accounts \
  -H "Authorization: Bearer STAFF_TOKEN"

# 5. 고객 토큰으로 직원 주소 호출                             → 403 기대
curl -i localhost:8081/staff/customers/7/accounts \
  -H "Authorization: Bearer CUSTOMER_TOKEN"

# 6. 고객 7번이 고객 8번 계좌를 조회                          → 403 기대
curl -i localhost:8081/internal/customers/8/accounts \
  -H "Authorization: Bearer CUSTOMER_TOKEN"

# 7. 외부 서버에는 직원 주소가 없다                           → 403 기대
curl -i localhost:8080/staff/customers/7/accounts \
  -H "Authorization: Bearer STAFF_TOKEN"

# 8. 감사 로그 확인 (위 요청들이 기록돼 있어야 한다)
curl -s localhost:8081/staff/audit-logs \
  -H "Authorization: Bearer STAFF_TOKEN"

# 9. AI 서버 상태 확인                                        → {"status":"ok"}
curl -s localhost:8000/health
```

## 지금 코드의 임시 처리

연습용이라 다음 단계에서 바꿔야 합니다.

- 회원·계좌가 메모리의 가짜 데이터입니다. DB 테이블로 바꿉니다.
- 비밀번호를 평문으로 비교합니다. BCrypt 해시로 바꿉니다.
- `jwt.secret`에 로컬용 기본값이 있습니다. 운영에서는 환경변수 `JWT_SECRET`으로만 넣습니다.
- 두 서버가 같은 비밀키를 나눠 씁니다(HS256). PKI를 붙일 때 개인키 서명, 공개키 검증 방식으로 바꿉니다.
- 외부 → 내부가 평문 HTTP입니다. mTLS를 적용합니다.
- 내부 서버가 `localhost:8081`로 열려 있습니다. AWS에서는 프라이빗 서브넷과 보안그룹으로 막습니다.

## 커밋 메시지 규칙

어느 서버의 변경인지 머리말을 붙입니다.

```
[external] 고객 이체 요청 API 추가
[internal] 원장 테이블 추가
[ai] 증빙자료 분류 모델 로드
[docs] ERD 수정
```

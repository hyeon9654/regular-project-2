# customer-app

고객용 Flutter 앱을 둘 자리입니다.

## 프로젝트 만들기

이 폴더 안에서 실행합니다.
Dart 패키지 이름에는 하이픈(-)을 쓸 수 없어서, 프로젝트 이름을 밑줄로 따로 지정합니다.

```bash
cd customer-app
flutter create --project-name customer_app .
```

## 호출하는 서버

고객 앱은 external-server (8080) 만 호출합니다.
internal-server 의 주소는 앱 코드에 넣지 않습니다.

| API | 설명 |
|---|---|
| `POST /api/auth/login` | 고객 로그인 → 토큰 |
| `GET /api/customer/accounts` | 본인 계좌 조회 |

요청 헤더에 `Authorization: Bearer <토큰>` 을 붙입니다.

## 실행 환경별 서버 주소

에뮬레이터 안에서 `localhost` 는 노트북이 아니라 에뮬레이터 자신을 가리킵니다.

| 실행 환경 | 서버 주소 |
|---|---|
| Android 에뮬레이터 | `http://10.0.2.2:8080` |
| iOS 시뮬레이터 | `http://localhost:8080` |
| 실제 폰 (같은 와이파이) | `http://<노트북 IP>:8080` |

## 주의

- 토큰은 `flutter_secure_storage` 같은 보안 저장소에 보관합니다.
- 앱 서명 키(`*.keystore`, `key.properties`)는 Git에 올리지 않습니다.
- Android 에뮬레이터에서 `http://` 주소를 쓰려면 개발 중에 한해 평문 통신 허용 설정이 필요합니다. 배포 때는 HTTPS 를 씁니다.

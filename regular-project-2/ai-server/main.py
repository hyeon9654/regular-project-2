from fastapi import FastAPI

app = FastAPI(title="regular-project-2 ai-server")


@app.get("/health")
def health():
    """서버가 떠 있는지 확인하는 용도."""
    return {"status": "ok"}


# 앞으로 추가할 것
# - POST /classify : 소명 증빙자료 이미지 분류 (전이학습 모델)
# - POST /fraud    : 이상거래 탐지
# - POST /forecast : 국내 증시 예측
#
# 원칙
# - 이 서버는 DB에 직접 접속하지 않는다. 필요한 데이터는 internal-server 가 넘겨준다.
# - 인터넷에서 직접 호출되지 않는다. internal-server 만 호출한다.

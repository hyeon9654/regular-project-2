# ai-server

Python FastAPI 서버입니다. 지금은 상태 확인 API 하나만 있습니다.

## 실행

```bash
python -m venv venv
source venv/bin/activate   # 윈도우: venv\Scripts\activate
pip install -r requirements.txt
uvicorn main:app --port 8000
```

`http://localhost:8000/health` 에서 `{"status":"ok"}` 가 나오면 정상입니다.
`http://localhost:8000/docs` 에서 API 문서를 볼 수 있습니다.

## 모델 파일

학습은 Colab에서 하고, 가중치 파일(`.pt` 등)은 S3에 올립니다.
모델 파일은 Git에 올리지 않습니다.

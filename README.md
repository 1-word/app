# 📦 VocaBox
🚀 **서비스 주소:** [https://vocabox.kro.kr](https://vocabox.kro.kr)  
자유롭게 단어를 커스터마이즈하여 저장하고 관리할 수 있는 **단어장 서비스**를 제공합니다.

---

## 📖 목차
- [🛠 기술 스택](#🛠-기술-스택)
- [✨ 주요 기능](#✨-주요-기능)
- [🐞 트러블 슈팅](#🐞-트러블-슈팅)

---

## 🛠 기술 스택

<details>
  <summary>🔧 기술 목록 펼쳐보기</summary>  

### ⚙️ **Backend**
<img src="https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
<img src="https://img.shields.io/badge/Spring%20Security-6DB33F?style=for-the-badge&logo=Spring-Security&logoColor=white">
<img src="https://img.shields.io/badge/Spring%20Batch-6DB33F?style=for-the-badge&logo=Spring&logoColor=white">
<img src="https://img.shields.io/badge/JPA-59666C?style=for-the-badge&logo=Hibernate&logoColor=white">
<img src="https://img.shields.io/badge/QueryDSL-0083CD?style=for-the-badge&&logoColor=white">
<img src="https://img.shields.io/badge/OAuth2-4285F4?style=for-the-badge&logo=OAuth&logoColor=white">
<img src="https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=json-web-tokens&logoColor=white">
<img src="https://img.shields.io/badge/Jsoup-1177AA?style=for-the-badge&logo=WebStorm&logoColor=white">

### 🗄 **Database**
<img src="https://img.shields.io/badge/MariaDB-003545?style=for-the-badge&logo=mariaDB&logoColor=white">
<img src="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=Redis&logoColor=white">

### 📊 **Monitoring**
<img src="https://img.shields.io/badge/Prometheus-E6522C?style=for-the-badge&logo=Prometheus&logoColor=white">
<img src="https://img.shields.io/badge/Grafana-F46800?style=for-the-badge&logo=Grafana&logoColor=white">

### 🚀 **Deployment**
<img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=Docker&logoColor=white">
<img src="https://img.shields.io/badge/GitHub%20Actions-2088FF?style=for-the-badge&logo=GitHub-Actions&logoColor=white">

</details>

---

## ✨ 주요 기능

- **단어 관리**: 자유롭게 단어를 추가, 수정, 삭제  
- **소셜 로그인 지원**: 카카오, 네이버, 구글 로그인  
- **자동 완성 및 사전 연동**
  - 약 13만 개 영어 단어를 Redis Sorted Set에 저장
  - JSoup을 활용한 다음 단어 사전 크롤링  
- **발음 파일 자동 생성** (Gtts 모듈 연동)  
- **날짜별 문장 관리**
  - 연관 단어 강조 표시 및 단어 연동  
- **단어 퀴즈 및 결과 저장**
  - 사용자 단어장 기반 퀴즈 생성
  - 퀴즈 이어하기 지원
  - 퀴즈 결과는 7일 후 자동 삭제

---

## 🐞 트러블 슈팅

🔹 **[소셜 로그인 시 토큰 값을 받을 수 없는 이슈](https://github.com/1-word/app/issues/15)**  
🔹 **[JPA N+1 문제 해결](https://github.com/1-word/app/issues/18)**  
🔹 **[JPA 일대다 관계에서 페이징 문제](https://github.com/1-word/app/issues/20)**  
🔹 **[멀티 모듈에서 트랜잭션 적용 이슈](https://github.com/1-word/app/issues/56)**

---

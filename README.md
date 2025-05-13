# 🧑🏻‍🌾 Farm2You
"Farm2You" 백엔드 개발 저장소 입니다.

### 🧑🏻‍💻 Developers 

|                                                         BE                                                         |                                                               BE                                                               |
|:------------------------------------------------------------------------------------------------------------------:|:------------------------------------------------------------------------------------------------------------------------------:|
| [<img src="https://avatars.githubusercontent.com/bum0w0" width="100px;" alt="bum0w0"/>](https://github.com/bum0w0) | [<img src="https://avatars.githubusercontent.com/lhimjy" width="100px;" alt="lhimjy"/>](https://github.com/lhimjy) |
|                                                      **김진범**                                                       |                                                            **임준용**                                                             |


###  📁 Development Environment

| IDE             | IntelliJ IDEA    |
|-----------------|------------------|
| **Language**    | Java 17          |
| **Framework**   | SpringBoot 3.4.5 |
| **Build Tools** | Gradle 8.13      |
| **DataBase**    | MySQL 8.4.4  |


### 🏷️ Commit Convention

|태그 이름|태그 설명|
|:---:|:---:|
|Feat|새로운 기능 추가|
|Fix|버그 수정|
|Build|빌드 관련 파일 수정|
|Design|CSS를 포함 UI 디자인 변경|
|Docs|문서(문서 추가, 수정, 삭제)|
|Style|스타일(코드 형식, 세미콜론 추가: 비즈니스 로직에 변경 없는 경우)|
|Refactor|코드 리팩토링|
|Comment|필요한 주석 추가 및 변경|
|Test|테스트(테스트 코드 추가, 수정, 삭제: 비즈니스 로직에 변경 없는 경우)|
|Rename|파일, 폴더명 이름 수정|
|Remove|파일, 폴더 삭제|

#### Example of Subject

> [Feat] 회원가입 API 구현

### 🎋 Branch Convention
- **main**: 배포용 브랜치 (항상 안정적인 상태 유지)
- **develop**: 통합 개발 브랜치 (다음 배포를 준비)
- **feature/**: 기능 개발 및 이슈 해결 브랜치 (작업 단위)
#### Branch Flow
 ```
Main Branch
  ▲
  └── Develop Branch ── 테스트 완료 후 병합 
                              ▲
                              └── Feature Branch ── 작업 완료 후 병합 
                                          └── 새로운 기능 추가

 ```

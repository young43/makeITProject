# makeIT Project

안드로이드로 개발한 팀빌딩 플랫폼 <b>makeIT</b> 입니다.

Github: https://github.com/young43/makeITProject

<br>

## 목차

1. 서비스 소개

2. 팀 정보
3. 버전 및 실행환경
4. 주요 기능
5. 특이사항

<br>

## 1. 서비스 소개

<b>makeIT </b> 서비스는 스타트업 팀 빌딩 뿐만 아니라, 프로젝트를 같이 진행하고자 하는 팀 또는 팀원들을 구할 수 있는 '팀 빌딩' 플랫폼입니다. 

코로나로 인해 비대면 교육이 활성화되면서, 팀 프로젝트 인원을 구하는 것이 생각보다 어려움을 인지하였습니다.  '에브리타임' 같은 커뮤니티를 통해서 팀을 구하는 것도 가능하지만, '팀 빌딩' 플랫폼을 사용한다면 팀빌딩이 조금 더 수월할 것이라는 기대감이 생기게 되었습니다. 

따라서 <b>makeIT</b> 팀 빌딩 플랫폼을 기획하게 되었고, 아래와 같은 목적성을 가진 프로젝트를 진행하게 되었습니다.

- 초기 스타트업 팀원을 구성할 수 있다.
- 프로젝트를 진행할 팀/팀원을 구할 수 있다.
- 기업(스타트업)이 아니어도, 개인이 프로젝트를 진행할 팀원을 구할 수 있다.
- 자신의 프로필(이력서) 관리가 가능하다.
- 서로 원하는 조건에 충족하는 사람을 쉽고 빠르게 찾을 수 있다.

<b>makeIT</b> 는 '만든다' 라는 의미를 가지고 있지만, '해낸다' 라는 뜻도 가지고 있습니다. 어플명 <b>makeIT</b> 는 팀을 구상하고, 프로젝트를 성공적으로 수행하고자 하는 의미에서 부여하였습니다. 

<br>

## 2. 팀 정보

| 이름   | 소속       | 역할                                | github                         |
| ------ | ---------- | ----------------------------------- | ------------------------------ |
| 김지홍 | 국민대학교 | UI 구성 및 로그인 기능 개발         | https://github.com/kimjihong9  |
| 김찬미 | 국민대학교 | 프론트엔드 개발 및 이력서 기능 개발 | https://github.com/kimchanmiii |
| 윤서영 | 국민대학교 | Firebase 연동 및 Bookmark 기능 개발 | https://github.com/young43     |

<br>

## 3. 버전 및 실행환경

- 개발 환경

  - Language: Java
  - IDE: Android Studio

- 실행 환경

  - AVD: Android Q(10.0 x86_64)
  - minSDKVersion: 16
  - compileSDKVersion: 30
  

<br>

## 4. 주요 기능

### 계정 관리

- 로그인을 하지 않았던 상태라면, 초기화면에 로그인과 회원가입 화면이 표시됩니다. 
- 계정이 존재하면 로그인할 수 있고, 없다면 회원가입을 진행할 수 있습니다.
- 한번 로그인하면 세션이 유지되어 자동으로 로그인이 됩니다.
- 로그인 후 [MY] 탭에서 '로그아웃' 버튼 클릭시, 로그아웃이 가능합니다.

<img src="https://github.com/young43/makeITProject/blob/main/img/login_join.jpg?raw=true" alt="두번째 화면" width=500/>

<br>

### 이력서

- 각 회원들은 자신들의 이력서를 등록하고 관리할 수 있습니다. 
- 이름, 이메일, 연락처, 자기소개, 경력을 입력하고 이력서 등록이 가능합니다.  
- 경력은 최대 3개까지 입력할 수 있으며, 입사/퇴사 날짜를 입력할 수 있습니다. 
- 이력서를 등록하면 Firebase DB에 내용이 저장되고, 다시 이력서를 등록하려고 하면 이전에 저장했던 내용을 불러오게 됩니다.

<img src="https://github.com/young43/makeITProject/blob/main/img/resume.jpg?raw=true" alt="두번째 화면" width=500/>

<br>

### 찜(Bookmark)

- 로그인 후 [공고] 탭을 클릭하게 되면, 현재 등록된 프로젝트 목록을 볼 수 있으며, 마음에 드는 프로젝트는 '하트' 버튼을 통해 찜할 수 있습니다.
- [HOME] 탭에서 '찜 목록' 버튼을 누르면 찜해놨던 프로젝트 리스트를 볼 수 있습니다.
- 찜 목록에서 삭제를 원하면 다시 '하트' 버튼을 클릭하면 됩니다. 

<img src="https://github.com/young43/makeITProject/blob/main/img/bookmark.jpg?raw=true" alt="두번째 화면" width=500/>

<br>

### 프로젝트 관리

- 프로젝트 등록 및 조회
  - 모든 회원들은 프로젝트를 등록할 수 있으며, 프로젝트 삭제 권한은 등록한 회원에게 있습니다.
  - 제목, 지역, 인원, 내용, 담당 연락처 내용들을 입력하고 프로젝트 등록이 가능합니다. 
  - 등록한 프로젝트는 [공고] 탭에서 확인 가능하며, 프로젝트 제목 및 내용으로 검색이 가능합니다. 
  

<img src="https://github.com/young43/makeITProject/blob/main/img/project_show.jpg?raw=true" alt="두번째 화면" width=500/>

- 프로젝트 지원
  - 프로젝트 목록을 클릭하게 되면, 관련 프로젝트 정보가 화면에 표시됩니다.
  - 관심이 있는 프로젝트에 지원가능하며, 지원할 경우 이메일로 연동되어 자신의 이력서를 자동으로 불러오게 됩니다. 
  - [HOME] 탭 '진행 중인 프로젝트'에서 본인이 지원한 프로젝트를 확인할 수 있습니다.
  - 해당 프로젝트를 롱 클릭 시  삭제가 가능합니다. 
  

<img src="https://github.com/young43/makeITProject/blob/main/img/project_resume.jpg?raw=true" alt="두번째 화면" width=500/>



# 프로젝트 개요

페이스북, 트위터와 비슷한 SNS앱 서버 사이드 프로젝트입니다. SNS의 회원, 포스팅, 팔로우 기능을 구현과 더불어 인프라 구축을 하였습니다.

## 가정과 목표
- 평균적으로 300명의 팔로워가 있습니다.
- 일일 활성 사용자는 1000만명이며 각 사용자는 하루에 5번 뉴스피드를 가져옵니다.
- 하루 5000만 회, **초당 대략 578회 요청**이 발생합니다.

## 프로젝트 진행 상황

### 1차 부하 테스트
고가용성 서비스를 위해 부하 테스트를 진행하였습니다. 개선점을 찾아 수정 후 2차 부하 테스트를 진행할 예정입니다.

**스케일 아웃**

서버 확장 전략으로 스케일 아웃을 선택하였습니다. 스케일 업 전략으로는 컴퓨터 자원이 향상됨에 따라 가격 또한 매우 급격하게 상승하는 문제와 하나의 서버는 다운될 경우 서비스 전반에 문제가 생기기 때문에 스케일 아웃을 필수적이라고 생각하였습니다.

**코드형 인프라**

AWS 리소스를 생성하는 작업은 30분 ~ 1시간 정도 소요되었습니다. Terraform을 사용하여 명령어 한번에 VPC, EKS Cluster, Managed node group 등을 생성하였습니다. 이후 시간 절약과 휴먼 에러를 방지할 수 있어 매우 잘한 일 중 하나가 되었습니다.

Kubernetes와 Helm을 통해 다수의 Server application, Monitoring tool, Load test tool을 실행하는데 몇개의 명령어로 가능하게 되었습니다.

**CI 환경 구축**

Github Actions를 통해 main 브랜치로 병합될 때 테스트 코드를 실행, 컨테이너 빌드, 빌드된 이미지를 저장소에 저장하도록 하였습니다.

**개선점**



###



## 프로젝트 아키텍처 다이어그램

![project](https://github.com/sunho-lee/super-sns/assets/27765412/c5710917-92bd-4b14-99fd-01d03c7e67cd)

## 인프라 다이어그램

![infra](https://github.com/sunho-lee/super-sns/assets/27765412/9e340fdf-3b1a-4fc0-ad80-91238d86e1e3)

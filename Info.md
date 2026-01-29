## *.jar
- *.jar -> boot jar으로 애플리케이션으로 띄울 수 있는 jar
- plain.jar -> 순수 애플리케이션?


## 테스트 빌드
- ./gradlew clean test --info : 테스트 후 clean하면 libs가 사라짐
- ./gradlew build : 빌드
- ./gradlew clean build --info

## 실행방법
java jar {filename}.jar

## keypair 저장
- 로컬 ~/.ssh/ 경로에 저장
- 권한 변경 chmod 600 interview-key-pair.pem 

## ec2 server 실행
- ssh -i ~/.ssh/mock-interview-key-pair.pem ec2-user@3.38.178.21

## 원격지로 파일 전송 방법
- scp
- rsync (고급기능)

## amazon에 jar로 서버를 구동시켜도 동작을 안한다.
- why? 방화벽때문  -> 비밀키를 통해서만 접속을 가능하기 떄문에 현재 ssh 22번 포트로만 접속을 할 수 있게 해놓았다.


## swagger 경로
- http://3.38.178.21:8090/swagger-ui/index.html

## 무중단 프로세스
- nohup
  - nohup java -jar ~~~
  - 터미널을 종료해도 무시하고 실행됨
  - nohup java -jar {} & 사용법을 숙지해라 -> 프로세스를 백그라운드로 넘기는 명령어

## 현재 시스템에 띄워진 전체 프로세스
- ps -aux

## 포트찾기
- netstat -lntp

주의할 점
- 탄력적ip를 ec2에 연결을 해지한 상태로 두게 되면 과금이 되니 주의해야한다.
- ec2와 연결만 되어 있으면 무료다

## nginx
- web server을 앞에 둬서 처리
- 속도 빠른 웹서버(오버헤드가 적다)
- yum install epel-release
- 재부팅 후에도 nginx가 자동으로 실행되게 해주는 명령어
  - systemctl enable nginx
- nginx 설정파일에 이상이 있는지 확인하는 방법
  - nginx -t
- journalctl -xe
- journalctl -u nginx
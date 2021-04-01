# soft_squared

## 소프트스퀘어드 512 Class Android 

- 1주차 - Android 이해
- [2주차 - LifeCycle](#myactivitylifecycle)
- [3주차 - ListView](#mylistview)
- [4주차 - Multi-Thread](#mythread)
- 5주차 - Network Intro & API
- 6주차 - Token & Restful


----------------------
### MyActivityLifeCycle
2주차 과제 - MyActivityLifeCycle

- MainActivity가 onCreate되면 Theme을 변경하는 식으로 Splash 화면 구성
- BottomNavigationView를 통해 Fragment 화면 전환
- RecyclerView와 Horizontal ScrollView 등을 활용한 퍼블리싱
- Exoplayer를 라이브러리를 활용하여 Video 스트리밍 가능
- Activity LifeCycle에 맞춰서 앱 밖으로 벗어나면 정지하고 다시 돌아오면 보고있던 장면부터 다시 재생
- SharedPreference를 활용하여 각 영상과 채널마다 구독, 좋아요 및 싫어요 저장
- 댓글창과 대댓글 창 화면 전환시 애니메이션 효과

![week2-1](https://user-images.githubusercontent.com/67727981/113272318-749de300-9316-11eb-8d95-2c4b90c07f23.png)
![week2-2](https://user-images.githubusercontent.com/67727981/113272322-75cf1000-9316-11eb-80c8-d00603a31824.png)
![week2-3](https://user-images.githubusercontent.com/67727981/113272324-75cf1000-9316-11eb-8ee0-3c2efe64bf88.png)
![week2-4](https://user-images.githubusercontent.com/67727981/113272327-7667a680-9316-11eb-8c56-0cd97c80f2e1.png)
![week2-5](https://user-images.githubusercontent.com/67727981/113272329-7667a680-9316-11eb-8411-e925c9b78510.png)
![week2-6](https://user-images.githubusercontent.com/67727981/113272331-77003d00-9316-11eb-9bbe-e3e7b2db2c12.png)
![week2-7](https://user-images.githubusercontent.com/67727981/113272333-77003d00-9316-11eb-8b3d-e4e6c793fbab.png)


-----------------

### MyListView
3주차 과제 - MyListView

- MainActivity가 onCreate되면 Theme을 변경하는 식으로 Splash 화면 구성
- BottomNavigationView를 통해 Fragment 화면 전환
- RecyclerView와 Horizontal ScrollView 등을 활용한 퍼블리싱
- SQLite를 사용하여 메세지 보낼 상대를 추가할 수 있고 각 채팅방마다 대화내용을 DB에 저장
- 나인패치를 이용해 말풍선을 만들어서 텍스트의 길이가 다르더라도 같은 모양의 말풍선 삽입
- 채팅방에서 대화를 하고 다시 채팅방 목록을 보면 마지막에 한 대화 및 날짜 업데이트
- RecyclerView에 Filter 인터페이스를 상속받아 DB에서 원하는 대화상대 검색 필터링 가능
- 라이브러리를 사용하여 옆으로 스와이프 제스쳐를 하면 숨어있던 버튼들이 나오고 대화목록에서 삭제 가능

![week3-1](https://user-images.githubusercontent.com/67727981/113273273-75834480-9317-11eb-8706-c5aa646e9362.png)
![week3-2](https://user-images.githubusercontent.com/67727981/113273277-761bdb00-9317-11eb-9f00-6d10472a33f0.png)
![week3-3](https://user-images.githubusercontent.com/67727981/113273278-761bdb00-9317-11eb-826b-0dec4f4623cf.png)
![week3-4](https://user-images.githubusercontent.com/67727981/113273280-76b47180-9317-11eb-91d2-383173a13176.png)
![week3-5](https://user-images.githubusercontent.com/67727981/113273283-76b47180-9317-11eb-84a4-46ca99cea1de.png)
![week3-6](https://user-images.githubusercontent.com/67727981/113273284-774d0800-9317-11eb-978d-881cdae48a8c.png)
![week3-7](https://user-images.githubusercontent.com/67727981/113273286-774d0800-9317-11eb-9858-94e18fdfe0f9.png)
![week3-8](https://user-images.githubusercontent.com/67727981/113273269-74521780-9317-11eb-9698-c9c54bb089ac.png)

-----------------

### MyThread
4주차 과제 - Multi-Thread

- MainActivity가 onCreate되면 Theme을 변경하는 식으로 Splash 화면 구성
- Dialog를 띄워서 인터페이스 콜백을 통해 배경음악 및 효과음 On/Off 가능
- SQLite와 RecyclerView를 사용해서 게임 기록을 저장하고 랭킹 테이블표 확인 가능
- 처음 게임을 시작하면 Thread를 사용해 3초간 준비시간을 주고 게임이 시작된다.
- Thread를 통해서 게임 timer가 작동되고 이는 랭킹에 등록되는 점수로 사용한다.
- 5x5의 배열에 1부터 25까지의 수가 랜덤으로 배정되고 타겟 넘버가 1부터 클릭할때마다 하나씩 올라가고 그 자리는 26부터 50까지의 수가 다시 랜덤으로 채운다. 26부터는 타일을 클릭하면 빈자리로 대체한다.
- 2초간 정답 타일을 클릭하지 못하면 Thread를 통해서 빨간 테두리로 힌트를 제공한다.
- 만약 앱 밖으로 나가게되면 게임 진행시간을 체크하는 Timer를 멈추고 다시 돌아오면 3초간의 준비시간 후에 다시 재개된다.
- 게임이 끝나게되면 Time Score를 통해서 이름을 입력하고 랭킹에 등록한다.

![녹화_2021_03_04_09_44_01_133](https://user-images.githubusercontent.com/67727981/113276068-6baf1080-931a-11eb-8907-d255920fb066.gif)

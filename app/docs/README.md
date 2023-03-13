# 프로젝트를 진행하며 공부한 것 및 정리할 것

1. [Compose에서 마지막 자리만 보이는 텍스트 필드를 만들어 보자 - BasicTextField 커스텀 좌충우돌](https://velog.io/@cksgodl/AndroidCompose-Compose%EC%97%90%EC%84%9C-%EB%A7%88%EC%A7%80%EB%A7%89-%EC%9E%90%EB%A6%AC%EB%A7%8C-%EB%B3%B4%EC%9D%B4%EB%8A%94-%ED%85%8D%EC%8A%A4%ED%8A%B8-%ED%95%84%EB%93%9C%EB%A5%BC-%EB%A7%8C%EB%93%A4%EC%96%B4-%EB%B3%B4%EC%9E%90)
2. [Window Insets을 Compose에서 다루는 방법](https://velog.io/@cksgodl/AndroidCompose-WindowInsets%EC%9D%84-Compose%EC%97%90%EC%84%9C-%EA%B4%80%EB%A6%AC%ED%95%98%EB%8A%94-%EB%B0%A9%EB%B2%95)
3. 상태 호이스팅과 `Preview`를 활용해 화면 개발하기
4. [Compose 에서 안드로이드 권장 아키텍쳐를 예시와 함께 알아보자.](https://velog.io/@cksgodl/AndroidCompose-Compose-%EC%97%90%EC%84%9C-%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9C-%EA%B6%8C%EC%9E%A5-%EC%95%84%ED%82%A4%ED%85%8D%EC%B3%90%EB%A5%BC-%EC%98%88%EC%8B%9C%EC%99%80-%ED%95%A8%EA%BB%98-%EC%95%8C%EC%95%84%EB%B3%B4%EC%9E%90)
5. `CustomBottomSheet`만드는 과정과 동작 방법 (`rememberBottomSheet`를 활용해 `Stateholder`를 만들어 재사용하는 과정)
    * 내가 만든 CustomBottomSheet 공유
6. 람다 식을 전달할 때의 멤버참조와 코틀린의 리플렉션에 관하여
7. Compose에서 BroadCast를 활용하여 인증문자에서 번호 가져오기 및 카메라 FileProvider를 활용하여 카메라 이미지 가져오기
    * contentUri와 fileUri의 차이점은 무엇인지
8. Authenticator를 활용해 401에러일 때 바로 토큰 재발급 하기
9. 인스타그램과 같이 이미지 위에 글자를 넣어서 저장해보자.
10. (@+) 네트워크 요청을 실패, 성공, 로딩 중일 때 화면 처리
11. Firebase - Analytics 및 Crashlytics를 활용해 앱 사용자에 대한 데이터를 수집해보자.

---

# 기능 요구 사항 및 구현 정리 ✅

## 회원가입, 로그인 화면 📕

로그인, 회원가입에 필요한 모든 도메인 로직체크는 `모델 객체`가 일하도록 만들었다.

### 로그인 뷰

* 로그인을 진행하며 `AccessToken`과 `RefreshToken`을 업데이트한다.

#### 카카오로 로그인

* `Kakao SDK`를 활용하여 로그인을 진행하며 다음과 같은 분기처리를 따른다.
    1. 유저 정보를 입력한 적이 없다면 `SignInProfilImage`뷰로 이동하여 마저 회원가입을 진행한다.
    2. 유저 정보를 입력한 적이 있다면 정보를 불러오고 메인화면으로 이동한다.

#### 전화번호로 로그인

로그인시 전화번호 및 비밀번호를 입력할 때 다음과 같은 규칙을 따라야 한다.

* 전화번호 입력 : (-제외하고 작성)
    * 숫자만 입력 -> `TextField`의 키보드 타입을 `Number`로 제한
* 비밀번호 입력 : 평상시 비밀번호 입력시 •••••a 표기. 마지막 자리만 보여주고, 나머지는 가림 표시. 비밀보기 버튼 눌렀을 경우 비밀번호 전체 공개. 버튼을 누르고 입력
  시 비밀번호 계속 보여짐
    * 비밀번호 입력시 마지막 번호가 보이는 것은 `VisualTranformation`을 커스텀한 `LastPasswordVisibleVisualTransformation`
      를 `TextField`에서 사용한다. 이는 `LastPasswordVisibleCustomTextField`컴포저블로 구현이 되어 있다.

### 회원가입 (유저 인적사항 입력)

유저 정보 예외처리 사항

* 이름 및 국적 : `NameAndNationality` -> 이름이 공백만 아니면 됨
* 성별 : `Gender` -> Unknown만 아니면 됨
* 생년월일 : `Birth` -> 숫자만 입력, 정확히 8자
* 휴대폰 번호 : `Phone` -> 번호가 정확히 11자리 여야함(하이픈 없음)

사용자는 해당 예외처리를 모두 지켜야 버튼이 enable된다.

인증 문자요쳥 버튼이 눌릴 시 3분 타이머를 `SignInViewModel`에서 돌리며 이시간내에 인증번호 검증을 수행해야한다.

### 회원가입 (인증번호 검증)

인증번호는 백스택으로 돌아와도 유지되게 진행한다.
`SignInViewModel`에 존재하는 `Timer`를 통해 3분을 측정하며 0초 이하가 되면 인증이 불가능하다.

재요청 버튼을 누르면 타이머가 초기화(3분)되며, 유저의 전화번호로 다시 문자가 간다.

`SystemBroadcastReceiver`를 활용해 문자가 왔을 때 해당 문자를 파싱하며 자동으로 인증번호를 채운다.

사용자는 6자리의 인증번호를 입력해야 버튼이 enable된다.

### 회원가입 (비밀번호)

`Password` 비밀번호는 숫자, 영문으로 이루어지며, 8자 이상 16자 이하여야 한다.

사용자는 비밀번호 형식을 맞춰야 버튼이 enable된다.

### 회원가입 (프로필 이미지, 닉네임)

* 닉네임 : 한글, 영어, 숫자 혼합 2~10글자이여야 하며 서버와의 중복체크를 거쳐야 한다.

정규식은 다음과 같다.

```
"""^[ㄱ-ㅎ|가-힣|a-z|A-Z|0-9|]{3,10}$"""
```

* 프로필 이미지
    * Oauth로그인은 SNS 프로필이미지를 사용할 수 있다.
    * 전화번호 로그인은 갤러리나 카메라로 사진찍기가 가능하다. (`외부 저장소`, `카메라` 접근 권한 필요)

#### 여기서 프로필 이미지에 관하여

프로필이미지는 `ProfileImageType`를 사용해 `sealed class`로 관리하되 서버의 파라미터에 대한 분기처리를 다음과 같이 한다.

서버로 보내는 이미지에 관한 파라미터는 `profileImgUrl: String`과 `multipartFile: MultiPart`로 나뉜다.

1. 아무런 이미지를 선택하지 않았을 때 (`ProfileImageType.DEFAULT`) `profileImgUrl`에는 공백("")을, `multipartFile`
   에는 `null`을 보낸다.
2. 기기내의 이미지를 선택했을 때 `Uri`로 부터 `Path`를 추출하여 `File`을 찾은 뒤 해당 파일을 서버로 보낸다. (`profileImgUrl`에는 공백("")을
   보낸다.)
3. 소셜로그인은 제공되는 `profileImgUrl`을 통해 이미지 주소를 가져오되, 유저가 따로 이미지를 설정하거나 이미지를 바꾸면 1, 2번 플로우로 동일하게 진행한다.

### 카테고리 선택

카테고리는 최소 1개, 최대는 제한이 없다.

---

## 지도 화면 📗

초기 로딩 시 사용자 위치정보를 가져오고, 네이버 지도가 준비되면 해당 위치로 카메라를 이동시키고 관련 쇼룸 정보를 가져온다.

초기 로딩 플래그는 `MapViewModel.initialMarkerLoadFlag`을 싱글톤으로 선언하여 사용하며 이후에는 로딩하지 않는다.

* 마커 정보는 마커 클러스터 기능을 제공해야 한다.
    * 지도는 [컴포즈 네이버 지도](https://github.com/fornewid/naver-map-compose)를 활용한다.
    * 마커 클러스터 기능은 [TedClustering](https://github.com/ParkSangGwon/TedNaverMapClustering)을 활용하여 구현한다.

* 바텀시트의 쇼룸 정보는 페이징을 제공해야 한다.
    * Issue : BottomSheet안에 페이징을 적용시키면 자동으로 Exapned되는 현상이 발생한다. 이를 방지하기 위해 Exapnded가 아니라면
      PagingContent에 대한 Height를 제한한다. (`Modifier.pagingHeight`)

> 바텀시트의 쇼룸 선택 시 디테일 화면으로 이동해야한다.

## 지도 관련 상태 및 터치 이벤트

### 상단 카테고리 선택 시

마커 정보를 가져올 때 북마크에 대한 정보는 프론트에서 처리한다. (`_isBookmarked`를 통해 처리)
마커 및 하단 바텀시트의 내용을 선택된 카테고리와 관련된 내용으로 변경한다. 플로우는 다음과 같다.

1. `mapFiltering(latLng: LatLng)`에서 카메라의 현재 위치에서의 마커를 검색하여 가져온다.
2. `mapScrollInfoPaging(latlng: LatLng)`에서 바텀시트의 내용을 업데이트 한다.(페이징 제공)

### 마커 선택 시

1. 마커의 이미지 및 크기를 변경한다.
2. 바텀 시트의 내용을 해당 마커에 관한 내용으로 변경한다.
    * `mapInfo(storeId: Int)`에서 해당 역할 수행
3. 지도를 클릭할 시 선택 마커를 초기화 하고 기존 바텀시트 내용으로 변경해야 한다.
4. 바텀시트의 쇼룸 선택 시 디테일 화면으로 이동해야한다.
    * `saveTempDatas` 및 `loadTempDatas`를 통해 데이터를 임시 저장하고 불러온다.

### 지도를 클릭 시

1. 마커가 선택중일 때
    * 선택 마커를 초기화 하고 마커 선택 전 바텀시트의 내용을 불러온다.

2. 마커가 선택중이 아닐 때
    * 지도를 줌 상태로 변경한다.(바텀시트 및 네비게이션바를 내리고 스테이터스바를 투명하게 하고, 검색 바를 올린다.)

3. **검색을 한 이후 지도일 때 (장소 검색, 쇼룸 검색)**
   검색 탭에서 지역명이나 쇼룸명을 검색하여 지도가 표시됬을 때는 지도를 줌 상태로 변경한다.
   반대로 지도를 한번 더 선택하면 지역명이나 쇼룸명을 검색했을 때 지도로 돌아간다.

지도에서 사용되는 맵 상태는 `enum class MapStatus`에 정의되어 있으며 각각 뜻하는 바는 다음과 같다.

```
enum class MapStatus {
    DEFAULT, // 맵의 기본 상태, 처음 들어왔을 때
    ZOOM, // 기본상태에서 맵을 클릭했을 때, StatusBar, NavigationBar가 투명해져야되며 핸드폰 전체에 맵뷰가 드러나야 한다.
    MARKER_CLICKED, // 기본 상태에서 마커를 클릭했을 때
    LOCATION_SEARCH, // 지역 검색을 했을 때 
    LOCATION_SEARCH_MARKER_CLICKED, // 지역 검색을 한 후 마커를 클릭했을 때
    SHOP_SEARCH, // 쇼룸 검색을 했을 때
    SEARCH_ZOOM, // 검색 결과 화면에서 화면을 선택했을 때 
    SEARCH_TAB; // 검색 탭에 들어왔을 때 -> 새로운 View로 이동하지 않는다(맵 뷰 다시 로딩 오래걸려서...)
}
```

마커와 지도에 대한 클릭 이벤트는 해당 `MapStatus`에 따라 다르게 작동하며, 이는 `onMarkerClick`, `onMapClick`의 람다 식으로 선언되어 있다.

### 검색 탭을 클릭 시 (검색 화면)

1. 검색 화면으로 이동함과 동시에 키보드 포커스를 요청해야 한다.
    * `focusManager.requestFocus()`를 통해 포커스를 요청한다.
2. 검색 화면과 지도화면은 같은 네이버지도를 공유함으로 검색 화면에 들어오기 전 마커 상태, 바텀시트 상태를 저장한 후 지도화면으로 돌아올 때 상태를 복구해야한다.
    * `saveTempDatas` 및 `loadTempDatas`를 통해 데이터를 임시 저장하고 불러온다.
3. 최근 검색어를 제공해야 한다.
    * RoomDB를 활용하여 로컬에서 처리한다.
4. 검색어를 입력하면 디바운싱 및 동일한 글자는 강조로 표시해야한다.
    * 디바운싱은 `mapSearch()`에서 제공되며 0.2초 내에 같은 요청이 들어올 시 이전 `Job`을 취소하고 다시 요청하는 식으로 구현.
5. 지역명 검색, 쇼룸 검색을 나누어 제공해야 한다.
    * 지역명 검색 결과일 때는 바텀시트가 화면 전체까지 올라가야 하며 쇼룸 검색 결과일 때는 바텀시트가 화면의 일부만 올라가야 한다.

### GPS 아이콘 클릭 시

유저의 현재 위치로 카메라를 이동시켜야 한다.

* `MovingCameraWrapper` Enum 클래스를 정의하여 해당 클래스가 `MOVING`일 때 카메라를 이동시킨다.

```
LaunchedEffect(key1 = uiState.movingCameraPosition) {
    when (uiState.movingCameraPosition) {
        MovingCameraWrapper.DEFAULT -> {
            // Do Nothing
        }
        is MovingCameraWrapper.MOVING -> {
            cameraPositionState.animate(
                update = CameraUpdate.scrollAndZoomTo(
                    LatLng(uiState.movingCameraPosition.location), 13.0
                )
            )
            mapViewModel.updateMovingCamera(MovingCameraWrapper.DEFAULT) // 한번 움직였으면 다시 움직이지 않도록 상태를 초기화
        }
    }
}
```

2. 유저의 위치는 5초마다 받아서 업데이트한다.

### 현재 위치에서 검색 시

1. 카메라의 현재 포지션을 기반으로 쇼룸을 검색한다.
    * 카메라의 포지션은 `ApplicationState`에서 전역으로 관리한다. (다른 뷰에 들어갔다 나와도 해당 포지션을 유지하도록)

```
@Stable
class ApplicationState(
    // ...
    val cameraPositionState: CameraPositionState
)
```

2. 검색에 관한 필터는 상단 카테고리바에 의해 이루어진다.
    * (상단 카테고리 선택 시)를 검색하여 해당 내용 확인

---

## 쇼룸 디테일 화면 📖

1. 쇼룸이미지, 매장명, 전화번호, 등등의 정보를 제공해야 하며 정보가 로딩되며 스켈레톤 UI를 제공해야한다.
    * 스켈레톤 UI는 다음과 같은 확장함수를 제공한다.

```
// 해당 변수가 공백이라면 155 x 18의 크기로 스켈레톤 UI를 제공한다.
`String_변수`.skeletonUI(size = 155.dp to 18.dp) {
    // Contents
}
```

2. 유저 리뷰는 페이징을 제공해야 한다.
3. 블로그 리뷰선택 시 웹뷰를 활용해 표시해야 한다.
   웹뷰는 그래프에 선언되어 있으며

```
route = "${WEB_VIEW_ROUTE}?title={title}&url={url}"
```

과 같은 형식으로 라우팅을 통해 데이터를 전달한다.

## 리뷰 작성 및 디테일 화면 ✅

리뷰 작성 시 카메라 및 갤러리에서 이미지를 불러와 원하는 문구를 위에 작성할 수 있어야 한다.

1. 리뷰 작성 화면에서 텍스트 추가 버튼을 눌러 텍스트를 추가한다.
2. 해당 텍스트는 Idx가 부여되며, 이후 위치, 크기, 색상, 정렬을 변경할 수 있어야 한다.

텍스트의 수정, 크기, 색상, 정렬과 관련된 내용은 `EditUiStatus`에서 관리한다.

```
data class EditUiStatus(
    val isEdit: Boolean, // 현재 편집 모드인지, 아닌지
    val editIdx: Int, // 편집 중인 텍스트의 Idx
    val fontSize: TextUnit, 
    val fontColor: Color,
    val textAlign: TextAlign = TextAlign.Start,
    val isColorPickerVisibility: Boolean = false, // 컬러 피커의 visibility
    val textField: TextFieldValue = TextFieldValue(""), // 텍스트 필드의 값
) 
```

텍스트의 위치와 관련된 내용은 `ReviewTextField`컴포저블 내부에서 관리한다.

```
var offsetX by remember { mutableStateOf(configuration.screenWidthDp.dp.value / 2) }
var offsetY by remember { mutableStateOf(configuration.screenHeightDp.dp.value / 2) }

Box(
    modifier = Modifier
        .offset {
            IntOffset(
                offsetX.roundToInt(),
                offsetY.roundToInt()
            )
        }
        .pointerInput(Unit) {
            detectDragGestures { change, dragAmount ->
                change.consume()
                offsetX += dragAmount.x
                offsetY += dragAmount.y
            }
        }
)
```

2. 리뷰 조회 시 이미지를 좌, 우로 드래그하여 이전 리뷰, 이후 리뷰를 조회할 수 있어야한다.
3. 리뷰 조회 시 이미지의 좌측, 우측 화면을 터치하여 이전 리뷰, 이후 리뷰를 조회할 수 있어야한다.

좌, 우 이미지를 조회하는 소스는 다음과 같다.

```
var reviewDirection by remember {
    mutableStateOf(ReviewMoveDirection.DEFAULT)
}

LaunchedEffect(key1 = reviewDirection) {
    when (reviewDirection) {
        ReviewMoveDirection.LEFT -> {
            loadPreviousReview()
            reviewDirection = ReviewMoveDirection.DEFAULT
        }
        ReviewMoveDirection.RIGHT -> {
            loadNextReview()
            reviewDirection = ReviewMoveDirection.DEFAULT
        }
        ReviewMoveDirection.DEFAULT -> {
            // TO NOTHING
        }
    }
}

Box(modifier = Modifier
    .fillMaxSize()
    .pointerInput(Unit) { // 드래그 이벤트 설정
        detectDragGestures(
            onDragEnd = {
                if (abs(offsetX) <= screenWidthFloat / 2) {
                    offsetX = 0f
                } else if (offsetX < -(screenWidthFloat / 2)) {
                    reviewDirection = ReviewMoveDirection.RIGHT
                } else if (offsetX > screenWidthFloat / 2) {
                    reviewDirection = ReviewMoveDirection.LEFT
                }
            }
        ) { change, dragAmount ->
            updateOffestX(dragAmount.x)
            change.consume()
        }
    }
    .pointerInput(Unit) { // 터치 이벤트 설정
        detectTapGestures {
            reviewDirection =
                if (screenWidthFloat / 2 > it.x) {
                    ReviewMoveDirection.LEFT
                } else {
                    ReviewMoveDirection.RIGHT
                }
        }
    }
)
```

4. 하단의 장소 상세버튼을 누르면 쇼룸 디테일페이지로 이동해야 한다.
5. 리뷰는 북마크가 가능해야 한다.
6. 자신이 쓴 리뷰에 대해서는 삭제가, 남이 쓴 리뷰에 대해서는 신고가 가능해야 한다.

## 홈 화면 🏚

1. 상단 배너는 최대 10개까지 뷰페이저를 제공하며, 마지막 페이지는 항상 더 많은 쇼룸 보기 배너어야 한다.
2. 사용자 리뷰는 페이징을 제공해야 하며, 이미 읽은 기록이 있는 리뷰는 음영처리를 해야한다.
3. 상단 아이콘을 활용해 카테고리 수정이 가능해야 한다.
4. 쇼룸 선택시 디테일 화면으로 이동해야 한다.

## 마이페이지 화면 🧹

1. 내가 쓴 리뷰, 북마크한 쇼룸 및 후기에 대한 조회가 가능해야 하며 페이징을 제공해야 한다.
2. 내 프로필 이미지 및 닉네임 수정이 가능해야 한다.

## 설정 화면 ⚙️

1. 약관 상세보기, 쇼룸 추가 URL, 앱 버전 등의 정보를 제공해야 한다.
2. 로그아웃 기능을 제공해야 한다.
3. 사용자 정보 상세조회를 제공해야 한다.
    * 비밀번호 및 SNS연동 기능을 제공한다.
    * 회원 탈퇴 기능을 제공한다.
4. 알림 설정을 제공해야 한다.

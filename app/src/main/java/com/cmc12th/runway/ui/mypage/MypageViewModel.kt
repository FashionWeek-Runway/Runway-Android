package com.cmc12th.runway.ui.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.cmc12th.runway.data.response.ErrorResponse
import com.cmc12th.runway.data.response.user.MyReviewsItem
import com.cmc12th.runway.data.response.user.StoreMetaDataItem
import com.cmc12th.runway.domain.repository.AuthRepository
import com.cmc12th.runway.domain.usecase.GetMyProfileDataUseCase
import com.cmc12th.runway.ui.domain.model.RunwayCategory
import com.cmc12th.runway.ui.map.components.DetailState
import com.cmc12th.runway.ui.mypage.view.MypageTabInfo
import com.cmc12th.runway.ui.signin.SignInCompleteUiState
import com.cmc12th.runway.ui.signin.SignInProfileImageUiState
import com.cmc12th.runway.ui.signin.model.CategoryTag
import com.cmc12th.runway.ui.signin.model.Nickname
import com.cmc12th.runway.ui.signin.model.ProfileImageType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

data class MypageUiState(
    val onDetail: DetailState = DetailState.default(),
    val selectedPage: MypageTabInfo = MypageTabInfo.MY_REVIEW,
)

@HiltViewModel
class MypageViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val getMyProfileDataUseCase: GetMyProfileDataUseCase,
) : ViewModel() {

    private val _myReviews = MutableStateFlow<PagingData<MyReviewsItem>>(PagingData.empty())
    val myReviews: StateFlow<PagingData<MyReviewsItem>> = _myReviews.asStateFlow()
    private val _bookmarkedStore =
        MutableStateFlow<PagingData<StoreMetaDataItem>>(PagingData.empty())
    val bookmarkedStore: StateFlow<PagingData<StoreMetaDataItem>> = _bookmarkedStore.asStateFlow()
    private val _onDetail = MutableStateFlow(DetailState.default())
    private val _selectedPage = MutableStateFlow(MypageTabInfo.MY_REVIEW)

    private val _nickName = MutableStateFlow(Nickname.default())
    private val _profileImage = MutableStateFlow<ProfileImageType>(ProfileImageType.DEFAULT)
    private val _categoryTags = MutableStateFlow(RunwayCategory.generateCategoryTags())

    val complteUiState: StateFlow<SignInCompleteUiState> =
        combine(_profileImage, _nickName, _categoryTags) { profileImage, nickName, categoryTags ->
            SignInCompleteUiState(
                profileImage = profileImage,
                nickName = nickName,
                categoryTags = categoryTags
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SignInCompleteUiState()
        )

    val profileImageUiState: StateFlow<SignInProfileImageUiState> =
        combine(_profileImage, _nickName) { profileImage, nickName ->
            SignInProfileImageUiState(profileImage = profileImage, nickName = nickName)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SignInProfileImageUiState()
        )

    val uiState = combine(_onDetail, _selectedPage) { onDetail, selectedPage ->
        MypageUiState(
            onDetail = onDetail,
            selectedPage = selectedPage
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MypageUiState()
    )

    fun getMyReviews() = viewModelScope.launch {
        getMyProfileDataUseCase.myReviewPaging().cachedIn(viewModelScope).collect {
            _myReviews.value = it
        }
    }

    fun getBookmarkedStore() = viewModelScope.launch {
        getMyProfileDataUseCase.bookmarkedStorePaging().cachedIn(viewModelScope).collect {
            _bookmarkedStore.value = it
        }
    }

    fun updateOnDetail(state: DetailState) {
        _onDetail.value = state
    }

    fun updateSelectedPage(tabInfo: MypageTabInfo) {
        _selectedPage.value = tabInfo
    }

    fun updateProfileImage(profileImage: ProfileImageType) {
        _profileImage.value = profileImage
    }

    fun updateNickName(nickname: String) {
        _nickName.value = _nickName.value.copy(text = nickname)
    }

    fun getMyProfile() = viewModelScope.launch {
        authRepository.getProfileInfoToEdit().collect { apiState ->
            apiState.onSuccess {
                updateNickName(it.result.nickname)
                if (it.result.imgUrl == null) {
                    updateProfileImage(ProfileImageType.DEFAULT)
                } else {
                    updateProfileImage(ProfileImageType.SOCIAL(it.result.imgUrl))
                }
            }
        }
    }

    fun modifyProfile(
        onSuccess: () -> Unit,
        onError: (ErrorResponse) -> Unit,
    ) = viewModelScope.launch {
        val basic = when (_profileImage.value) {
            ProfileImageType.DEFAULT -> 1
            else -> 0
        }
        val multipartFile =
            getMyProfileDataUseCase.convetProfileImageToMultipartFile(_profileImage.value,
                "multipartFile")
        val nickname = MultipartBody.Part.createFormData(
            "nickname",
            _nickName.value.text
        )
        authRepository.patchProfileImage(
            basic = MultipartBody.Part.createFormData("basic", basic.toString()),
            multipartFile = multipartFile,
            nickname = nickname,
        ).collect { apiState ->
            apiState.onSuccess {
                updateNickName(it.result.nickname)
                _categoryTags.value = it.result.categoryList.mapIndexed { _, categoryName ->
                    CategoryTag(-1, categoryName, -1)
                }.toMutableList()
                if (it.result.imgUrl == null) {
                    updateProfileImage(ProfileImageType.DEFAULT)
                } else {
                    updateProfileImage(ProfileImageType.SOCIAL(it.result.imgUrl))
                }
                onSuccess()
            }
            apiState.onError {
                onError(it)
            }
        }
    }

}
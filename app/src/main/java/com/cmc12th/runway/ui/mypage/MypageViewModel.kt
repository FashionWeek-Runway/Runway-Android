package com.cmc12th.runway.ui.mypage

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.cmc12th.runway.data.response.ErrorResponse
import com.cmc12th.runway.data.response.user.MyReviewsItem
import com.cmc12th.runway.data.response.user.StoreMetaDataItem
import com.cmc12th.runway.domain.repository.AuthRepository
import com.cmc12th.runway.domain.repository.SignInRepository
import com.cmc12th.runway.ui.login.passwordsearch.SearchPasswordPhoneUiState
import com.cmc12th.runway.ui.map.components.DetailState
import com.cmc12th.runway.ui.mypage.view.MypageTabInfo
import com.cmc12th.runway.ui.signin.SignInProfileImageUiState
import com.cmc12th.runway.ui.signin.model.Nickname
import com.cmc12th.runway.ui.signin.model.ProfileImageType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MypageUiState(
    val onDetail: DetailState = DetailState.default(),
    val selectedPage: MypageTabInfo = MypageTabInfo.MY_REVIEW
)

@HiltViewModel
class MypageViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val signInRepository: SignInRepository
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

    fun checkNickname(onSuccess: () -> Unit, onError: (ErrorResponse) -> Unit) =
        viewModelScope.launch {
            signInRepository.checkNickname(nickname = _nickName.value.text).collect {
                it.onSuccess {
                    onSuccess()
                }
                it.onError(onError)
            }
        }

    fun getMyReviews() = viewModelScope.launch {
        authRepository.myReviewPaging().cachedIn(viewModelScope).collect {
            _myReviews.value = it
        }
    }

    fun getBookmarkedStore() = viewModelScope.launch {
        authRepository.bookmarkedStorePaging().cachedIn(viewModelScope).collect {
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

}
package com.cmc12th.runway.ui.mypage.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.cmc12th.runway.ui.domain.model.ApplicationState
import com.cmc12th.runway.ui.mypage.MypageViewModel
import com.cmc12th.runway.utils.Constants
import com.cmc12th.runway.utils.Constants.LOGIN_GRAPH
import com.cmc12th.runway.utils.Constants.MAIN_GRAPH
import com.cmc12th.runway.utils.Constants.SPLASH_ROUTE

@Composable
fun MypageScreen(appState: ApplicationState) {

    val mypageViewModel: MypageViewModel = hiltViewModel()
    Column(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize()
    ) {
        Text(text = "Mypage Screen")
        Button(onClick = { appState.navController.navigate(Constants.PHOTO_REVIEW_ROUTE) }) {
            Text(text = "리뷰작성하러 가기")
        }
        Button(onClick = {
            mypageViewModel.logout() {
                appState.navController.navigate(LOGIN_GRAPH) {
                    popUpTo(MAIN_GRAPH) {
                        inclusive = true
                    }
                }
            }
        }) {
            Text(text = "로그아웃 하기")
        }
    }
}
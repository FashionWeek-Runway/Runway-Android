package com.cmc12th.runway.ui.mypage

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cmc12th.runway.ui.domain.model.ApplicationState
import com.cmc12th.runway.utils.Constants

@Composable
fun MypageScreen(appState: ApplicationState) {
    Column(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize()
    ) {
        Text(text = "Mypage Screen")
        Button(onClick = { appState.navController.navigate(Constants.PHOTO_REVIEW_ROOT) }) {
            Text(text = "리뷰작성하러 가기")
        }
    }
}
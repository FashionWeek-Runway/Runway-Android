package com.cmc12th.runway.ui.home.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cmc12th.runway.ui.components.BackIcon
import com.cmc12th.runway.ui.components.HeightSpacer
import com.cmc12th.runway.ui.domain.model.ApplicationState
import com.cmc12th.runway.ui.home.HomeViewModel
import com.cmc12th.runway.ui.signin.view.CategoryGroup
import com.cmc12th.runway.ui.theme.*

@Composable
fun EditCategoryScreen(
    appState: ApplicationState,
    nickname: String,
    homeViewModel: HomeViewModel = hiltViewModel(),
) {

    val uiState by homeViewModel.categoryUiState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        homeViewModel.getCategorys()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BackIcon {
                appState.navController.popBackStack()
            }
            Text(text = "카테고리 선택", style = Body1B)
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(20.dp)
        ) {
            Column {
                Row {
                    Text(
                        text = nickname,
                        style = HeadLine3,
                        color = Primary
                    )
                    Text(text = "님의 옷 스타일을", style = SubHeadline1)
                }
                Text(text = "선택해주세요.", style = SubHeadline1)
            }
            HeightSpacer(height = 20.dp)
            Text(text = "선택한 스타일을 기반으로 매장을 추천해드려요.", style = Body1, color = Gray700)

            HeightSpacer(height = 20.dp)
            /** 카테고리 입력 */
            CategoryGroup(
                uiState.categoryTags
            ) { homeViewModel.updateCategoryTags(it) }
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            enabled = uiState.anyCategorySelected(),
            shape = RoundedCornerShape(4.dp),
            colors = ButtonDefaults.buttonColors(if (uiState.anyCategorySelected()) Color.Black else Gray300),
            onClick = {
                homeViewModel.setCategorys {
                    appState.showSnackbar("카테고리가 변경되었습니다.")
                    appState.popBackStack()
                }
            }
        ) {
            Text(text = "다음", modifier = Modifier.padding(0.dp, 5.dp), fontSize = 16.sp)
        }
    }
}
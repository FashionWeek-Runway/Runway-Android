package com.cmc12th.runway.ui.signin.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cmc12th.runway.ui.components.BackIcon
import com.cmc12th.runway.ui.components.HeightSpacer
import com.cmc12th.runway.ui.domain.model.ApplicationState
import com.cmc12th.runway.ui.signin.SignInViewModel
import com.cmc12th.runway.ui.signin.components.OnBoardStep
import com.cmc12th.runway.ui.signin.components.StyleCategoryCheckBox
import com.cmc12th.runway.ui.signin.model.CategoryTag
import com.cmc12th.runway.ui.theme.*

@Composable
fun SignInCategoryScreen(
    appState: ApplicationState,
    signInViewModel: SignInViewModel = hiltViewModel()
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(modifier = Modifier.padding(20.dp)) {
            BackIcon()
        }
        OnBoardStep(6)

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(20.dp)
        ) {
            HeightSpacer(height = 20.dp)
            Column() {
                Row {
                    Text(text = "나패피", style = HeadLine3, color = Primary)
                    Text(text = "님의 옷 스타일을", style = SubHeadline1)
                }
                Text(text = "선택해주세요.", style = SubHeadline1)
            }
            HeightSpacer(height = 20.dp)
            Text(text = "선택한 스타일을 기반으로 매장을 추천해드려요.", style = Body1, color = Gray700)

            HeightSpacer(height = 20.dp)
            /** 카테고리 입력 */
            CategoryGroup(
                signInViewModel.categoryTags
            ) { signInViewModel.updateCategoryTags(it) }
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            shape = RectangleShape,
            colors = ButtonDefaults.buttonColors(Gray300),
            onClick = {}
        ) {
            Text(text = "다음", modifier = Modifier.padding(0.dp, 5.dp), fontSize = 16.sp)
        }
    }
}

@Composable
fun CategoryGroup(
    categoryTags: SnapshotStateList<CategoryTag>,
    updateCategoryTag: (CategoryTag) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(15.dp)) {
        categoryTags.chunked(2).forEach { item ->
            Row(horizontalArrangement = Arrangement.spacedBy(15.dp)) {
                item.forEach { categoryTag ->
                    StyleCategoryCheckBox(
                        isSelected = categoryTag.isSelected,
                        onClicked = { updateCategoryTag(categoryTag) },
                        title = categoryTag.name
                    )
                }
            }
        }
    }
}


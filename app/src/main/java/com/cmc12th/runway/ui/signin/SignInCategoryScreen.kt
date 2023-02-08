package com.cmc12th.runway.ui.signin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cmc12th.runway.ui.components.BackIcon
import com.cmc12th.runway.ui.components.HeightSpacer
import com.cmc12th.runway.ui.components.onboard.OnBoardStep
import com.cmc12th.runway.ui.components.onboard.StyleCategoryCheckBox
import com.cmc12th.runway.ui.theme.*

@Composable
fun SignInCategoryScreen() {

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
            CategoryGroup()
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
fun CategoryGroup() {
    Row() {
        StyleCategoryCheckBox(isSelected = false, onSelecte = { /*TODO*/ }, title = "미니멀")
        StyleCategoryCheckBox(isSelected = true, onSelecte = { /*TODO*/ }, title = "캐주얼")
    }
}


@Preview
@Composable
fun SignInCategoryScreenPrview() {
    SignInCategoryScreen()
}


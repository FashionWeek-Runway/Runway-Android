@file:OptIn(ExperimentalAnimationApi::class)

package com.cmc12th.runway.ui.signin.view

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cmc12th.runway.data.response.ErrorResponse
import com.cmc12th.runway.ui.components.BackIcon
import com.cmc12th.runway.ui.components.HeightSpacer
import com.cmc12th.runway.ui.domain.model.ApplicationState
import com.cmc12th.runway.ui.signin.SignInViewModel
import com.cmc12th.runway.ui.signin.components.OnBoardStep
import com.cmc12th.runway.ui.signin.components.StyleCategoryCheckBox
import com.cmc12th.runway.ui.signin.model.CategoryTag
import com.cmc12th.runway.ui.signin.model.SignInType
import com.cmc12th.runway.ui.theme.*
import com.cmc12th.runway.utils.Constants.SIGNIN_COMPLETE_ROUTE
import com.cmc12th.runway.utils.Constants.SIGNIN_GRAPH

@Composable
fun SignInCategoryScreen(
    appState: ApplicationState,
    signInViewModel: SignInViewModel = hiltViewModel(),
) {

    val uiState by signInViewModel.categoryUiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Box(modifier = Modifier.padding(20.dp)) {
            BackIcon {
                appState.navController.popBackStack()
            }
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
                    Text(
                        text = uiState.nickName.text,
                        style = HeadLine3,
                        color = Primary
                    )
                    Text(text = "?????? ??? ????????????", style = SubHeadline1)
                }
                Text(text = "??????????????????.", style = SubHeadline1)
            }
            HeightSpacer(height = 20.dp)
            Text(text = "????????? ???????????? ???????????? ????????? ??????????????????.", style = Body1, color = Gray700)

            HeightSpacer(height = 20.dp)
            /** ???????????? ?????? */
            CategoryGroup(
                uiState.categoryTags
            ) { signInViewModel.updateCategoryTags(it) }
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            enabled = uiState.anyCategorySelected(),
            shape = RoundedCornerShape(4.dp),
            colors = ButtonDefaults.buttonColors(if (uiState.anyCategorySelected()) Color.Black else Gray300),
            onClick = {
                val onSuccess = {
                    appState.navController.navigate(SIGNIN_COMPLETE_ROUTE) {
                        popUpTo(SIGNIN_GRAPH)
                    }
                }
                val onError: (ErrorResponse) -> Unit = { appState.showSnackbar(it.message) }
                when (uiState.signInType) {
                    SignInType.Phone -> signInViewModel.signUp(
                        onSuccess = onSuccess,
                        onError = onError
                    )
                    SignInType.SOCIAL -> signInViewModel.kakaoSignUp(
                        onSuccess = onSuccess,
                        onError = onError
                    )
                }

            }
        ) {
            Text(text = "??????", modifier = Modifier.padding(0.dp, 5.dp), fontSize = 16.sp)
        }
    }
}

@Composable
fun CategoryGroup(
    categoryTags: MutableList<CategoryTag>,
    updateCategoryTag: (CategoryTag) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(15.dp)) {
        categoryTags.chunked(2).forEach { item ->
            Row(horizontalArrangement = Arrangement.spacedBy(15.dp)) {
                item.forEach { categoryTag ->
                    val surfaceColor: State<Color> = animateColorAsState(
                        if (categoryTag.isSelected) Primary else White
                    )
                    StyleCategoryCheckBox(
                        isSelected = categoryTag.isSelected,
                        color = surfaceColor.value,
                        onClicked = {
                            updateCategoryTag(categoryTag)
                        },
                        title = categoryTag.name
                    )
                }
            }
        }
    }
}


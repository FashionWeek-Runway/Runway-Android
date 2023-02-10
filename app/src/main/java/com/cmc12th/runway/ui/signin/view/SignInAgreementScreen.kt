package com.cmc12th.runway.ui.signin.view

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cmc12th.runway.R
import com.cmc12th.runway.ui.components.BackIcon
import com.cmc12th.runway.ui.components.HeightSpacer
import com.cmc12th.runway.ui.components.WidthSpacer
import com.cmc12th.runway.ui.components.WidthSpacerLine
import com.cmc12th.runway.ui.signin.components.OnBoardStep
import com.cmc12th.runway.ui.domain.model.ApplicationState
import com.cmc12th.runway.ui.signin.SignInViewModel
import com.cmc12th.runway.ui.signin.model.Agreement
import com.cmc12th.runway.ui.theme.*
import com.cmc12th.runway.utils.Constants.SIGNIN_AGREEMENT_DETAIL_ROUTE
import com.cmc12th.runway.utils.Constants.SIGNIN_PROFILE_IMAGE_ROUTE

@Composable
fun SignInAgreementScreen(
    appState: ApplicationState,
    signInViewModel: SignInViewModel = hiltViewModel(),
) {

    val uiState by signInViewModel.agreementUiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(modifier = Modifier.padding(20.dp)) {
            BackIcon()
        }
        OnBoardStep(4)
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(20.dp)
                .scrollable(rememberScrollState(), orientation = Orientation.Vertical),
        ) {
            /** 약관동의 텍스트 */
            HeadLineText()

            Column {
                /** 약관 전체 동의 */
                AgreementAll(
                    checkState = !(uiState.agreements.any { !it.isChecked }),
                    onChecked = {
                        if (uiState.agreements.any { !it.isChecked }) {
                            signInViewModel.updateAgreements(
                                agreement = uiState.agreements.map { it.copy(isChecked = true) }
                                    .toMutableList()
                            )
                        } else {
                            signInViewModel.updateAgreements(
                                agreement = uiState.agreements.map { it.copy(isChecked = false) }
                                    .toMutableList()
                            )
                        }
                    }
                )
                WidthSpacerLine(1.dp, Gray300)
                HeightSpacer(height = 10.dp)

                /** 개별 약관 동의 */
                uiState.agreements.forEachIndexed { index, value ->
                    AgreementComponent(
                        onClick = { appState.navController.navigate(SIGNIN_AGREEMENT_DETAIL_ROUTE) },
                        agreement = value,
                        isChecked = uiState.agreements[index].isChecked,
                        onCheck = {
                            signInViewModel.updateAgreements(uiState.agreements.mapIndexed { idx, agreement ->
                                if (index == idx) agreement.copy(isChecked = !agreement.isChecked) else agreement
                            }.toMutableList())
                        }
                    )
                }
                HeightSpacer(height = 60.dp)
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RectangleShape,
                    colors = ButtonDefaults.buttonColors(if (uiState.isAllChcked()) Black else Gray300),
                    onClick = {
                        appState.navController.navigate(SIGNIN_PROFILE_IMAGE_ROUTE)
                    }) {
                    Text(text = "다음", modifier = Modifier.padding(0.dp, 5.dp), fontSize = 16.sp)
                }
            }
        }
    }
}

@Composable
private fun ColumnScope.HeadLineText() {
    Column(
        modifier = Modifier
            .padding(top = 20.dp)
            .weight(1f)
    ) {
        Text(text = "런웨이 사용을 위해", fontSize = 20.sp, fontWeight = FontWeight.Normal)
        Row {
            Text(text = "약관 동의", style = HeadLine3)
            Text(text = "가 필요해요.", fontSize = 20.sp, fontWeight = FontWeight.Normal)
        }
    }
}

@Composable
fun AgreementComponent(
    onClick: () -> Unit,
    onCheck: () -> Unit,
    isChecked: Boolean,
    agreement: Agreement,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        AgreementCheckBox(checkState = isChecked,
            onChecked = { onCheck() })
        WidthSpacer(width = 20.dp)
        Row(
            modifier = Modifier.clickable {
                onClick()
            },
        ) {
            Text(text = agreement.title, style = Body2)
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow),
                contentDescription = "IC_ARROW",
                modifier = Modifier
                    .rotate(180f)
                    .size(18.dp), tint = Gray400
            )
        }
    }
}

@Composable
fun AgreementAll(
    checkState: Boolean,
    onChecked: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp, 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AgreementCheckBox(
            checkState = checkState,
            onChecked = { onChecked() }
        )
        WidthSpacer(width = 20.dp)
        Text(text = "약관 전체 동의", fontSize = 16.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun AgreementCheckBox(
    checkState: Boolean,
    onChecked: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(24.dp)
            .border(BorderStroke(if (checkState) 0.dp else 1.dp, Gray300))
            .background(if (checkState) Primary else Color.White)
            .clickable {
                onChecked()
            }
    ) {
        AnimatedVisibility(
            visible = checkState, modifier = Modifier
                .align(Alignment.Center)
        ) {
            if (checkState) {
                Icon(
                    modifier = Modifier
                        .size(16.dp)
                        .align(Alignment.Center),
                    painter = painterResource(id = R.drawable.ic_check),
                    contentDescription = "IC_CHECK",
                    tint = Point
                )
            }
        }
    }
}

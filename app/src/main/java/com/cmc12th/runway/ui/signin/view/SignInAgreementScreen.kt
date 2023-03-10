package com.cmc12th.runway.ui.signin.view

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cmc12th.runway.R
import com.cmc12th.runway.ui.components.*
import com.cmc12th.runway.ui.signin.components.OnBoardStep
import com.cmc12th.runway.ui.domain.model.ApplicationState
import com.cmc12th.runway.ui.signin.SignInViewModel
import com.cmc12th.runway.ui.signin.model.Agreement
import com.cmc12th.runway.ui.theme.*
import com.cmc12th.runway.utils.Constants
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
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Box(modifier = Modifier.padding(20.dp)) {
            BackIcon {
                appState.navController.popBackStack()
            }
        }
        OnBoardStep(4)
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(20.dp)
                .scrollable(rememberScrollState(), orientation = Orientation.Vertical),
        ) {
            /** ???????????? ????????? */
            HeadLineText()

            Column {
                /** ?????? ?????? ?????? */
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
                HeightSpacer(height = 14.dp)

                val context = LocalContext.current
                /** ?????? ?????? ?????? */
                uiState.agreements.forEachIndexed { index, value ->
                    AgreementComponent(
                        onClick = {
                            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(value.link)))
                        },
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
                    shape = RoundedCornerShape(4.dp),
                    enabled = uiState.isAllChcked(),
                    colors = ButtonDefaults.buttonColors(if (uiState.isAllChcked()) Black else Gray300),
                    onClick = {
                        appState.navController.navigate("$SIGNIN_PROFILE_IMAGE_ROUTE?profileImage=&kakaoId=")
                    }) {
                    Text(text = "??????", modifier = Modifier.padding(0.dp, 5.dp), fontSize = 16.sp)
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
        Text(text = "????????? ????????? ??????", fontSize = 20.sp, fontWeight = FontWeight.Normal)
        Row {
            Text(text = "?????? ??????", style = HeadLine3)
            Text(text = "??? ????????????.", fontSize = 20.sp, fontWeight = FontWeight.Normal)
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
            .padding(10.dp, 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RunwayCheckBox(checkState = isChecked,
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
            .padding(10.dp, 20.dp)
            .clickable {
                onChecked()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        RunwayCheckBox(
            checkState = checkState,
            onChecked = { onChecked() }
        )
        WidthSpacer(width = 20.dp)
        Text(text = "?????? ?????? ??????", fontSize = 16.sp, fontWeight = FontWeight.Medium)
    }
}

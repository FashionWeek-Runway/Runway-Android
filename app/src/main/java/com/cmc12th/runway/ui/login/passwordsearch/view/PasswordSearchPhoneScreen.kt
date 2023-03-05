@file:OptIn(
    ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class,
    ExperimentalMaterialApi::class
)

package com.cmc12th.runway.ui.login.passwordsearch.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cmc12th.runway.R
import com.cmc12th.runway.ui.components.BackIcon
import com.cmc12th.runway.ui.components.CustomBottomSheet
import com.cmc12th.runway.ui.components.CustomTextField
import com.cmc12th.runway.ui.components.HeightSpacer
import com.cmc12th.runway.ui.components.util.bottomBorder
import com.cmc12th.runway.ui.domain.model.ApplicationState
import com.cmc12th.runway.ui.domain.model.BottomSheetContent
import com.cmc12th.runway.ui.domain.model.BottomSheetContentItem
import com.cmc12th.runway.ui.domain.rememberBottomSheet
import com.cmc12th.runway.ui.login.passwordsearch.PasswordSearchViewModel
import com.cmc12th.runway.ui.signin.components.OnBoardHeadLine
import com.cmc12th.runway.ui.signin.model.MobileCarrier
import com.cmc12th.runway.ui.signin.model.Phone
import com.cmc12th.runway.ui.signin.model.Phone.Companion.PHONE_NUMBER_LENGTH
import com.cmc12th.runway.ui.theme.*
import com.cmc12th.runway.utils.Constants
import com.cmc12th.runway.utils.Constants.PASSWORD_SEARCH_PHONE_VERIFY_ROUTE
import kotlinx.coroutines.launch

@Composable
fun PasswordSearchPhoneScreen(
    appState: ApplicationState,
    passwordSearchViewModel: PasswordSearchViewModel = hiltViewModel(),
) {
    val coroutineScope = rememberCoroutineScope()
    val bottomsheetState = rememberBottomSheet()
    val keyboardController = LocalSoftwareKeyboardController.current

    val showBottomSheet: (BottomSheetContent) -> Unit = {
        keyboardController?.hide()
        coroutineScope.launch {
            bottomsheetState.bottomsheetContent.value = it
            bottomsheetState.modalSheetState.show()
        }
    }

    CustomBottomSheet(
        bottomsheetState
    ) {
        PasswordSearchPhoneContents(
            appState = appState,
            viewModel = passwordSearchViewModel,
            showBottomSheet = showBottomSheet
        )
    }
}

@Composable
fun PasswordSearchPhoneContents(
    appState: ApplicationState,
    viewModel: PasswordSearchViewModel,
    showBottomSheet: (BottomSheetContent) -> Unit,
) {

    val uiState by viewModel.searchPasswordPhoneUiState.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()
    val passwordErrorMessage = remember {
        mutableStateOf("")
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember {
        FocusRequester()
    }
    val sendVerifyMessage: () -> Unit = {
        passwordErrorMessage.value = ""
        keyboardController?.hide()
        viewModel.sendVerifyMessage(
            onSuccess = {
                viewModel.startTimer()
                appState.navController.navigate(PASSWORD_SEARCH_PHONE_VERIFY_ROUTE)
            },
            onError = {
                when (it.code) {
                    "U005" -> passwordErrorMessage.value = it.message
                    else -> {}
                }
                appState.showSnackbar(it.message)
            })
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .imePadding(),
    ) {

        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BackIcon {
                appState.popBackStack()
            }
            Text(text = "비밀번호 찾기", style = Body1B, color = Color.Black)
        }

        Column(
            modifier = Modifier
                .padding(top = 20.dp, start = 20.dp, end = 20.dp)
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(30.dp)
        ) {
            OnBoardHeadLine("휴대폰 번호", "를 입력해주세요.")

            /** 핸드폰 번호 찾기 */
            PhoneContainer(
                focusRequest = focusRequester,
                sendVerifyMessage = {
                    if (uiState.userPhoneVerificationStatus) sendVerifyMessage()
                },
                errorMessage = passwordErrorMessage.value,
                showBottomSheet = showBottomSheet,
                phone = uiState.phone,
                updateMobildeCarrier = { viewModel.updateMobileCarrier(it) },
                updatePhoneNumber = {
                    passwordErrorMessage.value = ""
                    viewModel.updatePhoneNumber(it)
                },
            )
        }

        /** 인증 요청 */
        Button(modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp, 10.dp),
            shape = RoundedCornerShape(4.dp),
            enabled = uiState.userPhoneVerificationStatus,
            colors = ButtonDefaults.buttonColors(if (uiState.userPhoneVerificationStatus) Color.Black else Gray300),
            onClick = { sendVerifyMessage() }) {
            Text(
                modifier = Modifier.padding(0.dp, 5.dp),
                text = "인증 요청",
                fontSize = 16.sp,
                color = Color.White
            )
        }
    }
}

@Composable
private fun PhoneContainer(
    showBottomSheet: (BottomSheetContent) -> Unit,
    phone: Phone,
    updateMobildeCarrier: (MobileCarrier) -> Unit,
    updatePhoneNumber: (String) -> Unit,
    focusRequest: FocusRequester,
    errorMessage: String,
    sendVerifyMessage: () -> Unit,
) {
    val isDropDownMenuExpanded = remember {
        mutableStateOf(false)
    }

    Column {
        HeightSpacer(height = 30.dp)
        Column(modifier = Modifier.fillMaxWidth()) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Box {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .bottomBorder(
                                1.dp,
                                if (isDropDownMenuExpanded.value) Color.Black else Gray600
                            ),
                        onClick = {
                            showBottomSheet(BottomSheetContent(
                                title = "통신사",
                                itemList = MobileCarrier.values().map {
                                    BottomSheetContentItem(
                                        itemName = it.getName(),
                                        onItemClick = { updateMobildeCarrier(it) },
                                        isSeleceted = it == phone.mobileCarrier
                                    )
                                }
                            ))
                        },
                        colors = ButtonDefaults.buttonColors(Color.White)
                    ) {
                    }
                    Text(
                        modifier = Modifier.align(Alignment.CenterStart),
                        text = phone.mobileCarrier.getName(), color = Color.Black,
                        textAlign = TextAlign.Start
                    )
                    Icon(
                        tint = Gray400,
                        painter = painterResource(id = R.drawable.ic_arrow),
                        contentDescription = "IC_ARROW",
                        modifier = Modifier
                            .rotate(270f)
                            .size(20.dp)
                            .align(Alignment.CenterEnd),
                    )
                }
            }
        }
        HeightSpacer(height = 10.dp)
        CustomTextField(
            modifier = Modifier
                .fillMaxWidth(),
            fontSize = 16.sp,
            focusRequest = focusRequest,
            value = phone.number,
            placeholderText = "휴대폰 번호 입력('-' 제외)",
            onvalueChanged = {
                if (it.length <= PHONE_NUMBER_LENGTH) {
                    updatePhoneNumber(it)
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = {
                sendVerifyMessage()
            }),
            onErrorState = errorMessage.isNotBlank() || (phone.number.isNotBlank() && !phone.checkValidation()),
            errorMessage = errorMessage.ifBlank { "휴대폰번호 11자를 입력해주세요." }
        )
    }
}


package com.cmc12th.runway.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.cmc12th.runway.R

val SpoqaHansSansNeo = FontFamily(
    Font(R.font.spoqa_han_sans_neo_bold, FontWeight.Bold, FontStyle.Normal),
    Font(
        R.font.spoqa_han_sans_neo_regular,
        FontWeight.Normal,
        FontStyle.Normal
    ), // Regular을 Normal로 취급
    Font(R.font.spoqa_han_sans_neo_medium, FontWeight.Medium, FontStyle.Normal),
    Font(R.font.spoqa_han_sans_neo_light, FontWeight.Light, FontStyle.Normal),
)

val HeadLine1 = TextStyle(
    fontFamily = SpoqaHansSansNeo,
    fontWeight = FontWeight.Bold,
    fontSize = 28.sp,
)

val HeadLine2 = TextStyle(
    fontFamily = SpoqaHansSansNeo,
    fontWeight = FontWeight.Bold,
    fontSize = 24.sp,
)

val HeadLine3 = TextStyle(
    fontFamily = SpoqaHansSansNeo,
    fontWeight = FontWeight.Bold,
    fontSize = 20.sp,
)

val HeadLine4 = TextStyle(
    fontFamily = SpoqaHansSansNeo,
    fontWeight = FontWeight.Bold,
    fontSize = 18.sp,
)

val SubHeadline1 = TextStyle(
    fontFamily = SpoqaHansSansNeo,
    fontWeight = FontWeight.Medium,
    fontSize = 20.sp,
)

val SubHeadline1B = TextStyle(
    fontFamily = SpoqaHansSansNeo,
    fontWeight = FontWeight.Bold,
    fontSize = 20.sp,
)

val Body1 = TextStyle(
    fontFamily = SpoqaHansSansNeo,
    fontWeight = FontWeight.Normal,
    fontSize = 16.sp,
)

val Body1B = TextStyle(
    fontFamily = SpoqaHansSansNeo,
    fontWeight = FontWeight.Bold,
    fontSize = 16.sp,
)

val Body1M = TextStyle(
    fontFamily = SpoqaHansSansNeo,
    fontWeight = FontWeight.Medium,
    fontSize = 16.sp,
)

val Body2M = TextStyle(
    fontFamily = SpoqaHansSansNeo,
    fontWeight = FontWeight.Medium,
    fontSize = 14.sp,
)

val Body2 = TextStyle(
    fontFamily = SpoqaHansSansNeo,
    fontWeight = FontWeight.Normal,
    fontSize = 14.sp,
)

val Caption = TextStyle(
    fontFamily = SpoqaHansSansNeo,
    fontWeight = FontWeight.Normal,
    fontSize = 12.sp,
)

val Button1 = TextStyle(
    fontFamily = SpoqaHansSansNeo,
    fontWeight = FontWeight.Medium,
    fontSize = 16.sp,
)

val Button2 = TextStyle(
    fontFamily = SpoqaHansSansNeo,
    fontWeight = FontWeight.Medium,
    fontSize = 12.sp,
)

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = SpoqaHansSansNeo,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
)


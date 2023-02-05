package com.cmc12th.runway.ui.detail

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.cmc12th.runway.ui.domain.model.ApplicationState

@Composable
fun PhotoReviewResultScreen(appState: ApplicationState, bitmap: Bitmap) {

//    val croppedImage = remember {
//        mutableStateOf<Bitmap?>(null)
//    }

    Column(modifier = Modifier.fillMaxSize()) {
        Image(
            modifier = Modifier.size(500.dp),
            painter = rememberAsyncImagePainter(bitmap),
            contentScale = ContentScale.Crop,
            contentDescription = null,
        )
    }

}
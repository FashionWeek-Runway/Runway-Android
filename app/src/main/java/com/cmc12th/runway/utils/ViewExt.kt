package com.cmc12th.runway.utils

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.PixelCopy
import android.view.View
import android.view.Window
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.cmc12th.runway.ui.theme.Gray200


// start of extension.
fun View.toBitmap(
    onBitmapReady: (Bitmap) -> Unit,
    onBitmapError: (Exception) -> Unit,
    bottomBarHeight: Float,
    topbarheight: Float,
) {

    try {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val temporalBitmap =
                Bitmap.createBitmap(
                    this.width,
                    this.height - bottomBarHeight.toInt() - topbarheight.toInt(),
                    Bitmap.Config.ARGB_8888
                )

            // Above Android O, use PixelCopy due
            // https://stackoverflow.com/questions/58314397/
            val window: Window = (this.context as Activity).window
            val location = IntArray(2)
            this.getLocationInWindow(location)

            val viewRectangle =
                Rect(
                    location[0],
                    location[1] + topbarheight.toInt(),
                    location[0] + this.width,
                    location[1] + this.height - bottomBarHeight.toInt()
                )

            val onPixelCopyListener: PixelCopy.OnPixelCopyFinishedListener =
                PixelCopy.OnPixelCopyFinishedListener { copyResult ->
                    if (copyResult == PixelCopy.SUCCESS) {
                        onBitmapReady(temporalBitmap)
                    } else {
                        error("Error while copying pixels, copy result: $copyResult")
                    }
                }

            PixelCopy.request(
                window,
                viewRectangle,
                temporalBitmap,
                onPixelCopyListener,
                Handler(Looper.getMainLooper())
            )
        } else {

            val temporalBitmap = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.RGB_565)
            val canvas = Canvas(temporalBitmap)
            this.draw(canvas)
            canvas.setBitmap(null)
            onBitmapReady(temporalBitmap)
        }

    } catch (exception: Exception) {
        onBitmapError(exception)
    }
}


fun captureView(view: View, window: Window, bitmapCallback: (Bitmap) -> Unit) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val location = IntArray(2)
        view.getLocationInWindow(location)
        PixelCopy.request(
            window,
            Rect(
                location[0], location[1],
                (location[0] + view.width), (location[1] + view.height)
            ),
            bitmap,
            { it ->
                if (it == PixelCopy.SUCCESS) {
                    bitmapCallback.invoke(bitmap)
                }
            },
            Handler(Looper.getMainLooper())
        )
    } else {
        val tBitmap = Bitmap.createBitmap(
            view.width, view.height, Bitmap.Config.RGB_565
        )
        val canvas = Canvas(tBitmap)
        view.draw(canvas)
        canvas.setBitmap(null)
        bitmapCallback.invoke(tBitmap)
    }
}

inline fun Modifier.noRippleClickable(crossinline onClick: () -> Unit): Modifier = composed {
    clickable(indication = null,
        interactionSource = remember { MutableInteractionSource() }) {
        onClick()
    }
}

@Composable
fun String.skeletonUI(
    color: Color = Gray200,
    size: Pair<Dp, Dp>,
    content: @Composable () -> Unit,
) {
    if (this.isEmpty()) {
        Box(modifier = Modifier
            .size(
                width = size.first,
                height = size.second,
            )
            .background(color)
        )
    } else {
        content()
    }
}

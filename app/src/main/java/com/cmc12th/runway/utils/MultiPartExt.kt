package com.cmc12th.runway.utils

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

/** MultipartBody으로 바꾸는 작업 */
fun String.toPlainRequestBody() = requireNotNull(this).toRequestBody("text/plain".toMediaType())
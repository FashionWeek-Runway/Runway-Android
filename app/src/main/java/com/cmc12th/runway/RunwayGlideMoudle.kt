package com.cmc12th.runway

import android.content.Context
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Priority
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import okhttp3.OkHttpClient
import java.io.InputStream
import java.util.concurrent.TimeUnit


@GlideModule
class RunwayGlideMoudle : AppGlideModule() {

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        super.applyOptions(context, builder)

        // 이미지 불러오는 시간을 보고싶다면 아래 주석을 풀어주세요
        // builder.setLogLevel(Log.VERBOSE)

        builder.setDefaultRequestOptions(
            RequestOptions()
                .placeholder(R.color.gray100)
                .override(1000, 1000)
                .encodeQuality(90) // 유저 리뷰 퀄리티는 50%로 설정
                .dontTransform() // 이미지를 변형하지 않음
                .dontAnimate() // 이미지를 애니메이션하지 않음
                .encodeFormat(android.graphics.Bitmap.CompressFormat.JPEG)
                .format(DecodeFormat.PREFER_RGB_565) // 이미지 포맷을 RGB565로 설정
                .priority(Priority.IMMEDIATE) // 이미지를 빠르게 불러옴
                .diskCacheStrategy(DiskCacheStrategy.ALL) // 디스크 캐시 전략을 ALL로 설정
                .error(R.drawable.img_dummy)    // 이미지 로드 실패시 더미 이미지를 보여줌
        )
    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        val okHttpClient = OkHttpClient.Builder()
//            .connectTimeout(5, TimeUnit.SECONDS)
//            .readTimeout(5, TimeUnit.SECONDS)
//            .writeTimeout(5, TimeUnit.SECONDS)
//            .retryOnConnectionFailure(true)
            .build()

        registry.replace(
            GlideUrl::class.java,
            InputStream::class.java,
            OkHttpUrlLoader.Factory(okHttpClient)
        )

    }
}
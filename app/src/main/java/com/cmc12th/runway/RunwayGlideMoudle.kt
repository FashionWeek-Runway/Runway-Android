package com.cmc12th.runway

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.executor.GlideExecutor
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import java.io.InputStream
import java.util.concurrent.TimeUnit


@GlideModule
class RunwayGlideMoudle : AppGlideModule() {

    override fun applyOptions(context: Context, builder: GlideBuilder) {

        // 이미지 불러오는 시간을 보고싶다면 아래 주석을 풀어주세요
        // builder.setLogLevel(Log.VERBOSE)

        builder.setSourceExecutor(GlideExecutor.newSourceBuilder().setThreadCount(20).build())
        builder.setDiskCacheExecutor(GlideExecutor.newSourceBuilder().setThreadCount(20).build())

        builder.setDefaultRequestOptions(
            RequestOptions()
                .placeholder(R.color.gray100) // 이미지 로드 전 더미 이미지를 보여줌
                .diskCacheStrategy(DiskCacheStrategy.ALL) // 디스크 캐시 전략
                .error(R.drawable.img_dummy)    // 이미지 로드 실패시 더미 이미지를 보여줌
        )
    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        val okHttpClient = OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .connectionPool(ConnectionPool(60, 10, TimeUnit.SECONDS))
            .build()
        registry.replace(
            GlideUrl::class.java,
            InputStream::class.java,
            OkHttpUrlLoader.Factory(okHttpClient)
        )
    }
}
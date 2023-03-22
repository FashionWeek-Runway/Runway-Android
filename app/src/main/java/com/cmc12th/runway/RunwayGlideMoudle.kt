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
import com.bumptech.glide.load.engine.executor.GlideExecutor
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.util.Executors
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import java.io.InputStream
import java.util.concurrent.TimeUnit


@GlideModule
class RunwayGlideMoudle : AppGlideModule() {

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        super.applyOptions(context, builder)

        // 이미지 불러오는 시간을 보고싶다면 아래 주석을 풀어주세요
        builder.setLogLevel(Log.VERBOSE)

        builder.setSourceExecutor(GlideExecutor.newSourceExecutor())
        builder.setDiskCacheExecutor(GlideExecutor.newDiskCacheExecutor())

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

    // https://stackoverflow.com/questions/45012504/glide-how-to-load-multiple-images-in-parallel
    // 이미지 로딩관련 보강
    // ConnectionPool은 OkHttp 라이브러리에서 제공하는 커넥션 관리 기능 중 하나로, 동일한 서버에 대한 여러 개의 HTTP 요청을 처리할 때, 커넥션 재사용을 통해 성능을 최적화하는 기능입니다.
    //
    //ConnectionPool은 OkHttp 클라이언트 내부에서 유지되는 커넥션 객체를 관리합니다. 커넥션 객체는 HTTP 요청을 처리하기 위해 TCP 소켓 연결을 생성하는데, 매번 새로운 연결을 생성하면 비용이 많이 들어 성능에 악영향을 끼치게 됩니다. 이를 방지하기 위해 ConnectionPool은 커넥션 객체를 재사용하여 동일한 서버에 대한 여러 개의 요청에 대해 동일한 커넥션을 사용할 수 있도록 합니다.
    //
    //ConnectionPool은 기본적으로 5개의 커넥션을 유지하며, 커넥션을 사용하고 반환함으로써 커넥션 개수를 유지하고 커넥션 객체의 재사용을 가능하게 합니다. 또한, ConnectionPool은 일정 시간 동안 사용되지 않은 커넥션을 제거하여 불필요한 자원 사용을 방지합니다.
    //
    //ConnectionPool은 다음과 같은 메서드를 제공합니다.
    //
    //ConnectionPool(): ConnectionPool 객체 생성
    //ConnectionPool(int maxIdleConnections, long keepAliveDuration, TimeUnit timeUnit): ConnectionPool 객체 생성, 커넥션 최대 개수 및 유휴 커넥션 유지 시간 지정
    //ConnectionPool(Executor executor, int maxIdleConnections, long keepAliveDuration, TimeUnit timeUnit): ConnectionPool 객체 생성, 스레드 풀 및 커넥션 최대 개수, 유휴 커넥션 유지 시간 지정
    //evictAll(): ConnectionPool에서 모든 커넥션 객체 제거
    //ConnectionPool은 OkHttp의 OkHttpClient 객체 내부에서 사용되며, 기본적으로 자동으로 활성화되어 있습니다. 따라서 일반적인 사용자는 별도의 설정이나 변경 없이도 ConnectionPool을 사용할 수 있습니다.
    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        val okHttpClient = OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .connectionPool(ConnectionPool(100, 3, TimeUnit.SECONDS))
            .build()

        val okHttpClient2 = OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .connectionPool(ConnectionPool(100, 3, TimeUnit.SECONDS))
            .build()
        val okHttpClient3 = OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .connectionPool(ConnectionPool(100, 3, TimeUnit.SECONDS))
            .build()
        registry.replace(
            GlideUrl::class.java,
            InputStream::class.java,
            OkHttpUrlLoader.Factory(okHttpClient)
        )
        registry.replace(
            GlideUrl::class.java,
            InputStream::class.java,
            OkHttpUrlLoader.Factory(okHttpClient2)
        )
        registry.replace(
            GlideUrl::class.java,
            InputStream::class.java,
            OkHttpUrlLoader.Factory(okHttpClient3)
        )

    }
}
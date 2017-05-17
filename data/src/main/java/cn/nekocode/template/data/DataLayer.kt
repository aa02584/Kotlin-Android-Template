package cn.nekocode.template.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.paperdb.Paper
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * @author nekocode (nekocode.cn@gmail.com)
 */
object DataLayer {
    const val API_HOST_URL: String = "http://gank.io/api/data/%E7%A6%8F%E5%88%A9/"
    var context: Context? = null
    var client: OkHttpClient? = null
    var gson: Gson? = null
    var retrofit: Retrofit? = null


    fun init(context: Context) {
        DataLayer.context = context.applicationContext

        client = OkHttpClient.Builder()
                .cache(Cache(File(context.cacheDir, "okhttp"), 10 * 1024 * 1024L))
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build()

        gson = GsonBuilder().setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'").create()

        retrofit = Retrofit.Builder()
                .baseUrl(API_HOST_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build()

        Paper.init(context)
    }
}
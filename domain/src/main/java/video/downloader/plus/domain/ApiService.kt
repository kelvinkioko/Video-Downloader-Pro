package video.downloader.plus.domain

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {

    @POST("login")
    @FormUrlEncoded
    fun startUserSignin(@Field("email") email_address: String?, @Field("password") password: String?): Call<ResponseBody>

}
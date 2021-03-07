package site.yoonsang.myapi

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface RetrofitService {

    @GET("/data/2.5/air_pollution")
    fun getCurrentDustData(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appid: String
    ): Call<DustResponse>

    @GET("/v1/nid/me")
    fun getNaverUserInfo(
        @Header("Authorization") authorization: String
    ): Call<NaverUserResponse>

    @GET("/v2/local/search/address.json")
    fun getAddressData(
        @Query("query") address: String,
        @Header("Authorization") authorization: String
    ): Call<AddressResponse>
}
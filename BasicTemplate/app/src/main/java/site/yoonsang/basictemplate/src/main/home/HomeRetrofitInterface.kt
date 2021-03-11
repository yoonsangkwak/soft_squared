package site.yoonsang.basictemplate.src.main.home

import retrofit2.Call
import retrofit2.http.*
import site.yoonsang.basictemplate.src.main.home.models.PostSignUpRequest
import site.yoonsang.basictemplate.src.main.home.models.SignUpResponse
import site.yoonsang.basictemplate.src.main.home.models.UserResponse

interface HomeRetrofitInterface {
    @GET("/users")
    fun getUsers(): Call<UserResponse>

    @POST("/users")
    fun postSignUp(@Body params: PostSignUpRequest): Call<SignUpResponse>
}
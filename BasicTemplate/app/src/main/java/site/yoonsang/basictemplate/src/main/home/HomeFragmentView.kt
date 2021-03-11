package site.yoonsang.basictemplate.src.main.home

import site.yoonsang.basictemplate.src.main.home.models.SignUpResponse
import site.yoonsang.basictemplate.src.main.home.models.UserResponse

interface HomeFragmentView {

    fun onGetUserSuccess(response: UserResponse)

    fun onGetUserFailure(message: String)

    fun onPostSignUpSuccess(response: SignUpResponse)

    fun onPostSignUpFailure(message: String)
}
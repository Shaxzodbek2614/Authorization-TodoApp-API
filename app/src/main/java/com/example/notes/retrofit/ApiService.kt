package com.example.notes.retrofit

import com.example.notes.models.GetDetails
import com.example.notes.models.GetUserDetails
import com.example.notes.models.PostDetails
import com.example.notes.models.PostRequestUser
import com.example.notes.models.PostResponseToken
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @POST("user/register/")
    fun registerUser(@Body postRequestUser: PostRequestUser): Call<String>

    @POST("user/token/")
    fun loginUser(@Body postRequestUser: PostRequestUser): Call<PostResponseToken>

    @GET("user/details/")
    fun getUserDetails(@Header("Authorization") token: String): Call<GetUserDetails>

    @DELETE("user/delete/")
    fun deleteUser(@Header("Authorization") token: String): Call<Any>

    @GET("main/rejalar/")
    fun getRejalar(@Header("Authorization") token: String): Call<List<GetDetails>>

    @POST("main/reja-qo'shish/")
    fun addReja(@Header("Authorization") token: String, @Body postDetails: PostDetails): Call<Any>

    @DELETE("main/rejalar/{reja_id}/o'chirish/")
    fun deleteReja(@Header("Authorization") token: String, @Path("reja_id") id: Int): Call<Any>

    @PUT("main/rejalar/{reja_id}/tahrirlash/")
    fun editReja(@Header("Authorization") token: String, @Path("reja_id") id: Int, @Body postDetails: PostDetails): Call<Any>


}
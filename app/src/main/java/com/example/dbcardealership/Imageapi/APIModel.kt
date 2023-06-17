package com.example.dbcardealership.Imageapi

import com.example.dbcardealership.DealershipApiService
import com.example.dbcardealership.showroom.Showroom
import com.example.dbcardealership.сar.Car
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

data class APIModel(
    val total: Int,
    val total_pages: Int,
    val results: List<APIResultModel>
)

data class APIResultModel(
    val id: String,
    val description: String,
    val urls: UrlModel
)

data class UrlModel(
    val raw: String,
    val full: String,
    val regular: String,
    val small: String,
    val thumb: String,
    val small_s3: String
)

interface CarPhotoApiService {

    @GET("photos")
    suspend fun getModule(@Query("client_id") client_id: String,
        @Query("page") page: Int,
        @Query("per_page") per_page: Int,
        @Query("query") query: String,
    ): APIModel
}

suspend fun getCarUrl(car: String): String {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.unsplash.com/search/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service = retrofit.create(CarPhotoApiService::class.java)
    val model = service.getModule("IwKE_3FEZhANclZ3OrJqj0O3w5p-FnrJbDIeJNzboD8", 1, 1, car)
    return model.results[0].urls.regular
}

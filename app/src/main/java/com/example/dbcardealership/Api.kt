package com.example.dbcardealership

import com.example.dbcardealership.client.Client
import com.example.dbcardealership.client.ClientRegisterModel
import com.example.dbcardealership.contract.Contract
import com.example.dbcardealership.manager.Manager
import com.example.dbcardealership.manager.ManagerRegisterModel
import com.example.dbcardealership.сar.Car
import com.example.dbcardealership.showroom.Showroom
import com.example.dbcardealership.user.UserLogin
import com.example.dbcardealership.сar.CarCreateModel
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface DealershipApiService {

    @GET("/showroom")
    suspend fun getShowrooms(): List<Showroom>

    @GET("/client")
    suspend fun getClients(): List<Client>

    @GET("/manager")
    suspend fun getManagers(): List<Manager>

    @GET("/cars")
    suspend fun getCars(): List<Car>

    @GET("/contract")
    suspend fun getContracts(): List<Contract>

    @GET("/showroom/{id}/cars/in_stock")
    suspend fun getShowroomCarsInStock(@Path("id") id: Int): List<Car>

    @GET("car/client/{phone}")
    suspend fun getCarsByClientPhone(@Path("phone") phone: String): Response<List<Car>>

    @GET("car/{id}")
    suspend fun getCarById(@Path("id") id: Int): Car

    @GET("/client/phone/{phone}")
    suspend fun getClientIdByPhone(@Path("phone") phone: String): Response<Int>

    @GET("car/manager/{phone}")
    suspend fun getCarsByManagerPhone(@Path("phone") phone: String): Response<List<Car>>

    @POST("/client/login")
    suspend fun loginClient(@Body client: UserLogin): Response<Client>

    @POST("/manager/login")
    suspend fun loginManager(@Body manager: UserLogin): Response<Manager>

    @POST("/client/register")
    suspend fun registerClient(@Body client: ClientRegisterModel): Response<Int>

    @POST("/manager/register")
    suspend fun registerManager(@Body manager: ManagerRegisterModel): Response<Int>

    @POST("/contract")
    suspend fun sendContract(@Body contract: Contract): Response<Int>

    @POST("/car")
    suspend fun createCar(@Body car: CarCreateModel): Response<Int>

}

fun SetUpAPI(): DealershipApiService {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://comexamplektor-sample-production.up.railway.app")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service = retrofit.create(DealershipApiService::class.java)
    return service
}

suspend fun getAllShowrooms(): List<Showroom> {
    val service = SetUpAPI()

    return service.getShowrooms()
}

suspend fun getCarsInStockByShowroomId(id: Int): List<Car> {
    val service = SetUpAPI()

    return service.getShowroomCarsInStock(id)
}

suspend fun getShowroomsAddress(): List<String>{
    val service = SetUpAPI()
    var address = mutableListOf<String>()

    service.getShowrooms().forEach { showroom ->
        address.add(showroom.address)
    }
    return address
}

suspend fun getShowroomIdByAddress(address: String): Int {
    val service = SetUpAPI()
    service.getShowrooms().forEach { showroom ->
        if(showroom.address == address)
            return showroom.id
    }
    return 0
}
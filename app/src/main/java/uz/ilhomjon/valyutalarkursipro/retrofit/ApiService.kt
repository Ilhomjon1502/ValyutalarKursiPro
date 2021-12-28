package uz.ilhomjon.valyutalarkursipro.retrofit

import retrofit2.Call
import retrofit2.http.GET
import uz.ilhomjon.valyutalarkursipro.models.Valyuta

interface ApiService {
    @GET("json")
    fun getUsers(): Call<List<Valyuta>>
}
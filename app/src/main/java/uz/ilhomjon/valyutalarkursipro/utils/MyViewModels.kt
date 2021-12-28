package uz.ilhomjon.valyutalarkursipro.utils

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uz.ilhomjon.valyutalarkursipro.models.Valyuta
import uz.ilhomjon.valyutalarkursipro.retrofit.ApiClient

class MyViewModels : ViewModel(){

    private val TAG = "MyViewModels"
    private var liveDouble = MutableLiveData<List<Valyuta>>()

    fun getUsers(): LiveData<List<Valyuta>> {
        ApiClient.apiService.getUsers().enqueue(object : Callback<List<Valyuta>> {
            override fun onResponse(call: Call<List<Valyuta>>, response: Response<List<Valyuta>>) {
                if (response.isSuccessful){
                    liveDouble.value = response.body()
                    Log.d(TAG, "onResponse: ${response.body()}")
                }
            }
            override fun onFailure(call: Call<List<Valyuta>>, t: Throwable) {
                Log.d(TAG, "onFailure: Retrofit")
            }
        })
        return liveDouble
    }
}
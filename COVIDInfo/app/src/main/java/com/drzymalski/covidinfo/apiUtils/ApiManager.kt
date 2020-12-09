package com.drzymalski.covidinfo.apiUtils

import com.drzymalski.covidinfo.apiUtils.models.CovidDay
import com.drzymalski.covidinfo.apiUtils.models.SummaryData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*
import java.io.IOException
import java.util.concurrent.CountDownLatch

class ApiManager {

    private val client = OkHttpClient()

    companion object {
        private const val BASE_URL = "https://api.covid19api.com"

        private fun getJSONFromApi(url: String): String  {
            val request = Request.Builder()
                .url(url)
                .build()
            var finalJsonContent = ""
            val countDownLatch = CountDownLatch(1)
            countDownLatch.run {
                with(OkHttpClient()) {
                         newCall(request).enqueue(object : Callback {
                             override fun onFailure(call: Call, e: IOException) {}
                             override fun onResponse(call: Call, response: Response) {
                                 try {
                                     val res = response.body()?.string()
                                     finalJsonContent = res.toString()
                                     countDown()
                                 }catch (ex:OutOfMemoryError){//some memory alocation errors when the format does't match
                                     println(ex.message)
                                     print(finalJsonContent)
                                 }
                             }
                         })
                    }
                await()
            }
            return finalJsonContent
        }

        fun getCovidDataFromApi(url: String): List<Any> {
            val gson = Gson()
            val listPersonType = object : TypeToken<List<Any>>() {}.type
            return  gson.fromJson(getJSONFromApi(url), listPersonType)
        }

        fun getCovidDataFromApi(dateFrom: String, dateTo: String, country: String): List<CovidDay> {
            val url= "$BASE_URL/country/${country}?from=${dateFrom}T00:00:00Z&to=${dateTo}T00:00:00Z"
            //if (dateTo!="") url+= "$BASE_URL/country/Poland?from=${dateFrom}T00:00:00Z&to=${dateTo}T00:00:00Z"

            val listPersonType = object : TypeToken<List<CovidDay>>() {}.type
            return  Gson().fromJson(getJSONFromApi(url), listPersonType)
        }

        fun getSummaryFromApi(): SummaryData {
            val url= "$BASE_URL/summary"
            val listPersonType = object : TypeToken<SummaryData>() {}.type
            return  Gson().fromJson(getJSONFromApi(url), listPersonType)
        }
    }

}
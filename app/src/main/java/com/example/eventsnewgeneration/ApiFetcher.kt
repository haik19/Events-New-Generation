package com.example.eventsnewgeneration

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.coroutineScope
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.sheets.v4.Sheets
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*


class ApiFetcher {

    private var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://sheets.googleapis.com/v4/spreadsheets/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    fun sendRequest(viewLifecycleOwner: LifecycleOwner) {
        val service: FetchSheetsData = retrofit.create(FetchSheetsData::class.java)
        viewLifecycleOwner.lifecycle.coroutineScope.launch(Dispatchers.IO) {
            val response = service.getData(
                "1KuR9cHpPGU_zJNvXMc991QD3WC0iEpB3hklwuQ-vwEo",
                "AIzaSyAFnyvYasKnKYkOnCJf6OW2lpIT5--_m4I"
            )
            println()
        }
    }

    var transport = AndroidHttp.newCompatibleTransport()
    var factory: JsonFactory = JacksonFactory.getDefaultInstance()
    val sheetsService = Sheets.Builder(transport, factory, null)
        .setApplicationName("My Awesome App")
        .build()
    val spreadsheetId: String = "1KuR9cHpPGU_zJNvXMc991QD3WC0iEpB3hklwuQ-vwEo"

    var ranges: List<String> = Arrays.asList(
        "Sheet1!A3:J6",  //replace with your sheet name
    )

    fun send(viewLifecycleOwner: LifecycleOwner) =
        viewLifecycleOwner.lifecycle.coroutineScope.launch(Dispatchers.IO) {
            var result = sheetsService.spreadsheets().values()
                .batchGet(spreadsheetId)
                .setKey("AIzaSyAFnyvYasKnKYkOnCJf6OW2lpIT5--_m4I")
                .setRanges(ranges).execute()

            println("test data ${result.valueRanges.joinToString()}")

            result.valueRanges[0].getValues().map {

                val finalArray = mutableListOf<String>()
                for (x in 0..9) {
                    when {
                        x == 0 -> {
                           finalArray.add(it[x].toString())
                        }
                        x > 3 -> {
                            finalArray.add(it[x].toString())
                        }
                    }
                }

                println("finalArray " + finalArray.joinToString())


                finalArray.forEach {
                }



            }

        }


}
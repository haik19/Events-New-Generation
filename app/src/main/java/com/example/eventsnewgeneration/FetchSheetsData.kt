package com.example.eventsnewgeneration

import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FetchSheetsData {
    @GET("{spreadsheetId}/values/Sheet1!A1:D5")
    suspend fun getData(
        @Path("spreadsheetId") id: String,
        @Query("key") key: String
    ): Response<JSONObject>
}
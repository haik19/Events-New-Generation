#!/usr/bin/env kscript

    @file:DependsOn("com.google.apis:google-api-services-sheets:v4-rev516-1.23.0")

import com.google.api.client.http.apache.ApacheHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.model.BatchGetValuesResponse

val tableId: String = "1KuR9cHpPGU_zJNvXMc991QD3WC0iEpB3hklwuQ-vwEo"
val ranges: List<String> = mutableListOf("A1:J1")
val sheetsService: Sheets =
    Sheets.Builder(ApacheHttpTransport(), JacksonFactory.getDefaultInstance(), null)
        .setApplicationName("Events New Generation").build()

fetchTableData()
fun fetchTableData() {
    val result: BatchGetValuesResponse = sheetsService.spreadsheets().values()
        .batchGet(tableId)
        .setKey("AIzaSyAFnyvYasKnKYkOnCJf6OW2lpIT5--_m4I")
        .setRanges(ranges).execute()
    println(" test data ${result.valueRanges.joinToString()}")
}
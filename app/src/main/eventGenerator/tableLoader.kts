#!/usr/bin/env kscript
//INCLUDE utils.kt

@file:DependsOn("com.google.apis:google-api-services-sheets:v4-rev516-1.23.0")

class TableLoader {
    val tableId: String = "1KuR9cHpPGU_zJNvXMc991QD3WC0iEpB3hklwuQ-vwEo"
    val ranges: List<String> = mutableListOf("A1:J1")
    val sheetsService: Sheets =
        Sheets.Builder(ApacheHttpTransport(), JacksonFactory.getDefaultInstance(), null)
            .setApplicationName("Events New Generation").build()

    fun fetchTableData() {
        val result: BatchGetValuesResponse = sheetsService.spreadsheets().values()
            .batchGet(tableId)
            .setKey("AIzaSyAFnyvYasKnKYkOnCJf6OW2lpIT5--_m4I")
            .setRanges(ranges).execute()
        println(" test data ${result.valueRanges.joinToString()}")
    }
}
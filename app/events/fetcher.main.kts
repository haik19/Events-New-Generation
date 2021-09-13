#!/usr/bin/env kscript

@file:DependsOn("com.google.apis:google-api-services-sheets:v4-rev516-1.23.0")
@file:DependsOn("com.squareup:kotlinpoet:1.8.0")
@file:Import("models.kts")

import com.google.api.client.http.apache.ApacheHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.model.BatchGetValuesResponse
import com.google.api.services.sheets.v4.model.ValueRange

val tableId: String = "1KuR9cHpPGU_zJNvXMc991QD3WC0iEpB3hklwuQ-vwEo"
val ranges: List<String> = mutableListOf("A1:J6")
val sheetsService: Sheets =
    Sheets.Builder(ApacheHttpTransport(), JacksonFactory.getDefaultInstance(), null)
        .setApplicationName("Events New Generation").build()


fetchTableData()
fun fetchTableData() {
    val result: BatchGetValuesResponse = sheetsService.spreadsheets().values()
        .batchGet(tableId)
        .setKey("AIzaSyAFnyvYasKnKYkOnCJf6OW2lpIT5--_m4I")
        .setRanges(ranges).execute()

    parseToEvents(result.valueRanges)
}

fun parseToEvents(result: List<ValueRange>): List<AnalyticEvent> {
    val parsedEvents = mutableListOf<AnalyticEvent>()

    val stringListResult = mapToString(result)
    val paramsName: List<String> = stringListResult[0]
    val paramsType: List<String> = stringListResult[1]

    stringListResult.forEachIndexed { index, list ->
        if (index > 1) {
            val aEvent = AnalyticEvent()
            aEvent.eventName = list[0]
            aEvent.desctiption = list[1]
            parsedEvents.add(aEvent)
            val paramsHashMap = HashMap<String, String>()
            list.forEachIndexed { paramIndex, item ->
                if (item == "x") {
                    paramsHashMap.put(paramsName.get(paramIndex), paramsType.get(paramIndex))
                }
            }
            aEvent.params = paramsHashMap
        }
    }
    println(parsedEvents.joinToString())
    return parsedEvents
}

fun mapToString(result: List<ValueRange>): List<List<String>> {
    val stringResult = mutableListOf<List<String>>()
    result.forEach { valueRange ->
        valueRange.getValues().forEach { values ->
            val params = mutableListOf<String>()
            values.forEach {
                params.add("$it")
            }
            stringResult.add(params)
        }
    }
    return stringResult
}

generateClasses()
fun generateClasses() {
    val greeterClass = ClassName("", "Greeter")

    file.writeTo(System.out)
}








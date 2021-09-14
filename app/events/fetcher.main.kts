#!/usr/bin/env kscript

@file:DependsOn("com.google.apis:google-api-services-sheets:v4-rev516-1.23.0")
@file:DependsOn("com.squareup:kotlinpoet:1.9.0")

import com.google.api.client.http.apache.ApacheHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.model.BatchGetValuesResponse
import com.google.api.services.sheets.v4.model.ValueRange
import com.squareup.kotlinpoet.*
import java.nio.file.Path

val outputPath =
    "/Users/hayk/StudioProjects/Events-New-Generation/app/src/main/java/com/example/eventsnewgeneration/events"

val tableId: String = "1KuR9cHpPGU_zJNvXMc991QD3WC0iEpB3hklwuQ-vwEo"
val ranges: List<String> = mutableListOf("A1:o33")
val sheetsService: Sheets =
    Sheets.Builder(ApacheHttpTransport(), JacksonFactory.getDefaultInstance(), null)
        .setApplicationName("Events New Generation").build()

val parsedEvents = mutableListOf<AnalyticEvent>()

fetchTableData()
fun fetchTableData() {
    val result: BatchGetValuesResponse = sheetsService.spreadsheets().values()
        .batchGet(tableId)
        .setKey("AIzaSyAFnyvYasKnKYkOnCJf6OW2lpIT5--_m4I")
        .setRanges(ranges).execute()
    parseToEvents(result.valueRanges)
}

fun parseToEvents(result: List<ValueRange>): List<AnalyticEvent> {

    val stringListResult = mapToString(result)
    val paramsName: List<String> = stringListResult[0]
    val paramsType: List<String> = stringListResult[1]

    stringListResult.forEachIndexed { index, list ->
        if (index > 1) {
            val aEvent = AnalyticEvent()
            aEvent.eventName = toCamelCase(list[0], false)
            aEvent.desctiption = list[1]
            parsedEvents.add(aEvent)
            val paramsHashMap = HashMap<String, String>()
            list.forEachIndexed { paramIndex, item ->
                if (item == "x") {
                    paramsHashMap[toCamelCase(paramsName[paramIndex], true)] =
                        paramsType[paramIndex]
                }
            }
            aEvent.params = paramsHashMap
        }
    }
    println(parsedEvents.joinToString())
    return parsedEvents
}

fun toCamelCase(name: String, asParameter: Boolean): String {
    val namesChunk = name.split("_")
    var newName = ""
    when (namesChunk.isEmpty()) {
        true -> {
            newName = if (asParameter) name else name.replace(name[0], name[0].uppercaseChar())
        }
        else -> {
            namesChunk.forEachIndexed chunk@{ index, s ->
                if (asParameter && index == 0) {
                    newName = s
                    return@chunk
                }
                newName = newName.plus(s.replaceFirst(s[0], s[0].uppercaseChar()))
            }
        }
    }

    return newName
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

fun generateParams(params: Map<String, String>): FunSpec.Builder {
    val dd = FunSpec.constructorBuilder()
    params.forEach {
        dd.addParameter(ParameterSpec.builder(it.key, getCorrectType(it.value)).build())
    }
    return dd
}

fun generateProperties(builder: TypeSpec.Builder, params: Map<String, String>) {
    params.forEach {
        builder.addProperty(
            PropertySpec.builder(it.key, getCorrectType(it.value)).initializer(it.key).build()
        )
    }
}

fun getCorrectType(type: String) = when (type) {
    "Int" -> Int::class
    "Boolean" -> Boolean::class
    "String" -> String::class
    else -> String::class
}

generateClasses()
fun generateClasses() {
    val file = FileSpec.builder("", "SocialEvents")
    parsedEvents.forEach {
        file.addType(
            TypeSpec.classBuilder(it.eventName).addModifiers(KModifier.DATA)
                .addKdoc(CodeBlock.of(it.desctiption)).apply {
                    primaryConstructor(generateParams(it.params).build())
                    generateProperties(this, it.params)
                }.build()
        )
        file.build().writeTo(Path.of(outputPath))
    }
}

data class AnalyticEvent(
    var eventName: String = "",
    var desctiption: String = "",
    var params: Map<String, String> = HashMap()
)








package com.example.eventsnewgeneration

import java.util.HashMap

data class Event (val eventName : String, val desc : String, val paramsMap : HashMap<String, String>)
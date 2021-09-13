//models.kts
data class AnalyticEvent(
    var eventName: String = "",
    var desctiption: String =  "" ,
    var params: Map<String, String> = HashMap()
)
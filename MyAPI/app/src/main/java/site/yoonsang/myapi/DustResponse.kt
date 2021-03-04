package site.yoonsang.myapi

import com.google.gson.annotations.SerializedName

class DustResponse(
    @SerializedName("list") var dataList: ArrayList<DataList> = ArrayList()
)

class DataList {
    @SerializedName("main") var main: Main? = null
    @SerializedName("components") var components: Components? = null
    @SerializedName("dt") var dt: Long? = null
}

class Main {
    @SerializedName("aqi")
    var aqi: Int = 0
}

class Components {
    @SerializedName("co")
    var co: Double = 0.toDouble()
    @SerializedName("no2")
    var no2: Double = 0.toDouble()
    @SerializedName("o3")
    var o3: Double = 0.toDouble()
    @SerializedName("so2")
    var so2: Double = 0.toDouble()
    @SerializedName("pm2_5")
    var pm2_5: Double = 0.toDouble()
    @SerializedName("pm10")
    var pm10: Double = 0.toDouble()
}
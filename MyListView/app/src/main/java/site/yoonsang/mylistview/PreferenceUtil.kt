package site.yoonsang.mylistview

import android.content.Context
import android.content.SharedPreferences

class PreferenceUtil(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("MyListViewFiles", Context.MODE_PRIVATE)

    fun getBoolean(key: String): Boolean {
        return prefs.getBoolean(key, true)
    }

    fun setBoolean(key: String, value: Boolean) {
        prefs.edit().putBoolean(key, value).apply()
    }

    fun getLong(key: String): Long {
        return prefs.getLong(key, 1613809855616)
    }

    fun setLong(key: String, value: Long) {
        prefs.edit().putLong(key, value).apply()
    }

    fun getString(key: String): String {
        return prefs.getString(key, "이러쿵 저러쿵")!!
    }

    fun setString(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }
}
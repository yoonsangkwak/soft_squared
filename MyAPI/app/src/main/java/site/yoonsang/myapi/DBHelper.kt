package site.yoonsang.myapi

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

const val DB_NAME = "MyAPI"
const val DB_VERSION = 1
const val TABLE_NAME = "fragment"
const val COL_ID = "id"
const val COL_LAT = "lat"
const val COL_LON = "lon"

data class LocationInfo(
    val lat: String,
    val lon: String
)

class DBHelper(
    context: Context,
    name: String,
    version: Int
): SQLiteOpenHelper(context, name, null, version) {
    override fun onCreate(db: SQLiteDatabase?) {
        val create = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
                COL_ID + " INTEGER PRIMARY KEY, " +
                COL_LAT + " lat, " +
                COL_LON + " lon " + ")"
        db?.execSQL(create)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}

    fun insertData(locationInfo: LocationInfo) {
        val wd = writableDatabase
        val values = ContentValues()
        values.put(COL_LAT, locationInfo.lat)
        values.put(COL_LON, locationInfo.lon)
        wd.insert(TABLE_NAME, null, values)
        wd.close()
    }

    fun selectData(): ArrayList<LocationInfo> {
        val list = arrayListOf<LocationInfo>()
        val select = "SELECT * FROM $TABLE_NAME"
        val rd = readableDatabase
        val cursor = rd.rawQuery(select, null)

        while (cursor.moveToNext()) {
            val lat = cursor.getString(cursor.getColumnIndex(COL_LAT))
            val lon = cursor.getString(cursor.getColumnIndex(COL_LON))
            val locaInfo = LocationInfo(lat, lon)
            list.add(locaInfo)
        }
        cursor.close()
        rd.close()

        return list
    }

    fun searchData(position: Int): LocationInfo {
        val select = "SELECT * FROM $TABLE_NAME"
        val rd = readableDatabase
        val cursor = rd.rawQuery(select, null)
        cursor.moveToPosition(position)
        val lat = cursor.getString(cursor.getColumnIndex(COL_LAT))
        val lon = cursor.getString(cursor.getColumnIndex(COL_LON))
        val answer = LocationInfo(lat, lon)
        cursor.close()
        rd.close()
        return answer
    }

    fun deleteData(locationInfo: LocationInfo) {
        val wd = writableDatabase
        wd.delete(TABLE_NAME, "$COL_LAT = ? AND $COL_LON = ?", arrayOf(locationInfo.lat, locationInfo.lon))
        wd.close()
    }
}
package site.yoonsang.mythread

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

const val DB_NAME = "MyThread"
const val DB_VERSION = 1
const val TABLE_NAME = "HighScores"
const val COL_ID = "id"
const val COL_NAME = "name"
const val COL_TIME = "time"
const val COL_SCORE = "score"

data class Record(
    val name: String,
    val time: Int,
    val score: String
)

class DBHelper(
    context: Context,
    name: String,
    version: Int
): SQLiteOpenHelper(context, name, null, version) {
    override fun onCreate(db: SQLiteDatabase?) {
        val create = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " NAME, " +
                COL_TIME + " TIME, " +
                COL_SCORE + " SCORE " + ")"
        db?.execSQL(create)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}

    fun insertRecord(record: Record) {
        val wd = writableDatabase
        val values = ContentValues()
        values.put(COL_NAME, record.name)
        values.put(COL_TIME, record.time)
        values.put(COL_SCORE, record.score)
        wd.insert(TABLE_NAME, null, values)
        wd.close()
    }

    fun selectRecord(): ArrayList<Record> {
        val list = arrayListOf<Record>()
        val select = "SELECT * FROM $TABLE_NAME ORDER BY $COL_TIME ASC"
        val rd = readableDatabase
        val cursor = rd.rawQuery(select, null)

        while (cursor.moveToNext()) {
            val name = cursor.getString(cursor.getColumnIndex(COL_NAME))
            val time = cursor.getInt(cursor.getColumnIndex(COL_TIME))
            val score = cursor.getString(cursor.getColumnIndex(COL_SCORE))
            val record = Record(name, time, score)
            list.add(record)
        }
        cursor.close()
        rd.close()
        return list
    }
}
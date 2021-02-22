package site.yoonsang.mylistview

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

const val DB_VERSION = 1
const val MESSAGE_TABLE_NAME = "MyChat"
const val MESSAGE_COL_ID = "id"
const val MESSAGE_COL_CONTENT = "content"

class DBHelper(
    context: Context,
    name: String,
    version: Int
) : SQLiteOpenHelper(context, name, null, version) {

    override fun onCreate(db: SQLiteDatabase?) {
        val create = "CREATE TABLE IF NOT EXISTS $MESSAGE_TABLE_NAME ($MESSAGE_COL_ID INTEGER PRIMARY KEY AUTOINCREMENT, $MESSAGE_COL_CONTENT TEXT)"
        db?.execSQL(create)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

    fun insertMyChat(message: Message) {
        val wd = writableDatabase
        val values = ContentValues()
        values.put(MESSAGE_COL_CONTENT, message.message)
        wd.insert(MESSAGE_TABLE_NAME, null, values)
        wd.close()
    }

    fun selectMyChat(): MutableList<Message> {
        val list = mutableListOf<Message>()
        val select = "SELECT * FROM $MESSAGE_TABLE_NAME"
        val rd = readableDatabase
        val cursor = rd.rawQuery(select, null)

        while (cursor.moveToNext()) {
            val message = cursor.getString(cursor.getColumnIndex(MESSAGE_COL_CONTENT))
            val myChat = Message(message)
            list.add(myChat)
        }
        cursor.close()
        rd.close()

        return list
    }
}
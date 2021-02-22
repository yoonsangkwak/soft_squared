package site.yoonsang.mylistview

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

//data class Chat(
//    val id: Int,
//    val name: String,
//    val uploadDate: Long,
//    val profileImage: Int
//)

const val TABLE_NAME = "ChatList"
const val COL_ID = "id"
const val COL_NAME = "name"
const val COL_MESSAGE = "message"
const val COL_UPLOAD_DATE = "date"
const val COL_PROFILE_IMG = "img"

class DBChatHelper(
    context: Context,
    name: String,
    version: Int
) : SQLiteOpenHelper(context, name, null, version) {

    override fun onCreate(db: SQLiteDatabase?) {
        val create = "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
                "$COL_ID id, " +
                "$COL_NAME NAME, " +
                "$COL_MESSAGE MESSAGE, " +
                "$COL_UPLOAD_DATE DATE, " +
                "$COL_PROFILE_IMG img)"
        db?.execSQL(create)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}

    fun insertChatList(chat: Chat) {
        val wd = writableDatabase
        val values = ContentValues()
        values.put(COL_ID, chat.id)
        values.put(COL_NAME, chat.name)
        values.put(COL_MESSAGE, chat.message)
        values.put(COL_UPLOAD_DATE, chat.uploadDate)
        values.put(COL_PROFILE_IMG, chat.profileImage)
        wd.insert(TABLE_NAME, null, values)
        wd.close()
    }

    fun selectChatList(): ArrayList<Chat> {
        val list = arrayListOf<Chat>()
        val select = "SELECT * FROM $TABLE_NAME"
        val rd = readableDatabase
        val cursor = rd.rawQuery(select, null)

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndex(COL_ID))
            val name = cursor.getString(cursor.getColumnIndex(COL_NAME))
            val message = cursor.getString(cursor.getColumnIndex(COL_MESSAGE))
            val date = cursor.getLong(cursor.getColumnIndex(COL_UPLOAD_DATE))
            val img = cursor.getInt(cursor.getColumnIndex(COL_PROFILE_IMG))
            val chat = Chat(id, name, message, date, img)
            list.add(chat)
        }
        cursor.close()
        rd.close()

        return list
    }

    fun updateChatList(chat: Chat) {
        val wd = writableDatabase
        val values = ContentValues()
        values.put(COL_ID, chat.id)
        values.put(COL_NAME, chat.name)
        values.put(COL_MESSAGE, chat.message)
        values.put(COL_UPLOAD_DATE, chat.uploadDate)
        values.put(COL_PROFILE_IMG, chat.profileImage)
        wd.update(TABLE_NAME, values, "id = ${chat.id}", null)
        wd.close()
    }

    fun deleteChatList(chat: Chat) {
        val wd = writableDatabase
        wd.delete(TABLE_NAME, "id = ${chat.id}", null)
        wd.close()
    }
}
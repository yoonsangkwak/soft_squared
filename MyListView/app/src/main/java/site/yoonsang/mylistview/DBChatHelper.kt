package site.yoonsang.mylistview

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

const val DB_CHAT = "chatList"
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
        val create = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " NAME, " +
                COL_MESSAGE + " MESSAGE, " +
                COL_UPLOAD_DATE + " DATE, " +
                COL_PROFILE_IMG + " img " + ")"
        db?.execSQL(create)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}

    fun insertChatList(chat: Chat) {
        val wd = writableDatabase
        val values = ContentValues()
        values.put(COL_NAME, chat.name)
        values.put(COL_MESSAGE, chat.message)
        values.put(COL_UPLOAD_DATE, chat.uploadDate)
        values.put(COL_PROFILE_IMG, chat.profileImage)
        wd.insert(TABLE_NAME, null, values)
        wd.close()
    }

    fun selectChatList(): ArrayList<Chat> {
        val list = arrayListOf<Chat>()
        val select = "SELECT * FROM $TABLE_NAME ORDER BY $COL_UPLOAD_DATE DESC"
        val rd = readableDatabase
        val cursor = rd.rawQuery(select, null)

        while (cursor.moveToNext()) {
            val name = cursor.getString(cursor.getColumnIndex(COL_NAME))
            val message = cursor.getString(cursor.getColumnIndex(COL_MESSAGE))
            val date = cursor.getLong(cursor.getColumnIndex(COL_UPLOAD_DATE))
            val img = cursor.getInt(cursor.getColumnIndex(COL_PROFILE_IMG))
            val chat = Chat(name, message, date, img)
            list.add(chat)
        }
        cursor.close()
        rd.close()

        return list
    }

    fun searchChat(newName: String): Chat {
        val select = "SELECT * FROM $TABLE_NAME"
        val rd = readableDatabase
        val cursor = rd.rawQuery(select, null)
        var answer = Chat("", "", 0L, 0)

        while (cursor.moveToNext()) {
            val name = cursor.getString(cursor.getColumnIndex(COL_NAME))
            val message = cursor.getString(cursor.getColumnIndex(COL_MESSAGE))
            val date = cursor.getLong(cursor.getColumnIndex(COL_UPLOAD_DATE))
            val img = cursor.getInt(cursor.getColumnIndex(COL_PROFILE_IMG))
            if (name == newName) {
                answer = Chat(name, message, date, img)
                break
            }
        }

        cursor.close()
        rd.close()

        return answer
    }

    fun updateChatList(chat: Chat) {
        val wd = writableDatabase
        val values = ContentValues()
        values.put(COL_NAME, chat.name)
        values.put(COL_MESSAGE, chat.message)
        values.put(COL_UPLOAD_DATE, chat.uploadDate)
        values.put(COL_PROFILE_IMG, chat.profileImage)
        wd.update(TABLE_NAME, values, "$COL_NAME = ?", arrayOf(chat.name))
        wd.close()
    }

    fun deleteChatList(chat: Chat) {
        val wd = writableDatabase
        wd.delete(TABLE_NAME, "$COL_NAME = ?", arrayOf(chat.name))
        wd.close()
    }
}
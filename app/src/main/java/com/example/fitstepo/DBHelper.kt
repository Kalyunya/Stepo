package com.example.fitstepo

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.core.database.sqlite.transaction

class DBHelper(context: Context) :
    SQLiteOpenHelper(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        createUsersTable(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS Users")
        onCreate(db)
    }




    private fun createUsersTable(db: SQLiteDatabase) {
        db.transaction {
            execSQL(
                """
                CREATE TABLE IF NOT EXISTS Users (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    fullName TEXT,
                    password TEXT,
                    email TEXT UNIQUE,
                    gender TEXT,
                    age INTEGER,
                    height REAL,
                    weight REAL,
                    goals TEXT,
                    time TEXT,
                    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    avatar TEXT
                );
                
                """.trimIndent()
            )
        }
    }
    fun getUserByEmail(email: String): User? {
        val db = readableDatabase
        val cursor = db.query(
            "Users",
            null,
            "email = ?",
            arrayOf(email),
            null, null, null
        )

        var user: User? = null
        if (cursor.moveToFirst()) {
            val fullName = cursor.getString(cursor.getColumnIndexOrThrow("fullName"))
            val password = cursor.getString(cursor.getColumnIndexOrThrow("password"))
            val age = cursor.getInt(cursor.getColumnIndexOrThrow("age"))
            val height = cursor.getInt(cursor.getColumnIndexOrThrow("height"))
            val weight = cursor.getInt(cursor.getColumnIndexOrThrow("weight"))
            val gender = cursor.getString(cursor.getColumnIndexOrThrow("gender"))
            val goals = cursor.getString(cursor.getColumnIndexOrThrow("goals"))
            val time = cursor.getString(cursor.getColumnIndexOrThrow("time"))
            val avatar = cursor.getString(cursor.getColumnIndexOrThrow("avatar"))
            user = User(fullName, email, password, age, height, weight, gender, goals, time, avatar )
        }
        cursor.close()
        return user
    }

}
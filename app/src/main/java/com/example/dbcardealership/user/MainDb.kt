package com.example.dbcardealership.user

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class], version = 2)
abstract class MainDb : RoomDatabase() {
    abstract fun userDao() : UserDao

    companion object{
        fun getDb(context: Context): MainDb{
            return Room.databaseBuilder(
                context.applicationContext,
                MainDb::class.java,
                "my_sec.db"
            ).build()
        }
    }
}
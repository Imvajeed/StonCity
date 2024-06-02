package com.example.stonecityi.roomdatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UserData::class], version = 1)
abstract class RoomDataBaseSingle : RoomDatabase() {
    abstract val userDao : UserDao
    companion object{
        @Volatile
        private var INSTANCE: RoomDataBaseSingle ?=null
        fun getInstance(context: Context):RoomDataBaseSingle{
            synchronized(this){
                var instance = INSTANCE
                if(instance==null){
                    //Creating databse objects
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        RoomDataBaseSingle::class.java,
                        "StoneCity_db"
                    ).allowMainThreadQueries().build()
                }
                return instance
            }
        }
    }
}
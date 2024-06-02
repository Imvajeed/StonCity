package com.example.stonecityi.roomdatabase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {
    @Insert
    suspend fun insertData(data: UserData) :Long

    @Update
    suspend fun updateData(data: UserData)

    @Delete
    suspend fun deleteData(data: UserData)

    @Query("select * from UserInformation")
    fun getUserInformation(): UserData

    @Query("delete from UserInformation")
    fun deleteAll()

    @Query("select count() from UserInformation where UserId = :id")
    fun userPresent(id : String) : Int

}
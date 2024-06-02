package com.example.stonecityi.roomdatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "UserInformation")
data class UserData(
    @PrimaryKey
    @ColumnInfo(name = "UserId")
    val userId : String,
    @ColumnInfo(name = "UserName")
    val userName : String,
    @ColumnInfo(name = "UserEmail")
    val userEmail: String,
    @ColumnInfo(name = "UserContact")
    val contactNum: String,
    @ColumnInfo(name = "UserAddress")
    val userAddress: String,
    @ColumnInfo(name = "UserImage")
    val imageUrl : String
)

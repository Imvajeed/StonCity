package com.example.stonecityi.roomdatabase

class UserRepository(val dao: UserDao) {

    fun getUserInformation():UserData{
        return dao.getUserInformation()
    }

    suspend fun insertData(userData: UserData) : Long{
        return dao.insertData(userData)
    }
    suspend fun deleteData(userData: UserData){
        return dao.deleteData(userData)
    }
    suspend fun updataData(userData: UserData){
        return dao.updateData(userData)
    }
    fun deleteAll(){
        return dao.deleteAll()
    }
    fun findUser(id : String):Int{
        return dao.userPresent(id)
    }
}
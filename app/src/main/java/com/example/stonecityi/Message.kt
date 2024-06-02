package com.example.stonecityi

class Message{
    var msg : String? = null
    var userId : String? = null

    constructor(){}

    constructor(message: String?,userId: String?){
        this.msg = message
        this.userId = userId
    }
}
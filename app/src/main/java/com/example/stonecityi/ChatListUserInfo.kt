package com.example.stonecityi

class ChatListUserInfo {
    var userName : String? = null
    var userId : String? = null
    var userImgUrl: String? = null
    constructor(){}
    constructor(username : String?, userId : String?, userImgUrl : String? ){
        this.userName = username
        this.userId = userId
        this.userImgUrl = userImgUrl
    }
}
package com.example.myinsta.Model

import com.google.firebase.auth.FirebaseAuth

class User {

    private var uid:String = ""
    private var username:String  = ""
    private var fullname:String  = ""
    private var bio:String  = ""
    private var image:String  = ""



    constructor()

    constructor(username:String, fullname:String, bio:String,  image:String ,  uid:String){
        this.username = username
        this.fullname = fullname
        this.bio = bio
        this.image = image
        this.uid = uid
    }

    fun getUsername():String{
        return username
    }

    fun setUsername(userName:String){
        this.username =  userName
    }


    fun getFullname():String{
        return fullname
    }
    fun setFullname(fullName:String){
        this.fullname =  fullName
    }

    fun getBio():String{
        return bio
    }
    fun setBio(bio:String){
        this.bio =  bio
    }


    fun getImage():String{
        return image
    }
    fun setImage(image:String){
        this.image =  image
    }


    fun getUId():String{
        return uid
    }
    fun setUId(uid:String){
        this.uid = uid
    }

}
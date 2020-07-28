package com.example.myinsta.Model

class Post {
    private var postid : String =""
    private var image : String =""
    private var publisher : String =""
    private var description: String =""

    constructor()

    constructor(postid: String, image: String, publisher: String, description: String) {
        this.postid = postid
        this.image = image
        this.publisher = publisher
        this.description = description
    }


    fun getPostId():String{
        return postid
    }
    fun setPostId(postid:String){
        this.postid =  postid
    }


    fun getImage():String{
        return image
    }
    fun setImage(image:String){
        this.image = image
    }


    fun getPublisher():String{
        return publisher
    }

    fun setPublisher(publisher:String){
        this.publisher =  publisher
    }

    fun getDescription():String{
        return description
    }

    fun setDescription(description:String){
        this.description =  description
    }



}
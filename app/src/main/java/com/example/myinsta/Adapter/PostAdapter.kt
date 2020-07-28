package com.example.myinsta.Adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.example.myinsta.MainActivity
import com.example.myinsta.Model.Post
import com.example.myinsta.Model.User
import com.example.myinsta.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class PostAdapter
    (private val mContext:Context,
     private val mPost: List<Post>): RecyclerView.Adapter<PostAdapter.ViewHolder>() {


    private var firebaseUser: FirebaseUser?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view = LayoutInflater.from(mContext).inflate(R.layout.posts_layout,parent,false)
    return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mPost.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        firebaseUser = FirebaseAuth.getInstance().currentUser
        val post = mPost[position]
        Log.v("image",post.getImage())
        Picasso.get().load(post.getImage()).into(holder.postImage)
        holder.description.text =  post.getDescription()
//        Picasso.get().load(post.getImage())).into(holder.postImage)

        publisherInfo(holder.profileImage, holder.userName, holder.publisher, post.getPublisher())
        isLikes(post.getPostId(),holder.likeButton)
        numberOfLikes(holder.like, post.getPostId() )

        holder.likeButton.setOnClickListener {

            if (holder.likeButton.tag == "Like")
            {
                FirebaseDatabase.getInstance().reference
                    .child("Likes")
                    .child(post.getPostId())
                    .child(firebaseUser!!.uid)
                    .setValue(true)
            }else
            {
                FirebaseDatabase.getInstance().reference
                    .child("Likes")
                    .child(post.getPostId())
                    .child(firebaseUser!!.uid)
                    .removeValue()

//                val intent = Intent(mContext,MainActivity::class.java)
//                mContext.startActivity(intent)

            }
        }

    }



    private fun numberOfLikes(like: TextView, postId: String) {

        val LikesRef = FirebaseDatabase.getInstance().reference
            .child("Likes").child(postId)
        LikesRef.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot)
            {
                if (snapshot.exists()) {
                    like.text = snapshot.childrenCount.toString() +" Likes"
                }
            }

        })
    }






    private fun isLikes(postId: String, likeButton: ImageView) {
        val firebaseUser  = FirebaseAuth.getInstance().currentUser

        val LikesRef = FirebaseDatabase.getInstance().reference
            .child("Likes").child(postId)

        LikesRef.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.child(firebaseUser!!.uid).exists()){
                    likeButton.setImageResource(R.drawable.heart_clicked)
                    likeButton.tag = "Liked"
                }
                else{
                    likeButton.setImageResource(R.drawable.heart_not_clicked)
                    likeButton.tag = "Like"
                }
            }

        })
    }


    inner class ViewHolder(@NonNull itemView: View): RecyclerView.ViewHolder(itemView){

        var profileImage: CircleImageView
        var postImage : ImageView
        var likeButton : ImageView
        var commentButton : ImageView
        var saveButton : ImageView
        var userName : TextView
        var like : TextView
        var publisher : TextView
        var description: TextView
        var comments: TextView

        init {
            profileImage = itemView.findViewById(R.id.user_profile_image_post)
            postImage = itemView.findViewById(R.id.post_image_home)
            likeButton = itemView.findViewById(R.id.post_image_like_btn)
            commentButton = itemView.findViewById(R.id.post_image_comment_btn)
            saveButton = itemView.findViewById(R.id.post_save_comment_btn)
            userName = itemView.findViewById(R.id.user_name_post)
            like = itemView.findViewById(R.id.likes)
            publisher = itemView.findViewById(R.id.publisher)
            description = itemView.findViewById(R.id.description)
            comments = itemView.findViewById(R.id.comments )
        }


    }


    private fun publisherInfo(profileImage: CircleImageView, userName: TextView, publisher: TextView, publisherId: String) {

        Log.v("this is in pubinfo" , publisherId)
        val userRef = FirebaseDatabase.getInstance().reference.child("Users").child(publisherId)

        userRef.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists())
                {
                    val user =  snapshot.getValue<User>(User::class.java)
                    Picasso.get().load(user!!.getImage()).placeholder(R.drawable.profile).into(profileImage)
                    userName.text = user.getUsername()
                    publisher.text= user.getFullname()
                }
            }

        })

     }


}
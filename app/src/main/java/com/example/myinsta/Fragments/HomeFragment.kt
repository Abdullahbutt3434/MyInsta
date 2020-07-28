package com.example.myinsta.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myinsta.Adapter.PostAdapter
import com.example.myinsta.Model.Post
import com.example.myinsta.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class HomeFragment : Fragment() {
    private var postAdapter : PostAdapter ? =null
    private var postList:MutableList<Post>? = null
    private var followingList: MutableList<Post> ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_home, container, false)

        var recyclerView: RecyclerView? = null
        recyclerView =  view.findViewById(R.id.recycler_view_home)
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        recyclerView.layoutManager = linearLayoutManager


        postList = ArrayList()
        postAdapter = context?.let { PostAdapter( it, postList as ArrayList<Post>) }
        recyclerView.adapter = postAdapter

        checkFollowings()


        return view
    }

    private fun checkFollowings() {
        followingList = ArrayList()
        val followingRef = FirebaseDatabase.getInstance().reference
                .child("Follow").child(FirebaseAuth.getInstance().currentUser!!.uid)
                .child("Following")
//
        followingRef.addValueEventListener(object : ValueEventListener {

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists())
                    {
                    (followingList as ArrayList<String>).clear()

                    for(datasnapshot in snapshot.children){
                        datasnapshot.key?.let{
                            (followingList as ArrayList<String>).add(it)
                        }
                    }

                    RetrivePosts()
                }
            }

        })
    }
//
    private fun RetrivePosts() {
        val postRef = FirebaseDatabase.getInstance().reference
            .child("Posts")
        postRef.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                postList?.clear()

                for(snapshot in snapshot.children){
                    val post = snapshot.getValue(Post::class.java)
                    Log.v("image" , post!!.getImage())
                    for(id in  (followingList as ArrayList<String>))
                    {
                        if(post!!.getPublisher() == id)
                        {
                            postList!!.add(post)
                        }

                        postAdapter!!.notifyDataSetChanged()
                    }
                }
            }

        })
    }

}
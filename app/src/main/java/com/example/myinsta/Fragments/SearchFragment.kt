 package com.example.myinsta.Fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myinsta.Adapter.UserAdapter
import com.example.myinsta.Model.User
import com.example.myinsta.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_search.view.*
import android.util.Log.d as d1

 /**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragment : Fragment() {

     private var recyclerView : RecyclerView? =null
     private var userAdapter : UserAdapter? =null
     private var mUser: MutableList<User> ? = null

     override fun onCreateView(
         inflater: LayoutInflater, container: ViewGroup?,
         savedInstanceState: Bundle?
     ): View? {
         val view = inflater.inflate(R.layout.fragment_search, container, false)

         recyclerView = view.findViewById(R.id.recycler_view_search)
         recyclerView?.setHasFixedSize(true)
         recyclerView?.layoutManager = LinearLayoutManager(context)

         mUser = ArrayList()
         userAdapter = context?.let { UserAdapter(it,  mUser as ArrayList<User>, true) }
         recyclerView?.adapter = userAdapter

         view.search_edit_text.addTextChangedListener(object : TextWatcher{
             override fun afterTextChanged(s: Editable?) {
             }

             override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
             }

             override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                 if(view.search_edit_text.text.toString() == ""){

                 }
                 else{
                     recyclerView?.visibility = View.VISIBLE
                     retriveUser()
                     searhUser(s.toString().toLowerCase())
                 }
             }

         })

         return view
     }

     private fun searhUser(input: String) {
         val query = FirebaseDatabase.getInstance().getReference()
             .child("Users")
             .orderByChild("fullname")
             .startAt(input)
             .endAt(input+ "\uf8ff")

         query.addValueEventListener(object : ValueEventListener
         {
             override fun onCancelled(error: DatabaseError) {
             }

             override fun onDataChange(dataSnapshot: DataSnapshot) {

                 mUser?.clear()
                 for(snapshot in dataSnapshot.children){
                     val user = snapshot.getValue(User::class.java)
                     if(user != null){
                         mUser?.add(user)
                     }
                 }

                 userAdapter?.notifyDataSetChanged()

             }

         })
     }


     private fun retriveUser() {
         val userRef = FirebaseDatabase.getInstance().getReference().child("Users")
         userRef.addValueEventListener(object : ValueEventListener
         {
             override fun onCancelled(error: DatabaseError) {
             }

             override fun onDataChange(dataSnapshot: DataSnapshot) {

                 if(view?.search_edit_text?.text.toString() == "")
                 {
                     mUser?.clear()
                     for(snapshot in dataSnapshot.children){
                         val user = snapshot.getValue(User::class.java)

                         if(user != null){
                             mUser?.add(user)
                         }
                     }

                     userAdapter?.notifyDataSetChanged()

                 }
             }

         })
     }


 }
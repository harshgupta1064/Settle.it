package com.example.splitwise.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.content.Context
import android.content.Intent
import com.example.splitwise.GroupsActivity
import com.example.splitwise.databinding.GroupsItemBinding
import com.example.splitwise.models.GroupModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class groupsAdapter(
    private val groupIdList: ArrayList<String>,
    private val context: Context
) : RecyclerView.Adapter<groupsAdapter.GroupsViewHolder>() {
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var rootRef = FirebaseDatabase.getInstance().reference
    private var selectedGroupName :String = String()
    private var selectedGroupId :String = String()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupsViewHolder {
        val binding = GroupsItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return GroupsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GroupsViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return groupIdList.size
    }

    inner class GroupsViewHolder(private val binding: GroupsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val groupId = groupIdList[position]
            binding.apply {
                rootRef.child("Groups").child(groupId).addListenerForSingleValueEvent(object:ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val group = snapshot.getValue(GroupModel::class.java)
                        if(group!=null){
                            selectedGroupName = group.groupName!!
                            selectedGroupId = group.groupId!!
                            groupName.text = selectedGroupName
                            noOfMembers.text = group.groupMembers!!.size.toString()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
                binding.groupCardView.setOnClickListener{
                    val intent = Intent(context,GroupsActivity::class.java)
                    intent.putExtra("groupName",selectedGroupName)
                    intent.putExtra("groupId",selectedGroupId)
                    context.startActivity(intent)
                }
            }
        }
    }
}

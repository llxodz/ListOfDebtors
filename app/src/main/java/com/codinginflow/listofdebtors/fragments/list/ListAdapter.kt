package com.codinginflow.listofdebtors.fragments.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.codinginflow.listofdebtors.R
import com.codinginflow.listofdebtors.model.User
import kotlinx.android.synthetic.main.custom_row.view.*
import java.sql.Date
import java.sql.Timestamp
import java.text.DateFormat

class ListAdapter : RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    private var userList = emptyList<User>()

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.custom_row, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = userList[position]

        val date = DateFormat.getDateInstance().format(currentItem.timestamp)

        holder.itemView.tv_name.text = currentItem.name
        holder.itemView.tv_description.text = currentItem.note
        holder.itemView.tv_amount.text = currentItem.amount.toInt().toString()
        holder.itemView.tv_date.text = date

        holder.itemView.rowLayout.setOnClickListener {
            val action = ListFragmentDirections.actionListFragmentToUpdateFragment(currentItem)
            holder.itemView.findNavController().navigate(action)
        }

    }

    fun setData(user: List<User>) {
        this.userList = user
        notifyDataSetChanged()
    }
}
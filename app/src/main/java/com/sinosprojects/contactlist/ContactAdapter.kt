package com.sinosprojects.contactlist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.contact_card.view.*

class ContactAdapter(items: List<ContactDTO>, ctx: Context) : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {
    private var contactList = items
    private var context = ctx

    override fun getItemCount(): Int {
        return contactList.size
    }

    override fun onBindViewHolder(holder: ContactAdapter.ContactViewHolder, position: Int) {
        holder.name.text = contactList[position].name
        holder.mobile.text = contactList[position].number

        if(contactList[position].image != null) {
            holder.profileImage.setImageBitmap(contactList[position].image)
        } else {
            holder.profileImage.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.ic_launcher_round))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactAdapter.ContactViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.contact_card, parent, false)
        return ContactViewHolder(view)
    }

    class ContactViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val name = v.contact_name!!
        val mobile = v.contact_mobile!!
        val profileImage = v.contact_image!!
    }
}
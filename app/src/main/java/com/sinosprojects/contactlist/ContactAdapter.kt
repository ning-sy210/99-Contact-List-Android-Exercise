package com.sinosprojects.contactlist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.contact_card.view.*

class ContactAdapter(items: List<ContactDTO>, ctx: Context) : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {
    private var contactList = items
    private var context = ctx
    private var previousExpandedContactPos : Int? = null

    override fun getItemCount(): Int {
        return contactList.size
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.name.text = contactList[position].name
        holder.mobile.text = contactList[position].number

        if(contactList[position].image != null) {
            holder.profileImage.setImageBitmap(contactList[position].image)
        } else {
            holder.profileImage.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.ic_launcher_round))
        }

        var isExpanded = contactList[position].isExpanded

        if(isExpanded) {
            holder.additionalInfo.visibility = View.VISIBLE
        } else {
            holder.additionalInfo.visibility = View.GONE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.contact_card, parent, false)
        return ContactViewHolder(view, contactList)
    }

    inner class ContactViewHolder : RecyclerView.ViewHolder, View.OnClickListener {
        var name : TextView
        var mobile : TextView
        var profileImage : ImageView

        var additionalInfo : ConstraintLayout
        var contactList : List<ContactDTO>

        constructor(view: View, contacts: List<ContactDTO>) : super(view) {
            name = view.contact_name!!
            mobile = view.contact_mobile!!
            profileImage = view.contact_image!!

            additionalInfo = view.additional_info
            contactList = contacts
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val contact = contactList[adapterPosition]
            contact.isExpanded = !contact.isExpanded
            notifyItemChanged(adapterPosition)

            if (previousExpandedContactPos != null) {
                val prevContact = contactList[previousExpandedContactPos!!]
                prevContact.isExpanded = false
                notifyItemChanged(previousExpandedContactPos!!)
            }

            previousExpandedContactPos = adapterPosition
        }
    }
}
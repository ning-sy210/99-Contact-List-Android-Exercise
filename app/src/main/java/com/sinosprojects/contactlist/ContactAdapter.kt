package com.sinosprojects.contactlist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.contact_card.view.*

class ContactAdapter(items: ArrayList<Contact>, ctx: Context) : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>(), Filterable {
    private var contactList = items
    private var contactListFull = ArrayList(items)
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
        var contactList : List<Contact>

        constructor(view: View, contacts: List<Contact>) : super(view) {
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

            if (previousExpandedContactPos != null && previousExpandedContactPos != adapterPosition) {
                val prevContact = contactList[previousExpandedContactPos!!]
                prevContact.isExpanded = false
                notifyItemChanged(previousExpandedContactPos!!)
            }

            if (adapterPosition != previousExpandedContactPos) {
                previousExpandedContactPos = adapterPosition
            } else {
                previousExpandedContactPos = null
            }
        }
    }

    override fun getFilter(): Filter {
        return filter;
    }

    private val filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            var filteredList = ArrayList<Contact>()

            if (constraint == null || constraint.isEmpty()) {
                contactListFull[previousExpandedContactPos!!].isExpanded = false
                filteredList.addAll(contactListFull)
            } else {
                if (previousExpandedContactPos != null) {
                    contactListFull[previousExpandedContactPos!!].isExpanded = false
                    previousExpandedContactPos = null
                }

                val filteredPattern = constraint.toString().toLowerCase().trim()

                for(item in contactListFull) {
                    if (item.name.toLowerCase().contains(filteredPattern) || item.number.contains(filteredPattern)) {
                        filteredList.add(item)
                    }
                }
            }

            var results = FilterResults()
            results.values = filteredList

            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            contactList.clear()
            if (results != null) {
                contactList.addAll(results.values as Collection<Contact>)
            }
            notifyDataSetChanged()
        }
    }
}
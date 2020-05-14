package com.sinosprojects.contactlist

import android.app.AlertDialog
import android.content.ContentResolver
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {
    private val REQUEST_READ_CONTACTS_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        contactList.layoutManager = LinearLayoutManager(this)

        if (!hasContactListAccessPermission()) {
            requestContactListAccessPermission()
        }

        readContactList(contactList)
    }

    fun readContactList(rv: RecyclerView) {
        val contactList : MutableList<ContactDTO> = ArrayList()
        val contacts = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.Data.DISPLAY_NAME + " ASC")

        if (contacts != null) {
            while (contacts.moveToNext()) {
                val name = contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val number = contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                val obj = ContactDTO()
                obj.name = name
                obj.number = number

                val photo_uri = contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI))

                if (photo_uri != null) {
                    obj.image = MediaStore.Images.Media.getBitmap(contentResolver, Uri.parse(photo_uri))
                }

                contactList.add(obj)
            }

            rv.adapter = ContactAdapter(contactList, this)
            contacts.close()
        }
    }

    private fun hasContactListAccessPermission() : Boolean {
        return ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestContactListAccessPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_CONTACTS)) {
            AlertDialog.Builder(this)
                    .setTitle("Permission Required")
                    .setMessage("This permission is needed in order to display your contacts.")
                    .setPositiveButton("Okay", DialogInterface.OnClickListener { dialog, which ->
                        ActivityCompat.requestPermissions(this,
                                arrayOf(android.Manifest.permission.READ_CONTACTS), REQUEST_READ_CONTACTS_CODE)
                    })
                    .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which ->
                        dialog.dismiss()
                    })
                    .create()
                    .show()
        } else {
            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.READ_CONTACTS), REQUEST_READ_CONTACTS_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_READ_CONTACTS_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

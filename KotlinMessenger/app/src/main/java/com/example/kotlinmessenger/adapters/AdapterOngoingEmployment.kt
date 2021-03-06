package com.example.kotlinmessenger.adapters

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinmessenger.*
import com.example.kotlinmessenger.jobs.jobItem
import com.example.kotlinmessenger.models.User
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue
import com.google.firebase.firestore.FirebaseFirestore
import com.example.kotlinmessenger.DetailsPage.*

class AdapterOngoingEmployment (var parentFragmentManager : FragmentManager,
                                val arrayAdapter: ArrayList<jobItem>, val isMine : Boolean) : RecyclerView.Adapter<AdapterOngoingEmployment.ListViewHolder>()
{
    inner class  ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var _title : TextView = itemView.findViewById(R.id.ongoingEmploymentJobtitle)
        var _employer : TextView = itemView.findViewById(R.id.OngoingEmploymentEmployer)
        val _detailsButton : ImageButton = itemView.findViewById(R.id.ongoingEmploymentDetails)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view : View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ongoing_employment, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val emp = arrayAdapter[position]
        Log.i("WorkerID", emp.recruiterId)

        val mDatabase = FirebaseDatabase.getInstance().getReference()
        mDatabase.child("users").child(emp.recruiterId).get().addOnSuccessListener {
            Log.i("firebase", "Got value ${it.value}")
            val value = it.getValue<User>()
            holder._title.text = emp.title
            if (value != null) {
                holder._employer.text = value.username
            }
            holder._detailsButton.setOnClickListener {
                var mBundle = Bundle()
                mBundle.putString("jobId", emp.id)
                mBundle.putString("jobname", emp.title)
                mBundle.putString("recruiterId", emp.recruiterId)
                mBundle.putString("workerId", emp.worker)
                if (value != null) {
                    mBundle.putString("worker", value.username)
                }

                val jobDetails = ongoing_employment_detail()
                jobDetails.arguments = mBundle
                changeFragment(R.id.ongoingframelayout, jobDetails, parentFragmentManager)
            }
        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }
    }

    override fun getItemCount(): Int {
        return arrayAdapter.size
    }
}
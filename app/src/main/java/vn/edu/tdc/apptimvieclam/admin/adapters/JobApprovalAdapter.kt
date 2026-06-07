package vn.edu.tdc.apptimvieclam.admin.adapters

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import vn.edu.tdc.apptimvieclam.databinding.AdminItemJobApprovalBinding
import vn.edu.tdc.apptimvieclam.models.Company

class JobApprovalAdapter(
    private val context: Activity,
    private val list: ArrayList<Company>
) : BaseAdapter() {

    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup?
    ): View {

        val binding: AdminItemJobApprovalBinding

        if (convertView == null) {

            binding =
                AdminItemJobApprovalBinding.inflate(
                    context.layoutInflater,
                    parent,
                    false
                )

        } else {

            binding = AdminItemJobApprovalBinding.bind(
                    convertView
                )
        }

        val job = list[position]

        binding.txtJobTitle.text =
            job.title

        binding.txtCompany.text =
            job.company.name

        binding.btnApproveJob.setOnClickListener {

            FirebaseDatabase
                .getInstance()
                .reference
                .child("jobs")
                .child(job.jobId)
                .child("status")
                .setValue("approved")

            Toast.makeText(
                context,
                "Đã duyệt bài đăng",
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.btnRejectJob.setOnClickListener {

            FirebaseDatabase
                .getInstance()
                .reference
                .child("jobs")
                .child(job.jobId)
                .removeValue()

            Toast.makeText(
                context,
                "Đã từ chối bài đăng",
                Toast.LENGTH_SHORT
            ).show()
        }

        return binding.root
    }
}
package vn.edu.tdc.apptimvieclam.admin.adapters

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import vn.edu.tdc.apptimvieclam.databinding.AdminItemRecruiterBinding
import vn.edu.tdc.apptimvieclam.models.User

class RecruiterAdapter(
    private val context: Activity,
    private val list: ArrayList<User>
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

        val binding: AdminItemRecruiterBinding

        if (convertView == null) {

            binding = AdminItemRecruiterBinding.inflate(
                context.layoutInflater,
                parent,
                false
            )

        } else {

            binding = AdminItemRecruiterBinding.bind(convertView)
        }

        val recruiter = list[position]

        binding.txtCompanyName.text =
            recruiter.fullName

        binding.txtRecruiterEmail.text =
            recruiter.email

        binding.btnApproveRecruiter.setOnClickListener {

            FirebaseDatabase
                .getInstance()
                .reference
                .child("users")
                .child(recruiter.uid)
                .child("status")
                .setValue("approved")

            Toast.makeText(
                context,
                "Đã duyệt nhà tuyển dụng",
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.btnRejectRecruiter.setOnClickListener {

            FirebaseDatabase
                .getInstance()
                .reference
                .child("users")
                .child(recruiter.uid)
                .removeValue()

            Toast.makeText(
                context,
                "Đã từ chối nhà tuyển dụng",
                Toast.LENGTH_SHORT
            ).show()
        }

        return binding.root
    }
}
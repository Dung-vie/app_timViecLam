package vn.edu.tdc.apptimvieclam.adapters

import android.app.Activity
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import vn.edu.tdc.apptimvieclam.databinding.ListJobLayoutBinding
import vn.edu.tdc.apptimvieclam.models.Company
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.database
import vn.edu.tdc.apptimvieclam.R


class MyListViewAdapter(private val context: Activity,
                        private val list: ArrayList<Company>,
                        private val onJobRemoved: ((Company) -> Unit)? = null
    )
    : BaseAdapter() {
    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Any {
        return list.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding: ListJobLayoutBinding

        if (convertView == null) {
            binding = ListJobLayoutBinding.inflate(context.layoutInflater,  parent, false)
        } else { // Tai su dung binding da co
            binding = ListJobLayoutBinding.bind(convertView)
        }

        val company = list.get(position)

        checkSavedJob(company) { saved ->

            if (saved) {

                binding.ivBookmark.setImageResource(
                    R.drawable.ic_bookmark_filled
                )

            } else {

                binding.ivBookmark.setImageResource(
                    R.drawable.ic_bookmark_border
                )
            }
        }

        Glide.with(context)
            .load(company.company.image) // URL từ API
            .into(binding.logo) // truyền vào ImageView

        binding.txtTitle.text =  "Tuyển ${company.title}"
        binding.txtCompany.text = "${company.company.name}"
        binding.txtLocation.text = "Nơi làm việc: ${company.location}"
        binding.txtType.text = "Tuyển: ${company.types?.firstOrNull()?.nameType ?: "Part-time"}"
        binding.txtPublish.text = "Ngày đăng tuyển: ${company.publish}"
//        Log.d("text", "${binding.txtType.text}")
//        Log.d("text2", "${company.types?.firstOrNull()?.nameType}")
        binding.ivBookmark.setOnClickListener {

            toggleSaveJob(company) { saved ->

                if (saved) {

                    binding.ivBookmark.setImageResource(
                        R.drawable.ic_bookmark_filled
                    )

                } else {

                    binding.ivBookmark.setImageResource(
                        R.drawable.ic_bookmark_border
                    )
                }
            }
        }
        return binding.root
    }
    // Luu job
    private fun checkSavedJob(
        company: Company,
        callback: (Boolean) -> Unit
    ) {

        val user = FirebaseAuth.getInstance().currentUser
            ?: return

        val jobKey =
            company.title + "_" + company.company.name

        Firebase.database.reference
            .child("saved_jobs")
            .child(user.uid)
            .child(jobKey)
            .get()
            .addOnSuccessListener {

                callback(it.exists())
            }
    }
    private fun toggleSaveJob(
        company: Company,
        onResult: (Boolean) -> Unit
    ) {

        val user = FirebaseAuth.getInstance().currentUser
            ?: return

        val jobKey =
            company.title + "_" + company.company.name

        val saveRef = Firebase.database.reference
            .child("saved_jobs")
            .child(user.uid)
            .child(jobKey)

        saveRef.get()
            .addOnSuccessListener { snapshot ->

                if (snapshot.exists()) {

                    saveRef.removeValue()
                        .addOnSuccessListener {

                            onResult(false)

                            onJobRemoved?.invoke(company)
                        }

                } else {

                    saveRef.setValue(company)
                        .addOnSuccessListener {

                            onResult(true)
                        }
                }
            }
    }

}
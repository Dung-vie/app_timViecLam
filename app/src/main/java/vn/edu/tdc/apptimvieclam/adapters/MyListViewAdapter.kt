package vn.edu.tdc.apptimvieclam.adapters

import android.app.Activity
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import vn.edu.tdc.apptimvieclam.databinding.ListJobLayoutBinding
import vn.edu.tdc.apptimvieclam.models.Company
import com.bumptech.glide.Glide


class MyListViewAdapter(private val context: Activity, private val list: ArrayList<Company>) : BaseAdapter() {
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
        return binding.root
    }
}
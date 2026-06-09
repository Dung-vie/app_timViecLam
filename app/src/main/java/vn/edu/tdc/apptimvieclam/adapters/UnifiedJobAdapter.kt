package vn.edu.tdc.apptimvieclam.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import vn.edu.tdc.apptimvieclam.databinding.ListJobLayoutBinding
import vn.edu.tdc.apptimvieclam.models.UnifiedJob


class UnifiedJobAdapter(
    private var list: ArrayList<UnifiedJob>,
    private val onItemClick: (UnifiedJob) -> Unit
) : RecyclerView.Adapter<UnifiedJobAdapter.ViewHolder>() {


    inner class ViewHolder(val binding: ListJobLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(ListJobLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))


    override fun getItemCount() = list.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val job = list[position]
        with(holder.binding) {
            txtTitle.text = job.title
            txtCompany.text = job.companyName
            txtLocation.text = job.location
            txtType.text = job.jobType
            txtPublish.text = if (job.source == "firebase") "Mới đăng" else ""


            Glide.with(holder.itemView.context)
                .load(job.logoUrl)
                .placeholder(vn.edu.tdc.apptimvieclam.R.drawable.ic_company_placeholder)
                .into(logo)


            holder.itemView.setOnClickListener { onItemClick(job) }
        }
    }


    fun updateList(newList: ArrayList<UnifiedJob>) {
        list = newList
        notifyDataSetChanged()
    }
}
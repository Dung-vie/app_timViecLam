package vn.edu.tdc.apptimvieclam.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import vn.edu.tdc.apptimvieclam.databinding.ListJobLayoutBinding
import vn.edu.tdc.apptimvieclam.models.Company

class JobAdapter(
    private var companies: ArrayList<Company>
) : RecyclerView.Adapter<JobAdapter.JobViewHolder>() {

    inner class JobViewHolder(
        val binding: ListJobLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): JobViewHolder {

        val binding = ListJobLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return JobViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: JobViewHolder,
        position: Int
    ) {

        val company = companies[position]

        holder.binding.txtTitle.text =
            company.title

        holder.binding.txtCompany.text =
            company.company.name

        holder.binding.txtLocation.text =
            company.location

        holder.binding.txtPublish.text =
            company.publish

        if (company.types.isNotEmpty()) {
            holder.binding.txtType.text =
                company.types[0].nameType
        }

        Glide.with(holder.itemView.context)
            .load(company.company.image)
            .into(holder.binding.logo)
    }

    override fun getItemCount(): Int {
        return companies.size
    }

    fun updateList(newList: ArrayList<Company>) {
        companies = newList
        notifyDataSetChanged()
    }
}
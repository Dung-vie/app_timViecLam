package vn.edu.tdc.apptimvieclam.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import vn.edu.tdc.apptimvieclam.R
import vn.edu.tdc.apptimvieclam.databinding.ItemManageJobBinding
import vn.edu.tdc.apptimvieclam.models.Job

class JobAdapter(
    private var jobs: ArrayList<Job>,
    private val onEdit: (Job) -> Unit,
    private val onDelete: (Job) -> Unit,
    private val onItemClick: (Job) -> Unit
) : RecyclerView.Adapter<JobAdapter.JobViewHolder>() {

    inner class JobViewHolder(
        val binding: ItemManageJobBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val binding = ItemManageJobBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return JobViewHolder(binding)
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {

        val job = jobs[position]

        holder.binding.apply {

            // TEXT
            tvJobTitle.text = job.title
            tvCompanyName.text = job.companyName
            tvJobLocation.text = job.location
            tvJobSalary.text = job.salary
            tvJobType.text = job.jobType

            // IMAGE
            if (!job.companyLogo.isNullOrEmpty()) {
                Glide.with(root.context)
                    .load(job.companyLogo)
                    .placeholder(R.drawable.ic_company_placeholder)
                    .into(ivCompanyLogo)
            } else {
                ivCompanyLogo.setImageResource(R.drawable.ic_company_placeholder)
            }

            // CLICK ITEM
            root.setOnClickListener {
                onItemClick(job)
            }

            // EDIT
            btnEdit.setOnClickListener {
                onEdit(job)
            }

            // DELETE
            btnDelete.setOnClickListener {
                onDelete(job)
            }
        }
    }

    override fun getItemCount(): Int = jobs.size

    fun updateList(newList: ArrayList<Job>) {
        jobs.clear()
        jobs.addAll(newList)
        notifyDataSetChanged()
    }
}
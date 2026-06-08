package vn.edu.tdc.apptimvieclam.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.edu.tdc.apptimvieclam.databinding.ItemManageJobBinding
import vn.edu.tdc.apptimvieclam.models.Job

class ManageJobAdapter(
    private val list: MutableList<Job>,
    private val onEdit: (Job) -> Unit,
    private val onDelete: (Job) -> Unit
) : RecyclerView.Adapter<ManageJobAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemManageJobBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(ItemManageJobBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val job = list[position]
        with(holder.binding) {
            tvJobTitle.text = job.title
            tvCompanyName.text = job.companyName
            tvJobLocation.text = job.location
            tvJobSalary.text = if (job.salary.isNotEmpty()) job.salary else "Thỏa thuận"
            tvJobType.text = job.jobType
            btnEdit.setOnClickListener { onEdit(job) }
            btnDelete.setOnClickListener { onDelete(job) }
        }
    }
}
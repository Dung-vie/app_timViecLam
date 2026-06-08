package vn.edu.tdc.apptimvieclam.admin.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.edu.tdc.apptimvieclam.databinding.ItemApplicantLayoutBinding
import vn.edu.tdc.apptimvieclam.models.Applications

class ApplicantAdapter(
    private val list: MutableList<Applications>,
    private val onAccept: (Applications) -> Unit,
    private val onReject: (Applications) -> Unit
) : RecyclerView.Adapter<ApplicantAdapter.ViewHolder>() {

    inner class ViewHolder(
        val binding: ItemApplicantLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        return ViewHolder(
            ItemApplicantLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {

        val item = list[position]

        holder.binding.tvApplicantName.text =
            item.applicantName

        holder.binding.tvEmail.text =
            item.email

        holder.binding.tvJobApplied.text =
            item.jobTitle

        holder.binding.tvApplyDate.text =
            item.applyDate

        holder.binding.tvStatus.text =
            item.status

        holder.binding.btnAccept.setOnClickListener {
            onAccept(item)
        }

        holder.binding.btnReject.setOnClickListener {
            onReject(item)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}
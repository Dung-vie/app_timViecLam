package vn.edu.tdc.apptimvieclam.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.edu.tdc.apptimvieclam.databinding.ItemAppliedJobBinding
import vn.edu.tdc.apptimvieclam.models.Applications

class AppliedJobAdapter(private var appList: List<Applications>) :
    RecyclerView.Adapter<AppliedJobAdapter.AppliedJobViewHolder>() {

    inner class AppliedJobViewHolder(val binding: ItemAppliedJobBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppliedJobViewHolder {
        val binding = ItemAppliedJobBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return AppliedJobViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AppliedJobViewHolder, position: Int) {
        val app = appList[position]

        with(holder.binding) {
            txtTitle.text = app.jobTitle
            txtCompany.text = app.company
            txtApplyDate.text = "Ngày nộp: ${app.applyDate}"
            txtStatus.text = app.status

            when (app.status) {
                "Pending" -> txtStatus.setTextColor(Color.parseColor("#FFA500"))
                "Accepted" -> txtStatus.setTextColor(Color.parseColor("#008000"))
                "Rejected" -> txtStatus.setTextColor(Color.parseColor("#FF0000"))
            }
        }
    }

    override fun getItemCount(): Int = appList.size

    fun updateList(newList: List<Applications>) {
        appList = newList
        notifyDataSetChanged()
    }
}
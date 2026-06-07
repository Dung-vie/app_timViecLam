package vn.edu.tdc.apptimvieclam.adapters

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import vn.edu.tdc.apptimvieclam.databinding.ItemNotificationLayoutBinding
import vn.edu.tdc.apptimvieclam.models.Notification

class NotificationAdapter(
    private val context: Activity,
    private val list: ArrayList<Notification>
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

        val binding: ItemNotificationLayoutBinding

        if (convertView == null) {

            binding =
                ItemNotificationLayoutBinding.inflate(
                    context.layoutInflater,
                    parent,
                    false
                )

        } else {

            binding =
                ItemNotificationLayoutBinding.bind(
                    convertView
                )
        }

        val notification = list[position]

        binding.txtNotificationTitle.text = notification.title

        binding.txtNotificationMessage.text =
            notification.message

        binding.txtNotificationTime.text =
            notification.createdAt.toString()

        if (notification.isRead) {

            binding.viewUnread.visibility =
                View.GONE

        } else {

            binding.viewUnread.visibility =
                View.VISIBLE
        }

        return binding.root
    }
}
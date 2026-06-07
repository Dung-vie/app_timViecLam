package vn.edu.tdc.apptimvieclam.admin.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import vn.edu.tdc.apptimvieclam.databinding.AdminDashboardLayoutBinding

class AdminDashboardActivity : AppCompatActivity() {

    private lateinit var binding: AdminDashboardLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AdminDashboardLayoutBinding.inflate(layoutInflater)

        setContentView(binding.root)
        playApproved()

    }

    private fun playApproved() {
        binding.layoutRecruiterManager.setOnClickListener {
            val intent = Intent(this,ManageRecruiterActivity::class.java)
            startActivity(intent)
        }

        binding.layoutJobManager.setOnClickListener {
            val intent = Intent(this,ManageJobsActivity::class.java)
            startActivity(intent)
        }
    }
}
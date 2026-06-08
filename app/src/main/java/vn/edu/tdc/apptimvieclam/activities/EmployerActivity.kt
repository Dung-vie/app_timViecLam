package vn.edu.tdc.apptimvieclam.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import vn.edu.tdc.apptimvieclam.admin.activities.ApplicantListActivity
import vn.edu.tdc.apptimvieclam.databinding.EmployerDashboardLayoutBinding

class EmployerActivity : AppCompatActivity() {

    private lateinit var binding: EmployerDashboardLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = EmployerDashboardLayoutBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.layoutApplicants.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    ApplicantListActivity::class.java
                )
            )
        }
    }
}
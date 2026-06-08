package vn.edu.tdc.apptimvieclam.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import vn.edu.tdc.apptimvieclam.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadUser()
        setupClicks()
    }

    private fun loadUser() {

        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        FirebaseDatabase.getInstance()
            .getReference("users")
            .child(uid)
            .get()
            .addOnSuccessListener { snapshot ->

                val name = snapshot.child("name").value?.toString()
                val email = snapshot.child("email").value?.toString()

                binding.tvUserName.text = name ?: "No name"
                binding.tvUserLocation.text = email ?: "No email"
            }
            .addOnFailureListener {
                Toast.makeText(this, "Load user failed", Toast.LENGTH_SHORT).show()
            }
    }

    private fun setupClicks() {

        // EDIT PROFILE
        binding.llEditProfile.setOnClickListener {
            Toast.makeText(this, "Edit Profile clicked", Toast.LENGTH_SHORT).show()

            // TODO: mở màn edit profile
        }

        // SETTINGS ICON
        binding.ivSettings.setOnClickListener {
            Toast.makeText(this, "Settings clicked", Toast.LENGTH_SHORT).show()
        }

        // SHARE ICON
        binding.ivShare.setOnClickListener {
            Toast.makeText(this, "Share clicked", Toast.LENGTH_SHORT).show()
        }

        // MENU ITEMS
        binding.llAboutMe.setOnClickListener {
            Toast.makeText(this, "About me", Toast.LENGTH_SHORT).show()
        }

        binding.llWorkExperience.setOnClickListener {
            Toast.makeText(this, "Work experience", Toast.LENGTH_SHORT).show()
        }

        binding.llEducation.setOnClickListener {
            Toast.makeText(this, "Education", Toast.LENGTH_SHORT).show()
        }

        binding.llSkill.setOnClickListener {
            Toast.makeText(this, "Skill", Toast.LENGTH_SHORT).show()
        }

        binding.llLanguage.setOnClickListener {
            Toast.makeText(this, "Language", Toast.LENGTH_SHORT).show()
        }

        binding.llAppreciation.setOnClickListener {
            Toast.makeText(this, "Appreciation", Toast.LENGTH_SHORT).show()
        }

        binding.llResume.setOnClickListener {
            Toast.makeText(this, "Resume", Toast.LENGTH_SHORT).show()
        }

    }

}
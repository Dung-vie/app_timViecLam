package vn.edu.tdc.apptimvieclam.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import vn.edu.tdc.apptimvieclam.R
import vn.edu.tdc.apptimvieclam.databinding.ProfileLayoutBinding

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ProfileLayoutBinding

    private val editProfileLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            loadUser()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ProfileLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadUser()
        setupClicks()
    }

    private fun loadUser() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid.isNullOrEmpty()) {
            Toast.makeText(this, R.string.error_not_logged_in, Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        FirebaseDatabase.getInstance()
            .getReference("users")
            .child(uid)
            .get()
            .addOnSuccessListener { snapshot ->
                bindUser(snapshot)
            }
            .addOnFailureListener {
                Toast.makeText(this, R.string.profile_load_failed, Toast.LENGTH_SHORT).show()
            }
    }

    private fun bindUser(snapshot: DataSnapshot) {
        val name = snapshot.child("name").value?.toString()
        val email = snapshot.child("email").value?.toString()
            ?: FirebaseAuth.getInstance().currentUser?.email
        val address = readAddress(snapshot)

        binding.tvUserName.text = name?.takeIf { it.isNotBlank() } ?: getString(R.string.hint_full_name)

        binding.tvUserLocation.text = when {
            !address.isNullOrBlank() -> address
            !email.isNullOrBlank() -> email
            else -> getString(R.string.hint_address)
        }

        bindSectionContent(binding.tvAboutContent, snapshot.child("about").value?.toString())
        bindSectionContent(binding.tvExperienceContent, snapshot.child("experience").value?.toString())
        bindSectionContent(binding.tvEducationContent, snapshot.child("education").value?.toString())
        bindSectionContent(binding.tvSkillsContent, snapshot.child("skills").value?.toString())
        bindSectionContent(binding.tvLanguagesContent, snapshot.child("languages").value?.toString())
        bindSectionContent(binding.tvAppreciationContent, snapshot.child("appreciation").value?.toString())
        bindSectionContent(binding.tvResumeContent, snapshot.child("resume").value?.toString())
    }

    private fun bindSectionContent(textView: TextView, value: String?) {
        val content = value?.trim()
        if (content.isNullOrEmpty()) {
            textView.visibility = View.GONE
            return
        }

        textView.text = content
        textView.setTextColor(ContextCompat.getColor(this, R.color.profile_content_text))
        textView.visibility = View.VISIBLE
    }

    private fun readAddress(snapshot: DataSnapshot): String? {
        val address = snapshot.child("address").value?.toString()
        if (!address.isNullOrBlank()) return address

        val location = snapshot.child("location").value?.toString()
        return location?.takeIf { it.isNotBlank() }
    }

    private fun setupClicks() {
        binding.llEditProfile.setOnClickListener {
            editProfileLauncher.launch(Intent(this, EditProfileActivity::class.java))
        }
    }
}

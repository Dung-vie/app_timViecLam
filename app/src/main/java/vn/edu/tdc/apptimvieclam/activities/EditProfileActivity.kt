package vn.edu.tdc.apptimvieclam.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import vn.edu.tdc.apptimvieclam.R
import vn.edu.tdc.apptimvieclam.databinding.EditProfileLayoutBinding

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: EditProfileLayoutBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private var uid = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = EditProfileLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.orEmpty()

        if (uid.isEmpty()) {
            Toast.makeText(this, R.string.error_not_logged_in, Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // bat su kien cho nut back
        binding.ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        database = FirebaseDatabase.getInstance()
            .getReference("users")
            .child(uid)

        setupClicks()
        loadProfile()
    }

    private fun setupClicks() {
        binding.btnCancel.setOnClickListener { finish() }
        binding.btnSave.setOnClickListener { updateProfile() }
    }

    private fun loadProfile() {
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.edtFullName.setText(snapshot.child("name").value?.toString().orEmpty())
                binding.edtEmail.setText(
                    snapshot.child("email").value?.toString()
                        ?: auth.currentUser?.email.orEmpty()
                )
                binding.edtPhone.setText(snapshot.child("phone").value?.toString().orEmpty())
                binding.edtAddress.setText(readAddress(snapshot))
                binding.edtAbout.setText(snapshot.child("about").value?.toString().orEmpty())
                binding.edtExperience.setText(snapshot.child("experience").value?.toString().orEmpty())
                binding.edtEducation.setText(snapshot.child("education").value?.toString().orEmpty())
                binding.edtSkills.setText(snapshot.child("skills").value?.toString().orEmpty())
                binding.edtLanguages.setText(snapshot.child("languages").value?.toString().orEmpty())
                binding.edtAppreciation.setText(snapshot.child("appreciation").value?.toString().orEmpty())
                binding.edtResume.setText(snapshot.child("resume").value?.toString().orEmpty())
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@EditProfileActivity,
                    R.string.profile_load_failed,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun readAddress(snapshot: DataSnapshot): String {
        val address = snapshot.child("address").value?.toString()
        if (!address.isNullOrBlank()) return address

        return snapshot.child("location").value?.toString().orEmpty()
    }

    private fun updateProfile() {
        val fullName = binding.edtFullName.text.toString().trim()
        val phone = binding.edtPhone.text.toString().trim()

        if (fullName.isEmpty()) {
            binding.edtFullName.error = getString(R.string.error_name_required)
            binding.edtFullName.requestFocus()
            return
        }

        if (phone.isNotEmpty() && !isValidPhone(phone)) {
            binding.edtPhone.error = getString(R.string.error_phone_invalid)
            binding.edtPhone.requestFocus()
            return
        }

        val updates = hashMapOf<String, Any>(
            "name" to fullName,
            "phone" to phone,
            "address" to binding.edtAddress.text.toString().trim(),
            "about" to binding.edtAbout.text.toString().trim(),
            "experience" to binding.edtExperience.text.toString().trim(),
            "education" to binding.edtEducation.text.toString().trim(),
            "skills" to binding.edtSkills.text.toString().trim(),
            "languages" to binding.edtLanguages.text.toString().trim(),
            "appreciation" to binding.edtAppreciation.text.toString().trim(),
            "resume" to binding.edtResume.text.toString().trim()
        )

        database.updateChildren(updates)
            .addOnSuccessListener {
                Toast.makeText(
                    this,
                    R.string.profile_updated_success,
                    Toast.LENGTH_SHORT
                ).show()
                setResult(RESULT_OK)
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(
                    this,
                    R.string.profile_update_failed,
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun isValidPhone(phone: String): Boolean {
        val digitsOnly = phone.replace(Regex("[^0-9]"), "")
        return digitsOnly.length in 9..15
    }
}

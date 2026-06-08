package vn.edu.tdc.apptimvieclam.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import vn.edu.tdc.apptimvieclam.R
import vn.edu.tdc.apptimvieclam.databinding.SavedLayoutBinding

class SavedActivity : AppCompatActivity() {
    private lateinit var binding: SavedLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SavedLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playMenu()
        loadAddRecuiter()

    }

    private fun playMenu() {
        binding.bottomMenu.menuSaved.setBackgroundResource(
            R.drawable.bg_menu_selected
        )

        binding.bottomMenu.menuSearch.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)

            finish()
        }

        binding.bottomMenu.menuHome.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)

            finish()
        }

        binding.bottomMenu.menuSetting.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)

            finish()
        }
    }

    private fun loadAddRecuiter() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        FirebaseDatabase.getInstance()
            .reference
            .child("users")
            .child(uid)
            .child("role")
            .get()
            .addOnSuccessListener { snapshot ->

                val role = snapshot.getValue(String::class.java)

                binding.bottomMenu.menuAdd.visibility =
                    if (role.equals("EMPLOYER", true) ||
                        role.equals("ADMIN", true)
                    ) {
                        View.VISIBLE
                    } else {
                        View.GONE
                    }
            }
    }

}
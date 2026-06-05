package vn.edu.tdc.apptimvieclam.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import vn.edu.tdc.apptimvieclam.R
import vn.edu.tdc.apptimvieclam.databinding.SettingLayoutBinding

class SettingActivity : AppCompatActivity() {

    private lateinit var binding: SettingLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = SettingLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomMenu.menuSetting.setBackgroundResource(
            R.drawable.bg_menu_selected
        )

        binding.bottomMenu.menuSearch.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
            finish()
        }

        binding.bottomMenu.menuSaved.setOnClickListener {
            startActivity(Intent(this, SavedActivity::class.java))
            finish()
        }

        binding.bottomMenu.menuHome.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }

        // Đăng xuất
        binding.btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()

            val intent = Intent(this, LoginActivity::class.java)
            intent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)
            finish()
        }
    }
}
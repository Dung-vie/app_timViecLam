package vn.edu.tdc.apptimvieclam.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import vn.edu.tdc.apptimvieclam.R
import vn.edu.tdc.apptimvieclam.databinding.SettingLayoutBinding

class SettingActivity : AppCompatActivity() {
    private lateinit var binding: SettingLayoutBinding
    private lateinit var btnUpdatePassword: Button
    private lateinit var btnLogout: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SettingLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playMenu()


        btnUpdatePassword = findViewById(R.id.btnUpdatePassword)
        btnLogout = findViewById(R.id.btnLogout)

        // Chuyển sang màn hình đổi mật khẩu
        btnUpdatePassword.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    UpdatePasswordActivity::class.java
                )
            )
        }

        // Đăng xuất
        btnLogout.setOnClickListener {

            FirebaseAuth.getInstance().signOut()

            startActivity(
                Intent(
                    this,
                    LoginActivity::class.java
                )
            )

            finish()
        }
    }
companion object {

}
    private fun playMenu() {
        binding.bottomMenu.menuSetting.setBackgroundResource(
            R.drawable.bg_menu_selected
        )

        binding.bottomMenu.menuSearch.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)

            finish()
        }

        binding.bottomMenu.menuSaved.setOnClickListener {
            val intent = Intent(this, SavedActivity::class.java)
            startActivity(intent)

            finish()
        }

        binding.bottomMenu.menuHome.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)

            finish()
        }

    }
}
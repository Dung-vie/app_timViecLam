package vn.edu.tdc.apptimvieclam.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import vn.edu.tdc.apptimvieclam.R

class SettingActivity : AppCompatActivity() {

    private lateinit var btnUpdatePassword: Button
    private lateinit var btnLogout: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.setting_layout)

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
}
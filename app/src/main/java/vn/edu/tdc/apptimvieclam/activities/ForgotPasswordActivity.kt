package vn.edu.tdc.apptimvieclam.activities

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import vn.edu.tdc.apptimvieclam.R

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var edtEmail: EditText
    private lateinit var btnResetPassword: Button
    private lateinit var btnBackLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Layout Forgot Password
        setContentView(R.layout.forgot_password_layout)

        // Firebase
        auth = FirebaseAuth.getInstance()

        // Mapping ViewA
        edtEmail = findViewById(R.id.edtEmail)
        btnResetPassword = findViewById(R.id.btnReset)
        btnBackLogin = findViewById(R.id.btnBackLogin)

        // Reset Password
        btnResetPassword.setOnClickListener {

            val email = edtEmail.text.toString().trim()

            // Validate
            if (email.isEmpty()) {

                Toast.makeText(
                    this,
                    "Vui lòng nhập email",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

                Toast.makeText(
                    this,
                    "Email không hợp lệ",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            // Send reset email
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->

                    if (task.isSuccessful) {

                        Toast.makeText(
                            this,
                            "Đã gửi email đặt lại mật khẩu",
                            Toast.LENGTH_LONG
                        ).show()

                    } else {

                        Toast.makeText(
                            this,
                            "Không thể gửi email reset",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }

        // Back Login
        btnBackLogin.setOnClickListener {

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

            overridePendingTransition(
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )

            finish()
        }
    }
}
package vn.edu.tdc.apptimvieclam.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import vn.edu.tdc.apptimvieclam.databinding.LoginLayoutBinding

class LoginActivity : AppCompatActivity() {

    // View Binding
    private lateinit var binding: LoginLayoutBinding

    // Firebase
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Khởi tạo Binding
        binding = LoginLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Firebase
        auth = FirebaseAuth.getInstance()

        // =========================
        // LOGIN FIREBASE
        // =========================
        binding.btnLogin.setOnClickListener {

            val email = binding.edtEmail.text.toString().trim()
            val password = binding.edtPassword.text.toString().trim()

            // Validate Email
            if (email.isEmpty()) {

                binding.edtEmail.error = "Vui lòng nhập email"
                binding.edtEmail.requestFocus()
                return@setOnClickListener
            }

            // Validate Password
            if (password.isEmpty()) {

                binding.edtPassword.error = "Vui lòng nhập mật khẩu"
                binding.edtPassword.requestFocus()
                return@setOnClickListener
            }

            // Firebase Login
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->

                    if (task.isSuccessful) {

                        Toast.makeText(
                            this,
                            "Đăng nhập thành công",
                            Toast.LENGTH_SHORT
                        ).show()

                        // Chuyển sang HomeActivity
                        // startActivity(Intent(this, HomeActivity::class.java))
                        // finish()

                    } else {

                        Toast.makeText(
                            this,
                            "Sai email hoặc mật khẩu",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }

        // =========================
        // SIGN UP
        // =========================
        binding.txtSignUp.setOnClickListener {

            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)

            overridePendingTransition(
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )
        }

        // =========================
        // FORGOT PASSWORD
        // =========================
        binding.txtForgotPassword.setOnClickListener {

            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)

            overridePendingTransition(
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )
        }
    }
}
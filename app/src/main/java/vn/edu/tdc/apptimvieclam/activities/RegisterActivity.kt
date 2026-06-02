package vn.edu.tdc.apptimvieclam.activities

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import vn.edu.tdc.apptimvieclam.databinding.RegisterLayoutBinding

class RegisterActivity : AppCompatActivity() {

    // View Binding
    private lateinit var binding: RegisterLayoutBinding

    // Firebase
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Binding
        binding = RegisterLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Firebase Auth
        auth = FirebaseAuth.getInstance()

        // =========================
        // ĐĂNG KÝ FIREBASE
        // =========================
        binding.btnRegister.setOnClickListener {

            val email = binding.edtEmail.text.toString().trim()
            val password = binding.edtPassword.text.toString().trim()
            val confirmPassword =
                binding.edtConfirmPassword.text.toString().trim()

            // Validate Email
            if (email.isEmpty()) {

                binding.edtEmail.error = "Vui lòng nhập email"
                binding.edtEmail.requestFocus()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

                binding.edtEmail.error = "Email không hợp lệ"
                binding.edtEmail.requestFocus()
                return@setOnClickListener
            }

            // Validate Password
            if (password.isEmpty()) {

                binding.edtPassword.error = "Vui lòng nhập mật khẩu"
                binding.edtPassword.requestFocus()
                return@setOnClickListener
            }

            if (password.length < 6) {

                binding.edtPassword.error =
                    "Mật khẩu tối thiểu 6 ký tự"

                binding.edtPassword.requestFocus()
                return@setOnClickListener
            }

            // Confirm Password
            if (confirmPassword.isEmpty()) {

                binding.edtConfirmPassword.error =
                    "Vui lòng xác nhận mật khẩu"

                binding.edtConfirmPassword.requestFocus()
                return@setOnClickListener
            }

            if (password != confirmPassword) {

                binding.edtConfirmPassword.error =
                    "Mật khẩu không khớp"

                binding.edtConfirmPassword.requestFocus()
                return@setOnClickListener
            }

            // Firebase Register
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->

                    if (task.isSuccessful) {

                        Toast.makeText(
                            this,
                            "Đăng ký thành công",
                            Toast.LENGTH_SHORT
                        ).show()

                        // Chuyển sang Login
                        val intent = Intent(
                            this,
                            LoginActivity::class.java
                        )

                        startActivity(intent)

                        overridePendingTransition(
                            android.R.anim.fade_in,
                            android.R.anim.fade_out
                        )

                        finish()

                    } else {

                        Toast.makeText(
                            this,
                            "Đăng ký thất bại: ${task.exception?.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }

        // =========================
        // CHUYỂN SANG LOGIN
        // =========================
        binding.txtSignIn.setOnClickListener {

            val intent = Intent(
                this,
                LoginActivity::class.java
            )

            startActivity(intent)

            overridePendingTransition(
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )

            finish()
        }

        // =========================
        // FORGOT PASSWORD
        // =========================
        binding.txtForgotPassword.setOnClickListener {

            val intent = Intent(
                this,
                ForgotPasswordActivity::class.java
            )

            startActivity(intent)

            overridePendingTransition(
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )
        }
    }
}
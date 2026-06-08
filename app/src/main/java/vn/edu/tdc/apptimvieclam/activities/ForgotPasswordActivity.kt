package vn.edu.tdc.apptimvieclam.activities

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import vn.edu.tdc.apptimvieclam.databinding.ForgotPasswordLayoutBinding

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ForgotPasswordLayoutBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ForgotPasswordLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        // Gửi email reset password
        binding.btnReset.setOnClickListener {

            val email = binding.edtEmail.text.toString().trim()

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

            sendResetEmail(email)
        }

        // Quay về Login
        binding.btnBackLogin.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    LoginActivity::class.java
                )
            )

            finish()
        }
    }

    private fun sendResetEmail(email: String) {

        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    AlertDialog.Builder(this)
                        .setTitle("Khôi phục mật khẩu")
                        .setMessage(
                            "Liên kết đặt lại mật khẩu đã được gửi đến:\n\n$email\n\nVui lòng kiểm tra Gmail của bạn."
                        )
                        .setCancelable(false)
                        .setPositiveButton("OK") { _, _ ->

                            startActivity(
                                Intent(
                                    this,
                                    LoginActivity::class.java
                                )
                            )

                            finish()
                        }
                        .show()

                } else {

                    Toast.makeText(
                        this,
                        task.exception?.message
                            ?: "Gửi email thất bại",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }
}
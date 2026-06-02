package vn.edu.tdc.apptimvieclam.activities

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import vn.edu.tdc.apptimvieclam.databinding.ForgotPasswordLayoutBinding

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ForgotPasswordLayoutBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // View Binding
        binding = ForgotPasswordLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Firebase
        auth = FirebaseAuth.getInstance()

        // =========================
        // RESET PASSWORD
        // =========================
        binding.btnReset.setOnClickListener {

            val email = binding.edtEmail.text.toString().trim()

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

            // Firebase Send Reset Email
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

        // =========================
        // BACK LOGIN
        // =========================
        binding.btnBackLogin.setOnClickListener {

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
package vn.edu.tdc.apptimvieclam.activities

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import vn.edu.tdc.apptimvieclam.R

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var edtConfirmPassword: EditText
    private lateinit var btnRegister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_layout)

        // Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Ánh xạ view
        edtEmail = findViewById(R.id.edtEmail)
        edtPassword = findViewById(R.id.edtPassword)
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword)
        btnRegister = findViewById(R.id.btnRegister)

        // =========================
        // Đăng ký Firebase
        // =========================
        btnRegister.setOnClickListener {

            val email = edtEmail.text.toString().trim()
            val password = edtPassword.text.toString().trim()
            val confirmPassword = edtConfirmPassword.text.toString().trim()

            // Validate Email
            if (email.isEmpty()) {
                edtEmail.error = "Vui lòng nhập email"
                edtEmail.requestFocus()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                edtEmail.error = "Email không hợp lệ"
                edtEmail.requestFocus()
                return@setOnClickListener
            }

            // Validate Password
            if (password.isEmpty()) {
                edtPassword.error = "Vui lòng nhập mật khẩu"
                edtPassword.requestFocus()
                return@setOnClickListener
            }

            if (password.length < 6) {
                edtPassword.error = "Mật khẩu tối thiểu 6 ký tự"
                edtPassword.requestFocus()
                return@setOnClickListener
            }

            // Confirm Password
            if (confirmPassword.isEmpty()) {
                edtConfirmPassword.error = "Vui lòng xác nhận mật khẩu"
                edtConfirmPassword.requestFocus()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                edtConfirmPassword.error = "Mật khẩu không khớp"
                edtConfirmPassword.requestFocus()
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
                        val intent = Intent(this, LoginActivity::class.java)
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
        // Chuyển sang Login
        // =========================
        val txtSignIn = findViewById<TextView>(R.id.txtSignIn)

        txtSignIn.setOnClickListener {

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

            overridePendingTransition(
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )

            finish()
        }

        // =========================
        // Forgot Password
        // =========================
        val txtForgotPassword = findViewById<TextView>(R.id.txtForgotPassword)

        txtForgotPassword.setOnClickListener {

            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)

            overridePendingTransition(
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )
        }
    }
}
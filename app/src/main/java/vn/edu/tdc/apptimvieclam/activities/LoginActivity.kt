package vn.edu.tdc.apptimvieclam.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import vn.edu.tdc.apptimvieclam.R

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText

    private lateinit var btnLogin: Button
    private lateinit var txtSignUp: TextView
    private lateinit var txtForgotPassword: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_layout)

        // Firebase
        auth = FirebaseAuth.getInstance()

        // Ánh xạ View
        edtEmail = findViewById(R.id.edtEmail)
        edtPassword = findViewById(R.id.edtPassword)

        btnLogin = findViewById(R.id.btnLogin)
        txtSignUp = findViewById(R.id.txtSignUp)
        txtForgotPassword = findViewById(R.id.txtForgotPassword)

        // =========================
        // LOGIN FIREBASE
        // =========================
        btnLogin.setOnClickListener {

            val email = edtEmail.text.toString().trim()
            val password = edtPassword.text.toString().trim()

            // Validate
            if (email.isEmpty()) {

                edtEmail.error = "Vui lòng nhập email"
                edtEmail.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()) {

                edtPassword.error = "Vui lòng nhập mật khẩu"
                edtPassword.requestFocus()
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

                        // TODO: Chuyển sang HomeActivity
                        // startActivity(Intent(this, HomeActivity::class.java))

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
        txtSignUp.setOnClickListener {

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
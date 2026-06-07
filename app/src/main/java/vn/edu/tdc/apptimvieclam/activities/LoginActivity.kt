package vn.edu.tdc.apptimvieclam.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.database
import vn.edu.tdc.apptimvieclam.admin.activities.AdminDashboardActivity
import vn.edu.tdc.apptimvieclam.databinding.LoginLayoutBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: LoginLayoutBinding

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Khởi tạo Binding
        binding = LoginLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Firebase
        auth = FirebaseAuth.getInstance()
        // Nếu đã đăng nhập thì vào Home luôn
        if (auth.currentUser != null) {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
            return
        }
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
                        val uid = auth.currentUser!!.uid

                        Firebase.database.getReference("users")
                            .child(uid)
                            .get()
                            .addOnSuccessListener { snapshot ->

                                val role = snapshot.child("role").getValue(String::class.java)

                                val status = snapshot.child("status").getValue(String::class.java)

                                if (role == "ADMIN") {
                                    val intent = Intent(this, AdminDashboardActivity::class.java)
                                    startActivity(intent)

                                    finish()
                                } else if (role == "USER") {
                                    val intent = Intent(this, HomeActivity::class.java)
                                    startActivity(intent)

                                    finish()

                                } else if (role == "EMPLOYER") {
                                    if (status == "ACTIVE") {
                                        val intent = Intent(this, HomeActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    } else {
                                        Toast.makeText(
                                            this,
                                            "Tài khoản đang chờ duyệt",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        auth.signOut()
                                    }
                                }
                            }

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
package vn.edu.tdc.apptimvieclam.activities

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.database
import vn.edu.tdc.apptimvieclam.databinding.RegisterLayoutBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: RegisterLayoutBinding

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = RegisterLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Firebase Auth
        auth = FirebaseAuth.getInstance()

        // =========================
        // ĐĂNG KÝ FIREBASE
        // =========================
        binding.btnRegister.setOnClickListener {
            val name = binding.edtFullName.text.toString().trim()
            val email = binding.edtEmail.text.toString().trim()
            val password = binding.edtPassword.text.toString().trim()
            val confirmPassword = binding.edtConfirmPassword.text.toString().trim()
            val selectedRole = binding.rgRole.checkedRadioButtonId
            var role = ""
            if (selectedRole == binding.rbUser.id) {
                role = "USER"
            } else if (selectedRole == binding.rbEmployer.id) {
                role = "EMPLOYER"
            }

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

            //Check role
            if (role.isEmpty()) {

                Toast.makeText(
                    this,
                    "Vui lòng chọn vai trò",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            // Firebase Register
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->

                    if (task.isSuccessful) {
                        val uid = auth.currentUser!!.uid

                        val database = Firebase.database

                        val userData = hashMapOf(
                            "uid" to uid,
                            "name" to name,
                            "email" to email,
                            "role" to role,
                            "status" to if (role == "EMPLOYER")
                                "PENDING"
                            else
                                "ACTIVE"
                        )

                        database.getReference("users")
                            .child(uid)
                            .setValue(userData)
                            .addOnSuccessListener {

                                Toast.makeText(
                                    this,
                                    "Đăng ký thành công",
                                    Toast.LENGTH_SHORT
                                ).show()

                                val intent = Intent(this, LoginActivity::class.java)
                                startActivity(intent)

                                overridePendingTransition(
                                    android.R.anim.fade_in,
                                    android.R.anim.fade_out
                                )
                                finish()
                            }



                    } else {

                        Toast.makeText(
                            this,
                            "Đăng ký thất bại: ${task.exception?.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }

        // CHUYỂN SANG LOGIN
        binding.txtSignIn.setOnClickListener {

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
package vn.edu.tdc.apptimvieclam.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import vn.edu.tdc.apptimvieclam.databinding.UpdatePasswordLayoutBinding

class UpdatePasswordActivity : AppCompatActivity() {

    private lateinit var binding: UpdatePasswordLayoutBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = UpdatePasswordLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        // Quay lại
        binding.btnBack.setOnClickListener {
            finish()
        }

        // Cập nhật mật khẩu
        binding.btnUpdate.setOnClickListener {

            val oldPass = binding.edtOldPassword.text.toString().trim()
            val newPass = binding.edtNewPassword.text.toString().trim()
            val confirmPass = binding.edtConfirmPassword.text.toString().trim()

            if (oldPass.isEmpty()) {
                binding.edtOldPassword.error = "Vui lòng nhập mật khẩu cũ"
                binding.edtOldPassword.requestFocus()
                return@setOnClickListener
            }

            if (newPass.isEmpty()) {
                binding.edtNewPassword.error = "Vui lòng nhập mật khẩu mới"
                binding.edtNewPassword.requestFocus()
                return@setOnClickListener
            }

            if (newPass.length < 6) {
                binding.edtNewPassword.error = "Mật khẩu tối thiểu 6 ký tự"
                binding.edtNewPassword.requestFocus()
                return@setOnClickListener
            }

            if (confirmPass.isEmpty()) {
                binding.edtConfirmPassword.error = "Vui lòng xác nhận mật khẩu"
                binding.edtConfirmPassword.requestFocus()
                return@setOnClickListener
            }

            if (newPass != confirmPass) {
                binding.edtConfirmPassword.error = "Mật khẩu xác nhận không khớp"
                binding.edtConfirmPassword.requestFocus()
                return@setOnClickListener
            }

            val user = auth.currentUser

            if (user == null) {

                Toast.makeText(
                    this,
                    "Người dùng chưa đăng nhập",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            val email = user.email

            if (email.isNullOrEmpty()) {

                Toast.makeText(
                    this,
                    "Không tìm thấy email tài khoản",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            // Xác thực lại bằng mật khẩu cũ
            val credential =
                EmailAuthProvider.getCredential(
                    email,
                    oldPass
                )

            user.reauthenticate(credential)
                .addOnSuccessListener {

                    // Đổi mật khẩu Firebase
                    user.updatePassword(newPass)
                        .addOnSuccessListener {

                            Toast.makeText(
                                this,
                                "Đổi mật khẩu thành công",
                                Toast.LENGTH_LONG
                            ).show()

                            // Đăng xuất để đăng nhập lại
                            FirebaseAuth.getInstance().signOut()

                            startActivity(
                                Intent(
                                    this,
                                    LoginActivity::class.java
                                )
                            )

                            finishAffinity()
                        }
                        .addOnFailureListener { e ->

                            Toast.makeText(
                                this,
                                e.message,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                }
                .addOnFailureListener {

                    Toast.makeText(
                        this,
                        "Mật khẩu cũ không đúng",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }
}
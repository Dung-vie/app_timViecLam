package vn.edu.tdc.apptimvieclam.activities

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Patterns
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import vn.edu.tdc.apptimvieclam.databinding.ForgotPasswordLayoutBinding

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ForgotPasswordLayoutBinding
    private lateinit var auth: FirebaseAuth

    // OTP Demo
    private var generatedOtp = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ForgotPasswordLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        // =========================
        // RESET PASSWORD
        // =========================
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

            // Tạo OTP Demo
            generatedOtp = generateOtp()

            Toast.makeText(
                this,
                "OTP Demo: $generatedOtp",
                Toast.LENGTH_LONG
            ).show()

            showOtpDialog()
        }

        // =========================
        // BACK LOGIN
        // =========================
        binding.btnBackLogin.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    LoginActivity::class.java
                )
            )

            overridePendingTransition(
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )

            finish()
        }
    }

    /**
     * Sinh OTP ngẫu nhiên 6 số
     */
    private fun generateOtp(): String {
        return (100000..999999).random().toString()
    }

    /**
     * Dialog nhập OTP
     */
    private fun showOtpDialog() {

        val edtOtp = EditText(this)

        edtOtp.hint = "Nhập mã OTP"
        edtOtp.inputType = InputType.TYPE_CLASS_NUMBER

        AlertDialog.Builder(this)
            .setTitle("Xác thực OTP")
            .setMessage("Vui lòng nhập mã OTP vừa nhận")
            .setView(edtOtp)

            .setPositiveButton("Xác nhận") { _, _ ->

                val otpInput =
                    edtOtp.text.toString().trim()

                if (otpInput == generatedOtp) {

                    Toast.makeText(
                        this,
                        "OTP chính xác",
                        Toast.LENGTH_SHORT
                    ).show()

                    showResetPasswordDialog()

                } else {

                    Toast.makeText(
                        this,
                        "OTP không đúng",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            .setNegativeButton("Hủy", null)
            .show()
    }

    /**
     * Dialog đổi mật khẩu
     */
    private fun showResetPasswordDialog() {

        val layout = LinearLayout(this)

        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(50, 20, 50, 20)

        val edtNewPassword = EditText(this)
        edtNewPassword.hint = "Mật khẩu mới"
        edtNewPassword.inputType =
            InputType.TYPE_CLASS_TEXT or
                    InputType.TYPE_TEXT_VARIATION_PASSWORD

        val edtConfirmPassword = EditText(this)
        edtConfirmPassword.hint = "Xác nhận mật khẩu"
        edtConfirmPassword.inputType =
            InputType.TYPE_CLASS_TEXT or
                    InputType.TYPE_TEXT_VARIATION_PASSWORD

        layout.addView(edtNewPassword)
        layout.addView(edtConfirmPassword)

        AlertDialog.Builder(this)
            .setTitle("Đổi mật khẩu")
            .setView(layout)

            .setPositiveButton("Lưu") { _, _ ->

                val newPass =
                    edtNewPassword.text.toString().trim()

                val confirmPass =
                    edtConfirmPassword.text.toString().trim()

                if (newPass.isEmpty()) {

                    Toast.makeText(
                        this,
                        "Vui lòng nhập mật khẩu mới",
                        Toast.LENGTH_SHORT
                    ).show()

                    return@setPositiveButton
                }

                if (newPass.length < 6) {

                    Toast.makeText(
                        this,
                        "Mật khẩu tối thiểu 6 ký tự",
                        Toast.LENGTH_SHORT
                    ).show()

                    return@setPositiveButton
                }

                if (newPass != confirmPass) {

                    Toast.makeText(
                        this,
                        "Mật khẩu không khớp",
                        Toast.LENGTH_SHORT
                    ).show()

                    return@setPositiveButton
                }

                Toast.makeText(
                    this,
                    "Đổi mật khẩu thành công (Demo)",
                    Toast.LENGTH_LONG
                ).show()

                finish()
            }

            .setNegativeButton("Hủy", null)
            .show()
    }
}
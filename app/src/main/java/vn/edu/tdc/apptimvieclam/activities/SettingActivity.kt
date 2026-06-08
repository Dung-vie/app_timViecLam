package vn.edu.tdc.apptimvieclam.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import vn.edu.tdc.apptimvieclam.R
import vn.edu.tdc.apptimvieclam.databinding.SettingLayoutBinding

class SettingActivity : AppCompatActivity() {
    private lateinit var binding: SettingLayoutBinding
    private var name:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SettingLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadUserName()
        playMenu()
        loadRecuiterPost()
        loadAddRecuiter()

        // Chuyển sang màn hình đổi mật khẩu
        binding.txtUpdatePass.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    UpdatePasswordActivity::class.java
                )
            )
        }
        // Thông tin tài khoản
        binding.txtInfo.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        // Đăng xuất
        binding.btnLogout.setOnClickListener {

            FirebaseAuth.getInstance().signOut()

            startActivity(
                Intent(
                    this,
                    LoginActivity::class.java
                )
            )

            finish()
        }
    }

    private fun playMenu() {
        binding.bottomMenu.menuSetting.setBackgroundResource(
            R.drawable.bg_menu_selected
        )

        binding.bottomMenu.menuSearch.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)

            finish()
        }

        binding.bottomMenu.menuSaved.setOnClickListener {
            val intent = Intent(this, SavedActivity::class.java)
            startActivity(intent)

            finish()
        }

        binding.bottomMenu.menuHome.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)

            finish()
        }

        // Đăng xuất
        binding.btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()

            Toast.makeText(this, "Đăng xuất thành công", Toast.LENGTH_SHORT).show()
        }

        // Chuyển sang màn hình đổi mật khẩu
        binding.txtUpdatePass.setOnClickListener {
            val intent = Intent(this, UpdatePasswordActivity::class.java)
            startActivity(intent)
        }

    }

    private fun loadUserName() {
        val user = FirebaseAuth.getInstance().currentUser

        if (user != null) {
            Firebase.database.reference
                .child("users")
                .child(user.uid)
                .child("name")
                .get()
                .addOnSuccessListener { snapshot ->
                    name = snapshot.getValue(String::class.java) ?: "Người dùng"
                    binding.txtName.text = name
                }
                .addOnFailureListener {
                    binding.txtName.text = "Người dùng"
                }
        }
    }

    private fun loadRecuiterPost() {

        val uid = FirebaseAuth.getInstance().currentUser?.uid
            ?: return

        FirebaseDatabase.getInstance()
            .reference
            .child("users")
            .child(uid)
            .child("role")
            .get()
            .addOnSuccessListener { snapshot ->

                val role =
                    snapshot.getValue(String::class.java)

                binding.txtPost.visibility =
                    if (
                        role.equals("EMPLOYER", true) ||
                        role.equals("ADMIN", true)
                    )
                        View.VISIBLE
                    else
                        View.GONE
            }
    }

    private fun loadAddRecuiter() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        FirebaseDatabase.getInstance()
            .reference
            .child("users")
            .child(uid)
            .child("role")
            .get()
            .addOnSuccessListener { snapshot ->

                val role = snapshot.getValue(String::class.java)

                binding.bottomMenu.menuAdd.visibility =
                    if (role.equals("EMPLOYER", true) ||
                        role.equals("ADMIN", true)
                    ) {
                        View.VISIBLE
                    } else {
                        View.GONE
                    }
            }
    }
}
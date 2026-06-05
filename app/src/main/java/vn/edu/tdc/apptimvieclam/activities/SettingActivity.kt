package vn.edu.tdc.apptimvieclam.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import vn.edu.tdc.apptimvieclam.R
import vn.edu.tdc.apptimvieclam.databinding.SettingLayoutBinding

class SettingActivity : AppCompatActivity() {
    private lateinit var binding: SettingLayoutBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SettingLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

    }
}
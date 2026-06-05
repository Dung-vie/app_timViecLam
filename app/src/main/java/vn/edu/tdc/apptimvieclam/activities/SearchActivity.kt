package vn.edu.tdc.apptimvieclam.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import vn.edu.tdc.apptimvieclam.R
import vn.edu.tdc.apptimvieclam.databinding.HomeLayoutBinding
import vn.edu.tdc.apptimvieclam.databinding.SearchLayoutBinding

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: SearchLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SearchLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomMenu.menuSearch.setBackgroundResource(
            R.drawable.bg_menu_selected
        )

        binding.bottomMenu.menuHome.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)

            finish()
        }

        binding.bottomMenu.menuSaved.setOnClickListener {
            val intent = Intent(this, SavedActivity::class.java)
            startActivity(intent)

            finish()
        }

        binding.bottomMenu.menuSetting.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)

            finish()
        }

    }
}
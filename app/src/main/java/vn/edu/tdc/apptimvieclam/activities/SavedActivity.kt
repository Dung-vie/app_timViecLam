package vn.edu.tdc.apptimvieclam.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import vn.edu.tdc.apptimvieclam.R
import vn.edu.tdc.apptimvieclam.databinding.SavedLayoutBinding

class SavedActivity : AppCompatActivity() {
    private lateinit var binding: SavedLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SavedLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomMenu.menuSaved.setBackgroundResource(
            R.drawable.bg_menu_selected
        )

        binding.bottomMenu.menuSearch.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)

            finish()
        }

        binding.bottomMenu.menuHome.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
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
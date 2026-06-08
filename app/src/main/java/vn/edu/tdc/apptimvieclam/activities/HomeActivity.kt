package vn.edu.tdc.apptimvieclam.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import vn.edu.tdc.apptimvieclam.databinding.HomeLayoutBinding
import vn.edu.tdc.apptimvieclam.models.Company
import vn.edu.tdc.apptimvieclam.models.CompanyAPI
import vn.edu.tdc.apptimvieclam.models.CompanyList
import vn.edu.tdc.apptimvieclam.R
import vn.edu.tdc.apptimvieclam.adapters.MyListViewAdapter
import vn.edu.tdc.apptimvieclam.databinding.ItemCompanyLayoutBinding
import vn.edu.tdc.apptimvieclam.databinding.ItemFilterLayoutBinding
import vn.edu.tdc.apptimvieclam.models.Notification
import vn.edu.tdc.apptimvieclam.utils.NotificationHelper

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: HomeLayoutBinding
    private var name:String = ""
    private lateinit var companies: ArrayList<Company>
    private lateinit var adapter: MyListViewAdapter
    private lateinit var companyAPI: CompanyAPI
    private val REQ = 999

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HomeLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadUserName()

        companies = ArrayList<Company>()

        adapter = MyListViewAdapter(this, companies)
        binding.listJob.adapter = adapter

        loadQuickFilters()
        playMenu()
        loadAddRecuiter()
        getCompanies(companies)
        if (checkPermission(Manifest.permission.POST_NOTIFICATIONS)) {
            //Cho phep
            doSomething()
        }
        // Cho phép doSomething()
        else {
            requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), REQ)
        }
    }

    private fun playMenu() {
        binding.bottomMenu.menuHome.setBackgroundResource(
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

        binding.bottomMenu.menuSetting.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)

            finish()
        }

        binding.bottomMenu.menuAdd.setOnClickListener {
            startActivity(Intent(this, CreateJobActivity::class.java))
        }

        binding.imgNotify.setOnClickListener {
            val intent = Intent(this, NotificationActivity::class.java)
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
                    name = snapshot.getValue(String::class.java)
                        ?: "Người dùng"
                    binding.txtName.text = name
                }
                .addOnFailureListener {
                    binding.txtName.text = "Người dùng"
                }
        }
    }

    private fun loadQuickFilters() {
        val filters = resources.getStringArray(R.array.quick_filters)

        filters.forEach { filter ->
            val itemBinding = ItemFilterLayoutBinding.inflate(
                layoutInflater,
                binding.layoutQuickFilter,
                false
            )

            itemBinding.txtFilter.text = filter

            itemBinding.root.setOnClickListener {
                val intent = Intent(this, SearchActivity::class.java)
                intent.putExtra("FILTER", filter)

                startActivity(intent)
            }

            binding.layoutQuickFilter.addView(
                itemBinding.root
            )
        }
    }

    //B3:Viết hàm xử lý dữ liệu:
    private fun getCompanies(companies: ArrayList<Company> ) {
        //B1: Xóa dữ liệu cũ
        companies.clear()
        //B2: Định nghĩa đối tượng retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl(CompanyAPI.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        //B3: Xây dưnng đối tượng weatherAPI
        companyAPI = retrofit.create(CompanyAPI::class.java)
        //B4: Gọi hàm đọc dữ liệu từ Webservice
        val call = companyAPI.getCompany()
        //B5. Xử lý
        call.enqueue(object : Callback<CompanyList> {
            override fun onResponse(call: Call<CompanyList>, result: Response<CompanyList>) {
                if (!result.isSuccessful) return

                val companyList = result.body()

                companyList?.companyList?.let {
                    if (isFinishing || isDestroyed) return

                    companies.clear()
                    companies.addAll(it.take(5))
                    adapter.notifyDataSetChanged()

                    loadHighlightCompanies(it)
                }
            }

            override fun onFailure(p0: Call<CompanyList>, p1: Throwable) {
            }
        })
    }

    private fun loadHighlightCompanies(companies: List<Company>) {

        if (isFinishing || isDestroyed) return

        binding.layoutCompany.removeAllViews()

        val topCompanies = companies
            .groupBy { it.company.name }
            .values
            .sortedByDescending { it.size }
            .take(5)

        topCompanies.forEach { jobs ->

            val company = jobs.first()

            val itemBinding = ItemCompanyLayoutBinding.inflate(
                layoutInflater,
                binding.layoutCompany,
                false
            )

            itemBinding.txtCompany.text = company.company.name

            // FIX GLIDE AN TOÀN
            if (!isFinishing && !isDestroyed) {
                Glide.with(itemBinding.imgLogo)
                    .load(company.company.image)
                    .into(itemBinding.imgLogo)
            }

            itemBinding.root.setOnClickListener {
                val intent = Intent(this, SearchActivity::class.java)
                intent.putExtra("COMPANY", company.company.name)
                startActivity(intent)
            }

            binding.layoutCompany.addView(itemBinding.root)
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
                        role.equals("ADMIN", true)) {
                        View.VISIBLE
                    }
                    else {
                        View.GONE
                    }
            }
    }

    private fun checkPermission(permission: String): Boolean {
        return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQ && permissions.size == grantResults.size && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            doSomething()
        }
    }

    private fun doSomething() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val startTime = System.currentTimeMillis()

        FirebaseDatabase.getInstance()
            .reference
            .child("notifications")
            .child(uid)
            .addChildEventListener(object : ChildEventListener {

                override fun onChildAdded(
                    snapshot: DataSnapshot,
                    previousChildName: String?
                ) {
                    val notification =
                        snapshot.getValue(Notification::class.java)

                    if (
                        notification != null &&
                        notification.createdAt >= startTime
                    ) {
                        showNotification(
                            notification.title,
                            notification.message
                        )
                    }
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    @SuppressLint("MissingPermission")
    private fun showNotification(
        title: String,
        message: String
    ) {
        val intent = Intent(this, NotificationActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(this,0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(
            this,
            NotificationHelper.CHANNEL_ID
        )
            .setSmallIcon(R.drawable.ic_notify)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val manager = NotificationManagerCompat.from(this)

        manager.notify(
            System.currentTimeMillis().toInt(),
            notification
        )
    }
}
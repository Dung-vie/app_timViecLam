package vn.edu.tdc.apptimvieclam.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import vn.edu.tdc.apptimvieclam.adapters.AppliedJobAdapter
import vn.edu.tdc.apptimvieclam.databinding.AppliedJobsLayoutBinding
import vn.edu.tdc.apptimvieclam.models.Applications

import com.google.firebase.database.ValueEventListener
class AppliedJobsActivity : AppCompatActivity() {
    private lateinit var binding: AppliedJobsLayoutBinding
    private lateinit var appliedJobAdapter: AppliedJobAdapter
    private val applicationList = mutableListOf<Applications>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Khởi tạo và thiết lập ViewBinding
        binding = AppliedJobsLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 2. Bắt sự kiện nút Back quay lại màn hình trước
        binding.btnBack.setOnClickListener {
            finish()
        }

        // 3. Cấu hình RecyclerView hiển thị danh sách dạng hàng dọc
        binding.recyclerViewAppliedJobs.layoutManager = LinearLayoutManager(this)

        // 4. Gọi hàm tải danh sách từ Realtime Database về
        loadAppliedJobs()
    }

    private fun loadAppliedJobs() {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
        if (currentUserId == null) {
            Toast.makeText(this, "Vui lòng đăng nhập để xem danh sách!", Toast.LENGTH_SHORT).show()
            return
        }

        val dbRef = FirebaseDatabase.getInstance().getReference("applications")

        // Thực hiện truy vấn lọc theo mã userId của tài khoản hiện tại
        dbRef.orderByChild("userId").equalTo(currentUserId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    applicationList.clear()

                    for (child in snapshot.children) {
                        val app = child.getValue(Applications::class.java)
                        if (app != null) {
                            applicationList.add(app)
                        }
                    }

                    // Đảo ngược mảng để các đơn ứng tuyển mới nộp hiển thị lên trên cùng
                    applicationList.reverse()

                    // Kiểm tra xem Adapter đã từng khởi tạo chưa để tối ưu bộ nhớ qua ViewBinding
                    if (!::appliedJobAdapter.isInitialized) {
                        appliedJobAdapter = AppliedJobAdapter(applicationList)
                        binding.recyclerViewAppliedJobs.adapter = appliedJobAdapter
                    } else {
                        appliedJobAdapter.updateList(applicationList)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@AppliedJobsActivity, "Lỗi tải dữ liệu từ máy chủ", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
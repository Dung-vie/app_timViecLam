package vn.edu.tdc.apptimvieclam.models

data class Applications(
    // --- Các trường CŨ bạn đã có sẵn (giữ nguyên) ---
    var applicantName: String = "",
    var email: String = "",
    var jobTitle: String = "",
    var company: String = "", // Bạn có thể dùng luôn biến này thay cho companyName trong task
    var applyDate: String = "",

    // --- Các trường MỚI bổ sung để hoàn thành Task ---
    var applicationId: String = "", // Lưu mã ID duy nhất của đơn ứng tuyển trên Firebase
    var userId: String = "",        // Bắt buộc phải có để kiểm tra người dùng đã Apply chưa
    var jobId: String = "",         // Bắt buộc phải có để kiểm tra họ đã Apply công việc nào
    var status: String = "Pending"  // Trạng thái mặc định theo đúng yêu cầu
)
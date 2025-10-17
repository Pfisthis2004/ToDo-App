📝 ToDo App (Android)

Ứng dụng quản lý công việc hằng ngày được phát triển bằng Kotlin theo mô hình MVVM Architecture,
sử dụng Room Database, LiveData, và ViewBinding.
Mục tiêu: rèn luyện kỹ năng thiết kế ứng dụng Android theo chuẩn Clean Architecture.

🚀 Tính năng nổi bật

➕ Thêm công việc mới

✏️ Cập nhật hoặc chỉnh sửa công việc

❌ Xóa từng công việc hoặc toàn bộ danh sách

✅ Đánh dấu công việc hoàn thành / chưa hoàn thành

🔍 Tìm kiếm và lọc theo mức độ ưu tiên

🔔 Thông báo nhắc nhở (Notification + WorkManager)

💾 Lưu trữ dữ liệu offline bằng Room Database

🧱 Cấu trúc dự án
📁 Cấu trúc dự án
├── data/
│   ├── models/
│   │   ├── Priority.kt         # Enum định nghĩa mức độ ưu tiên
│   │   └── ToDoData.kt         # Entity của Room Database
│   ├── repository/
│   │   └── ToDoRepository.kt   # Lớp quản lý truy xuất dữ liệu (DAO + logic)
│   ├── viewmodel/
│   │   ├── ToDoViewModel.kt    # Chứa logic xử lý dữ liệu cho UI
│   │   ├── ToDoDao.kt          # DAO – định nghĩa truy vấn database
│   │   ├── ToDoDatabase.kt     # Tạo database instance
│   │   └── Converter.kt        # Chuyển đổi kiểu dữ liệu (Priority <-> String)
│
├── fragments/
│   ├── add/
│   │   └── AddFragment.kt      # Giao diện thêm công việc
│   ├── list/
│   │   ├── ListFragment.kt     # Danh sách công việc
│   │   ├── adapter/
│   │   │   ├── ListAdapter.kt  # Adapter cho RecyclerView
│   │   │   ├── ToDoDiffUtil.kt # So sánh dữ liệu cũ/mới để cập nhật hiệu quả
│   │   │   └── SwipeToDelete.kt# Xóa công việc bằng thao tác vuốt
│   │   └── NotificationHelper.kt # Hiển thị thông báo
│   ├── update/
│   │   └── UpdateFragment.kt   # Màn hình cập nhật công việc
│   ├── BindingAdapters.kt      # Gắn dữ liệu động vào UI
│   └── SharedViewModel.kt      # ViewModel chia sẻ dữ liệu giữa fragment
│
├── utils/
│   ├── ReminderWorker.kt       # Worker lên lịch nhắc nhở
│   └── Utils.kt                # Hàm tiện ích dùng chung
│
└── MainActivity.kt             # Activity chính chứa Navigation Host


🧰 Công nghệ sử dụng
Thành phần	Mô tả
Ngôn ngữ	Kotlin
Kiến trúc	MVVM + Repository Pattern
Database	Room
UI	RecyclerView, LiveData, ViewBinding
Thư viện AndroidX	Navigation, Lifecycle, Material Components
Thông báo & tác vụ nền	WorkManager, NotificationCompat



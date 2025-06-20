# 🧑‍💻 User Service

`user_service` là một service cung cấp các chức năng quản lý người dùng trong hệ thống, bao gồm:


## 🔧 Các chức năng chính

- ✅ **Đăng ký**: Cho phép người dùng tạo tài khoản mới.
- 🔐 **Đăng nhập**: Xác thực thông tin người dùng và cấp JWT token.
- 👥 **Lấy danh sách người dùng**: Truy vấn danh sách toàn bộ người dùng (có thể giới hạn theo phân quyền).
- 🙋 **Xem profile cá nhân**: Trả về thông tin cá nhân của người dùng hiện tại.
- 📝 **Cập nhật profile**: Cho phép người dùng cập nhật thông tin cá nhân.

- 🗑️ **Xóa mềm**: Hệ thống không xóa vĩnh viễn người dùng mà đánh dấu đã xóa.

- 🛡️ **Phân quyền**: Hỗ trợ phân loại người dùng theo vai trò như `ADMIN`, `USER`, ...
- 🔑 **Xác thực bằng JWT**: Sử dụng JSON Web Token cho quá trình xác thực và ủy quyền.

## Installation
1. ```git clone https://github.com/TuandG/user_service.git```
2. ```cd user_service```
3. ```docker-compose up --build```


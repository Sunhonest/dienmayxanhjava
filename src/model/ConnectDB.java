package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDB {
    
    // Cấu hình kết nối XAMPP
    private static final String HOST = "localhost";
    private static final String PORT = "3306";
    private static final String DBNAME = "dbdienmayxanh"; // Tên database bạn vừa tạo
    private static final String USERNAME = "root";        // Mặc định XAMPP là root
    private static final String PASSWORD = "";            // Mặc định XAMPP không có pass

    // Chuỗi kết nối chuẩn (đã bao gồm xử lý Tiếng Việt)
    private static final String URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DBNAME 
                                    + "?useUnicode=true&characterEncoding=UTF-8&useSSL=false";

    // Hàm lấy kết nối
    public static Connection getConnection() {
        Connection cons = null;
        try {
            // 1. Đăng ký Driver MySQL
            // (Lưu ý: Bạn phải Add thư viện mysql-connector-java-8.x.x.jar vào project trước nhé)
            Class.forName("com.mysql.cj.jdbc.Driver");

            
            // 2. Mở kết nối
            cons = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            // System.out.println("Kết nối CSDL thành công!"); // Bật lên nếu muốn test
            
        } catch (ClassNotFoundException e) {
            System.err.println("Lỗi: Không tìm thấy Driver MySQL! Bạn đã add thư viện chưa?");
            // e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Lỗi: Không thể kết nối đến XAMPP. Kiểm tra xem đã Start MySQL chưa?");
            // e.printStackTrace();
        }
        return cons;
    }
    
    // Hàm đóng kết nối (Dùng xong thì đóng cho nhẹ máy)
    public static void closeConnection(Connection c) {
        try {
            if (c != null) {
                c.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Main để chạy thử xem kết nối được chưa
    public static void main(String[] args) {
        Connection c = getConnection();
        if (c != null) {
            System.out.println("✅ CHÚC MỪNG! Đã kết nối thành công tới Database: " + DBNAME);
            closeConnection(c);
        } else {
            System.out.println("❌ THẤT BẠI! Kiểm tra lại XAMPP hoặc tên Database.");
        }
    }
}

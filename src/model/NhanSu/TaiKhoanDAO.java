/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.NhanSu;

/**
 *
 * @author trana
 */
import domain.TaiKhoan;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.ConnectDB;

public class TaiKhoanDAO {

    // 1. Lấy danh sách tất cả tài khoản
    public List<TaiKhoan> getAll() {
        List<TaiKhoan> list = new ArrayList<>();
        String sql = "SELECT * FROM TaiKhoan";

        try (Connection cons = ConnectDB.getConnection();
             PreparedStatement ps = cons.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                TaiKhoan tk = new TaiKhoan();
                tk.setTenDangNhap(rs.getString("TenDangNhap"));
                tk.setMatKhau(rs.getString("MatKhau"));
                tk.setMaNV(rs.getString("MaNV"));
                tk.setCapDoQuyen(rs.getInt("CapDoQuyen"));
                tk.setTrangThai(rs.getString("TrangThai"));
                list.add(tk);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2. Thêm mới tài khoản
    public int insert(TaiKhoan tk) {
        String sql = "INSERT INTO TaiKhoan (TenDangNhap, MatKhau, MaNV, CapDoQuyen, TrangThai) VALUES (?, ?, ?, ?, ?)";
        try (Connection cons = ConnectDB.getConnection();
             PreparedStatement ps = cons.prepareStatement(sql)) {

            ps.setString(1, tk.getTenDangNhap());
            ps.setString(2, tk.getMatKhau());
            ps.setString(3, tk.getMaNV());
            ps.setInt(4, tk.getCapDoQuyen());
            ps.setString(5, tk.getTrangThai());

            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // 3. Cập nhật thông tin (Cho phép đổi cả Tên đăng nhập)
    // Cần tham số oldUsername để biết dòng nào cần sửa
    public int update(TaiKhoan tk, String oldUsername) {
        // Cập nhật: TenDangNhap, MaNV, Quyen, TrangThai (MatKhau giữ nguyên, đổi ở hàm riêng)
        String sql = "UPDATE TaiKhoan SET TenDangNhap=?, MaNV=?, CapDoQuyen=?, TrangThai=? WHERE TenDangNhap=?";
        try (Connection cons = ConnectDB.getConnection();
             PreparedStatement ps = cons.prepareStatement(sql)) {

            ps.setString(1, tk.getTenDangNhap()); // Tên mới
            ps.setString(2, tk.getMaNV());
            ps.setInt(3, tk.getCapDoQuyen());
            ps.setString(4, tk.getTrangThai());
            
            ps.setString(5, oldUsername); // Điều kiện WHERE: Tên cũ

            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    // 4. Reset mật khẩu (Cập nhật riêng mật khẩu)
    public int resetPassword(String username, String newPass) {
        String sql = "UPDATE TaiKhoan SET MatKhau=? WHERE TenDangNhap=?";
        try (Connection cons = ConnectDB.getConnection();
             PreparedStatement ps = cons.prepareStatement(sql)) {

            ps.setString(1, newPass);
            ps.setString(2, username);

            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // 5. Xóa tài khoản
    public int delete(String username) {
        String sql = "DELETE FROM TaiKhoan WHERE TenDangNhap=?";
        try (Connection cons = ConnectDB.getConnection();
             PreparedStatement ps = cons.prepareStatement(sql)) {

            ps.setString(1, username);
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // 6. KIỂM TRA ĐĂNG NHẬP
    public TaiKhoan checkLogin(String user, String pass) {
        TaiKhoan tk = null;
        String sql = "SELECT * FROM TaiKhoan WHERE TenDangNhap=? AND MatKhau=? AND TrangThai='Hoạt động'";
        
        try (Connection cons = ConnectDB.getConnection();
             PreparedStatement ps = cons.prepareStatement(sql)) {
             
            ps.setString(1, user);
            ps.setString(2, pass);
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                tk = new TaiKhoan();
                tk.setTenDangNhap(rs.getString("TenDangNhap"));
                tk.setMatKhau(rs.getString("MatKhau"));
                tk.setMaNV(rs.getString("MaNV"));
                tk.setCapDoQuyen(rs.getInt("CapDoQuyen"));
                tk.setTrangThai(rs.getString("TrangThai"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tk;
    }
    
    // 7. Check trùng Username
    public boolean checkTrungUsername(String user) {
        String sql = "SELECT TenDangNhap FROM TaiKhoan WHERE TenDangNhap=?";
        try (Connection cons = ConnectDB.getConnection();
             PreparedStatement ps = cons.prepareStatement(sql)) {
             
            ps.setString(1, user);
            ResultSet rs = ps.executeQuery();
            return rs.next(); // True nếu đã có rồi
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 8. Đếm số lượng Admin đang hoạt động (Để bảo vệ Admin cuối cùng)
    public int getSoLuongAdmin() {
        // Giả sử Quyền 3 là Admin
        String sql = "SELECT COUNT(*) FROM TaiKhoan WHERE CapDoQuyen = 3 AND TrangThai = N'Hoạt động'";
        try (Connection cons = ConnectDB.getConnection();
             PreparedStatement ps = cons.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
             
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
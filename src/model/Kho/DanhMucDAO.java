package model.Kho;

import domain.Kho.DanhMuc;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.ConnectDB;

public class DanhMucDAO {

    // Lấy tất cả danh mục
    public List<DanhMuc> getAll() {
        List<DanhMuc> list = new ArrayList<>();
        try (Connection cons = ConnectDB.getConnection();
             PreparedStatement ps = cons.prepareStatement("SELECT * FROM danhmuc");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new DanhMuc(rs.getString("MaDanhMuc"), rs.getString("TenDanhMuc")));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // Thêm danh mục mới
    public int insert(DanhMuc dm) {
        int result = 0;
        try (Connection cons = ConnectDB.getConnection();
             PreparedStatement ps = cons.prepareStatement("INSERT INTO danhmuc(MaDanhMuc, TenDanhMuc) VALUES (?, ?)")) {
            ps.setString(1, dm.getMaDanhMuc());
            ps.setString(2, dm.getTenDanhMuc());
            result = ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
        return result;
    }

    // Cập nhật danh mục
    public int update(DanhMuc dm) {
        int result = 0;
        try (Connection cons = ConnectDB.getConnection();
             PreparedStatement ps = cons.prepareStatement("UPDATE danhmuc SET TenDanhMuc=? WHERE MaDanhMuc=?")) {
            ps.setString(1, dm.getTenDanhMuc());
            ps.setString(2, dm.getMaDanhMuc());
            result = ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
        return result;
    }

    // Xóa danh mục
    public int delete(String maDM) {
        int result = 0;
        try (Connection cons = ConnectDB.getConnection();
             PreparedStatement ps = cons.prepareStatement("DELETE FROM danhmuc WHERE MaDanhMuc=?")) {
            ps.setString(1, maDM);
            result = ps.executeUpdate();
        } catch (SQLException e) { 
            // Có thể ném lỗi ra Controller để xử lý thông báo "Đang có sản phẩm thuộc danh mục này"
            e.printStackTrace(); 
        }
        return result;
    }

    // Kiểm tra trùng mã
    public boolean checkTrungMa(String maDM) {
        boolean check = false;
        try (Connection cons = ConnectDB.getConnection();
             PreparedStatement ps = cons.prepareStatement("SELECT MaDanhMuc FROM danhmuc WHERE MaDanhMuc = ?")) {
            ps.setString(1, maDM);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) check = true;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return check;
    }
}
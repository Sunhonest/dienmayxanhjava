/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.NhanSu;

/**
 *
 * @author trana
 */
import domain.NhanVien;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.ConnectDB;

public class NhanVienDAO {

    // 1. Lấy danh sách tất cả nhân viên (Hiển thị lên bảng)
    public List<NhanVien> getAll() {
        List<NhanVien> list = new ArrayList<>();
        String sql = "SELECT * FROM NhanVien";

        try (Connection cons = ConnectDB.getConnection();
             PreparedStatement ps = cons.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                NhanVien nv = new NhanVien();
                nv.setMaNV(rs.getString("MaNV"));
                nv.setHoTen(rs.getString("HoTen"));
                nv.setGioiTinh(rs.getString("GioiTinh"));
                nv.setSdt(rs.getString("SDT"));
                nv.setEmail(rs.getString("Email"));
                nv.setMaCV(rs.getString("MaCV")); // Lấy mã chức vụ (VD: TN, QL)
                list.add(nv);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2. Thêm mới nhân viên
    public int insert(NhanVien nv) {
        String sql = "INSERT INTO NhanVien (MaNV, HoTen, GioiTinh, SDT, Email, MaCV) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection cons = ConnectDB.getConnection();
             PreparedStatement ps = cons.prepareStatement(sql)) {

            ps.setString(1, nv.getMaNV());
            ps.setString(2, nv.getHoTen());
            ps.setString(3, nv.getGioiTinh());
            ps.setString(4, nv.getSdt());
            ps.setString(5, nv.getEmail());
            ps.setString(6, nv.getMaCV());

            return ps.executeUpdate(); // Trả về 1 nếu thành công, 0 nếu thất bại
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // 3. Cập nhật nhân viên (Dựa theo Mã NV)
    public int update(NhanVien nv) {
        String sql = "UPDATE NhanVien SET HoTen=?, GioiTinh=?, SDT=?, Email=?, MaCV=? WHERE MaNV=?";
        try (Connection cons = ConnectDB.getConnection();
             PreparedStatement ps = cons.prepareStatement(sql)) {

            ps.setString(1, nv.getHoTen());
            ps.setString(2, nv.getGioiTinh());
            ps.setString(3, nv.getSdt());
            ps.setString(4, nv.getEmail());
            ps.setString(5, nv.getMaCV());
            ps.setString(6, nv.getMaNV()); // Điều kiện WHERE

            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // 4. Xóa nhân viên
    public int delete(String maNV) {
        String sql = "DELETE FROM NhanVien WHERE MaNV=?";
        try (Connection cons = ConnectDB.getConnection();
             PreparedStatement ps = cons.prepareStatement(sql)) {

            ps.setString(1, maNV);
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    // 5. Kiểm tra trùng mã (Để không cho nhập trùng mã NV đã có)
    public boolean checkTrungMa(String maNV) {
        String sql = "SELECT MaNV FROM NhanVien WHERE MaNV=?";
        try (Connection cons = ConnectDB.getConnection();
             PreparedStatement ps = cons.prepareStatement(sql)) {
             
            ps.setString(1, maNV);
            ResultSet rs = ps.executeQuery();
            return rs.next(); // Nếu có dữ liệu => Đã trùng
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

package model.NhanSu;

import domain.ChucVu; // Đảm bảo bạn đã có class này bên package domain
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.ConnectDB;

public class ChucVuDAO {

    // 1. Lấy danh sách
    public List<ChucVu> getAll() {
        List<ChucVu> list = new ArrayList<>();
        String sql = "SELECT * FROM ChucVu";

        try (Connection cons = ConnectDB.getConnection();
             PreparedStatement ps = cons.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ChucVu cv = new ChucVu();
                cv.setMaCV(rs.getString("MaCV"));
                cv.setTenCV(rs.getString("TenCV"));
                cv.setLuongCoBan(rs.getDouble("LuongCoBan"));
                cv.setMoTa(rs.getString("MoTa"));
                list.add(cv);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    // 2. Thêm mới
    public int insert(ChucVu cv) {
        String sql = "INSERT INTO ChucVu(MaCV, TenCV, LuongCoBan, MoTa) VALUES (?, ?, ?, ?)";
        try (Connection cons = ConnectDB.getConnection();
             PreparedStatement ps = cons.prepareStatement(sql)) {
             
            ps.setString(1, cv.getMaCV());
            ps.setString(2, cv.getTenCV());
            ps.setDouble(3, cv.getLuongCoBan());
            ps.setString(4, cv.getMoTa());
            
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    // 3. Sửa
    public int update(ChucVu cv) {
        String sql = "UPDATE ChucVu SET TenCV=?, LuongCoBan=?, MoTa=? WHERE MaCV=?";
        try (Connection cons = ConnectDB.getConnection();
             PreparedStatement ps = cons.prepareStatement(sql)) {
             
            ps.setString(1, cv.getTenCV());
            ps.setDouble(2, cv.getLuongCoBan());
            ps.setString(3, cv.getMoTa());
            ps.setString(4, cv.getMaCV());
            
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    // 4. Xóa
    public int delete(String maCV) {
        String sql = "DELETE FROM ChucVu WHERE MaCV=?";
        try (Connection cons = ConnectDB.getConnection();
             PreparedStatement ps = cons.prepareStatement(sql)) {
             
            ps.setString(1, maCV);
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    // 5. Check trùng mã
    public boolean checkTrungMa(String maCV) {
        String sql = "SELECT MaCV FROM ChucVu WHERE MaCV=?";
        try (Connection cons = ConnectDB.getConnection();
             PreparedStatement ps = cons.prepareStatement(sql)) {
            ps.setString(1, maCV);
            return ps.executeQuery().next();
        } catch (Exception e) {
            return false;
        }
    }
}
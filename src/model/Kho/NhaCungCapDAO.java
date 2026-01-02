package model.Kho;

import domain.Kho.NhaCungCap;
import model.ConnectDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NhaCungCapDAO {
    
    // Lấy tất cả nhà cung cấp
    public List<NhaCungCap> getAll() {
        List<NhaCungCap> list = new ArrayList<>();
        try (Connection cons = ConnectDB.getConnection();
             PreparedStatement ps = cons.prepareStatement("SELECT * FROM nhacungcap");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                NhaCungCap ncc = new NhaCungCap();
                ncc.setMaNCC(rs.getString("MaNCC"));
                ncc.setTenNCC(rs.getString("TenNCC"));
                ncc.setDiaChi(rs.getString("DiaChi"));
                ncc.setSdt(rs.getString("SDT"));
                list.add(ncc);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // Thêm mới
    public int insert(NhaCungCap ncc) {
        int result = 0;
        try (Connection cons = ConnectDB.getConnection();
             PreparedStatement ps = cons.prepareStatement("INSERT INTO nhacungcap(MaNCC, TenNCC, DiaChi, SDT) VALUES (?, ?, ?, ?)")) {
            ps.setString(1, ncc.getMaNCC());
            ps.setString(2, ncc.getTenNCC());
            ps.setString(3, ncc.getDiaChi());
            ps.setString(4, ncc.getSdt());
            result = ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
        return result;
    }

    // Cập nhật
    public int update(NhaCungCap ncc) {
        int result = 0;
        try (Connection cons = ConnectDB.getConnection();
             PreparedStatement ps = cons.prepareStatement("UPDATE nhacungcap SET TenNCC=?, DiaChi=?, SDT=? WHERE MaNCC=?")) {
            ps.setString(1, ncc.getTenNCC());
            ps.setString(2, ncc.getDiaChi());
            ps.setString(3, ncc.getSdt());
            ps.setString(4, ncc.getMaNCC());
            result = ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
        return result;
    }

    // Xóa
    public int delete(String maNCC) {
        int result = 0;
        try (Connection cons = ConnectDB.getConnection();
             PreparedStatement ps = cons.prepareStatement("DELETE FROM nhacungcap WHERE MaNCC=?")) {
            ps.setString(1, maNCC);
            result = ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
        return result;
    }

    // Kiểm tra trùng mã
    public boolean checkTrungMa(String maNCC) {
        boolean check = false;
        try (Connection cons = ConnectDB.getConnection();
             PreparedStatement ps = cons.prepareStatement("SELECT MaNCC FROM nhacungcap WHERE MaNCC = ?")) {
            ps.setString(1, maNCC);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) check = true;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return check;
    }
}
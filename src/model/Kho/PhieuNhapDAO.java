package model.Kho;

import domain.Kho.PhieuNhap;
import model.ConnectDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PhieuNhapDAO {

    // 1. CẬP NHẬT: Lấy thêm cột GhiChu từ Database
    public List<PhieuNhap> getAll() {
        List<PhieuNhap> list = new ArrayList<>();
        String sql = "SELECT pn.*, sp.TenSP, ncc.TenNCC " +
                     "FROM phieu_nhap pn " +
                     "JOIN sanpham sp ON pn.MaSP = sp.MaSP " +
                     "JOIN nhacungcap ncc ON pn.MaNCC = ncc.MaNCC " +
                     "ORDER BY pn.ID DESC"; 
        
        try (Connection cons = ConnectDB.getConnection();
             PreparedStatement ps = cons.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                PhieuNhap pn = new PhieuNhap();
                pn.setId(rs.getInt("ID"));
                pn.setMaPhieu(rs.getString("MaPhieu"));
                pn.setMaNV(rs.getString("MaNV"));
                pn.setMaNCC(rs.getString("MaNCC"));
                pn.setTenNCC(rs.getString("TenNCC"));
                pn.setMaSP(rs.getString("MaSP"));
                pn.setTenSP(rs.getString("TenSP"));
                pn.setSoLuong(rs.getInt("SoLuong"));
                pn.setDonGia(rs.getDouble("DonGia"));
                pn.setThanhTien(rs.getDouble("ThanhTien"));
                pn.setNgayNhap(rs.getTimestamp("NgayNhap"));
                // --- FIX: Lấy thêm ghi chú ---
                pn.setGhiChu(rs.getString("GhiChu")); 
                list.add(pn);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // 2. CẬP NHẬT: Thêm cột GhiChu vào câu lệnh INSERT
    public boolean them(PhieuNhap pn) {
        Connection cons = null;
        try {
            cons = ConnectDB.getConnection();
            cons.setAutoCommit(false); 

            // Thêm GhiChu vào SQL
            String sqlInsert = "INSERT INTO phieu_nhap(MaPhieu, MaNV, MaNCC, MaSP, SoLuong, DonGia, ThanhTien, GhiChu) VALUES(?,?,?,?,?,?,?,?)";
            PreparedStatement ps = cons.prepareStatement(sqlInsert);
            ps.setString(1, pn.getMaPhieu());
            ps.setString(2, pn.getMaNV());
            ps.setString(3, pn.getMaNCC());
            ps.setString(4, pn.getMaSP());
            ps.setInt(5, pn.getSoLuong());
            ps.setDouble(6, pn.getDonGia());
            ps.setDouble(7, pn.getThanhTien());
            // --- FIX: Set giá trị Ghi chú ---
            ps.setString(8, pn.getGhiChu());
            
            ps.executeUpdate();

            // Cộng tồn kho (Giữ nguyên)
            String sqlUpdateKho = "UPDATE sanpham SET TonKho = TonKho + ?, GiaNhap = ? WHERE MaSP = ?";
            PreparedStatement psKho = cons.prepareStatement(sqlUpdateKho);
            psKho.setInt(1, pn.getSoLuong());
            psKho.setDouble(2, pn.getDonGia());
            psKho.setString(3, pn.getMaSP());
            psKho.executeUpdate();

            cons.commit();
            return true;
        } catch (Exception e) {
            try { if(cons!=null) cons.rollback(); } catch(SQLException ex){}
            e.printStackTrace();
            return false;
        } finally {
            ConnectDB.closeConnection(cons);
        }
    }
    
    // 3. CẬP NHẬT: Cho phép sửa nội dung Ghi chú
    public boolean suaPhieuNhap(PhieuNhap pnMoi, int soLuongCu) {
        Connection cons = null;
        try {
            cons = ConnectDB.getConnection();
            cons.setAutoCommit(false); 

            String sqlUpdate = "UPDATE phieu_nhap SET SoLuong=?, DonGia=?, ThanhTien=?, GhiChu=? WHERE ID=?";
            PreparedStatement ps = cons.prepareStatement(sqlUpdate);
            ps.setInt(1, pnMoi.getSoLuong());
            ps.setDouble(2, pnMoi.getDonGia());
            ps.setDouble(3, pnMoi.getThanhTien());
            // --- FIX: Lấy ghi chú từ object truyền vào thay vì hardcode ---
            ps.setString(4, pnMoi.getGhiChu()); 
            ps.setInt(5, pnMoi.getId());
            ps.executeUpdate();

            // Cân bằng kho (Giữ nguyên)
            int chenhLech = pnMoi.getSoLuong() - soLuongCu; 
            String sqlKho = "UPDATE sanpham SET TonKho = TonKho + ? WHERE MaSP = ?";
            PreparedStatement psKho = cons.prepareStatement(sqlKho);
            psKho.setInt(1, chenhLech); 
            psKho.setString(2, pnMoi.getMaSP());
            psKho.executeUpdate();

            cons.commit(); 
            return true;
        } catch (Exception e) {
            try { if(cons!=null) cons.rollback(); } catch(SQLException ex){}
            e.printStackTrace();
            return false;
        } finally {
            ConnectDB.closeConnection(cons);
        }
    }

    // Các hàm khác giữ nguyên...
    public boolean xoa(int id, String maSP, int soLuong) {
         // (Code hàm xóa giữ nguyên như cũ)
         Connection cons = null;
        try {
            cons = ConnectDB.getConnection();
            cons.setAutoCommit(false);
            String sqlDel = "DELETE FROM phieu_nhap WHERE ID = ?";
            PreparedStatement ps = cons.prepareStatement(sqlDel);
            ps.setInt(1, id);
            ps.executeUpdate();
            String sqlUpdateKho = "UPDATE sanpham SET TonKho = TonKho - ? WHERE MaSP = ?";
            PreparedStatement psKho = cons.prepareStatement(sqlUpdateKho);
            psKho.setInt(1, soLuong);
            psKho.setString(2, maSP);
            psKho.executeUpdate();
            cons.commit();
            return true;
        } catch (Exception e) {
            try { if(cons!=null) cons.rollback(); } catch(SQLException ex){}
            return false;
        } finally {
            ConnectDB.closeConnection(cons);
        }
    }
}
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.BaoHanh;

import domain.PhieuBaoHanh;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.ConnectDB; 

public class BaoHanhDAO {

    // 1. Lấy toàn bộ danh sách phiếu bảo hành
    // 1. Lấy toàn bộ danh sách (ĐÃ SỬA: JOIN VỚI BẢNG SẢN PHẨM ĐỂ LẤY TÊN)
    public List<PhieuBaoHanh> getAll() {
        List<PhieuBaoHanh> list = new ArrayList<>();
        // Sửa câu lệnh SQL: JOIN bảng phieu_baohanh với sanpham
        String sql = "SELECT p.*, sp.TenSP " +
                     "FROM phieu_baohanh p " +
                     "JOIN sanpham sp ON p.MaSP = sp.MaSP";
        
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                PhieuBaoHanh p = new PhieuBaoHanh();
                p.setMaPhieu(rs.getString("MaPhieu"));
                p.setMaHoaDon(rs.getString("MaHoaDon"));
                p.setMaSP(rs.getString("MaSP"));
                
                // --- QUAN TRỌNG: LẤY TÊN SP TỪ CSDL ---
                p.setTenSP(rs.getString("TenSP")); 
                // --------------------------------------
                
                p.setSoSerial(rs.getString("SoSerial"));
                p.setTenKhachHang(rs.getString("TenKhachHang"));
                p.setSoDienThoai(rs.getString("SoDienThoai"));
                p.setNgayTiepNhan(rs.getDate("NgayTiepNhan"));
                p.setMoTaLoi(rs.getString("MoTaLoi"));
                p.setTrangThai(rs.getString("TrangThai"));
                
                list.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2. Tra cứu thông tin từ Hóa Đơn (Cho nút Check)
    // 2. Tra cứu thông tin từ Hóa Đơn (ĐÃ CẬP NHẬT LẤY TÊN SP)
    public String[] getThongTinTuHoaDon(String maHD) {
        // Tăng kích thước mảng lên 4 phần tử để chứa thêm Tên SP
        String[] result = new String[4]; 
        
        // Join thêm bảng sanpham (sp) để lấy TenSP
        String sql = "SELECT kh.HoTen, kh.SDT, ct.MaSP, sp.TenSP " +
                     "FROM hoadon hd " +
                     "JOIN khachhang kh ON hd.MaKH = kh.MaKH " +
                     "JOIN chitiet_donhang ct ON hd.MaDonHang = ct.MaDonHang " + 
                     "JOIN sanpham sp ON ct.MaSP = sp.MaSP " + // <-- Dòng mới thêm
                     "WHERE hd.MaHoaDon = ? LIMIT 1"; 

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, maHD);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    result[0] = rs.getString("MaSP");   
                    result[1] = rs.getString("HoTen");  
                    result[2] = rs.getString("SDT");
                    result[3] = rs.getString("TenSP"); // <-- Lấy thêm Tên SP
                    return result; 
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 3. Thêm mới
    public int insert(PhieuBaoHanh p) {
        String sql = "INSERT INTO phieu_baohanh (MaPhieu, MaHoaDon, MaSP, SoSerial, TenKhachHang, SoDienThoai, NgayTiepNhan, MoTaLoi, TrangThai) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, p.getMaPhieu());
            ps.setString(2, p.getMaHoaDon());
            ps.setString(3, p.getMaSP());
            ps.setString(4, p.getSoSerial());
            ps.setString(5, p.getTenKhachHang());
            ps.setString(6, p.getSoDienThoai());
            
            if (p.getNgayTiepNhan() != null) {
                ps.setDate(7, new java.sql.Date(p.getNgayTiepNhan().getTime()));
            } else {
                ps.setDate(7, new java.sql.Date(System.currentTimeMillis()));
            }
            
            ps.setString(8, p.getMoTaLoi());
            ps.setString(9, p.getTrangThai());
            
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // 4. Cập nhật
    public int update(PhieuBaoHanh p) {
        String sql = "UPDATE phieu_baohanh SET MoTaLoi=?, TrangThai=?, SoSerial=? WHERE MaPhieu=?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, p.getMoTaLoi());
            ps.setString(2, p.getTrangThai());
            ps.setString(3, p.getSoSerial());
            ps.setString(4, p.getMaPhieu());
            
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // 5. Xóa
    public int delete(String maPhieu) {
        String sql = "DELETE FROM phieu_baohanh WHERE MaPhieu=?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maPhieu);
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    // 6. Kiểm tra trùng mã
    public boolean checkTrungMa(String maPhieu) {
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT 1 FROM phieu_baohanh WHERE MaPhieu=?")) {
            ps.setString(1, maPhieu);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    // 7. [MỚI] Tìm kiếm phiếu theo từ khóa (Mã phiếu, Tên khách, Serial, SĐT)
    // 7. Tìm kiếm (CẬP NHẬT JOIN TÊN SP)
    public List<PhieuBaoHanh> search(String keyword) {
        List<PhieuBaoHanh> list = new ArrayList<>();
        // Join thêm bảng sanpham
        String sql = "SELECT p.*, sp.TenSP " +
                     "FROM phieu_baohanh p " +
                     "JOIN sanpham sp ON p.MaSP = sp.MaSP " +
                     "WHERE p.MaPhieu LIKE ? OR p.TenKhachHang LIKE ? OR p.SoSerial LIKE ? OR p.SoDienThoai LIKE ?";
        
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            String pattern = "%" + keyword + "%";
            ps.setString(1, pattern);
            ps.setString(2, pattern);
            ps.setString(3, pattern);
            ps.setString(4, pattern);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PhieuBaoHanh p = new PhieuBaoHanh();
                    p.setMaPhieu(rs.getString("MaPhieu"));
                    p.setMaHoaDon(rs.getString("MaHoaDon"));
                    p.setMaSP(rs.getString("MaSP"));
                    p.setTenSP(rs.getString("TenSP")); // <--- LẤY TÊN SP
                    p.setSoSerial(rs.getString("SoSerial"));
                    p.setTenKhachHang(rs.getString("TenKhachHang"));
                    p.setSoDienThoai(rs.getString("SoDienThoai"));
                    p.setNgayTiepNhan(rs.getDate("NgayTiepNhan"));
                    p.setMoTaLoi(rs.getString("MoTaLoi"));
                    p.setTrangThai(rs.getString("TrangThai"));
                    list.add(p);
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // 8. [MỚI] Lấy chi tiết 1 phiếu theo Mã (Dùng cho chức năng Trả máy)
    // 8. Lấy chi tiết theo ID (CẬP NHẬT JOIN TÊN SP)
    public PhieuBaoHanh getByID(String maPhieu) {
        String sql = "SELECT p.*, sp.TenSP " +
                     "FROM phieu_baohanh p " +
                     "JOIN sanpham sp ON p.MaSP = sp.MaSP " +
                     "WHERE p.MaPhieu = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, maPhieu);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    PhieuBaoHanh p = new PhieuBaoHanh();
                    p.setMaPhieu(rs.getString("MaPhieu"));
                    p.setMaHoaDon(rs.getString("MaHoaDon"));
                    p.setMaSP(rs.getString("MaSP"));
                    p.setTenSP(rs.getString("TenSP")); // <--- LẤY TÊN SP
                    p.setSoSerial(rs.getString("SoSerial"));
                    p.setTenKhachHang(rs.getString("TenKhachHang"));
                    p.setSoDienThoai(rs.getString("SoDienThoai"));
                    p.setNgayTiepNhan(rs.getDate("NgayTiepNhan"));
                    p.setMoTaLoi(rs.getString("MoTaLoi"));
                    p.setTrangThai(rs.getString("TrangThai"));
                    return p;
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }
}
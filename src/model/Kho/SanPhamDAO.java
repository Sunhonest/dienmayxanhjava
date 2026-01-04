/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.Kho;

/**
 *
 * @author nguye
 */
import domain.Kho.SanPham;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.ConnectDB;

public class SanPhamDAO {

    public List<SanPham> getAll() {
        List<SanPham> list = new ArrayList<>();
        // Chỉ lấy sản phẩm đang kinh doanh để hiển thị
        String sql = "SELECT * FROM sanpham "; //WHERE TrangThaiKinhDoanh = 'DANG_BAN'
        try (Connection cons = ConnectDB.getConnection();
             PreparedStatement ps = cons.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                SanPham sp = new SanPham();
                sp.setMaSP(rs.getString("MaSP"));
                sp.setTenSP(rs.getString("TenSP"));
                sp.setMaDanhMuc(rs.getString("MaDanhMuc"));
                sp.setThuongHieu(rs.getString("ThuongHieu"));
                sp.setDonViTinh(rs.getString("DonViTinh"));
                sp.setGiaNhap(rs.getDouble("GiaNhap"));
                sp.setGiaBan(rs.getDouble("GiaBan"));
                sp.setTonKho(rs.getInt("TonKho"));
                sp.setThoiGianBaoHanh(rs.getInt("ThoiGianBaoHanh"));
                sp.setTrangThaiHang(rs.getString("TrangThaiHang"));
                sp.setTrangThaiKinhDoanh(rs.getString("TrangThaiKinhDoanh"));
                sp.setMoTa(rs.getString("MoTa"));
                sp.setHinhAnh(rs.getString("HinhAnh"));
                list.add(sp);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public int insert(SanPham sp) {
        String sql = "INSERT INTO sanpham (MaSP, TenSP, MaDanhMuc, ThuongHieu, DonViTinh, GiaNhap, GiaBan, TonKho, ThoiGianBaoHanh, TrangThaiHang, TrangThaiKinhDoanh, MoTa, HinhAnh) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection cons = ConnectDB.getConnection();
             PreparedStatement ps = cons.prepareStatement(sql)) {
            ps.setString(1, sp.getMaSP());
            ps.setString(2, sp.getTenSP());
            ps.setString(3, sp.getMaDanhMuc());
            ps.setString(4, sp.getThuongHieu());
            ps.setString(5, sp.getDonViTinh());
            ps.setDouble(6, sp.getGiaNhap());
            ps.setDouble(7, sp.getGiaBan());
            ps.setInt(8, sp.getTonKho()); 
            ps.setInt(9, sp.getThoiGianBaoHanh());
            ps.setString(10, sp.getTrangThaiHang());
            ps.setString(11, "DANG_BAN"); // Mặc định
            ps.setString(12, sp.getMoTa());
            ps.setString(13, sp.getHinhAnh());
            return ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); return 0; }
    }

    public int update(SanPham sp) {
        String sql = "UPDATE sanpham SET TenSP=?, MaDanhMuc=?, ThuongHieu=?, DonViTinh=?, GiaNhap=?, GiaBan=?, ThoiGianBaoHanh=?, TrangThaiHang=?, MoTa=?, HinhAnh=? WHERE MaSP=?";
        try (Connection cons = ConnectDB.getConnection();
             PreparedStatement ps = cons.prepareStatement(sql)) {
            ps.setString(1, sp.getTenSP());
            ps.setString(2, sp.getMaDanhMuc());
            ps.setString(3, sp.getThuongHieu());
            ps.setString(4, sp.getDonViTinh());
            ps.setDouble(5, sp.getGiaNhap());
            ps.setDouble(6, sp.getGiaBan());
            ps.setInt(7, sp.getThoiGianBaoHanh());
            ps.setString(8, sp.getTrangThaiHang());
            ps.setString(9, sp.getMoTa());
            ps.setString(10, sp.getHinhAnh());
            ps.setString(11, sp.getMaSP());
            return ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); return 0; }
    }

//    // Xóa mềm: Chuyển trạng thái sang NGUNG_KINH_DOANH
//    public int delete(String maSP) {
//        String sql = "UPDATE sanpham SET TrangThaiKinhDoanh = 'NGUNG_KINH_DOANH' WHERE MaSP=?";
//        try (Connection cons = ConnectDB.getConnection();
//             PreparedStatement ps = cons.prepareStatement(sql)) {
//            ps.setString(1, maSP);
//            return ps.executeUpdate();
//        } catch (Exception e) { e.printStackTrace(); return 0; }
//    }

    // Xóa cứng: Xóa hẳn dữ liệu khỏi bảng Database
    public int delete(String maSP) {
        // Thay đổi câu lệnh SQL từ UPDATE sang DELETE
        String sql = "DELETE FROM sanpham WHERE MaSP=?";
        
        try (Connection cons = ConnectDB.getConnection();
             PreparedStatement ps = cons.prepareStatement(sql)) {
            
            ps.setString(1, maSP);
            
            return ps.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    public boolean checkTrungMa(String maSP) {
        try (Connection cons = ConnectDB.getConnection();
             PreparedStatement ps = cons.prepareStatement("SELECT MaSP FROM sanpham WHERE MaSP=?")) {
            ps.setString(1, maSP);
            return ps.executeQuery().next();
        } catch (Exception e) { return false; }
    }
}
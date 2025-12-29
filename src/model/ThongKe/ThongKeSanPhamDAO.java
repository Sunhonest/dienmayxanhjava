package model.ThongKe;

import domain.ThongKeSanPham;
import model.ConnectDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO class cho thống kê sản phẩm bán chạy
 * @author nguye
 */
public class ThongKeSanPhamDAO {

    /**
     * Thống kê sản phẩm bán chạy nhất trong khoảng thời gian
     * @param ngayBatDau ngày bắt đầu (yyyy-mm-dd)
     * @param ngayKetThuc ngày kết thúc (yyyy-mm-dd)
     * @param limit số lượng sản phẩm muốn lấy (top N)
     * @return danh sách sản phẩm bán chạy
     */
    public List<ThongKeSanPham> getSanPhamBanChay(String ngayBatDau, String ngayKetThuc, int limit) {
        List<ThongKeSanPham> list = new ArrayList<>();
        String sql = "SELECT sp.MaSP, sp.TenSP, dm.TenDanhMuc as danhMuc, " +
                     "SUM(ctd.SoLuong) as soLuongBan, " +
                     "SUM(ctd.ThanhTien) as doanhThu, " +
                     "sp.GiaBan, sp.TonKho, sp.TrangThaiKinhDoanh as trangThai " +
                     "FROM chitiet_donhang ctd " +
                     "INNER JOIN donhang dh ON ctd.MaDonHang = dh.MaDonHang " +
                     "INNER JOIN hoadon h ON dh.MaDonHang = h.MaDonHang " +
                     "INNER JOIN sanpham sp ON ctd.MaSP = sp.MaSP " +
                     "LEFT JOIN danhmuc dm ON sp.MaDanhMuc = dm.MaDanhMuc " +
                     "WHERE h.TrangThai = 'DA_THANH_TOAN' " +
                     "AND DATE(h.NgayLap) BETWEEN ? AND ? " +
                     "GROUP BY sp.MaSP, sp.TenSP, dm.TenDanhMuc, sp.GiaBan, sp.TonKho, sp.TrangThaiKinhDoanh " +
                     "ORDER BY soLuongBan DESC " +
                     "LIMIT ?";
        
        try (Connection cons = ConnectDB.getConnection();
             PreparedStatement ps = cons.prepareStatement(sql)) {
            
            ps.setString(1, ngayBatDau);
            ps.setString(2, ngayKetThuc);
            ps.setInt(3, limit);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                ThongKeSanPham tk = new ThongKeSanPham();
                tk.setMaSP(rs.getString("MaSP"));
                tk.setTenSP(rs.getString("TenSP"));
                tk.setDanhMuc(rs.getString("danhMuc"));
                tk.setSoLuongBan(rs.getInt("soLuongBan"));
                tk.setDoanhThu(rs.getDouble("doanhThu"));
                tk.setGiaBan(rs.getDouble("GiaBan"));
                tk.setTonKho(rs.getInt("TonKho"));
                tk.setTrangThai(rs.getString("trangThai"));
                list.add(tk);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Thống kê sản phẩm bán chậm nhất trong khoảng thời gian
     * @param ngayBatDau ngày bắt đầu (yyyy-mm-dd)
     * @param ngayKetThuc ngày kết thúc (yyyy-mm-dd)
     * @param limit số lượng sản phẩm muốn lấy (top N)
     * @return danh sách sản phẩm bán chậm
     */
    public List<ThongKeSanPham> getSanPhamBanCham(String ngayBatDau, String ngayKetThuc, int limit) {
        List<ThongKeSanPham> list = new ArrayList<>();
        String sql = "SELECT sp.MaSP, sp.TenSP, dm.TenDanhMuc as danhMuc, " +
                     "COALESCE(SUM(ctd.SoLuong), 0) as soLuongBan, " +
                     "COALESCE(SUM(ctd.ThanhTien), 0) as doanhThu, " +
                     "sp.GiaBan, sp.TonKho, sp.TrangThaiKinhDoanh as trangThai " +
                     "FROM sanpham sp " +
                     "LEFT JOIN danhmuc dm ON sp.MaDanhMuc = dm.MaDanhMuc " +
                     "LEFT JOIN chitiet_donhang ctd ON sp.MaSP = ctd.MaSP " +
                     "LEFT JOIN donhang dh ON ctd.MaDonHang = dh.MaDonHang " +
                     "LEFT JOIN hoadon h ON dh.MaDonHang = h.MaDonHang " +
                     "AND h.TrangThai = 'DA_THANH_TOAN' " +
                     "AND DATE(h.NgayLap) BETWEEN ? AND ? " +
                     "WHERE sp.TrangThaiKinhDoanh = 'DANG_BAN' " +
                     "GROUP BY sp.MaSP, sp.TenSP, dm.TenDanhMuc, sp.GiaBan, sp.TonKho, sp.TrangThaiKinhDoanh " +
                     "ORDER BY soLuongBan ASC " +
                     "LIMIT ?";
        
        try (Connection cons = ConnectDB.getConnection();
             PreparedStatement ps = cons.prepareStatement(sql)) {
            
            ps.setString(1, ngayBatDau);
            ps.setString(2, ngayKetThuc);
            ps.setInt(3, limit);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                ThongKeSanPham tk = new ThongKeSanPham();
                tk.setMaSP(rs.getString("MaSP"));
                tk.setTenSP(rs.getString("TenSP"));
                tk.setDanhMuc(rs.getString("danhMuc"));
                tk.setSoLuongBan(rs.getInt("soLuongBan"));
                tk.setDoanhThu(rs.getDouble("doanhThu"));
                tk.setGiaBan(rs.getDouble("GiaBan"));
                tk.setTonKho(rs.getInt("TonKho"));
                tk.setTrangThai(rs.getString("trangThai"));
                list.add(tk);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Thống kê sản phẩm theo danh mục
     * @param maDanhMuc mã danh mục (null để lấy tất cả)
     * @param ngayBatDau ngày bắt đầu
     * @param ngayKetThuc ngày kết thúc
     * @return danh sách thống kê sản phẩm theo danh mục
     */
    public List<ThongKeSanPham> getSanPhamTheoDanhMuc(String maDanhMuc, String ngayBatDau, String ngayKetThuc) {
        List<ThongKeSanPham> list = new ArrayList<>();
        String sql = "SELECT sp.MaSP, sp.TenSP, dm.TenDanhMuc as danhMuc, " +
                     "COALESCE(SUM(ctd.SoLuong), 0) as soLuongBan, " +
                     "COALESCE(SUM(ctd.ThanhTien), 0) as doanhThu, " +
                     "sp.GiaBan, sp.TonKho, sp.TrangThaiKinhDoanh as trangThai " +
                     "FROM sanpham sp " +
                     "LEFT JOIN danhmuc dm ON sp.MaDanhMuc = dm.MaDanhMuc " +
                     "LEFT JOIN chitiet_donhang ctd ON sp.MaSP = ctd.MaSP " +
                     "LEFT JOIN donhang dh ON ctd.MaDonHang = dh.MaDonHang " +
                     "LEFT JOIN hoadon h ON dh.MaDonHang = h.MaDonHang " +
                     "AND h.TrangThai = 'DA_THANH_TOAN' " +
                     "AND DATE(h.NgayLap) BETWEEN ? AND ? " +
                     "WHERE sp.TrangThaiKinhDoanh = 'DANG_BAN' ";
        
        if (maDanhMuc != null && !maDanhMuc.trim().isEmpty()) {
            sql += "AND sp.MaDanhMuc = ? ";
        }
        
        sql += "GROUP BY sp.MaSP, sp.TenSP, dm.TenDanhMuc, sp.GiaBan, sp.TonKho, sp.TrangThaiKinhDoanh " +
               "ORDER BY soLuongBan DESC";
        
        try (Connection cons = ConnectDB.getConnection();
             PreparedStatement ps = cons.prepareStatement(sql)) {
            
            ps.setString(1, ngayBatDau);
            ps.setString(2, ngayKetThuc);
            if (maDanhMuc != null && !maDanhMuc.trim().isEmpty()) {
                ps.setString(3, maDanhMuc);
            }
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                ThongKeSanPham tk = new ThongKeSanPham();
                tk.setMaSP(rs.getString("MaSP"));
                tk.setTenSP(rs.getString("TenSP"));
                tk.setDanhMuc(rs.getString("danhMuc"));
                tk.setSoLuongBan(rs.getInt("soLuongBan"));
                tk.setDoanhThu(rs.getDouble("doanhThu"));
                tk.setGiaBan(rs.getDouble("GiaBan"));
                tk.setTonKho(rs.getInt("TonKho"));
                tk.setTrangThai(rs.getString("trangThai"));
                list.add(tk);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Thống kê sản phẩm sắp hết hàng
     * @param nguongTonKho ngưỡng tồn kho tối thiểu
     * @return danh sách sản phẩm sắp hết hàng
     */
    public List<ThongKeSanPham> getSanPhamSapHetHang(int nguongTonKho) {
        List<ThongKeSanPham> list = new ArrayList<>();
        String sql = "SELECT sp.MaSP, sp.TenSP, dm.TenDanhMuc as danhMuc, " +
                     "0 as soLuongBan, 0 as doanhThu, " +
                     "sp.GiaBan, sp.TonKho, sp.TrangThaiKinhDoanh as trangThai " +
                     "FROM sanpham sp " +
                     "LEFT JOIN danhmuc dm ON sp.MaDanhMuc = dm.MaDanhMuc " +
                     "WHERE sp.TrangThaiKinhDoanh = 'DANG_BAN' " +
                     "AND sp.TonKho <= ? " +
                     "ORDER BY sp.TonKho ASC";
        
        try (Connection cons = ConnectDB.getConnection();
             PreparedStatement ps = cons.prepareStatement(sql)) {
            
            ps.setInt(1, nguongTonKho);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                ThongKeSanPham tk = new ThongKeSanPham();
                tk.setMaSP(rs.getString("MaSP"));
                tk.setTenSP(rs.getString("TenSP"));
                tk.setDanhMuc(rs.getString("danhMuc"));
                tk.setSoLuongBan(rs.getInt("soLuongBan"));
                tk.setDoanhThu(rs.getDouble("doanhThu"));
                tk.setGiaBan(rs.getDouble("GiaBan"));
                tk.setTonKho(rs.getInt("TonKho"));
                tk.setTrangThai(rs.getString("trangThai"));
                list.add(tk);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Thống kê doanh thu theo danh mục sản phẩm
     * @param ngayBatDau ngày bắt đầu
     * @param ngayKetThuc ngày kết thúc
     * @return danh sách thống kê doanh thu theo danh mục
     */
    public List<ThongKeSanPham> getDoanhThuTheoDanhMuc(String ngayBatDau, String ngayKetThuc) {
        List<ThongKeSanPham> list = new ArrayList<>();
        String sql = "SELECT dm.MaDanhMuc as maSP, dm.TenDanhMuc as tenSP, dm.TenDanhMuc as danhMuc, " +
                     "SUM(ctd.SoLuong) as soLuongBan, " +
                     "SUM(ctd.ThanhTien) as doanhThu, " +
                     "0 as giaBan, 0 as tonKho, 'ACTIVE' as trangThai " +
                     "FROM chitiet_donhang ctd " +
                     "INNER JOIN donhang dh ON ctd.MaDonHang = dh.MaDonHang " +
                     "INNER JOIN hoadon h ON dh.MaDonHang = h.MaDonHang " +
                     "INNER JOIN sanpham sp ON ctd.MaSP = sp.MaSP " +
                     "INNER JOIN danhmuc dm ON sp.MaDanhMuc = dm.MaDanhMuc " +
                     "WHERE h.TrangThai = 'DA_THANH_TOAN' " +
                     "AND DATE(h.NgayLap) BETWEEN ? AND ? " +
                     "GROUP BY dm.MaDanhMuc, dm.TenDanhMuc " +
                     "ORDER BY doanhThu DESC";
        
        try (Connection cons = ConnectDB.getConnection();
             PreparedStatement ps = cons.prepareStatement(sql)) {
            
            ps.setString(1, ngayBatDau);
            ps.setString(2, ngayKetThuc);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                ThongKeSanPham tk = new ThongKeSanPham();
                tk.setMaSP(rs.getString("maSP"));
                tk.setTenSP(rs.getString("tenSP"));
                tk.setDanhMuc(rs.getString("danhMuc"));
                tk.setSoLuongBan(rs.getInt("soLuongBan"));
                tk.setDoanhThu(rs.getDouble("doanhThu"));
                tk.setGiaBan(rs.getDouble("giaBan"));
                tk.setTonKho(rs.getInt("tonKho"));
                tk.setTrangThai(rs.getString("trangThai"));
                list.add(tk);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
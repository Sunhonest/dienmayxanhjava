package model.ThongKe;

import domain.ThongKeDoanhThu;
import model.ConnectDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ThongKeDoanhThuDAO {

    /**
     * Thống kê doanh thu theo ngày
     * @param ngayBatDau ngày bắt đầu (yyyy-mm-dd)
     * @param ngayKetThuc ngày kết thúc (yyyy-mm-dd)
     * @return danh sách thống kê theo từng ngày
     */
    public List<ThongKeDoanhThu> thongKeTheoNgay(String ngayBatDau, String ngayKetThuc) {
        List<ThongKeDoanhThu> list = new ArrayList<>();
        String sql = "SELECT DATE(h.NgayLap) as ngay, " +
                     "SUM(CASE WHEN h.TrangThai = 'DA_THANH_TOAN' THEN h.TongTienHang ELSE 0 END) as tongDoanhThu, " +
                     "SUM(CASE WHEN h.TrangThai = 'DA_THANH_TOAN' THEN h.TienGiam ELSE 0 END) as tongGiamGia, " +
                     "SUM(CASE WHEN h.TrangThai = 'DA_THANH_TOAN' THEN h.TongThanhToan ELSE 0 END) as doanhThuThucTe, " +
                     "COUNT(CASE WHEN h.TrangThai = 'DA_THANH_TOAN' THEN 1 END) as soDonHang, " +
                     "COUNT(CASE WHEN h.TrangThai = 'HUY' THEN 1 END) as soDonHangHuy " +
                     "FROM hoadon h " +
                     "WHERE DATE(h.NgayLap) BETWEEN ? AND ? " +
                     "GROUP BY DATE(h.NgayLap) " +
                     "ORDER BY DATE(h.NgayLap)";
        
        try (Connection cons = ConnectDB.getConnection();
             PreparedStatement ps = cons.prepareStatement(sql)) {
            
            ps.setString(1, ngayBatDau);
            ps.setString(2, ngayKetThuc);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                ThongKeDoanhThu tk = new ThongKeDoanhThu();
                tk.setKhoanThoiGian(rs.getString("ngay"));
                tk.setTongDoanhThu(rs.getDouble("tongDoanhThu"));
                tk.setTongGiamGia(rs.getDouble("tongGiamGia"));
                tk.setDoanhThuThucTe(rs.getDouble("doanhThuThucTe"));
                tk.setSoDonHang(rs.getInt("soDonHang"));
                tk.setSoDonHangHuy(rs.getInt("soDonHangHuy"));
                list.add(tk);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Thống kê doanh thu theo tháng
     * @param nam năm thống kê
     * @return danh sách thống kê theo từng tháng trong năm
     */
    public List<ThongKeDoanhThu> thongKeTheoThang(int nam) {
        List<ThongKeDoanhThu> list = new ArrayList<>();
        String sql = "SELECT MONTH(h.NgayLap) as thang, " +
                     "SUM(CASE WHEN h.TrangThai = 'DA_THANH_TOAN' THEN h.TongTienHang ELSE 0 END) as tongDoanhThu, " +
                     "SUM(CASE WHEN h.TrangThai = 'DA_THANH_TOAN' THEN h.TienGiam ELSE 0 END) as tongGiamGia, " +
                     "SUM(CASE WHEN h.TrangThai = 'DA_THANH_TOAN' THEN h.TongThanhToan ELSE 0 END) as doanhThuThucTe, " +
                     "COUNT(CASE WHEN h.TrangThai = 'DA_THANH_TOAN' THEN 1 END) as soDonHang, " +
                     "COUNT(CASE WHEN h.TrangThai = 'HUY' THEN 1 END) as soDonHangHuy " +
                     "FROM hoadon h " +
                     "WHERE YEAR(h.NgayLap) = ? " +
                     "GROUP BY MONTH(h.NgayLap) " +
                     "ORDER BY MONTH(h.NgayLap)";
        
        try (Connection cons = ConnectDB.getConnection();
             PreparedStatement ps = cons.prepareStatement(sql)) {
            
            ps.setInt(1, nam);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                ThongKeDoanhThu tk = new ThongKeDoanhThu();
                tk.setKhoanThoiGian("Tháng " + rs.getInt("thang") + "/" + nam);
                tk.setTongDoanhThu(rs.getDouble("tongDoanhThu"));
                tk.setTongGiamGia(rs.getDouble("tongGiamGia"));
                tk.setDoanhThuThucTe(rs.getDouble("doanhThuThucTe"));
                tk.setSoDonHang(rs.getInt("soDonHang"));
                tk.setSoDonHangHuy(rs.getInt("soDonHangHuy"));
                list.add(tk);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Thống kê doanh thu theo năm
     * @param namBatDau năm bắt đầu
     * @param namKetThuc năm kết thúc
     * @return danh sách thống kê theo từng năm
     */
    public List<ThongKeDoanhThu> thongKeTheoNam(int namBatDau, int namKetThuc) {
        List<ThongKeDoanhThu> list = new ArrayList<>();
        String sql = "SELECT YEAR(h.NgayLap) as nam, " +
                     "SUM(CASE WHEN h.TrangThai = 'DA_THANH_TOAN' THEN h.TongTienHang ELSE 0 END) as tongDoanhThu, " +
                     "SUM(CASE WHEN h.TrangThai = 'DA_THANH_TOAN' THEN h.TienGiam ELSE 0 END) as tongGiamGia, " +
                     "SUM(CASE WHEN h.TrangThai = 'DA_THANH_TOAN' THEN h.TongThanhToan ELSE 0 END) as doanhThuThucTe, " +
                     "COUNT(CASE WHEN h.TrangThai = 'DA_THANH_TOAN' THEN 1 END) as soDonHang, " +
                     "COUNT(CASE WHEN h.TrangThai = 'HUY' THEN 1 END) as soDonHangHuy " +
                     "FROM hoadon h " +
                     "WHERE YEAR(h.NgayLap) BETWEEN ? AND ? " +
                     "GROUP BY YEAR(h.NgayLap) " +
                     "ORDER BY YEAR(h.NgayLap)";
        
        try (Connection cons = ConnectDB.getConnection();
             PreparedStatement ps = cons.prepareStatement(sql)) {
            
            ps.setInt(1, namBatDau);
            ps.setInt(2, namKetThuc);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                ThongKeDoanhThu tk = new ThongKeDoanhThu();
                tk.setKhoanThoiGian("Năm " + rs.getInt("nam"));
                tk.setTongDoanhThu(rs.getDouble("tongDoanhThu"));
                tk.setTongGiamGia(rs.getDouble("tongGiamGia"));
                tk.setDoanhThuThucTe(rs.getDouble("doanhThuThucTe"));
                tk.setSoDonHang(rs.getInt("soDonHang"));
                tk.setSoDonHangHuy(rs.getInt("soDonHangHuy"));
                list.add(tk);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Thống kê tổng doanh thu trong khoảng thời gian
     * @param ngayBatDau ngày bắt đầu
     * @param ngayKetThuc ngày kết thúc
     * @return thống kê tổng hợp
     */
    public ThongKeDoanhThu thongKeTongHop(String ngayBatDau, String ngayKetThuc) {
        String sql = "SELECT " +
                     "SUM(CASE WHEN h.TrangThai = 'DA_THANH_TOAN' THEN h.TongTienHang ELSE 0 END) as tongDoanhThu, " +
                     "SUM(CASE WHEN h.TrangThai = 'DA_THANH_TOAN' THEN h.TienGiam ELSE 0 END) as tongGiamGia, " +
                     "SUM(CASE WHEN h.TrangThai = 'DA_THANH_TOAN' THEN h.TongThanhToan ELSE 0 END) as doanhThuThucTe, " +
                     "COUNT(CASE WHEN h.TrangThai = 'DA_THANH_TOAN' THEN 1 END) as soDonHang, " +
                     "COUNT(CASE WHEN h.TrangThai = 'HUY' THEN 1 END) as soDonHangHuy " +
                     "FROM hoadon h " +
                     "WHERE DATE(h.NgayLap) BETWEEN ? AND ?";
        
        try (Connection cons = ConnectDB.getConnection();
             PreparedStatement ps = cons.prepareStatement(sql)) {
            
            ps.setString(1, ngayBatDau);
            ps.setString(2, ngayKetThuc);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                ThongKeDoanhThu tk = new ThongKeDoanhThu();
                tk.setKhoanThoiGian(ngayBatDau + " đến " + ngayKetThuc);
                tk.setTongDoanhThu(rs.getDouble("tongDoanhThu"));
                tk.setTongGiamGia(rs.getDouble("tongGiamGia"));
                tk.setDoanhThuThucTe(rs.getDouble("doanhThuThucTe"));
                tk.setSoDonHang(rs.getInt("soDonHang"));
                tk.setSoDonHangHuy(rs.getInt("soDonHangHuy"));
                return tk;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Thống kê doanh thu theo phương thức thanh toán
     * @param ngayBatDau ngày bắt đầu
     * @param ngayKetThuc ngày kết thúc
     * @return danh sách thống kê theo phương thức thanh toán
     */
    public List<ThongKeDoanhThu> thongKeTheoPhuongThucThanhToan(String ngayBatDau, String ngayKetThuc) {
        List<ThongKeDoanhThu> list = new ArrayList<>();
        String sql = "SELECT h.PhuongThucTT as phuongThuc, " +
                     "SUM(CASE WHEN h.TrangThai = 'DA_THANH_TOAN' THEN h.TongTienHang ELSE 0 END) as tongDoanhThu, " +
                     "SUM(CASE WHEN h.TrangThai = 'DA_THANH_TOAN' THEN h.TienGiam ELSE 0 END) as tongGiamGia, " +
                     "SUM(CASE WHEN h.TrangThai = 'DA_THANH_TOAN' THEN h.TongThanhToan ELSE 0 END) as doanhThuThucTe, " +
                     "COUNT(CASE WHEN h.TrangThai = 'DA_THANH_TOAN' THEN 1 END) as soDonHang, " +
                     "COUNT(CASE WHEN h.TrangThai = 'HUY' THEN 1 END) as soDonHangHuy " +
                     "FROM hoadon h " +
                     "WHERE DATE(h.NgayLap) BETWEEN ? AND ? " +
                     "GROUP BY h.PhuongThucTT " +
                     "ORDER BY doanhThuThucTe DESC";
        
        try (Connection cons = ConnectDB.getConnection();
             PreparedStatement ps = cons.prepareStatement(sql)) {
            
            ps.setString(1, ngayBatDau);
            ps.setString(2, ngayKetThuc);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                ThongKeDoanhThu tk = new ThongKeDoanhThu();
                tk.setKhoanThoiGian(rs.getString("phuongThuc"));
                tk.setTongDoanhThu(rs.getDouble("tongDoanhThu"));
                tk.setTongGiamGia(rs.getDouble("tongGiamGia"));
                tk.setDoanhThuThucTe(rs.getDouble("doanhThuThucTe"));
                tk.setSoDonHang(rs.getInt("soDonHang"));
                tk.setSoDonHangHuy(rs.getInt("soDonHangHuy"));
                list.add(tk);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
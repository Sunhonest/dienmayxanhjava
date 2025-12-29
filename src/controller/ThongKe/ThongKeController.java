package controller.ThongKe;

import domain.ThongKeDoanhThu;
import domain.ThongKeSanPham;
import model.ThongKe.ThongKeDoanhThuDAO;
import model.ThongKe.ThongKeSanPhamDAO;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Controller cho thống kê
 * @author nguye
 */
public class ThongKeController {
    private ThongKeDoanhThuDAO doanhThuDAO;
    private ThongKeSanPhamDAO sanPhamDAO;

    public ThongKeController() {
        this.doanhThuDAO = new ThongKeDoanhThuDAO();
        this.sanPhamDAO = new ThongKeSanPhamDAO();
    }

    // ===== THỐNG KÊ DOANH THU =====

    /**
     * Thống kê doanh thu theo ngày
     * @param ngayBatDau ngày bắt đầu
     * @param ngayKetThuc ngày kết thúc
     * @return danh sách thống kê
     */
    public List<ThongKeDoanhThu> thongKeDoanhThuTheoNgay(Date ngayBatDau, Date ngayKetThuc) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String start = sdf.format(ngayBatDau);
            String end = sdf.format(ngayKetThuc);
            return doanhThuDAO.thongKeTheoNgay(start, end);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Có lỗi xảy ra khi thống kê doanh thu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    /**
     * Thống kê doanh thu theo tháng trong năm
     * @param nam năm thống kê
     * @return danh sách thống kê
     */
    public List<ThongKeDoanhThu> thongKeDoanhThuTheoThang(int nam) {
        try {
            return doanhThuDAO.thongKeTheoThang(nam);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Có lỗi xảy ra khi thống kê doanh thu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    /**
     * Thống kê doanh thu theo năm
     * @param namBatDau năm bắt đầu
     * @param namKetThuc năm kết thúc
     * @return danh sách thống kê
     */
    public List<ThongKeDoanhThu> thongKeDoanhThuTheoNam(int namBatDau, int namKetThuc) {
        try {
            return doanhThuDAO.thongKeTheoNam(namBatDau, namKetThuc);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Có lỗi xảy ra khi thống kê doanh thu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    /**
     * Thống kê tổng doanh thu trong khoảng thời gian
     * @param ngayBatDau ngày bắt đầu
     * @param ngayKetThuc ngày kết thúc
     * @return thống kê tổng hợp
     */
    public ThongKeDoanhThu thongKeTongDoanhThu(Date ngayBatDau, Date ngayKetThuc) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String start = sdf.format(ngayBatDau);
            String end = sdf.format(ngayKetThuc);
            return doanhThuDAO.thongKeTongHop(start, end);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Có lỗi xảy ra khi thống kê doanh thu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    /**
     * Thống kê doanh thu theo phương thức thanh toán
     * @param ngayBatDau ngày bắt đầu
     * @param ngayKetThuc ngày kết thúc
     * @return danh sách thống kê
     */
    public List<ThongKeDoanhThu> thongKeTheoPhuongThucThanhToan(Date ngayBatDau, Date ngayKetThuc) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String start = sdf.format(ngayBatDau);
            String end = sdf.format(ngayKetThuc);
            return doanhThuDAO.thongKeTheoPhuongThucThanhToan(start, end);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Có lỗi xảy ra khi thống kê doanh thu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    // ===== THỐNG KÊ SẢN PHẨM =====

    /**
     * Thống kê sản phẩm bán chạy nhất
     * @param ngayBatDau ngày bắt đầu
     * @param ngayKetThuc ngày kết thúc
     * @param limit số lượng sản phẩm top
     * @return danh sách sản phẩm bán chạy
     */
    public List<ThongKeSanPham> thongKeSanPhamBanChay(Date ngayBatDau, Date ngayKetThuc, int limit) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String start = sdf.format(ngayBatDau);
            String end = sdf.format(ngayKetThuc);
            return sanPhamDAO.getSanPhamBanChay(start, end, limit);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Có lỗi xảy ra khi thống kê sản phẩm: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    /**
     * Thống kê sản phẩm bán chậm nhất
     * @param ngayBatDau ngày bắt đầu
     * @param ngayKetThuc ngày kết thúc
     * @param limit số lượng sản phẩm
     * @return danh sách sản phẩm bán chậm
     */
    public List<ThongKeSanPham> thongKeSanPhamBanCham(Date ngayBatDau, Date ngayKetThuc, int limit) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String start = sdf.format(ngayBatDau);
            String end = sdf.format(ngayKetThuc);
            return sanPhamDAO.getSanPhamBanCham(start, end, limit);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Có lỗi xảy ra khi thống kê sản phẩm: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    /**
     * Thống kê sản phẩm theo danh mục
     * @param maDanhMuc mã danh mục (null để lấy tất cả)
     * @param ngayBatDau ngày bắt đầu
     * @param ngayKetThuc ngày kết thúc
     * @return danh sách sản phẩm theo danh mục
     */
    public List<ThongKeSanPham> thongKeSanPhamTheoDanhMuc(String maDanhMuc, Date ngayBatDau, Date ngayKetThuc) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String start = sdf.format(ngayBatDau);
            String end = sdf.format(ngayKetThuc);
            return sanPhamDAO.getSanPhamTheoDanhMuc(maDanhMuc, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Có lỗi xảy ra khi thống kê sản phẩm: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    /**
     * Thống kê sản phẩm sắp hết hàng
     * @param nguongTonKho ngưỡng tồn kho
     * @return danh sách sản phẩm sắp hết hàng
     */
    public List<ThongKeSanPham> thongKeSanPhamSapHetHang(int nguongTonKho) {
        try {
            return sanPhamDAO.getSanPhamSapHetHang(nguongTonKho);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Có lỗi xảy ra khi thống kê sản phẩm: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    /**
     * Thống kê doanh thu theo danh mục sản phẩm
     * @param ngayBatDau ngày bắt đầu
     * @param ngayKetThuc ngày kết thúc
     * @return danh sách doanh thu theo danh mục
     */
    public List<ThongKeSanPham> thongKeDoanhThuTheoDanhMuc(Date ngayBatDau, Date ngayKetThuc) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String start = sdf.format(ngayBatDau);
            String end = sdf.format(ngayKetThuc);
            return sanPhamDAO.getDoanhThuTheoDanhMuc(start, end);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Có lỗi xảy ra khi thống kê doanh thu theo danh mục: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    // ===== HELPER METHODS =====

    /**
     * Lấy ngày đầu tháng hiện tại
     * @return ngày đầu tháng
     */
    public Date getDauThangHienTai() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * Lấy ngày cuối tháng hiện tại
     * @return ngày cuối tháng
     */
    public Date getCuoiThangHienTai() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }

    /**
     * Lấy ngày đầu năm hiện tại
     * @return ngày đầu năm
     */
    public Date getDauNamHienTai() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_YEAR, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * Lấy ngày cuối năm hiện tại
     * @return ngày cuối năm
     */
    public Date getCuoiNamHienTai() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_YEAR, cal.getActualMaximum(Calendar.DAY_OF_YEAR));
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }

    /**
     * Lấy 7 ngày trước
     * @return ngày 7 ngày trước
     */
    public Date get7NgayTruoc() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -7);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * Lấy 30 ngày trước
     * @return ngày 30 ngày trước
     */
    public Date get30NgayTruoc() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -30);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * Validate khoảng thời gian
     * @param ngayBatDau ngày bắt đầu
     * @param ngayKetThuc ngày kết thúc
     * @return true nếu hợp lệ
     */
    public boolean validateTimeRange(Date ngayBatDau, Date ngayKetThuc) {
        if (ngayBatDau == null || ngayKetThuc == null) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn ngày bắt đầu và ngày kết thúc!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (ngayBatDau.after(ngayKetThuc)) {
            JOptionPane.showMessageDialog(null, "Ngày bắt đầu phải trước ngày kết thúc!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        Date today = new Date();
        if (ngayBatDau.after(today)) {
            JOptionPane.showMessageDialog(null, "Ngày bắt đầu không được lớn hơn ngày hiện tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }
}
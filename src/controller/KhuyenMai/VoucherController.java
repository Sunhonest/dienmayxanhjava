package controller.KhuyenMai;

import domain.Voucher;
import model.KhuyenMai.VoucherDAO;

import javax.swing.*;
import java.util.Date;
import java.util.List;

/**
 * Controller cho quản lý voucher
 * @author nguye
 */
public class VoucherController {
    private VoucherDAO voucherDAO;

    public VoucherController() {
        this.voucherDAO = new VoucherDAO();
    }

    /**
     * Lấy tất cả voucher
     * @return danh sách voucher
     */
    public List<Voucher> getAllVouchers() {
        return voucherDAO.getAll();
    }

    /**
     * Lấy voucher theo ID
     * @param id ID của voucher
     * @return voucher hoặc null
     */
    public Voucher getVoucherById(int id) {
        return voucherDAO.getById(id);
    }

    /**
     * Lấy voucher theo mã
     * @param maVoucher mã voucher
     * @return voucher hoặc null
     */
    public Voucher getVoucherByMa(String maVoucher) {
        return voucherDAO.getByMaVoucher(maVoucher);
    }

    /**
     * Thêm voucher mới
     * @param voucher voucher cần thêm
     * @return true nếu thành công
     */
    public boolean addVoucher(Voucher voucher) {
        try {
            // Kiểm tra dữ liệu hợp lệ
            String validationResult = validateVoucher(voucher, true);
            if (!validationResult.equals("OK")) {
                JOptionPane.showMessageDialog(null, validationResult, "Lỗi dữ liệu", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            // Kiểm tra mã voucher trùng
            if (voucherDAO.isMaVoucherExists(voucher.getMaVoucher())) {
                JOptionPane.showMessageDialog(null, "Mã voucher đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            int result = voucherDAO.insert(voucher);
            if (result > 0) {
                JOptionPane.showMessageDialog(null, "Thêm voucher thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Thêm voucher thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Có lỗi xảy ra: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * Cập nhật voucher
     * @param voucher voucher cần cập nhật
     * @return true nếu thành công
     */
    public boolean updateVoucher(Voucher voucher) {
        try {
            // Kiểm tra dữ liệu hợp lệ
            String validationResult = validateVoucher(voucher, false);
            if (!validationResult.equals("OK")) {
                JOptionPane.showMessageDialog(null, validationResult, "Lỗi dữ liệu", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            int result = voucherDAO.update(voucher);
            if (result > 0) {
                JOptionPane.showMessageDialog(null, "Cập nhật voucher thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Cập nhật voucher thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Có lỗi xảy ra: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * Xóa voucher
     * @param voucherID ID của voucher cần xóa
     * @return true nếu thành công
     */
    public boolean deleteVoucher(int voucherID) {
        try {
            int choice = JOptionPane.showConfirmDialog(null, 
                "Bạn có chắc chắn muốn xóa voucher này?\n" +
                "Lưu ý: Nếu voucher đã được sử dụng, hệ thống sẽ chuyển trạng thái thành 'NGUNG' thay vì xóa hoàn toàn.", 
                "Xác nhận xóa", 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.QUESTION_MESSAGE);

            if (choice == JOptionPane.YES_OPTION) {
                int result = voucherDAO.delete(voucherID);
                if (result > 0) {
                    JOptionPane.showMessageDialog(null, "Xóa voucher thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    return true;
                } else {
                    JOptionPane.showMessageDialog(null, "Xóa voucher thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Có lỗi xảy ra: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * Lấy danh sách voucher đang hoạt động
     * @return danh sách voucher có thể sử dụng
     */
    public List<Voucher> getActiveVouchers() {
        return voucherDAO.getActiveVouchers();
    }

    /**
     * Áp dụng voucher cho đơn hàng
     * @param maVoucher mã voucher
     * @param tongTienDonHang tổng tiền đơn hàng
     * @return số tiền được giảm, -1 nếu không áp dụng được
     */
    public double applyVoucher(String maVoucher, double tongTienDonHang) {
        try {
            Voucher voucher = voucherDAO.getByMaVoucher(maVoucher);
            if (voucher == null) {
                JOptionPane.showMessageDialog(null, "Mã voucher không tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return -1;
            }

            // Kiểm tra voucher có còn hiệu lực không
            Date now = new Date();
            if (now.before(voucher.getNgayBatDau()) || now.after(voucher.getNgayKetThuc())) {
                JOptionPane.showMessageDialog(null, "Voucher đã hết hạn hoặc chưa được kích hoạt!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return -1;
            }

            // Kiểm tra trạng thái voucher
            if (!"KICH_HOAT".equals(voucher.getTrangThai())) {
                JOptionPane.showMessageDialog(null, "Voucher không còn hoạt động!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return -1;
            }

            // Kiểm tra số lượng voucher
            if (voucher.getSoLuong() <= 0) {
                JOptionPane.showMessageDialog(null, "Voucher đã hết lượt sử dụng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return -1;
            }

            // Kiểm tra điều kiện đơn hàng tối thiểu
            if (tongTienDonHang < voucher.getDonHangToiThieu()) {
                JOptionPane.showMessageDialog(null, 
                    String.format("Đơn hàng phải có giá trị tối thiểu %.0f VNĐ để sử dụng voucher này!", voucher.getDonHangToiThieu()), 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
                return -1;
            }

            // Tính toán số tiền giảm
            double tienGiam = 0;
            if ("PHAN_TRAM".equals(voucher.getLoaiGiam())) {
                tienGiam = tongTienDonHang * voucher.getGiaTriGiam() / 100;
                if (tienGiam > voucher.getGiamToiDa()) {
                    tienGiam = voucher.getGiamToiDa();
                }
            } else if ("TIEN_MAT".equals(voucher.getLoaiGiam())) {
                tienGiam = voucher.getGiaTriGiam();
                if (tienGiam > tongTienDonHang) {
                    tienGiam = tongTienDonHang;
                }
            }

            return tienGiam;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Có lỗi xảy ra khi áp dụng voucher: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            return -1;
        }
    }

    /**
     * Sử dụng voucher (giảm số lượng)
     * @param voucherID ID voucher
     * @return true nếu thành công
     */
    public boolean useVoucher(int voucherID) {
        return voucherDAO.decreaseVoucherQuantity(voucherID) > 0;
    }

    /**
     * Validate dữ liệu voucher
     * @param voucher voucher cần kiểm tra
     * @param isNew có phải voucher mới không
     * @return "OK" nếu hợp lệ, thông báo lỗi nếu không hợp lệ
     */
    private String validateVoucher(Voucher voucher, boolean isNew) {
        if (voucher.getMaVoucher() == null || voucher.getMaVoucher().trim().isEmpty()) {
            return "Mã voucher không được để trống!";
        }

        if (voucher.getTenVoucher() == null || voucher.getTenVoucher().trim().isEmpty()) {
            return "Tên voucher không được để trống!";
        }

        if (voucher.getLoaiGiam() == null || (!voucher.getLoaiGiam().equals("PHAN_TRAM") && !voucher.getLoaiGiam().equals("TIEN_MAT"))) {
            return "Loại giảm giá không hợp lệ!";
        }

        if (voucher.getGiaTriGiam() <= 0) {
            return "Giá trị giảm phải lớn hơn 0!";
        }

        if ("PHAN_TRAM".equals(voucher.getLoaiGiam()) && voucher.getGiaTriGiam() > 100) {
            return "Giá trị giảm theo phần trăm không được vượt quá 100%!";
        }

        if (voucher.getDonHangToiThieu() < 0) {
            return "Đơn hàng tối thiểu không được âm!";
        }

        if (voucher.getNgayBatDau() == null || voucher.getNgayKetThuc() == null) {
            return "Ngày bắt đầu và ngày kết thúc không được để trống!";
        }

        if (voucher.getNgayBatDau().after(voucher.getNgayKetThuc())) {
            return "Ngày bắt đầu phải trước ngày kết thúc!";
        }

        if (voucher.getSoLuong() < 0) {
            return "Số lượng voucher không được âm!";
        }

        if (voucher.getTrangThai() == null || 
            (!voucher.getTrangThai().equals("NHAP") && 
             !voucher.getTrangThai().equals("KICH_HOAT") && 
             !voucher.getTrangThai().equals("NGUNG"))) {
            return "Trạng thái voucher không hợp lệ!";
        }

        return "OK";
    }
}
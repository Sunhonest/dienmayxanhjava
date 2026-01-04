/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller.QLBH;

import domain.QLBH.QLDH;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import model.QLBH.DonHangDAO;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import view.viewQLBH.QuanLyDonHang;

/**
 *
 * @author Admin
 */
public class QLDHController implements ActionListener {

    private final QuanLyDonHang view;
    private final DonHangDAO dao;

    public QLDHController(QuanLyDonHang view) {
        this.view = view;
        this.dao = new DonHangDAO();

        this.view.addActionListener(this);
        this.view.getTable().addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                view.fillFormTuBang();
                // khi click bảng: vẫn đảm bảo hiển thị đúng giảm giá + giá thanh toán
                recalcDiscountAndPay();
            }
        });

        loadComboBoxData();
        loadData(dao.getAll());

        bindEvents();

        // set mặc định theo MaDonHang đầu tiên
        fillThanhTienFromCTDH();
        recalcDiscountAndPay();
    }

    private void bindEvents() {
        // ✅ đổi MaDonHang => tự SUM thành tiền từ CTDH + tính lại giảm giá
        view.getCboMaDonHang().addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                fillThanhTienFromCTDH();
                recalcDiscountAndPay();
            }
        });

        // ✅ đổi Voucher => tính lại giảm giá
        view.getCboVoucherID().addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                recalcDiscountAndPay();
            }
        });

        // ✅ FIX: đổi Trạng thái => trước đây hay bị “mất giảm giá”
        // Giờ cứ đổi trạng thái là recalc lại (KHÔNG reset về 0)
        view.getCboTrangThai().addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                recalcDiscountAndPay();
            }
        });
    }

    private void loadData(List<QLDH> list) {
        DefaultTableModel model = view.getModel();
        model.setRowCount(0);

        DecimalFormat df = new DecimalFormat("#,##0.00");
        for (QLDH dh : list) {
            model.addRow(new Object[]{
                dh.getId(),
                dh.getMaKH(),
                dh.getMaDonHang(),
                dh.getNgayTao(),
                df.format(dh.getThanhTien()),
                df.format(dh.getTienGiam()),
                df.format(dh.getGiaThanhToan()),
                dh.getVoucherID(),
                dh.getTrangThai(),
                dh.getMaNV()
            });
        }
    }

    private void loadComboBoxData() {
        // ✅ MaDonHang lấy từ CTDH
        view.getCboMaDonHang().removeAllItems();
        for (String maDH : dao.getAllMaDonHangFromCTDH()) {
            view.getCboMaDonHang().addItem(maDH);
        }

        view.getCboMaKH().removeAllItems();
        for (String maKH : dao.getAllMaKH()) view.getCboMaKH().addItem(maKH);

        view.getCboMaNV().removeAllItems();
        for (String maNV : dao.getAllMaNV()) view.getCboMaNV().addItem(maNV);

        view.getCboVoucherID().removeAllItems();
        view.getCboVoucherID().addItem("0");
        for (String vid : dao.getAllVoucherIDActive()) view.getCboVoucherID().addItem(vid);

        if (view.getCboMaDonHang().getItemCount() > 0) {
            view.getCboMaDonHang().setSelectedIndex(0);
        }
    }

    private void fillThanhTienFromCTDH() {
        String maDH = String.valueOf(view.getCboMaDonHang().getSelectedItem());
        if (maDH == null || maDH.isBlank()) {
            view.setThanhTien(0.0);
            return;
        }
        float tong = dao.getThanhTienFromCTDH(maDH);
        view.setThanhTien(tong);
    }

    private void recalcDiscountAndPay() {
        try {
            String v = String.valueOf(view.getCboVoucherID().getSelectedItem());
            int voucherId = (v == null || v.isBlank()) ? 0 : Integer.parseInt(v);

            double thanhTien = view.getThanhTienValue();
            double tienGiam = 0.0;

            if (voucherId > 0) {
                tienGiam = dao.tinhTienGiamTheoVoucher(voucherId, thanhTien);
            }

            view.setTienGiam(tienGiam); // setTienGiam đã tự cập nhật giá thanh toán

        } catch (Exception ex) {
            view.setTienGiam(0.0);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        try {
            switch (cmd) {
                case "Them": xuLyThem(); break;
                case "Sua": xuLySua(); break;
                case "Xoa": xuLyXoa(); break;
                case "LamMoi":
                    view.resetForm();
                    loadComboBoxData();
                    loadData(dao.getAll());
                    fillThanhTienFromCTDH();
                    recalcDiscountAndPay();
                    break;
                case "Tim":
                    loadData(dao.search(view.getKeyword()));
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view, "Lỗi: " + ex.getMessage());
        }
    }

    private void xuLyThem() {
        QLDH dh = view.getDonHangFromInput();

        if (dh.getMaDonHang() == null || dh.getMaDonHang().isBlank()) {
            JOptionPane.showMessageDialog(view, "Chưa chọn mã đơn hàng!");
            return;
        }

        if (dao.checkTrungMa(dh.getMaDonHang())) {
            JOptionPane.showMessageDialog(view, "Mã đơn hàng đã tồn tại!");
            return;
        }

        // ✅ ép đúng Thành tiền theo CTDH
        float thanhTien = dao.getThanhTienFromCTDH(dh.getMaDonHang());
        dh.setThanhTien(thanhTien);

        // ✅ tính lại giảm giá + giá thanh toán
        double tienGiam = (dh.getVoucherID() > 0)
                ? dao.tinhTienGiamTheoVoucher(dh.getVoucherID(), thanhTien)
                : 0.0;

        dh.setTienGiam((float) tienGiam);
        dh.setGiaThanhToan((float) Math.max(0, thanhTien - tienGiam));

        if (dh.getNgayTao() == null) dh.setNgayTao(new Date());

        if (dao.insert(dh) > 0) {
            JOptionPane.showMessageDialog(view, "Thêm thành công!");
            loadData(dao.getAll());
            view.resetForm();
            loadComboBoxData();
            fillThanhTienFromCTDH();
            recalcDiscountAndPay();
        }
    }

    private void xuLySua() {
        QLDH dh = view.getDonHangFromInput();

        if (dh.getMaDonHang() == null || dh.getMaDonHang().isBlank()) {
            JOptionPane.showMessageDialog(view, "Chưa chọn mã đơn hàng!");
            return;
        }

        // ✅ ép đúng Thành tiền theo CTDH
        float thanhTien = dao.getThanhTienFromCTDH(dh.getMaDonHang());
        dh.setThanhTien(thanhTien);

        double tienGiam = (dh.getVoucherID() > 0)
                ? dao.tinhTienGiamTheoVoucher(dh.getVoucherID(), thanhTien)
                : 0.0;

        dh.setTienGiam((float) tienGiam);
        dh.setGiaThanhToan((float) Math.max(0, thanhTien - tienGiam));

        if (dao.update(dh) > 0) {
            JOptionPane.showMessageDialog(view, "Cập nhật thành công!");
            loadData(dao.getAll());
            view.resetForm();
            loadComboBoxData();
            fillThanhTienFromCTDH();
            recalcDiscountAndPay();
        }
    }

    private void xuLyXoa() {
        String maDH = view.getMaDonHangDangChon();
        if (maDH == null) return;

        int cf = JOptionPane.showConfirmDialog(view,
                "Xóa đơn hàng: " + maDH + " ?",
                "Xác nhận", JOptionPane.YES_NO_OPTION);

        if (cf == JOptionPane.YES_OPTION) {
            if (dao.delete(maDH) > 0) {
                JOptionPane.showMessageDialog(view, "Đã xóa!");
                loadData(dao.getAll());
                view.resetForm();
                loadComboBoxData();
                fillThanhTienFromCTDH();
                recalcDiscountAndPay();
            }
        }
    }
}

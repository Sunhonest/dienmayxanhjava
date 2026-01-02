/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller.QLBH;

import domain.QLBH.QLDH;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.QLBH.DonHangDAO;
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
            @Override
            public void mouseClicked(MouseEvent e) {
                view.fillFormTuBang();
            }
        });
        loadComboBoxData();
        loadData();
        
    }

    private void loadData() {
        DefaultTableModel model = view.getModel();
        model.setRowCount(0);

        List<QLDH> list = dao.getAll();
        DecimalFormat df = new DecimalFormat("#,##0.00");

        for (QLDH dh : list) {
            model.addRow(new Object[]{
                dh.getMaKH(),
                dh.getMaDonHang(),
                dh.getNgayTao(),
                df.format(dh.getTongTien()),
                df.format(dh.getTienGiam()),
                dh.getVoucherID(),
                dh.getTrangThai(),
                dh.getMaNV()
            });
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
                    loadData();
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view, "Lỗi: " + ex.getMessage());
        }
    }

    private void xuLyThem() {
        QLDH dh = view.getDonHangFromInput();

        if (dh.getMaDonHang() == null || dh.getMaDonHang().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Chưa nhập Mã đơn hàng!");
            return;
        }
        if (dao.checkTrungMa(dh.getMaDonHang())) {
            JOptionPane.showMessageDialog(view, "Mã đơn hàng đã tồn tại!");
            return;
        }
         // 1) Tính tiền giảm từ bảng voucher theo VoucherID
            double tongTien = dh.getTongTien(); // nếu QLDH đang float thì cast sang double
            int voucherId = dh.getVoucherID();

            double tienGiam = 0.0;
            if (voucherId > 0) {
                tienGiam = dao.tinhTienGiamTheoVoucher(voucherId, tongTien);
            }

        // 2) Set lại tiền giảm cho đúng trước khi insert DB
        dh.setTienGiam((float) tienGiam); // hoặc double tùy domain bạn

        // 3) Insert DB
        if (dao.insert(dh) > 0) {
            JOptionPane.showMessageDialog(view, "Thêm thành công!");
            loadData();
            view.resetForm();
        }
            if (dao.insert(dh) > 0) {
                JOptionPane.showMessageDialog(view, "Thêm thành công!");
                loadData();
                view.resetForm();
            }
        }

    private void xuLySua() {
        QLDH dh = view.getDonHangFromInput();
        if (dao.update(dh) > 0) {
            JOptionPane.showMessageDialog(view, "Cập nhật thành công!");
            loadData();
            view.resetForm();
        }
    }

    private void xuLyXoa() {
        String maDH = view.getMaDonHangDangChon();
        if (maDH == null) return;

        int cf = JOptionPane.showConfirmDialog(view, "Xóa đơn hàng: " + maDH + " ?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (cf == JOptionPane.YES_OPTION) {
            if (dao.delete(maDH) > 0) {
                JOptionPane.showMessageDialog(view, "Đã xóa!");
                loadData();
                view.resetForm();
            }
        }
    }

    private void loadComboBoxData() {
        // MaKH
        view.getCboMaKH().removeAllItems();
        for (String maKH : dao.getAllMaKH()) {
            view.getCboMaKH().addItem(maKH);
        }

        // MaNV
        view.getCboMaNV().removeAllItems();
        for (String maNV : dao.getAllMaNV()) {
            view.getCboMaNV().addItem(maNV);
        }

        // Voucher
        view.getCboVoucherID().removeAllItems();
        view.getCboVoucherID().addItem("0"); // không dùng voucher
        for (String vid : dao.getAllVoucherIDActive()) {
            view.getCboVoucherID().addItem(vid);
        }
    }

}

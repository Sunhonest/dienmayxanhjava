/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller.QLBH;

import domain.QLBH.QLHD;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.QLBH.HoaDonDAO;
import view.viewQLBH.QuanLyHoaDon;

/**
 *
 * @author Admin
 */
public class QLHDController implements ActionListener {
    private final QuanLyHoaDon view;
    private final HoaDonDAO dao;

    public QLHDController(QuanLyHoaDon view) {
        this.view = view;
        this.dao = new HoaDonDAO();

        this.view.addActionListener(this);           
        this.view.getTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                view.fillFormTuBang();
            }
        });

        loadData();
    }

    private void loadData() {
        DefaultTableModel model = view.getModel();
        model.setRowCount(0);

        List<QLHD> list = dao.getAll();
        DecimalFormat df = new DecimalFormat("#,##0.00");

        for (QLHD hd : list) {
            model.addRow(new Object[]{
                hd.getMaHoaDon(),
                hd.getMaDonHang(),
                hd.getMaKH(),
                hd.getNgayLap(),
                df.format(hd.getTongTienHang()),
                df.format(hd.getTienGiam()),
                df.format(hd.getTongThanhToan()),
                hd.getPhuongThucTT(),
                hd.getTrangThai(),
                hd.getMaNV_Lap()
            });
        }
    }

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
        QLHD dh = view.getHoaDonFromInput();

        if (dh.getMaHoaDon() == null || dh.getMaHoaDon().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Chưa nhập Mã hóa đơn!");
            return;
        }
        if (dao.checkTrungMa(dh.getMaHoaDon())) {
            JOptionPane.showMessageDialog(view, "Mã hóa đơn đã tồn tại!");
            return;
        }

        if (dao.insert(dh) > 0) {
            JOptionPane.showMessageDialog(view, "Thêm thành công!");
            loadData();
            view.resetForm();
        }
    }

    private void xuLySua() {
        QLHD dh = view.getHoaDonFromInput();
        if (dao.update(dh) > 0) {
            JOptionPane.showMessageDialog(view, "Cập nhật thành công!");
            loadData();
            view.resetForm();
        }
    }

    private void xuLyXoa() {
        String MaHoaDon = view.getMaHoaDonDangChon();
        if (MaHoaDon == null) return;

        int cf = JOptionPane.showConfirmDialog(view, "Xóa đơn hàng: " + MaHoaDon + " ?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (cf == JOptionPane.YES_OPTION) {
            if (dao.delete(MaHoaDon) > 0) {
                JOptionPane.showMessageDialog(view, "Đã xóa!");
                loadData();
                view.resetForm();
            }
        }
    }
}

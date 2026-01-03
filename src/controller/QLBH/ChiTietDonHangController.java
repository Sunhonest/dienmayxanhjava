/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller.QLBH;

import domain.QLBH.ChiTietDonHang;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.QLBH.ChiTietDonHangDAO;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import view.viewQLBH.QuanLyChiTietDonHang;

/**
 *
 * @author Admin
 */
public class ChiTietDonHangController implements ActionListener {

    private final QuanLyChiTietDonHang view;
    private final ChiTietDonHangDAO dao;

    public ChiTietDonHangController(QuanLyChiTietDonHang view) {
        this.view = view;
        this.dao = new ChiTietDonHangDAO();

        this.view.addActionListener(this);
        this.view.getTable().addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                view.fillFormTuBang();
            }
        });

        // Auto: đổi SP -> lấy GiaBan -> tính thành tiền
        view.getCboMaSP().addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String maSP = String.valueOf(view.getCboMaSP().getSelectedItem());
                double gia = dao.getGiaBanByMaSP(maSP);
                view.setDonGia(gia);
            }
        });

        // Auto: đổi số lượng -> tính thành tiền
        // (view.updateThanhTien() đã có)
        // bắt sự kiện thay đổi spinner bằng timer nhẹ cho khỏi rườm rà
        new javax.swing.Timer(200, ev -> view.updateThanhTien()).start();

        loadCombo();
        loadData(dao.getAll());
        view.resetForm();
    }

    private void loadCombo() {
        // MaDonHang
        view.getCboMaDonHang().removeAllItems();
        for (String ma : dao.getAllMaDonHang()) view.getCboMaDonHang().addItem(ma);

        // MaSP
        view.getCboMaSP().removeAllItems();
        for (String ma : dao.getAllMaSP()) view.getCboMaSP().addItem(ma);

        // set donGia theo sp đầu (nếu có)
        if (view.getCboMaSP().getItemCount() > 0) {
            String maSP = String.valueOf(view.getCboMaSP().getSelectedItem());
            view.setDonGia(dao.getGiaBanByMaSP(maSP));
        }
    }

    private void loadData(List<ChiTietDonHang> list) {
        DefaultTableModel model = view.getModel();
        model.setRowCount(0);

        DecimalFormat df = new DecimalFormat("#,##0.00");
        for (ChiTietDonHang ct : list) {
            model.addRow(new Object[]{
                ct.getId(),
                ct.getMaDonHang(),
                ct.getMaSP(),
                ct.getSoLuong(),
                df.format(ct.getDonGia()),
                df.format(ct.getThanhTien())
            });
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        try {
            switch (cmd) {
                case "Them": them(); break;
                case "Sua": sua(); break;
                case "Xoa": xoa(); break;
                case "LamMoi":
                    view.resetForm();
                    loadCombo();
                    loadData(dao.getAll());
                    break;
                case "Tim":
                    loadData(dao.search(view.getKeyword()));
                    break;
                case "NhapExcel":
                    nhapExcel();
                    break;
                case "XuatExcel":
                    xuatExcel();
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view, "Lỗi: " + ex.getMessage());
        }
    }

    private void them() {
        ChiTietDonHang ct = view.getChiTietFromInput();

        if (ct.getMaDonHang() == null || ct.getMaDonHang().isBlank()) {
            JOptionPane.showMessageDialog(view, "Chưa chọn Mã đơn hàng!");
            return;
        }
        if (ct.getMaSP() == null || ct.getMaSP().isBlank()) {
            JOptionPane.showMessageDialog(view, "Chưa chọn Mã SP!");
            return;
        }
        if (ct.getSoLuong() <= 0) {
            JOptionPane.showMessageDialog(view, "Số lượng phải > 0!");
            return;
        }

        // nếu người dùng chưa nhập đơn giá -> lấy GiaBan
        if (ct.getDonGia() <= 0) {
            ct.setDonGia(dao.getGiaBanByMaSP(ct.getMaSP()));
            ct.setThanhTien(ct.getSoLuong() * ct.getDonGia());
        }

        if (dao.insert(ct) > 0) {
            JOptionPane.showMessageDialog(view, "Thêm chi tiết đơn hàng thành công!");
            loadData(dao.getAll());
            view.resetForm();
        }
    }

    private void sua() {
        Integer id = view.getIdDangChon();
        if (id == null) {
            JOptionPane.showMessageDialog(view, "Chọn dòng cần sửa!");
            return;
        }
        ChiTietDonHang ct = view.getChiTietFromInput();
        ct.setId(id); // chắc chắn đúng id đang chọn

        if (dao.update(ct) > 0) {
            JOptionPane.showMessageDialog(view, "Cập nhật thành công!");
            loadData(dao.getAll());
            view.resetForm();
        }
    }

    private void xoa() {
        Integer id = view.getIdDangChon();
        if (id == null) return;

        int cf = JOptionPane.showConfirmDialog(view, "Xóa chi tiết đơn hàng ID=" + id + " ?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (cf == JOptionPane.YES_OPTION) {
            if (dao.delete(id) > 0) {
                JOptionPane.showMessageDialog(view, "Đã xóa!");
                loadData(dao.getAll());
                view.resetForm();
            }
        }
    }

    // ================== EXCEL ==================
    // Cột: A-F = ID | MaDonHang | MaSP | SoLuong | DonGia | ThanhTien
    private void nhapExcel() throws Exception {
        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(view) != JFileChooser.APPROVE_OPTION) return;

        File file = fc.getSelectedFile();
        int count = 0;

        try (FileInputStream fis = new FileInputStream(file);
             Workbook wb = new XSSFWorkbook(fis)) {

            Sheet sheet = wb.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) { // bỏ header
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String maDH = getCellString(row.getCell(1));
                String maSP = getCellString(row.getCell(2));
                int soLuong = parseIntSafe(getCellString(row.getCell(3)));
                double donGia = parseDoubleSafe(getCellString(row.getCell(4)));
                double thanhTien = parseDoubleSafe(getCellString(row.getCell(5)));

                if (maDH.isBlank() || maSP.isBlank() || soLuong <= 0) continue;

                if (donGia <= 0) donGia = dao.getGiaBanByMaSP(maSP);
                if (thanhTien <= 0) thanhTien = soLuong * donGia;

                ChiTietDonHang ct = new ChiTietDonHang();
                ct.setMaDonHang(maDH);
                ct.setMaSP(maSP);
                ct.setSoLuong(soLuong);
                ct.setDonGia(donGia);
                ct.setThanhTien(thanhTien);

                if (dao.insert(ct) > 0) count++;
            }
        }

        JOptionPane.showMessageDialog(view, "Nhập Excel xong. Thêm mới: " + count + " dòng.");
        loadData(dao.getAll());
        view.resetForm();
    }

    private void xuatExcel() throws Exception {
        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new File("chitiet_donhang.xlsx"));
        if (fc.showSaveDialog(view) != JFileChooser.APPROVE_OPTION) return;

        File file = fc.getSelectedFile();
        List<ChiTietDonHang> list = dao.getAll();

        try (Workbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("ChiTietDonHang");

            Row header = sheet.createRow(0);
            String[] cols = {"ID", "MaDonHang", "MaSP", "SoLuong", "DonGia", "ThanhTien"};
            for (int c = 0; c < cols.length; c++) header.createCell(c).setCellValue(cols[c]);

            int r = 1;
            for (ChiTietDonHang ct : list) {
                Row row = sheet.createRow(r++);
                row.createCell(0).setCellValue(ct.getId());
                row.createCell(1).setCellValue(ct.getMaDonHang());
                row.createCell(2).setCellValue(ct.getMaSP());
                row.createCell(3).setCellValue(ct.getSoLuong());
                row.createCell(4).setCellValue(ct.getDonGia());
                row.createCell(5).setCellValue(ct.getThanhTien());
            }

            for (int c = 0; c < cols.length; c++) sheet.autoSizeColumn(c);

            try (FileOutputStream fos = new FileOutputStream(file)) {
                wb.write(fos);
            }
        }

        JOptionPane.showMessageDialog(view, "Xuất Excel thành công!");
    }

    private String getCellString(Cell cell) {
        if (cell == null) return "";
        cell.setCellType(CellType.STRING);
        return cell.getStringCellValue().trim();
    }

    private int parseIntSafe(String s) {
        try { return Integer.parseInt(s.trim()); } catch (Exception e) { return 0; }
    }

    private double parseDoubleSafe(String s) {
        try { return Double.parseDouble(s.trim().replace(",", "")); } catch (Exception e) { return 0.0; }
    }
}

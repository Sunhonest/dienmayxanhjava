/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller.QLBH;

/**
 *
 * @author Admin
 */
import domain.QLBH.KhachHang;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import model.QLBH.KhachHangDAO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import view.viewQLBH.QuanLyKhachHang;

public class KhachHangController implements ActionListener {

    private final QuanLyKhachHang view;
    private final KhachHangDAO dao;

    public KhachHangController(QuanLyKhachHang view) {
        this.view = view;
        this.dao = new KhachHangDAO();

        this.view.addActionListener(this);
        this.view.getTable().addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                view.fillFormTuBang();
            }
        });

        loadData(dao.getAll());
        setMaKHMoi();
    }

    private void setMaKHMoi() {
        view.setMaKH(dao.taoMaKHMoi());
    }

    private void loadData(List<KhachHang> list) {
        DefaultTableModel model = view.getModel();
        model.setRowCount(0);
        for (KhachHang kh : list) {
            model.addRow(new Object[]{
                kh.getMaKH(),
                kh.getHoTen(),
                kh.getSdt(),
                kh.getEmail(),
                kh.getDiaChi()
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
                    loadData(dao.getAll());
                    setMaKHMoi();
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
        KhachHang kh = view.getKhachHangFromInput();

        if (kh.getMaKH() == null || kh.getMaKH().isBlank()) {
            kh.setMaKH(dao.taoMaKHMoi());
        }
        if (kh.getHoTen() == null || kh.getHoTen().isBlank()) {
            JOptionPane.showMessageDialog(view, "Chưa nhập Họ tên!");
            return;
        }

        if (dao.checkTrungMa(kh.getMaKH())) {
            JOptionPane.showMessageDialog(view, "Mã KH đã tồn tại!");
            return;
        }

        if (dao.insert(kh) > 0) {
            JOptionPane.showMessageDialog(view, "Thêm khách hàng thành công!");
            view.resetForm();
            loadData(dao.getAll());
            setMaKHMoi();
        }
    }

    private void sua() {
        String ma = view.getMaKHDangChon();
        if (ma == null) {
            JOptionPane.showMessageDialog(view, "Chọn khách hàng cần sửa!");
            return;
        }

        KhachHang kh = view.getKhachHangFromInput();
        if (dao.update(kh) > 0) {
            JOptionPane.showMessageDialog(view, "Cập nhật thành công!");
            view.resetForm();
            loadData(dao.getAll());
            setMaKHMoi();
        }
    }

    private void xoa() {
        String ma = view.getMaKHDangChon();
        if (ma == null) return;

        int cf = JOptionPane.showConfirmDialog(view, "Xóa khách hàng: " + ma + " ?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (cf == JOptionPane.YES_OPTION) {
            if (dao.delete(ma) > 0) {
                JOptionPane.showMessageDialog(view, "Đã xóa!");
                view.resetForm();
                loadData(dao.getAll());
                setMaKHMoi();
            }
        }
    }

    // ================== EXCEL ==================
    // File Excel: cột A-E lần lượt: MaKH | HoTen | SDT | Email | DiaChi
    private void nhapExcel() throws Exception {
        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(view) != JFileChooser.APPROVE_OPTION) return;

        File file = fc.getSelectedFile();
        try (FileInputStream fis = new FileInputStream(file);
             Workbook wb = new XSSFWorkbook(fis)) {

            Sheet sheet = wb.getSheetAt(0);
            int count = 0;

            for (int i = 1; i <= sheet.getLastRowNum(); i++) { // bỏ header dòng 0
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String maKH = getCellString(row.getCell(0));
                String hoTen = getCellString(row.getCell(1));
                String sdt = getCellString(row.getCell(2));
                String email = getCellString(row.getCell(3));
                String diaChi = getCellString(row.getCell(4));

                if (hoTen == null || hoTen.isBlank()) continue;

                if (maKH == null || maKH.isBlank()) maKH = dao.taoMaKHMoi();
                if (dao.checkTrungMa(maKH)) continue;

                KhachHang kh = new KhachHang(maKH, hoTen, sdt, email, diaChi);
                if (dao.insert(kh) > 0) count++;
            }

            JOptionPane.showMessageDialog(view, "Nhập Excel xong. Thêm mới: " + count + " dòng.");
            loadData(dao.getAll());
            view.resetForm();
            setMaKHMoi();
        }
    }

    private void xuatExcel() throws Exception {
        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new File("khachhang.xlsx"));
        if (fc.showSaveDialog(view) != JFileChooser.APPROVE_OPTION) return;

        File file = fc.getSelectedFile();
        List<KhachHang> list = dao.getAll();

        try (Workbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("KhachHang");

            Row header = sheet.createRow(0);
            String[] cols = {"MaKH", "HoTen", "SDT", "Email", "DiaChi"};
            for (int c = 0; c < cols.length; c++) header.createCell(c).setCellValue(cols[c]);

            int r = 1;
            for (KhachHang kh : list) {
                Row row = sheet.createRow(r++);
                row.createCell(0).setCellValue(kh.getMaKH());
                row.createCell(1).setCellValue(kh.getHoTen());
                row.createCell(2).setCellValue(kh.getSdt());
                row.createCell(3).setCellValue(kh.getEmail());
                row.createCell(4).setCellValue(kh.getDiaChi());
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
}
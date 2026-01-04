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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import model.QLBH.HoaDonDAO;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
            @Override public void mouseClicked(MouseEvent e) {
                view.fillFormTuBang();
            }
        });

        loadCombos();
        bindDonHangChangeEvent();
        loadData(dao.getAll());

        setMaHoaDonMoi(); // ✅ tự tăng + không cho sửa
    }

    private void setMaHoaDonMoi() {
        String ma = dao.taoMaHoaDonMoi();
        while (dao.checkTrungMa(ma)) {
            int num = Integer.parseInt(ma.replace("HD", ""));
            ma = String.format("HD%03d", num + 1);
        }
        view.setMaHoaDon(ma);
    }

    private void loadData(List<QLHD> list) {
        DefaultTableModel model = view.getModel();
        model.setRowCount(0);

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
                    loadCombos();
                    loadData(dao.getAll());
                    setMaHoaDonMoi();
                    break;

                case "Tim":
                    loadData(dao.search(view.getKeyword()));
                    break;

                case "NhapExcel":
                    nhapExcel();
                    loadData(dao.getAll());
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

    private void xuLyThem() {
        QLHD hd = view.getHoaDonFromInput();

        // ✅ nếu trống mã -> auto sinh
        if (hd.getMaHoaDon() == null || hd.getMaHoaDon().isBlank()) {
            hd.setMaHoaDon(dao.taoMaHoaDonMoi());
        }

        if (dao.checkTrungMa(hd.getMaHoaDon())) {
            JOptionPane.showMessageDialog(view, "Mã hóa đơn đã tồn tại!");
            return;
        }

        // ✅ đảm bảo tổng thanh toán đúng
        float thanhToan = hd.getTongTienHang() - hd.getTienGiam();
        if (thanhToan < 0) thanhToan = 0;
        hd.setTongThanhToan(thanhToan);

        if (dao.insert(hd) > 0) {
            JOptionPane.showMessageDialog(view, "Thêm thành công!");
            loadData(dao.getAll());
            view.resetForm();
            loadCombos();
            setMaHoaDonMoi();
        }
    }

    private void xuLySua() {
        QLHD hd = view.getHoaDonFromInput();

        float thanhToan = hd.getTongTienHang() - hd.getTienGiam();
        if (thanhToan < 0) thanhToan = 0;
        hd.setTongThanhToan(thanhToan);

        if (dao.update(hd) > 0) {
            JOptionPane.showMessageDialog(view, "Cập nhật thành công!");
            loadData(dao.getAll());
            view.resetForm();
            loadCombos();
            setMaHoaDonMoi();
        }
    }

    private void xuLyXoa() {
        String maHoaDon = view.getMaHoaDonDangChon();
        if (maHoaDon == null) return;

        int cf = JOptionPane.showConfirmDialog(view,
                "Xóa hóa đơn: " + maHoaDon + " ?",
                "Xác nhận", JOptionPane.YES_NO_OPTION);

        if (cf == JOptionPane.YES_OPTION) {
            if (dao.delete(maHoaDon) > 0) {
                JOptionPane.showMessageDialog(view, "Đã xóa!");
                loadData(dao.getAll());
                view.resetForm();
                loadCombos();
                setMaHoaDonMoi();
            }
        }
    }

    // ============ COMBOS ============
    private void loadCombos() {
        // DonHang
        view.getCboMaDonHang().removeAllItems();
        for (String maDH : dao.getAllMaDonHang()) view.getCboMaDonHang().addItem(maDH);

        // MaKH (combo) - dùng để hiển thị, nhưng khóa enable=false
        view.getCboMaKH().removeAllItems();
        for (String maKH : dao.getAllMaKH()) view.getCboMaKH().addItem(maKH);

        // MaNV Lap (combo)
        view.getCboMaNVLap().removeAllItems();
        for (String maNV : dao.getAllMaNV()) view.getCboMaNVLap().addItem(maNV);

        // fill item đầu
        if (view.getCboMaDonHang().getItemCount() > 0) {
            String first = String.valueOf(view.getCboMaDonHang().getItemAt(0));
            fillFromDonHang(first);
        }
    }

    private void bindDonHangChangeEvent() {
        view.getCboMaDonHang().addItemListener(e -> {
            if (e.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
                String maDonHang = String.valueOf(e.getItem());
                fillFromDonHang(maDonHang);
            }
        });
    }

    private void fillFromDonHang(String maDonHang) {
        HoaDonDAO.DonHangInfo info = dao.getDonHangInfo(maDonHang);
        if (info != null) {
            view.setDonHangInfoToForm(info.maKH, info.tongTienHang, info.tienGiam, info.maNV);
        } else {
            view.setDonHangInfoToForm("", 0f, 0f, "");
        }
    }

    // ================== EXCEL ==================
    // A-J: MaHoaDon | MaDonHang | MaKH | NgayLap | TongTienHang | TienGiam | TongThanhToan | PhuongThucTT | TrangThai | MaNV_Lap
    private void nhapExcel() throws Exception {
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new FileNameExtensionFilter("Excel (*.xlsx)", "xlsx"));
        if (fc.showOpenDialog(view) != JFileChooser.APPROVE_OPTION) return;

        File file = fc.getSelectedFile();
        int count = 0;

        try (FileInputStream fis = new FileInputStream(file);
             Workbook wb = new XSSFWorkbook(fis)) {

            Sheet sheet = wb.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String maHD = getCellString(row.getCell(0));
                String maDH = getCellString(row.getCell(1));
                Date ngayLap = getCellDate(row.getCell(3));
                String pttt = getCellString(row.getCell(7));
                String trangThai = getCellString(row.getCell(8));
                String maNVLap = getCellString(row.getCell(9));

                if (maDH.isBlank()) continue;

                // nếu mã HD trống -> auto
                if (maHD.isBlank()) maHD = dao.taoMaHoaDonMoi();
                if (dao.checkTrungMa(maHD)) continue;

                // Lấy chuẩn từ đơn hàng để đúng MaKH, tiền
                HoaDonDAO.DonHangInfo info = dao.getDonHangInfo(maDH);
                if (info == null) continue;

                float tongHang = info.tongTienHang;
                float giam = info.tienGiam;
                float thanhToan = tongHang - giam;
                if (thanhToan < 0) thanhToan = 0;

                if (maNVLap.isBlank()) maNVLap = info.maNV;

                QLHD hd = new QLHD();
                hd.setMaHoaDon(maHD);
                hd.setMaDonHang(maDH);
                hd.setMaKH(info.maKH);
                hd.setNgayLap(ngayLap == null ? new Date() : ngayLap);
                hd.setTongTienHang(tongHang);
                hd.setTienGiam(giam);
                hd.setTongThanhToan(thanhToan);
                hd.setPhuongThucTT(pttt.isBlank() ? "TIEN_MAT" : pttt);
                hd.setTrangThai(trangThai.isBlank() ? "DA_THANH_TOAN" : trangThai);
                hd.setMaNV_Lap(maNVLap);

                if (dao.insert(hd) > 0) count++;
            }
        }

        JOptionPane.showMessageDialog(view, "Nhập Excel xong. Thêm mới: " + count + " dòng.");
        view.resetForm();
        loadCombos();
        setMaHoaDonMoi();
    }

    private void xuatExcel() throws Exception {
        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new File("hoadon.xlsx"));
        fc.setFileFilter(new FileNameExtensionFilter("Excel (*.xlsx)", "xlsx"));
        if (fc.showSaveDialog(view) != JFileChooser.APPROVE_OPTION) return;

        File file = fc.getSelectedFile();
        List<QLHD> list = dao.getAll();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try (Workbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("HoaDon");

            Row header = sheet.createRow(0);
            String[] cols = {"MaHoaDon","MaDonHang","MaKH","NgayLap","TongTienHang","TienGiam","TongThanhToan","PhuongThucTT","TrangThai","MaNV_Lap"};
            for (int c = 0; c < cols.length; c++) header.createCell(c).setCellValue(cols[c]);

            int r = 1;
            for (QLHD hd : list) {
                Row row = sheet.createRow(r++);
                row.createCell(0).setCellValue(hd.getMaHoaDon());
                row.createCell(1).setCellValue(hd.getMaDonHang());
                row.createCell(2).setCellValue(hd.getMaKH());
                row.createCell(3).setCellValue(hd.getNgayLap() == null ? "" : sdf.format(hd.getNgayLap()));
                row.createCell(4).setCellValue(hd.getTongTienHang());
                row.createCell(5).setCellValue(hd.getTienGiam());
                row.createCell(6).setCellValue(hd.getTongThanhToan());
                row.createCell(7).setCellValue(hd.getPhuongThucTT());
                row.createCell(8).setCellValue(hd.getTrangThai());
                row.createCell(9).setCellValue(hd.getMaNV_Lap());
            }

            for (int c = 0; c < cols.length; c++) sheet.autoSizeColumn(c);

            try (FileOutputStream fos = new FileOutputStream(file)) {
                wb.write(fos);
            }
        }

        JOptionPane.showMessageDialog(view, "Xuất Excel thành công!");
    }

    // ===== Excel helpers =====
    private String getCellString(Cell cell) {
        if (cell == null) return "";
        cell.setCellType(CellType.STRING);
        return cell.getStringCellValue().trim();
    }

    private Date getCellDate(Cell cell) {
        if (cell == null) return null;
        try {
            if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
                return cell.getDateCellValue();
            }
            String s = getCellString(cell);
            if (s.isBlank()) return null;
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(s);
        } catch (Exception e) {
            return null;
        }
    }
}

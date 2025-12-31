package controller.Kho;

import domain.Kho.NhaCungCap;
import domain.Kho.PhieuNhap;
import domain.Kho.SanPham;
import model.Kho.NhaCungCapDAO;
import model.Kho.PhieuNhapDAO;
import model.Kho.SanPhamDAO;
import view.viewKho.QuanLyNhapKho;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

// --- IMPORT CHO EXCEL (POI) ---
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font; // Sửa lại Font
import org.apache.poi.ss.usermodel.Row;  // Sửa lại Row
import org.apache.poi.ss.usermodel.Sheet; // Sửa lại Sheet
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
public class NhapKhoController implements ActionListener {

    private QuanLyNhapKho view;
    private SanPhamDAO spDao;
    private NhaCungCapDAO nccDao;
    private PhieuNhapDAO pnDao;
    private DecimalFormat df = new DecimalFormat("#,###");
    private domain.TaiKhoan taiKhoan;

    public NhapKhoController(QuanLyNhapKho view, domain.TaiKhoan tk) {
        this.view = view;
        this.taiKhoan = tk;
        this.spDao = new SanPhamDAO();
        this.nccDao = new NhaCungCapDAO();
        this.pnDao = new PhieuNhapDAO();
      
        view.addBtnListener(this);
        initData();
        loadTableTuDB(); 

        view.addTableMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                fillForm();
            }
        });
    }
    
    private void initData() {
        view.setNhaCungCap(nccDao.getAll());
        view.setListSanPham(spDao.getAll());
        if (taiKhoan != null) view.setNhanVien(taiKhoan.getMaNV());
        view.setMaPhieu("PN" + System.currentTimeMillis());
    }

    // 1. CẬP NHẬT: Hiển thị Ghi chú ra bảng
    private void loadTableTuDB() {
        List<PhieuNhap> list = pnDao.getAll();
        DefaultTableModel model = view.getModel();
        model.setRowCount(0);
        double tongTien = 0;
        for (PhieuNhap pn : list) {
            model.addRow(new Object[]{
                pn.getId(),        
                pn.getMaSP(),      
                pn.getTenSP(),     
                pn.getSoLuong(),   
                df.format(pn.getDonGia()), 
                df.format(pn.getThanhTien()), 
                // --- FIX: Hiển thị Ghi chú thay vì NgayNhap (Vì bảng bên View cột cuối là Ghi chú) ---
                pn.getGhiChu()   
            });
            tongTien += pn.getThanhTien();
        }
        view.setTongTien("Tổng tiền nhập: " + df.format(tongTien) + " VNĐ");
    }

    // ... (actionPerformed giữ nguyên)
    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd.equals("Thêm")) themPhieu();
        else if (cmd.equals("Xóa")) xoaPhieu();
        else if (cmd.equals("Mới")) {
            view.clearForm();
            view.setListSanPham(spDao.getAll()); 
            view.setNhaCungCap(nccDao.getAll());
            view.setMaPhieu("PN" + System.currentTimeMillis());
        } else if (cmd.equals("Sửa")) suaPhieu();
        else if (cmd.equals("Xuất Excel")) xuatExcel(); // <-- Thêm xử lý sự kiện này
        
        if (e.getSource() == view.getCboSanPham()) {
            domain.Kho.SanPham sp = view.getSelectedSanPham();
            if (sp != null) view.setTonKho(String.valueOf(sp.getTonKho()));
        }
    }

    // 2. CẬP NHẬT: Lấy Ghi chú từ View khi Thêm
    private void themPhieu() {
        SanPham sp = view.getSelectedSanPham();
        String strSL = view.getSoLuong();
        String strGia = view.getDonGia();

        if (sp == null || strSL.isEmpty() || strGia.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Vui lòng nhập đủ thông tin!");
            return;
        }
        try {
            int sl = Integer.parseInt(strSL);
            double gia = Double.parseDouble(strGia);
            
            PhieuNhap pn = new PhieuNhap();
            pn.setMaPhieu(view.getMaPhieu());
            pn.setMaNV(view.getNhanVien());
            NhaCungCap ncc = view.getSelectedNCC();
            pn.setMaNCC(ncc != null ? ncc.getMaNCC() : null);
            pn.setMaSP(sp.getMaSP());
            pn.setSoLuong(sl);
            pn.setDonGia(gia);
            pn.setThanhTien(sl * gia);
            // --- FIX: Lấy ghi chú từ TextField ---
            pn.setGhiChu(view.getGhiChu()); 

            if (pnDao.them(pn)) {
                JOptionPane.showMessageDialog(view, "Đã nhập kho thành công!");
                loadTableTuDB();
                view.clearForm();
            } else {
                JOptionPane.showMessageDialog(view, "Lỗi nhập kho!");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view, "Số lượng/Giá phải là số!");
        }
    }

    // 3. CẬP NHẬT: Lấy Ghi chú từ View khi Sửa
    private void suaPhieu() {
        int row = view.getTable().getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(view, "Chọn phiếu cần sửa!");
            return;
        }
        try {
            int id = Integer.parseInt(view.getModel().getValueAt(row, 0).toString());
            int soLuongCu = Integer.parseInt(view.getModel().getValueAt(row, 3).toString());
            String maSP = view.getModel().getValueAt(row, 1).toString();

            int soLuongMoi = Integer.parseInt(view.getSoLuong());
            double donGiaMoi = Double.parseDouble(view.getDonGia());
            
            PhieuNhap pn = new PhieuNhap();
            pn.setId(id);
            pn.setMaSP(maSP);
            pn.setSoLuong(soLuongMoi);
            pn.setDonGia(donGiaMoi);
            pn.setThanhTien(soLuongMoi * donGiaMoi);
            // --- FIX: Lấy ghi chú người dùng nhập để update ---
            pn.setGhiChu(view.getGhiChu()); 

            if (pnDao.suaPhieuNhap(pn, soLuongCu)) {
                JOptionPane.showMessageDialog(view, "Sửa thành công!");
                loadTableTuDB(); 
                view.clearForm();
            } else {
                JOptionPane.showMessageDialog(view, "Lỗi khi sửa!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Dữ liệu không hợp lệ!");
        }
    }
    
    // ... (xoaPhieu giữ nguyên)
    private void xoaPhieu() {
        int row = view.getTable().getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(view, "Chọn dòng cần xóa!");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(view, "Bạn có chắc muốn xóa (Sẽ trừ lại tồn kho)?");
        if (confirm != JOptionPane.YES_OPTION) return;

        int id = Integer.parseInt(view.getModel().getValueAt(row, 0).toString());
        String maSP = view.getModel().getValueAt(row, 1).toString();
        int sl = Integer.parseInt(view.getModel().getValueAt(row, 3).toString());

        if (pnDao.xoa(id, maSP, sl)) {
            JOptionPane.showMessageDialog(view, "Đã xóa!");
            loadTableTuDB();
        } else {
            JOptionPane.showMessageDialog(view, "Lỗi khi xóa!");
        }
    }
    
    private void xuatExcel() {
        // 1. Cho người dùng chọn đường dẫn lưu file
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn nơi lưu file Excel");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Excel Files", "xlsx"));
        
        int userSelection = fileChooser.showSaveDialog(view);
        
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            // Đảm bảo đuôi file là .xlsx
            String filePath = fileToSave.getAbsolutePath();
            if (!filePath.endsWith(".xlsx")) {
                filePath += ".xlsx";
            }

            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("Lịch sử nhập kho");

                // 2. Tạo Header (Dòng tiêu đề)
                Row headerRow = sheet.createRow(0);
                String[] columns = {"ID", "Mã SP", "Tên SP", "Số lượng", "Đơn giá", "Thành tiền", "Ghi chú"};
                
                // Style cho header (In đậm)
                CellStyle headerStyle = workbook.createCellStyle();
                Font font = workbook.createFont();
                font.setBold(true);
                headerStyle.setFont(font);

                for (int i = 0; i < columns.length; i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(columns[i]);
                    cell.setCellStyle(headerStyle);
                }

                // 3. Lấy dữ liệu từ JTable trong View và ghi vào Excel
                DefaultTableModel model = view.getModel();
                for (int i = 0; i < model.getRowCount(); i++) {
                    Row row = sheet.createRow(i + 1);
                    for (int j = 0; j < columns.length; j++) {
                        Object value = model.getValueAt(i, j);
                        row.createCell(j).setCellValue(value != null ? value.toString() : "");
                    }
                }

                // 4. Tự động chỉnh độ rộng cột
                for (int i = 0; i < columns.length; i++) {
                    sheet.autoSizeColumn(i);
                }

                // 5. Ghi ra file
                try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                    workbook.write(fileOut);
                }

                // Thông báo và mở file
                int open = JOptionPane.showConfirmDialog(view, 
                        "Xuất file thành công! Bạn có muốn mở file ngay không?", 
                        "Thông báo", JOptionPane.YES_NO_OPTION);
                if (open == JOptionPane.YES_OPTION) {
                    Desktop.getDesktop().open(new File(filePath));
                }

            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(view, "Lỗi khi ghi file Excel: " + ex.getMessage());
            }
        }
    }

    // 4. CẬP NHẬT: Đổ dữ liệu Ghi chú ngược lại TextField khi click bảng
    private void fillForm() {
        int row = view.getTable().getSelectedRow();
        if (row >= 0) {
            String maSP = view.getModel().getValueAt(row, 1).toString();
            String sl = view.getModel().getValueAt(row, 3).toString();
            String gia = view.getModel().getValueAt(row, 4).toString().replace(",", "").replace(".", ""); 
            
            // --- FIX: Lấy dữ liệu cột Ghi Chú (cột 6) ---
            Object ghiChuObj = view.getModel().getValueAt(row, 6);
            String ghiChu = (ghiChuObj != null) ? ghiChuObj.toString() : "";

            view.setSelectedSanPhamByMa(maSP);
            view.setSoLuong(sl);
            view.setDonGia(gia);
            view.setGhiChu(ghiChu);
            
        }
    }
}
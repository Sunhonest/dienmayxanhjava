package controller.Kho;

import domain.NhaCungCap;
import domain.PhieuNhap;
import domain.SanPham;
import model.Kho.NhaCungCapDAO;
import model.Kho.PhieuNhapDAO;
import model.Kho.SanPhamDAO;
import view.viewKho.QuanLyNhapKho;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

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

//        
        view.addBtnListener(this);
        initData();
        loadTableTuDB(); // Load dữ liệu thật từ DB ngay khi mở

        // Sự kiện click bảng
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
        if (taiKhoan != null) {
        view.setNhanVien(taiKhoan.getMaNV()); // Lấy mã NV từ tài khoản đăng nhập
    }
        view.setMaPhieu("PN" + System.currentTimeMillis());
    }

    private void loadTableTuDB() {
        List<PhieuNhap> list = pnDao.getAll();
        DefaultTableModel model = view.getModel();
        model.setRowCount(0);
        double tongTien = 0;
        for (PhieuNhap pn : list) {
            model.addRow(new Object[]{
                pn.getId(),        // Cột 0: ID ẩn (để xóa/sửa)
                pn.getMaSP(),      // Cột 1
                pn.getTenSP(),     // Cột 2
                pn.getSoLuong(),   // Cột 3
                df.format(pn.getDonGia()), // Cột 4
                df.format(pn.getThanhTien()), // Cột 5
                pn.getNgayNhap()   // Cột 6
            });
            tongTien += pn.getThanhTien();
        }
        view.setTongTien("Tổng tiền nhập: " + df.format(tongTien) + " VNĐ");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd.equals("Thêm")) {
            themPhieu();
        } else if (cmd.equals("Xóa")) {
            xoaPhieu();
        } else if (cmd.equals("Mới")) {
            view.clearForm();
            view.setMaPhieu("PN" + System.currentTimeMillis());
        }else if (cmd.equals("Sửa")) { 
            suaPhieu();
        }
        if (e.getSource() == view.getCboSanPham()) { // Cần thêm getter getCboSanPham() bên View
        domain.SanPham sp = view.getSelectedSanPham();
        if (sp != null) {
            view.setTonKho(String.valueOf(sp.getTonKho()));
        }
    }
        // Nút Sửa: Tạm thời tôi khuyên nên dùng Xóa -> Nhập lại cho an toàn tồn kho
        // Hoặc bạn có thể implement suaGia như bên DAO
    }
    
    private void suaPhieu() {
    int row = view.getTable().getSelectedRow();
    if (row < 0) {
        JOptionPane.showMessageDialog(view, "Chọn phiếu cần sửa!");
        return;
    }

    try {
        // Lấy thông tin ID và Số lượng CŨ từ bảng (trước khi sửa)
        int id = Integer.parseInt(view.getModel().getValueAt(row, 0).toString());
        int soLuongCu = Integer.parseInt(view.getModel().getValueAt(row, 3).toString());
        String maSP = view.getModel().getValueAt(row, 1).toString();

        // Lấy thông tin MỚI từ form
        int soLuongMoi = Integer.parseInt(view.getSoLuong());
        double donGiaMoi = Double.parseDouble(view.getDonGia());
        
        PhieuNhap pn = new PhieuNhap();
        pn.setId(id);
        pn.setMaSP(maSP);
        pn.setSoLuong(soLuongMoi);
        pn.setDonGia(donGiaMoi);
        pn.setThanhTien(soLuongMoi * donGiaMoi);

        // Gọi DAO
        if (pnDao.suaPhieuNhap(pn, soLuongCu)) {
            JOptionPane.showMessageDialog(view, "Sửa thành công!");
            loadTableTuDB(); // Load lại bảng
            view.clearForm();
        } else {
            JOptionPane.showMessageDialog(view, "Lỗi khi sửa!");
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(view, "Dữ liệu không hợp lệ!");
    }
}

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

            // Gọi DAO lưu thẳng vào DB
            if (pnDao.them(pn)) {
                JOptionPane.showMessageDialog(view, "Đã nhập kho thành công!");
                loadTableTuDB(); // Load lại bảng từ DB
                view.clearForm();
            } else {
                JOptionPane.showMessageDialog(view, "Lỗi nhập kho!");
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view, "Số lượng/Giá phải là số!");
        }
    }

    private void xoaPhieu() {
        int row = view.getTable().getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(view, "Chọn dòng cần xóa!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(view, "Bạn có chắc muốn xóa (Sẽ trừ lại tồn kho)?");
        if (confirm != JOptionPane.YES_OPTION) return;

        // Lấy thông tin từ bảng để xóa
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

    private void fillForm() {
        int row = view.getTable().getSelectedRow();
        if (row >= 0) {
            // Lấy dữ liệu từ bảng đổ lên form
            String maSP = view.getModel().getValueAt(row, 1).toString();
            String sl = view.getModel().getValueAt(row, 3).toString();
            // Xử lý chuỗi tiền tệ (bỏ dấu phẩy)
            String gia = view.getModel().getValueAt(row, 4).toString().replace(",", "").replace(".", ""); 
            
            view.setSelectedSanPhamByMa(maSP);
            view.setSoLuong(sl);
            view.setDonGia(gia);
        }
    }
}
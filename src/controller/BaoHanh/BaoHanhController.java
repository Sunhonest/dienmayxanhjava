package controller.BaoHanh;

import domain.PhieuBaoHanh;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.BaoHanh.BaoHanhDAO;
import view.viewBaoHanh.QuanLyBaoHanh;

public class BaoHanhController implements ActionListener {
    
    private QuanLyBaoHanh view;
    private BaoHanhDAO dao;

    public BaoHanhController(QuanLyBaoHanh view) {
        this.view = view;
        this.dao = new BaoHanhDAO();
        
        // 1. Gắn sự kiện click nút
        this.view.addBtnListener(this);
        
        // 2. Gắn sự kiện click dòng trong bảng
        this.view.getTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                fillFormTuBang();
            }
        });
        
        // 3. Tải dữ liệu ban đầu
        loadData();
    }

    private void loadData() {
        DefaultTableModel model = view.getModel();
        model.setRowCount(0); 
        
        List<PhieuBaoHanh> list = dao.getAll();
        for (PhieuBaoHanh p : list) {
            // Thứ tự cột: [0]MaPhieu, [1]MaSP, [2]TenSP, [3]Serial, [4]Khach, [5]SDT, [6]Ngay, [7]TrangThai, [8]Loi
            model.addRow(new Object[]{
                p.getMaPhieu(),
                p.getMaSP(),
                p.getTenSP(),           // Cột 2: Tên SP
                p.getSoSerial(),
                p.getTenKhachHang(),
                p.getSoDienThoai(),
                p.getNgayTiepNhan(),
                p.getTrangThai(),
                p.getMoTaLoi()          // Cột 8: Lỗi
            });
        }
    }

    // --- HÀM NÀY ĐÃ ĐƯỢC CẬP NHẬT CHỈ SỐ INDEX ---
    private void fillFormTuBang() {
        int row = view.getTable().getSelectedRow();
        if (row >= 0) {
            // Lấy dữ liệu từ dòng được chọn (Lưu ý index đã thay đổi do thêm cột TenSP)
            String maPhieu  = view.getTable().getValueAt(row, 0).toString();
            String maSP     = view.getTable().getValueAt(row, 1).toString();
            String tenSP    = view.getTable().getValueAt(row, 2).toString(); // Index 2 là Tên SP
            String serial   = view.getTable().getValueAt(row, 3).toString(); // Serial bị đẩy sang 3
            String tenKhach = view.getTable().getValueAt(row, 4).toString(); // Khách bị đẩy sang 4
            String sdt      = view.getTable().getValueAt(row, 5).toString(); // SDT bị đẩy sang 5
            // Index 6 là ngày, ta bỏ qua
            String trangThai= view.getTable().getValueAt(row, 7).toString(); // Trạng thái sang 7
            
            Object objLoi   = view.getTable().getValueAt(row, 8);            // Lỗi sang 8
            String loi      = (objLoi != null) ? objLoi.toString() : "";

            // Tạo đối tượng tạm để đẩy lên View
            PhieuBaoHanh p = new PhieuBaoHanh();
            p.setMaPhieu(maPhieu);
            p.setMaSP(maSP);
            p.setTenSP(tenSP); // Set thêm tên SP
            p.setSoSerial(serial);
            p.setTenKhachHang(tenKhach);
            p.setSoDienThoai(sdt);
            p.setTrangThai(trangThai);
            p.setMoTaLoi(loi); // Set thêm lỗi
            
            // --- GỌI HÀM CỦA VIEW ---
            view.fillForm(p); 
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        try {
            switch(cmd) {
                case "TiepNhan": xuLyThem(); break;
                case "CapNhat": xuLySua(); break;
                case "Xoa": xuLyXoa(); break;
                case "LamMoi": 
                    view.resetForm();
                    loadData();
                    break;
                case "Check": xuLyCheckThongTin(); break;
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Lỗi: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void xuLyCheckThongTin() {
        String maHD = view.getTxtMaHoaDon().getText().trim();
        if (maHD.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Vui lòng nhập Mã Hóa Đơn!");
            return;
        }
        
        String[] info = dao.getThongTinTuHoaDon(maHD);
        
        if (info != null) {
            // info[0]: MaSP, info[1]: TenKhach, info[2]: SDT, info[3]: TenSP
            view.fillThongTinKiemTra(info[0], info[1], info[2], info[3]); 
            JOptionPane.showMessageDialog(view, "Đã tìm thấy: " + info[1]);
        } else {
            JOptionPane.showMessageDialog(view, "Không tìm thấy Hóa đơn: " + maHD);
        }
    }
    
    private void xuLyThem() {
        PhieuBaoHanh p = view.getPhieuTuForm();
        if (p.getMaPhieu().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Vui lòng nhập Mã phiếu!");
            return;
        }
        if (dao.insert(p) > 0) {
            JOptionPane.showMessageDialog(view, "Thêm thành công!");
            loadData();
            view.resetForm();
        } else {
            JOptionPane.showMessageDialog(view, "Thêm thất bại!");
        }
    }

    private void xuLySua() {
        PhieuBaoHanh p = view.getPhieuTuForm();
        if (p.getMaPhieu().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn phiếu cần sửa!");
            return;
        }
        
        if (dao.update(p) > 0) {
            JOptionPane.showMessageDialog(view, "Cập nhật thành công!");
            loadData();
            view.resetForm();
        } else {
            JOptionPane.showMessageDialog(view, "Cập nhật thất bại!");
        }
    }

    private void xuLyXoa() {
        String maPhieu = view.getPhieuTuForm().getMaPhieu();
        if (maPhieu.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn phiếu cần xóa!");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(view, "Xóa phiếu " + maPhieu + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (dao.delete(maPhieu) > 0) {
                JOptionPane.showMessageDialog(view, "Đã xóa!");
                loadData();
                view.resetForm();
            } else {
                JOptionPane.showMessageDialog(view, "Xóa thất bại!");
            }
        }
    }
}
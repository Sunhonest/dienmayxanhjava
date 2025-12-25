package controller.NhanSu;

import domain.ChucVu;
import model.NhanSu.ChucVuDAO;
import view.viewNhanSu.QuanLyChucVu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat; // Import thư viện format số
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class ChucVuController implements ActionListener {
    
    private QuanLyChucVu view;
    private ChucVuDAO dao;

    public ChucVuController(QuanLyChucVu view) {
        this.view = view;
        this.dao = new ChucVuDAO();
        
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
        List<ChucVu> list = dao.getAll();
        
        // Tạo bộ định dạng số: cứ 3 số là có dấu chấm
        DecimalFormat df = new DecimalFormat("#,###"); 
        
        for (ChucVu cv : list) {
            // Format lương: 1000000 -> "1.000.000" (Nếu máy bạn dùng dấu phẩy thì replace lại)
            String luongHienThi = df.format(cv.getLuongCoBan()).replace(",", ".");
            
            model.addRow(new Object[]{
                cv.getMaCV(),
                cv.getTenCV(),
                luongHienThi, // Hiển thị chuỗi đã format
                cv.getMoTa()
            });
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        try {
            switch(cmd) {
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
        ChucVu cv = view.getChucVuFromInput();
        if(cv.getMaCV().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Chưa nhập mã chức vụ!");
            return;
        }
        
        // CHECK LƯƠNG > 100 TRIỆU
        if(cv.getLuongCoBan() > 100000000) {
            JOptionPane.showMessageDialog(view, "Lương cơ bản không được vượt quá 100.000.000 VND!");
            return;
        }

        if(dao.checkTrungMa(cv.getMaCV())) {
            JOptionPane.showMessageDialog(view, "Mã chức vụ đã tồn tại!");
            return;
        }
        if(dao.insert(cv) > 0) {
            JOptionPane.showMessageDialog(view, "Thêm thành công!");
            loadData();
            view.resetForm();
        }
    }
    
    private void xuLySua() {
        ChucVu cv = view.getChucVuFromInput();
        
        // CHECK LƯƠNG > 100 TRIỆU KHI SỬA
        if(cv.getLuongCoBan() > 100000000) {
            JOptionPane.showMessageDialog(view, "Lương cơ bản không được vượt quá 100.000.000 VND!");
            return;
        }

        if(dao.update(cv) > 0) {
            JOptionPane.showMessageDialog(view, "Cập nhật thành công!");
            loadData();
            view.resetForm();
        } else {
             JOptionPane.showMessageDialog(view, "Lỗi cập nhật!");
        }
    }
    
    private void xuLyXoa() {
        String maCV = view.getMaCVDangChon();
        if(maCV == null) return;
        
        int cf = JOptionPane.showConfirmDialog(view, "Xóa chức vụ: " + maCV + "?");
        if(cf == JOptionPane.YES_OPTION) {
            if(dao.delete(maCV) > 0) {
                JOptionPane.showMessageDialog(view, "Đã xóa!");
                loadData();
                view.resetForm();
            }
        }
    }
}
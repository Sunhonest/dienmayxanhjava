package controller.NhanSu;

import domain.ChucVu;
import domain.NhanVien;
import model.NhanSu.ChucVuDAO;
import model.NhanSu.NhanVienDAO;
import view.viewNhanSu.QuanLyNhanVien;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class NhanVienController implements ActionListener {
    
    private QuanLyNhanVien view;
    private NhanVienDAO nvDao;
    private ChucVuDAO cvDao;

    public NhanVienController(QuanLyNhanVien view) {
        this.view = view;
        this.nvDao = new NhanVienDAO();
        this.cvDao = new ChucVuDAO();
        
        this.view.addActionListener(this);
        this.view.getTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                view.fillFormTuBang();
                // Khi click vào bảng -> View tự setTrangThaiNut(true) trong fillFormTuBang
            }
        });

        loadDataChucVu();
        loadDataLenBang();
        
        // Mặc định ban đầu: Chỉ cho Thêm
        view.setTrangThaiNut(false);
    }

    public void loadDataChucVu() {
        List<ChucVu> listCV = cvDao.getAll();
        view.setDuLieuChucVu(listCV);
    }

    public void loadDataLenBang() {
        DefaultTableModel model = view.getModel();
        model.setRowCount(0); 
        List<NhanVien> list = nvDao.getAll();
        for (NhanVien nv : list) {
            model.addRow(new Object[]{
                nv.getMaNV(), nv.getHoTen(), nv.getGioiTinh(),
                nv.getMaCV(), nv.getSdt(), nv.getEmail()
            });
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        try {
            switch (command) {
                case "Them": xuLyThem(); break;
                case "Sua": xuLySua(); break;
                case "Xoa": xuLyXoa(); break;
                case "Làm mới": 
                    view.resetForm(); // Trong này đã gọi setTrangThaiNut(false)
                    loadDataLenBang();
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view, "Lỗi: " + ex.getMessage());
        }
    }

    private void xuLyThem() {
        NhanVien nv = view.getNhanVienFromInput();
        if(nv.getMaNV().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Chưa nhập Mã NV!");
            return;
        }
        if(nvDao.checkTrungMa(nv.getMaNV())) {
             JOptionPane.showMessageDialog(view, "Mã NV đã tồn tại!");
             return;
        }
        if (nvDao.insert(nv) > 0) {
            JOptionPane.showMessageDialog(view, "Thêm thành công!");
            loadDataLenBang();
            view.resetForm(); // Reset form & nút về trạng thái Thêm
        }
    }

    private void xuLySua() {
        NhanVien nv = view.getNhanVienFromInput();
        if (nvDao.update(nv) > 0) {
            JOptionPane.showMessageDialog(view, "Sửa thành công!");
            loadDataLenBang();
            view.resetForm(); // Sau khi sửa xong thì quay về trạng thái Thêm
        } else {
             JOptionPane.showMessageDialog(view, "Lỗi khi sửa!");
        }
    }

    private void xuLyXoa() {
        String maNV = view.getMaNVDangChon();
        if (maNV == null) return;
        
        int confirm = JOptionPane.showConfirmDialog(view, "Xóa nhân viên " + maNV + "?");
        if (confirm == JOptionPane.YES_OPTION) {
            if (nvDao.delete(maNV) > 0) {
                JOptionPane.showMessageDialog(view, "Đã xóa!");
                loadDataLenBang();
                view.resetForm(); // Sau khi xóa xong thì quay về trạng thái Thêm
            }
        }
    }
}
package controller.NhanSu;

import domain.NhanVien;
import domain.TaiKhoan;
import model.NhanSu.NhanVienDAO;
import model.NhanSu.TaiKhoanDAO;
import view.viewNhanSu.QuanLyTaiKhoan;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class TaiKhoanController implements ActionListener {

    private QuanLyTaiKhoan view;
    private TaiKhoanDAO tkDao;
    private NhanVienDAO nvDao;
    
    // Cache danh sách nhân viên
    private List<NhanVien> listNhanVienCache; 

    public TaiKhoanController(QuanLyTaiKhoan view) {
        this.view = view;
        this.tkDao = new TaiKhoanDAO();
        this.nvDao = new NhanVienDAO();
        
        this.listNhanVienCache = nvDao.getAll();

        this.view.addActionListener(this);
        
        // --- SỬA 1: Xử lý click bảng ---
        this.view.getTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // 1. Đổ dữ liệu lên các ô text
                view.fillFormTuBang();
                
                // 2. Xóa trắng ô Mật khẩu để Admin biết là "Nhập mới thì đổi, ko nhập thì thôi"
                // (Giả sử view có hàm getTxtMatKhau(), nếu không bạn hãy thêm vào View nhé)
                try {
                    view.getTxtMatKhau().setText("");
                } catch (Exception ex) {
                    // Bỏ qua nếu View chưa public hàm này
                }
            }
        });

        // Sự kiện tìm kiếm gợi ý
        this.view.getTxtMaNV().addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String input = view.getTxtMaNV().getText().trim();
                if (!input.isEmpty()) {
                    List<String> suggestions = timKiemMaNV(input);
                    view.hienThiGoiY(suggestions);
                }
            }
        });

        loadData();
    }
    
    private List<String> timKiemMaNV(String keyword) {
        List<String> ketQua = new ArrayList<>();
        String keywordLower = keyword.toLowerCase();
        
        if (listNhanVienCache == null || listNhanVienCache.isEmpty()) {
            listNhanVienCache = nvDao.getAll();
        }

        for (NhanVien nv : listNhanVienCache) {
            if (nv.getMaNV().toLowerCase().contains(keywordLower) || 
                nv.getHoTen().toLowerCase().contains(keywordLower)) {
                ketQua.add(nv.getMaNV() + " - " + nv.getHoTen());
            }
        }
        return ketQua;
    }

    public void loadData() {
        DefaultTableModel model = view.getModel();
        model.setRowCount(0);
        List<TaiKhoan> list = tkDao.getAll();
        
        for (TaiKhoan tk : list) {
            String quyenHienThi;
            switch (tk.getCapDoQuyen()) {
                case 3: quyenHienThi = "Admin (Level 3)"; break;
                case 2: quyenHienThi = "Quản lý (Level 2)"; break;
                default: quyenHienThi = "Nhân viên"; break;
            }
            
            model.addRow(new Object[]{
                tk.getTenDangNhap(), 
                tk.getMaNV(), 
                quyenHienThi, 
                tk.getTrangThai()
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
                case "Reset": xuLyResetPass(); break;
                case "LamMoi": 
                    view.resetForm();
                    listNhanVienCache = nvDao.getAll(); 
                    loadData();
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view, "Lỗi hệ thống: " + ex.getMessage());
        }
    }
    
    private String extractMaNV(String rawInput) {
        if (rawInput != null && rawInput.contains(" - ")) {
            return rawInput.split(" - ")[0].trim();
        }
        return rawInput != null ? rawInput.trim() : "";
    }

    private void xuLyThem() {
        TaiKhoan tk = view.getTaiKhoanFromInput();
        tk.setMaNV(extractMaNV(tk.getMaNV()));

        // Với Thêm mới, bắt buộc phải có mật khẩu
        if (tk.getTenDangNhap().isEmpty() || tk.getMatKhau().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Vui lòng nhập đầy đủ Username và Password!");
            return;
        }
        
        if (!nvDao.checkTrungMa(tk.getMaNV())) {
             JOptionPane.showMessageDialog(view, "Mã nhân viên không tồn tại trong hồ sơ!");
             return;
        }
        
        if (tkDao.checkTrungUsername(tk.getTenDangNhap())) {
            JOptionPane.showMessageDialog(view, "Tên đăng nhập này đã được sử dụng!");
            return;
        }
        
        if (tkDao.insert(tk) > 0) {
            JOptionPane.showMessageDialog(view, "Thêm tài khoản thành công!");
            loadData();
            view.resetForm();
        }
    }

    // --- SỬA 2: Logic Sửa thông minh (Tự nhận diện có đổi pass hay không) ---
    private void xuLySua() {
        int row = view.getTable().getSelectedRow();
        if(row < 0) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn tài khoản cần sửa!");
            return;
        }

        TaiKhoan tkMoi = view.getTaiKhoanFromInput();
        tkMoi.setMaNV(extractMaNV(tkMoi.getMaNV()));
        String userCu = view.getUsernameCu(); 
        
        // Lấy mật khẩu từ ô nhập liệu (nếu có)
        String passMoi = tkMoi.getMatKhau().trim();

        // Check bảo vệ Admin cuối cùng
        String quyenCuStr = view.getTable().getValueAt(row, 2).toString();
        boolean isCurrentAdmin = quyenCuStr.contains("Admin") || quyenCuStr.contains("Level 3");

        if (isCurrentAdmin) {
            if (tkDao.getSoLuongAdmin() <= 1) {
                if (tkMoi.getCapDoQuyen() != 3 || "Đã khóa".equals(tkMoi.getTrangThai())) {
                      JOptionPane.showMessageDialog(view, "CẢNH BÁO: Không thể hạ quyền hoặc khóa Admin duy nhất!");
                      return;
                }
            }
        }

        // Check trùng username nếu đổi tên
        if (!tkMoi.getTenDangNhap().equals(userCu)) {
            if (tkDao.checkTrungUsername(tkMoi.getTenDangNhap())) {
                JOptionPane.showMessageDialog(view, "Tên đăng nhập mới đã tồn tại!");
                return;
            }
        }

        // --- THỰC HIỆN UPDATE ---
        // B1: Update thông tin chung (Quyền, Trạng thái, MaNV...)
        boolean kqUpdateInfo = tkDao.update(tkMoi, userCu) > 0;
        
        // B2: Update mật khẩu (CHỈ KHI Admin có nhập vào ô mật khẩu)
        boolean kqUpdatePass = false;
        if (!passMoi.isEmpty()) {
            // Có nhập -> Gọi hàm resetPassword để ghi đè mật khẩu mới
            if (tkDao.resetPassword(tkMoi.getTenDangNhap(), passMoi) > 0) {
                kqUpdatePass = true;
            }
        } else {
            // Không nhập -> Coi như thành công (giữ nguyên pass cũ)
            kqUpdatePass = true; 
        }

        if (kqUpdateInfo && kqUpdatePass) {
            String msg = passMoi.isEmpty() ? "Cập nhật thông tin thành công!" : "Cập nhật thông tin và đổi mật khẩu thành công!";
            JOptionPane.showMessageDialog(view, msg);
            loadData();
            view.resetForm();
        } else {
            JOptionPane.showMessageDialog(view, "Cập nhật thất bại! Vui lòng kiểm tra lại.");
        }
    }

    private void xuLyXoa() {
        String user = view.getUsernameDangChon();
        if (user == null || user.isEmpty()) return;
        
        // --- SỬA 3: Thêm trim() để chắc chắn xóa đúng tên ---
        user = user.trim(); 

        // Logic check Admin cuối cùng khi xóa...
        int row = view.getTable().getSelectedRow();
        String quyenHienTai = view.getTable().getValueAt(row, 2).toString();

        if (quyenHienTai.contains("Admin") || quyenHienTai.contains("Level 3")) {
            if (tkDao.getSoLuongAdmin() <= 1) {
                 JOptionPane.showMessageDialog(view, "KHÔNG THỂ XÓA Admin cuối cùng!");
                 return;
            }
        }
        
        int cf = JOptionPane.showConfirmDialog(view, "Xóa tài khoản [" + user + "]?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (cf == JOptionPane.YES_OPTION) {
            if (tkDao.delete(user) > 0) {
                JOptionPane.showMessageDialog(view, "Đã xóa!");
                loadData();
                view.resetForm();
            } else {
                JOptionPane.showMessageDialog(view, "Xóa thất bại!");
            }
        }
    }

    private void xuLyResetPass() {
        String user = view.getUsernameDangChon();
        if(user == null || user.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn tài khoản cần Reset mật khẩu!");
            return;
        }
        
        // --- SỬA 4: QUAN TRỌNG - Trim() tên đăng nhập để khớp với Database ---
        user = user.trim(); 
        
        String newPass = JOptionPane.showInputDialog(view, "Nhập mật khẩu mới cho user [" + user + "]:");
        if(newPass != null && !newPass.trim().isEmpty()){
            if(tkDao.resetPassword(user, newPass.trim()) > 0) {
                JOptionPane.showMessageDialog(view, "Đổi mật khẩu thành công!");
            } else {
                JOptionPane.showMessageDialog(view, "Lỗi: Không tìm thấy User trong CSDL (Check khoảng trắng thừa)!");
            }
        }
    }
}
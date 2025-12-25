package controller.NhanSu;

import domain.TaiKhoan;
import model.NhanSu.TaiKhoanDAO;
import view.DangNhapFrame;
import view.MainForm; // Sửa MainFrame thành MainForm cho đúng tên file bạn có

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;

public class DangNhapController implements ActionListener {

    private DangNhapFrame view;
    private TaiKhoanDAO dao;

    public DangNhapController(DangNhapFrame view) {
        this.view = view;
        this.dao = new TaiKhoanDAO();

        // Đăng ký sự kiện cho các nút trong View
        this.view.addBtnLoginListener(this);
        this.view.addBtnExitListener(this);
        this.view.addChkShowPassListener(this);
        this.view.addBtnConfirmChangeListener(this);
        this.view.addBtnBackListener(this);

        // Đăng ký sự kiện click link "Đổi mật khẩu"
        this.view.addLinkChangePassListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                view.showChangePassForm();
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        if (cmd.equals("LOGIN")) {
            xuLyDangNhap();
        } else if (cmd.equals("EXIT")) {
            System.exit(0);
        } else if (cmd.equals("SHOW_PASS")) {
            view.toggleShowPassword();
        } else if (cmd.equals("CONFIRM_CHANGE")) {
            xuLyDoiMatKhau();
        } else if (cmd.equals("BACK")) {
            view.showLoginForm();
        }
    }

    // --- LOGIC 1: ĐĂNG NHẬP ---
    private void xuLyDangNhap() {
        String user = view.getUsernameLogin();
        String pass = view.getPasswordLogin();

        // 1. Validate
        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Vui lòng nhập đầy đủ thông tin!", 
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 2. Gọi Model kiểm tra
        TaiKhoan tk = dao.checkLogin(user, pass);

        if (tk != null) {
            JOptionPane.showMessageDialog(view, "Đăng nhập thành công!");
            
            // Mở MainForm và truyền tài khoản sang
            new MainForm(tk).setVisible(true);
            
            // Đóng form đăng nhập
            view.dispose();
        } else {
            JOptionPane.showMessageDialog(view, 
                    "Tên đăng nhập hoặc mật khẩu không đúng!\n(Hoặc tài khoản bị khóa)", 
                    "Lỗi đăng nhập", JOptionPane.ERROR_MESSAGE);
        }
    }

    // --- LOGIC 2: ĐỔI MẬT KHẨU ---
    private void xuLyDoiMatKhau() {
        String user = view.getUsernameChange();
        String oldPass = view.getOldPass();
        String newPass = view.getNewPass();
        String confirmPass = view.getConfirmPass();

        // 1. Validate rỗng
        if (user.isEmpty() || oldPass.isEmpty() || newPass.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 2. Validate mật khẩu mới trùng khớp
        if (!newPass.equals(confirmPass)) {
            JOptionPane.showMessageDialog(view, "Mật khẩu mới không trùng khớp!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 3. Kiểm tra mật khẩu cũ có đúng không (Bằng cách check login lại)
        TaiKhoan check = dao.checkLogin(user, oldPass);
        if (check == null) {
            JOptionPane.showMessageDialog(view, "Mật khẩu hiện tại không đúng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 4. Tiến hành cập nhật
        if (dao.resetPassword(user, newPass) > 0) {
            JOptionPane.showMessageDialog(view, "Đổi mật khẩu thành công! Vui lòng đăng nhập lại.");
            view.showLoginForm(); // Quay về login
        } else {
            JOptionPane.showMessageDialog(view, "Lỗi hệ thống, vui lòng thử lại sau.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}
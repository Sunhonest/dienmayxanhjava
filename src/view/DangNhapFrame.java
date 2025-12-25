package view;

import controller.NhanSu.DangNhapController;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import javax.swing.*;
import javax.swing.border.*;

public class DangNhapFrame extends JFrame {

    // --- CÁC COMPONENT FORM ĐĂNG NHẬP ---
    private JTextField txtUserLogin;
    private JPasswordField txtPassLogin;
    private JButton btnLogin, btnExit;
    private JCheckBox chkHienMatKhau;
    private JLabel lblLinkDoiMatKhau; 

    // --- CÁC COMPONENT FORM ĐỔI MẬT KHẨU ---
    private JTextField txtUserChange;
    private JPasswordField txtPassOld;
    private JPasswordField txtPassNew;
    private JPasswordField txtPassConfirm;
    private JButton btnConfirmChange, btnBack;

    // --- LAYOUT & COLOR ---
    private CardLayout cardLayout;
    private JPanel pnlRightCards;
    
    private final Color COLOR_PRIMARY = Color.decode("#2196F3"); // Xanh
    private final Color COLOR_WARNING = Color.decode("#FF9800"); // Cam
    private final Color COLOR_DANGER = Color.decode("#F44336");  // Đỏ
    
    private final String PATH_LOGO = "/icons/logo.png"; 

    public DangNhapFrame() {
        initComponents();
        // Không gọi addEvents() ở đây nữa vì Controller sẽ lo việc đó
    }

    private void initComponents() {
        setTitle("Hệ thống Quản lý Nhân sự - HRM");
        setSize(950, 550); 
        setLocationRelativeTo(null); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(1, 2)); 

        // ====================================================================
        // 1. PHẦN BÊN TRÁI (LOGO & BRAND)
        // ====================================================================
        JPanel pnlLeft = new JPanel();
        pnlLeft.setBackground(COLOR_PRIMARY);
        pnlLeft.setLayout(new GridBagLayout()); 

        JPanel pnlBrandContent = new JPanel();
        pnlBrandContent.setLayout(new BoxLayout(pnlBrandContent, BoxLayout.Y_AXIS));
        pnlBrandContent.setOpaque(false);

        JLabel lblLogo = new JLabel();
        ImageIcon iconLogo = createResizedIcon(PATH_LOGO, 140, -1); 
        if (iconLogo != null) lblLogo.setIcon(iconLogo);
        else {
            lblLogo.setText("HRM");
            lblLogo.setFont(new Font("Arial", Font.BOLD, 80));
            lblLogo.setForeground(Color.WHITE);
        }
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSubTitle = new JLabel("QUẢN LÝ NHÂN SỰ");
        lblSubTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblSubTitle.setForeground(Color.WHITE);
        lblSubTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblSlogan = new JLabel("Hiệu quả - Chuyên nghiệp - Tin cậy");
        lblSlogan.setFont(new Font("Arial", Font.ITALIC, 14));
        lblSlogan.setForeground(new Color(224, 224, 224));
        lblSlogan.setAlignmentX(Component.CENTER_ALIGNMENT);

        pnlBrandContent.add(lblLogo);
        pnlBrandContent.add(Box.createVerticalStrut(20));
        pnlBrandContent.add(lblSubTitle);
        pnlBrandContent.add(Box.createVerticalStrut(10));
        pnlBrandContent.add(lblSlogan);
        pnlLeft.add(pnlBrandContent);

        // ====================================================================
        // 2. PHẦN BÊN PHẢI
        // ====================================================================
        JPanel pnlRight = new JPanel();
        pnlRight.setBackground(Color.WHITE);
        pnlRight.setLayout(new GridBagLayout()); 

        cardLayout = new CardLayout();
        pnlRightCards = new JPanel(cardLayout);
        pnlRightCards.setBackground(Color.WHITE);
        pnlRightCards.setPreferredSize(new Dimension(380, 480)); 

        JPanel pnlLogin = createLoginForm();
        JPanel pnlChangePass = createChangePassForm();

        pnlRightCards.add(pnlLogin, "LOGIN");
        pnlRightCards.add(pnlChangePass, "CHANGE_PASS");

        pnlRight.add(pnlRightCards);

        add(pnlLeft);
        add(pnlRight);
        
        // --- QUAN TRỌNG: GÁN ACTION COMMAND ĐỂ CONTROLLER NHẬN DIỆN ---
        btnLogin.setActionCommand("LOGIN");
        btnExit.setActionCommand("EXIT");
        chkHienMatKhau.setActionCommand("SHOW_PASS");
        btnConfirmChange.setActionCommand("CONFIRM_CHANGE");
        btnBack.setActionCommand("BACK");
    }

    // ========================================================================
    // TẠO FORM ĐĂNG NHẬP
    // ========================================================================
    private JPanel createLoginForm() {
        JPanel pnl = new JPanel();
        pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));
        pnl.setBackground(Color.WHITE);
        pnl.setBorder(new EmptyBorder(0, 30, 0, 30)); 

        // TITLE
        JPanel pnlTitle = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlTitle.setBackground(Color.WHITE);
        pnlTitle.setAlignmentX(Component.LEFT_ALIGNMENT); 
        
        JLabel lblTitle = new JLabel("ĐĂNG NHẬP");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitle.setForeground(COLOR_PRIMARY);
        pnlTitle.add(lblTitle);
        
        pnl.add(pnlTitle);
        pnl.add(Box.createVerticalStrut(30));

        // INPUTS
        pnl.add(createLabel("Tên đăng nhập"));
        txtUserLogin = new JTextField();
        styleTextField(txtUserLogin);
        pnl.add(txtUserLogin);
        pnl.add(Box.createVerticalStrut(15));

        pnl.add(createLabel("Mật khẩu"));
        txtPassLogin = new JPasswordField();
        styleTextField(txtPassLogin);
        pnl.add(txtPassLogin);
        pnl.add(Box.createVerticalStrut(10));

        // OPTION
        JPanel pnlOption = new JPanel(new BorderLayout());
        pnlOption.setBackground(Color.WHITE);
        pnlOption.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        pnlOption.setAlignmentX(Component.LEFT_ALIGNMENT); 
        
        chkHienMatKhau = new JCheckBox("Hiện mật khẩu");
        chkHienMatKhau.setBackground(Color.WHITE);
        chkHienMatKhau.setFocusPainted(false);
        chkHienMatKhau.setFont(new Font("Arial", Font.PLAIN, 12));
        
        lblLinkDoiMatKhau = new JLabel("Đổi mật khẩu?");
        lblLinkDoiMatKhau.setForeground(COLOR_PRIMARY);
        lblLinkDoiMatKhau.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblLinkDoiMatKhau.setFont(new Font("Arial", Font.ITALIC | Font.BOLD, 12));

        pnlOption.add(chkHienMatKhau, BorderLayout.WEST);
        pnlOption.add(lblLinkDoiMatKhau, BorderLayout.EAST);
        
        pnl.add(pnlOption);
        pnl.add(Box.createVerticalStrut(40));

        // BUTTONS
        btnLogin = createButton("ĐĂNG NHẬP", COLOR_PRIMARY);
        btnExit = createButton("THOÁT", COLOR_DANGER);
        
        pnl.add(btnLogin);
        pnl.add(Box.createVerticalStrut(15));
        pnl.add(btnExit);

        pnl.add(Box.createVerticalGlue());
        return pnl;
    }

    // ========================================================================
    // TẠO FORM ĐỔI MẬT KHẨU
    // ========================================================================
    private JPanel createChangePassForm() {
        JPanel pnl = new JPanel();
        pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));
        pnl.setBackground(Color.WHITE);
        pnl.setBorder(new EmptyBorder(0, 30, 0, 30));

        JPanel pnlTitle = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlTitle.setBackground(Color.WHITE);
        pnlTitle.setAlignmentX(Component.LEFT_ALIGNMENT); 

        JLabel lblTitle = new JLabel("ĐỔI MẬT KHẨU");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 26));
        lblTitle.setForeground(COLOR_WARNING);
        pnlTitle.add(lblTitle);
        
        pnl.add(pnlTitle);
        pnl.add(Box.createVerticalStrut(20));

        pnl.add(createLabel("Tên đăng nhập cần đổi"));
        txtUserChange = new JTextField();
        styleTextField(txtUserChange);
        pnl.add(txtUserChange);
        pnl.add(Box.createVerticalStrut(10));

        pnl.add(createLabel("Mật khẩu hiện tại"));
        txtPassOld = new JPasswordField();
        styleTextField(txtPassOld);
        pnl.add(txtPassOld);
        pnl.add(Box.createVerticalStrut(10));

        pnl.add(createLabel("Mật khẩu mới"));
        txtPassNew = new JPasswordField();
        styleTextField(txtPassNew);
        pnl.add(txtPassNew);
        pnl.add(Box.createVerticalStrut(10));
        
        pnl.add(createLabel("Nhập lại mật khẩu mới"));
        txtPassConfirm = new JPasswordField();
        styleTextField(txtPassConfirm);
        pnl.add(txtPassConfirm);
        pnl.add(Box.createVerticalStrut(30));

        btnConfirmChange = createButton("XÁC NHẬN ĐỔI", COLOR_WARNING);
        btnBack = createButton("QUAY LẠI", Color.GRAY);

        pnl.add(btnConfirmChange);
        pnl.add(Box.createVerticalStrut(15));
        pnl.add(btnBack);

        pnl.add(Box.createVerticalGlue());
        return pnl;
    }

    // ========================================================================
    // STYLE HELPERS
    // ========================================================================
    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Arial", Font.BOLD, 13));
        lbl.setForeground(Color.GRAY);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT); 
        return lbl;
    }

    private void styleTextField(JTextField txt) {
        txt.setFont(new Font("Arial", Font.PLAIN, 15));
        txt.setForeground(Color.BLACK);
        txt.setHorizontalAlignment(JTextField.LEFT); 
        
        txt.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        txt.setBackground(Color.WHITE);
        txt.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        txt.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        txt.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                txt.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 2, 0, COLOR_PRIMARY),
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)
                ));
            }
            @Override
            public void focusLost(FocusEvent e) {
                txt.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 2, 0, Color.LIGHT_GRAY),
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)
                ));
            }
        });
    }

    private JButton createButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45)); 
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        return btn;
    }

    private ImageIcon createResizedIcon(String path, int width, int height) {
        try {
            URL imgURL = getClass().getResource(path);
            if (imgURL != null) {
                ImageIcon originalIcon = new ImageIcon(imgURL);
                Image scaledImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                return new ImageIcon(scaledImage);
            }
            return null;
        } catch (Exception e) { return null; }
    }

    // ========================================================================
    // CÁC HÀM PUBLIC API CHO CONTROLLER GỌI (MVC)
    // ========================================================================
    
    // 1. Lấy dữ liệu
    public String getUsernameLogin() { return txtUserLogin.getText().trim(); }
    public String getPasswordLogin() { return new String(txtPassLogin.getPassword()); }

    public String getUsernameChange() { return txtUserChange.getText().trim(); }
    public String getOldPass() { return new String(txtPassOld.getPassword()); }
    public String getNewPass() { return new String(txtPassNew.getPassword()); }
    public String getConfirmPass() { return new String(txtPassConfirm.getPassword()); }

    // 2. Điều khiển giao diện
    public void showLoginForm() { 
        txtPassLogin.setText(""); // Reset pass field
        cardLayout.show(pnlRightCards, "LOGIN"); 
    }
    
    public void showChangePassForm() {
        txtUserChange.setText(getUsernameLogin());
        txtPassOld.setText("");
        txtPassNew.setText("");
        txtPassConfirm.setText("");
        cardLayout.show(pnlRightCards, "CHANGE_PASS");
    }

    public void toggleShowPassword() {
        if (chkHienMatKhau.isSelected()) txtPassLogin.setEchoChar((char) 0);
        else txtPassLogin.setEchoChar('•');
    }

    // 3. Đăng ký sự kiện
    public void addBtnLoginListener(ActionListener al) { btnLogin.addActionListener(al); }
    public void addBtnExitListener(ActionListener al) { btnExit.addActionListener(al); }
    public void addChkShowPassListener(ActionListener al) { chkHienMatKhau.addActionListener(al); }
    public void addLinkChangePassListener(MouseAdapter ma) { lblLinkDoiMatKhau.addMouseListener(ma); }
    public void addBtnConfirmChangeListener(ActionListener al) { btnConfirmChange.addActionListener(al); }
    public void addBtnBackListener(ActionListener al) { btnBack.addActionListener(al); }

    // 4. MAIN TEST
    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
        SwingUtilities.invokeLater(() -> {
            DangNhapFrame view = new DangNhapFrame();
            // Kích hoạt Controller
            new DangNhapController(view); 
            view.setVisible(true);
        });
    }
}
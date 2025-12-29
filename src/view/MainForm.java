package view;

import controller.Kho.SanPhamController;
import domain.QLBH.QLDH;
import domain.TaiKhoan;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

// Import View
import view.viewQLBH.QuanLyDonHang;
import view.viewNhanSu.QuanLyChucVu;
import view.viewNhanSu.QuanLyTaiKhoan;
import view.viewNhanSu.QuanLyNhanVien;
import view.DangNhapFrame; // Để mở lại khi đăng xuất

import view.viewKho.QuanLyNhapKho;
import controller.Kho.NhapKhoController;

import view.viewBaoHanh.QuanLyBaoHanh;//import viewBaoHanh
import view.viewBaoHanh.TraCuuBaoHanh; //import viewTraCuuBaoHanh
import view.viewBaoHanh.TraMay;//import viewTraMay
// Import Controller
import controller.QLBH.QLDHController;
import controller.NhanSu.NhanVienController;
import controller.NhanSu.TaiKhoanController;
import controller.NhanSu.ChucVuController;
import controller.NhanSu.DangNhapController; // QUAN TRỌNG: Để gắn não cho form đăng nhập
import controller.QLBH.QLHDController;
import view.viewKho.QuanLySanPham;
import view.viewQLBH.QuanLyHoaDon;

public class MainForm extends JFrame {

    private JPanel pnlMenu;
    private JPanel pnlCards;
    private CardLayout cardLayout;
    
    // --- BIẾN TOÀN CỤC ĐỂ XỬ LÝ PHÂN QUYỀN ---
    private JPanel pnlNhanSuHeader; // Tiêu đề menu "Nhân sự hệ thống"
    private JPanel pnlNhanSuSub;    // Phần xổ xuống của menu Nhân sự
    private JPanel pnlItemTaiKhoan; // Mục con "Quản lý tài khoản"

    // Biến lưu tài khoản đang đăng nhập
    private TaiKhoan taiKhoanHienTai;

    // --- MÀU SẮC ---
    private final Color COLOR_PRIMARY = Color.decode("#2196F3");
    private final Color COLOR_DARK    = Color.decode("#1565C0");
    private final Color COLOR_TEXT    = Color.WHITE;
    private final Color COLOR_LOGOUT  = Color.decode("#F44336"); // Màu đỏ cho nút thoát
    
    private final Font FONT_MENU = new Font("Segoe UI", Font.BOLD, 14);
    private final Font FONT_SUB  = new Font("Segoe UI", Font.PLAIN, 14);

    // Constructor nhận Tài khoản
    public MainForm(TaiKhoan tk) {
        this.taiKhoanHienTai = tk;
        initComponents();
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        // Gọi hàm phân quyền ngay khi mở form
        phanQuyenHienThi();
    }

    private void initComponents() {
        String tenUser = (taiKhoanHienTai != null) ? taiKhoanHienTai.getTenDangNhap() : "Ẩn danh";
        setTitle("Hệ thống Quản lý Điện máy Xanh - Xin chào: " + tenUser);
        
        setSize(1300, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // --- 1. MENU TRÁI ---
        pnlMenu = new JPanel();
        pnlMenu.setLayout(new BoxLayout(pnlMenu, BoxLayout.Y_AXIS));
        pnlMenu.setBackground(COLOR_PRIMARY);
        pnlMenu.setPreferredSize(new Dimension(280, 0));

        // ==> LOGO <==
        JPanel pnlLogo = new JPanel(new BorderLayout());
        pnlLogo.setBackground(Color.WHITE);
        pnlLogo.setPreferredSize(new Dimension(280, 100)); 
        pnlLogo.setMaximumSize(new Dimension(280, 100));
        pnlLogo.setBorder(new EmptyBorder(10, 10, 10, 10)); 
        
        JLabel lblLogo = new JLabel("", SwingConstants.CENTER);
        pnlLogo.add(lblLogo, BorderLayout.CENTER);
        setLocalLogo(lblLogo, "logo.png"); 
        
        pnlMenu.add(pnlLogo);
        
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(255, 255, 255, 80));
        pnlMenu.add(sep);

        // ==> USER INFO (AVATAR + TÊN) <==
        addUserInfo();
        
        pnlMenu.add(Box.createRigidArea(new Dimension(0, 15)));

        // --- 2. MENU ITEM ---
        
        addMenuItem("Sản phẩm Kho", 
                "https://img.icons8.com/fluency/48/product.png", 
                new String[]{"Danh sách sản phẩm", "Nhập kho", "Kiểm kê", "Cảnh báo tồn"});
        
        // ==> MENU NHÂN SỰ <==
        addMenuItem("Nhân sự hệ thống", 
                "https://img.icons8.com/fluency/48/conference-call.png", 
                new String[]{
                    "Quản lý nhân viên", 
                    "Quản lý tài khoản", 
                    "Quản lý chức vụ"
                });
        
        addMenuItem("Bán hàng (POS)", 
                "https://img.icons8.com/fluency/48/pos-terminal.png", 
                new String[]{"Quản lý đơn hàng", "Quản lý hóa đơn","Thống kê"});
        
        addMenuItem("Dịch vụ bảo hành", 
                "https://img.icons8.com/fluency/48/maintenance.png",
                new String[]{"Tiếp nhận bảo hành", "Tra cứu", "Trả máy"});

        addMenuItem("Khuyến mãi & Thống kê", 
                "https://img.icons8.com/fluency/48/analytics.png", 
                new String[]{"Doanh thu ngày", "Doanh thu tháng", "Lợi nhuận"});
        
        // Đẩy nút Đăng xuất xuống đáy
        pnlMenu.add(Box.createVerticalGlue());
        // ==> NÚT ĐĂNG XUẤT <==
        addLogoutButton();
        // --- 3. NỘI DUNG PHẢI (CARD LAYOUT) ---
        cardLayout = new CardLayout();
        pnlCards = new JPanel(cardLayout);
        pnlCards.setBackground(Color.decode("#F5F5F5"));
        // Màn hình trang chủ
        JPanel pnlHome = new JPanel(new GridBagLayout());
        pnlHome.setBackground(Color.WHITE);
        pnlHome.add(new JLabel("Chào mừng " + tenUser + " đến với hệ thống!"));
        pnlCards.add(pnlHome, "Home");
        // ==> ADD VIEWS & CONTROLLERS <==
        QuanLyNhanVien viewNV = new QuanLyNhanVien();     
        new NhanVienController(viewNV);                   
        pnlCards.add(viewNV, "Quản lý nhân viên");
        
        QuanLyTaiKhoan viewTK = new QuanLyTaiKhoan();     
        new TaiKhoanController(viewTK);                   
        pnlCards.add(viewTK, "Quản lý tài khoản");
        
        QuanLyChucVu viewCV = new QuanLyChucVu(); 
        new ChucVuController(viewCV);             
        pnlCards.add(viewCV, "Quản lý chức vụ");
        
        QuanLySanPham viewSP = new QuanLySanPham();
        new SanPhamController(viewSP); // Gắn não cho view
        pnlCards.add(viewSP, "Danh sách sản phẩm");
        
       // Tìm đoạn khởi tạo NhapKhoController và sửa thành:
       QuanLyNhapKho viewNhap = new QuanLyNhapKho();
       new NhapKhoController(viewNhap, this.taiKhoanHienTai); // Truyền tài khoản vào
       pnlCards.add(viewNhap,"Nhập kho");
        
        // ===== QUẢN LÝ ĐƠN HÀNG =====
        QuanLyDonHang viewDonHang = new QuanLyDonHang();
        new QLDHController(viewDonHang);  
        pnlCards.add(viewDonHang, "Quản lý đơn hàng");

        QuanLyHoaDon viewHD = new QuanLyHoaDon(); 
        new QLHDController(viewHD);             
        pnlCards.add(viewHD, "Quản lý hóa đơn");
        

        // ... (Code các phần khác giữ nguyên)

        // 1. Màn hình Tiếp nhận
        QuanLyBaoHanh viewTiepNhan = new QuanLyBaoHanh();
        // new BaoHanhController(viewTiepNhan); // Tạm thời comment lại nếu chưa code xong Controller
        pnlCards.add(viewTiepNhan, "Tiếp nhận bảo hành");

        // --- SỬA ĐOẠN NÀY ĐỂ HIỆN GIAO DIỆN TRA CỨU ---
        
        // Bước 1: Khởi tạo giao diện (View)
        TraCuuBaoHanh viewTraCuu = new TraCuuBaoHanh(); 
        
        // Bước 2: Controller (VÌ BẠN CHƯA VIẾT NÊN ĐỪNG GỌI NÓ, HOẶC COMMENT LẠI)
        // new TraCuuController(viewTraCuu); <== Comment dòng này lại để không bị báo lỗi đỏ
        
        // Bước 3: THÊM VÀO CARD LAYOUT (QUAN TRỌNG NHẤT)
        // Cái tên "Tra cứu" trong ngoặc kép PHẢI GIỐNG Y HỆT tên trong menu addMenuItem ở trên
        pnlCards.add(viewTraCuu, "Tra cứu"); 

        // ----------------------------------------------

        // 3. Màn hình Trả máy
        TraMay viewTraMay = new TraMay();
        // new TraMayController(viewTraMay); // Comment lại nếu chưa có
        pnlCards.add(viewTraMay, "Trả máy");

        // ...
        add(new JScrollPane(pnlMenu), BorderLayout.WEST);
        add(pnlCards, BorderLayout.CENTER);     
    }
    // HÀM HIỂN THỊ THÔNG TIN USER (NEW)
    private void addUserInfo() {
        JPanel pnlUser = new JPanel(new BorderLayout());
        pnlUser.setBackground(COLOR_PRIMARY);
        pnlUser.setMaximumSize(new Dimension(280, 80));
        pnlUser.setBorder(new EmptyBorder(15, 20, 15, 10));

        // 1. Xác định Icon và Chức danh dựa vào Quyền
        String roleName = "NHÂN VIÊN";
        String iconLink = "https://img.icons8.com/fluency/48/employee-card.png"; 
        int quyen = (taiKhoanHienTai != null) ? taiKhoanHienTai.getCapDoQuyen() : 0;

        if (quyen == 3) {
            roleName = "QUẢN TRỊ VIÊN (ADMIN)";
            iconLink = "https://img.icons8.com/fluency/48/admin-settings-male.png"; 
        } else if (quyen == 2) {
            roleName = "QUẢN LÝ";
            iconLink = "https://img.icons8.com/fluency/48/manager.png";
        }

        // 2. Tạo Label Icon
        JLabel lblAvatar = new JLabel();
        ImageIcon icon = loadIconFromWeb(iconLink);
        if(icon != null) {
            Image img = icon.getImage().getScaledInstance(45, 45, Image.SCALE_SMOOTH);
            lblAvatar.setIcon(new ImageIcon(img));
        }
        
        // 3. Tạo Panel chứa Tên và Chức danh
        JPanel pnlText = new JPanel();
        pnlText.setLayout(new BoxLayout(pnlText, BoxLayout.Y_AXIS));
        pnlText.setBackground(COLOR_PRIMARY);
        pnlText.setBorder(new EmptyBorder(0, 15, 0, 0)); 

        String tenUser = (taiKhoanHienTai != null) ? taiKhoanHienTai.getTenDangNhap() : "Guest";
        JLabel lblName = new JLabel(tenUser);
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblName.setForeground(Color.WHITE);

        JLabel lblRole = new JLabel(roleName);
        lblRole.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblRole.setForeground(new Color(200, 230, 255)); // Màu xanh nhạt

        pnlText.add(lblName);
        pnlText.add(Box.createVerticalStrut(3));
        pnlText.add(lblRole);

        pnlUser.add(lblAvatar, BorderLayout.WEST);
        pnlUser.add(pnlText, BorderLayout.CENTER);

        pnlMenu.add(pnlUser);
        
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(255, 255, 255, 40));
        pnlMenu.add(sep);
    }
    // HÀM TẠO NÚT ĐĂNG XUẤT (CÓ GẮN CONTROLLER)
    private void addLogoutButton() {
        JPanel pnlLogout = new JPanel(new BorderLayout());
        pnlLogout.setBackground(COLOR_LOGOUT); 
        pnlLogout.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        pnlLogout.setBorder(new EmptyBorder(0, 15, 0, 15));
        pnlLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel lblLeft = new JLabel("Đăng xuất");
        lblLeft.setFont(FONT_MENU);
        lblLeft.setForeground(Color.WHITE);
        
        ImageIcon icon = loadIconFromWeb("https://img.icons8.com/fluency/48/exit.png");
        if (icon != null) {
            lblLeft.setIcon(icon);
            lblLeft.setIconTextGap(15);
        }

        pnlLogout.add(lblLeft, BorderLayout.CENTER);

        pnlLogout.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int choice = JOptionPane.showConfirmDialog(MainForm.this, 
                        "Bạn có chắc chắn muốn đăng xuất?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    // 1. Đóng Main Form
                    dispose(); 
                    
                    // 2. Tạo màn hình Đăng nhập mới
                    DangNhapFrame loginView = new DangNhapFrame();
                    
                    // 3. GẮN CONTROLLER ĐỂ NÓ HOẠT ĐỘNG (QUAN TRỌNG)
                    new DangNhapController(loginView);
                    
                    // 4. Hiển thị
                    loginView.setVisible(true); 
                }
            }
            @Override
            public void mouseEntered(MouseEvent e) { pnlLogout.setBackground(COLOR_LOGOUT.darker()); }
            @Override
            public void mouseExited(MouseEvent e) { pnlLogout.setBackground(COLOR_LOGOUT); }
        });

        pnlMenu.add(pnlLogout);
    }
    // HÀM PHÂN QUYỀN
    private void phanQuyenHienThi() {
        if (taiKhoanHienTai == null) return;
        
        int quyen = taiKhoanHienTai.getCapDoQuyen();
        
        // --- LEVEL 1: NHÂN VIÊN ---
        if (quyen == 1) {
            if (pnlNhanSuHeader != null) pnlNhanSuHeader.setVisible(false);
            if (pnlNhanSuSub != null) pnlNhanSuSub.setVisible(false);
        } 
        
        // --- LEVEL 2: QUẢN LÝ ---
        else if (quyen == 2) {
            if (pnlNhanSuHeader != null) pnlNhanSuHeader.setVisible(true);
            if (pnlNhanSuSub != null) pnlNhanSuSub.setVisible(true);
            
            // Ẩn item Quản lý tài khoản
            if (pnlItemTaiKhoan != null) pnlItemTaiKhoan.setVisible(false);
        }
    }
    // HÀM TẠO MENU (CÓ BẮT REFERENCE ĐỂ PHÂN QUYỀN)
    private void addMenuItem(String name, String iconUrlLink, String[] subItems) {
        JPanel pnlParent = new JPanel(new BorderLayout());
        pnlParent.setBackground(COLOR_PRIMARY);
        pnlParent.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        pnlParent.setBorder(new EmptyBorder(0, 15, 0, 15));
        pnlParent.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel lblLeft = new JLabel(name);
        lblLeft.setFont(FONT_MENU);
        lblLeft.setForeground(COLOR_TEXT);
        
        ImageIcon icon = loadIconFromWeb(iconUrlLink);
        if (icon != null) {
            lblLeft.setIcon(icon);
            lblLeft.setIconTextGap(15);
        }

        JLabel lblRight = new JLabel("\u25B6");
        lblRight.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 12));
        lblRight.setForeground(COLOR_TEXT);

        pnlParent.add(lblLeft, BorderLayout.WEST);
        pnlParent.add(lblRight, BorderLayout.EAST);

        JPanel pnlSub = new JPanel();
        pnlSub.setLayout(new BoxLayout(pnlSub, BoxLayout.Y_AXIS));
        pnlSub.setBackground(COLOR_DARK);
        pnlSub.setVisible(false);

        if (name.equals("Nhân sự hệ thống")) {
            this.pnlNhanSuHeader = pnlParent;
            this.pnlNhanSuSub = pnlSub;
        }

        for (String item : subItems) {
            JPanel pnlItem = new JPanel(new BorderLayout());
            pnlItem.setBackground(COLOR_DARK);
            pnlItem.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            pnlItem.setBorder(new EmptyBorder(0, 58, 0, 10));
            pnlItem.setCursor(new Cursor(Cursor.HAND_CURSOR));

            JLabel lblItem = new JLabel(item);
            lblItem.setFont(FONT_SUB);
            lblItem.setForeground(COLOR_TEXT);
            pnlItem.add(lblItem, BorderLayout.CENTER);

            if (item.equals("Quản lý tài khoản")) {
                this.pnlItemTaiKhoan = pnlItem;
            }

            pnlItem.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { pnlItem.setBackground(COLOR_PRIMARY); }
                public void mouseExited(MouseEvent e) { pnlItem.setBackground(COLOR_DARK); }
                public void mousePressed(MouseEvent e) { 
                    cardLayout.show(pnlCards, item); 
                }
            });
            pnlSub.add(pnlItem);
        }

        pnlParent.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                boolean isOpening = !pnlSub.isVisible();
                pnlSub.setVisible(isOpening);
                if (isOpening) {
                    lblRight.setText("\u25BC");
                    pnlParent.setBackground(COLOR_DARK);
                } else {
                    lblRight.setText("\u25B6");
                    pnlParent.setBackground(COLOR_PRIMARY);
                }
                pnlMenu.revalidate();
                pnlMenu.repaint();
            }
            @Override
            public void mouseEntered(MouseEvent e) { pnlParent.setBackground(COLOR_DARK); }
            @Override
            public void mouseExited(MouseEvent e) { if (!pnlSub.isVisible()) pnlParent.setBackground(COLOR_PRIMARY); }
        });

        pnlMenu.add(pnlParent);
        pnlMenu.add(pnlSub);
    }

    // === HELPERS ===
    private ImageIcon loadIconFromWeb(String link) {
        try {
            URL url = new URL(link);
            Image image = ImageIO.read(url).getScaledInstance(24, 24, Image.SCALE_SMOOTH);
            return new ImageIcon(image);
        } catch (Exception e) {}
        return null;
    }

    private void setLocalLogo(JLabel label, String fileName) {
        try {
            URL url = getClass().getResource("/icons/" + fileName);
            if (url != null) {
                ImageIcon originalIcon = new ImageIcon(url);
                int originalW = originalIcon.getIconWidth();
                int originalH = originalIcon.getIconHeight();
                int targetW = 240; 
                int targetH = 90; 
                double ratio = Math.min((double)targetW / originalW, (double)targetH / originalH);
                int newW = (int)(originalW * ratio);
                int newH = (int)(originalH * ratio);
                Image img = originalIcon.getImage().getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
                label.setIcon(new ImageIcon(img));
            } else {
                label.setText("LOGO");
                label.setForeground(Color.RED);
            }
        } catch (Exception e) {}
    }
    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
        
        SwingUtilities.invokeLater(() -> {
            // Test Admin (Level 3)
            TaiKhoan adminMock = new TaiKhoan();
            adminMock.setTenDangNhap("Admin_Test");
            adminMock.setCapDoQuyen(3); 
            
            new MainForm(adminMock).setVisible(true);
        });
    }
}
package view.viewBaoHanh;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

public class TraMay extends JPanel {

    // Components
    private JTextField txtMaPhieu;
    private JButton btnCheck, btnXacNhanTra;
    private JTextArea txtThongTinPhieu; 
    
    // Nút chức năng phụ
    private JButton btnSuaPhieu, btnHuyPhieu;

    // Colors
    private final Color COLOR_BG_LEFT   = Color.WHITE;
    private final Color COLOR_BG_RIGHT  = Color.decode("#E9ECEF"); 
    private final Color COLOR_PRIMARY   = Color.decode("#0097D8"); // Xanh dương
    private final Color COLOR_SUCCESS   = Color.decode("#28A745"); 
    private final Color COLOR_DANGER    = Color.decode("#E74C3C"); 
    private final Color COLOR_WARNING   = Color.decode("#FFC107");
    private final Color COLOR_DISABLED  = Color.decode("#BDC3C7");

    public TraMay() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout()); 

        // ====================================================================
        // 1. HEADER
        // ====================================================================
        JPanel pnlHeader = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 15));
        pnlHeader.setBackground(Color.WHITE);
        pnlHeader.setBorder(new MatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        
        JLabel lblTitle = new JLabel("BÀN GIAO & TRẢ MÁY");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(COLOR_PRIMARY);
        pnlHeader.add(lblTitle);
        add(pnlHeader, BorderLayout.NORTH);

        // ====================================================================
        // 2. LEFT PANEL: KHU VỰC ĐIỀU KHIỂN
        // ====================================================================
        JPanel pnlLeft = new JPanel();
        pnlLeft.setLayout(new GridBagLayout());
        pnlLeft.setBackground(COLOR_BG_LEFT);
        pnlLeft.setPreferredSize(new Dimension(400, 0)); // Giảm chiều rộng panel trái chút cho bên phải rộng hơn
        pnlLeft.setBorder(new EmptyBorder(20, 30, 20, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; 
        gbc.gridy = 0; 
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 10, 0); 
        gbc.weightx = 1.0;

        // --- A. Nhập Mã Phiếu ---
        pnlLeft.add(createLabel("NHẬP MÃ PHIẾU BẢO HÀNH"), gbc);
        
        gbc.gridy++;
        JPanel pnlSearch = new JPanel(new BorderLayout(5, 0));
        pnlSearch.setBackground(COLOR_BG_LEFT);
        
        txtMaPhieu = new JTextField();
        txtMaPhieu.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtMaPhieu.setPreferredSize(new Dimension(200, 45)); // Ô nhập cao hơn chút
        txtMaPhieu.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Color.LIGHT_GRAY, 1), new EmptyBorder(0, 10, 0, 10)
        ));
        
        btnCheck = new JButton("CHECK");
        styleButton(btnCheck, COLOR_SUCCESS);
        btnCheck.setPreferredSize(new Dimension(80, 45));
        
        pnlSearch.add(txtMaPhieu, BorderLayout.CENTER);
        pnlSearch.add(btnCheck, BorderLayout.EAST);
        pnlLeft.add(pnlSearch, gbc);

        // --- B. Nút Xác Nhận Trả (Đẩy lên ngay dưới ô tìm kiếm) ---
        gbc.gridy++;
        gbc.insets = new Insets(30, 0, 15, 0); // Cách ô tìm kiếm 30px
        
        btnXacNhanTra = new JButton("XÁC NHẬN TRẢ KHÁCH");
        styleButton(btnXacNhanTra, COLOR_PRIMARY); // Màu xanh dương
        btnXacNhanTra.setPreferredSize(new Dimension(0, 55)); // Nút to, rõ ràng
        btnXacNhanTra.setEnabled(false); // Mặc định ẩn, hiện khi check xong
        btnXacNhanTra.setBackground(COLOR_DISABLED);
        pnlLeft.add(btnXacNhanTra, gbc);

        // --- C. Nhóm nút phụ (Sửa/Hủy) ---
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 10, 0);
        JPanel pnlSub = new JPanel(new GridLayout(1, 2, 10, 0));
        pnlSub.setBackground(COLOR_BG_LEFT);
        
        btnSuaPhieu = new JButton("Sửa phiếu");
        styleButton(btnSuaPhieu, COLOR_WARNING);
        btnSuaPhieu.setForeground(Color.BLACK);
        
        btnHuyPhieu = new JButton("Hủy phiếu");
        styleButton(btnHuyPhieu, COLOR_DANGER);
        
        pnlSub.add(btnSuaPhieu);
        pnlSub.add(btnHuyPhieu);
        pnlLeft.add(pnlSub, gbc);

        // Đẩy nội dung lên trên cùng
        gbc.gridy++;
        gbc.weighty = 1.0; 
        pnlLeft.add(Box.createGlue(), gbc);

        add(pnlLeft, BorderLayout.WEST);

        // ====================================================================
        // 3. RIGHT PANEL: HÓA ĐƠN (RỘNG RA)
        // ====================================================================
        // Sử dụng BorderLayout thay vì GridBagLayout để giấy giãn hết cỡ
        JPanel pnlRight = new JPanel(new BorderLayout()); 
        pnlRight.setBackground(COLOR_BG_RIGHT);
        // Tạo khoảng đệm (padding) để thấy nền xám xung quanh tờ giấy
        pnlRight.setBorder(new EmptyBorder(20, 20, 20, 20)); 
        
        // Tờ giấy trắng
        JPanel pnlPaper = new JPanel(new BorderLayout());
        pnlPaper.setBackground(Color.WHITE);
        // Bỏ setPreferredSize(500, 600) để giấy tự giãn
        
        // Hiệu ứng bóng đổ (Shadow)
        pnlPaper.setBorder(new CompoundBorder(
            new LineBorder(Color.LIGHT_GRAY, 1),
            new EmptyBorder(20, 30, 20, 30)
        ));

        JLabel lblPaperHeader = new JLabel("PHIẾU BÀN GIAO & BẢO HÀNH");
        lblPaperHeader.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblPaperHeader.setHorizontalAlignment(SwingConstants.CENTER);
        lblPaperHeader.setBorder(new EmptyBorder(0, 0, 20, 0));
        pnlPaper.add(lblPaperHeader, BorderLayout.NORTH);

        txtThongTinPhieu = new JTextArea();
        txtThongTinPhieu.setFont(new Font("Monospaced", Font.PLAIN, 15)); // Chữ to hơn xíu cho dễ đọc
        txtThongTinPhieu.setEditable(false);
        txtThongTinPhieu.setLineWrap(true); // Tự động xuống dòng nếu hóa đơn quá rộng
        txtThongTinPhieu.setWrapStyleWord(true);
        txtThongTinPhieu.setText("\n  (Thông tin phiếu sẽ hiện tại đây...)");
        
        // Bọc ScrollPane nhưng bỏ border của scrollpane để nhìn liền mạch
        JScrollPane scroll = new JScrollPane(txtThongTinPhieu);
        scroll.setBorder(null);
        pnlPaper.add(scroll, BorderLayout.CENTER);

        pnlRight.add(pnlPaper, BorderLayout.CENTER); // Add vào Center để lấp đầy
        add(pnlRight, BorderLayout.CENTER);
    }
    
    // --- HELPERS ---
    private JLabel createLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 12));
        l.setForeground(Color.GRAY);
        return l;
    }

    private void styleButton(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    // Getters & Setters
    public String getMaPhieuInput() { return txtMaPhieu.getText().trim(); }
    public void setThongTinHienThi(String text) { txtThongTinPhieu.setText(text); }
    
    // Đã xóa hàm setStatusHeader vì không còn nhãn trạng thái
    // Nếu Controller của bạn vẫn gọi hàm này, bạn có thể uncomment dòng dưới để tránh lỗi code:
    /*
    public void setStatusHeader(String text, boolean isSuccess) {
        // Không làm gì hoặc hiện thông báo nhỏ
    }
    */

    public void setEnableButtonTra(boolean enable) { 
        btnXacNhanTra.setEnabled(enable); 
        btnXacNhanTra.setBackground(enable ? COLOR_PRIMARY : COLOR_DISABLED);
    }
    
    public JButton getBtnCheck() { return btnCheck; }
    public JButton getBtnXacNhan() { return btnXacNhanTra; }
    public JButton getBtnSua() { return btnSuaPhieu; }
    public JButton getBtnHuy() { return btnHuyPhieu; }
}
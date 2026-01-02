package view.viewBaoHanh;

import domain.PhieuBaoHanh;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

public class TraMay extends JPanel {

    // Components
    private JTextField txtMaPhieu; // Ô nhập tìm kiếm
    private JButton btnCheck, btnXacNhanTra;
    private JTextArea txtThongTinPhieu; // Tờ giấy bên phải (In hóa đơn)
    
    // --- CÁC TRƯỜNG HIỂN THỊ CHI TIẾT (MỚI THÊM) ---
    private JTextField txtTenKhach, txtSDT, txtTenSP, txtSerial, txtTrangThai;
    private JTextArea txtLoi; // Lỗi dùng TextArea cho rộng

    // Colors
    private final Color COLOR_BG_LEFT   = Color.WHITE;
    private final Color COLOR_BG_RIGHT  = Color.decode("#E9ECEF"); 
    private final Color COLOR_PRIMARY   = Color.decode("#0097D8"); 
    private final Color COLOR_SUCCESS   = Color.decode("#28A745"); 
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
        // 2. LEFT PANEL: KHU VỰC TÌM KIẾM & THÔNG TIN (SỬA LẠI)
        // ====================================================================
        JPanel pnlLeft = new JPanel();
        pnlLeft.setLayout(new BoxLayout(pnlLeft, BoxLayout.Y_AXIS)); // Xếp dọc
        pnlLeft.setBackground(COLOR_BG_LEFT);
        pnlLeft.setPreferredSize(new Dimension(450, 0)); // Rộng hơn chút để chứa form
        pnlLeft.setBorder(new EmptyBorder(20, 30, 20, 30));

        // --- A. Tìm kiếm ---
        pnlLeft.add(createLabel("NHẬP MÃ PHIẾU BẢO HÀNH:"));
        pnlLeft.add(Box.createVerticalStrut(5));
        
        JPanel pnlSearch = new JPanel(new BorderLayout(5, 0));
        pnlSearch.setBackground(COLOR_BG_LEFT);
        pnlSearch.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        pnlSearch.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        txtMaPhieu = new JTextField();
        styleTextField(txtMaPhieu);
        
        btnCheck = new JButton("CHECK");
        styleButton(btnCheck, COLOR_SUCCESS);
        btnCheck.setPreferredSize(new Dimension(80, 40));
        
        pnlSearch.add(txtMaPhieu, BorderLayout.CENTER);
        pnlSearch.add(btnCheck, BorderLayout.EAST);
        pnlLeft.add(pnlSearch);
        
        pnlLeft.add(Box.createVerticalStrut(20)); // Khoảng cách

        // --- B. Form Thông tin chi tiết (MỚI THÊM) ---
        // Group Khách hàng
        pnlLeft.add(createLabel("Tên khách hàng:"));
        txtTenKhach = createReadOnlyField();
        pnlLeft.add(txtTenKhach);
        pnlLeft.add(Box.createVerticalStrut(10));
        
        pnlLeft.add(createLabel("Số điện thoại:"));
        txtSDT = createReadOnlyField();
        pnlLeft.add(txtSDT);
        pnlLeft.add(Box.createVerticalStrut(10));

        // Group Sản phẩm
        pnlLeft.add(createLabel("Tên sản phẩm:"));
        txtTenSP = createReadOnlyField();
        pnlLeft.add(txtTenSP);
        pnlLeft.add(Box.createVerticalStrut(10));
        
        pnlLeft.add(createLabel("Số Serial/IMEI:"));
        txtSerial = createReadOnlyField();
        pnlLeft.add(txtSerial);
        pnlLeft.add(Box.createVerticalStrut(10));
        
        // Group Tình trạng
        pnlLeft.add(createLabel("Mô tả lỗi ban đầu:"));
        txtLoi = new JTextArea(3, 20);
        txtLoi.setLineWrap(true);
        txtLoi.setEditable(false);
        txtLoi.setBackground(new Color(245, 245, 245));
        txtLoi.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        JScrollPane scrLoi = new JScrollPane(txtLoi);
        scrLoi.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrLoi.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        pnlLeft.add(scrLoi);
        pnlLeft.add(Box.createVerticalStrut(10));
        
        pnlLeft.add(createLabel("Trạng thái hiện tại:"));
        txtTrangThai = createReadOnlyField();
        txtTrangThai.setFont(new Font("Segoe UI", Font.BOLD, 14));
        txtTrangThai.setForeground(Color.RED); // Nổi bật trạng thái
        pnlLeft.add(txtTrangThai);
        
        pnlLeft.add(Box.createVerticalStrut(30)); // Cách nút bấm xa ra

        // --- C. Nút Xác Nhận Trả ---
        btnXacNhanTra = new JButton("XÁC NHẬN TRẢ KHÁCH");
        styleButton(btnXacNhanTra, COLOR_PRIMARY); 
        btnXacNhanTra.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        btnXacNhanTra.setEnabled(false); 
        btnXacNhanTra.setBackground(COLOR_DISABLED);
        btnXacNhanTra.setAlignmentX(Component.LEFT_ALIGNMENT);
        pnlLeft.add(btnXacNhanTra);

        pnlLeft.add(Box.createVerticalGlue()); // Đẩy mọi thứ lên trên
        add(pnlLeft, BorderLayout.WEST);

        // ====================================================================
        // 3. RIGHT PANEL: HÓA ĐƠN (GIỮ NGUYÊN ĐỂ IN PHIẾU)
        // ====================================================================
        JPanel pnlRight = new JPanel(new BorderLayout()); 
        pnlRight.setBackground(COLOR_BG_RIGHT);
        pnlRight.setBorder(new EmptyBorder(20, 20, 20, 20)); 
        
        JPanel pnlPaper = new JPanel(new BorderLayout());
        pnlPaper.setBackground(Color.WHITE);
        pnlPaper.setBorder(new CompoundBorder(
            new LineBorder(Color.LIGHT_GRAY, 1),
            new EmptyBorder(20, 30, 20, 30)
        ));

        JLabel lblPaperHeader = new JLabel("BIÊN BẢN BÀN GIAO");
        lblPaperHeader.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblPaperHeader.setHorizontalAlignment(SwingConstants.CENTER);
        lblPaperHeader.setBorder(new EmptyBorder(0, 0, 20, 0));
        pnlPaper.add(lblPaperHeader, BorderLayout.NORTH);

        txtThongTinPhieu = new JTextArea();
        txtThongTinPhieu.setFont(new Font("Monospaced", Font.PLAIN, 14)); 
        txtThongTinPhieu.setEditable(false);
        txtThongTinPhieu.setLineWrap(true);
        txtThongTinPhieu.setText("\n(Phiếu trả khách sẽ hiển thị tại đây sau khi xác nhận)");
        
        JScrollPane scroll = new JScrollPane(txtThongTinPhieu);
        scroll.setBorder(null);
        pnlPaper.add(scroll, BorderLayout.CENTER);

        pnlRight.add(pnlPaper, BorderLayout.CENTER);
        add(pnlRight, BorderLayout.CENTER);
    }
    
    // --- HELPERS ---
    private JLabel createLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 12));
        l.setForeground(Color.DARK_GRAY);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private void styleTextField(JTextField t) {
        t.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        t.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        t.setPreferredSize(new Dimension(100, 35));
    }
    
    private JTextField createReadOnlyField() {
        JTextField t = new JTextField();
        styleTextField(t);
        t.setEditable(false);
        t.setBackground(new Color(245, 245, 245));
        t.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        t.setAlignmentX(Component.LEFT_ALIGNMENT);
        return t;
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
    
    public void setEnableButtonTra(boolean enable) { 
        btnXacNhanTra.setEnabled(enable); 
        btnXacNhanTra.setBackground(enable ? COLOR_PRIMARY : COLOR_DISABLED);
    }
    
    // Hàm mới: Đổ dữ liệu vào Form chi tiết
    public void fillFormChiTiet(PhieuBaoHanh p) {
        txtTenKhach.setText(p.getTenKhachHang());
        txtSDT.setText(p.getSoDienThoai());
        txtTenSP.setText(p.getTenSP());
        txtSerial.setText(p.getSoSerial());
        txtLoi.setText(p.getMoTaLoi());
        txtTrangThai.setText(p.getTrangThai());
    }
    
    // Hàm mới: Reset Form
    public void clearForm() {
        txtTenKhach.setText("");
        txtSDT.setText("");
        txtTenSP.setText("");
        txtSerial.setText("");
        txtLoi.setText("");
        txtTrangThai.setText("");
    }
    
    public JButton getBtnCheck() { return btnCheck; }
    public JButton getBtnXacNhan() { return btnXacNhanTra; }
}
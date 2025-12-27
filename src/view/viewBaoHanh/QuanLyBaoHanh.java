package view.viewBaoHanh;

import domain.PhieuBaoHanh;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Date;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class QuanLyBaoHanh extends JPanel {

    // --- COMPONENTS ---
    private JTable tblBaoHanh;
    private DefaultTableModel model;
    
    // Input Fields
    private JTextField txtMaPhieu, txtSoSerial, txtMaHoaDon, txtMaSP;
    private JTextField txtTenKhach, txtSDT, txtNgayTiepNhan;
    private JTextArea txtMoTaLoi; 
    private JComboBox<String> cboTrangThai; 
    
    // Action Buttons
    private JButton btnTiepNhan, btnCapNhat, btnXoa, btnLamMoi;
    private JButton btnCheckSerial; // Nút nhỏ check serial

    // --- COLORS (Lấy từ ảnh mẫu) ---
    private final Color COLOR_HEADER_BG = Color.decode("#0097D8"); // Xanh dương header bảng
    private final Color COLOR_TITLE     = Color.decode("#0097D8"); // Xanh tiêu đề lớn
    private final Color COLOR_BTN_GREEN = Color.decode("#4CAF50"); // Nút Thêm
    private final Color COLOR_BTN_BLUE  = Color.decode("#0097D8"); // Nút Làm mới
    private final Color COLOR_BTN_GRAY  = Color.decode("#B0B0B0"); // Nút Sửa/Xóa (khi chưa chọn)
    
    private final Font FONT_BIG_TITLE = new Font("Arial", Font.BOLD, 24);
    private final Font FONT_SUB_TITLE = new Font("Arial", Font.BOLD, 16);
    private final Font FONT_LABEL     = new Font("Arial", Font.BOLD, 12);
    private final Font FONT_TEXT      = new Font("Arial", Font.PLAIN, 14);

    public QuanLyBaoHanh() {
        initComponents();
        setTrangThaiNut(false);
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10)); // Gap nhỏ
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // ====================================================================
        // 1. TIÊU ĐỀ LỚN: "TIẾP NHẬN BẢO HÀNH" (Giống QUẢN LÝ NHÂN SỰ)
        // ====================================================================
        JLabel lblTitle = new JLabel("TIẾP NHẬN BẢO HÀNH"); // Hoặc QUẢN LÝ BẢO HÀNH
        lblTitle.setFont(FONT_BIG_TITLE);
        lblTitle.setForeground(COLOR_TITLE);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        // Padding bottom cho tiêu đề
        lblTitle.setBorder(new EmptyBorder(0, 0, 15, 0)); 
        add(lblTitle, BorderLayout.NORTH);

        // ====================================================================
        // 2. CENTER: BẢNG DỮ LIỆU (HEADER XANH, CHỮ TRẮNG)
        // ====================================================================
        String[] columns = {
            "Mã Phiếu", "Mã SP", "Serial/IMEI", "Khách hàng", "SĐT", "Ngày nhận", "Trạng thái"
        };
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        tblBaoHanh = new JTable(model);
        styleTable(tblBaoHanh); // Hàm style nằm ở dưới
        
        JScrollPane scrTable = new JScrollPane(tblBaoHanh);
        scrTable.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        add(scrTable, BorderLayout.CENTER);

        // ====================================================================
        // 3. EAST: PANEL FORM (BÊN PHẢI - GIỐNG ẢNH MẪU)
        // ====================================================================
        JPanel pnlRight = new JPanel();
        pnlRight.setLayout(new BorderLayout()); // Để nút bấm dính xuống đáy
        pnlRight.setBackground(Color.WHITE);
        pnlRight.setPreferredSize(new Dimension(350, 0)); // Kích thước cố định bề ngang
        pnlRight.setBorder(new CompoundBorder(
            new MatteBorder(0, 1, 0, 0, Color.LIGHT_GRAY), // Đường kẻ dọc ngăn cách
            new EmptyBorder(0, 15, 0, 0) // Padding nội dung
        ));

        // --- 3.1 HEADER FORM: "THÔNG TIN CHI TIẾT" ---
        JLabel lblSubTitle = new JLabel("THÔNG TIN CHI TIẾT");
        lblSubTitle.setFont(FONT_SUB_TITLE);
        lblSubTitle.setForeground(COLOR_TITLE);
        lblSubTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblSubTitle.setBorder(new EmptyBorder(0, 0, 15, 0));
        pnlRight.add(lblSubTitle, BorderLayout.NORTH);

        // --- 3.2 FIELDS: CÁC Ô NHẬP LIỆU ---
        // Dùng Box Layout Y_AXIS để xếp chồng dọc
        JPanel pnlFields = new JPanel();
        pnlFields.setLayout(new BoxLayout(pnlFields, BoxLayout.Y_AXIS));
        pnlFields.setBackground(Color.WHITE);

        // Mã phiếu
        txtMaPhieu = createField(pnlFields, "Mã phiếu:");
        txtMaPhieu.setEnabled(false);

        // Serial + Button Check (Gom chung 1 dòng)
        pnlFields.add(createLabel("Số Serial/IMEI:"));
        JPanel pnlSer = new JPanel(new BorderLayout(5, 0));
        pnlSer.setBackground(Color.WHITE);
        pnlSer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        pnlSer.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        txtSoSerial = new JTextField(); styleTextField(txtSoSerial);
        btnCheckSerial = new JButton("Check"); 
        btnCheckSerial.setBackground(Color.decode("#FF9800"));
        btnCheckSerial.setForeground(Color.BLACK);
        btnCheckSerial.setFocusPainted(false);
        
        pnlSer.add(txtSoSerial, BorderLayout.CENTER);
        pnlSer.add(btnCheckSerial, BorderLayout.EAST);
        pnlFields.add(pnlSer);
        pnlFields.add(Box.createVerticalStrut(10));

        // Các ô khác
        // Gom Mã HĐ và Mã SP vào 1 dòng cho gọn (vì form Nhân sự ít trường hơn)
        JPanel pnlGop = new JPanel(new GridLayout(1, 2, 5, 0));
        pnlGop.setBackground(Color.WHITE);
        pnlGop.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        pnlGop.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel p1 = createSubPanel("Mã Hóa Đơn:"); txtMaHoaDon = new JTextField(); styleTextField(txtMaHoaDon); txtMaHoaDon.setEnabled(false); p1.add(txtMaHoaDon);
        JPanel p2 = createSubPanel("Mã Sản Phẩm:"); txtMaSP = new JTextField(); styleTextField(txtMaSP); txtMaSP.setEnabled(false); p2.add(txtMaSP);
        pnlGop.add(p1); pnlGop.add(p2);
        pnlFields.add(pnlGop);
        
        txtTenKhach = createField(pnlFields, "Họ và tên khách:");
        txtSDT = createField(pnlFields, "Số điện thoại:");
        
        pnlFields.add(createLabel("Mô tả lỗi:"));
        txtMoTaLoi = new JTextArea(3, 20);
        txtMoTaLoi.setFont(FONT_TEXT);
        txtMoTaLoi.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        txtMoTaLoi.setLineWrap(true);
        JScrollPane scrLoi = new JScrollPane(txtMoTaLoi);
        scrLoi.setAlignmentX(Component.LEFT_ALIGNMENT);
        pnlFields.add(scrLoi);
        pnlFields.add(Box.createVerticalStrut(10));
        
        // Trạng thái (Combobox)
        pnlFields.add(createLabel("Trạng thái:"));
        cboTrangThai = new JComboBox<>(new String[]{
            "Mới tiếp nhận", "Đang sửa chữa", "Đã xong", "Đã trả khách"
        });
        cboTrangThai.setFont(FONT_TEXT);
        cboTrangThai.setBackground(Color.WHITE);
        cboTrangThai.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        cboTrangThai.setAlignmentX(Component.LEFT_ALIGNMENT);
        pnlFields.add(cboTrangThai);
        
        // Ngày nhận (Ẩn đi hoặc hiện read-only)
        txtNgayTiepNhan = new JTextField(java.time.LocalDate.now().toString());
        
        pnlRight.add(pnlFields, BorderLayout.CENTER);

        // --- 3.3 BUTTONS: 2x2 GRID Ở DƯỚI CÙNG (GIỐNG ẢNH MẪU) ---
        JPanel pnlButtons = new JPanel(new GridLayout(2, 2, 10, 10)); // 2 hàng, 2 cột, gap 10
        pnlButtons.setBackground(Color.WHITE);
        pnlButtons.setBorder(new EmptyBorder(20, 0, 0, 0)); // Cách form 1 đoạn
        pnlButtons.setPreferredSize(new Dimension(350, 100)); // Chiều cao khu vực nút

        // Tạo nút giống hệt ảnh
        btnTiepNhan = createButton("Thêm", COLOR_BTN_GREEN); // Nút Thêm (Tiếp nhận)
        btnTiepNhan.setActionCommand("TiepNhan");
        
        btnCapNhat = createButton("Sửa", COLOR_BTN_GRAY);    // Nút Sửa
        btnCapNhat.setActionCommand("CapNhat");
        
        btnXoa = createButton("Xóa", COLOR_BTN_GRAY);        // Nút Xóa
        btnXoa.setActionCommand("Xoa");
        
        btnLamMoi = createButton("Làm mới", COLOR_BTN_BLUE); // Nút Làm mới
        btnLamMoi.setActionCommand("LamMoi");

        // Thứ tự add vào grid: Trái trên -> Phải trên -> Trái dưới -> Phải dưới
        pnlButtons.add(btnTiepNhan);
        pnlButtons.add(btnCapNhat);
        pnlButtons.add(btnXoa);
        pnlButtons.add(btnLamMoi);

        pnlRight.add(pnlButtons, BorderLayout.SOUTH);

        add(pnlRight, BorderLayout.EAST);
    }

    // ========================================================================
    // HELPER METHODS (STYLE GIỐNG ẢNH)
    // ========================================================================

    private void styleTable(JTable table) {
        // 1. Cài đặt style cho phần thân bảng (Rows)
        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setGridColor(Color.LIGHT_GRAY);
        table.setShowVerticalLines(true); // Kẻ dọc
        table.setSelectionBackground(Color.decode("#D6EAF8")); // Màu khi chọn dòng
        table.setSelectionForeground(Color.BLACK);

        // 2. Cài đặt style cho phần Tiêu đề (Header)
        JTableHeader header = table.getTableHeader();
        header.setPreferredSize(new Dimension(0, 40)); // Chiều cao header
        
        // --- SỬA LỖI MẤT CHỮ TẠI ĐÂY ---
        // Gán một bộ Render thủ công để ép màu nền và màu chữ
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                // Gọi hàm gốc để lấy JLabel mặc định
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                // Thiết lập Font & Màu sắc
                label.setFont(new Font("Arial", Font.BOLD, 14));
                label.setBackground(COLOR_HEADER_BG); // Màu nền Xanh (#0097D8)
                label.setForeground(Color.WHITE);     // Màu chữ Trắng
                label.setHorizontalAlignment(JLabel.CENTER);
                
                // [QUAN TRỌNG] Phải có dòng này màu nền mới hiện ra
                label.setOpaque(true); 
                
                // Thêm đường kẻ dọc màu trắng ngăn cách các cột cho đẹp
                label.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.WHITE));
                
                return label;
            }
        });
    }

    private JTextField createField(JPanel p, String text) {
        p.add(createLabel(text)); 
        JTextField txt = new JTextField(); 
        styleTextField(txt);
        p.add(txt); 
        p.add(Box.createVerticalStrut(10)); // Khoảng cách giữa các ô
        return txt;
    }

    private JLabel createLabel(String text) {
        JLabel l = new JLabel(text); 
        l.setFont(FONT_LABEL); 
        l.setForeground(Color.BLACK);
        l.setAlignmentX(Component.LEFT_ALIGNMENT); 
        return l;
    }

    private void styleTextField(JTextField t) {
        t.setFont(FONT_TEXT); 
        t.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        t.setPreferredSize(new Dimension(100, 30));
        t.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY)); // Viền đơn mảnh
        t.setAlignmentX(Component.LEFT_ALIGNMENT);
    }
    
    private JPanel createSubPanel(String l) {
        JPanel p = new JPanel(); p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(Color.WHITE); p.add(createLabel(l)); return p;
    }

    // Tạo nút phẳng, màu đặc
    private JButton createButton(String text, Color bg) {
        JButton b = new JButton(text);
        b.setBackground(bg);
        b.setForeground(Color.WHITE); // Chữ trắng
        b.setFont(new Font("Arial", Font.BOLD, 14));
        b.setFocusPainted(false);
        b.setBorderPainted(false); // Không viền lồi
        return b;
    }

    // ========================================================================
    // LOGIC & DATA
    // ========================================================================

    public void setTrangThaiNut(boolean dangChonRow) {
        btnTiepNhan.setEnabled(!dangChonRow);
        btnCapNhat.setEnabled(dangChonRow);
        btnXoa.setEnabled(dangChonRow);
        
        // Đổi màu khi enable/disable để giống ảnh
        // Nếu chọn dòng -> Nút Thêm xám, Nút Sửa/Xóa hiện màu (Vàng/Đỏ hoặc giữ xám đậm)
        // Ở đây tôi set màu đặc trưng để dễ nhìn
        if (dangChonRow) {
            btnTiepNhan.setBackground(COLOR_BTN_GRAY);
            btnCapNhat.setBackground(Color.decode("#FFC107")); // Vàng khi active
            btnXoa.setBackground(Color.decode("#F44336"));     // Đỏ khi active
        } else {
            btnTiepNhan.setBackground(COLOR_BTN_GREEN);
            btnCapNhat.setBackground(COLOR_BTN_GRAY);
            btnXoa.setBackground(COLOR_BTN_GRAY);
        }
    }

    public void resetForm() {
        txtMaPhieu.setText("");
        txtSoSerial.setText("");
        txtMaHoaDon.setText("");
        txtMaSP.setText("");
        txtTenKhach.setText("");
        txtSDT.setText("");
        txtMoTaLoi.setText("");
        if(cboTrangThai.getItemCount() > 0) cboTrangThai.setSelectedIndex(0);
        
        txtSoSerial.setEnabled(true);
        tblBaoHanh.clearSelection();
        setTrangThaiNut(false);
    }

    public PhieuBaoHanh getPhieuTuForm() {
        PhieuBaoHanh p = new PhieuBaoHanh();
        p.setMaPhieu(txtMaPhieu.getText().trim());
        p.setSoSerial(txtSoSerial.getText().trim());
        p.setMaHoaDon(txtMaHoaDon.getText().trim());
        p.setMaSP(txtMaSP.getText().trim());
        p.setTenKhachHang(txtTenKhach.getText().trim());
        p.setSoDienThoai(txtSDT.getText().trim());
        p.setMoTaLoi(txtMoTaLoi.getText().trim());
        p.setTrangThai(cboTrangThai.getSelectedItem().toString());
        
        try {
            p.setNgayTiepNhan(java.sql.Date.valueOf(txtNgayTiepNhan.getText()));
        } catch (Exception e) {
            p.setNgayTiepNhan(new Date());
        }
        return p;
    }

    public void fillForm(PhieuBaoHanh p) {
        txtMaPhieu.setText(p.getMaPhieu());
        txtSoSerial.setText(p.getSoSerial());
        txtMaHoaDon.setText(p.getMaHoaDon());
        txtMaSP.setText(p.getMaSP());
        txtTenKhach.setText(p.getTenKhachHang());
        txtSDT.setText(p.getSoDienThoai());
        txtMoTaLoi.setText(p.getMoTaLoi());
        
        // Map trạng thái từ DB sang Combobox
        String stt = p.getTrangThai();
        // Cần map lại nếu trong DB lưu ENUM (MOI_TIEP_NHAN) sang Tiếng Việt
        // Ở đây giả sử DB lưu gì hiện nấy, hoặc bạn dùng logic cũ
        cboTrangThai.setSelectedItem(stt); 
        
        setTrangThaiNut(true);
    }

    public void addBtnListener(ActionListener al) {
        btnTiepNhan.addActionListener(al);
        btnCapNhat.addActionListener(al);
        btnXoa.addActionListener(al);
        btnLamMoi.addActionListener(al);
        btnCheckSerial.addActionListener(al);
    }

    public JTable getTable() { return tblBaoHanh; }
    public DefaultTableModel getModel() { return model; }
}
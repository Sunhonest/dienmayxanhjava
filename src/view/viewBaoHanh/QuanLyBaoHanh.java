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
    private JTextField txtTenSP;

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
        // --- TÌM DÒNG NÀY VÀ THAY THẾ ---
        String[] columns = {
            "Mã Phiếu", "Mã SP", "Tên SP", "Serial/IMEI", "Khách hàng", "SĐT", "Ngày nhận", "Trạng thái", "Lỗi"
        };
        // (Lưu ý: Mình đã thêm "Tên SP" và "Lỗi" vào danh sách này)
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
        // --- 3.2 FIELDS: CÁC Ô NHẬP LIỆU ---
        JPanel pnlFields = new JPanel();
        pnlFields.setLayout(new BoxLayout(pnlFields, BoxLayout.Y_AXIS));
        pnlFields.setBackground(Color.WHITE);

        // 1. Mã phiếu (Giữ nguyên)
        txtMaPhieu = createField(pnlFields, "Mã phiếu:");
        txtMaPhieu.setEnabled(true); // Cho phép nhập tay (hoặc để false nếu dùng tự động sinh)

        // ====================================================================
        // 2. ĐỔI MỚI: MÃ HÓA ĐƠN + NÚT CHECK (Đưa lên trên)
        // ====================================================================
        pnlFields.add(createLabel("Mã Hóa Đơn (Nhập để tra cứu):"));
        JPanel pnlCheckRow = new JPanel(new BorderLayout(5, 0));
        pnlCheckRow.setBackground(Color.WHITE);
        pnlCheckRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        pnlCheckRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Khởi tạo txtMaHoaDon tại đây
        txtMaHoaDon = new JTextField(); 
        styleTextField(txtMaHoaDon);
        txtMaHoaDon.setEnabled(true); // QUAN TRỌNG: Phải mở để nhập thì mới check được
        
        // Nút Check
        btnCheckSerial = new JButton("Check"); 
        btnCheckSerial.setBackground(Color.decode("#FF9800"));
        btnCheckSerial.setForeground(Color.BLACK);
        btnCheckSerial.setFocusPainted(false);
        // Đặt ActionCommand để Controller bắt được
        btnCheckSerial.setActionCommand("Check"); 
        
        pnlCheckRow.add(txtMaHoaDon, BorderLayout.CENTER);
        pnlCheckRow.add(btnCheckSerial, BorderLayout.EAST);
        
        pnlFields.add(pnlCheckRow);
        pnlFields.add(Box.createVerticalStrut(10));

        // Khai báo biến txtTenSP ở đầu class hoặc ngay đây nếu lười (nhưng tốt nhất là khai báo private JTextField txtTenSP ở đầu class nhé)
        // Nếu chưa khai báo ở đầu file thì thêm dòng này vào đầu class: private JTextField txtTenSP;

        // ====================================================================
        // 3. ĐỔI MỚI: SERIAL + MÃ SP + TÊN SP
        // ====================================================================
        
        // --- Dòng 1: Số Serial ---
        pnlFields.add(createLabel("Số Serial/IMEI thực tế:"));
        txtSoSerial = new JTextField(); styleTextField(txtSoSerial);
        pnlFields.add(txtSoSerial);
        pnlFields.add(Box.createVerticalStrut(5));

        // --- Dòng 2: Mã SP và Tên SP (Gom chung 1 hàng) ---
        JPanel pnlSP = new JPanel(new GridLayout(1, 2, 5, 0));
        pnlSP.setBackground(Color.WHITE);
        pnlSP.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        pnlSP.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Cột Mã SP
        JPanel pMa = createSubPanel("Mã Sản Phẩm:"); 
        txtMaSP = new JTextField(); styleTextField(txtMaSP); txtMaSP.setEnabled(false);
        pMa.add(txtMaSP);
        
        // Cột Tên SP (MỚI)
        JPanel pTen = createSubPanel("Tên Sản Phẩm:"); 
        txtTenSP = new JTextField(); styleTextField(txtTenSP); txtTenSP.setEnabled(false);
        
        pTen.add(txtTenSP); // Nhớ khai báo biến txtTenSP ở đầu file nhé!

        pnlSP.add(pMa);
        pnlSP.add(pTen);
        pnlFields.add(pnlSP);
        
        // 4. Các ô còn lại (Giữ nguyên)
        txtTenKhach = createField(pnlFields, "Họ và tên khách:");
        txtSDT = createField(pnlFields, "Số điện thoại:");
        
        pnlFields.add(createLabel("Mô tả lỗi:"));
        txtMoTaLoi = new JTextArea(3, 20);
        // ... (Giữ nguyên phần còn lại bên dưới của bạn) ...
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
        cboTrangThai.setEnabled(false);
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
        if (txtTenSP != null) txtTenSP.setText("");
        txtTenKhach.setText("");
        txtSDT.setText("");
        txtMoTaLoi.setText("");
        
        cboTrangThai.setSelectedItem("Mới tiếp nhận");
        cboTrangThai.setEnabled(false);
       // if(cboTrangThai.getItemCount() > 0) cboTrangThai.setSelectedIndex(0);
        
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
        // --- BẮT ĐẦU SỬA ---
String selectedText = cboTrangThai.getSelectedItem().toString();
String dbValue = "MOI_TIEP_NHAN"; // Mặc định

switch (selectedText) {
    case "Mới tiếp nhận":
        dbValue = "MOI_TIEP_NHAN";
        break;
    case "Đang sửa chữa":
        dbValue = "DANG_SUA";
        break;
    case "Đã xong":
        dbValue = "DA_XONG";
        break;
    case "Đã trả khách":
        dbValue = "DA_TRA";
        break;
}
p.setTrangThai(dbValue);
// --- KẾT THÚC SỬA ---
        
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
        if (txtTenSP != null) txtTenSP.setText(p.getTenSP());
        // Map trạng thái từ DB sang Combobox
        String stt = p.getTrangThai();
        // Cần map lại nếu trong DB lưu ENUM (MOI_TIEP_NHAN) sang Tiếng Việt
        // Ở đây giả sử DB lưu gì hiện nấy, hoặc bạn dùng logic cũ
        // Map từ DB (Tiếng Anh) sang View (Tiếng Việt)
String dbStatus = p.getTrangThai();
switch (dbStatus) {
    case "MOI_TIEP_NHAN": cboTrangThai.setSelectedItem("Mới tiếp nhận"); break;
    case "DANG_SUA":      cboTrangThai.setSelectedItem("Đang sửa chữa"); break;
    case "DA_XONG":       cboTrangThai.setSelectedItem("Đã xong"); break;
    case "DA_TRA":        cboTrangThai.setSelectedItem("Đã trả khách"); break;
    default:              cboTrangThai.setSelectedIndex(0);
}
        cboTrangThai.setEnabled(true);
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

public JTextField getTxtMaHoaDon() {
        return txtMaHoaDon;
    }

    // Hàm điền dữ liệu tự động khi Check thành công
    // Sửa lại hàm này để nhận 4 tham số
    public void fillThongTinKiemTra(String maSP, String tenKhach, String sdt, String tenSP) {
        txtMaSP.setText(maSP);
        txtTenKhach.setText(tenKhach);
        txtSDT.setText(sdt);
        txtTenSP.setText(tenSP); // <-- Điền tên sản phẩm vào ô mới
    }
}
// Thêm vào cuối file QuanLyBaoHanh.java

    
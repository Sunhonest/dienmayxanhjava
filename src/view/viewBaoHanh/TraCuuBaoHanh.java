package view.viewBaoHanh;

import domain.PhieuBaoHanh;
import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class TraCuuBaoHanh extends JPanel {

    // Components
    private JTextField txtTuKhoa;
    private JButton btnTimKiem;
    private JTable tblKetQua;
    private DefaultTableModel model;

    // --- CÁC NÚT CHỨC NĂNG ---
    private JButton btnThemMoi, btnSua, btnXoa;

    // --- CÁC TRƯỜNG CHI TIẾT ---
    private JTextField txtMaPhieu, txtMaHoaDon, txtSerial, txtMaSP, txtTenSP;
    private JTextField txtKhachHang, txtSDT, txtNgayNhan;
    
    // --- THAY ĐỔI: Dùng ComboBox cho trạng thái ---
    private JComboBox<String> cboTrangThai; 
    // ---------------------------------------------
    
    private JTextArea txtMoTaLoi;

    // Colors
    private final Color COLOR_PRIMARY = Color.decode("#0097D8");
    private final Color COLOR_BG      = Color.WHITE;
    private final Color COLOR_BTN_ADD = Color.decode("#28A745");
    private final Color COLOR_BTN_EDIT= Color.decode("#FFC107");
    private final Color COLOR_BTN_DEL = Color.decode("#E74C3C");

    public TraCuuBaoHanh() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(COLOR_BG);
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // 1. HEADER
        JPanel pnlTop = new JPanel(new BorderLayout());
        pnlTop.setBackground(COLOR_BG);
        
        JLabel lblTitle = new JLabel("TRA CỨU & QUẢN LÝ LỊCH SỬ BẢO HÀNH");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(COLOR_PRIMARY);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        pnlTop.add(lblTitle, BorderLayout.NORTH);

        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlSearch.setBackground(COLOR_BG);
        txtTuKhoa = new JTextField(30);
        txtTuKhoa.setPreferredSize(new Dimension(300, 35));
        txtTuKhoa.setBorder(BorderFactory.createLineBorder(COLOR_PRIMARY));
        
        btnTimKiem = new JButton("Tìm kiếm");
        styleButton(btnTimKiem, COLOR_PRIMARY);
        btnTimKiem.setPreferredSize(new Dimension(100, 35));

        pnlSearch.add(new JLabel("Từ khóa: "));
        pnlSearch.add(txtTuKhoa);
        pnlSearch.add(btnTimKiem);
        pnlTop.add(pnlSearch, BorderLayout.CENTER);
        add(pnlTop, BorderLayout.NORTH);

        // 2. TABLE
        String[] columns = {
            "Mã Phiếu", "Mã SP", "Tên SP", "Serial", "Khách hàng", "SĐT", "Ngày nhận", "Trạng thái"
        };
        model = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tblKetQua = new JTable(model);
        styleTable(tblKetQua);
        
        JScrollPane scrTable = new JScrollPane(tblKetQua);
        scrTable.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        add(scrTable, BorderLayout.CENTER);

        // 3. CHI TIẾT (BÊN PHẢI)
        JPanel pnlRight = new JPanel(new BorderLayout());
        pnlRight.setBackground(COLOR_BG);
        pnlRight.setPreferredSize(new Dimension(350, 0));
        pnlRight.setBorder(new CompoundBorder(
            new MatteBorder(0, 1, 0, 0, Color.LIGHT_GRAY),
            new EmptyBorder(0, 15, 0, 0)
        ));

        JPanel pnlFields = new JPanel();
        pnlFields.setLayout(new BoxLayout(pnlFields, BoxLayout.Y_AXIS));
        pnlFields.setBackground(COLOR_BG);

        pnlFields.add(createLabelHeader("CHI TIẾT PHIẾU"));
        
        txtMaPhieu   = createReadOnlyField(pnlFields, "Mã phiếu:");
        txtMaHoaDon  = createReadOnlyField(pnlFields, "Mã hóa đơn gốc:");
        txtSerial    = createReadOnlyField(pnlFields, "Số Serial/IMEI:");
        txtMaSP      = createReadOnlyField(pnlFields, "Mã sản phẩm:");
        txtTenSP     = createReadOnlyField(pnlFields, "Tên sản phẩm:");
        txtKhachHang = createReadOnlyField(pnlFields, "Khách hàng:");
        txtSDT       = createReadOnlyField(pnlFields, "Số điện thoại:");
        txtNgayNhan  = createReadOnlyField(pnlFields, "Ngày tiếp nhận:");
        
        // --- SỬA TRẠNG THÁI THÀNH COMBOBOX ---
        pnlFields.add(createLabel("Trạng thái hiện tại:"));
        cboTrangThai = new JComboBox<>(new String[]{
            "Mới tiếp nhận", "Đang sửa chữa", "Đã xong", "Đã trả khách"
        });
        cboTrangThai.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cboTrangThai.setBackground(new Color(245, 245, 245));
        cboTrangThai.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        cboTrangThai.setAlignmentX(Component.LEFT_ALIGNMENT);
        cboTrangThai.setEnabled(false); // Mặc định khóa
        pnlFields.add(cboTrangThai);
        pnlFields.add(Box.createVerticalStrut(5));
        // -------------------------------------
        
        pnlFields.add(createLabel("Mô tả lỗi:"));
        txtMoTaLoi = new JTextArea(3, 20);
        txtMoTaLoi.setLineWrap(true);
        txtMoTaLoi.setEditable(false);
        txtMoTaLoi.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        pnlFields.add(new JScrollPane(txtMoTaLoi));
        
        pnlFields.add(Box.createVerticalGlue());
        pnlRight.add(pnlFields, BorderLayout.CENTER);

        JPanel pnlActions = new JPanel(new GridLayout(1, 3, 5, 0));
        pnlActions.setBackground(COLOR_BG);
        pnlActions.setBorder(new EmptyBorder(10, 0, 0, 0));
        pnlActions.setPreferredSize(new Dimension(0, 40));

        btnThemMoi = new JButton("Thêm");
        styleButton(btnThemMoi, COLOR_BTN_ADD);
        
        btnSua = new JButton("Sửa");
        styleButton(btnSua, COLOR_BTN_EDIT);
        btnSua.setForeground(Color.BLACK);
        
        btnXoa = new JButton("Xóa");
        styleButton(btnXoa, COLOR_BTN_DEL);

        pnlActions.add(btnThemMoi);
        pnlActions.add(btnSua);
        pnlActions.add(btnXoa);
        
        pnlRight.add(pnlActions, BorderLayout.SOUTH);
        add(pnlRight, BorderLayout.EAST);
    }

    // --- HELPERS STYLE ---
    private void styleTable(JTable table) {
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setGridColor(new Color(230, 230, 230));
        table.setShowVerticalLines(true);
        table.setSelectionBackground(Color.decode("#D6EAF8"));
        table.setSelectionForeground(Color.BLACK);
        
        JTableHeader header = table.getTableHeader();
        header.setPreferredSize(new Dimension(0, 40));
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                label.setBackground(COLOR_PRIMARY);
                label.setForeground(Color.WHITE);
                label.setHorizontalAlignment(JLabel.CENTER);
                label.setOpaque(true);
                label.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.WHITE));
                return label;
            }
        });
    }

    private void styleButton(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private JTextField createReadOnlyField(JPanel p, String text) {
        p.add(createLabel(text));
        JTextField txt = new JTextField();
        txt.setEditable(false);
        txt.setBackground(new Color(245, 245, 245));
        txt.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        txt.setPreferredSize(new Dimension(100, 30));
        txt.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        txt.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(txt);
        p.add(Box.createVerticalStrut(5));
        return txt;
    }

    private JLabel createLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 12));
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }
    
    private JLabel createLabelHeader(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 16));
        l.setForeground(COLOR_PRIMARY);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        l.setBorder(new EmptyBorder(0, 0, 10, 0));
        return l;
    }

    public String getTuKhoa() { return txtTuKhoa.getText().trim(); }
    public DefaultTableModel getModel() { return model; }
    public JTable getTable() { return tblKetQua; }
    
    // Đổ dữ liệu vào form (ĐÃ CẬP NHẬT LOGIC COMBOBOX)
    public void setThongTinChiTiet(PhieuBaoHanh p) {
        txtMaPhieu.setText(p.getMaPhieu());
        txtMaHoaDon.setText(p.getMaHoaDon());
        txtSerial.setText(p.getSoSerial());
        txtMaSP.setText(p.getMaSP());
        txtTenSP.setText(p.getTenSP());
        txtKhachHang.setText(p.getTenKhachHang());
        txtSDT.setText(p.getSoDienThoai());
        txtNgayNhan.setText(p.getNgayTiepNhan().toString());
        txtMoTaLoi.setText(p.getMoTaLoi());
        
        // Map từ DB (Tiếng Anh) sang Tiếng Việt cho ComboBox
        String dbStatus = p.getTrangThai();
        switch (dbStatus) {
            case "MOI_TIEP_NHAN": cboTrangThai.setSelectedItem("Mới tiếp nhận"); break;
            case "DANG_SUA":      cboTrangThai.setSelectedItem("Đang sửa chữa"); break;
            case "DA_XONG":       cboTrangThai.setSelectedItem("Đã xong"); break;
            case "DA_TRA":        cboTrangThai.setSelectedItem("Đã trả khách"); break;
            default:              cboTrangThai.setSelectedIndex(0);
        }
    }
    
    public void addBtnListener(ActionListener al) {
        btnTimKiem.addActionListener(al);
        btnThemMoi.addActionListener(al);
        btnSua.addActionListener(al);
        btnXoa.addActionListener(al);
    }
    
    public JButton getBtnSua() {
        return btnSua;
    }

    public JTextField getTxtMaPhieu() {
        return txtMaPhieu;
    }

    // Bật/Tắt sửa (ĐÃ CẬP NHẬT CHO COMBOBOX)
    public void batCheDoSua(boolean enable) {
        Color bg = enable ? Color.WHITE : new Color(245, 245, 245);
        
        txtSerial.setEditable(enable);
        txtSerial.setBackground(bg);
        
        cboTrangThai.setEnabled(enable); // Mở khóa ComboBox
        cboTrangThai.setBackground(bg);
        
        txtMoTaLoi.setEditable(enable);
        txtMoTaLoi.setBackground(bg);
    }

    public void setBtnSuaText(String text) {
        btnSua.setText(text);
        if (text.equals("Lưu")) {
            btnSua.setBackground(Color.decode("#007BFF")); 
        } else {
            btnSua.setBackground(Color.decode("#FFC107")); 
        }
    }

    // Lấy thông tin từ form để Update (ĐÃ CẬP NHẬT LOGIC COMBOBOX)
    public PhieuBaoHanh getPhieuCapNhat() {
        PhieuBaoHanh p = new PhieuBaoHanh();
        p.setMaPhieu(txtMaPhieu.getText());
        p.setSoSerial(txtSerial.getText());
        p.setMoTaLoi(txtMoTaLoi.getText());
        
        // Lấy tiếng Việt từ ComboBox đổi về mã DB để lưu
        String selectedText = cboTrangThai.getSelectedItem().toString();
        String dbValue = "MOI_TIEP_NHAN"; // Mặc định

        switch (selectedText) {
            case "Mới tiếp nhận": dbValue = "MOI_TIEP_NHAN"; break;
            case "Đang sửa chữa": dbValue = "DANG_SUA"; break;
            case "Đã xong":       dbValue = "DA_XONG"; break;
            case "Đã trả khách":  dbValue = "DA_TRA"; break;
        }
        p.setTrangThai(dbValue);
        
        return p;
    }
}
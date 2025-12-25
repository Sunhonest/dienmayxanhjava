package view.viewNhanSu;

import domain.TaiKhoan;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class QuanLyTaiKhoan extends JPanel {

    // Components
    private JTable tblTaiKhoan;
    private DefaultTableModel model;
    
    private JTextField txtMaNV, txtTenDangNhap;
    // Dùng JPasswordField cho mật khẩu để bảo mật, hoặc JTextField nếu muốn hiện rõ
    private JTextField txtMatKhau; 
    private JComboBox<String> cboQuyen; 
    private JComboBox<String> cboTrangThai;
    
    // 4 Nút chức năng xếp 2x2
    private JButton btnThem, btnSua, btnXoa, btnLamMoi;
    
    // Popup gợi ý cho ô Mã NV
    private JPopupMenu popupGoiY; 

    private final Color COLOR_PRIMARY = Color.decode("#2196F3");
    private final Font FONT_HEADER = new Font("Arial", Font.BOLD, 24);
    private final Font FONT_LABEL = new Font("Arial", Font.BOLD, 14);

    public QuanLyTaiKhoan() {
        initComponents();
        setTrangThaiNut(false); 
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // --- TITLE ---
        JLabel lblTitle = new JLabel("QUẢN LÝ TÀI KHOẢN");
        lblTitle.setFont(FONT_HEADER);
        lblTitle.setForeground(COLOR_PRIMARY);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblTitle, BorderLayout.NORTH);

        // --- TABLE ---
        String[] columns = {"Tên đăng nhập", "Mã NV", "Quyền hạn", "Trạng thái"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        tblTaiKhoan = new JTable(model);
        tblTaiKhoan.setRowHeight(35);
        tblTaiKhoan.setFont(new Font("Arial", Font.PLAIN, 14));
        tblTaiKhoan.setSelectionBackground(Color.decode("#BBDEFB"));
        tblTaiKhoan.setSelectionForeground(Color.BLACK);
        tblTaiKhoan.setShowGrid(true);
        tblTaiKhoan.setGridColor(Color.LIGHT_GRAY);
        
        JTableHeader header = tblTaiKhoan.getTableHeader();
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 40));
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                label.setFont(new Font("Arial", Font.BOLD, 15));
                label.setBackground(COLOR_PRIMARY);
                label.setForeground(Color.WHITE);
                label.setHorizontalAlignment(JLabel.CENTER);
                label.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.WHITE));
                return label;
            }
        });

        add(new JScrollPane(tblTaiKhoan), BorderLayout.CENTER);

        // --- FORM INPUT (BÊN PHẢI - GIỐNG QUẢN LÝ NHÂN VIÊN) ---
        JPanel pnlInput = new JPanel();
        pnlInput.setLayout(new BoxLayout(pnlInput, BoxLayout.Y_AXIS)); // Xếp dọc
        pnlInput.setBackground(Color.WHITE);
        pnlInput.setPreferredSize(new Dimension(350, 0)); // Kích thước cố định 350
        pnlInput.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.LIGHT_GRAY, 1, true),
                new EmptyBorder(20, 20, 20, 20)
        ));

        // 1. TIÊU ĐỀ FORM
        JPanel pnlTitleWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlTitleWrapper.setBackground(Color.WHITE);
        pnlTitleWrapper.setAlignmentX(Component.LEFT_ALIGNMENT); 
        pnlTitleWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JLabel lblForm = new JLabel("THÔNG TIN TÀI KHOẢN");
        lblForm.setFont(new Font("Arial", Font.BOLD, 18));
        lblForm.setForeground(COLOR_PRIMARY);
        pnlTitleWrapper.add(lblForm);
        
        pnlInput.add(pnlTitleWrapper);
        pnlInput.add(Box.createVerticalStrut(20)); // Khoảng cách tới ô nhập liệu

        // 2. CÁC Ô NHẬP LIỆU
        // Mã nhân viên (Có popup gợi ý)
        txtMaNV = createField(pnlInput, "Mã nhân viên (Gõ để tìm):");
        popupGoiY = new JPopupMenu(); // Init popup
        
        txtTenDangNhap = createField(pnlInput, "Tên đăng nhập:");
        
        // Mật khẩu
        txtMatKhau = createField(pnlInput, "Mật khẩu:");
        
        // Quyền hạn
        pnlInput.add(createLabel("Quyền hạn:"));
        cboQuyen = new JComboBox<>(new String[]{"Nhân viên", "Quản lý (Level 2)", "Admin (Level 3)"});
        styleComponent(cboQuyen);
        pnlInput.add(cboQuyen);
        pnlInput.add(Box.createVerticalStrut(15));

        // Trạng thái
        pnlInput.add(createLabel("Trạng thái:"));
        cboTrangThai = new JComboBox<>(new String[]{"Hoạt động", "Đã khóa"});
        styleComponent(cboTrangThai);
        pnlInput.add(cboTrangThai);
        pnlInput.add(Box.createVerticalStrut(15));
        
        // Đẩy nút xuống đáy panel (Giống QLNV)
        pnlInput.add(Box.createVerticalGlue()); 

        // 3. NÚT BẤM (GRID 2x2)
        JPanel pnlBtnWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlBtnWrapper.setBackground(Color.WHITE);
        pnlBtnWrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Grid layout cho nút: 2 hàng, 2 cột -> Rất đẹp
        JPanel pnlGridButtons = new JPanel(new GridLayout(2, 2, 10, 10)); 
        pnlGridButtons.setBackground(Color.WHITE);
        pnlGridButtons.setPreferredSize(new Dimension(300, 90)); // Chiều cao vừa đủ cho 2 hàng
        
        btnThem = createButton("Them", "Thêm", Color.decode("#4CAF50"));
        btnSua = createButton("Sua", "Sửa", Color.decode("#FFC107"));
        btnXoa = createButton("Xoa", "Xóa", Color.decode("#F44336"));
        btnLamMoi = createButton("LamMoi", "Làm mới", COLOR_PRIMARY);

        // Hàng 1
        pnlGridButtons.add(btnThem);
        pnlGridButtons.add(btnSua);
        // Hàng 2
        pnlGridButtons.add(btnXoa);
        pnlGridButtons.add(btnLamMoi);

        pnlBtnWrapper.add(pnlGridButtons);
        pnlInput.add(pnlBtnWrapper);
        
        add(pnlInput, BorderLayout.EAST);
    }

    // === HELPER UI ===
    private JTextField createField(JPanel panel, String labelText) {
        panel.add(createLabel(labelText));
        JTextField txt = new JTextField();
        styleComponent(txt);
        panel.add(txt);
        panel.add(Box.createVerticalStrut(15));
        return txt;
    }

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(FONT_LABEL);
        lbl.setForeground(Color.BLACK);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT); 
        return lbl;
    }

    private void styleComponent(JComponent comp) {
        comp.setFont(new Font("Arial", Font.PLAIN, 14));
        comp.setBackground(Color.WHITE);
        comp.setForeground(Color.BLACK);
        comp.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        comp.setAlignmentX(Component.LEFT_ALIGNMENT); 
    }

    private JButton createButton(String command, String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setActionCommand(command);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // =================================================================
    // === LOGIC MÀU SẮC NÚT ===
    // =================================================================

    public void setTrangThaiNut(boolean dangChonHang) {
        btnThem.setEnabled(!dangChonHang); 
        btnSua.setEnabled(dangChonHang);
        btnXoa.setEnabled(dangChonHang);
        
        btnThem.setBackground(!dangChonHang ? Color.decode("#4CAF50") : Color.LIGHT_GRAY);
        btnSua.setBackground(dangChonHang ? Color.decode("#FFC107") : Color.LIGHT_GRAY);
        btnXoa.setBackground(dangChonHang ? Color.decode("#F44336") : Color.LIGHT_GRAY);
    }

    // === API CONTROLLER ===

    public void addActionListener(ActionListener ac) {
        btnThem.addActionListener(ac);
        btnSua.addActionListener(ac);
        btnXoa.addActionListener(ac);
        btnLamMoi.addActionListener(ac);
    }

    public void resetForm() {
        txtTenDangNhap.setText("");
        txtMatKhau.setText("");
        txtMaNV.setText("");
        
        cboQuyen.setSelectedIndex(0);
        cboTrangThai.setSelectedIndex(0);
        
        txtTenDangNhap.setEnabled(true);
        txtMaNV.setEnabled(true);
        
        setTrangThaiNut(false); 
        tblTaiKhoan.clearSelection();
    }

    public TaiKhoan getTaiKhoanFromInput() {
        TaiKhoan tk = new TaiKhoan();
        tk.setTenDangNhap(txtTenDangNhap.getText().trim());
        tk.setMatKhau(txtMatKhau.getText().trim());
        tk.setMaNV(txtMaNV.getText().trim());
        
        // Map quyền từ combobox về int
        String quyenStr = cboQuyen.getSelectedItem().toString();
        if(quyenStr.contains("Admin")) tk.setCapDoQuyen(3);
        else if(quyenStr.contains("Quản lý")) tk.setCapDoQuyen(2);
        else tk.setCapDoQuyen(1); // Nhân viên
        
        tk.setTrangThai(cboTrangThai.getSelectedItem().toString());
        return tk;
    }

    public void fillFormTuBang() {
        int row = tblTaiKhoan.getSelectedRow();
        if (row >= 0) {
            txtTenDangNhap.setText(tblTaiKhoan.getValueAt(row, 0).toString());
            // txtTenDangNhap.setEnabled(false); // Thường username ko cho sửa, tùy bạn
            
            txtMaNV.setText(tblTaiKhoan.getValueAt(row, 1).toString());
            // txtMaNV.setEnabled(false);
            
            // Xử lý fill combobox Quyền
            String quyen = tblTaiKhoan.getValueAt(row, 2).toString();
            if(quyen.contains("Admin")) cboQuyen.setSelectedItem("Admin (Level 3)");
            else if(quyen.contains("Quản lý")) cboQuyen.setSelectedItem("Quản lý (Level 2)");
            else cboQuyen.setSelectedItem("Nhân viên");
            
            cboTrangThai.setSelectedItem(tblTaiKhoan.getValueAt(row, 3).toString());
            
            setTrangThaiNut(true);
        }
    }
    
    // API cho Gợi ý Search Mã NV
    public void hienThiGoiY(List<String> suggestions) {
        popupGoiY.setVisible(false);
        popupGoiY.removeAll();
        if (suggestions.isEmpty()) return;

        for (String s : suggestions) {
            JMenuItem item = new JMenuItem(s);
            item.addActionListener(e -> {
                txtMaNV.setText(s.split(" - ")[0]); // Lấy mã NV
                popupGoiY.setVisible(false);
            });
            popupGoiY.add(item);
        }
        popupGoiY.show(txtMaNV, 0, txtMaNV.getHeight());
        txtMaNV.requestFocus();
    }

    // Getters
    public String getUsernameDangChon() {
        int row = tblTaiKhoan.getSelectedRow();
        if (row >= 0) return tblTaiKhoan.getValueAt(row, 0).toString();
        return null;
    }
    
    public String getUsernameCu() {
        // Hàm này dùng để lưu username trước khi sửa (nếu cho phép sửa username)
        // Hiện tại tạm lấy từ bảng ra
        return getUsernameDangChon();
    }

    public DefaultTableModel getModel() { return model; }
    public JTable getTable() { return tblTaiKhoan; }
    public JTextField getTxtMaNV() { return txtMaNV; }
    public JTextField getTxtMatKhau() { return txtMatKhau; }
}
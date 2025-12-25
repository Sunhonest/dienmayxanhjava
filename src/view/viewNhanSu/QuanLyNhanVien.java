package view.viewNhanSu;

import domain.ChucVu;
import domain.NhanVien; 
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class QuanLyNhanVien extends JPanel {

    // Components
    private JTable tblNhanVien;
    private DefaultTableModel model;
    
    private JTextField txtMaNV, txtHoTen, txtSDT, txtEmail;
    private JComboBox<Object> cboChucVu; 
    private JComboBox<String> cboGioiTinh;
    
    private JButton btnThem, btnSua, btnXoa, btnLamMoi;

    private final Color COLOR_PRIMARY = Color.decode("#2196F3");
    private final Font FONT_HEADER = new Font("Arial", Font.BOLD, 24);
    private final Font FONT_LABEL = new Font("Arial", Font.BOLD, 14);

    public QuanLyNhanVien() {
        initComponents();
        setTrangThaiNut(false); 
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // --- TITLE ---
        JLabel lblTitle = new JLabel("QUẢN LÝ NHÂN SỰ");
        lblTitle.setFont(FONT_HEADER);
        lblTitle.setForeground(COLOR_PRIMARY);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblTitle, BorderLayout.NORTH);

        // --- TABLE ---
        String[] columns = {"Mã NV", "Họ tên", "Giới tính", "Chức vụ", "SĐT", "Email"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        tblNhanVien = new JTable(model);
        tblNhanVien.setRowHeight(35);
        tblNhanVien.setFont(new Font("Arial", Font.PLAIN, 14));
        tblNhanVien.setSelectionBackground(Color.decode("#BBDEFB"));
        tblNhanVien.setSelectionForeground(Color.BLACK);
        tblNhanVien.setShowGrid(true);
        tblNhanVien.setGridColor(Color.LIGHT_GRAY);
        
        JTableHeader header = tblNhanVien.getTableHeader();
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

        add(new JScrollPane(tblNhanVien), BorderLayout.CENTER);

        // --- FORM INPUT (BÊN PHẢI) ---
        JPanel pnlInput = new JPanel();
        pnlInput.setLayout(new BoxLayout(pnlInput, BoxLayout.Y_AXIS)); // Xếp dọc
        pnlInput.setBackground(Color.WHITE);
        pnlInput.setPreferredSize(new Dimension(350, 0)); 
        pnlInput.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.LIGHT_GRAY, 1, true),
                new EmptyBorder(20, 20, 20, 20)
        ));

        // 1. TIÊU ĐỀ FORM (Dùng Panel riêng để căn giữa chuẩn)
        JPanel pnlTitleWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlTitleWrapper.setBackground(Color.WHITE);
        // QUAN TRỌNG: Wrapper phải căn trái để khớp trục với BoxLayout, nhưng nội dung bên trong nó căn giữa
        pnlTitleWrapper.setAlignmentX(Component.LEFT_ALIGNMENT); 
        pnlTitleWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JLabel lblForm = new JLabel("THÔNG TIN CHI TIẾT");
        lblForm.setFont(new Font("Arial", Font.BOLD, 18));
        lblForm.setForeground(COLOR_PRIMARY);
        pnlTitleWrapper.add(lblForm);
        
        pnlInput.add(pnlTitleWrapper);
        pnlInput.add(Box.createVerticalStrut(15));

        // 2. CÁC Ô NHẬP LIỆU (Thẳng tắp bên trái)
        txtMaNV = createField(pnlInput, "Mã nhân viên:");
        txtHoTen = createField(pnlInput, "Họ và tên:");
        
        pnlInput.add(createLabel("Giới tính:"));
        cboGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ"});
        styleComponent(cboGioiTinh);
        pnlInput.add(cboGioiTinh);
        pnlInput.add(Box.createVerticalStrut(15));

        txtSDT = createField(pnlInput, "Số điện thoại:");

        pnlInput.add(createLabel("Chức vụ:"));
        cboChucVu = new JComboBox<>(); 
        styleComponent(cboChucVu);
        pnlInput.add(cboChucVu);
        pnlInput.add(Box.createVerticalStrut(15));

        txtEmail = createField(pnlInput, "Email:");
        
        pnlInput.add(Box.createVerticalGlue()); 

        // 3. NÚT BẤM (Dùng Wrapper Panel để căn giữa cụm nút)
        JPanel pnlBtnWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlBtnWrapper.setBackground(Color.WHITE);
        pnlBtnWrapper.setAlignmentX(Component.LEFT_ALIGNMENT); // Khớp trục với form
        
        // Grid 2x2 cho các nút
        JPanel pnlGridButtons = new JPanel(new GridLayout(2, 2, 10, 10)); 
        pnlGridButtons.setBackground(Color.WHITE);
        pnlGridButtons.setPreferredSize(new Dimension(280, 80)); // Kích thước cố định cho cụm nút
        
        btnThem = createButton("Them", "Thêm", Color.decode("#4CAF50"));
        btnSua = createButton("Sua", "Sửa", Color.decode("#FFC107"));
        btnXoa = createButton("Xoa", "Xóa", Color.decode("#F44336"));
        btnLamMoi = createButton("Làm mới", "Làm mới", COLOR_PRIMARY);

        pnlGridButtons.add(btnThem);
        pnlGridButtons.add(btnSua);
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
        // QUAN TRỌNG: Căn trái tuyệt đối
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT); 
        return lbl;
    }

    private void styleComponent(JComponent comp) {
        comp.setFont(new Font("Arial", Font.PLAIN, 14));
        comp.setBackground(Color.WHITE);
        comp.setForeground(Color.BLACK);
        comp.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        // QUAN TRỌNG: Căn trái tuyệt đối
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

    public void setDuLieuChucVu(List<ChucVu> listCV) {
        cboChucVu.removeAllItems();
        for (ChucVu cv : listCV) {
            cboChucVu.addItem(cv); 
        }
    }

    public void resetForm() {
        txtMaNV.setText("");
        txtHoTen.setText("");
        txtSDT.setText("");
        txtEmail.setText("");
        cboGioiTinh.setSelectedIndex(0);
        if(cboChucVu.getItemCount() > 0) cboChucVu.setSelectedIndex(0);
        
        txtMaNV.setEnabled(true);
        txtMaNV.requestFocus();
        
        setTrangThaiNut(false); 
        tblNhanVien.clearSelection();
    }

    public NhanVien getNhanVienFromInput() {
        NhanVien nv = new NhanVien();
        nv.setMaNV(txtMaNV.getText().trim());
        nv.setHoTen(txtHoTen.getText().trim());
        nv.setGioiTinh(cboGioiTinh.getSelectedItem().toString());
        nv.setSdt(txtSDT.getText().trim());
        nv.setEmail(txtEmail.getText().trim());
        
        Object selected = cboChucVu.getSelectedItem();
        if (selected != null && selected instanceof ChucVu) {
            ChucVu cv = (ChucVu) selected;
            nv.setMaCV(cv.getMaCV());
        } else {
            nv.setMaCV("NV");
        }
        return nv;
    }

    public void fillFormTuBang() {
        int row = tblNhanVien.getSelectedRow();
        if (row >= 0) {
            txtMaNV.setText(tblNhanVien.getValueAt(row, 0).toString());
            txtMaNV.setEnabled(false);
            txtHoTen.setText(tblNhanVien.getValueAt(row, 1).toString());
            cboGioiTinh.setSelectedItem(tblNhanVien.getValueAt(row, 2).toString());
            
            String maCV = tblNhanVien.getValueAt(row, 3).toString();
            setSelectedChucVu(maCV);
            
            txtSDT.setText(tblNhanVien.getValueAt(row, 4).toString());
            txtEmail.setText(tblNhanVien.getValueAt(row, 5).toString());
            
            setTrangThaiNut(true);
        }
    }

    private void setSelectedChucVu(String maCV) {
        for (int i = 0; i < cboChucVu.getItemCount(); i++) {
            Object item = cboChucVu.getItemAt(i);
            if (item instanceof ChucVu) {
                ChucVu cv = (ChucVu) item;
                if (cv.getMaCV().equals(maCV)) {
                    cboChucVu.setSelectedIndex(i);
                    return;
                }
            }
        }
    }

    public String getMaNVDangChon() {
        int row = tblNhanVien.getSelectedRow();
        if (row >= 0) return tblNhanVien.getValueAt(row, 0).toString();
        return null;
    }

    public DefaultTableModel getModel() { return model; }
    public JTable getTable() { return tblNhanVien; }
}
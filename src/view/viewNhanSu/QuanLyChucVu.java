package view.viewNhanSu;

import domain.ChucVu;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class QuanLyChucVu extends JPanel {

    // Components
    private JTable tblChucVu;
    private DefaultTableModel model;
    
    private JTextField txtMaCV, txtTenCV, txtLuongCoBan, txtMoTa;
    private JButton btnThem, btnSua, btnXoa, btnLamMoi;

    private final Color COLOR_PRIMARY = Color.decode("#2196F3");
    private final Font FONT_HEADER = new Font("Arial", Font.BOLD, 24);
    private final Font FONT_LABEL = new Font("Arial", Font.BOLD, 14);

    public QuanLyChucVu() {
        initComponents();
        setTrangThaiNut(false); 
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // --- TITLE ---
        JLabel lblTitle = new JLabel("QUẢN LÝ CHỨC VỤ & LƯƠNG");
        lblTitle.setFont(FONT_HEADER);
        lblTitle.setForeground(COLOR_PRIMARY);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblTitle, BorderLayout.NORTH);

        // --- TABLE ---
        String[] columns = {"Mã CV", "Tên chức vụ", "Lương cơ bản (VND)", "Mô tả"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        tblChucVu = new JTable(model);
        tblChucVu.setRowHeight(35);
        tblChucVu.setFont(new Font("Arial", Font.PLAIN, 14));
        tblChucVu.setSelectionBackground(Color.decode("#BBDEFB"));
        tblChucVu.setSelectionForeground(Color.BLACK);
        tblChucVu.setShowGrid(true);
        tblChucVu.setGridColor(Color.LIGHT_GRAY);
        
        JTableHeader header = tblChucVu.getTableHeader();
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
        
        // Căn phải cho cột Lương
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        tblChucVu.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);

        add(new JScrollPane(tblChucVu), BorderLayout.CENTER);

        // --- FORM INPUT (STYLE GIỐNG NHÂN VIÊN) ---
        JPanel pnlInput = new JPanel();
        pnlInput.setLayout(new BoxLayout(pnlInput, BoxLayout.Y_AXIS));
        pnlInput.setBackground(Color.WHITE);
        pnlInput.setPreferredSize(new Dimension(350, 0)); // Kích thước chuẩn 350
        pnlInput.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.LIGHT_GRAY, 1, true),
                new EmptyBorder(20, 20, 20, 20)
        ));

        // 1. TIÊU ĐỀ FORM (Wrapper Center)
        JPanel pnlTitleWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlTitleWrapper.setBackground(Color.WHITE);
        pnlTitleWrapper.setAlignmentX(Component.LEFT_ALIGNMENT); 
        pnlTitleWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JLabel lblForm = new JLabel("THÔNG TIN CHI TIẾT");
        lblForm.setFont(new Font("Arial", Font.BOLD, 18));
        lblForm.setForeground(COLOR_PRIMARY);
        pnlTitleWrapper.add(lblForm);
        
        pnlInput.add(pnlTitleWrapper);
        pnlInput.add(Box.createVerticalStrut(15)); // Khoảng cách chuẩn

        // 2. CÁC Ô NHẬP LIỆU (Left)
        txtMaCV = createField(pnlInput, "Mã chức vụ:");
        txtTenCV = createField(pnlInput, "Tên chức vụ:");
        txtLuongCoBan = createField(pnlInput, "Lương cơ bản (VND):");
        txtMoTa = createField(pnlInput, "Mô tả công việc:");
        
        pnlInput.add(Box.createVerticalGlue()); 

        // 3. NÚT BẤM (Wrapper Center + Grid 2x2)
        JPanel pnlBtnWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlBtnWrapper.setBackground(Color.WHITE);
        pnlBtnWrapper.setAlignmentX(Component.LEFT_ALIGNMENT); 
        
        JPanel pnlGridButtons = new JPanel(new GridLayout(2, 2, 10, 10)); // Grid 2x2
        pnlGridButtons.setBackground(Color.WHITE);
        pnlGridButtons.setPreferredSize(new Dimension(280, 80)); // Kích thước chuẩn
        
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
        panel.add(Box.createVerticalStrut(15)); // Khoảng cách giữa các ô là 15px
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

    // === LOGIC MÀU SẮC NÚT ===
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
        txtMaCV.setText("");
        txtTenCV.setText("");
        txtLuongCoBan.setText("");
        txtMoTa.setText("");
        
        txtMaCV.setEnabled(true);
        txtMaCV.requestFocus();
        
        setTrangThaiNut(false);
        tblChucVu.clearSelection();
    }
    
    // Lấy dữ liệu từ form (Tự động lọc bỏ dấu chấm lương)
    public ChucVu getChucVuFromInput() {
        ChucVu cv = new ChucVu();
        cv.setMaCV(txtMaCV.getText().trim());
        cv.setTenCV(txtTenCV.getText().trim());
        
        try {
            // Lọc bỏ dấu chấm/phẩy: "10.000.000" -> "10000000"
            String luongRaw = txtLuongCoBan.getText().trim()
                    .replace(".", "")
                    .replace(",", "");
            
            if(luongRaw.isEmpty()) cv.setLuongCoBan(0);
            else cv.setLuongCoBan(Double.parseDouble(luongRaw));
        } catch (NumberFormatException e) {
            cv.setLuongCoBan(0);
        }
        
        cv.setMoTa(txtMoTa.getText().trim());
        return cv;
    }
    
    public String getMaCVDangChon() {
        int row = tblChucVu.getSelectedRow();
        if(row >= 0) return tblChucVu.getValueAt(row, 0).toString();
        return null;
    }
    
    // Đổ dữ liệu lên form (GIỮ NGUYÊN dấu chấm để dễ nhìn)
    public void fillFormTuBang() {
        int row = tblChucVu.getSelectedRow();
        if(row >= 0) {
            txtMaCV.setText(tblChucVu.getValueAt(row, 0).toString());
            txtMaCV.setEnabled(false);
            
            txtTenCV.setText(tblChucVu.getValueAt(row, 1).toString());
            
            // Lấy lương đã format từ bảng -> Đưa thẳng vào ô nhập liệu
            String luongFormatted = tblChucVu.getValueAt(row, 2).toString();
            txtLuongCoBan.setText(luongFormatted);
            
            txtMoTa.setText(tblChucVu.getValueAt(row, 3).toString());
            
            setTrangThaiNut(true);
        }
    }
    
    public DefaultTableModel getModel() { return model; }
    public JTable getTable() { return tblChucVu; }
}
package view.viewKho;

import domain.Kho.NhaCungCap;
import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class QuanLyNhaCungCap extends JPanel {

    // --- COMPONENTS ---
    private JTable tblNhaCungCap;
    private DefaultTableModel model;

    // Input Fields (Thêm Địa chỉ và SĐT so với Danh mục)
    private JTextField txtMaNCC, txtTenNCC, txtDiaChi, txtSDT;
    
    // Buttons
    private JButton btnThem, btnSua, btnXoa, btnLamMoi;

    // --- COLORS & FONTS (Đồng bộ với QuanLyDanhMuc) ---
    private final Color COLOR_PRIMARY = Color.decode("#2196F3");
    private final Font FONT_HEADER = new Font("Arial", Font.BOLD, 24);
    private final Font FONT_LABEL = new Font("Arial", Font.BOLD, 14);

    public QuanLyNhaCungCap() {
        initComponents();
        setTrangThaiNut(false);
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // --- HEADER ---
        JLabel lblTitle = new JLabel("QUẢN LÝ NHÀ CUNG CẤP");
        lblTitle.setFont(FONT_HEADER);
        lblTitle.setForeground(COLOR_PRIMARY);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblTitle, BorderLayout.NORTH);

        // --- CENTER: TABLE ---
        // Bảng thêm cột Địa chỉ và SĐT
        String[] columns = {"Mã NCC", "Tên Nhà Cung Cấp", "Địa Chỉ", "Số Điện Thoại"};
        model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tblNhaCungCap = new JTable(model);
        styleTable(tblNhaCungCap);

        JScrollPane scrTable = new JScrollPane(tblNhaCungCap);
        add(scrTable, BorderLayout.CENTER);

        // --- EAST: FORM INPUT ---
        JPanel pnlRight = new JPanel();
        pnlRight.setLayout(new BorderLayout());
        pnlRight.setBackground(Color.WHITE);
        pnlRight.setPreferredSize(new Dimension(350, 0));
        pnlRight.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.LIGHT_GRAY, 1, true),
                new EmptyBorder(10, 10, 10, 10)
        ));

        // Tiêu đề Form
        JLabel lblFormTitle = new JLabel("THÔNG TIN NCC", SwingConstants.CENTER);
        lblFormTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblFormTitle.setForeground(COLOR_PRIMARY);
        lblFormTitle.setPreferredSize(new Dimension(0, 40));
        pnlRight.add(lblFormTitle, BorderLayout.NORTH);

        // Nội dung Form
        JPanel pnlContent = new JPanel();
        pnlContent.setLayout(new BoxLayout(pnlContent, BoxLayout.Y_AXIS));
        pnlContent.setBackground(Color.WHITE);

        txtMaNCC = createField(pnlContent, "Mã NCC:");
        txtTenNCC = createField(pnlContent, "Tên NCC:");
        txtDiaChi = createField(pnlContent, "Địa chỉ:");
        txtSDT = createField(pnlContent, "Số điện thoại:");

        // Đẩy content lên trên cùng
        pnlContent.add(Box.createVerticalGlue());
        
        pnlRight.add(pnlContent, BorderLayout.CENTER);

        // Khu vực Nút bấm
        JPanel pnlButtons = new JPanel(new GridLayout(2, 2, 10, 10));
        pnlButtons.setBackground(Color.WHITE);
        pnlButtons.setBorder(new EmptyBorder(10, 0, 0, 0));
        pnlButtons.setPreferredSize(new Dimension(0, 80));

        btnThem = createButton("Them", "Thêm", Color.decode("#4CAF50"));
        btnSua = createButton("Sua", "Sửa", Color.decode("#FFC107"));
        btnXoa = createButton("Xoa", "Xóa", Color.decode("#F44336"));
        btnLamMoi = createButton("LamMoi", "Làm mới", COLOR_PRIMARY);

        pnlButtons.add(btnThem);
        pnlButtons.add(btnSua);
        pnlButtons.add(btnXoa);
        pnlButtons.add(btnLamMoi);

        pnlRight.add(pnlButtons, BorderLayout.SOUTH);

        add(pnlRight, BorderLayout.EAST);
    }

    // --- HELPER METHODS (Giữ nguyên style) ---

    private JTextField createField(JPanel panel, String labelText) {
        panel.add(createLabel(labelText));
        JTextField txt = new JTextField();
        styleComponent(txt);
        panel.add(txt);
        panel.add(Box.createVerticalStrut(15)); // Giảm khoảng cách chút vì nhiều trường hơn
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
        comp.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        comp.setPreferredSize(new Dimension(100, 35));
        comp.setAlignmentX(Component.LEFT_ALIGNMENT);
        if (comp instanceof JTextField) {
            ((JTextField) comp).setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        }
    }

    private JButton createButton(String cmd, String text, Color bg) {
        JButton b = new JButton(text);
        b.setActionCommand(cmd);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Arial", Font.BOLD, 13));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    private void styleTable(JTable table) {
        table.setRowHeight(35);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setShowGrid(true);
        table.setGridColor(Color.LIGHT_GRAY);
        table.setSelectionBackground(Color.decode("#BBDEFB"));
        table.setSelectionForeground(Color.BLACK);
        
        JTableHeader header = table.getTableHeader();
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
        
        table.getColumnModel().getColumn(0).setPreferredWidth(80); // Mã NCC nhỏ hơn chút
        table.getColumnModel().getColumn(1).setPreferredWidth(200); // Tên to hơn
    }

    // --- LOGIC METHODS ---
    public void setTrangThaiNut(boolean isSelect) {
        btnThem.setEnabled(!isSelect);
        btnThem.setBackground(!isSelect ? Color.decode("#4CAF50") : Color.LIGHT_GRAY);
        
        btnSua.setEnabled(isSelect);
        btnSua.setBackground(isSelect ? Color.decode("#FFC107") : Color.LIGHT_GRAY);
        
        btnXoa.setEnabled(isSelect);
        btnXoa.setBackground(isSelect ? Color.decode("#F44336") : Color.LIGHT_GRAY);
        
        txtMaNCC.setEnabled(!isSelect); 
    }

    public void addActionListener(ActionListener ac) {
        btnThem.addActionListener(ac);
        btnSua.addActionListener(ac);
        btnXoa.addActionListener(ac);
        btnLamMoi.addActionListener(ac);
    }

    public void resetForm() {
        txtMaNCC.setText(""); 
        txtTenNCC.setText("");
        txtDiaChi.setText("");
        txtSDT.setText("");
        setTrangThaiNut(false); 
        tblNhaCungCap.clearSelection();
    }
    
    public NhaCungCap getNhaCungCapInput() {
        String ma = txtMaNCC.getText().trim();
        String ten = txtTenNCC.getText().trim();
        String diachi = txtDiaChi.getText().trim();
        String sdt = txtSDT.getText().trim();
        
        if(ma.isEmpty() || ten.isEmpty()) return null;
        
        NhaCungCap ncc = new NhaCungCap();
        ncc.setMaNCC(ma);
        ncc.setTenNCC(ten);
        ncc.setDiaChi(diachi);
        ncc.setSdt(sdt);
        return ncc;
    }
    
    public void fillForm(NhaCungCap ncc) {
        txtMaNCC.setText(ncc.getMaNCC());
        txtTenNCC.setText(ncc.getTenNCC());
        txtDiaChi.setText(ncc.getDiaChi());
        txtSDT.setText(ncc.getSdt());
        setTrangThaiNut(true);
    }

    public String getMaNCCChon() {
        int r = tblNhaCungCap.getSelectedRow();
        if(r >= 0) return tblNhaCungCap.getValueAt(r, 0).toString();
        return null;
    }
    
    public JTable getTable() { return tblNhaCungCap; }
    public DefaultTableModel getModel() { return model; }
}
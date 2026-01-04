package view.viewKho;

import domain.Kho.NhaCungCap;
import domain.Kho.SanPham;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.List;

public class QuanLyNhapKho extends JPanel {
    // --- KHAI BÁO BIẾN ---
    private JTextField txtMaPhieu, txtNhanVien, txtTonKho, txtSoLuong, txtDonGia, txtGhiChu;
    private JComboBox<NhaCungCap> cboNhaCungCap;
    private JComboBox<String> cboSanPham;
    
    private JButton btnThem, btnSua, btnXoa, btnLamMoi;
    
    private JTable tblLichSuNhap;
    private DefaultTableModel modelLichSu;
    private JLabel lblTongTien;

    // Cache dữ liệu
    private List<SanPham> listSPCache;
    
    // COLOR
    private final Color COLOR_PRIMARY = Color.decode("#2196F3");

    public QuanLyNhapKho() {
        initComponents();
        setTrangThaiNut(false);
    }
    
    private void initComponents() {
        // 1. SETUP LAYOUT CHÍNH (BorderLayout chuẩn)
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // --- HEADER ---
        JLabel lbTitle = new JLabel("QUẢN LÝ NHẬP KHO");
        lbTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lbTitle.setForeground(COLOR_PRIMARY);
        lbTitle.setHorizontalAlignment(SwingConstants.CENTER);
        add(lbTitle, BorderLayout.NORTH);

        // ========================================================
        // CENTER: BẢNG DỮ LIỆU
        // ========================================================
        JPanel pnlCenter = new JPanel(new BorderLayout(0, 10));
        pnlCenter.setBackground(Color.WHITE);
        
        // Bảng
        String[] columnNames = {"ID", "Mã SP", "Tên SP", "Số lượng", "Đơn giá", "Thành tiền","Ghi chú"};
        modelLichSu = new DefaultTableModel(columnNames, 0) {
             @Override
             public boolean isCellEditable(int row, int column) { return false; }
        };
        tblLichSuNhap = new JTable(modelLichSu);
        styleTable(tblLichSuNhap);
        
        tblLichSuNhap.getColumnModel().getColumn(0).setPreferredWidth(40);
        tblLichSuNhap.getColumnModel().getColumn(1).setPreferredWidth(60);
        tblLichSuNhap.getColumnModel().getColumn(2).setPreferredWidth(150);
        
        pnlCenter.add(new JScrollPane(tblLichSuNhap), BorderLayout.CENTER);
        
        // Label Tổng tiền (Đặt ở dưới bảng)
        lblTongTien = new JLabel("Tổng tiền: 0 VNĐ", SwingConstants.RIGHT);
        lblTongTien.setFont(new Font("Arial", Font.BOLD, 16));
        lblTongTien.setForeground(Color.RED);
        pnlCenter.add(lblTongTien, BorderLayout.SOUTH);
        
        add(pnlCenter, BorderLayout.CENTER);

        // ========================================================
        // EAST: FORM NHẬP (Giống QuanLyNhanVien)
        // ========================================================
        JPanel pnlRight = new JPanel();
        pnlRight.setLayout(new BorderLayout());
        pnlRight.setBackground(Color.WHITE);
        pnlRight.setPreferredSize(new Dimension(350, 0)); // Cố định chiều rộng 350
        pnlRight.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.LIGHT_GRAY, 1, true),
                new EmptyBorder(10, 10, 10, 10)
        ));

        // Tiêu đề Form
        JLabel lblFormTitle = new JLabel("THÔNG TIN NHẬP HÀNG", SwingConstants.CENTER);
        lblFormTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblFormTitle.setForeground(COLOR_PRIMARY);
        lblFormTitle.setPreferredSize(new Dimension(0, 40));
        pnlRight.add(lblFormTitle, BorderLayout.NORTH);
        
        // Nội dung Form
        JPanel pnlContent = new JPanel();
        pnlContent.setLayout(new BoxLayout(pnlContent, BoxLayout.Y_AXIS));
        pnlContent.setBackground(Color.WHITE);

        txtMaPhieu = createField(pnlContent, "Mã phiếu:");
        
        pnlContent.add(createLabel("Nhân viên:"));
        txtNhanVien = new JTextField();
        styleComponent(txtNhanVien);
        txtNhanVien.setEditable(false);
        pnlContent.add(txtNhanVien);
        pnlContent.add(Box.createVerticalStrut(10));

        pnlContent.add(createLabel("Nhà cung cấp:"));
        cboNhaCungCap = new JComboBox<>();
        styleComponent(cboNhaCungCap);
        pnlContent.add(cboNhaCungCap);
        pnlContent.add(Box.createVerticalStrut(10));
        
        pnlContent.add(createLabel("Sản phẩm:"));
        cboSanPham = new JComboBox<>();
        styleComponent(cboSanPham);
        pnlContent.add(cboSanPham);
        pnlContent.add(Box.createVerticalStrut(10));
        
        pnlContent.add(createLabel("Tồn hiện tại:"));
        txtTonKho = new JTextField();
        styleComponent(txtTonKho);
        txtTonKho.setEditable(false);
        txtTonKho.setForeground(Color.RED);
        txtTonKho.setFont(new Font("Arial", Font.BOLD, 14));
        pnlContent.add(txtTonKho);
        pnlContent.add(Box.createVerticalStrut(10));
        
        txtSoLuong = createField(pnlContent, "Số lượng nhập:");
        txtDonGia = createField(pnlContent, "Đơn giá:");
        txtGhiChu = createField(pnlContent, "Ghi chú:");
        
        // Thêm khoảng trống để đẩy nút xuống nếu cần
        pnlContent.add(Box.createVerticalGlue()); 
        
        pnlRight.add(pnlContent, BorderLayout.CENTER);

        // Khu vực Nút bấm
        // ... (Phần form nhập ở trên giữ nguyên) ...

        // Khu vực Nút bấm
        JPanel pnlButtons = new JPanel(new GridLayout(1, 4, 10, 10)); // Sửa lại thành 1 hàng 4 nút cho gọn
        pnlButtons.setBackground(Color.WHITE);
        pnlButtons.setBorder(new EmptyBorder(10, 0, 0, 0));
        pnlButtons.setPreferredSize(new Dimension(0, 50)); // Giảm chiều cao xuống vì chỉ còn 1 hàng

        btnThem = createBtn("Thêm", Color.decode("#4CAF50"));
        btnSua = createBtn("Sửa", Color.decode("#FF9800"));
        btnXoa = createBtn("Xóa", Color.decode("#F44336"));
        btnLamMoi = createBtn("Mới", COLOR_PRIMARY);

        pnlButtons.add(btnThem);
        pnlButtons.add(btnSua);
        pnlButtons.add(btnXoa);
        pnlButtons.add(btnLamMoi);

        // XÓA BỎ đoạn pnlExcel và btnXuatExcel cũ ở đây

        // Thêm trực tiếp panel nút vào vùng SOUTH của pnlRight
        pnlRight.add(pnlButtons, BorderLayout.SOUTH);
        add(pnlRight, BorderLayout.EAST);
    }

    // ========================================================================
    // HELPER METHODS
    // ========================================================================
    private JTextField createField(JPanel panel, String labelText) {
        panel.add(createLabel(labelText));
        JTextField txt = new JTextField();
        styleComponent(txt);
        panel.add(txt);
        panel.add(Box.createVerticalStrut(10));
        return txt;
    }

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Arial", Font.BOLD, 14));
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

    private void styleTable(JTable table) {
        table.setRowHeight(35);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setGridColor(Color.LIGHT_GRAY);
        table.setShowGrid(true);
        table.setSelectionBackground(Color.decode("#BBDEFB"));
        table.setSelectionForeground(Color.BLACK);

        JTableHeader header = table.getTableHeader();
        header.setPreferredSize(new Dimension(0, 40)); 
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
    }

    private JButton createBtn(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);              
        btn.setForeground(Color.WHITE);     
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setFocusPainted(false);         
        btn.setBorderPainted(false);        
        btn.setOpaque(true);                
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // ==========================================================
    // LOGIC METHODS (GIỮ NGUYÊN)
    // ==========================================================
    
    public void addBtnListener(ActionListener ac) {
        btnThem.addActionListener(ac);
        btnXoa.addActionListener(ac);
        btnSua.addActionListener(ac); 
        btnLamMoi.addActionListener(ac);
        
        cboSanPham.addActionListener(ac);
    }
    
    public void addTableMouseListener(MouseListener ml) {
        tblLichSuNhap.addMouseListener(ml);
    }

    public void setNhaCungCap(List<NhaCungCap> list) {
        cboNhaCungCap.removeAllItems();
        for(NhaCungCap n : list) cboNhaCungCap.addItem(n);
    }

    public void setListSanPham(List<SanPham> list) {
        this.listSPCache = list;
        cboSanPham.removeAllItems();
        for(SanPham s : list) cboSanPham.addItem(s.getMaSP() + " - " + s.getTenSP());
    }

    public SanPham getSelectedSanPham() {
        if(cboSanPham.getSelectedIndex() < 0) return null;
        return listSPCache.get(cboSanPham.getSelectedIndex());
    }
    
    public NhaCungCap getSelectedNCC() {
        return (NhaCungCap) cboNhaCungCap.getSelectedItem();
    }
    
    public JComboBox<String> getCboSanPham() { return cboSanPham; }

    public void setSelectedSanPhamByMa(String maSP) {
        if (listSPCache == null) return;
        for(int i=0; i<listSPCache.size(); i++) {
            if(listSPCache.get(i).getMaSP().equals(maSP)) {
                cboSanPham.setSelectedIndex(i);
                break;
            }
        }
    }

    // --- THÊM MỚI HÀM NÀY ---
    public void setTrangThaiNut(boolean isSelect) {
        // Nút Thêm: Sáng khi KHÔNG chọn dòng (mode thêm mới), Xám khi chọn dòng
        btnThem.setEnabled(!isSelect);
        btnThem.setBackground(!isSelect ? Color.decode("#4CAF50") : Color.LIGHT_GRAY);

        // Nút Sửa: Xám khi KHÔNG chọn dòng, Sáng khi chọn dòng
        btnSua.setEnabled(isSelect);
        btnSua.setBackground(isSelect ? Color.decode("#FF9800") : Color.LIGHT_GRAY);

        // Nút Xóa: Xám khi KHÔNG chọn dòng, Sáng khi chọn dòng
        btnXoa.setEnabled(isSelect);
        btnXoa.setBackground(isSelect ? Color.decode("#F44336") : Color.LIGHT_GRAY);

        // Mã phiếu: Khóa không cho sửa khi đang chọn dòng lịch sử
        txtMaPhieu.setEnabled(!isSelect);
    }
    
    public String getMaPhieu() { return txtMaPhieu.getText().trim(); }
    public void setMaPhieu(String s) { txtMaPhieu.setText(s); }
    public String getNhanVien() { return txtNhanVien.getText(); }
    public void setNhanVien(String s) { txtNhanVien.setText(s); }
    public void setTonKho(String s) { txtTonKho.setText(s); }
    public String getSoLuong() { return txtSoLuong.getText().trim(); }
    public void setSoLuong(String s) { txtSoLuong.setText(s); }
    public String getDonGia() { return txtDonGia.getText().trim(); }
    public void setDonGia(String s) { txtDonGia.setText(s); }
    public String getGhiChu() { return txtGhiChu.getText().trim(); }
    public void setGhiChu(String s) { txtGhiChu.setText(s); }
    public void setTongTien(String s) { lblTongTien.setText(s); }

    public DefaultTableModel getModel() { return modelLichSu; }
    public JTable getTable() { return tblLichSuNhap; }

    public void clearForm() {
        txtMaPhieu.setText("");
        txtSoLuong.setText("");
        txtDonGia.setText("");
        txtGhiChu.setText("");
        if(cboSanPham.getItemCount() > 0) cboSanPham.setSelectedIndex(0);
        tblLichSuNhap.clearSelection();
        setTrangThaiNut(false);
    }
}
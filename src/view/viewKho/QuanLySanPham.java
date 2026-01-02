package view.viewKho;

import domain.Kho.DanhMuc;
import domain.Kho.SanPham;
import java.awt.*;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class QuanLySanPham extends JPanel {

    // --- COMPONENTS ---
    private JTable tblSanPham;
    private DefaultTableModel model;

    // Input Fields
    private JTextField txtMaSP, txtTenSP, txtThuongHieu, txtGiaNhap, txtGiaBan, txtBaoHanh, txtHinhAnh;
    private JComboBox<Object> cboDanhMuc;
    private JComboBox<String> cboDonViTinh, cboTrangThaiHang;
    private JTextArea txtMoTa;
    private JLabel lblAnhPreview; 

    // Buttons
    private JButton btnThem, btnSua, btnXoa, btnLamMoi;

    // --- COLORS & FONTS ---
    private final Color COLOR_PRIMARY = Color.decode("#2196F3"); // Màu chủ đạo giống Nhân sự
    private final Font FONT_HEADER = new Font("Arial", Font.BOLD, 24);
    private final Font FONT_LABEL = new Font("Arial", Font.BOLD, 14);

    public QuanLySanPham() {
        initComponents();
        setTrangThaiNut(false);
    }

    private void initComponents() {
        // 1. SETUP LAYOUT CHÍNH (Giống QuanLyNhanVien)
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // --- HEADER ---
        JLabel lblTitle = new JLabel("QUẢN LÝ SẢN PHẨM KHO");
        lblTitle.setFont(FONT_HEADER);
        lblTitle.setForeground(COLOR_PRIMARY);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblTitle, BorderLayout.NORTH);

        // --- CENTER: TABLE ---
        String[] columns = {"Mã SP", "Tên SP", "Danh mục", "Thương hiệu", "Tồn", "ĐVT", "Giá bán"};
        model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tblSanPham = new JTable(model);
        styleTable(tblSanPham); // Áp dụng style chuẩn

        JScrollPane scrTable = new JScrollPane(tblSanPham);
        add(scrTable, BorderLayout.CENTER);

        // --- EAST: FORM INPUT ---
        // Sử dụng Panel chứa form bên phải, rộng 350px
        JPanel pnlRight = new JPanel();
        pnlRight.setLayout(new BorderLayout());
        pnlRight.setBackground(Color.WHITE);
        pnlRight.setPreferredSize(new Dimension(350, 0));
        pnlRight.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.LIGHT_GRAY, 1, true),
                new EmptyBorder(10, 10, 10, 10) // Padding trong
        ));

        // Tiêu đề Form
        JLabel lblFormTitle = new JLabel("THÔNG TIN CHI TIẾT", SwingConstants.CENTER);
        lblFormTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblFormTitle.setForeground(COLOR_PRIMARY);
        lblFormTitle.setPreferredSize(new Dimension(0, 40));
        pnlRight.add(lblFormTitle, BorderLayout.NORTH);

        // Nội dung Form (Dùng BoxLayout trục Y)
        JPanel pnlContent = new JPanel();
        pnlContent.setLayout(new BoxLayout(pnlContent, BoxLayout.Y_AXIS));
        pnlContent.setBackground(Color.WHITE);

        // Thêm các trường vào pnlContent
        txtMaSP = createField(pnlContent, "Mã sản phẩm:");
        txtTenSP = createField(pnlContent, "Tên sản phẩm:");
        
        pnlContent.add(createLabel("Danh mục:"));
        cboDanhMuc = new JComboBox<>();
        styleComponent(cboDanhMuc);
        pnlContent.add(cboDanhMuc);
        pnlContent.add(Box.createVerticalStrut(10));

        txtThuongHieu = createField(pnlContent, "Thương hiệu:");

        // Hàng ĐVT và Trạng thái (chia đôi)
        JPanel pnlRow = new JPanel(new GridLayout(1, 2, 5, 0));
        pnlRow.setBackground(Color.WHITE);
        pnlRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));
        pnlRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel p1 = new JPanel(new BorderLayout()); p1.setBackground(Color.WHITE);
        p1.add(createLabel("Đơn vị tính:"), BorderLayout.NORTH);
        cboDonViTinh = new JComboBox<>(new String[]{"Cái", "Chiếc", "Bộ", "Hộp", "Kg"});
        styleComponent(cboDonViTinh);
        p1.add(cboDonViTinh, BorderLayout.CENTER);
        
        JPanel p2 = new JPanel(new BorderLayout()); p2.setBackground(Color.WHITE);
        p2.add(createLabel("Trạng thái:"), BorderLayout.NORTH);
        cboTrangThaiHang = new JComboBox<>(new String[]{"MOI", "CU", "TRUNG_BAY"});
        styleComponent(cboTrangThaiHang);
        p2.add(cboTrangThaiHang, BorderLayout.CENTER);

        pnlRow.add(p1);
        pnlRow.add(p2);
        pnlContent.add(pnlRow);
        pnlContent.add(Box.createVerticalStrut(10));

        txtGiaNhap = createField(pnlContent, "Giá nhập:");
        txtGiaBan = createField(pnlContent, "Giá bán:");
        txtBaoHanh = createField(pnlContent, "Bảo hành (tháng):");
        txtHinhAnh = createField(pnlContent, "Link Ảnh:");

        // Preview Ảnh
        lblAnhPreview = new JLabel("Chưa có ảnh", SwingConstants.CENTER);
        lblAnhPreview.setPreferredSize(new Dimension(90, 90));
        lblAnhPreview.setMaximumSize(new Dimension(90, 90));
        lblAnhPreview.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        lblAnhPreview.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JPanel pnlImg = new JPanel(); 
        pnlImg.setBackground(Color.WHITE);
        pnlImg.add(lblAnhPreview);
        pnlImg.setAlignmentX(Component.LEFT_ALIGNMENT);
        pnlContent.add(pnlImg);

        // Mô tả
        pnlContent.add(createLabel("Mô tả:"));
        txtMoTa = new JTextArea(3, 20);
        txtMoTa.setFont(new Font("Arial", Font.PLAIN, 14));
        txtMoTa.setLineWrap(true);
        txtMoTa.setWrapStyleWord(true);
        JScrollPane scrMoTa = new JScrollPane(txtMoTa);
        scrMoTa.setAlignmentX(Component.LEFT_ALIGNMENT);
        pnlContent.add(scrMoTa);

        // Do form dài, ta bỏ pnlContent vào ScrollPane của pnlRight
        JScrollPane scrInput = new JScrollPane(pnlContent);
        scrInput.setBorder(null);
        scrInput.getVerticalScrollBar().setUnitIncrement(16);
        pnlRight.add(scrInput, BorderLayout.CENTER);

        // Khu vực Nút bấm (Phía dưới Form)
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

    // ========================================================================
    // HELPER METHODS (STYLE GIỐNG NHÂN SỰ)
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
                label.setBackground(COLOR_PRIMARY); // Màu xanh dương đồng bộ
                label.setForeground(Color.WHITE);
                label.setHorizontalAlignment(JLabel.CENTER);
                label.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.WHITE));
                return label;
            }
        });
        
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        table.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
        table.getColumnModel().getColumn(6).setCellRenderer(rightRenderer);
        
        table.getColumnModel().getColumn(0).setPreferredWidth(60);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
    }

    // ========================================================================
    // LOGIC METHODS (GIỮ NGUYÊN NHƯ CŨ)
    // ========================================================================
    public void setTrangThaiNut(boolean isSelect) {
        btnThem.setEnabled(!isSelect);
        btnThem.setBackground(!isSelect ? Color.decode("#4CAF50") : Color.LIGHT_GRAY);
        
        btnSua.setEnabled(isSelect);
        btnSua.setBackground(isSelect ? Color.decode("#FFC107") : Color.LIGHT_GRAY);
        
        btnXoa.setEnabled(isSelect);
        btnXoa.setBackground(isSelect ? Color.decode("#F44336") : Color.LIGHT_GRAY);
        
        txtMaSP.setEnabled(!isSelect); 
    }

    public void addActionListener(ActionListener ac) {
        btnThem.addActionListener(ac);
        btnSua.addActionListener(ac);
        btnXoa.addActionListener(ac);
        btnLamMoi.addActionListener(ac);
    }
    
    public void setDuLieuDanhMuc(List<DanhMuc> listDM) {
        cboDanhMuc.removeAllItems();
        for(DanhMuc dm : listDM) cboDanhMuc.addItem(dm);
    }
    
    public void resetForm() {
        txtMaSP.setText(""); txtTenSP.setText(""); txtThuongHieu.setText("");
        txtGiaNhap.setText(""); txtGiaBan.setText(""); txtBaoHanh.setText(""); 
        txtMoTa.setText(""); txtHinhAnh.setText("");
        lblAnhPreview.setIcon(null); lblAnhPreview.setText("Chưa có ảnh");
        if(cboDanhMuc.getItemCount() > 0) cboDanhMuc.setSelectedIndex(0);
        cboDonViTinh.setSelectedIndex(0);
        setTrangThaiNut(false); 
        tblSanPham.clearSelection();
    }
    
    public SanPham getSanPhamInput() {
        SanPham sp = new SanPham();
        sp.setMaSP(txtMaSP.getText().trim());
        sp.setTenSP(txtTenSP.getText().trim());
        if(cboDanhMuc.getSelectedItem() instanceof DanhMuc)
            sp.setMaDanhMuc(((DanhMuc)cboDanhMuc.getSelectedItem()).getMaDM());
        sp.setThuongHieu(txtThuongHieu.getText().trim());
        sp.setDonViTinh(cboDonViTinh.getSelectedItem().toString());
        sp.setTrangThaiHang(cboTrangThaiHang.getSelectedItem().toString());
        try { sp.setGiaNhap(Double.parseDouble(txtGiaNhap.getText().replace(",", ""))); } catch(Exception e) { sp.setGiaNhap(0); }
        try { sp.setGiaBan(Double.parseDouble(txtGiaBan.getText().replace(",", ""))); } catch(Exception e) { sp.setGiaBan(0); }
        try { sp.setThoiGianBaoHanh(Integer.parseInt(txtBaoHanh.getText())); } catch(Exception e) { sp.setThoiGianBaoHanh(0); }
        sp.setMoTa(txtMoTa.getText());
        sp.setHinhAnh(txtHinhAnh.getText().trim());
        return sp;
    }
    
    public void fillForm(SanPham sp) {
        txtMaSP.setText(sp.getMaSP());
        txtTenSP.setText(sp.getTenSP());
        txtThuongHieu.setText(sp.getThuongHieu());
        cboDonViTinh.setSelectedItem(sp.getDonViTinh());
        cboTrangThaiHang.setSelectedItem(sp.getTrangThaiHang());
        for(int i=0; i<cboDanhMuc.getItemCount(); i++) {
            DanhMuc dm = (DanhMuc) cboDanhMuc.getItemAt(i);
            if(dm.getMaDM().equals(sp.getMaDanhMuc())) {
                cboDanhMuc.setSelectedIndex(i); break;
            }
        }
        java.text.DecimalFormat df = new java.text.DecimalFormat("###0");
        txtGiaNhap.setText(df.format(sp.getGiaNhap()));
        txtGiaBan.setText(df.format(sp.getGiaBan()));
        txtBaoHanh.setText(sp.getThoiGianBaoHanh()+"");
        txtMoTa.setText(sp.getMoTa());
        txtHinhAnh.setText(sp.getHinhAnh());
        loadAnh(sp.getHinhAnh());
        setTrangThaiNut(true);
    }
    
    public void loadAnh(String link) {
        if(link != null && !link.isEmpty()) {
            try {
                URL url = new URL(link);
                Image img = ImageIO.read(url).getScaledInstance(90, 90, Image.SCALE_SMOOTH);
                lblAnhPreview.setIcon(new ImageIcon(img));
                lblAnhPreview.setText("");
            } catch (Exception e) {
                lblAnhPreview.setIcon(null); lblAnhPreview.setText("Lỗi ảnh");
            }
        } else {
            lblAnhPreview.setIcon(null); lblAnhPreview.setText("No Image");
        }
    }

    public String getMaSPChon() {
        int r = tblSanPham.getSelectedRow();
        if(r >= 0) return tblSanPham.getValueAt(r, 0).toString();
        return null;
    }
    
    public JTable getTable() { return tblSanPham; }
    public DefaultTableModel getModel() { return model; }
}
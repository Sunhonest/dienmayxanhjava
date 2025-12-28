package view.viewKho;

import domain.DanhMuc;
import domain.SanPham;
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
    private final Color COLOR_THEM = Color.decode("#4CAF50");
    private final Color COLOR_SUA = Color.decode("#FFC107");
    private final Color COLOR_XOA = Color.decode("#F44336");
    private final Color COLOR_MOI = Color.decode("#2196F3");
    private final Color COLOR_DISABLE = Color.decode("#E0E0E0");
    private final Color COLOR_TEXT_DISABLE = Color.GRAY;
    
    private final Font FONT_HEADER = new Font("Arial", Font.BOLD, 24);
    private final Font FONT_LABEL = new Font("Arial", Font.BOLD, 13);
    private final Font FONT_INPUT = new Font("Arial", Font.PLAIN, 14);

    public QuanLySanPham() {
        initComponents();
        setTrangThaiNut(false);
    }

    private void initComponents() {
        // 1. CẤU HÌNH LAYOUT TUYỆT ĐỐI TỔNG THỂ
        setLayout(null);
        setBackground(Color.WHITE);
        
        // Kích thước khả dụng ước tính từ MainForm: 
        // Width = 1300 (Main) - 280 (Menu) = 1020px
        // Height = ~700px
        // Ta set kích thước tổng thể vừa vặn vùng này
        int viewW = 1020; 
        int viewH = 700;
        setPreferredSize(new Dimension(viewW, viewH));

        // --- HEADER ---
        JLabel lblTitle = new JLabel("QUẢN LÝ SẢN PHẨM KHO");
        lblTitle.setFont(FONT_HEADER);
        lblTitle.setForeground(COLOR_MOI);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        // Header rộng full chiều ngang
        lblTitle.setBounds(0, 10, viewW, 40); 
        add(lblTitle);

        // ========================================================
        // TÍNH TOÁN KÍCH THƯỚC HAI KHU VỰC
        // ========================================================
        int margin = 20; // Lề
        int gap = 20;    // Khoảng cách giữa Bảng và Form
        
        // Form bên phải cố định chiều rộng cho đẹp
        int formW = 340; 
        
        // Bảng bên trái chiếm phần còn lại
        int tableW = viewW - (margin * 2) - formW - gap; 
        
        int startY = 60; // Vị trí bắt đầu theo chiều dọc
        int commonH = viewH - startY - 20; // Chiều cao chung (trừ đi lề dưới)

        // ========================================================
        // KHU VỰC TRÁI: BẢNG (TABLE)
        // ========================================================
        int tableX = margin;
        
        // Cấu hình Model Bảng
        String[] columns = {"Mã SP", "Tên SP", "Danh mục", "Thương hiệu", "Tồn", "ĐVT", "Giá bán"};
        model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tblSanPham = new JTable(model);
        styleTable(tblSanPham);

        JScrollPane scrTable = new JScrollPane(tblSanPham);
        scrTable.setBounds(tableX, startY, tableW, commonH);
        add(scrTable);

        // ========================================================
        // KHU VỰC PHẢI: FORM CÓ THANH CUỘN
        // ========================================================
        int formX = tableX + tableW + gap;
        
        JPanel pnlRight = new JPanel();
        pnlRight.setLayout(null);
        pnlRight.setBackground(Color.WHITE);
        pnlRight.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY), 
                "Thông tin chi tiết", 
                TitledBorder.DEFAULT_JUSTIFICATION, 
                TitledBorder.DEFAULT_POSITION, 
                new Font("Arial", Font.BOLD, 14), 
                COLOR_MOI
        ));
        pnlRight.setBounds(formX, startY, formW, commonH);
        add(pnlRight);

        // --- 1. PANEL NỘI DUNG (SCROLL CONTENT) ---
        JPanel pnlScrollContent = new JPanel();
        pnlScrollContent.setLayout(null); 
        pnlScrollContent.setBackground(Color.WHITE);

        int xLb = 10, xTxt = 110; // Label rộng 100, Text bắt đầu từ 110
        int wLb = 100, wTxt = 190; // Form rộng 340 -> trừ lề -> Text còn khoảng 190-200
        int hRow = 30;            
        int yStart = 10, yStep = 45; 

        // --- CÁC FIELD ---
        // 1. Mã SP
        pnlScrollContent.add(createLabel("Mã sản phẩm:", xLb, yStart, wLb, hRow));
        txtMaSP = createTextField(xTxt, yStart, wTxt, hRow);
        pnlScrollContent.add(txtMaSP);

        // 2. Tên SP
        yStart += yStep;
        pnlScrollContent.add(createLabel("Tên sản phẩm:", xLb, yStart, wLb, hRow));
        txtTenSP = createTextField(xTxt, yStart, wTxt, hRow);
        pnlScrollContent.add(txtTenSP);

        // 3. Danh mục
        yStart += yStep;
        pnlScrollContent.add(createLabel("Danh mục:", xLb, yStart, wLb, hRow));
        cboDanhMuc = new JComboBox<>();
        cboDanhMuc.setFont(FONT_INPUT);
        cboDanhMuc.setBackground(Color.WHITE);
        cboDanhMuc.setBounds(xTxt, yStart, wTxt, hRow);
        pnlScrollContent.add(cboDanhMuc);

        // 4. Thương hiệu
        yStart += yStep;
        pnlScrollContent.add(createLabel("Thương hiệu:", xLb, yStart, wLb, hRow));
        txtThuongHieu = createTextField(xTxt, yStart, wTxt, hRow);
        pnlScrollContent.add(txtThuongHieu);

        // 5. ĐVT & Trạng thái
        yStart += yStep;
        pnlScrollContent.add(createLabel("ĐVT / T.Thái:", xLb, yStart, wLb, hRow));
        
        cboDonViTinh = new JComboBox<>(new String[]{"Cái", "Chiếc", "Bộ", "Hộp", "Kg"});
        cboDonViTinh.setFont(FONT_INPUT);
        cboDonViTinh.setBackground(Color.WHITE);
        cboDonViTinh.setBounds(xTxt, yStart, 90, hRow); // Chia đôi chiều rộng
        pnlScrollContent.add(cboDonViTinh);
        
        cboTrangThaiHang = new JComboBox<>(new String[]{"MOI", "CU", "TRUNG_BAY"});
        cboTrangThaiHang.setFont(FONT_INPUT);
        cboTrangThaiHang.setBackground(Color.WHITE);
        cboTrangThaiHang.setBounds(xTxt + 95, yStart, 95, hRow);
        pnlScrollContent.add(cboTrangThaiHang);

        // 6. Giá nhập
        yStart += yStep;
        pnlScrollContent.add(createLabel("Giá nhập:", xLb, yStart, wLb, hRow));
        txtGiaNhap = createTextField(xTxt, yStart, wTxt, hRow);
        pnlScrollContent.add(txtGiaNhap);

        // 7. Giá bán
        yStart += yStep;
        pnlScrollContent.add(createLabel("Giá bán:", xLb, yStart, wLb, hRow));
        txtGiaBan = createTextField(xTxt, yStart, wTxt, hRow);
        pnlScrollContent.add(txtGiaBan);

        // 8. Bảo hành
        yStart += yStep;
        pnlScrollContent.add(createLabel("Bảo hành (tháng):", xLb, yStart, wLb + 10, hRow));
        txtBaoHanh = createTextField(xTxt, yStart, wTxt, hRow);
        pnlScrollContent.add(txtBaoHanh);

        // 9. Link Ảnh
        yStart += yStep;
        pnlScrollContent.add(createLabel("Link Ảnh:", xLb, yStart, wLb, hRow));
        txtHinhAnh = createTextField(xTxt, yStart, wTxt, hRow);
        pnlScrollContent.add(txtHinhAnh);

        // 10. Preview Ảnh
        int imgSize = 90;
        lblAnhPreview = new JLabel("Chưa có ảnh", SwingConstants.CENTER);
        lblAnhPreview.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        // Căn giữa ảnh trong vùng textfield
        lblAnhPreview.setBounds(xTxt + 50, yStart + 35, imgSize, imgSize);
        pnlScrollContent.add(lblAnhPreview);

        // 11. Mô tả
        yStart += yStep + imgSize + 10; 
        pnlScrollContent.add(createLabel("Mô tả:", xLb, yStart, wLb, hRow));
        
        txtMoTa = new JTextArea();
        txtMoTa.setFont(FONT_INPUT);
        txtMoTa.setLineWrap(true);
        txtMoTa.setWrapStyleWord(true);
        txtMoTa.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        
        JScrollPane scrMoTa = new JScrollPane(txtMoTa);
        scrMoTa.setBounds(xTxt, yStart, wTxt, 60);
        pnlScrollContent.add(scrMoTa);

        // --- SET CHIỀU CAO CONTENT ---
        int contentHeight = yStart + 70; 
        pnlScrollContent.setPreferredSize(new Dimension(formW - 40, contentHeight));

        // --- 2. SCROLLPANE CHỨA CONTENT ---
        // Chiều cao ScrollPane = Chiều cao Form - Chiều cao nút - Padding
        int scrollH = commonH - 80; 
        JScrollPane scrollPane = new JScrollPane(pnlScrollContent);
        scrollPane.setBounds(10, 25, formW - 20, scrollH); 
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        pnlRight.add(scrollPane);

        // --- 3. KHU VỰC NÚT BẤM ---
        int btnW = 70; // Nút nhỏ hơn chút để vừa formW 340
        int btnH = 35;
        int btnGap = 8;
        int btnY = commonH - 45; 
        int startBtnX = 15;

        btnThem = createButton("Them", "Thêm", COLOR_THEM, startBtnX, btnY, btnW, btnH);
        pnlRight.add(btnThem);

        btnSua = createButton("Sua", "Sửa", COLOR_SUA, startBtnX + btnW + btnGap, btnY, btnW, btnH);
        pnlRight.add(btnSua);

        btnXoa = createButton("Xoa", "Xóa", COLOR_XOA, startBtnX + (btnW + btnGap)*2, btnY, btnW, btnH);
        pnlRight.add(btnXoa);

        btnLamMoi = createButton("LamMoi", "Mới", COLOR_MOI, startBtnX + (btnW + btnGap)*3, btnY, 65, btnH);
        pnlRight.add(btnLamMoi);
    }

    // ========================================================================
    // HELPER METHODS (Rút gọn code cho sạch)
    // ========================================================================

    private JLabel createLabel(String text, int x, int y, int w, int h) {
        JLabel l = new JLabel(text);
        l.setFont(FONT_LABEL);
        l.setForeground(Color.DARK_GRAY);
        l.setBounds(x, y, w, h);
        return l;
    }

    private JTextField createTextField(int x, int y, int w, int h) {
        JTextField t = new JTextField();
        t.setFont(FONT_INPUT);
        t.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        t.setBounds(x, y, w, h);
        return t;
    }

    private JButton createButton(String cmd, String text, Color bg, int x, int y, int w, int h) {
        JButton b = new JButton(text);
        b.setActionCommand(cmd);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Arial", Font.BOLD, 13));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setOpaque(true);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setBounds(x, y, w, h);
        return b;
    }

    private void styleTable(JTable table) {
        table.setRowHeight(35);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setShowGrid(true);
        table.setGridColor(Color.LIGHT_GRAY);
        
        JTableHeader header = table.getTableHeader();
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 40));
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                label.setFont(new Font("Arial", Font.BOLD, 14));
                label.setBackground(COLOR_MOI);
                label.setForeground(Color.WHITE);
                label.setHorizontalAlignment(JLabel.CENTER);
                label.setOpaque(true); 
                
                // --- THÊM ĐƯỜNG KẺ TRẮNG NGĂN CÁCH CÁC CỘT ---
                // Tạo viền trắng 1px ở bên phải và bên dưới mỗi ô tiêu đề
                label.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.WHITE));
                
                return label;
            }
        });

        // Căn phải cho cột Tiền & Tồn kho
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        table.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
        table.getColumnModel().getColumn(6).setCellRenderer(rightRenderer);
        
        // Chỉnh độ rộng cột cho đẹp
        table.getColumnModel().getColumn(0).setPreferredWidth(60);  // Mã
        table.getColumnModel().getColumn(1).setPreferredWidth(150); // Tên
        table.getColumnModel().getColumn(2).setPreferredWidth(100); // Danh mục
    }

    // ========================================================================
    // LOGIC METHODS (GIỮ NGUYÊN)
    // ========================================================================

    public void setTrangThaiNut(boolean isSelect) {
        btnThem.setEnabled(!isSelect);
        btnThem.setBackground(!isSelect ? COLOR_THEM : COLOR_DISABLE);
        
        btnSua.setEnabled(isSelect);
        btnSua.setBackground(isSelect ? COLOR_SUA : COLOR_DISABLE);
        
        btnXoa.setEnabled(isSelect);
        btnXoa.setBackground(isSelect ? COLOR_XOA : COLOR_DISABLE);
        
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
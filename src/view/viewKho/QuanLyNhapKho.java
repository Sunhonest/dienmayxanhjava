package view.viewKho;

import domain.NhaCungCap;
import domain.SanPham;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.List;

public class QuanLyNhapKho extends JPanel {
    // --- KHAI BÁO BIẾN ---
    private JTextField txtMaPhieu;
    private JTextField txtNhanVien;
    private JComboBox<NhaCungCap> cboNhaCungCap;
    private JComboBox<String> cboSanPham;
    private JTextField txtTonKho;
    private JTextField txtSoLuong;
    private JTextField txtDonGia;
    
    // --- THÊM MỚI: Ô GHI CHÚ ---
    private JTextField txtGhiChu;
    
    private JButton btnThem;
    private JButton btnSua; // --- THÊM MỚI: NÚT SỬA ---
    private JButton btnXoa;
    private JButton btnLamMoi;
    
    private JTable tblLichSuNhap;
    private DefaultTableModel modelLichSu;
    private JLabel lblTongTien;

    // Cache dữ liệu
    private List<SanPham> listSPCache;

    public QuanLyNhapKho() {
        // 1. CẤU HÌNH LAYOUT TUYỆT ĐỐI
        setLayout(null);
        setBackground(Color.WHITE);
        
        // Kích thước panel tổng: 1020 x 700
        int viewW = 1020;
        int viewH = 700;
        setPreferredSize(new Dimension(viewW, viewH));

        // --- TITLE ---
        JLabel lbTitle = new JLabel("QUẢN LÝ NHẬP KHO (MODE TRỰC TIẾP)", SwingConstants.CENTER);
        lbTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lbTitle.setForeground(Color.decode("#0D47A1")); // Xanh dương đậm hơn
        lbTitle.setBounds(0, 10, viewW, 40);
        add(lbTitle);

        // ========================================================
        // KHU VỰC TRÁI: FORM NHẬP
        // ========================================================
        int formW = 380;
        int formH = 600;
        
        JPanel pnlLeft = new JPanel();
        pnlLeft.setLayout(null); 
        pnlLeft.setBackground(Color.WHITE);
        pnlLeft.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY), 
                "Thông tin nhập hàng", 
                TitledBorder.DEFAULT_JUSTIFICATION, 
                TitledBorder.DEFAULT_POSITION, 
                new Font("Arial", Font.BOLD, 14)
        ));
        pnlLeft.setBounds(20, 60, formW, formH);
        add(pnlLeft);

        // --- CÁC COMPONENT TRONG PANEL TRÁI ---
        int xLabel = 20, xText = 140;
        int wLabel = 110, wText = 210;
        int hRow = 30;
        int yStart = 30, yStep = 45; // Giảm khoảng cách yStep một chút để nhét vừa ô Ghi chú

        // 1. Mã phiếu
        JLabel lb1 = createLabel("Mã phiếu:");
        lb1.setBounds(xLabel, yStart, wLabel, hRow);
        pnlLeft.add(lb1);
        
        txtMaPhieu = new JTextField();
        txtMaPhieu.setBounds(xText, yStart, wText, hRow);
        pnlLeft.add(txtMaPhieu);

        // 2. Nhân viên
        JLabel lb2 = createLabel("Nhân viên:");
        lb2.setBounds(xLabel, yStart + yStep, wLabel, hRow);
        pnlLeft.add(lb2);
        
        txtNhanVien = new JTextField();
        txtNhanVien.setBounds(xText, yStart + yStep, wText, hRow);
        txtNhanVien.setEditable(false);
        pnlLeft.add(txtNhanVien);

        // 3. Nhà cung cấp
        JLabel lb3 = createLabel("Nhà cung cấp:");
        lb3.setBounds(xLabel, yStart + yStep*2, wLabel, hRow);
        pnlLeft.add(lb3);
        
        cboNhaCungCap = new JComboBox<>();
        cboNhaCungCap.setBounds(xText, yStart + yStep*2, wText, hRow);
        cboNhaCungCap.setBackground(Color.WHITE);
        pnlLeft.add(cboNhaCungCap);

        // 4. Sản phẩm
        JLabel lb4 = createLabel("Sản phẩm:");
        lb4.setBounds(xLabel, yStart + yStep*3, wLabel, hRow);
        pnlLeft.add(lb4);
        
        cboSanPham = new JComboBox<>();
        cboSanPham.setBounds(xText, yStart + yStep*3, wText, hRow);
        cboSanPham.setBackground(Color.WHITE);
        pnlLeft.add(cboSanPham);

        // 5. Tồn kho
        JLabel lb5 = createLabel("Tồn hiện tại:");
        lb5.setBounds(xLabel, yStart + yStep*4, wLabel, hRow);
        pnlLeft.add(lb5);
        
        txtTonKho = new JTextField();
        txtTonKho.setBounds(xText, yStart + yStep*4, wText, hRow);
        txtTonKho.setEditable(false);
        txtTonKho.setForeground(Color.RED);
        txtTonKho.setFont(new Font("Arial", Font.BOLD, 12));
        pnlLeft.add(txtTonKho);

        // 6. Số lượng
        JLabel lb6 = createLabel("Số lượng nhập:");
        lb6.setBounds(xLabel, yStart + yStep*5, wLabel, hRow);
        pnlLeft.add(lb6);
        
        txtSoLuong = new JTextField();
        txtSoLuong.setBounds(xText, yStart + yStep*5, wText, hRow);
        pnlLeft.add(txtSoLuong);

        // 7. Đơn giá
        JLabel lb7 = createLabel("Đơn giá:");
        lb7.setBounds(xLabel, yStart + yStep*6, wLabel, hRow);
        pnlLeft.add(lb7);
        
        txtDonGia = new JTextField();
        txtDonGia.setBounds(xText, yStart + yStep*6, wText, hRow);
        pnlLeft.add(txtDonGia);

        // 8. --- THÊM MỚI: Ghi chú ---
        JLabel lb8 = createLabel("Ghi chú:");
        lb8.setBounds(xLabel, yStart + yStep*7, wLabel, hRow);
        pnlLeft.add(lb8);
        
        txtGhiChu = new JTextField();
        txtGhiChu.setBounds(xText, yStart + yStep*7, wText, hRow);
        pnlLeft.add(txtGhiChu);

        // 9. Các nút bấm (Đặt ở dưới cùng Panel Trái)
        // Đẩy yBtn xuống thấp hơn vì đã thêm ô Ghi chú
        int btnY = yStart + yStep*8 + 20; 
        int btnW = 80; // Giảm chiều rộng nút một chút để nhét 4 nút vào hàng
        int btnH = 35;
        int gap = 10;
        int startXBtn = 15;

        // Nút THÊM: Màu xanh lá (#4CAF50)
        btnThem = createBtn("Thêm", Color.decode("#4CAF50")); 
        btnThem.setBounds(startXBtn, btnY, btnW, btnH);
        pnlLeft.add(btnThem);

        // --- THÊM MỚI: Nút SỬA: Màu Cam (#FF9800) ---
        btnSua = createBtn("Sửa", Color.decode("#FF9800"));
        btnSua.setBounds(startXBtn + btnW + gap, btnY, btnW, btnH);
        pnlLeft.add(btnSua);

        // Nút XÓA: Màu đỏ (#F44336)
        btnXoa = createBtn("Xóa", Color.decode("#F44336"));    
        btnXoa.setBounds(startXBtn + (btnW + gap)*2, btnY, btnW, btnH);
        pnlLeft.add(btnXoa);

        // Nút MỚI: Màu xanh dương (#2196F3)
        btnLamMoi = createBtn("Mới", Color.decode("#2196F3")); 
        btnLamMoi.setBounds(startXBtn + (btnW + gap)*3, btnY, btnW, btnH);
        pnlLeft.add(btnLamMoi);

        // ========================================================
        // KHU VỰC PHẢI: BẢNG DỮ LIỆU
        // ========================================================
        int tableW = 590;
        
        JPanel pnlRight = new JPanel();
        pnlRight.setLayout(null);
        pnlRight.setBackground(Color.WHITE);
        pnlRight.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY), 
                "Lịch sử nhập hàng (Dữ liệu Database)", 
                TitledBorder.DEFAULT_JUSTIFICATION, 
                TitledBorder.DEFAULT_POSITION, 
                new Font("Arial", Font.BOLD, 14)
        ));
        pnlRight.setBounds(420, 60, tableW, formH);
        add(pnlRight);

        // 1. Bảng (Table)
        // Có thể thêm cột "Ghi chú" vào bảng nếu muốn hiển thị
        String[] columnNames = {"ID", "Mã SP", "Tên SP", "Số lượng", "Đơn giá", "Thành tiền", "Ghi chú"};
        modelLichSu = new DefaultTableModel(columnNames, 0) {
             @Override
             public boolean isCellEditable(int row, int column) {
                 return false;
             }
        };
        tblLichSuNhap = new JTable(modelLichSu);
        
        // --- ÁP DỤNG STYLE ---
        styleTable(tblLichSuNhap);
        
        // Chỉnh độ rộng cột
        tblLichSuNhap.getColumnModel().getColumn(0).setPreferredWidth(30);  // ID
        tblLichSuNhap.getColumnModel().getColumn(1).setPreferredWidth(50);  // Mã
        tblLichSuNhap.getColumnModel().getColumn(2).setPreferredWidth(120); // Tên
        tblLichSuNhap.getColumnModel().getColumn(3).setPreferredWidth(40);  // SL
        
        JScrollPane sp = new JScrollPane(tblLichSuNhap);
        sp.setBounds(15, 30, tableW - 30, 520); 
        pnlRight.add(sp);

        // 2. Tổng tiền
        lblTongTien = new JLabel("Tổng tiền: 0 VNĐ", SwingConstants.RIGHT);
        lblTongTien.setFont(new Font("Arial", Font.BOLD, 16));
        lblTongTien.setForeground(Color.RED);
        lblTongTien.setBounds(15, 560, tableW - 30, 30);
        pnlRight.add(lblTongTien);
    }

    // ========================================================================
    // HÀM STYLE TABLE
    // ========================================================================
    private void styleTable(JTable table) {
        table.setRowHeight(30); 
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setGridColor(Color.LIGHT_GRAY);
        table.setShowVerticalLines(true); 
        table.setSelectionBackground(Color.decode("#D6EAF8")); 
        table.setSelectionForeground(Color.BLACK);

        JTableHeader header = table.getTableHeader();
        header.setPreferredSize(new Dimension(0, 40)); 
        
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                label.setFont(new Font("Arial", Font.BOLD, 14));
                label.setBackground(Color.decode("#0097D8")); 
                label.setForeground(Color.WHITE);             
                label.setHorizontalAlignment(JLabel.CENTER);
                label.setOpaque(true); 
                label.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.WHITE));
                return label;
            }
        });
    }

    private JLabel createLabel(String text) {
        JLabel lb = new JLabel(text);
        lb.setFont(new Font("Arial", Font.BOLD, 13));
        return lb;
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
    // GETTER/SETTER CHO CONTROLLER
    // ==========================================================
    
    public void addBtnListener(ActionListener ac) {
        btnThem.addActionListener(ac);
        btnXoa.addActionListener(ac);
        btnSua.addActionListener(ac); // --- THÊM LISTENER CHO NÚT SỬA ---
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
    
    public JComboBox<String> getCboSanPham() { return cboSanPham; } // Thêm getter này nếu chưa có để bắt sự kiện

    public void setSelectedSanPhamByMa(String maSP) {
        if (listSPCache == null) return;
        for(int i=0; i<listSPCache.size(); i++) {
            if(listSPCache.get(i).getMaSP().equals(maSP)) {
                cboSanPham.setSelectedIndex(i);
                break;
            }
        }
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
    
    // --- THÊM GETTER/SETTER CHO GHI CHÚ ---
    public String getGhiChu() { return txtGhiChu.getText().trim(); }
    public void setGhiChu(String s) { txtGhiChu.setText(s); }
    
    public void setTongTien(String s) { lblTongTien.setText(s); }

    public DefaultTableModel getModel() { return modelLichSu; }
    public JTable getTable() { return tblLichSuNhap; }

    public void clearForm() {
        txtSoLuong.setText("");
        txtDonGia.setText("");
        txtGhiChu.setText(""); // Xóa cả ghi chú
        if(cboSanPham.getItemCount() > 0) cboSanPham.setSelectedIndex(0);
        tblLichSuNhap.clearSelection();
    }
}
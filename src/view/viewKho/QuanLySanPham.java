package view.viewKho;

import domain.Kho.DanhMuc;
import domain.Kho.SanPham;
import java.awt.*;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

public class QuanLySanPham extends JPanel {

    // --- COMPONENTS ---
    private JTable tblSanPham;
    private DefaultTableModel model;

    // Input Fields
    private JTextField txtMaSP, txtTenSP, txtThuongHieu, txtGiaNhap, txtGiaBan, txtBaoHanh, txtHinhAnh;
    private JComboBox<Object> cboDanhMuc;
    private JComboBox<String> cboDonViTinh, cboTrangThaiHang;
    private JTextArea txtMoTa;

    // Buttons
    private JButton btnThem, btnSua, btnXoa, btnLamMoi;

    // --- COLORS & FONTS ---
    private final Color COLOR_PRIMARY = Color.decode("#2196F3");
    private final Font FONT_HEADER = new Font("Arial", Font.BOLD, 26);
    private final Font FONT_SECTION = new Font("Arial", Font.BOLD, 16);
    private final Font FONT_LABEL = new Font("Arial", Font.BOLD, 12);
    private final Font FONT_INPUT = new Font("Arial", Font.PLAIN, 14);

    public QuanLySanPham() {
        initComponents();
        setTrangThaiNut(false);
    }

    private void initComponents() {
        // Layout chính: BorderLayout
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        // Tăng lề trên lên 20px để hạ thấp tiêu đề xuống theo yêu cầu
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // ====================================================================
        // 1. HEADER (TIÊU ĐỀ LỚN)
        // ====================================================================
        JLabel lblTitle = new JLabel("QUẢN LÝ SẢN PHẨM", SwingConstants.CENTER);
        lblTitle.setFont(FONT_HEADER);
        lblTitle.setForeground(COLOR_PRIMARY);
        add(lblTitle, BorderLayout.NORTH);

        // ====================================================================
        // 2. CENTER (FORM NHẬP + BẢNG)
        // ====================================================================
        JPanel pnlCenter = new JPanel(new BorderLayout(0, 15));
        pnlCenter.setBackground(Color.WHITE);

        // --- A. FORM NHẬP LIỆU (QUAY VỀ KIỂU CŨ: TITLEDBORDER) ---
        JPanel pnlInputWrapper = new JPanel(new BorderLayout());
        pnlInputWrapper.setBackground(Color.WHITE);
        
        // Viền Xám - Tiêu đề Xanh (Kiểu ban đầu bạn ưng ý)
        TitledBorder borderForm = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1), 
                " THÔNG TIN CHI TIẾT ", 
                TitledBorder.DEFAULT_JUSTIFICATION, 
                TitledBorder.DEFAULT_POSITION, 
                FONT_SECTION, 
                COLOR_PRIMARY
        );
        pnlInputWrapper.setBorder(borderForm);

        // Grid 3 hàng, 4 cột
        JPanel pnlInputs = new JPanel(new GridLayout(3, 4, 20, 15));
        pnlInputs.setBackground(Color.WHITE);
        pnlInputs.setBorder(new EmptyBorder(15, 20, 10, 20));

        // -- Hàng 1
        txtMaSP = createField(pnlInputs, "Mã sản phẩm:");
        txtTenSP = createField(pnlInputs, "Tên sản phẩm:");
        
        JPanel pDM = new JPanel(new BorderLayout()); pDM.setBackground(Color.WHITE);
        pDM.add(createLabel("Danh mục:"), BorderLayout.NORTH);
        cboDanhMuc = new JComboBox<>(); styleComponent(cboDanhMuc);
        pDM.add(cboDanhMuc, BorderLayout.CENTER);
        pnlInputs.add(pDM);

        txtThuongHieu = createField(pnlInputs, "Thương hiệu:");

        // -- Hàng 2
        txtGiaNhap = createField(pnlInputs, "Giá nhập:");
        txtGiaBan = createField(pnlInputs, "Giá bán:");
        
        JPanel pDVT = new JPanel(new BorderLayout()); pDVT.setBackground(Color.WHITE);
        pDVT.add(createLabel("Đơn vị tính:"), BorderLayout.NORTH);
        cboDonViTinh = new JComboBox<>(new String[]{"Cái", "Chiếc", "Bộ", "Hộp", "Kg"}); 
        styleComponent(cboDonViTinh);
        pDVT.add(cboDonViTinh, BorderLayout.CENTER);
        pnlInputs.add(pDVT);
        
        JPanel pTT = new JPanel(new BorderLayout()); pTT.setBackground(Color.WHITE);
        pTT.add(createLabel("Trạng thái:"), BorderLayout.NORTH);
        cboTrangThaiHang = new JComboBox<>(new String[]{"MOI", "CU", "TRUNG_BAY"}); 
        styleComponent(cboTrangThaiHang);
        pTT.add(cboTrangThaiHang, BorderLayout.CENTER);
        pnlInputs.add(pTT);

        // -- Hàng 3
        txtBaoHanh = createField(pnlInputs, "Bảo hành (tháng):");
        txtHinhAnh = createField(pnlInputs, "Link Ảnh:");
        
        // Mô tả
        JPanel pMoTa = new JPanel(new BorderLayout()); pMoTa.setBackground(Color.WHITE);
        pMoTa.add(createLabel("Mô tả:"), BorderLayout.NORTH);
        txtMoTa = new JTextArea(1, 1);
        txtMoTa.setFont(FONT_INPUT);
        txtMoTa.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        pMoTa.add(new JScrollPane(txtMoTa), BorderLayout.CENTER);
        pnlInputs.add(pMoTa);
        
        pnlInputs.add(new JLabel("")); // Filler

        pnlInputWrapper.add(pnlInputs, BorderLayout.CENTER);

        // Panel Nút bấm
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        pnlButtons.setBackground(Color.WHITE);
        pnlButtons.setBorder(new EmptyBorder(5, 0, 10, 0));
        
        btnThem = createButton("Them", "Thêm", Color.decode("#4CAF50"));
        btnSua = createButton("Sua", "Sửa", Color.decode("#FFC107"));
        btnXoa = createButton("Xoa", "Xóa", Color.decode("#F44336"));
        btnLamMoi = createButton("LamMoi", "Làm mới", COLOR_PRIMARY);

        pnlButtons.add(btnThem); pnlButtons.add(btnSua); pnlButtons.add(btnXoa); pnlButtons.add(btnLamMoi);
        pnlInputWrapper.add(pnlButtons, BorderLayout.SOUTH);

        pnlCenter.add(pnlInputWrapper, BorderLayout.NORTH);

        // --- B. BẢNG DỮ LIỆU ---
        String[] columns = {"Mã SP", "Hình ảnh", "Tên SP", "Danh mục", "Thương hiệu", "Tồn", "ĐVT", "Giá bán"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 1 ? Icon.class : String.class;
            }
        };
        
        tblSanPham = new JTable(model);
        styleTable(tblSanPham);

        JScrollPane scrTable = new JScrollPane(tblSanPham);
        scrTable.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        
        pnlCenter.add(scrTable, BorderLayout.CENTER);
        
        add(pnlCenter, BorderLayout.CENTER);
    }

    // ========================================================================
    // HELPER METHODS
    // ========================================================================
    
    private void styleTable(JTable table) {
        table.setRowHeight(80);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setSelectionBackground(Color.decode("#BBDEFB"));
        table.setSelectionForeground(Color.BLACK);
        table.setShowGrid(true);
        table.setGridColor(Color.LIGHT_GRAY);
        
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
        
        TableColumnModel cm = table.getColumnModel();
        cm.getColumn(0).setPreferredWidth(70);
        cm.getColumn(1).setPreferredWidth(90);
        cm.getColumn(2).setPreferredWidth(200);
        
        // Sử dụng Renderer có Caching để mượt mà
        cm.getColumn(1).setCellRenderer(new CachingImageRenderer());
        
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        cm.getColumn(7).setCellRenderer(rightRenderer);
    }

    class CachingImageRenderer extends DefaultTableCellRenderer {
        private Map<String, ImageIcon> imageCache = new HashMap<>();

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = new JLabel();
            label.setHorizontalAlignment(JLabel.CENTER);
            label.setOpaque(true);
            label.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());

            if (value != null && !value.toString().trim().isEmpty()) {
                String urlStr = value.toString();
                if (imageCache.containsKey(urlStr)) {
                    label.setIcon(imageCache.get(urlStr));
                } else {
                    try {
                        URL url = new URL(urlStr);
                        ImageIcon icon = new ImageIcon(url);
                        if (icon.getIconWidth() > 0) {
                             Image img = icon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
                             ImageIcon scaledIcon = new ImageIcon(img);
                             imageCache.put(urlStr, scaledIcon);
                             label.setIcon(scaledIcon);
                        } else {
                            label.setText("Lỗi");
                        }
                    } catch (Exception e) {
                        label.setText("Lỗi Link");
                    }
                }
            } else {
                label.setText("No Img");
            }
            return label;
        }
    }

    private JTextField createField(JPanel panel, String labelText) {
        JPanel p = new JPanel(new BorderLayout()); 
        p.setBackground(Color.WHITE);
        p.add(createLabel(labelText), BorderLayout.NORTH);
        JTextField txt = new JTextField();
        styleComponent(txt);
        p.add(txt, BorderLayout.CENTER);
        panel.add(p);
        return txt;
    }

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(FONT_LABEL);
        lbl.setForeground(Color.DARK_GRAY);
        return lbl;
    }

    private void styleComponent(JComponent comp) {
        comp.setFont(FONT_INPUT);
        comp.setBackground(Color.WHITE);
        comp.setPreferredSize(new Dimension(100, 32));
        if (comp instanceof JTextField || comp instanceof JComboBox) {
            ((JComponent) comp).setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(0, 5, 0, 5)
            ));
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
        b.setPreferredSize(new Dimension(120, 38));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    // ========================================================================
    // GETTERS & SETTERS
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
        setTrangThaiNut(true);
    }

    public String getMaSPChon() {
        int r = tblSanPham.getSelectedRow();
        if(r >= 0) return tblSanPham.getValueAt(r, 0).toString();
        return null;
    }
    
    public JTable getTable() { return tblSanPham; }
    public DefaultTableModel getModel() { return model; }
}
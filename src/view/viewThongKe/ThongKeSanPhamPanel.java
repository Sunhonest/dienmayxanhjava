
package view.viewThongKe;
import controller.ThongKe.ThongKeController;
import model.Kho.DanhMucDAO;
import domain.Kho.DanhMuc;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ThongKeSanPhamPanel extends JPanel {
    private JTable tblThongKe;
    private DefaultTableModel model;
    private JSpinner spnTuNgay, spnDenNgay;
    private JComboBox<String> cboLoaiThongKe;
    private JComboBox<Object> cboDanhMuc;
    private JSpinner spnSoLuong, spnNguongTonKho;
    private JLabel lblTongSanPham, lblTongSoLuongBan, lblTongDoanhThu;
    private JButton btnThongKe, btnLamMoi;
    private ThongKeController thongKeController;
    private DanhMucDAO danhMucDAO;
    private final Color COLOR_PRIMARY = Color.decode("#2196F3");
    private final Color COLOR_SUCCESS = Color.decode("#4CAF50");
    private final Color COLOR_WARNING = Color.decode("#FF9800");
    private final Color COLOR_INFO = Color.decode("#00BCD4");
    private final Font FONT_HEADER = new Font("Arial", Font.BOLD, 24);
    private final Font FONT_LABEL = new Font("Arial", Font.BOLD, 14);
    private final Font FONT_INPUT = new Font("Arial", Font.PLAIN, 14);
    private final Font FONT_SUMMARY = new Font("Arial", Font.BOLD, 16);
    public ThongKeSanPhamPanel() {
        thongKeController = new ThongKeController();
        danhMucDAO = new DanhMucDAO();
        initComponents();
        setDefaultValues();
        loadDanhMuc();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel lblTitle = new JLabel("THỐNG KÊ SẢN PHẨM BÁN CHẠY");
        lblTitle.setFont(FONT_HEADER);
        lblTitle.setForeground(COLOR_PRIMARY);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblTitle, BorderLayout.NORTH);
        
        JPanel pnlMain = new JPanel(new BorderLayout(10, 10));
        pnlMain.setBackground(Color.WHITE);
        
        JPanel pnlTop = new JPanel();
        pnlTop.setLayout(new BoxLayout(pnlTop, BoxLayout.Y_AXIS));
        pnlTop.setBackground(Color.WHITE);
        
        JPanel pnlFilter = createFilterPanel();
        JPanel pnlSummary = createSummaryPanel();
        pnlTop.add(pnlFilter);
        pnlTop.add(Box.createVerticalStrut(10));
        pnlTop.add(pnlSummary);
        
        pnlMain.add(pnlTop, BorderLayout.NORTH);
        
        JPanel pnlTable = createTableArea();
        pnlMain.add(pnlTable, BorderLayout.CENTER);
        
        add(pnlMain, BorderLayout.CENTER);

        JPanel pnlButtons = createButtonArea();
        add(pnlButtons, BorderLayout.SOUTH);

        addActionListeners();
    }
    
    private JPanel createFilterPanel() {
        JPanel pnlFilter = new JPanel();
        pnlFilter.setLayout(null);
        pnlFilter.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY),
                "Bộ lọc thống kê", TitledBorder.LEFT, TitledBorder.TOP, FONT_LABEL));
        pnlFilter.setBackground(Color.WHITE);
        pnlFilter.setPreferredSize(new Dimension(0, 160));
        pnlFilter.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));
        
        int gap = 15, lblW = 120, inputW = 200;

        JLabel lblLoaiThongKe = new JLabel("Loại thống kê:");
        lblLoaiThongKe.setBounds(gap, 25, lblW, 30);
        lblLoaiThongKe.setFont(FONT_LABEL);
        pnlFilter.add(lblLoaiThongKe);
        
        cboLoaiThongKe = new JComboBox<>(new String[]{
            "Sản phẩm bán chạy", 
            "Sản phẩm bán chậm", 
            "Theo danh mục", 
            "Sắp hết hàng",
            "Doanh thu theo danh mục"
        });
        cboLoaiThongKe.setBounds(lblW + gap, 25, 400, 30);  // Full width
        cboLoaiThongKe.setFont(FONT_INPUT);
        pnlFilter.add(cboLoaiThongKe);
        cboLoaiThongKe.addActionListener(e -> updateFilterVisibility());
        
        JLabel lblTuNgay = new JLabel("Từ ngày:");
        lblTuNgay.setBounds(gap, 60, lblW, 30);
        lblTuNgay.setFont(FONT_LABEL);
        pnlFilter.add(lblTuNgay);
        
        spnTuNgay = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editor1 = new JSpinner.DateEditor(spnTuNgay, "dd/MM/yyyy");
        spnTuNgay.setEditor(editor1);
        spnTuNgay.setBounds(lblW + gap, 60, inputW, 30);
        pnlFilter.add(spnTuNgay);
        
        JLabel lblDenNgay = new JLabel("Đến ngày:");
        lblDenNgay.setBounds(lblW + inputW + gap * 2, 60, lblW, 30);
        lblDenNgay.setFont(FONT_LABEL);
        pnlFilter.add(lblDenNgay);
        
        spnDenNgay = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editor2 = new JSpinner.DateEditor(spnDenNgay, "dd/MM/yyyy");
        spnDenNgay.setEditor(editor2);
        spnDenNgay.setBounds(lblW * 2 + inputW + gap * 3, 60, inputW, 30);
        pnlFilter.add(spnDenNgay);
        
        JLabel lblSoLuong = new JLabel("Số lượng (Top):");
        lblSoLuong.setBounds(lblW * 2 + inputW * 2 + gap * 4, 60, lblW, 30);
        lblSoLuong.setFont(FONT_LABEL);
        pnlFilter.add(lblSoLuong);
        
        spnSoLuong = new JSpinner(new SpinnerNumberModel(10, 1, 100, 1));
        spnSoLuong.setBounds(lblW * 3 + inputW * 2 + gap * 5, 60, 80, 30);
        spnSoLuong.setFont(FONT_INPUT);
        pnlFilter.add(spnSoLuong);
        
        JLabel lblDanhMuc = new JLabel("Danh mục:");
        lblDanhMuc.setBounds(gap, 95, lblW, 30);
        lblDanhMuc.setFont(FONT_LABEL);
        pnlFilter.add(lblDanhMuc);
        
        cboDanhMuc = new JComboBox<>();
        cboDanhMuc.setBounds(lblW + gap, 95, inputW, 30);
        cboDanhMuc.setFont(FONT_INPUT);
        pnlFilter.add(cboDanhMuc);
        
        JLabel lblNguongTonKho = new JLabel("Ngưỡng tồn kho:");
        lblNguongTonKho.setBounds(lblW + inputW + gap * 2, 95, lblW + 20, 30);
        lblNguongTonKho.setFont(FONT_LABEL);
        pnlFilter.add(lblNguongTonKho);
        
        spnNguongTonKho = new JSpinner(new SpinnerNumberModel(10, 1, 1000, 1));
        spnNguongTonKho.setBounds(lblW * 2 + inputW + gap * 3, 95, inputW, 30);
        spnNguongTonKho.setFont(FONT_INPUT);
        pnlFilter.add(spnNguongTonKho);
        
        return pnlFilter;
    }
    
    private JPanel createSummaryPanel() {
        JPanel pnlSummary = new JPanel();
        pnlSummary.setLayout(null);
        pnlSummary.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY),
                "Tổng kết", TitledBorder.LEFT, TitledBorder.TOP, FONT_LABEL));
        pnlSummary.setBackground(Color.WHITE);
        pnlSummary.setPreferredSize(new Dimension(0, 90));
        pnlSummary.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        
        int labelW = 250, labelH = 25, gap = 30;
        
        lblTongSanPham = new JLabel("Tổng số sản phẩm: 0");
        lblTongSanPham.setBounds(gap, 25, labelW, labelH);
        lblTongSanPham.setFont(FONT_SUMMARY);
        lblTongSanPham.setForeground(COLOR_SUCCESS);
        pnlSummary.add(lblTongSanPham);
        
        lblTongSoLuongBan = new JLabel("Tổng số lượng bán: 0");
        lblTongSoLuongBan.setBounds(gap + labelW, 25, labelW, labelH);
        lblTongSoLuongBan.setFont(FONT_SUMMARY);
        lblTongSoLuongBan.setForeground(COLOR_INFO);
        pnlSummary.add(lblTongSoLuongBan);
        
        lblTongDoanhThu = new JLabel("Tổng doanh thu: 0 VNĐ");
        lblTongDoanhThu.setBounds(gap + labelW * 2, 25, labelW, labelH);
        lblTongDoanhThu.setFont(FONT_SUMMARY);
        lblTongDoanhThu.setForeground(COLOR_WARNING);
        pnlSummary.add(lblTongDoanhThu);
        return pnlSummary;
    }
    
    private JPanel createTableArea() {
        String[] columns = {"Mã SP", "Tên sản phẩm", "Danh mục", "Số lượng bán", "Doanh thu", "Giá bán", "Tồn kho", "Trạng thái"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        
        tblThongKe = new JTable(model);
        styleTable(tblThongKe);
        
        JPanel pnlTable = new JPanel(new BorderLayout());
        pnlTable.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY),
                "Chi tiết thống kê", TitledBorder.LEFT, TitledBorder.TOP, FONT_LABEL));
        pnlTable.setBackground(Color.WHITE);
        pnlTable.add(new JScrollPane(tblThongKe), BorderLayout.CENTER);
        return pnlTable;
    }
    
    private JPanel createButtonArea() {
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        pnlButtons.setBackground(Color.WHITE);
        pnlButtons.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        btnThongKe = styleButton(new JButton("Thống kê"), COLOR_SUCCESS);
        btnLamMoi = styleButton(new JButton("Làm mới"), COLOR_PRIMARY);
        
        pnlButtons.add(btnThongKe);
        pnlButtons.add(btnLamMoi);
        
        return pnlButtons;
    }
    
    private JButton styleButton(JButton button, Color bgColor) {
        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(120, 40));
        return button;
    }
    
    private void addActionListeners() {
        btnThongKe.addActionListener(e -> thongKe());
        btnLamMoi.addActionListener(e -> lamMoi());
    }
    
    private void updateFilterVisibility() {
        String loaiThongKe = (String) cboLoaiThongKe.getSelectedItem();
        
        boolean showDateRange = !"Sắp hết hàng".equals(loaiThongKe);
        boolean showDanhMuc = "Theo danh mục".equals(loaiThongKe);
        boolean showSoLuong = "Sản phẩm bán chạy".equals(loaiThongKe) || "Sản phẩm bán chậm".equals(loaiThongKe);
        boolean showNguongTonKho = "Sắp hết hàng".equals(loaiThongKe);
        
        spnTuNgay.setVisible(showDateRange);
        spnDenNgay.setVisible(showDateRange);
        cboDanhMuc.setVisible(showDanhMuc);
        spnSoLuong.setVisible(showSoLuong);
        spnNguongTonKho.setVisible(showNguongTonKho);
        
        Container parent = spnTuNgay.getParent();
        for (Component comp : parent.getComponents()) {
            if (comp instanceof JLabel) {
                JLabel lbl = (JLabel) comp;
                String text = lbl.getText();
                if ("Từ ngày:".equals(text) || "Đến ngày:".equals(text)) {
                    lbl.setVisible(showDateRange);
                } else if ("Danh mục:".equals(text)) {
                    lbl.setVisible(showDanhMuc);
                } else if ("Số lượng (Top):".equals(text)) {
                    lbl.setVisible(showSoLuong);
                } else if ("Ngưỡng tồn kho:".equals(text)) {
                    lbl.setVisible(showNguongTonKho);
                }
            }
        }
    }
    
    private void styleTable(JTable table) {
        table.setRowHeight(35);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.setSelectionBackground(Color.decode("#BBDEFB"));
        table.setSelectionForeground(Color.BLACK);
        table.setShowGrid(true);
        table.setGridColor(Color.LIGHT_GRAY);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JTableHeader header = table.getTableHeader();
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 40));
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                label.setFont(new Font("Arial", Font.BOLD, 13));
                label.setBackground(COLOR_PRIMARY);
                label.setForeground(Color.WHITE);
                label.setHorizontalAlignment(JLabel.CENTER);
                label.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.WHITE));
                return label;
            }
        });
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);  
        table.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);  
        table.getColumnModel().getColumn(5).setCellRenderer(rightRenderer); 
        table.getColumnModel().getColumn(6).setCellRenderer(centerRenderer); 
        table.getColumnModel().getColumn(7).setCellRenderer(centerRenderer); 
    }
    
    private void setDefaultValues() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        spnTuNgay.setValue(cal.getTime());
        
        cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        spnDenNgay.setValue(cal.getTime());
        
        updateFilterVisibility();
    }
    
    private void loadDanhMuc() {
        cboDanhMuc.removeAllItems();
        cboDanhMuc.addItem("-- Tất cả danh mục --");
        
        List<DanhMuc> listDanhMuc = danhMucDAO.getAll();
        for (DanhMuc dm : listDanhMuc) {
            cboDanhMuc.addItem(dm);
        }
    }
    
    private void thongKe() {
        try {
            String loaiThongKe = (String) cboLoaiThongKe.getSelectedItem();
            List<domain.ThongKeSanPham> list = null;
            
            Date tuNgay = (Date) spnTuNgay.getValue();
            Date denNgay = (Date) spnDenNgay.getValue();
            int soLuong = (Integer) spnSoLuong.getValue();
            int nguongTonKho = (Integer) spnNguongTonKho.getValue();
            
            switch (loaiThongKe) {
                case "Sản phẩm bán chạy":
                    if (!thongKeController.validateTimeRange(tuNgay, denNgay)) return;
                    list = thongKeController.thongKeSanPhamBanChay(tuNgay, denNgay, soLuong);
                    break;
                    
                case "Sản phẩm bán chậm":
                    if (!thongKeController.validateTimeRange(tuNgay, denNgay)) return;
                    list = thongKeController.thongKeSanPhamBanCham(tuNgay, denNgay, soLuong);
                    break;
                    
                case "Theo danh mục":
                    if (!thongKeController.validateTimeRange(tuNgay, denNgay)) return;
                    String maDanhMuc = null;
                    if (cboDanhMuc.getSelectedIndex() > 0) {
                        DanhMuc dm = (DanhMuc) cboDanhMuc.getSelectedItem();
                        maDanhMuc = dm.getMaDM();
                    }
                    list = thongKeController.thongKeSanPhamTheoDanhMuc(maDanhMuc, tuNgay, denNgay);
                    break;
                    
                case "Sắp hết hàng":
                    list = thongKeController.thongKeSanPhamSapHetHang(nguongTonKho);
                    break;
                    
                case "Doanh thu theo danh mục":
                    if (!thongKeController.validateTimeRange(tuNgay, denNgay)) return;
                    list = thongKeController.thongKeDoanhThuTheoDanhMuc(tuNgay, denNgay);
                    break;
            }
            
            loadData(list);
            updateSummary(list);
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Có lỗi xảy ra khi thống kê: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void loadData(List<domain.ThongKeSanPham> list) {
        model.setRowCount(0);
        DecimalFormat df = new DecimalFormat("#,###");
        
        if (list != null) {
            for (domain.ThongKeSanPham tk : list) {
                model.addRow(new Object[]{
                    tk.getMaSP(),
                    tk.getTenSP(),
                    tk.getDanhMuc(),
                    tk.getSoLuongBan(),
                    df.format(tk.getDoanhThu()),
                    df.format(tk.getGiaBan()),
                    tk.getTonKho(),
                    tk.getTrangThai()
                });
            }
        }
    }
    
    private void updateSummary(List<domain.ThongKeSanPham> list) {
        DecimalFormat df = new DecimalFormat("#,###");
        
        if (list != null && !list.isEmpty()) {
            int tongSanPham = list.size();
            int tongSoLuongBan = list.stream().mapToInt(domain.ThongKeSanPham::getSoLuongBan).sum();
            double tongDoanhThu = list.stream().mapToDouble(domain.ThongKeSanPham::getDoanhThu).sum();
            
            lblTongSanPham.setText("Tổng số sản phẩm: " + tongSanPham);
            lblTongSoLuongBan.setText("Tổng số lượng bán: " + tongSoLuongBan);
            lblTongDoanhThu.setText("Tổng doanh thu: " + df.format(tongDoanhThu) + " VNĐ");
        } else {
            lblTongSanPham.setText("Tổng số sản phẩm: 0");
            lblTongSoLuongBan.setText("Tổng số lượng bán: 0");
            lblTongDoanhThu.setText("Tổng doanh thu: 0 VNĐ");
        }
    }
    
    private void lamMoi() {
        model.setRowCount(0);
        setDefaultValues();
        updateSummary(null);
    }
}
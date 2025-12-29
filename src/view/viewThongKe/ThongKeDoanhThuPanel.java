package view.viewThongKe;

import controller.ThongKe.ThongKeController;

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

public class ThongKeDoanhThuPanel extends JPanel {
    
    // Components
    private JTable tblThongKe;
    private DefaultTableModel model;
    
    // Date selectors
    private JSpinner spnTuNgay, spnDenNgay;
    private JComboBox<String> cboLoaiThongKe;
    private JSpinner spnNam;
    
    // Summary labels
    private JLabel lblTongDoanhThu, lblTongGiamGia, lblDoanhThuThucTe, lblSoDonHang;
    
    // Buttons
    private JButton btnThongKe, btnXuat, btnLamMoi;
    
    // Controller
    private ThongKeController thongKeController;
    
    // Colors & Fonts - Updated to match voucher management style
    private final Color COLOR_PRIMARY = Color.decode("#2196F3");
    private final Color COLOR_SUCCESS = Color.decode("#4CAF50");
    private final Color COLOR_WARNING = Color.decode("#FF9800");
    private final Color COLOR_INFO = Color.decode("#00BCD4");
    private final Color COLOR_DANGER = Color.decode("#F44336");
    private final Color COLOR_PURPLE = Color.decode("#9C27B0");
    
    private final Font FONT_HEADER = new Font("Arial", Font.BOLD, 24);
    private final Font FONT_FORM_TITLE = new Font("Arial", Font.BOLD, 18);
    private final Font FONT_LABEL = new Font("Arial", Font.BOLD, 14);
    private final Font FONT_INPUT = new Font("Arial", Font.PLAIN, 14);
    private final Font FONT_SUMMARY = new Font("Arial", Font.BOLD, 16);
    
    public ThongKeDoanhThuPanel() {
        thongKeController = new ThongKeController();
        initComponents();
        setDefaultValues();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // --- TITLE ---
        JLabel lblTitle = new JLabel("THỐNG KÊ DOANH THU");
        lblTitle.setFont(FONT_HEADER);
        lblTitle.setForeground(COLOR_PRIMARY);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblTitle, BorderLayout.NORTH);
        
        // --- CENTER: Main Content ---
        JPanel pnlMain = new JPanel(new BorderLayout(10, 10));
        pnlMain.setBackground(Color.WHITE);
        
        // Top: Filter + Summary
        JPanel pnlTop = new JPanel();
        pnlTop.setLayout(new BoxLayout(pnlTop, BoxLayout.Y_AXIS));
        pnlTop.setBackground(Color.WHITE);
        
        JPanel pnlFilter = createFilterPanel();
        JPanel pnlSummary = createSummaryPanel();
        pnlTop.add(pnlFilter);
        pnlTop.add(Box.createVerticalStrut(10));
        pnlTop.add(pnlSummary);
        
        pnlMain.add(pnlTop, BorderLayout.NORTH);
        
        // Center: Table
        JPanel pnlTable = createTableArea();
        pnlMain.add(pnlTable, BorderLayout.CENTER);
        
        add(pnlMain, BorderLayout.CENTER);
        
        // --- SOUTH: Buttons ---
        JPanel pnlButtons = createButtonArea();
        add(pnlButtons, BorderLayout.SOUTH);
        
        // Add action listeners
        addActionListeners();
    }
    
    private JPanel createFilterPanel() {
        JPanel pnlFilter = new JPanel();
        pnlFilter.setLayout(null);
        pnlFilter.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY),
                "Bộ lọc thống kê", TitledBorder.LEFT, TitledBorder.TOP, FONT_LABEL));
        pnlFilter.setBackground(Color.WHITE);
        pnlFilter.setPreferredSize(new Dimension(0, 120));
        pnlFilter.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        
        int lblW = 100, inputW = 250, gap = 20; // tăng inputW cho select loại thống kê
        
        // Row 1
        JLabel lblLoaiThongKe = new JLabel("Loại thống kê:");
        lblLoaiThongKe.setBounds(gap, 25, lblW, 30);
        lblLoaiThongKe.setFont(FONT_LABEL);
        pnlFilter.add(lblLoaiThongKe);
        
        cboLoaiThongKe = new JComboBox<>(new String[]{"Theo ngày", "Theo tháng", "Theo năm", "Theo phương thức TT"});
        cboLoaiThongKe.setBounds(lblW + gap, 25, inputW, 30); // tăng chiều dài
        cboLoaiThongKe.setFont(FONT_INPUT);
        pnlFilter.add(cboLoaiThongKe);
        
        // Row 1 - continued
        JLabel lblTuNgay = new JLabel("Từ ngày:");
        lblTuNgay.setBounds(lblW + inputW + gap * 2, 25, lblW, 30);
        lblTuNgay.setFont(FONT_LABEL);
        pnlFilter.add(lblTuNgay);
        
        spnTuNgay = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editor1 = new JSpinner.DateEditor(spnTuNgay, "dd/MM/yyyy");
        spnTuNgay.setEditor(editor1);
        spnTuNgay.setBounds(lblW * 2 + inputW + gap * 3, 25, inputW, 30);
        pnlFilter.add(spnTuNgay);
        
        // Row 1 - continued
        JLabel lblDenNgay = new JLabel("Đến ngày:");
        lblDenNgay.setBounds(lblW * 2 + inputW * 2 + gap * 4, 25, lblW, 30);
        lblDenNgay.setFont(FONT_LABEL);
        pnlFilter.add(lblDenNgay);
        
        spnDenNgay = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editor2 = new JSpinner.DateEditor(spnDenNgay, "dd/MM/yyyy");
        spnDenNgay.setEditor(editor2);
        spnDenNgay.setBounds(lblW * 3 + inputW * 2 + gap * 5, 25, inputW, 30);
        pnlFilter.add(spnDenNgay);
        
        // Row 2 - For year selection
        JLabel lblNam = new JLabel("Năm:");
        lblNam.setBounds(gap, 60, lblW, 30);
        lblNam.setFont(FONT_LABEL);
        pnlFilter.add(lblNam);
        
        spnNam = new JSpinner(new SpinnerNumberModel(Calendar.getInstance().get(Calendar.YEAR), 2000, 2050, 1));
        spnNam.setBounds(lblW + gap, 60, 100, 30);
        spnNam.setFont(FONT_INPUT);
        pnlFilter.add(spnNam);
        
        cboLoaiThongKe.addActionListener(e -> updateFilterVisibility());
        return pnlFilter;
    }
    
    private JPanel createSummaryPanel() {
        JPanel pnlSummary = new JPanel();
        pnlSummary.setLayout(null);
        pnlSummary.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY),
                "Tổng kết", TitledBorder.LEFT, TitledBorder.TOP, FONT_LABEL));
        pnlSummary.setBackground(Color.WHITE);
        pnlSummary.setPreferredSize(new Dimension(0, 100));
        pnlSummary.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        
        int labelW = 280, labelH = 25, gap = 15;
        
        lblTongDoanhThu = new JLabel("Tổng doanh thu: 0 VNĐ");
        lblTongDoanhThu.setBounds(gap, 25, labelW, labelH);
        lblTongDoanhThu.setFont(FONT_SUMMARY);
        lblTongDoanhThu.setForeground(COLOR_SUCCESS);
        pnlSummary.add(lblTongDoanhThu);
        
        lblTongGiamGia = new JLabel("Tổng giảm giá: 0 VNĐ");
        lblTongGiamGia.setBounds(gap + labelW + gap, 25, labelW, labelH);
        lblTongGiamGia.setFont(FONT_SUMMARY);
        lblTongGiamGia.setForeground(COLOR_WARNING);
        pnlSummary.add(lblTongGiamGia);
        
        lblDoanhThuThucTe = new JLabel("Doanh thu thực tế: 0 VNĐ");
        lblDoanhThuThucTe.setBounds(gap, 50, labelW, labelH);
        lblDoanhThuThucTe.setFont(FONT_SUMMARY);
        lblDoanhThuThucTe.setForeground(COLOR_INFO);
        pnlSummary.add(lblDoanhThuThucTe);
        
        lblSoDonHang = new JLabel("Số đơn hàng: 0");
        lblSoDonHang.setBounds(gap + labelW + gap, 50, labelW, labelH);
        lblSoDonHang.setFont(FONT_SUMMARY);
        lblSoDonHang.setForeground(COLOR_PRIMARY);
        pnlSummary.add(lblSoDonHang);
        return pnlSummary;
    }
    
    private JPanel createTableArea() {
        String[] columns = {"Khoảng thời gian", "Tổng doanh thu", "Tổng giảm giá", "Doanh thu thực tế", "Số đơn hàng", "Số đơn hủy"};
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
        
        boolean showDateRange = !"Theo tháng".equals(loaiThongKe);
        boolean showYear = "Theo tháng".equals(loaiThongKe);
        
        spnTuNgay.setVisible(showDateRange);
        spnDenNgay.setVisible(showDateRange);
        spnNam.setVisible(showYear);
        
        // Find and show/hide labels
        Container parent = spnTuNgay.getParent();
        for (Component comp : parent.getComponents()) {
            if (comp instanceof JLabel) {
                JLabel lbl = (JLabel) comp;
                if ("Từ ngày:".equals(lbl.getText()) || "Đến ngày:".equals(lbl.getText())) {
                    lbl.setVisible(showDateRange);
                } else if ("Năm:".equals(lbl.getText())) {
                    lbl.setVisible(showYear);
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
        
        // Right align number columns
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        for (int i = 1; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(rightRenderer);
        }
    }
    
    private void setDefaultValues() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        spnTuNgay.setValue(cal.getTime());
        
        cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        spnDenNgay.setValue(cal.getTime());
        
        spnNam.setValue(Calendar.getInstance().get(Calendar.YEAR));
        updateFilterVisibility();
    }
    
    private void thongKe() {
        try {
            String loaiThongKe = (String) cboLoaiThongKe.getSelectedItem();
            List<domain.ThongKeDoanhThu> list = null;
            domain.ThongKeDoanhThu tongKet = null;
            
            switch (loaiThongKe) {
                case "Theo ngày":
                    Date tuNgay = (Date) spnTuNgay.getValue();
                    Date denNgay = (Date) spnDenNgay.getValue();
                    if (!thongKeController.validateTimeRange(tuNgay, denNgay)) return;
                    
                    list = thongKeController.thongKeDoanhThuTheoNgay(tuNgay, denNgay);
                    tongKet = thongKeController.thongKeTongDoanhThu(tuNgay, denNgay);
                    break;
                    
                case "Theo tháng":
                    int nam = (Integer) spnNam.getValue();
                    list = thongKeController.thongKeDoanhThuTheoThang(nam);
                    
                    Calendar cal = Calendar.getInstance();
                    cal.set(nam, 0, 1);
                    Date dauNam = cal.getTime();
                    cal.set(nam, 11, 31);
                    Date cuoiNam = cal.getTime();
                    tongKet = thongKeController.thongKeTongDoanhThu(dauNam, cuoiNam);
                    break;
                    
                case "Theo năm":
                    Date tuNgay2 = (Date) spnTuNgay.getValue();
                    Date denNgay2 = (Date) spnDenNgay.getValue();
                    if (!thongKeController.validateTimeRange(tuNgay2, denNgay2)) return;
                    
                    Calendar cal1 = Calendar.getInstance();
                    cal1.setTime(tuNgay2);
                    int namBatDau = cal1.get(Calendar.YEAR);
                    
                    cal1.setTime(denNgay2);
                    int namKetThuc = cal1.get(Calendar.YEAR);
                    
                    list = thongKeController.thongKeDoanhThuTheoNam(namBatDau, namKetThuc);
                    tongKet = thongKeController.thongKeTongDoanhThu(tuNgay2, denNgay2);
                    break;
                    
                case "Theo phương thức TT":
                    Date tuNgay3 = (Date) spnTuNgay.getValue();
                    Date denNgay3 = (Date) spnDenNgay.getValue();
                    if (!thongKeController.validateTimeRange(tuNgay3, denNgay3)) return;
                    
                    list = thongKeController.thongKeTheoPhuongThucThanhToan(tuNgay3, denNgay3);
                    tongKet = thongKeController.thongKeTongDoanhThu(tuNgay3, denNgay3);
                    break;
            }
            
            loadData(list);
            updateSummary(tongKet);
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Có lỗi xảy ra khi thống kê: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void loadData(List<domain.ThongKeDoanhThu> list) {
        model.setRowCount(0);
        DecimalFormat df = new DecimalFormat("#,###");
        
        if (list != null) {
            for (domain.ThongKeDoanhThu tk : list) {
                model.addRow(new Object[]{
                    tk.getKhoanThoiGian(),
                    df.format(tk.getTongDoanhThu()),
                    df.format(tk.getTongGiamGia()),
                    df.format(tk.getDoanhThuThucTe()),
                    tk.getSoDonHang(),
                    tk.getSoDonHangHuy()
                });
            }
        }
    }
    
    private void updateSummary(domain.ThongKeDoanhThu tongKet) {
        DecimalFormat df = new DecimalFormat("#,###");
        
        if (tongKet != null) {
            lblTongDoanhThu.setText("Tổng doanh thu: " + df.format(tongKet.getTongDoanhThu()) + " VNĐ");
            lblTongGiamGia.setText("Tổng giảm giá: " + df.format(tongKet.getTongGiamGia()) + " VNĐ");
            lblDoanhThuThucTe.setText("Doanh thu thực tế: " + df.format(tongKet.getDoanhThuThucTe()) + " VNĐ");
            lblSoDonHang.setText("Số đơn hàng: " + tongKet.getSoDonHang());
        } else {
            lblTongDoanhThu.setText("Tổng doanh thu: 0 VNĐ");
            lblTongGiamGia.setText("Tổng giảm giá: 0 VNĐ");
            lblDoanhThuThucTe.setText("Doanh thu thực tế: 0 VNĐ");
            lblSoDonHang.setText("Số đơn hàng: 0");
        }
    }
    
    private void xuatExcel() {
        // Đã loại bỏ chức năng xuất Excel
    }
    
    private void lamMoi() {
        model.setRowCount(0);
        setDefaultValues();
        updateSummary(null);
    }
}
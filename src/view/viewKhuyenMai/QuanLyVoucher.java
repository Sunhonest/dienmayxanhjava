package view.viewKhuyenMai;

import controller.KhuyenMai.VoucherController;
import controller.KhuyenMai.VoucherUIController;
import domain.Voucher;
import util.ExcelExporter;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class QuanLyVoucher extends JPanel {
    
    // Mode enum
    private enum Mode { NONE, THEM, SUA, XEM }
    private Mode currentMode = Mode.NONE;
    private int selectedVoucherId = -1;
    
    // Components
    private JTable tblVoucher;
    private DefaultTableModel model;
    
    // Input Fields
    private JTextField txtMaVoucher, txtTenVoucher, txtGiaTriGiam, txtGiamToiDa, txtDonHangToiThieu, txtSoLuong;
    private JComboBox<String> cboLoaiGiam, cboTrangThai;
    private JSpinner spnNgayBatDau, spnNgayKetThuc;
    private JTextField txtMaNV;
    
    // Buttons
    private JButton btnThem, btnSua, btnXoa, btnLamMoi, btnKichHoat, btnLuu, btnHuy;
    
    // Controller
    private VoucherController voucherController;
    
    // Colors & Fonts
    private final Color COLOR_PRIMARY = Color.decode("#2196F3");
    private final Color COLOR_SUCCESS = Color.decode("#4CAF50");
    private final Color COLOR_WARNING = Color.decode("#FF9800");
    private final Color COLOR_DANGER = Color.decode("#F44336");
    private final Color COLOR_PURPLE = Color.decode("#9C27B0");
    
    private final Font FONT_HEADER = new Font("Arial", Font.BOLD, 24);
    private final Font FONT_FORM_TITLE = new Font("Arial", Font.BOLD, 18);
    private final Font FONT_LABEL = new Font("Arial", Font.BOLD, 14);
    private final Font FONT_INPUT = new Font("Arial", Font.PLAIN, 14);
    
    public QuanLyVoucher() {
        voucherController = new VoucherController();
        initComponents();
        loadData();
        setTrangThaiNut(false);
        
        // Gắn UI Controller để xử lý sự kiện các nút bấm
        new VoucherUIController(this);
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // --- TITLE ---
        JLabel lblTitle = new JLabel("QUẢN LÝ VOUCHER KHUYẾN MÃI");
        lblTitle.setFont(FONT_HEADER);
        lblTitle.setForeground(COLOR_PRIMARY);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblTitle, BorderLayout.NORTH);
        
        // --- TABLE ---
        String[] columns = {"ID", "Mã Voucher", "Tên Voucher", "Loại", "Giá trị", "Giảm tối đa", "ĐH tối thiểu", "Ngày BD", "Ngày KT", "SL", "Trạng thái"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tblVoucher = new JTable(model);
        tblVoucher.setRowHeight(35);
        tblVoucher.setFont(new Font("Arial", Font.PLAIN, 13));
        tblVoucher.setSelectionBackground(Color.decode("#BBDEFB"));
        tblVoucher.setSelectionForeground(Color.BLACK);
        tblVoucher.setShowGrid(true);
        tblVoucher.setGridColor(Color.LIGHT_GRAY);
        
        // Hide ID column
        tblVoucher.getColumnModel().getColumn(0).setMinWidth(0);
        tblVoucher.getColumnModel().getColumn(0).setMaxWidth(0);
        
        JTableHeader header = tblVoucher.getTableHeader();
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
        
        add(new JScrollPane(tblVoucher), BorderLayout.CENTER);
        
        // Table selection listener
        tblVoucher.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedVoucher();
            }
        });
        
        // --- FORM INPUT (BÊN PHẢI) ---
        JPanel pnlInput = new JPanel();
        pnlInput.setLayout(new BorderLayout(10, 10));
        pnlInput.setBackground(Color.WHITE);
        pnlInput.setPreferredSize(new Dimension(380, 0));
        pnlInput.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.LIGHT_GRAY, 1, true),
                new EmptyBorder(15, 15, 15, 15)
        ));
        
        // 1. TIÊU ĐỀ FORM
        JLabel lblForm = new JLabel("THÔNG TIN CHI TIẾT");
        lblForm.setFont(FONT_FORM_TITLE);
        lblForm.setForeground(COLOR_PRIMARY);
        lblForm.setHorizontalAlignment(SwingConstants.CENTER);
        pnlInput.add(lblForm, BorderLayout.NORTH);
        
        // 2. FORM FIELDS
        JPanel pnlFields = new JPanel();
        pnlFields.setLayout(new BoxLayout(pnlFields, BoxLayout.Y_AXIS));
        pnlFields.setBackground(Color.WHITE);
        pnlFields.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Mã Voucher
        pnlFields.add(createSingleField("Mã Voucher:", txtMaVoucher = new JTextField()));
        txtMaVoucher.setEditable(false);
        pnlFields.add(Box.createVerticalStrut(5));
        
        // Tên Voucher
        pnlFields.add(createSingleField("Tên Voucher:", txtTenVoucher = new JTextField()));
        pnlFields.add(Box.createVerticalStrut(5));
        
        // Loại giảm
        cboLoaiGiam = new JComboBox<>(new String[]{"PHAN_TRAM", "TIEN_MAT"});
        pnlFields.add(createSingleComboField("Loại giảm:", cboLoaiGiam));
        cboLoaiGiam.addActionListener(e -> updateGiamToiDaField());
        pnlFields.add(Box.createVerticalStrut(5));
        
        // Giá trị giảm
        pnlFields.add(createSingleField("Giá trị giảm:", txtGiaTriGiam = new JTextField()));
        txtGiaTriGiam.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { updateGiamToiDaField(); }
            @Override
            public void removeUpdate(DocumentEvent e) { updateGiamToiDaField(); }
            @Override
            public void changedUpdate(DocumentEvent e) { updateGiamToiDaField(); }
        });
        pnlFields.add(Box.createVerticalStrut(5));
        
        // Giảm tối đa
        pnlFields.add(createSingleField("Giảm tối đa:", txtGiamToiDa = new JTextField()));
        pnlFields.add(Box.createVerticalStrut(5));
        
        // ĐH tối thiểu
        pnlFields.add(createSingleField("ĐH tối thiểu:", txtDonHangToiThieu = new JTextField()));
        pnlFields.add(Box.createVerticalStrut(5));
        
        // Ngày bắt đầu
        spnNgayBatDau = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editor1 = new JSpinner.DateEditor(spnNgayBatDau, "dd/MM/yyyy HH:mm");
        spnNgayBatDau.setEditor(editor1);
        pnlFields.add(createSingleSpinnerField("Ngày bắt đầu:", spnNgayBatDau));
        pnlFields.add(Box.createVerticalStrut(5));
        
        // Ngày kết thúc
        spnNgayKetThuc = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editor2 = new JSpinner.DateEditor(spnNgayKetThuc, "dd/MM/yyyy HH:mm");
        spnNgayKetThuc.setEditor(editor2);
        pnlFields.add(createSingleSpinnerField("Ngày kết thúc:", spnNgayKetThuc));
        pnlFields.add(Box.createVerticalStrut(5));
        
        // Số lượng
        pnlFields.add(createSingleField("Số lượng:", txtSoLuong = new JTextField()));
        pnlFields.add(Box.createVerticalStrut(5));
        
        // Trạng thái
        cboTrangThai = new JComboBox<>(new String[]{"NHAP", "KICH_HOAT", "NGUNG"});
        pnlFields.add(createSingleComboField("Trạng thái:", cboTrangThai));
        pnlFields.add(Box.createVerticalStrut(5));
        
        // Mã NV tạo
        pnlFields.add(createSingleField("Mã NV tạo:", txtMaNV = new JTextField()));
        txtMaNV.setEditable(false);
        
        // Scroll pane cho fields
        JScrollPane scrollFields = new JScrollPane(pnlFields);
        scrollFields.setBackground(Color.WHITE);
        scrollFields.getViewport().setBackground(Color.WHITE);
        scrollFields.setBorder(BorderFactory.createEmptyBorder());
        pnlInput.add(scrollFields, BorderLayout.CENTER);
        
        // 3. NÚT BẤM
        JPanel pnlBtnWrapper = new JPanel();
        pnlBtnWrapper.setLayout(new BoxLayout(pnlBtnWrapper, BoxLayout.Y_AXIS));
        pnlBtnWrapper.setBackground(Color.WHITE);
        pnlBtnWrapper.setBorder(BorderFactory.createEmptyBorder(15, 10, 10, 10));
        
        JPanel pnlBtnRow1 = new JPanel(new GridLayout(1, 4, 5, 5));
        pnlBtnRow1.setBackground(Color.WHITE);
        
        btnThem = createCompactButton("Them", "Thêm", COLOR_SUCCESS);
        btnSua = createCompactButton("Sua", "Sửa", COLOR_WARNING);
        btnXoa = createCompactButton("Xoa", "Xóa", COLOR_DANGER);
        btnLamMoi = createCompactButton("LamMoi", "Làm mới", COLOR_PRIMARY);
        
        pnlBtnRow1.add(btnThem);
        pnlBtnRow1.add(btnSua);
        pnlBtnRow1.add(btnXoa);
        pnlBtnRow1.add(btnLamMoi);
        pnlBtnWrapper.add(pnlBtnRow1);
        pnlBtnWrapper.add(Box.createVerticalStrut(5));
        
        JPanel pnlBtnRow2 = new JPanel(new GridLayout(1, 3, 5, 5));
        pnlBtnRow2.setBackground(Color.WHITE);
        
        btnKichHoat = createCompactButton("KichHoat", "Kích hoạt", COLOR_PURPLE);
        btnLuu = createCompactButton("Luu", "Lưu", Color.decode("#8BC34A"));
        btnHuy = createCompactButton("Huy", "Hủy", Color.decode("#9E9E9E"));
        
        pnlBtnRow2.add(btnKichHoat);
        pnlBtnRow2.add(btnLuu);
        pnlBtnRow2.add(btnHuy);
        pnlBtnWrapper.add(pnlBtnRow2);
        pnlInput.add(pnlBtnWrapper, BorderLayout.SOUTH);
        
        add(pnlInput, BorderLayout.EAST);
    }
    
    // === HELPER UI ===
    private JPanel createSingleField(String labelText, JTextField field) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("Arial", Font.BOLD, 12));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lbl);
        panel.add(Box.createVerticalStrut(2));
        
        field.setFont(new Font("Arial", Font.PLAIN, 12));
        field.setPreferredSize(new Dimension(300, 22));
        field.setMaximumSize(new Dimension(300, 22));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(field);
        
        return panel;
    }
    
    private JPanel createSingleComboField(String labelText, JComboBox<String> combo) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("Arial", Font.BOLD, 12));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lbl);
        panel.add(Box.createVerticalStrut(2));
        
        combo.setFont(new Font("Arial", Font.PLAIN, 12));
        combo.setPreferredSize(new Dimension(300, 22));
        combo.setMaximumSize(new Dimension(300, 22));
        combo.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(combo);
        
        return panel;
    }
    
    private JPanel createSingleSpinnerField(String labelText, JSpinner spinner) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("Arial", Font.BOLD, 12));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lbl);
        panel.add(Box.createVerticalStrut(2));
        
        spinner.setFont(new Font("Arial", Font.PLAIN, 12));
        spinner.setPreferredSize(new Dimension(300, 22));
        spinner.setMaximumSize(new Dimension(300, 22));
        spinner.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(spinner);
        
        return panel;
    }
    
    private JButton createCompactButton(String command, String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setActionCommand(command);
        btn.setFont(new Font("Arial", Font.BOLD, 10));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(0, 24));
        return btn;
    }
    
    private void xuatExcel() {
        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Không có dữ liệu để xuất Excel!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            String[] summaryInfo = {
                "Tổng số voucher: " + model.getRowCount(),
                "Ngày xuất: " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date())
            };
            
            ExcelExporter.exportTableToExcel(tblVoucher, "DANH SÁCH VOUCHER KHUYẾN MÃI", "DanhSachVoucher", summaryInfo);
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi xuất Excel: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(FONT_LABEL);
        lbl.setForeground(Color.BLACK);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }
    
    // === BUTTON STATE MANAGEMENT ===
    public void setTrangThaiNut(boolean dangChonHang) {
        boolean isInEditMode = (currentMode == Mode.THEM || currentMode == Mode.SUA);
        
        // Nút điều khiển chế độ (chỉ hiện khi KHÔNG ở chế độ edit)
        btnThem.setEnabled(!isInEditMode);
        btnSua.setEnabled(dangChonHang && !isInEditMode);
        btnXoa.setEnabled(dangChonHang && !isInEditMode);
        btnLamMoi.setEnabled(!isInEditMode);
        btnKichHoat.setEnabled(dangChonHang && !isInEditMode);
        
        // Nút Lưu & Hủy (chỉ hiện khi đang edit)
        btnLuu.setEnabled(isInEditMode);
        btnHuy.setEnabled(isInEditMode);
        
        // Cập nhật màu sắc
        updateButtonColors();
    }
    
    private void updateButtonColors() {
        btnThem.setBackground(btnThem.isEnabled() ? COLOR_SUCCESS : Color.LIGHT_GRAY);
        btnSua.setBackground(btnSua.isEnabled() ? COLOR_WARNING : Color.LIGHT_GRAY);
        btnXoa.setBackground(btnXoa.isEnabled() ? COLOR_DANGER : Color.LIGHT_GRAY);
        btnKichHoat.setBackground(btnKichHoat.isEnabled() ? COLOR_PURPLE : Color.LIGHT_GRAY);
        btnLuu.setBackground(btnLuu.isEnabled() ? Color.decode("#8BC34A") : Color.LIGHT_GRAY);
        btnHuy.setBackground(btnHuy.isEnabled() ? Color.decode("#9E9E9E") : Color.LIGHT_GRAY);
    }
    
    private void setInputFieldsEnabled(boolean enabled) {
        txtMaVoucher.setEnabled(enabled);
        txtTenVoucher.setEnabled(enabled);
        cboLoaiGiam.setEnabled(enabled);
        txtGiaTriGiam.setEnabled(enabled);
        txtGiamToiDa.setEnabled(enabled);
        txtDonHangToiThieu.setEnabled(enabled);
        spnNgayBatDau.setEnabled(enabled);
        spnNgayKetThuc.setEnabled(enabled);
        txtSoLuong.setEnabled(enabled);
        cboTrangThai.setEnabled(enabled);
        txtMaNV.setEnabled(enabled);
    }
    
    private void setMaVoucherEditable(boolean editable) {
        txtMaVoucher.setEnabled(editable);
    }
    
    private String generateMaVoucher() {
        String prefix = "V";
        long timestamp = System.currentTimeMillis();
        String maNV = txtMaNV.getText().trim();
        
        // Format: V + NhanVienID + Timestamp (last 6 digits)
        // Ví dụ: VNV001111111
        String suffix = String.valueOf(timestamp % 1000000);
        return prefix + (maNV.isEmpty() ? "NV001" : maNV) + suffix;
    }
    
    // === UPDATE GIẢM TỐI ĐA FIELD LOGIC ===
    private void updateGiamToiDaField() {
        String loaiGiam = (String) cboLoaiGiam.getSelectedItem();
        
        if ("PHAN_TRAM".equals(loaiGiam)) {
            // Phần trăm: cho phép nhập giảm tối đa
            txtGiamToiDa.setEnabled(true);
            txtGiamToiDa.setText("");
        } else if ("TIEN_MAT".equals(loaiGiam)) {
            // Tiền mặt: vô hiệu hóa, auto-fill = giá trị giảm
            txtGiamToiDa.setEnabled(false);
            if (!txtGiaTriGiam.getText().trim().isEmpty()) {
                txtGiamToiDa.setText(txtGiaTriGiam.getText());
            } else {
                txtGiamToiDa.setText("");
            }
        }
    }
    
    // === API ===
    public void addActionListener(ActionListener ac) {
        btnThem.addActionListener(ac);
        btnSua.addActionListener(ac);
        btnXoa.addActionListener(ac);
        btnLamMoi.addActionListener(ac);
        btnKichHoat.addActionListener(ac);
        btnLuu.addActionListener(ac);
        btnHuy.addActionListener(ac);
    }
    
    public void loadData() {
        model.setRowCount(0);
        List<Voucher> list = voucherController.getAllVouchers();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        
        for (Voucher v : list) {
            model.addRow(new Object[]{
                v.getVoucherID(),
                v.getMaVoucher(),
                v.getTenVoucher(),
                v.getLoaiGiam(),
                String.format("%.0f", v.getGiaTriGiam()),
                String.format("%.0f", v.getGiamToiDa()),
                String.format("%.0f", v.getDonHangToiThieu()),
                sdf.format(v.getNgayBatDau()),
                sdf.format(v.getNgayKetThuc()),
                v.getSoLuong(),
                v.getTrangThai()
            });
        }
    }
    
    private void loadSelectedVoucher() {
        int row = tblVoucher.getSelectedRow();
        if (row >= 0) {
            selectedVoucherId = (Integer) model.getValueAt(row, 0);
            Voucher voucher = voucherController.getVoucherById(selectedVoucherId);
            
            if (voucher != null) {
                txtMaVoucher.setText(voucher.getMaVoucher());
                txtTenVoucher.setText(voucher.getTenVoucher());
                cboLoaiGiam.setSelectedItem(voucher.getLoaiGiam());
                txtGiaTriGiam.setText(String.valueOf(voucher.getGiaTriGiam()));
                txtGiamToiDa.setText(String.valueOf(voucher.getGiamToiDa()));
                txtDonHangToiThieu.setText(String.valueOf(voucher.getDonHangToiThieu()));
                spnNgayBatDau.setValue(voucher.getNgayBatDau());
                spnNgayKetThuc.setValue(voucher.getNgayKetThuc());
                txtSoLuong.setText(String.valueOf(voucher.getSoLuong()));
                cboTrangThai.setSelectedItem(voucher.getTrangThai());
                txtMaNV.setText(voucher.getMaNV_Tao());
                
                // Set mode to XEM (view mode) khi chọn
                currentMode = Mode.XEM;
                setInputFieldsEnabled(false);
                setTrangThaiNut(true);
            }
        }
    }
    
    public void resetForm() {
        txtMaVoucher.setText("");
        txtTenVoucher.setText("");
        txtGiaTriGiam.setText("");
        txtGiamToiDa.setText("");
        txtDonHangToiThieu.setText("");
        spnNgayBatDau.setValue(new Date());
        spnNgayKetThuc.setValue(new Date());
        txtSoLuong.setText("");
        cboLoaiGiam.setSelectedIndex(0);
        cboTrangThai.setSelectedIndex(0);
        txtMaNV.setText("NV001");
        
        tblVoucher.clearSelection();
        selectedVoucherId = -1;
        currentMode = Mode.NONE;
        setInputFieldsEnabled(false);
        setTrangThaiNut(false);
        updateGiamToiDaField();
    }
    
    public void enterThemMode() {
        resetForm();  // Gọi resetForm TRƯỚC
        
        // SET MODE CUỐI CÙNG (sau khi resetForm)
        currentMode = Mode.THEM;
        
        // Tự động generate mã voucher
        txtMaVoucher.setText(generateMaVoucher());
        setInputFieldsEnabled(true);
        txtMaVoucher.setEnabled(false);  // Không cho chỉnh sửa mã
        txtMaNV.setEnabled(false);       // Không cho chỉnh sửa mã NV
        tblVoucher.clearSelection();
        updateGiamToiDaField();
        
        // Enable nút Lưu & Hủy, disable các nút khác
        btnThem.setEnabled(false);
        btnSua.setEnabled(false);
        btnXoa.setEnabled(false);
        btnLamMoi.setEnabled(false);
        btnKichHoat.setEnabled(false);
        btnLuu.setEnabled(true);
        btnHuy.setEnabled(true);
        updateButtonColors();
        
        txtTenVoucher.requestFocus();
    }
    
    public void enterSuaMode() {
        if (selectedVoucherId <= 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn voucher để sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // SET MODE TRƯỚC
        currentMode = Mode.SUA;
        
        setInputFieldsEnabled(true);
        txtMaVoucher.setEnabled(false);  // Không cho sửa mã voucher
        txtMaNV.setEnabled(false);       // Không cho sửa mã NV
        updateGiamToiDaField();
        
        // Enable nút Lưu & Hủy, disable các nút khác
        btnThem.setEnabled(false);
        btnSua.setEnabled(false);
        btnXoa.setEnabled(false);
        btnLamMoi.setEnabled(false);
        btnKichHoat.setEnabled(false);
        btnLuu.setEnabled(true);
        btnHuy.setEnabled(true);
        updateButtonColors();
        
        txtTenVoucher.requestFocus();
    }
    
    public void cancelEdit() {
        if (selectedVoucherId > 0) {
            loadSelectedVoucher();  // Load lại dữ liệu cũ
        } else {
            resetForm();
        }
        // Refresh button states
        setTrangThaiNut(selectedVoucherId > 0);
    }
    
    public void luu() {
        try {
            System.out.println("=== LƯU VOUCHER ===");
            System.out.println("Mode: " + currentMode);
            
            // Validate dữ liệu
            if (!validateInput()) {
                System.out.println("Validate thất bại!");
                return;
            }
            
            System.out.println("Validate thành công, đang lấy dữ liệu input...");
            Voucher voucher = getVoucherFromInput();
            System.out.println("Mã: " + voucher.getMaVoucher() + ", Tên: " + voucher.getTenVoucher());
            
            if (currentMode == Mode.THEM) {
                // Thêm mới
                System.out.println("Gọi addVoucher...");
                boolean success = voucherController.addVoucher(voucher);
                System.out.println("Result: " + success);
                
                if (success) {
                    JOptionPane.showMessageDialog(this, "Thêm voucher thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    loadData();
                    resetForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Thêm voucher thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } else if (currentMode == Mode.SUA) {
                // Sửa
                System.out.println("Gọi updateVoucher...");
                voucher.setVoucherID(selectedVoucherId);
                boolean success = voucherController.updateVoucher(voucher);
                System.out.println("Result: " + success);
                
                if (success) {
                    JOptionPane.showMessageDialog(this, "Cập nhật voucher thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    loadData();
                    resetForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Cập nhật voucher thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                System.out.println("Mode không hợp lệ: " + currentMode);
                JOptionPane.showMessageDialog(this, "Mode không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            System.err.println("NumberFormatException: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Dữ liệu nhập không hợp lệ! Vui lòng kiểm tra lại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void xoa() {
        if (selectedVoucherId <= 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn voucher để xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Bạn có chắc chắn muốn xóa voucher này?", 
            "Xác nhận xóa", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = voucherController.deleteVoucher(selectedVoucherId);
            if (success) {
                JOptionPane.showMessageDialog(this, "Xóa voucher thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                loadData();
                resetForm();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa voucher thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public void kichHoat() {
        if (selectedVoucherId <= 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn voucher để kích hoạt!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Voucher voucher = voucherController.getVoucherById(selectedVoucherId);
        if (voucher != null) {
            // Chuyển trạng thái
            String trangThaiHienTai = voucher.getTrangThai();
            String trangThaiMoi;
            
            if ("KICH_HOAT".equals(trangThaiHienTai)) {
                trangThaiMoi = "NGUNG";
            } else {
                trangThaiMoi = "KICH_HOAT";
            }
            
            voucher.setTrangThai(trangThaiMoi);
            boolean success = voucherController.updateVoucher(voucher);
            
            if (success) {
                JOptionPane.showMessageDialog(this, "Cập nhật trạng thái thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                loadData();
                resetForm();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật trạng thái thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private boolean validateInput() {
        if (txtMaVoucher.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã voucher không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (txtTenVoucher.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên voucher không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (txtGiaTriGiam.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Giá trị giảm không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (txtGiamToiDa.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Giảm tối đa không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (txtDonHangToiThieu.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Đơn hàng tối thiểu không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (txtSoLuong.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Số lượng không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
    
    public Voucher getVoucherFromInput() {
        Voucher voucher = new Voucher();
        voucher.setMaVoucher(txtMaVoucher.getText().trim());
        voucher.setTenVoucher(txtTenVoucher.getText().trim());
        voucher.setLoaiGiam((String) cboLoaiGiam.getSelectedItem());
        voucher.setGiaTriGiam(Double.parseDouble(txtGiaTriGiam.getText().trim()));
        voucher.setGiamToiDa(Double.parseDouble(txtGiamToiDa.getText().trim()));
        voucher.setDonHangToiThieu(Double.parseDouble(txtDonHangToiThieu.getText().trim()));
        voucher.setNgayBatDau((Date) spnNgayBatDau.getValue());
        voucher.setNgayKetThuc((Date) spnNgayKetThuc.getValue());
        voucher.setSoLuong(Integer.parseInt(txtSoLuong.getText().trim()));
        voucher.setTrangThai((String) cboTrangThai.getSelectedItem());
        voucher.setMaNV_Tao(txtMaNV.getText().trim());
        return voucher;
    }
    
    public Mode getCurrentMode() {
        return currentMode;
    }
    
    public int getSelectedVoucherId() {
        return selectedVoucherId;
    }
    
    public DefaultTableModel getModel() {
        return model;
    }
    
    public JTable getTable() {
        return tblVoucher;
    }
}

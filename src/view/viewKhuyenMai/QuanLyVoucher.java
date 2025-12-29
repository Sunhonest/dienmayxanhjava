package view.viewKhuyenMai;

import controller.KhuyenMai.VoucherController;
import controller.KhuyenMai.VoucherUIController;
import domain.Voucher;

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

/**
 * Panel quản lý voucher khuyến mãi - Redesigned with modern layout
 * @author nguye
 */
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
        pnlInput.setPreferredSize(new Dimension(550, 0));
        pnlInput.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.LIGHT_GRAY, 1, true),
                new EmptyBorder(20, 20, 20, 20)
        ));
        
        // 1. TIÊU ĐỀ FORM
        JLabel lblForm = new JLabel("THÔNG TIN CHI TIẾT");
        lblForm.setFont(FONT_FORM_TITLE);
        lblForm.setForeground(COLOR_PRIMARY);
        lblForm.setHorizontalAlignment(SwingConstants.CENTER);
        pnlInput.add(lblForm, BorderLayout.NORTH);
        
        // 2. FORM FIELDS (2 CỘT)
        JPanel pnlFields = new JPanel();
        pnlFields.setLayout(new GridLayout(0, 2, 15, 12));
        pnlFields.setBackground(Color.WHITE);
        
        // Cột 1 & 2: Mã & Tên Voucher
        pnlFields.add(createLabeledField("Mã Voucher:", txtMaVoucher = new JTextField()));
        txtMaVoucher.setEditable(false);  // Khóa mã voucher - tự generate
        pnlFields.add(createLabeledField("Tên Voucher:", txtTenVoucher = new JTextField()));
        
        // Cột 1 & 2: Loại giảm & Giá trị giảm
        JPanel pnlLoaiGiam = new JPanel(new BorderLayout());
        pnlLoaiGiam.setBackground(Color.WHITE);
        pnlLoaiGiam.add(createLabel("Loại giảm:"), BorderLayout.NORTH);
        cboLoaiGiam = new JComboBox<>(new String[]{"PHAN_TRAM", "TIEN_MAT"});
        styleComponent(cboLoaiGiam);
        cboLoaiGiam.addActionListener(e -> updateGiamToiDaField());
        pnlLoaiGiam.add(cboLoaiGiam, BorderLayout.CENTER);
        pnlFields.add(pnlLoaiGiam);
        
        pnlFields.add(createLabeledField("Giá trị giảm:", txtGiaTriGiam = new JTextField()));
        txtGiaTriGiam.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { updateGiamToiDaField(); }
            @Override
            public void removeUpdate(DocumentEvent e) { updateGiamToiDaField(); }
            @Override
            public void changedUpdate(DocumentEvent e) { updateGiamToiDaField(); }
        });
        
        // Cột 1 & 2: Giảm tối đa & ĐH tối thiểu
        pnlFields.add(createLabeledField("Giảm tối đa:", txtGiamToiDa = new JTextField()));
        pnlFields.add(createLabeledField("ĐH tối thiểu:", txtDonHangToiThieu = new JTextField()));
        
        // Cột 1 & 2: Ngày bắt đầu & Ngày kết thúc
        JPanel pnlNgayBD = new JPanel(new BorderLayout());
        pnlNgayBD.setBackground(Color.WHITE);
        pnlNgayBD.add(createLabel("Ngày bắt đầu:"), BorderLayout.NORTH);
        spnNgayBatDau = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editor1 = new JSpinner.DateEditor(spnNgayBatDau, "dd/MM/yyyy HH:mm");
        spnNgayBatDau.setEditor(editor1);
        styleComponent(spnNgayBatDau);
        pnlNgayBD.add(spnNgayBatDau, BorderLayout.CENTER);
        pnlFields.add(pnlNgayBD);
        
        JPanel pnlNgayKT = new JPanel(new BorderLayout());
        pnlNgayKT.setBackground(Color.WHITE);
        pnlNgayKT.add(createLabel("Ngày kết thúc:"), BorderLayout.NORTH);
        spnNgayKetThuc = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editor2 = new JSpinner.DateEditor(spnNgayKetThuc, "dd/MM/yyyy HH:mm");
        spnNgayKetThuc.setEditor(editor2);
        styleComponent(spnNgayKetThuc);
        pnlNgayKT.add(spnNgayKetThuc, BorderLayout.CENTER);
        pnlFields.add(pnlNgayKT);
        
        // Cột 1 & 2: Số lượng & Trạng thái
        pnlFields.add(createLabeledField("Số lượng:", txtSoLuong = new JTextField()));
        
        JPanel pnlTrangThai = new JPanel(new BorderLayout());
        pnlTrangThai.setBackground(Color.WHITE);
        pnlTrangThai.add(createLabel("Trạng thái:"), BorderLayout.NORTH);
        cboTrangThai = new JComboBox<>(new String[]{"NHAP", "KICH_HOAT", "NGUNG"});
        styleComponent(cboTrangThai);
        pnlTrangThai.add(cboTrangThai, BorderLayout.CENTER);
        pnlFields.add(pnlTrangThai);
        
        // Cột 1 (đầy đủ chiều rộng): Mã NV tạo
        pnlFields.add(createLabeledField("Mã NV tạo:", txtMaNV = new JTextField()));
        txtMaNV.setEditable(false);  // Khóa mã NV tạo
        
        // Scroll pane cho fields
        JScrollPane scrollFields = new JScrollPane(pnlFields);
        scrollFields.setBackground(Color.WHITE);
        scrollFields.getViewport().setBackground(Color.WHITE);
        scrollFields.setBorder(BorderFactory.createEmptyBorder());
        pnlInput.add(scrollFields, BorderLayout.CENTER);
        
        // 3. NÚT BẤM (dưới cùng)
        JPanel pnlBtnWrapper = new JPanel(new BorderLayout());
        pnlBtnWrapper.setBackground(Color.WHITE);
        pnlBtnWrapper.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        JPanel pnlGridButtons = new JPanel(new GridLayout(2, 4, 8, 8));
        pnlGridButtons.setBackground(Color.WHITE);
        
        btnThem = createButton("Them", "Thêm", COLOR_SUCCESS);
        btnSua = createButton("Sua", "Sửa", COLOR_WARNING);
        btnXoa = createButton("Xoa", "Xóa", COLOR_DANGER);
        btnLamMoi = createButton("LamMoi", "Làm mới", COLOR_PRIMARY);
        btnKichHoat = createButton("KichHoat", "Kích hoạt", COLOR_PURPLE);
        btnLuu = createButton("Luu", "Lưu", Color.decode("#8BC34A"));
        btnHuy = createButton("Huy", "Hủy", Color.decode("#9E9E9E"));
        
        pnlGridButtons.add(btnThem);
        pnlGridButtons.add(btnSua);
        pnlGridButtons.add(btnXoa);
        pnlGridButtons.add(btnLamMoi);
        pnlGridButtons.add(btnKichHoat);
        pnlGridButtons.add(btnLuu);
        pnlGridButtons.add(btnHuy);
        pnlGridButtons.add(new JPanel()); // Empty cell
        
        pnlBtnWrapper.add(pnlGridButtons, BorderLayout.CENTER);
        pnlInput.add(pnlBtnWrapper, BorderLayout.SOUTH);
        
        add(pnlInput, BorderLayout.EAST);
    }
    
    // === HELPER UI ===
    private JPanel createLabeledField(String labelText, JTextField field) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        JLabel lbl = createLabel(labelText);
        panel.add(lbl, BorderLayout.NORTH);
        
        styleComponent(field);
        field.setMaximumSize(null);
        panel.add(field, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JTextField createField(JPanel panel, String labelText) {
        panel.add(createLabel(labelText));
        JTextField txt = new JTextField();
        styleComponent(txt);
        panel.add(txt);
        panel.add(Box.createVerticalStrut(15));
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
        comp.setFont(FONT_INPUT);
        comp.setBackground(Color.WHITE);
        comp.setForeground(Color.BLACK);
        // Giảm chiều cao input xuống 28px thay vì 35px
        comp.setPreferredSize(new Dimension(0, 28));
        comp.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        comp.setMinimumSize(new Dimension(0, 28));
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

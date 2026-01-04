/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view.viewQLBH;

import domain.QLBH.QLDH;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 *
 * @author Admin
 */
public class QuanLyDonHang extends JPanel {

    private JTable tblDonHang;
    private DefaultTableModel model;

    // Search
    private JTextField txtTimKiem;
    private JButton btnTim;

    // Fields
    private JTextField txtID;                // ID auto tăng (disable)
    private JComboBox<String> cboMaDonHang;  // ✅ MaDonHang lấy từ CTDH
    private JComboBox<String> cboMaKH, cboMaNV, cboVoucherID;
    private JSpinner spNgayTao;

    // DB: ThanhTien, TienGiam, GiaThanhToan
    private JFormattedTextField txtThanhTien, txtTienGiam, txtGiaThanhToan;

    private JComboBox<String> cboTrangThai;

    // Buttons
    private JButton btnThem, btnSua, btnXoa, btnLamMoi, btnNhapExcel, btnXuatExcel;

    public QuanLyDonHang() {
        setLayout(new BorderLayout());

        JPanel root = new JPanel(new BorderLayout(0, 10));
        root.setBorder(new EmptyBorder(12, 12, 12, 12));

        JLabel lblTitle = new JLabel("QUẢN LÝ ĐƠN HÀNG", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(new Color(0, 140, 220));
        root.add(lblTitle, BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                createTablePanel(),
                createDetailPanel()
        );
        splitPane.setResizeWeight(0.70);
        splitPane.setDividerSize(6);

        root.add(splitPane, BorderLayout.CENTER);
        add(root, BorderLayout.CENTER);

        setButtonStateDefault();
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));

        JPanel top = new JPanel(new BorderLayout(8, 8));
        txtTimKiem = new JTextField();
        btnTim = new JButton("Tìm kiếm");
        btnTim.setActionCommand("Tim");

        top.add(new JLabel("Từ khóa:"), BorderLayout.WEST);
        top.add(txtTimKiem, BorderLayout.CENTER);
        top.add(btnTim, BorderLayout.EAST);

        model = new DefaultTableModel(
                new Object[]{
                        "ID", "Mã khách hàng", "Mã đơn hàng", "Ngày tạo",
                        "Thành tiền", "Tiền giảm", "Giá thanh toán",
                        "Voucher ID", "Trạng thái", "Mã nhân viên"
                }, 0
        ) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
            @Override public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 3) return Date.class;
                return Object.class;
            }
        };

        tblDonHang = new JTable(model);

        tblDonHang.setRowHeight(28);
        tblDonHang.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tblDonHang.setSelectionBackground(new Color(187, 222, 251));
        tblDonHang.setSelectionForeground(Color.BLACK);

        tblDonHang.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            private final java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            @Override protected void setValue(Object value) {
                if (value instanceof Date) setText(sdf.format((Date) value));
                else setText(value == null ? "" : value.toString());
            }
        });

        JTableHeader header = tblDonHang.getTableHeader();
        header.setBackground(new Color(33, 150, 243));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setPreferredSize(new Dimension(header.getWidth(), 35));
        header.setOpaque(false);
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                          boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = new JLabel(value == null ? "" : value.toString());
                label.setBackground(new Color(33, 150, 243));
                label.setForeground(Color.WHITE);
                label.setFont(new Font("Segoe UI", Font.BOLD, 14));
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setOpaque(true);
                label.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.WHITE));
                return label;
            }
        });

        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(tblDonHang), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createDetailPanel() {
        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setBorder(BorderFactory.createLineBorder(new Color(210, 210, 210)));

        JLabel lbl = new JLabel("THÔNG TIN CHI TIẾT", SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lbl.setForeground(new Color(0, 140, 220));
        lbl.setBorder(new EmptyBorder(12, 10, 12, 10));
        wrap.add(lbl, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(new EmptyBorder(10, 14, 10, 14));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 0, 8, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtID = new JTextField();
        txtID.setEnabled(false);

        cboMaDonHang = new JComboBox<>(); // ✅ lấy từ CTDH (controller fill)
        cboMaKH = new JComboBox<>();
        cboMaNV = new JComboBox<>();
        cboVoucherID = new JComboBox<>();
        cboVoucherID.addItem("0");

        spNgayTao = new JSpinner(new SpinnerDateModel(new Date(), null, null, Calendar.MINUTE));
        spNgayTao.setEditor(new JSpinner.DateEditor(spNgayTao, "yyyy-MM-dd HH:mm:ss"));

        NumberFormat nf = new DecimalFormat("#,##0.00");
        txtThanhTien = new JFormattedTextField(nf);
        txtTienGiam = new JFormattedTextField(nf);
        txtGiaThanhToan = new JFormattedTextField(nf);

        txtThanhTien.setValue(0.00);
        txtTienGiam.setValue(0.00);
        txtGiaThanhToan.setValue(0.00);

        // ✅ tự tính, không cho sửa
        txtThanhTien.setEditable(false);
        txtTienGiam.setEditable(false);
        txtGiaThanhToan.setEditable(false);

        cboTrangThai = new JComboBox<>(new String[]{"MOI_TAO", "DA_XAC_NHAN", "HUY"});

        int r = 0;
        r = addRow(form, gbc, r, "ID:", txtID);
        r = addRow(form, gbc, r, "Mã đơn hàng:", cboMaDonHang);
        r = addRow(form, gbc, r, "Mã KH:", cboMaKH);
        r = addRow(form, gbc, r, "Ngày tạo:", spNgayTao);
        r = addRow(form, gbc, r, "Thành tiền:", txtThanhTien);
        r = addRow(form, gbc, r, "Tiền giảm:", txtTienGiam);
        r = addRow(form, gbc, r, "Giá thanh toán:", txtGiaThanhToan);
        r = addRow(form, gbc, r, "VoucherID:", cboVoucherID);
        r = addRow(form, gbc, r, "Trạng thái:", cboTrangThai);
        r = addRow(form, gbc, r, "Mã NV:", cboMaNV);

        JScrollPane sp = new JScrollPane(form);
        sp.setBorder(null);
        sp.getVerticalScrollBar().setUnitIncrement(16);
        wrap.add(sp, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        btnPanel.setBorder(new EmptyBorder(10, 14, 14, 14));

        btnThem = new JButton("Thêm");
        btnSua = new JButton("Sửa");
        btnXoa = new JButton("Xóa");
        btnLamMoi = new JButton("Làm mới");
        btnNhapExcel = new JButton("Nhập Excel");
        btnXuatExcel = new JButton("Xuất Excel");

        stylePrimaryGreen(btnThem);
        stylePrimaryBlue(btnLamMoi);
        styleGray(btnSua);
        styleGray(btnXoa);
        stylePrimaryBlue(btnNhapExcel);
        stylePrimaryBlue(btnXuatExcel);

        btnThem.setActionCommand("Them");
        btnSua.setActionCommand("Sua");
        btnXoa.setActionCommand("Xoa");
        btnLamMoi.setActionCommand("LamMoi");
        btnNhapExcel.setActionCommand("NhapExcel");
        btnXuatExcel.setActionCommand("XuatExcel");

        btnPanel.add(btnThem);
        btnPanel.add(btnSua);
        btnPanel.add(btnXoa);
        btnPanel.add(btnLamMoi);
        btnPanel.add(btnNhapExcel);
        btnPanel.add(btnXuatExcel);

        wrap.add(btnPanel, BorderLayout.SOUTH);
        return wrap;
    }

    private int addRow(JPanel form, GridBagConstraints gbc, int row, String label, JComponent field) {
        JLabel lb = new JLabel(label);
        lb.setFont(new Font("Segoe UI", Font.BOLD, 13));

        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 1;
        form.add(lb, gbc);

        gbc.gridx = 0; gbc.gridy = row + 1; gbc.weightx = 1;
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setPreferredSize(new Dimension(0, 30));
        form.add(field, gbc);

        return row + 2;
    }

    // ================= API cho Controller =================
    public void addActionListener(ActionListener al) {
        btnThem.addActionListener(al);
        btnSua.addActionListener(al);
        btnXoa.addActionListener(al);
        btnLamMoi.addActionListener(al);
        btnTim.addActionListener(al);
        btnNhapExcel.addActionListener(al);
        btnXuatExcel.addActionListener(al);
    }

    public JTable getTable() { return tblDonHang; }
    public DefaultTableModel getModel() { return model; }
    public String getKeyword() { return txtTimKiem.getText().trim(); }

    public JComboBox<String> getCboMaDonHang() { return cboMaDonHang; }
    public JComboBox<String> getCboMaKH() { return cboMaKH; }
    public JComboBox<String> getCboMaNV() { return cboMaNV; }
    public JComboBox<String> getCboVoucherID() { return cboVoucherID; }
    public JComboBox<String> getCboTrangThai() { return cboTrangThai; }

    public void setID(int id) { txtID.setText(id <= 0 ? "" : String.valueOf(id)); }

    public void setThanhTien(double v) {
        txtThanhTien.setValue(v);
        capNhatGiaThanhToan();
    }

    public void setTienGiam(double v) {
        txtTienGiam.setValue(v);
        capNhatGiaThanhToan();
    }

    public double getThanhTienValue() {
        try { return Double.parseDouble(normalizeMoney(txtThanhTien.getText())); }
        catch (Exception e) { return 0.0; }
    }

    public void capNhatGiaThanhToan() {
        double thanhTien = getThanhTienValue();
        double tienGiam = 0.0;
        try { tienGiam = Double.parseDouble(normalizeMoney(txtTienGiam.getText())); }
        catch (Exception ignored) {}

        double gtt = Math.max(0, thanhTien - tienGiam);
        txtGiaThanhToan.setValue(gtt);
    }

    public String getMaDonHangDangChon() {
        int row = tblDonHang.getSelectedRow();
        if (row < 0) return null;
        return valueAt(row, 2);
    }

    public void fillFormTuBang() {
        int row = tblDonHang.getSelectedRow();
        if (row < 0) return;

        txtID.setText(valueAt(row, 0));
        cboMaKH.setSelectedItem(valueAt(row, 1));
        cboMaDonHang.setSelectedItem(valueAt(row, 2));

        Object oNgay = model.getValueAt(row, 3);
        if (oNgay instanceof Date) spNgayTao.setValue((Date) oNgay);
        else spNgayTao.setValue(new Date());

        // ✅ dùng setValue để không bị lỗi format / mất giá trị
        txtThanhTien.setValue(Double.parseDouble(normalizeMoney(valueAt(row, 4))));
        txtTienGiam.setValue(Double.parseDouble(normalizeMoney(valueAt(row, 5))));
        txtGiaThanhToan.setValue(Double.parseDouble(normalizeMoney(valueAt(row, 6))));

        String v = valueAt(row, 7);
        cboVoucherID.setSelectedItem((v == null || v.isBlank()) ? "0" : v);

        cboTrangThai.setSelectedItem(valueAt(row, 8));
        cboMaNV.setSelectedItem(valueAt(row, 9));

        btnSua.setEnabled(true);
        btnXoa.setEnabled(true);
    }

    public void resetForm() {
        txtID.setText("");

        if (cboMaDonHang.getItemCount() > 0) cboMaDonHang.setSelectedIndex(0);
        if (cboMaKH.getItemCount() > 0) cboMaKH.setSelectedIndex(0);
        if (cboMaNV.getItemCount() > 0) cboMaNV.setSelectedIndex(0);

        cboVoucherID.setSelectedItem("0");
        spNgayTao.setValue(new Date());

        txtThanhTien.setValue(0.00);
        txtTienGiam.setValue(0.00);
        txtGiaThanhToan.setValue(0.00);

        cboTrangThai.setSelectedIndex(0);

        tblDonHang.clearSelection();
        setButtonStateDefault();
    }

    public QLDH getDonHangFromInput() {
        QLDH dh = new QLDH();

        try { dh.setId(Integer.parseInt(txtID.getText().trim())); }
        catch (Exception ignored) { dh.setId(0); }

        dh.setMaDonHang(String.valueOf(cboMaDonHang.getSelectedItem()));
        dh.setMaKH(String.valueOf(cboMaKH.getSelectedItem()));
        dh.setMaNV(String.valueOf(cboMaNV.getSelectedItem()));
        dh.setNgayTao((Date) spNgayTao.getValue());

        float thanhTien = 0f;
        float tienGiam = 0f;

        try { thanhTien = Float.parseFloat(normalizeMoney(txtThanhTien.getText())); } catch (Exception ignored) {}
        try { tienGiam = Float.parseFloat(normalizeMoney(txtTienGiam.getText())); } catch (Exception ignored) {}

        dh.setThanhTien(thanhTien);
        dh.setTienGiam(tienGiam);
        dh.setGiaThanhToan(Math.max(0f, thanhTien - tienGiam));

        String v = String.valueOf(cboVoucherID.getSelectedItem());
        dh.setVoucherID((v == null || v.isBlank()) ? 0 : Integer.parseInt(v));

        dh.setTrangThai(String.valueOf(cboTrangThai.getSelectedItem()));
        return dh;
    }

    // ================= Helpers =================
    private String valueAt(int row, int col) {
        Object o = model.getValueAt(row, col);
        return o == null ? "" : o.toString();
    }

    private String normalizeMoney(String s) {
        if (s == null) return "0";
        s = s.trim().replace(",", "");
        if (s.isEmpty()) return "0";
        return s;
    }

    private void setButtonStateDefault() {
        btnSua.setEnabled(false);
        btnXoa.setEnabled(false);
    }

    // ================= Style methods =================
    private void stylePrimaryGreen(JButton b) {
        b.setBackground(new Color(76, 175, 80));
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
    }

    private void stylePrimaryBlue(JButton b) {
        b.setBackground(new Color(33, 150, 243));
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
    }

    private void styleGray(JButton b) {
        b.setBackground(new Color(200, 200, 200));
        b.setForeground(Color.BLACK);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
    }
}


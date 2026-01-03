/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view.viewQLBH;

import domain.QLBH.ChiTietDonHang;
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
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 *
 * @author Admin
 */
public class QuanLyChiTietDonHang extends JPanel {

    private JTable tblCTDH;
    private DefaultTableModel model;

    // Search
    private JTextField txtTimKiem;
    private JButton btnTim;

    // Fields
    private JTextField txtID;
    private JComboBox<String> cboMaDonHang, cboMaSP;
    private JSpinner spSoLuong;
    private JFormattedTextField txtDonGia, txtThanhTien;

    // Buttons
    private JButton btnThem, btnSua, btnXoa, btnLamMoi, btnNhapExcel, btnXuatExcel;

    public QuanLyChiTietDonHang() {
        setLayout(new BorderLayout());
        JPanel root = new JPanel(new BorderLayout(0, 10));
        root.setBorder(new EmptyBorder(12, 12, 12, 12));

        JLabel lblTitle = new JLabel("QUẢN LÝ CHI TIẾT ĐƠN HÀNG", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(new Color(0, 140, 220));
        root.add(lblTitle, BorderLayout.NORTH);

        JSplitPane split = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                createTablePanel(),
                createDetailPanel()
        );
        split.setResizeWeight(0.70);
        split.setDividerSize(6);

        root.add(split, BorderLayout.CENTER);
        add(root, BorderLayout.CENTER);

        setButtonStateDefault();

        // Tính thành tiền khi đổi số lượng
        spSoLuong.addChangeListener(e -> updateThanhTien());
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));

        // ===== TOP SEARCH BAR =====
        JPanel top = new JPanel(new BorderLayout(8, 8));
        txtTimKiem = new JTextField();
        btnTim = new JButton("Tìm kiếm");
        btnTim.setActionCommand("Tim");

        top.add(new JLabel("Từ khóa:"), BorderLayout.WEST);
        top.add(txtTimKiem, BorderLayout.CENTER);
        top.add(btnTim, BorderLayout.EAST);

        model = new DefaultTableModel(
                new Object[]{"ID", "Mã đơn hàng", "Mã SP", "Số lượng", "Đơn giá", "Thành tiền"}, 0
        ) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        tblCTDH = new JTable(model);

        // ===== STYLE TABLE =====
        tblCTDH.setRowHeight(28);
        tblCTDH.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tblCTDH.setSelectionBackground(new Color(187, 222, 251));
        tblCTDH.setSelectionForeground(Color.BLACK);

        // ===== STYLE HEADER (GIỐNG FORM BẠN) =====
        JTableHeader header = tblCTDH.getTableHeader();
        header.setBackground(new Color(33, 150, 243));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setPreferredSize(new Dimension(header.getWidth(), 35));
        header.setOpaque(false);
        header.setDefaultRenderer(new javax.swing.table.DefaultTableCellRenderer() {
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
        panel.add(new JScrollPane(tblCTDH), BorderLayout.CENTER);
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
        txtID.setEnabled(false); // ID auto_increment -> ko cho sửa

        cboMaDonHang = new JComboBox<>();
        cboMaSP = new JComboBox<>();

        spSoLuong = new JSpinner(new SpinnerNumberModel(1, 1, 9999, 1));

        NumberFormat nf = new DecimalFormat("#,##0.00");
        txtDonGia = new JFormattedTextField(nf);
        txtThanhTien = new JFormattedTextField(nf);
        txtThanhTien.setEditable(false);

        txtDonGia.setValue(0.00);
        txtThanhTien.setValue(0.00);

        int r = 0;
        r = addRow(form, gbc, r, "ID:", txtID);
        r = addRow(form, gbc, r, "Mã đơn hàng:", cboMaDonHang);
        r = addRow(form, gbc, r, "Mã SP:", cboMaSP);
        r = addRow(form, gbc, r, "Số lượng:", spSoLuong);
        r = addRow(form, gbc, r, "Đơn giá:", txtDonGia);
        r = addRow(form, gbc, r, "Thành tiền:", txtThanhTien);

        wrap.add(form, BorderLayout.CENTER);

        // ===== BUTTONS (3x2) =====
        JPanel btnPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        btnPanel.setBorder(new EmptyBorder(10, 14, 14, 14));

        btnThem = new JButton("Thêm");
        btnSua = new JButton("Sửa");
        btnXoa = new JButton("Xóa");
        btnLamMoi = new JButton("Làm mới");
        btnNhapExcel = new JButton("Nhập Excel");
        btnXuatExcel = new JButton("Xuất Excel");

        // style giống các form bạn
        stylePrimaryGreen(btnThem);
        stylePrimaryBlue(btnLamMoi);
        styleGray(btnSua);
        styleGray(btnXoa);
        stylePrimaryBlue(btnNhapExcel);
        stylePrimaryBlue(btnXuatExcel);

        // ActionCommand
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

    // ====== API cho Controller ======
    public void addActionListener(ActionListener al) {
        btnThem.addActionListener(al);
        btnSua.addActionListener(al);
        btnXoa.addActionListener(al);
        btnLamMoi.addActionListener(al);
        btnTim.addActionListener(al);
        btnNhapExcel.addActionListener(al);
        btnXuatExcel.addActionListener(al);
    }
    public void setDonGia(double donGia) {
        txtDonGia.setValue(donGia);
        updateThanhTien(); // tự cập nhật thành tiền
    }

    public void refreshThanhTien() {
        updateThanhTien();
    }


    public JTable getTable() { return tblCTDH; }
    public DefaultTableModel getModel() { return model; }

    public String getKeyword() { return txtTimKiem.getText().trim(); }

    public JComboBox<String> getCboMaDonHang() { return cboMaDonHang; }
    public JComboBox<String> getCboMaSP() { return cboMaSP; }

    public Integer getIDDangChon() {
        int row = tblCTDH.getSelectedRow();
        if (row < 0) return null;
        try { return Integer.parseInt(valueAt(row, 0)); }
        catch (Exception e) { return null; }
    }

    public void fillFormTuBang() {
        int row = tblCTDH.getSelectedRow();
        if (row < 0) return;

        txtID.setText(valueAt(row, 0));
        cboMaDonHang.setSelectedItem(valueAt(row, 1));
        cboMaSP.setSelectedItem(valueAt(row, 2));

        try { spSoLuong.setValue(Integer.parseInt(valueAt(row, 3))); }
        catch (Exception e) { spSoLuong.setValue(1); }

        txtDonGia.setText(valueAt(row, 4));
        txtThanhTien.setText(valueAt(row, 5));

        btnSua.setEnabled(true);
        btnXoa.setEnabled(true);
    }

    public void resetForm() {
        txtID.setText("");

        if (cboMaDonHang.getItemCount() > 0) cboMaDonHang.setSelectedIndex(0);
        if (cboMaSP.getItemCount() > 0) cboMaSP.setSelectedIndex(0);

        spSoLuong.setValue(1);
        txtDonGia.setValue(0.00);
        txtThanhTien.setValue(0.00);

        tblCTDH.clearSelection();
        setButtonStateDefault();
    }

    public ChiTietDonHang getCTDHFromInput() {
        ChiTietDonHang ct = new ChiTietDonHang();

        // ID (nếu có)
        try {
            String s = txtID.getText().trim();
            if (!s.isEmpty()) ct.setId(Integer.parseInt(s));
        } catch (Exception ignored) {}

        ct.setMaDonHang(String.valueOf(cboMaDonHang.getSelectedItem()));
        ct.setMaSP(String.valueOf(cboMaSP.getSelectedItem()));

        int soLuong = 1;
        try { soLuong = (int) spSoLuong.getValue(); } catch (Exception ignored) {}
        ct.setSoLuong(soLuong);

        double donGia = parseMoney(txtDonGia.getText());
        ct.setDonGia(donGia);

        double thanhTien = soLuong * donGia;
        ct.setThanhTien(thanhTien);

        return ct;
    }

    // ===== Helpers =====
    public void updateThanhTien() {
        int soLuong = 1;
        try { soLuong = (int) spSoLuong.getValue(); } catch (Exception ignored) {}
        double donGia = parseMoney(txtDonGia.getText());
        txtThanhTien.setValue(soLuong * donGia);
    }

    private String valueAt(int row, int col) {
        Object o = model.getValueAt(row, col);
        return o == null ? "" : o.toString();
    }

    private double parseMoney(String s) {
        if (s == null) return 0.0;
        s = s.trim().replace(",", "");
        if (s.isEmpty()) return 0.0;
        try { return Double.parseDouble(s); }
        catch (Exception e) { return 0.0; }
    }

    private void setButtonStateDefault() {
        btnSua.setEnabled(false);
        btnXoa.setEnabled(false);
    }

    // ===== Style methods (để bạn khỏi bị lỗi "không tồn tại method") =====
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
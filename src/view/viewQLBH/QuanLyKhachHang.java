/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view.viewQLBH;

import domain.QLBH.KhachHang;
import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class QuanLyKhachHang extends JPanel {

    private JTable tblKhachHang;
    private DefaultTableModel model;

    // Search
    private JTextField txtTimKiem;
    private JButton btnTim;

    // Fields
    private JTextField txtMaKH, txtHoTen, txtSDT, txtEmail, txtDiaChi;

    // Buttons
    private JButton btnThem, btnSua, btnXoa, btnLamMoi, btnNhapExcel, btnXuatExcel;

    public QuanLyKhachHang() {
        setLayout(new BorderLayout());
        JPanel root = new JPanel(new BorderLayout(0, 10));
        root.setBorder(new EmptyBorder(12, 12, 12, 12));

        JLabel lblTitle = new JLabel("QUẢN LÝ KHÁCH HÀNG", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(new Color(0, 140, 220));
        root.add(lblTitle, BorderLayout.NORTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, createTablePanel(), createDetailPanel());
        split.setResizeWeight(0.70);
        split.setDividerSize(6);

        root.add(split, BorderLayout.CENTER);
        add(root, BorderLayout.CENTER);

        setButtonStateDefault();
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));

        // TOP search bar
        JPanel top = new JPanel(new BorderLayout(8, 8));
        txtTimKiem = new JTextField();
        btnTim = new JButton("Tìm kiếm");
        btnTim.setActionCommand("Tim");

        top.add(new JLabel("Từ khóa:"), BorderLayout.WEST);
        top.add(txtTimKiem, BorderLayout.CENTER);
        top.add(btnTim, BorderLayout.EAST);

        model = new DefaultTableModel(new Object[]{"MaKH", "Họ tên", "SDT", "Email", "Địa chỉ"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        tblKhachHang = new JTable(model);

        // STYLE TABLE
        tblKhachHang.setRowHeight(28);
        tblKhachHang.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tblKhachHang.setSelectionBackground(new Color(187, 222, 251));
        tblKhachHang.setSelectionForeground(Color.BLACK);

        // STYLE HEADER (xanh giống form bạn)
        JTableHeader header = tblKhachHang.getTableHeader();
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
        panel.add(new JScrollPane(tblKhachHang), BorderLayout.CENTER);
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

        txtMaKH = new JTextField();
        txtMaKH.setEnabled(false); // KO CHO SỬA
        txtHoTen = new JTextField();
        txtSDT = new JTextField();
        txtEmail = new JTextField();
        txtDiaChi = new JTextField();

        int r = 0;
        r = addRow(form, gbc, r, "Mã KH:", txtMaKH);
        r = addRow(form, gbc, r, "Họ tên:", txtHoTen);
        r = addRow(form, gbc, r, "SDT:", txtSDT);
        r = addRow(form, gbc, r, "Email:", txtEmail);
        r = addRow(form, gbc, r, "Địa chỉ:", txtDiaChi);

        wrap.add(form, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        btnPanel.setBorder(new EmptyBorder(10, 14, 14, 14));

        btnThem = new JButton("Thêm");
        btnSua = new JButton("Sửa");
        btnXoa = new JButton("Xóa");
        btnLamMoi = new JButton("Làm mới");
        btnNhapExcel = new JButton("Nhập Excel");
        btnXuatExcel = new JButton("Xuất Excel");

        // STYLE BUTTONS giống chuẩn bạn
        stylePrimaryGreen(btnThem);
        styleGray(btnSua);
        styleGray(btnXoa);
        stylePrimaryBlue(btnLamMoi);
        stylePrimaryBlue(btnNhapExcel);
        stylePrimaryBlue(btnXuatExcel);

        // ActionCommand để Controller switch-case
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

    // ===== API =====
    public void addActionListener(ActionListener al) {
        btnThem.addActionListener(al);
        btnSua.addActionListener(al);
        btnXoa.addActionListener(al);
        btnLamMoi.addActionListener(al);
        btnTim.addActionListener(al);
        btnNhapExcel.addActionListener(al);
        btnXuatExcel.addActionListener(al);
    }

    public JTable getTable() { return tblKhachHang; }
    public DefaultTableModel getModel() { return model; }
    public String getKeyword() { return txtTimKiem.getText().trim(); }

    public void setMaKH(String ma) { txtMaKH.setText(ma); }

    public void fillFormTuBang() {
        int row = tblKhachHang.getSelectedRow();
        if (row < 0) return;

        txtMaKH.setText(valueAt(row, 0));
        txtHoTen.setText(valueAt(row, 1));
        txtSDT.setText(valueAt(row, 2));
        txtEmail.setText(valueAt(row, 3));
        txtDiaChi.setText(valueAt(row, 4));

        btnSua.setEnabled(true);
        btnXoa.setEnabled(true);
    }

    public void resetForm() {
        txtHoTen.setText("");
        txtSDT.setText("");
        txtEmail.setText("");
        txtDiaChi.setText("");
        tblKhachHang.clearSelection();
        setButtonStateDefault();
    }

    public String getMaKHDangChon() {
        int row = tblKhachHang.getSelectedRow();
        if (row < 0) return null;
        return valueAt(row, 0);
    }

    public KhachHang getKhachHangFromInput() {
        KhachHang kh = new KhachHang();
        kh.setMaKH(txtMaKH.getText().trim());
        kh.setHoTen(txtHoTen.getText().trim());
        kh.setSdt(txtSDT.getText().trim());
        kh.setEmail(txtEmail.getText().trim());
        kh.setDiaChi(txtDiaChi.getText().trim());
        return kh;
    }

    // ===== Helpers =====
    private String valueAt(int row, int col) {
        Object o = model.getValueAt(row, col);
        return o == null ? "" : o.toString();
    }

    private void setButtonStateDefault() {
        btnSua.setEnabled(false);
        btnXoa.setEnabled(false);
    }

    // ===== Style methods =====
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
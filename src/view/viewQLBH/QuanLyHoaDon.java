/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view.viewQLBH;

import domain.QLBH.QLHD;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Calendar;

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
import javax.swing.SpinnerDateModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Admin
 */
public class QuanLyHoaDon extends JPanel {

    private JTable tblHoaDon;
    private DefaultTableModel model;

    private JTextField txtMaHoaDon, txtMaKH, txtMaNVLap;
    private JComboBox<String> cboMaDonHang; 
    private JSpinner spNgayLap;
    private JFormattedTextField txtTongTienHang, txtTienGiam, txtTongThanhToan;
    private JComboBox<String> cboPhuongThucTT, cboTrangThai;


    private JButton btnThem, btnSua, btnXoa, btnLamMoi, btnNhapExcel, btnXuatExcel;


    public QuanLyHoaDon() {
        setLayout(new BorderLayout());

        JPanel root = new JPanel(new BorderLayout(0, 10));
        root.setBorder(new EmptyBorder(12, 12, 12, 12));

        JLabel lblTitle = new JLabel("QUẢN LÝ HÓA ĐƠN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(new Color(0, 140, 220));
        root.add(lblTitle, BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                createTablePanel(),
                createDetailPanel()
        );
        splitPane.setResizeWeight(0.72);
        splitPane.setDividerSize(6);
        root.add(splitPane, BorderLayout.CENTER);

        add(root, BorderLayout.CENTER);

        setButtonStateDefault();
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));

        model = new DefaultTableModel(
                new Object[]{
                        "Mã hóa đơn", "Mã đơn hàng", "Mã KH", "Ngày lập",
                        "Tổng tiền hàng", "Tiền giảm", "Tổng thanh toán",
                        "PT thanh toán", "Trạng thái", "Mã NV lập"
                }, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                // để cột Ngày lập có thể giữ Date object (nếu bạn addRow bằng Date)
                if (columnIndex == 3) return Date.class;
                return Object.class;
            }
        };

        tblHoaDon = new JTable(model);

        // STYLE TABLE
        tblHoaDon.setRowHeight(28);
        tblHoaDon.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tblHoaDon.setSelectionBackground(new Color(187, 222, 251));
        tblHoaDon.setSelectionForeground(Color.BLACK);

        // Render cột ngày cho đẹp (dd/MM/yyyy HH:mm:ss)
        tblHoaDon.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            private final java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            @Override
            protected void setValue(Object value) {
                if (value instanceof Date) {
                    setText(sdf.format((Date) value));
                } else {
                    setText(value == null ? "" : value.toString());
                }
            }
        });

        // STYLE HEADER
        JTableHeader header = tblHoaDon.getTableHeader();
        header.setBackground(new Color(33, 150, 243));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setPreferredSize(new Dimension(header.getWidth(), 35));
        header.setOpaque(false);
        header.setDefaultRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
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

        panel.add(new JScrollPane(tblHoaDon), BorderLayout.CENTER);
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

        txtMaHoaDon = new JTextField();
        cboMaDonHang = new JComboBox<>();  // <<< NEW
        txtMaKH = new JTextField();
        txtMaNVLap = new JTextField();

        txtMaKH.setEditable(false);
        txtMaNVLap.setEditable(false);

        spNgayLap = new JSpinner(new SpinnerDateModel(new Date(), null, null, Calendar.MINUTE));
        JSpinner.DateEditor editor = new JSpinner.DateEditor(spNgayLap, "yyyy-MM-dd HH:mm:ss");
        spNgayLap.setEditor(editor);

        NumberFormat nf = new DecimalFormat("#,##0.00");
        txtTongTienHang = new JFormattedTextField(nf);
        txtTienGiam = new JFormattedTextField(nf);
        txtTongThanhToan = new JFormattedTextField(nf);

        txtTongTienHang.setValue(0.00);
        txtTienGiam.setValue(0.00);
        txtTongThanhToan.setValue(0.00);

        txtTongTienHang.setEditable(false);
        txtTienGiam.setEditable(false);
        txtTongThanhToan.setEditable(false);

        cboPhuongThucTT = new JComboBox<>(new String[]{
                "TIEN_MAT", "CHUYEN_KHOAN", "THE"
        });

        cboTrangThai = new JComboBox<>(new String[]{
                "HUY", "DA_THANH_TOAN", "HOAN_TIEN"
        });

        int r = 0;
        r = addRow(form, gbc, r, "Mã hóa đơn:", txtMaHoaDon);
        r = addRow(form, gbc, r, "Mã đơn hàng:", cboMaDonHang); // <<< đổi ở đây
        r = addRow(form, gbc, r, "Mã KH:", txtMaKH);
        r = addRow(form, gbc, r, "Ngày lập:", spNgayLap);
        r = addRow(form, gbc, r, "Tổng tiền hàng:", txtTongTienHang);
        r = addRow(form, gbc, r, "Tiền giảm:", txtTienGiam);
        r = addRow(form, gbc, r, "Tổng thanh toán:", txtTongThanhToan);
        r = addRow(form, gbc, r, "Phương thức TT:", cboPhuongThucTT);
        r = addRow(form, gbc, r, "Trạng thái:", cboTrangThai);
        r = addRow(form, gbc, r, "Mã NV lập:", txtMaNVLap);


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

        // style giống các form khác
        stylePrimaryGreen(btnThem);
        stylePrimaryBlue(btnLamMoi);

        styleGray(btnSua);
        styleGray(btnXoa);

        // Excel: cho đồng bộ (bạn muốn xanh dương hết cũng được)
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

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 1;
        form.add(lb, gbc);

        gbc.gridx = 0;
        gbc.gridy = row + 1;
        gbc.weightx = 1;
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
    }

    public JTable getTable() {
        return tblHoaDon;
    }

    public DefaultTableModel getModel() {
        return model;
    }

    public void fillFormTuBang() {
        int row = tblHoaDon.getSelectedRow();
        if (row < 0) return;

        txtMaHoaDon.setText(valueAt(row, 0));
        txtMaHoaDon.setEnabled(false);

        cboMaDonHang.setSelectedItem(valueAt(row, 1));
        txtMaKH.setText(valueAt(row, 2));


        // Ngày lập: nếu model đang giữ Date object thì lấy trực tiếp
        Object oNgay = model.getValueAt(row, 3);
        if (oNgay instanceof Date) {
            spNgayLap.setValue((Date) oNgay);
        } else {
            // fallback: tránh crash
            spNgayLap.setValue(new Date());
        }

        txtTongTienHang.setText(valueAt(row, 4));
        txtTienGiam.setText(valueAt(row, 5));
        txtTongThanhToan.setText(valueAt(row, 6));

        cboPhuongThucTT.setSelectedItem(valueAt(row, 7));
        cboTrangThai.setSelectedItem(valueAt(row, 8));
        txtMaNVLap.setText(valueAt(row, 9));

        btnSua.setEnabled(true);
        btnXoa.setEnabled(true);
    }

    public void resetForm() {
        txtMaHoaDon.setText("");
        txtMaHoaDon.setEnabled(true);

        if (cboMaDonHang.getItemCount() > 0) cboMaDonHang.setSelectedIndex(0);
        txtMaKH.setText("");
        txtMaNVLap.setText("");

        txtTongTienHang.setValue(0.00);
        txtTienGiam.setValue(0.00);
        txtTongThanhToan.setValue(0.00);

        cboPhuongThucTT.setSelectedIndex(0);
        cboTrangThai.setSelectedIndex(0);

        tblHoaDon.clearSelection();
        setButtonStateDefault();
    }

    public String getMaHoaDonDangChon() {
        int row = tblHoaDon.getSelectedRow();
        if (row < 0) return null;
        return valueAt(row, 0); // cột MaHoaDon
    }

    public QLHD getHoaDonFromInput() {
        QLHD hd = new QLHD();

        hd.setMaHoaDon(txtMaHoaDon.getText().trim());
        hd.setMaDonHang(String.valueOf(cboMaDonHang.getSelectedItem()));
        hd.setMaKH(txtMaKH.getText().trim());
        hd.setNgayLap((Date) spNgayLap.getValue());

        float tongHang = Float.parseFloat(normalizeMoney(txtTongTienHang.getText()));
        float giam = Float.parseFloat(normalizeMoney(txtTienGiam.getText()));
        float thanhToan = tongHang - giam;
        if (thanhToan < 0) thanhToan = 0;

        hd.setTongTienHang(tongHang);
        hd.setTienGiam(giam);
        hd.setTongThanhToan(thanhToan);

        hd.setPhuongThucTT(String.valueOf(cboPhuongThucTT.getSelectedItem()));
        hd.setTrangThai(String.valueOf(cboTrangThai.getSelectedItem()));
        hd.setMaNV_Lap(txtMaNVLap.getText().trim());

        return hd;
    }


    private String valueAt(int row, int col) {
        Object o = model.getValueAt(row, col);
        return o == null ? "" : o.toString();
    }

    private String normalizeMoney(String s) {
        if (s == null) return "0.00";
        s = s.trim().replace(",", "");
        if (s.isEmpty()) return "0.00";
        return s;
    }

    private void setButtonStateDefault() {
        btnSua.setEnabled(false);
        btnXoa.setEnabled(false);
    }
    public JComboBox<String> getCboMaDonHang() { return cboMaDonHang; }

    public void setDonHangInfoToForm(String maKH, float tongTienHang, float tienGiam, String maNVLap) {
        txtMaKH.setText(maKH == null ? "" : maKH);
        txtMaNVLap.setText(maNVLap == null ? "" : maNVLap);

        txtTongTienHang.setValue((double) tongTienHang);
        txtTienGiam.setValue((double) tienGiam);

        float thanhToan = tongTienHang - tienGiam;
        if (thanhToan < 0) thanhToan = 0;
        txtTongThanhToan.setValue((double) thanhToan);
    }
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

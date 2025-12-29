/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view.viewQLBH;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 *
 * @author Admin
 */
public class ThongkeQLBH extends JPanel{

    private final ThongKePanel panel;

    public ThongkeQLBH() {
        setLayout(new BorderLayout());
        panel = new ThongKePanel();
        add(panel, BorderLayout.CENTER);
    }

    // ====== Controller API ======
    public JButton getBtnThongKe() { return panel.btnThongKe; }
    public JButton getBtnReset()   { return panel.btnReset; }

    public Date getFromDate() { return (Date) panel.spFrom.getValue(); }
    public Date getToDate()   { return (Date) panel.spTo.getValue(); }

    public int getGroupMode() { return panel.cboGroup.getSelectedIndex(); }

    public void resetFilterDefault() {
        panel.spFrom.setValue(daysAgo(7));
        panel.spTo.setValue(new Date());
        panel.cboGroup.setSelectedIndex(0);
    }

    public void setKPI(double doanhThu, int soHoaDon, double tienGiam) {
        panel.lbRevenue.setText(panel.moneyFmt.format(doanhThu));
        panel.lbInvoices.setText(String.valueOf(soHoaDon));
        panel.lbDiscount.setText(panel.moneyFmt.format(tienGiam));
    }

    public void setTrangThaiDonHang(Map<String, Integer> data) {
        panel.tmOrderStatus.setRowCount(0);
        if (data == null) return;
        data.forEach((k, v) -> panel.tmOrderStatus.addRow(new Object[]{k, v}));
    }

    /** mỗi row: {MaKH, HoTen, doanhThu} */
    public void setTopKhachHang(List<Object[]> rows) {
        panel.tmTopCustomers.setRowCount(0);
        if (rows == null) return;
        for (Object[] r : rows) {
            String maKH = String.valueOf(r[0]);
            String hoTen = String.valueOf(r[1]);
            double doanhThu = ((Number) r[2]).doubleValue();
            panel.tmTopCustomers.addRow(new Object[]{maKH, hoTen, panel.moneyFmt.format(doanhThu)});
        }
    }

    /** Controller sẽ truyền List<ThongkeQLBH.RevenuePoint> */
    public void setRevenueSeries(List<RevenuePoint> points) {
        panel.chart.setData(points);
    }

    public void showMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }

    private static Date daysAgo(int days) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, -days);
        return c.getTime();
    }

    // ===== Inner UI Panel =====
    static class ThongKePanel extends JPanel {

        // Filter
        final JSpinner spFrom;
        final JSpinner spTo;
        final JComboBox<String> cboGroup;
        final JButton btnThongKe;
        final JButton btnReset;

        // KPI
        final JLabel lbRevenue;
        final JLabel lbInvoices;
        final JLabel lbDiscount;

        // Chart
        final RevenueBarChart chart;

        // Tables
        final DefaultTableModel tmOrderStatus;
        final DefaultTableModel tmTopCustomers;

        final DecimalFormat moneyFmt = new DecimalFormat("#,##0.00");

        ThongKePanel() {
            setLayout(new BorderLayout(12, 12));
            setBorder(new EmptyBorder(12, 12, 12, 12));
            setBackground(Color.WHITE);

            // ===== TOP: Filter bar =====
            JPanel filter = new JPanel(new GridBagLayout());
            filter.setBackground(Color.WHITE);
            filter.setBorder(BorderFactory.createTitledBorder("Bộ lọc"));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(6, 6, 6, 6);
            gbc.anchor = GridBagConstraints.WEST;

            spFrom = createDateSpinner(daysAgo(7));
            spTo   = createDateSpinner(new Date());

            cboGroup = new JComboBox<>(new String[]{"Theo ngày", "Theo tháng", "Theo năm"});
            btnThongKe = new JButton("Thống kê");
            btnReset   = new JButton("Làm mới");

            gbc.gridx = 0; gbc.gridy = 0;
            filter.add(new JLabel("Từ ngày:"), gbc);
            gbc.gridx = 1;
            filter.add(spFrom, gbc);

            gbc.gridx = 2;
            filter.add(new JLabel("Đến ngày:"), gbc);
            gbc.gridx = 3;
            filter.add(spTo, gbc);

            gbc.gridx = 4;
            filter.add(new JLabel("Nhóm:"), gbc);
            gbc.gridx = 5;
            filter.add(cboGroup, gbc);

            gbc.gridx = 6;
            filter.add(btnThongKe, gbc);
            gbc.gridx = 7;
            filter.add(btnReset, gbc);

            add(filter, BorderLayout.NORTH);

            // ===== CENTER =====
            JPanel center = new JPanel(new BorderLayout(12, 12));
            center.setBackground(Color.WHITE);
            add(center, BorderLayout.CENTER);

            // KPI cards
            JPanel kpiRow = new JPanel(new GridLayout(1, 3, 12, 12));
            kpiRow.setBackground(Color.WHITE);

            lbRevenue  = new JLabel("0.00", SwingConstants.RIGHT);
            lbInvoices = new JLabel("0", SwingConstants.RIGHT);
            lbDiscount = new JLabel("0.00", SwingConstants.RIGHT);

            kpiRow.add(createKpiCard("Tổng doanh thu", lbRevenue));
            kpiRow.add(createKpiCard("Số hóa đơn (đã thanh toán)", lbInvoices));
            kpiRow.add(createKpiCard("Tổng tiền giảm", lbDiscount));

            center.add(kpiRow, BorderLayout.NORTH);

            // Chart
            chart = new RevenueBarChart();
            chart.setPreferredSize(new Dimension(800, 280));
            center.add(wrapTitled(chart, "Biểu đồ doanh thu"), BorderLayout.CENTER);

            // Tables
            JPanel bottom = new JPanel(new GridLayout(1, 2, 12, 12));
            bottom.setBackground(Color.WHITE);

            tmOrderStatus = new DefaultTableModel(new Object[]{"Trạng thái đơn", "Số lượng"}, 0) {
                @Override public boolean isCellEditable(int r, int c) { return false; }
            };
            JTable tbStatus = new JTable(tmOrderStatus);
            stylizeTable(tbStatus);
            bottom.add(wrapTitled(new JScrollPane(tbStatus), "Đơn hàng theo trạng thái"));

            tmTopCustomers = new DefaultTableModel(new Object[]{"Mã KH", "Họ tên", "Doanh thu"}, 0) {
                @Override public boolean isCellEditable(int r, int c) { return false; }
            };
            JTable tbTop = new JTable(tmTopCustomers);
            stylizeTable(tbTop);
            bottom.add(wrapTitled(new JScrollPane(tbTop), "Top khách hàng"));

            center.add(bottom, BorderLayout.SOUTH);
        }

        private JPanel createKpiCard(String title, JLabel valueLabel) {
            JPanel card = new JPanel(new BorderLayout());
            card.setBackground(Color.WHITE);
            card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(220, 220, 220)),
                    new EmptyBorder(12, 12, 12, 12)
            ));

            JLabel lbTitle = new JLabel(title);
            lbTitle.setFont(lbTitle.getFont().deriveFont(Font.BOLD, 14f));
            valueLabel.setFont(valueLabel.getFont().deriveFont(Font.BOLD, 22f));

            card.add(lbTitle, BorderLayout.NORTH);
            card.add(valueLabel, BorderLayout.CENTER);
            return card;
        }

        private JComponent wrapTitled(JComponent comp, String title) {
            JPanel p = new JPanel(new BorderLayout());
            p.setBackground(Color.WHITE);
            p.setBorder(BorderFactory.createTitledBorder(title));
            p.add(comp, BorderLayout.CENTER);
            return p;
        }

        private void stylizeTable(JTable t) {
            t.setRowHeight(26);
            t.getTableHeader().setReorderingAllowed(false);
            t.setFillsViewportHeight(true);
            t.setAutoCreateRowSorter(true);
        }

        private JSpinner createDateSpinner(Date date) {
            SpinnerDateModel model = new SpinnerDateModel(date, null, null, Calendar.DAY_OF_MONTH);
            JSpinner sp = new JSpinner(model);
            sp.setEditor(new JSpinner.DateEditor(sp, "yyyy-MM-dd"));
            return sp;
        }
    }

    // ========= DTO cho biểu đồ =========
    public static class RevenuePoint {
        public final String label;
        public final double value;

        public RevenuePoint(String label, double value) {
            this.label = label;
            this.value = value;
        }
    }

    // ========= Custom bar chart =========
    public static class RevenueBarChart extends JPanel {
        private List<RevenuePoint> data = java.util.Collections.emptyList();
        private final DecimalFormat fmt = new DecimalFormat("#,##0");

        public RevenueBarChart() {
            setBackground(Color.WHITE);
        }

        public void setData(List<RevenuePoint> data) {
            this.data = (data == null) ? java.util.Collections.emptyList() : data;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();
            int pad = 30;
            int chartW = w - pad * 2;
            int chartH = h - pad * 2;

            // Axis
            g2.setColor(new Color(210, 210, 210));
            g2.drawLine(pad, h - pad, w - pad, h - pad);
            g2.drawLine(pad, pad, pad, h - pad);

            if (data.isEmpty()) {
                g2.setColor(Color.GRAY);
                g2.drawString("Không có dữ liệu", pad + 10, pad + 20);
                g2.dispose();
                return;
            }

            double max = data.stream().mapToDouble(p -> p.value).max().orElse(1);
            int n = data.size();
            int gap = 8;
            int barW = Math.max(10, (chartW - (n - 1) * gap) / n);

            int x = pad + (chartW - (barW * n + gap * (n - 1))) / 2;
            int baseY = h - pad;

            for (int i = 0; i < n; i++) {
                RevenuePoint p = data.get(i);
                int barH = (int) Math.round((p.value / max) * (chartH - 20));
                int y = baseY - barH;

                g2.setColor(new Color(30, 144, 255));
                g2.fillRoundRect(x, y, barW, barH, 8, 8);

                g2.setColor(new Color(60, 60, 60));
                String valTxt = fmt.format(p.value);
                FontMetrics fm = g2.getFontMetrics();

                int tx = x + (barW - fm.stringWidth(valTxt)) / 2;
                g2.drawString(valTxt, tx, y - 6);

                String label = p.label;
                int lx = x + (barW - fm.stringWidth(label)) / 2;
                g2.drawString(label, lx, baseY + 16);

                x += barW + gap;
            }

            g2.dispose();
        }
    }
    
}


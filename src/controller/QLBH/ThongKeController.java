package controller.QLBH;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import model.QLBH.ThongKeDAO;
import view.viewQLBH.ThongkeQLBH;
import view.viewQLBH.ThongkeQLBH.RevenuePoint;

public class ThongKeController {

    private final ThongkeQLBH view;
    private final ThongKeDAO dao = new ThongKeDAO();

    public ThongKeController(ThongkeQLBH view) {
        this.view = view;
        registerEvents();
        load(); // load lần đầu
    }

    private void registerEvents() {
        view.getBtnThongKe().addActionListener(e -> load());

        view.getBtnReset().addActionListener(e -> {
            view.resetFilterDefault();
            load();
        });
    }

    private void load() {
        Date from = view.getFromDate();
        Date to = view.getToDate();
        if (from == null || to == null) return;

        Date toEnd = endOfDay(to);

        if (from.after(toEnd)) {
            view.showMessage("Ngày 'Từ' phải <= ngày 'Đến'.");
            return;
        }

        // ===== 1) KPI =====
        Map<String, Object> kpi = dao.getKPI(from, toEnd);
        double doanhThu = ((Number) kpi.getOrDefault("doanhThu", 0)).doubleValue();
        int soHoaDon = ((Number) kpi.getOrDefault("soHoaDon", 0)).intValue();
        double tienGiam = ((Number) kpi.getOrDefault("tienGiam", 0)).doubleValue();
        view.setKPI(doanhThu, soHoaDon, tienGiam);

        // ===== 2) Đơn hàng theo trạng thái =====
        Map<String, Integer> tt = dao.getTrangThaiDonHang(from, toEnd);
        view.setTrangThaiDonHang(tt);

        // ===== 3) Top khách hàng =====
        view.setTopKhachHang(dao.getTopKhachHang(from, toEnd));

        // ===== 4) Doanh thu theo nhóm để vẽ chart =====
        int groupMode = view.getGroupMode(); // 0=ngày,1=tháng,2=năm
        List<Object[]> rawSeries = dao.getDoanhThuSeries(from, toEnd, groupMode);

        List<RevenuePoint> points = new ArrayList<>();
        for (Object[] row : rawSeries) {
            String label = String.valueOf(row[0]);
            double val = ((Number) row[1]).doubleValue();
            points.add(new RevenuePoint(label, val));
        }
        view.setRevenueSeries(points);
    }

    private Date endOfDay(Date d) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 999);
        return c.getTime();
    }
}

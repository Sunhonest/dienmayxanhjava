package model.QLBH;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.ConnectDB;

public class ThongKeDAO {

    // 1) KPI: số hóa đơn + doanh thu + tiền giảm
    public Map<String, Object> getKPI(Date from, Date to) {
        Map<String, Object> rs = new HashMap<>();

        String sql = """
            SELECT 
                COUNT(*) AS soHoaDon,
                IFNULL(SUM(TongThanhToan),0) AS doanhThu,
                IFNULL(SUM(TienGiam),0) AS tienGiam
            FROM hoadon
            WHERE TrangThai = 'DA_THANH_TOAN'
              AND NgayLap BETWEEN ? AND ?
        """;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setTimestamp(1, new Timestamp(from.getTime()));
            ps.setTimestamp(2, new Timestamp(to.getTime()));

            try (ResultSet r = ps.executeQuery()) {
                if (r.next()) {
                    rs.put("soHoaDon", r.getLong("soHoaDon"));
                    rs.put("doanhThu", r.getDouble("doanhThu"));
                    rs.put("tienGiam", r.getDouble("tienGiam"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }

    // 2) Đơn hàng theo trạng thái
    public Map<String, Integer> getTrangThaiDonHang(Date from, Date to) {
        Map<String, Integer> map = new HashMap<>();

        String sql = """
            SELECT TrangThai, COUNT(*) AS soLuong
            FROM donhang
            WHERE NgayTao BETWEEN ? AND ?
            GROUP BY TrangThai
        """;

        try (Connection c = ConnectDB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setTimestamp(1, new Timestamp(from.getTime()));
            ps.setTimestamp(2, new Timestamp(to.getTime()));

            try (ResultSet r = ps.executeQuery()) {
                while (r.next()) {
                    map.put(r.getString("TrangThai"), r.getInt("soLuong"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    // 3) Top khách hàng (MaKH, HoTen, doanhThu)
    public List<Object[]> getTopKhachHang(Date from, Date to) {
        List<Object[]> list = new ArrayList<>();

        String sql = """
            SELECT h.MaKH, kh.HoTen, SUM(h.TongThanhToan) AS doanhThu
            FROM hoadon h
            JOIN khachhang kh ON h.MaKH = kh.MaKH
            WHERE h.TrangThai = 'DA_THANH_TOAN'
              AND h.NgayLap BETWEEN ? AND ?
            GROUP BY h.MaKH, kh.HoTen
            ORDER BY doanhThu DESC
            LIMIT 5
        """;

        try (Connection c = ConnectDB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setTimestamp(1, new Timestamp(from.getTime()));
            ps.setTimestamp(2, new Timestamp(to.getTime()));

            try (ResultSet r = ps.executeQuery()) {
                while (r.next()) {
                    list.add(new Object[]{
                        r.getString("MaKH"),
                        r.getString("HoTen"),
                        r.getDouble("doanhThu")
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 4) Doanh thu theo nhóm để vẽ biểu đồ
    // groupMode: 0=ngày, 1=tháng, 2=năm
    public List<Object[]> getDoanhThuSeries(Date from, Date to, int groupMode) {
        List<Object[]> list = new ArrayList<>();

        String fmt;
        if (groupMode == 1) fmt = "%Y-%m";
        else if (groupMode == 2) fmt = "%Y";
        else fmt = "%Y-%m-%d";

        String sql = """
            SELECT DATE_FORMAT(NgayLap, '%s') AS nhom,
                   IFNULL(SUM(TongThanhToan),0) AS doanhThu
            FROM hoadon
            WHERE TrangThai = 'DA_THANH_TOAN'
              AND NgayLap BETWEEN ? AND ?
            GROUP BY nhom
            ORDER BY MIN(NgayLap)
        """.formatted(fmt);

        try (Connection c = ConnectDB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setTimestamp(1, new Timestamp(from.getTime()));
            ps.setTimestamp(2, new Timestamp(to.getTime()));

            try (ResultSet r = ps.executeQuery()) {
                while (r.next()) {
                    list.add(new Object[]{
                        r.getString("nhom"),
                        r.getDouble("doanhThu")
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}

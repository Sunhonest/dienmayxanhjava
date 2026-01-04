/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.QLBH;

import domain.QLBH.QLDH;
import domain.QLBH.QLHD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import model.ConnectDB;

/**
 *
 * @author Admin
 */
public class HoaDonDAO {
    private static final String TABLE = "hoadon";

    public List<QLHD> getAll() {
        List<QLHD> list = new ArrayList<>();
        String sql = "SELECT MaHoaDon, MaDonHang, MaKH, NgayLap, TongTienHang, TienGiam, TongThanhToan, PhuongThucTT, TrangThai, MaNV_Lap "
                   + "FROM " + TABLE + " ORDER BY NgayLap DESC";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) list.add(map(rs));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // ✅ SEARCH
    public List<QLHD> search(String keyword) {
        List<QLHD> list = new ArrayList<>();
        String k = "%" + (keyword == null ? "" : keyword.trim()) + "%";

        String sql = "SELECT MaHoaDon, MaDonHang, MaKH, NgayLap, TongTienHang, TienGiam, TongThanhToan, PhuongThucTT, TrangThai, MaNV_Lap "
                   + "FROM " + TABLE + " "
                   + "WHERE MaHoaDon LIKE ? OR MaDonHang LIKE ? OR MaKH LIKE ? OR MaNV_Lap LIKE ? "
                   + "   OR TrangThai LIKE ? OR PhuongThucTT LIKE ? "
                   + "ORDER BY NgayLap DESC";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            for (int i = 1; i <= 6; i++) ps.setString(i, k);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // ✅ Tạo mã HD001, HD002...
    public String taoMaHoaDonMoi() {
        String sql = "SELECT MaHoaDon FROM " + TABLE + " ORDER BY MaHoaDon DESC LIMIT 1";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                String last = rs.getString(1); // VD: HD005
                int num = Integer.parseInt(last.replace("HD", ""));
                return String.format("HD%03d", num + 1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "HD001";
    }

    public boolean checkTrungMa(String MaHoaDon) {
        String sql = "SELECT 1 FROM " + TABLE + " WHERE MaHoaDon = ? LIMIT 1";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, MaHoaDon);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public int insert(QLHD hd) {
        String sql = "INSERT INTO " + TABLE
                + " (MaHoaDon, MaDonHang, MaKH, NgayLap, TongTienHang, TienGiam, TongThanhToan, PhuongThucTT, TrangThai, MaNV_Lap) "
                + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, hd.getMaHoaDon());
            ps.setString(2, hd.getMaDonHang());
            ps.setString(3, hd.getMaKH());

            if (hd.getNgayLap() == null) ps.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            else ps.setTimestamp(4, new Timestamp(hd.getNgayLap().getTime()));

            ps.setFloat(5, hd.getTongTienHang());
            ps.setFloat(6, hd.getTienGiam());
            ps.setFloat(7, hd.getTongThanhToan());

            ps.setString(8, hd.getPhuongThucTT());
            ps.setString(9, hd.getTrangThai());
            ps.setString(10, hd.getMaNV_Lap());

            return ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int update(QLHD hd) {
        String sql = "UPDATE " + TABLE
                + " SET MaDonHang=?, MaKH=?, NgayLap=?, TongTienHang=?, TienGiam=?, TongThanhToan=?, PhuongThucTT=?, TrangThai=?, MaNV_Lap=? "
                + " WHERE MaHoaDon=?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, hd.getMaDonHang());
            ps.setString(2, hd.getMaKH());

            if (hd.getNgayLap() == null) ps.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            else ps.setTimestamp(3, new Timestamp(hd.getNgayLap().getTime()));

            ps.setFloat(4, hd.getTongTienHang());
            ps.setFloat(5, hd.getTienGiam());
            ps.setFloat(6, hd.getTongThanhToan());
            ps.setString(7, hd.getPhuongThucTT());
            ps.setString(8, hd.getTrangThai());
            ps.setString(9, hd.getMaNV_Lap());

            ps.setString(10, hd.getMaHoaDon());

            return ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int delete(String MaHoaDon) {
        String sql = "DELETE FROM " + TABLE + " WHERE MaHoaDon = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, MaHoaDon);
            return ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // ================== COMBOBOX ==================
    public List<String> getAllMaDonHang() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT MaDonHang FROM donhang ORDER BY NgayTao DESC";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(rs.getString(1));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<String> getAllMaKH() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT MaKH FROM khachhang ORDER BY MaKH";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(rs.getString(1));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<String> getAllMaNV() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT MaNV FROM nhanvien ORDER BY MaNV";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(rs.getString(1));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static class DonHangInfo {
        public String maKH;
        public float tongTienHang;
        public float tienGiam;
        public String maNV;
    }

    public DonHangInfo getDonHangInfo(String maDonHang) {
        String sql = """
            SELECT MaKH,
                   ThanhTien AS TongTienHang,
                   IFNULL(TienGiam, 0) AS TienGiam,
                   GiaThanhToan,
                   MaNV
            FROM donhang
            WHERE MaDonHang = ?
        """;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maDonHang);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    DonHangInfo info = new DonHangInfo();
                    info.maKH = rs.getString("MaKH");
                    info.tongTienHang = rs.getFloat("TongTienHang"); // alias từ ThanhTien
                    info.tienGiam = rs.getFloat("TienGiam");
                    info.maNV = rs.getString("MaNV");
                    return info;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private QLHD map(ResultSet rs) throws Exception {
        QLHD hd = new QLHD();

        hd.setMaHoaDon(rs.getString("MaHoaDon"));
        hd.setMaDonHang(rs.getString("MaDonHang"));
        hd.setMaKH(rs.getString("MaKH"));

        Timestamp ts = rs.getTimestamp("NgayLap");
        hd.setNgayLap(ts == null ? null : new java.util.Date(ts.getTime()));

        hd.setTongTienHang(rs.getFloat("TongTienHang"));
        hd.setTienGiam(rs.getFloat("TienGiam"));
        hd.setTongThanhToan(rs.getFloat("TongThanhToan"));

        hd.setPhuongThucTT(rs.getString("PhuongThucTT"));
        hd.setTrangThai(rs.getString("TrangThai"));
        hd.setMaNV_Lap(rs.getString("MaNV_Lap"));

        return hd;
    }

}

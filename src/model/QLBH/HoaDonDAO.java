/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.QLBH;

import domain.QLBH.QLHD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
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
        String sql = "SELECT MaHoaDon, MaDonHang, MaKH, NgayLap, TongTienHang, TienGiam, TongThanhToan, PhuongThucTT, TrangThai, MaNV_Lap from " + TABLE;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                QLHD hd = new QLHD();
                hd.setMaHoaDon(rs.getString("MaHoaDon"));
                hd.setMaDonHang(rs.getString("MaDonHang"));
                hd.setMaKH(rs.getString("MaKH"));
                Timestamp ts = rs.getTimestamp("NgayLap");
                hd.setNgayLap(ts); // nếu domain bạn để Date/String thì bạn tự đổi cho khớp

                hd.setTongTienHang(rs.getFloat("TongTienHang"));
                hd.setTienGiam(rs.getFloat("TienGiam"));
                hd.setTongThanhToan(rs.getFloat("TongThanhToan"));
                hd.setPhuongThucTT(rs.getString("PhuongThucTT"));

                hd.setTrangThai(rs.getString("TrangThai"));
                hd.setMaNV_Lap(rs.getString("MaNV_Lap"));

                list.add(hd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
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
        String sql = "INSERT INTO " + TABLE +
                " (MaHoaDon, MaDonHang, MaKH, NgayLap, TongTienHang, TienGiam, TongThanhToan, PhuongThucTT, TrangThai, MaNV_Lap) " +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?,?,?)";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, hd.getMaHoaDon());
            ps.setString(2, hd.getMaDonHang());
            ps.setString(3, hd.getMaKH());

            // NgayTao
            // Nếu domain bạn đang là Timestamp/Date thì dùng 1 trong các dòng dưới
            if (hd.getNgayLap() == null) {
                ps.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            } else if (hd.getNgayLap() instanceof Timestamp) {
                ps.setTimestamp(4, (Timestamp) hd.getNgayLap());
            } else if (hd.getNgayLap() instanceof java.util.Date) {
                ps.setTimestamp(4, new Timestamp(((java.util.Date) hd.getNgayLap()).getTime()));
            } else {
                // fallback
                ps.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            }
            ps.setFloat(5,hd.getTongTienHang());
            ps.setFloat(6,hd.getTienGiam());
            ps.setFloat(7,hd.getTongThanhToan());

            ps.setString(8, hd.getPhuongThucTT());
            ps.setString(9, hd.getTrangThai());
            ps.setString(10,hd.getMaNV_Lap());

            return ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int update(QLHD hd) {
    String sql = "UPDATE " + TABLE +
            " SET MaDonHang=?, MaKH=?, NgayLap=?, TongTienHang=?, TienGiam=?, TongThanhToan=?, PhuongThucTT=?, TrangThai=?, MaNV_Lap=? " +
            " WHERE MaHoaDon=?";

    try (Connection con = ConnectDB.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, hd.getMaDonHang());
        ps.setString(2, hd.getMaKH());

        if (hd.getNgayLap() == null) {
            ps.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
        } else {
            ps.setTimestamp(3, new Timestamp(hd.getNgayLap().getTime())); // Date -> Timestamp
        }

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
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.QLBH;

import domain.QLBH.QLDH;
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
public class DonHangDAO {
    // Đổi tên bảng đúng với DB của bạn (vd: donhang / hoadon)
    private static final String TABLE = "donhang";

    public List<QLDH> getAll() {
        List<QLDH> list = new ArrayList<>();
        String sql = "SELECT MaKH, MaDonHang, NgayTao, TongTien, TienGiam, VoucherID, TrangThai, MaNV FROM " + TABLE;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                QLDH dh = new QLDH();
                dh.setMaKH(rs.getString("MaKH"));
                dh.setMaDonHang(rs.getString("MaDonHang"));

                Timestamp ts = rs.getTimestamp("NgayTao");
                dh.setNgayTao(ts); // nếu domain bạn để Date/String thì bạn tự đổi cho khớp

                dh.setTongTien(rs.getFloat("TongTien"));
                dh.setTienGiam(rs.getFloat("TienGiam"));

                int voucher = rs.getInt("VoucherID");
                if (rs.wasNull()) voucher = 0;
                dh.setVoucherID(voucher);

                dh.setTrangThai(rs.getString("TrangThai"));
                dh.setMaNV(rs.getString("MaNV"));

                list.add(dh);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean checkTrungMa(String maDonHang) {
        String sql = "SELECT 1 FROM " + TABLE + " WHERE MaDonHang = ? LIMIT 1";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maDonHang);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public int insert(QLDH dh) {
        String sql = "INSERT INTO " + TABLE +
                " (MaKH, MaDonHang, NgayTao, TongTien, TienGiam, VoucherID, TrangThai, MaNV) " +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, dh.getMaKH());
            ps.setString(2, dh.getMaDonHang());

            // NgayTao
            // Nếu domain bạn đang là Timestamp/Date thì dùng 1 trong các dòng dưới
            if (dh.getNgayTao() == null) {
                ps.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            } else if (dh.getNgayTao() instanceof Timestamp) {
                ps.setTimestamp(3, (Timestamp) dh.getNgayTao());
            } else if (dh.getNgayTao() instanceof java.util.Date) {
                ps.setTimestamp(3, new Timestamp(((java.util.Date) dh.getNgayTao()).getTime()));
            } else {
                // fallback
                ps.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            }
            ps.setFloat(4,dh.getTongTien());
            ps.setFloat(5,dh.getTienGiam());

            // VoucherID: nếu 0 thì set NULL (tuỳ bạn muốn)
            if (dh.getVoucherID() <= 0) ps.setNull(6, Types.INTEGER);
            else ps.setInt(6, dh.getVoucherID());

            ps.setString(7, dh.getTrangThai());
            ps.setString(8, dh.getMaNV());

            return ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int update(QLDH dh) {
        String sql = "UPDATE " + TABLE +
                " SET MaKH=?, NgayTao=?, TongTien=?, TienGiam=?, VoucherID=?, TrangThai=?, MaNV=? " +
                " WHERE MaDonHang=?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, dh.getMaKH());

            // NgayTao
            if (dh.getNgayTao() == null) {
                ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            } else if (dh.getNgayTao() instanceof Timestamp) {
                ps.setTimestamp(2, (Timestamp) dh.getNgayTao());
            } else if (dh.getNgayTao() instanceof java.util.Date) {
                ps.setTimestamp(2, new Timestamp(((java.util.Date) dh.getNgayTao()).getTime()));
            } else {
                ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            }

            ps.setFloat(3, dh.getTongTien());
            ps.setFloat(4, dh.getTienGiam());

            if (dh.getVoucherID() <= 0) ps.setNull(5, Types.INTEGER);
            else ps.setInt(5, dh.getVoucherID());

            ps.setString(6, dh.getTrangThai());
            ps.setString(7, dh.getMaNV());

            ps.setString(8, dh.getMaDonHang());

            return ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int delete(String maDonHang) {
        String sql = "DELETE FROM " + TABLE + " WHERE MaDonHang = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maDonHang);
            return ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}

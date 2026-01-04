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
    private static final String TABLE = "donhang";

    public List<QLDH> getAll() {
        List<QLDH> list = new ArrayList<>();
        String sql = "SELECT ID, MaDonHang, MaKH, NgayTao, ThanhTien, TienGiam, GiaThanhToan, VoucherID, TrangThai, MaNV "
                   + "FROM " + TABLE + " ORDER BY NgayTao DESC";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) list.add(map(rs));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<QLDH> search(String keyword) {
        List<QLDH> list = new ArrayList<>();
        String k = "%" + (keyword == null ? "" : keyword.trim()) + "%";

        String sql = "SELECT ID, MaDonHang, MaKH, NgayTao, ThanhTien, TienGiam, GiaThanhToan, VoucherID, TrangThai, MaNV "
                   + "FROM " + TABLE + " "
                   + "WHERE MaDonHang LIKE ? OR MaKH LIKE ? OR MaNV LIKE ? OR TrangThai LIKE ? "
                   + "   OR CAST(VoucherID AS CHAR) LIKE ? "
                   + "ORDER BY NgayTao DESC";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            for (int i = 1; i <= 5; i++) ps.setString(i, k);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // ✅ MaDonHang lấy từ chi tiết đơn hàng
    public List<String> getAllMaDonHangFromCTDH() {
        return simpleList("SELECT DISTINCT MaDonHang FROM chitiet_donhang ORDER BY MaDonHang");
    }

    // ✅ Tính Thành tiền từ CTDH theo MaDonHang (ưu tiên cột ThanhTien nếu có)
    public float getThanhTienFromCTDH(String maDonHang) {
        String sql =
            "SELECT " +
            "  COALESCE(SUM(ThanhTien), SUM(SoLuong * DonGia), 0) AS Tong " +
            "FROM chitiet_donhang " +
            "WHERE MaDonHang = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maDonHang);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getFloat("Tong");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0f;
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
        // ID auto tăng -> không insert ID
        String sql = "INSERT INTO " + TABLE
                + " (MaDonHang, MaKH, NgayTao, ThanhTien, TienGiam, GiaThanhToan, VoucherID, TrangThai, MaNV) "
                + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, dh.getMaDonHang());
            ps.setString(2, dh.getMaKH());

            if (dh.getNgayTao() == null) ps.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            else ps.setTimestamp(3, new Timestamp(dh.getNgayTao().getTime()));

            ps.setFloat(4, dh.getThanhTien());
            ps.setFloat(5, dh.getTienGiam());
            ps.setFloat(6, dh.getGiaThanhToan());

            if (dh.getVoucherID() <= 0) ps.setNull(7, Types.INTEGER);
            else ps.setInt(7, dh.getVoucherID());

            ps.setString(8, dh.getTrangThai());

            if (dh.getMaNV() == null || dh.getMaNV().isBlank()) ps.setNull(9, Types.VARCHAR);
            else ps.setString(9, dh.getMaNV());

            return ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int update(QLDH dh) {
        String sql = "UPDATE " + TABLE
                + " SET MaKH=?, NgayTao=?, ThanhTien=?, TienGiam=?, GiaThanhToan=?, VoucherID=?, TrangThai=?, MaNV=? "
                + " WHERE MaDonHang=?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, dh.getMaKH());

            if (dh.getNgayTao() == null) ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            else ps.setTimestamp(2, new Timestamp(dh.getNgayTao().getTime()));

            ps.setFloat(3, dh.getThanhTien());
            ps.setFloat(4, dh.getTienGiam());
            ps.setFloat(5, dh.getGiaThanhToan());

            if (dh.getVoucherID() <= 0) ps.setNull(6, Types.INTEGER);
            else ps.setInt(6, dh.getVoucherID());

            ps.setString(7, dh.getTrangThai());

            if (dh.getMaNV() == null || dh.getMaNV().isBlank()) ps.setNull(8, Types.VARCHAR);
            else ps.setString(8, dh.getMaNV());

            ps.setString(9, dh.getMaDonHang());

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

    public double tinhTienGiamTheoVoucher(int voucherId, double thanhTien) {
        String sql = """
            SELECT VoucherID, LoaiGiam, GiaTriGiam, GiamToiDa, DonHangToiThieu,
                   NgayBatDau, NgayKetThuc, SoLuong, TrangThai
            FROM voucher
            WHERE VoucherID = ?
        """;
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, voucherId);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) return 0.0;

            String trangThai = rs.getString("TrangThai");
            if (!"KICH_HOAT".equalsIgnoreCase(trangThai)) return 0.0;

            double giaTriGiam = rs.getDouble("GiaTriGiam");
            double giamToiDa = rs.getDouble("GiamToiDa");
            double donToiThieu = rs.getDouble("DonHangToiThieu");

            Timestamp batDau = rs.getTimestamp("NgayBatDau");
            Timestamp ketThuc = rs.getTimestamp("NgayKetThuc");
            Timestamp now = new Timestamp(System.currentTimeMillis());

            if (batDau != null && now.before(batDau)) return 0.0;
            if (ketThuc != null && now.after(ketThuc)) return 0.0;

            if (thanhTien < donToiThieu) return 0.0;

            String loaiGiam = rs.getString("LoaiGiam");
            double tienGiam = "PHAN_TRAM".equalsIgnoreCase(loaiGiam)
                    ? thanhTien * giaTriGiam / 100.0
                    : giaTriGiam;

            if (giamToiDa > 0) tienGiam = Math.min(tienGiam, giamToiDa);
            if (tienGiam > thanhTien) tienGiam = thanhTien;
            if (tienGiam < 0) tienGiam = 0;

            return tienGiam;

        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }
    }

    public List<String> getAllMaKH() {
        return simpleList("SELECT MaKH FROM khachhang ORDER BY MaKH");
    }

    public List<String> getAllMaNV() {
        return simpleList("SELECT MaNV FROM nhanvien ORDER BY MaNV");
    }

    public List<String> getAllVoucherIDActive() {
        return simpleList("SELECT VoucherID FROM voucher WHERE TrangThai='KICH_HOAT' ORDER BY VoucherID");
    }

    private List<String> simpleList(String sql) {
        List<String> list = new ArrayList<>();
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) list.add(rs.getString(1));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private QLDH map(ResultSet rs) throws Exception {
        QLDH dh = new QLDH();
        dh.setId(rs.getInt("ID"));
        dh.setMaDonHang(rs.getString("MaDonHang"));
        dh.setMaKH(rs.getString("MaKH"));

        Timestamp ts = rs.getTimestamp("NgayTao");
        dh.setNgayTao(ts == null ? null : new java.util.Date(ts.getTime()));

        dh.setThanhTien(rs.getFloat("ThanhTien"));
        dh.setTienGiam(rs.getFloat("TienGiam"));
        dh.setGiaThanhToan(rs.getFloat("GiaThanhToan"));

        int voucher = rs.getInt("VoucherID");
        if (rs.wasNull()) voucher = 0;
        dh.setVoucherID(voucher);

        dh.setTrangThai(rs.getString("TrangThai"));
        dh.setMaNV(rs.getString("MaNV"));
        return dh;
    }
}   
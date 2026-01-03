/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.QLBH;

import com.sun.jdi.connect.spi.Connection;
import domain.QLBH.ChiTietDonHang;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.ConnectDB;

/**
 *
 * @author Admin
 */
public class ChiTietDonHangDAO {
    private static final String TABLE = "chitiet_donhang";

    public List<ChiTietDonHang> getAll() {
        List<ChiTietDonHang> list = new ArrayList<>();
        String sql = "SELECT ID, MaDonHang, MaSP, SoLuong, DonGia, ThanhTien FROM " + TABLE + " ORDER BY ID DESC";

        try (java.sql.Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ChiTietDonHang ct = new ChiTietDonHang();
                ct.setId(rs.getInt("ID"));
                ct.setMaDonHang(rs.getString("MaDonHang"));
                ct.setMaSP(rs.getString("MaSP"));
                ct.setSoLuong(rs.getInt("SoLuong"));
                ct.setDonGia(rs.getDouble("DonGia"));
                ct.setThanhTien(rs.getDouble("ThanhTien"));
                list.add(ct);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public int insert(ChiTietDonHang ct) {
        String sql = "INSERT INTO " + TABLE + " (MaDonHang, MaSP, SoLuong, DonGia, ThanhTien) VALUES (?,?,?,?,?)";
        try (java.sql.Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, ct.getMaDonHang());
            ps.setString(2, ct.getMaSP());
            ps.setInt(3, ct.getSoLuong());
            ps.setDouble(4, ct.getDonGia());
            ps.setDouble(5, ct.getThanhTien());
            return ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int update(ChiTietDonHang ct) {
        String sql = "UPDATE " + TABLE + " SET MaDonHang=?, MaSP=?, SoLuong=?, DonGia=?, ThanhTien=? WHERE ID=?";
        try (java.sql.Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, ct.getMaDonHang());
            ps.setString(2, ct.getMaSP());
            ps.setInt(3, ct.getSoLuong());
            ps.setDouble(4, ct.getDonGia());
            ps.setDouble(5, ct.getThanhTien());
            ps.setInt(6, ct.getId());
            return ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int delete(int id) {
        String sql = "DELETE FROM " + TABLE + " WHERE ID=?";
        try (java.sql.Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<ChiTietDonHang> search(String keyword) {
        List<ChiTietDonHang> list = new ArrayList<>();
        String sql = "SELECT ID, MaDonHang, MaSP, SoLuong, DonGia, ThanhTien FROM " + TABLE +
                     " WHERE CAST(ID AS CHAR) LIKE ? OR MaDonHang LIKE ? OR MaSP LIKE ? " +
                     " ORDER BY ID DESC";

        String k = "%" + (keyword == null ? "" : keyword.trim()) + "%";
        try (java.sql.Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, k);
            ps.setString(2, k);
            ps.setString(3, k);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ChiTietDonHang ct = new ChiTietDonHang();
                    ct.setId(rs.getInt("ID"));
                    ct.setMaDonHang(rs.getString("MaDonHang"));
                    ct.setMaSP(rs.getString("MaSP"));
                    ct.setSoLuong(rs.getInt("SoLuong"));
                    ct.setDonGia(rs.getDouble("DonGia"));
                    ct.setThanhTien(rs.getDouble("ThanhTien"));
                    list.add(ct);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // ====== COMBOBOX DATA (KHÔNG TẠO DAO RIÊNG) ======
    public List<String> getAllMaDonHang() {
        return simpleList("SELECT MaDonHang FROM donhang ORDER BY MaDonHang");
    }

    public List<String> getAllMaSP() {
        return simpleList("SELECT MaSP FROM sanpham ORDER BY MaSP");
    }

    public double getGiaBanByMaSP(String maSP) {
        String sql = "SELECT GiaBan FROM sanpham WHERE MaSP=? LIMIT 1";
        try (java.sql.Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maSP);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getDouble(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    private List<String> simpleList(String sql) {
        List<String> list = new ArrayList<>();
        try (java.sql.Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) list.add(rs.getString(1));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}

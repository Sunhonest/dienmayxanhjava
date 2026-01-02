/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.QLBH;
import domain.QLBH.KhachHang;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.ConnectDB;

public class KhachHangDAO {
    private static final String TABLE = "khachhang";

    public List<KhachHang> getAll() {
        List<KhachHang> list = new ArrayList<>();
        String sql = "SELECT MaKH, HoTen, SDT, Email, DiaChi FROM " + TABLE + " ORDER BY MaKH";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                KhachHang kh = new KhachHang();
                kh.setMaKH(rs.getString("MaKH"));
                kh.setHoTen(rs.getString("HoTen"));
                kh.setSdt(rs.getString("SDT"));
                kh.setEmail(rs.getString("Email"));
                kh.setDiaChi(rs.getString("DiaChi"));
                list.add(kh);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean checkTrungMa(String maKH) {
        String sql = "SELECT 1 FROM " + TABLE + " WHERE MaKH = ? LIMIT 1";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maKH);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Tạo mã KH001, KH002... tăng dần
    public String taoMaKHMoi() {
        String sql = "SELECT MaKH FROM " + TABLE + " ORDER BY MaKH DESC LIMIT 1";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                String last = rs.getString(1); // VD: KH005
                int num = Integer.parseInt(last.replace("KH", ""));
                return String.format("KH%03d", num + 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "KH001";
    }

    public int insert(KhachHang kh) {
        String sql = "INSERT INTO " + TABLE + " (MaKH, HoTen, SDT, Email, DiaChi) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, kh.getMaKH());
            ps.setString(2, kh.getHoTen());
            ps.setString(3, kh.getSdt());
            ps.setString(4, kh.getEmail());
            ps.setString(5, kh.getDiaChi());
            return ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int update(KhachHang kh) {
        String sql = "UPDATE " + TABLE + " SET HoTen=?, SDT=?, Email=?, DiaChi=? WHERE MaKH=?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, kh.getHoTen());
            ps.setString(2, kh.getSdt());
            ps.setString(3, kh.getEmail());
            ps.setString(4, kh.getDiaChi());
            ps.setString(5, kh.getMaKH());
            return ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int delete(String maKH) {
        String sql = "DELETE FROM " + TABLE + " WHERE MaKH=?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maKH);
            return ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<KhachHang> search(String keyword) {
        List<KhachHang> list = new ArrayList<>();
        String sql = "SELECT MaKH, HoTen, SDT, Email, DiaChi FROM " + TABLE +
                     " WHERE MaKH LIKE ? OR HoTen LIKE ? OR SDT LIKE ? OR Email LIKE ? OR DiaChi LIKE ?" +
                     " ORDER BY MaKH";

        String k = "%" + (keyword == null ? "" : keyword.trim()) + "%";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            for (int i = 1; i <= 5; i++) ps.setString(i, k);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    KhachHang kh = new KhachHang();
                    kh.setMaKH(rs.getString("MaKH"));
                    kh.setHoTen(rs.getString("HoTen"));
                    kh.setSdt(rs.getString("SDT"));
                    kh.setEmail(rs.getString("Email"));
                    kh.setDiaChi(rs.getString("DiaChi"));
                    list.add(kh);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
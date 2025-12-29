package model.KhuyenMai;

import domain.Voucher;
import model.ConnectDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VoucherDAO {

    public List<Voucher> getAll() {
        List<Voucher> list = new ArrayList<>();
        String sql = "SELECT * FROM voucher ORDER BY VoucherID DESC";
        
        try (Connection cons = ConnectDB.getConnection();
             PreparedStatement ps = cons.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Voucher v = new Voucher();
                v.setVoucherID(rs.getInt("VoucherID"));
                v.setMaVoucher(rs.getString("MaVoucher"));
                v.setTenVoucher(rs.getString("TenVoucher"));
                v.setLoaiGiam(rs.getString("LoaiGiam"));
                v.setGiaTriGiam(rs.getDouble("GiaTriGiam"));
                v.setGiamToiDa(rs.getDouble("GiamToiDa"));
                v.setDonHangToiThieu(rs.getDouble("DonHangToiThieu"));
                v.setNgayBatDau(rs.getTimestamp("NgayBatDau"));
                v.setNgayKetThuc(rs.getTimestamp("NgayKetThuc"));
                v.setSoLuong(rs.getInt("SoLuong"));
                v.setTrangThai(rs.getString("TrangThai"));
                v.setMaNV_Tao(rs.getString("MaNV_Tao"));
                list.add(v);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Voucher getById(int id) {
        String sql = "SELECT * FROM voucher WHERE VoucherID = ?";
        
        try (Connection cons = ConnectDB.getConnection();
             PreparedStatement ps = cons.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                Voucher v = new Voucher();
                v.setVoucherID(rs.getInt("VoucherID"));
                v.setMaVoucher(rs.getString("MaVoucher"));
                v.setTenVoucher(rs.getString("TenVoucher"));
                v.setLoaiGiam(rs.getString("LoaiGiam"));
                v.setGiaTriGiam(rs.getDouble("GiaTriGiam"));
                v.setGiamToiDa(rs.getDouble("GiamToiDa"));
                v.setDonHangToiThieu(rs.getDouble("DonHangToiThieu"));
                v.setNgayBatDau(rs.getTimestamp("NgayBatDau"));
                v.setNgayKetThuc(rs.getTimestamp("NgayKetThuc"));
                v.setSoLuong(rs.getInt("SoLuong"));
                v.setTrangThai(rs.getString("TrangThai"));
                v.setMaNV_Tao(rs.getString("MaNV_Tao"));
                return v;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Voucher getByMaVoucher(String maVoucher) {
        String sql = "SELECT * FROM voucher WHERE MaVoucher = ?";
        
        try (Connection cons = ConnectDB.getConnection();
             PreparedStatement ps = cons.prepareStatement(sql)) {
            
            ps.setString(1, maVoucher);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                Voucher v = new Voucher();
                v.setVoucherID(rs.getInt("VoucherID"));
                v.setMaVoucher(rs.getString("MaVoucher"));
                v.setTenVoucher(rs.getString("TenVoucher"));
                v.setLoaiGiam(rs.getString("LoaiGiam"));
                v.setGiaTriGiam(rs.getDouble("GiaTriGiam"));
                v.setGiamToiDa(rs.getDouble("GiamToiDa"));
                v.setDonHangToiThieu(rs.getDouble("DonHangToiThieu"));
                v.setNgayBatDau(rs.getTimestamp("NgayBatDau"));
                v.setNgayKetThuc(rs.getTimestamp("NgayKetThuc"));
                v.setSoLuong(rs.getInt("SoLuong"));
                v.setTrangThai(rs.getString("TrangThai"));
                v.setMaNV_Tao(rs.getString("MaNV_Tao"));
                return v;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public int insert(Voucher voucher) {
        String sql = "INSERT INTO voucher (MaVoucher, TenVoucher, LoaiGiam, GiaTriGiam, GiamToiDa, " +
                     "DonHangToiThieu, NgayBatDau, NgayKetThuc, SoLuong, TrangThai, MaNV_Tao) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection cons = ConnectDB.getConnection();
             PreparedStatement ps = cons.prepareStatement(sql)) {
            
            ps.setString(1, voucher.getMaVoucher());
            ps.setString(2, voucher.getTenVoucher());
            ps.setString(3, voucher.getLoaiGiam());
            ps.setDouble(4, voucher.getGiaTriGiam());
            ps.setDouble(5, voucher.getGiamToiDa());
            ps.setDouble(6, voucher.getDonHangToiThieu());
            ps.setTimestamp(7, new Timestamp(voucher.getNgayBatDau().getTime()));
            ps.setTimestamp(8, new Timestamp(voucher.getNgayKetThuc().getTime()));
            ps.setInt(9, voucher.getSoLuong());
            ps.setString(10, voucher.getTrangThai());
            ps.setString(11, voucher.getMaNV_Tao());
            
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public int update(Voucher voucher) {
        String sql = "UPDATE voucher SET TenVoucher = ?, LoaiGiam = ?, GiaTriGiam = ?, GiamToiDa = ?, " +
                     "DonHangToiThieu = ?, NgayBatDau = ?, NgayKetThuc = ?, SoLuong = ?, TrangThai = ? " +
                     "WHERE VoucherID = ?";
        
        try (Connection cons = ConnectDB.getConnection();
             PreparedStatement ps = cons.prepareStatement(sql)) {
            
            ps.setString(1, voucher.getTenVoucher());
            ps.setString(2, voucher.getLoaiGiam());
            ps.setDouble(3, voucher.getGiaTriGiam());
            ps.setDouble(4, voucher.getGiamToiDa());
            ps.setDouble(5, voucher.getDonHangToiThieu());
            ps.setTimestamp(6, new Timestamp(voucher.getNgayBatDau().getTime()));
            ps.setTimestamp(7, new Timestamp(voucher.getNgayKetThuc().getTime()));
            ps.setInt(8, voucher.getSoLuong());
            ps.setString(9, voucher.getTrangThai());
            ps.setInt(10, voucher.getVoucherID());
            
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public int delete(int voucherID) {
        // Kiểm tra xem voucher có đang được sử dụng trong đơn hàng không
        String checkSql = "SELECT COUNT(*) FROM donhang WHERE VoucherID = ?";
        try (Connection cons = ConnectDB.getConnection();
             PreparedStatement checkPs = cons.prepareStatement(checkSql)) {
            
            checkPs.setInt(1, voucherID);
            ResultSet rs = checkPs.executeQuery();
            rs.next();
            int count = rs.getInt(1);
            
            if (count > 0) {
                // Nếu voucher đã được sử dụng, chỉ chuyển trạng thái thành NGUNG
                String updateSql = "UPDATE voucher SET TrangThai = 'NGUNG' WHERE VoucherID = ?";
                try (PreparedStatement updatePs = cons.prepareStatement(updateSql)) {
                    updatePs.setInt(1, voucherID);
                    return updatePs.executeUpdate();
                }
            } else {
                // Nếu chưa được sử dụng, xóa luôn
                String deleteSql = "DELETE FROM voucher WHERE VoucherID = ?";
                try (PreparedStatement deletePs = cons.prepareStatement(deleteSql)) {
                    deletePs.setInt(1, voucherID);
                    return deletePs.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean isMaVoucherExists(String maVoucher) {
        String sql = "SELECT COUNT(*) FROM voucher WHERE MaVoucher = ?";
        try (Connection cons = ConnectDB.getConnection();
             PreparedStatement ps = cons.prepareStatement(sql)) {
            
            ps.setString(1, maVoucher);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Voucher> getActiveVouchers() {
        List<Voucher> list = new ArrayList<>();
        String sql = "SELECT * FROM voucher WHERE TrangThai = 'KICH_HOAT' " +
                     "AND NgayBatDau <= NOW() AND NgayKetThuc >= NOW() AND SoLuong > 0";
        
        try (Connection cons = ConnectDB.getConnection();
             PreparedStatement ps = cons.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Voucher v = new Voucher();
                v.setVoucherID(rs.getInt("VoucherID"));
                v.setMaVoucher(rs.getString("MaVoucher"));
                v.setTenVoucher(rs.getString("TenVoucher"));
                v.setLoaiGiam(rs.getString("LoaiGiam"));
                v.setGiaTriGiam(rs.getDouble("GiaTriGiam"));
                v.setGiamToiDa(rs.getDouble("GiamToiDa"));
                v.setDonHangToiThieu(rs.getDouble("DonHangToiThieu"));
                v.setNgayBatDau(rs.getTimestamp("NgayBatDau"));
                v.setNgayKetThuc(rs.getTimestamp("NgayKetThuc"));
                v.setSoLuong(rs.getInt("SoLuong"));
                v.setTrangThai(rs.getString("TrangThai"));
                v.setMaNV_Tao(rs.getString("MaNV_Tao"));
                list.add(v);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int decreaseVoucherQuantity(int voucherID) {
        String sql = "UPDATE voucher SET SoLuong = SoLuong - 1 WHERE VoucherID = ? AND SoLuong > 0";
        try (Connection cons = ConnectDB.getConnection();
             PreparedStatement ps = cons.prepareStatement(sql)) {
            
            ps.setInt(1, voucherID);
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
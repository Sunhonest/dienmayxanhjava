package model.Kho;

import domain.PhieuNhap;
import model.ConnectDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PhieuNhapDAO {

    // Lấy tất cả dữ liệu trong DB ra bảng
    public List<PhieuNhap> getAll() {
        List<PhieuNhap> list = new ArrayList<>();
        String sql = "SELECT pn.*, sp.TenSP, ncc.TenNCC " +
                     "FROM phieu_nhap pn " +
                     "JOIN sanpham sp ON pn.MaSP = sp.MaSP " +
                     "JOIN nhacungcap ncc ON pn.MaNCC = ncc.MaNCC " +
                     "ORDER BY pn.ID DESC"; // Cái nào mới nhập hiện lên đầu
        
        try (Connection cons = ConnectDB.getConnection();
             PreparedStatement ps = cons.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                PhieuNhap pn = new PhieuNhap();
                pn.setId(rs.getInt("ID"));
                pn.setMaPhieu(rs.getString("MaPhieu"));
                pn.setMaNV(rs.getString("MaNV"));
                pn.setMaNCC(rs.getString("MaNCC"));
                pn.setTenNCC(rs.getString("TenNCC"));
                pn.setMaSP(rs.getString("MaSP"));
                pn.setTenSP(rs.getString("TenSP"));
                pn.setSoLuong(rs.getInt("SoLuong"));
                pn.setDonGia(rs.getDouble("DonGia"));
                pn.setThanhTien(rs.getDouble("ThanhTien"));
                pn.setNgayNhap(rs.getTimestamp("NgayNhap"));
                list.add(pn);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // THÊM: Insert vào phieu_nhap VÀ Cộng tồn kho SanPham
    public boolean them(PhieuNhap pn) {
        Connection cons = null;
        try {
            cons = ConnectDB.getConnection();
            cons.setAutoCommit(false); // Transaction

            // 1. Insert phiếu nhập
            String sqlInsert = "INSERT INTO phieu_nhap(MaPhieu, MaNV, MaNCC, MaSP, SoLuong, DonGia, ThanhTien) VALUES(?,?,?,?,?,?,?)";
            PreparedStatement ps = cons.prepareStatement(sqlInsert);
            ps.setString(1, pn.getMaPhieu());
            ps.setString(2, pn.getMaNV());
            ps.setString(3, pn.getMaNCC());
            ps.setString(4, pn.getMaSP());
            ps.setInt(5, pn.getSoLuong());
            ps.setDouble(6, pn.getDonGia());
            ps.setDouble(7, pn.getThanhTien());
            ps.executeUpdate();

            // 2. Cộng tồn kho
            String sqlUpdateKho = "UPDATE sanpham SET TonKho = TonKho + ?, GiaNhap = ? WHERE MaSP = ?";
            PreparedStatement psKho = cons.prepareStatement(sqlUpdateKho);
            psKho.setInt(1, pn.getSoLuong());
            psKho.setDouble(2, pn.getDonGia());
            psKho.setString(3, pn.getMaSP());
            psKho.executeUpdate();

            cons.commit();
            return true;
        } catch (Exception e) {
            try { if(cons!=null) cons.rollback(); } catch(SQLException ex){}
            e.printStackTrace();
            return false;
        } finally {
            ConnectDB.closeConnection(cons);
        }
    }
    
    // Hàm sửa phiếu nhập
public boolean suaPhieuNhap(PhieuNhap pnMoi, int soLuongCu) {
    Connection cons = null;
    try {
        cons = ConnectDB.getConnection();
        cons.setAutoCommit(false); // Bắt đầu Transaction

        // 1. Cập nhật bảng phieu_nhap
        String sqlUpdate = "UPDATE phieu_nhap SET SoLuong=?, DonGia=?, ThanhTien=?, GhiChu=? WHERE ID=?";
        PreparedStatement ps = cons.prepareStatement(sqlUpdate);
        ps.setInt(1, pnMoi.getSoLuong());
        ps.setDouble(2, pnMoi.getDonGia());
        ps.setDouble(3, pnMoi.getThanhTien());
        ps.setString(4, "Đã sửa: " + pnMoi.getSoLuong()); // Ví dụ lưu ghi chú
        ps.setInt(5, pnMoi.getId());
        ps.executeUpdate();

        // 2. Cân bằng kho: (Trừ số lượng cũ) + (Cộng số lượng mới)
        // Logic: Tồn kho = Tồn kho - Số cũ + Số mới
        int chenhLech = pnMoi.getSoLuong() - soLuongCu; 
        
        String sqlKho = "UPDATE sanpham SET TonKho = TonKho + ? WHERE MaSP = ?";
        PreparedStatement psKho = cons.prepareStatement(sqlKho);
        psKho.setInt(1, chenhLech); // Nếu số mới lớn hơn -> cộng thêm, nhỏ hơn -> trừ bớt
        psKho.setString(2, pnMoi.getMaSP());
        psKho.executeUpdate();

        cons.commit(); // Xác nhận thành công
        return true;
    } catch (Exception e) {
        try { if(cons!=null) cons.rollback(); } catch(SQLException ex){}
        e.printStackTrace();
        return false;
    } finally {
        ConnectDB.closeConnection(cons);
    }
}

    // XÓA: Delete phieu_nhap VÀ Trừ tồn kho (hoàn tác)
    public boolean xoa(int id, String maSP, int soLuong) {
        Connection cons = null;
        try {
            cons = ConnectDB.getConnection();
            cons.setAutoCommit(false);

            // 1. Xóa dòng nhập
            String sqlDel = "DELETE FROM phieu_nhap WHERE ID = ?";
            PreparedStatement ps = cons.prepareStatement(sqlDel);
            ps.setInt(1, id);
            ps.executeUpdate();

            // 2. Trừ lại tồn kho (vì nhập sai nên xóa -> phải trừ đi)
            String sqlUpdateKho = "UPDATE sanpham SET TonKho = TonKho - ? WHERE MaSP = ?";
            PreparedStatement psKho = cons.prepareStatement(sqlUpdateKho);
            psKho.setInt(1, soLuong);
            psKho.setString(2, maSP);
            psKho.executeUpdate();

            cons.commit();
            return true;
        } catch (Exception e) {
            try { if(cons!=null) cons.rollback(); } catch(SQLException ex){}
            e.printStackTrace();
            return false;
        } finally {
            ConnectDB.closeConnection(cons);
        }
    }

    // SỬA: Phức tạp hơn (Trừ cũ -> Cộng mới), tôi làm đơn giản là cập nhật thông tin thôi
    // Lưu ý: Nếu sửa số lượng thì phải tính toán lại kho, để an toàn tôi khuyên bạn nên Xóa đi Nhập lại.
    // Nhưng nếu bạn muốn update cơ bản (ví dụ sai giá):
    public boolean suaGia(int id, double donGiaMoi, double thanhTienMoi) {
         try (Connection cons = ConnectDB.getConnection();
             PreparedStatement ps = cons.prepareStatement("UPDATE phieu_nhap SET DonGia=?, ThanhTien=? WHERE ID=?")) {
            ps.setDouble(1, donGiaMoi);
            ps.setDouble(2, thanhTienMoi);
            ps.setInt(3, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
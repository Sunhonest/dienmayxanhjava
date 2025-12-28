/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.Kho;

/**
 *
 * @author nguye
 */
import domain.NhaCungCap;
import model.ConnectDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NhaCungCapDAO {
    public List<NhaCungCap> getAll() {
        List<NhaCungCap> list = new ArrayList<>();
        try (Connection cons = ConnectDB.getConnection();
             PreparedStatement ps = cons.prepareStatement("SELECT * FROM nhacungcap")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                NhaCungCap ncc = new NhaCungCap();
                ncc.setMaNCC(rs.getString("MaNCC"));
                ncc.setTenNCC(rs.getString("TenNCC"));
                ncc.setDiaChi(rs.getString("DiaChi"));
                ncc.setSdt(rs.getString("SDT"));
                list.add(ncc);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.Kho;

/**
 *
 * @author nguye
 */
import domain.DanhMuc;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.ConnectDB;

public class DanhMucDAO {
    public List<DanhMuc> getAll() {
        List<DanhMuc> list = new ArrayList<>();
        try (Connection cons = ConnectDB.getConnection();
             PreparedStatement ps = cons.prepareStatement("SELECT * FROM danhmuc");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new DanhMuc(rs.getString("MaDanhMuc"), rs.getString("TenDanhMuc")));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
}
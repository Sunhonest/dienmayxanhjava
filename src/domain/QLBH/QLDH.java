/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package domain.QLBH;

import java.util.Date;

/**
 *
 * @author Admin
 */
public class QLDH {
    private int id;                
    private String maDonHang;        
    private String maKH;
    private Date ngayTao;

    private float thanhTien;       
    private float tienGiam;        
    private float giaThanhToan;    

    private int voucherID;          
    private String trangThai;      
    private String maNV;

    public QLDH() {
    }

    public QLDH(int id, String maDonHang, String maKH, Date ngayTao, float thanhTien, float tienGiam, float giaThanhToan, int voucherID, String trangThai, String maNV) {
        this.id = id;
        this.maDonHang = maDonHang;
        this.maKH = maKH;
        this.ngayTao = ngayTao;
        this.thanhTien = thanhTien;
        this.tienGiam = tienGiam;
        this.giaThanhToan = giaThanhToan;
        this.voucherID = voucherID;
        this.trangThai = trangThai;
        this.maNV = maNV;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMaDonHang() {
        return maDonHang;
    }

    public void setMaDonHang(String maDonHang) {
        this.maDonHang = maDonHang;
    }

    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public Date getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(Date ngayTao) {
        this.ngayTao = ngayTao;
    }

    public float getThanhTien() {
        return thanhTien;
    }

    public void setThanhTien(float thanhTien) {
        this.thanhTien = thanhTien;
    }

    public float getTienGiam() {
        return tienGiam;
    }

    public void setTienGiam(float tienGiam) {
        this.tienGiam = tienGiam;
    }

    public float getGiaThanhToan() {
        return giaThanhToan;
    }

    public void setGiaThanhToan(float giaThanhToan) {
        this.giaThanhToan = giaThanhToan;
    }

    public int getVoucherID() {
        return voucherID;
    }

    public void setVoucherID(int voucherID) {
        this.voucherID = voucherID;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }
    
    
}

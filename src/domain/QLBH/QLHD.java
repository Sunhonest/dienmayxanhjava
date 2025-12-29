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
public class QLHD {
    private String MaHoaDon;
    private String MaDonHang;
    private String MaKH;
    private Date NgayLap;
    private float TongTienHang;
    private float TienGiam;
    private float TongThanhToan;
    private String PhuongThucTT;
    private String TrangThai;
    private String MaNV_Lap;

    public QLHD(String MaHoaDon, String MaDonHang, String MaKH, Date NgayLap, float TongTienHang, float TienGiam, float TongThanhToan, String PhuongThucTT, String TrangThai, String MaNV_Lap) {
        this.MaHoaDon = MaHoaDon;
        this.MaDonHang = MaDonHang;
        this.MaKH = MaKH;
        this.NgayLap = NgayLap;
        this.TongTienHang = TongTienHang;
        this.TienGiam = TienGiam;
        this.TongThanhToan = TongThanhToan;
        this.PhuongThucTT = PhuongThucTT;
        this.TrangThai = TrangThai;
        this.MaNV_Lap = MaNV_Lap;
    }

    public QLHD() {
    }

    public String getMaHoaDon() {
        return MaHoaDon;
    }

    public void setMaHoaDon(String MaHoaDon) {
        this.MaHoaDon = MaHoaDon;
    }

    public String getMaDonHang() {
        return MaDonHang;
    }

    public void setMaDonHang(String MaDonHang) {
        this.MaDonHang = MaDonHang;
    }

    public String getMaKH() {
        return MaKH;
    }

    public void setMaKH(String MaKH) {
        this.MaKH = MaKH;
    }

    public Date getNgayLap() {
        return NgayLap;
    }

    public void setNgayLap(Date NgayLap) {
        this.NgayLap = NgayLap;
    }

    public float getTongTienHang() {
        return TongTienHang;
    }

    public void setTongTienHang(float TongTienHang) {
        this.TongTienHang = TongTienHang;
    }

    public float getTienGiam() {
        return TienGiam;
    }

    public void setTienGiam(float TienGiam) {
        this.TienGiam = TienGiam;
    }

    public float getTongThanhToan() {
        return TongThanhToan;
    }

    public void setTongThanhToan(float TongThanhToan) {
        this.TongThanhToan = TongThanhToan;
    }

    public String getPhuongThucTT() {
        return PhuongThucTT;
    }

    public void setPhuongThucTT(String PhuongThucTT) {
        this.PhuongThucTT = PhuongThucTT;
    }

    public String getTrangThai() {
        return TrangThai;
    }

    public void setTrangThai(String TrangThai) {
        this.TrangThai = TrangThai;
    }

    public String getMaNV_Lap() {
        return MaNV_Lap;
    }

    public void setMaNV_Lap(String MaNV_Lap) {
        this.MaNV_Lap = MaNV_Lap;
    }
    
    
    
    
}

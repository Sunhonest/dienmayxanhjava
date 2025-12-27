/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package domain;

/**
 *
 * @author OS
 */

import java.util.Date;

public class PhieuBaoHanh {
    private String maPhieu;
    private String maHoaDon;      // Foreign Key -> hoadon
    private String maSP;          // Foreign Key -> sanpham
    private String soSerial;
    private String tenKhachHang;
    private String soDienThoai;
    private Date ngayTiepNhan;
    private String moTaLoi;
    private String trangThai;     // ENUM: MOI_TIEP_NHAN, DANG_SUA...
    private String maNV_TiepNhan; // Foreign Key -> nhanvien

    public PhieuBaoHanh() {
    }

    public PhieuBaoHanh(String maPhieu, String maHoaDon, String maSP, String soSerial, String tenKhachHang, String soDienThoai, Date ngayTiepNhan, String moTaLoi, String trangThai, String maNV_TiepNhan) {
        this.maPhieu = maPhieu;
        this.maHoaDon = maHoaDon;
        this.maSP = maSP;
        this.soSerial = soSerial;
        this.tenKhachHang = tenKhachHang;
        this.soDienThoai = soDienThoai;
        this.ngayTiepNhan = ngayTiepNhan;
        this.moTaLoi = moTaLoi;
        this.trangThai = trangThai;
        this.maNV_TiepNhan = maNV_TiepNhan;
    }

    // Getter & Setter
    public String getMaPhieu() { return maPhieu; }
    public void setMaPhieu(String maPhieu) { this.maPhieu = maPhieu; }

    public String getMaHoaDon() { return maHoaDon; }
    public void setMaHoaDon(String maHoaDon) { this.maHoaDon = maHoaDon; }

    public String getMaSP() { return maSP; }
    public void setMaSP(String maSP) { this.maSP = maSP; }

    public String getSoSerial() { return soSerial; }
    public void setSoSerial(String soSerial) { this.soSerial = soSerial; }

    public String getTenKhachHang() { return tenKhachHang; }
    public void setTenKhachHang(String tenKhachHang) { this.tenKhachHang = tenKhachHang; }

    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }

    public Date getNgayTiepNhan() { return ngayTiepNhan; }
    public void setNgayTiepNhan(Date ngayTiepNhan) { this.ngayTiepNhan = ngayTiepNhan; }

    public String getMoTaLoi() { return moTaLoi; }
    public void setMoTaLoi(String moTaLoi) { this.moTaLoi = moTaLoi; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public String getMaNV_TiepNhan() { return maNV_TiepNhan; }
    public void setMaNV_TiepNhan(String maNV_TiepNhan) { this.maNV_TiepNhan = maNV_TiepNhan; }
    
    @Override
    public String toString() {
        return maPhieu + " - " + tenKhachHang;
    }
}
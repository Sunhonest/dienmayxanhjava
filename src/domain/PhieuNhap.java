package domain;

import java.sql.Timestamp;

public class PhieuNhap {
    private int id; // ID tự tăng của dòng trong DB
    private String maPhieu;
    private String maNV;
    private String maNCC;
    private String tenNCC; // Thêm để hiển thị
    private String maSP;
    private String tenSP;  // Thêm để hiển thị
    private int soLuong;
    private double donGia;
    private double thanhTien;
    private Timestamp ngayNhap;

    public PhieuNhap() {}

    // Getter & Setter full
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getMaPhieu() { return maPhieu; }
    public void setMaPhieu(String maPhieu) { this.maPhieu = maPhieu; }
    public String getMaNV() { return maNV; }
    public void setMaNV(String maNV) { this.maNV = maNV; }
    public String getMaNCC() { return maNCC; }
    public void setMaNCC(String maNCC) { this.maNCC = maNCC; }
    public String getTenNCC() { return tenNCC; }
    public void setTenNCC(String tenNCC) { this.tenNCC = tenNCC; }
    public String getMaSP() { return maSP; }
    public void setMaSP(String maSP) { this.maSP = maSP; }
    public String getTenSP() { return tenSP; }
    public void setTenSP(String tenSP) { this.tenSP = tenSP; }
    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }
    public double getDonGia() { return donGia; }
    public void setDonGia(double donGia) { this.donGia = donGia; }
    public double getThanhTien() { return thanhTien; }
    public void setThanhTien(double thanhTien) { this.thanhTien = thanhTien; }
    public Timestamp getNgayNhap() { return ngayNhap; }
    public void setNgayNhap(Timestamp ngayNhap) { this.ngayNhap = ngayNhap; }
}
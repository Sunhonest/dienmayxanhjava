package domain.Kho;

import java.sql.Timestamp;

public class PhieuNhap {
    private int id;
    private String maPhieu;
    private String maNV;
    private String maNCC;
    private String tenNCC; // Biến phụ để hiển thị tên
    private String maSP;
    private String tenSP;  // Biến phụ để hiển thị tên
    private int soLuong;
    private double donGia;
    private double thanhTien;
    private Timestamp ngayNhap;
    
    private String ghiChu;

    public PhieuNhap() {
    }

    public PhieuNhap(int id, String maPhieu, String maNV, String maNCC, String maSP, int soLuong, double donGia, double thanhTien, Timestamp ngayNhap, String ghiChu) {
        this.id = id;
        this.maPhieu = maPhieu;
        this.maNV = maNV;
        this.maNCC = maNCC;
        this.maSP = maSP;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.thanhTien = thanhTien;
        this.ngayNhap = ngayNhap;
        this.ghiChu = ghiChu;
    }

    // --- GETTER & SETTER ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMaPhieu() {
        return maPhieu;
    }

    public void setMaPhieu(String maPhieu) {
        this.maPhieu = maPhieu;
    }

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public String getMaNCC() {
        return maNCC;
    }

    public void setMaNCC(String maNCC) {
        this.maNCC = maNCC;
    }

    public String getTenNCC() {
        return tenNCC;
    }

    public void setTenNCC(String tenNCC) {
        this.tenNCC = tenNCC;
    }

    public String getMaSP() {
        return maSP;
    }

    public void setMaSP(String maSP) {
        this.maSP = maSP;
    }

    public String getTenSP() {
        return tenSP;
    }

    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(double donGia) {
        this.donGia = donGia;
    }

    public double getThanhTien() {
        return thanhTien;
    }

    public void setThanhTien(double thanhTien) {
        this.thanhTien = thanhTien;
    }

    public Timestamp getNgayNhap() {
        return ngayNhap;
    }

    public void setNgayNhap(Timestamp ngayNhap) {
        this.ngayNhap = ngayNhap;
    }

    // --- THÊM GETTER/SETTER CHO GHI CHÚ ---
    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }
}
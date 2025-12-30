/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package domain.Kho;

/**
 *
 * @author nguye
 */
public class SanPham {
    private String maSP;
    private String tenSP;
    private String maDanhMuc;
    private String thuongHieu;
    private String donViTinh;
    private double giaNhap;
    private double giaBan;
    private int tonKho;
    private int thoiGianBaoHanh;
    private String trangThaiHang;      // MOI, CU, TRUNG_BAY
    private String trangThaiKinhDoanh; // DANG_BAN, NGUNG_KINH_DOANH
    private String moTa;
    private String hinhAnh;            // Link Cloudinary

    public SanPham() {
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
    public String getMaDanhMuc() { 
        return maDanhMuc; 
    }
    public void setMaDanhMuc(String maDanhMuc) { 
        this.maDanhMuc = maDanhMuc; 
    }
    public String getThuongHieu() { 
        return thuongHieu; 
    }
    public void setThuongHieu(String thuongHieu) { 
        this.thuongHieu = thuongHieu; 
    }
    public String getDonViTinh() { 
        return donViTinh; 
    }
    public void setDonViTinh(String donViTinh) { 
        this.donViTinh = donViTinh;
    }
    public double getGiaNhap() { 
        return giaNhap; 
    }
    public void setGiaNhap(double giaNhap) { 
        this.giaNhap = giaNhap;
    }
    public double getGiaBan() { 
        return giaBan; 
    }
    public void setGiaBan(double giaBan) { 
        this.giaBan = giaBan;
    }
    public int getTonKho() { 
        return tonKho; 
    }
    public void setTonKho(int tonKho) { 
        this.tonKho = tonKho;
    }
    public int getThoiGianBaoHanh() { 
        return thoiGianBaoHanh;
    }
    public void setThoiGianBaoHanh(int thoiGianBaoHanh) { 
        this.thoiGianBaoHanh = thoiGianBaoHanh; 
    }
    public String getTrangThaiHang() { 
        return trangThaiHang; 
    }
    public void setTrangThaiHang(String trangThaiHang) { 
        this.trangThaiHang = trangThaiHang;
    }
    public String getTrangThaiKinhDoanh() {
        return trangThaiKinhDoanh;
    }
    public void setTrangThaiKinhDoanh(String trangThaiKinhDoanh) { 
        this.trangThaiKinhDoanh = trangThaiKinhDoanh;
    }
    public String getMoTa() { 
        return moTa; 
    }
    public void setMoTa(String moTa) {
        this.moTa = moTa; 
    }
    public String getHinhAnh() { 
        return hinhAnh;
    }
    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh; 
    }
}

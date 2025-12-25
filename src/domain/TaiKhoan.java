/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package domain;

/**
 *
 * @author trana
 */
public class TaiKhoan {
    private String tenDangNhap;
    private String matKhau;
    private String maNV; // Khóa ngoại trỏ sang NhanVien (Chủ tài khoản)
    private int capDoQuyen; // 1: NV, 2: QL, 3: Admin
    private String trangThai; // "Hoạt động" hoặc "Đã khóa"

    public TaiKhoan() {
    }

    public TaiKhoan(String tenDangNhap, String matKhau, String maNV, int capDoQuyen, String trangThai) {
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.maNV = maNV;
        this.capDoQuyen = capDoQuyen;
        this.trangThai = trangThai;
    }

    // Getter & Setter
    public String getTenDangNhap() { return tenDangNhap; }
    public void setTenDangNhap(String tenDangNhap) { this.tenDangNhap = tenDangNhap; }

    public String getMatKhau() { return matKhau; }
    public void setMatKhau(String matKhau) { this.matKhau = matKhau; }

    public String getMaNV() { return maNV; }
    public void setMaNV(String maNV) { this.maNV = maNV; }

    public int getCapDoQuyen() { return capDoQuyen; }
    public void setCapDoQuyen(int capDoQuyen) { this.capDoQuyen = capDoQuyen; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
}

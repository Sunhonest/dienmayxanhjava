/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package domain;

/**
 *
 * @author trana
 */
public class NhanVien {
    private String maNV;
    private String hoTen;
    private String gioiTinh;
    private String sdt;
    private String email;
    private String maCV; // Khóa ngoại trỏ sang ChucVu

    public NhanVien() {
    }

    public NhanVien(String maNV, String hoTen, String gioiTinh, String sdt, String email, String maCV) {
        this.maNV = maNV;
        this.hoTen = hoTen;
        this.gioiTinh = gioiTinh;
        this.sdt = sdt;
        this.email = email;
        this.maCV = maCV;
    }

    // Getter & Setter
    public String getMaNV() { return maNV; }
    public void setMaNV(String maNV) { this.maNV = maNV; }

    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }

    public String getGioiTinh() { return gioiTinh; }
    public void setGioiTinh(String gioiTinh) { this.gioiTinh = gioiTinh; }

    public String getSdt() { return sdt; }
    public void setSdt(String sdt) { this.sdt = sdt; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMaCV() { return maCV; }
    public void setMaCV(String maCV) { this.maCV = maCV; }
    
    @Override
    public String toString() {
        return this.hoTen; // Hiển thị tên khi cần
    }
}

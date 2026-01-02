/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package domain.QLBH;

/**
 *
 * @author Admin
 */
public class KhachHang {
    private String maKH;
    private String hoTen;
    private String sdt;
    private String email;
    private String diaChi;

    public KhachHang() {}

    public KhachHang(String maKH, String hoTen, String sdt, String email, String diaChi) {
        this.maKH = maKH;
        this.hoTen = hoTen;
        this.sdt = sdt;
        this.email = email;
        this.diaChi = diaChi;
    }

    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }
    
}    
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package domain;

/**
 *
 * @author trana
 */
public class ChucVu {
    private String maCV;
    private String tenCV;
    private double luongCoBan; // Dùng double cho đơn giản (Thực tế nên dùng BigDecimal)
    private String moTa;

    public ChucVu() {
    }

    public ChucVu(String maCV, String tenCV, double luongCoBan, String moTa) {
        this.maCV = maCV;
        this.tenCV = tenCV;
        this.luongCoBan = luongCoBan;
        this.moTa = moTa;
    }

    // Getter & Setter
    public String getMaCV() { return maCV; }
    public void setMaCV(String maCV) { this.maCV = maCV; }

    public String getTenCV() { return tenCV; }
    public void setTenCV(String tenCV) { this.tenCV = tenCV; }

    public double getLuongCoBan() { return luongCoBan; }
    public void setLuongCoBan(double luongCoBan) { this.luongCoBan = luongCoBan; }

    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }

    // Hàm này cực quan trọng để hiển thị tên đẹp trong ComboBox
    @Override
    public String toString() {
        return this.tenCV; 
    }
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ChucVu) {
            ChucVu other = (ChucVu) obj;
            return other.getMaCV().equals(this.maCV);
        }
        return false;
    }
}

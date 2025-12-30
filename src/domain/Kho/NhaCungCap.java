/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package domain.Kho;

/**
 *
 * @author nguye
 */
public class NhaCungCap {
    private String maNCC;
    private String tenNCC;
    private String diaChi;
    private String sdt;

    public NhaCungCap() {}
    public NhaCungCap(String maNCC, String tenNCC) {
        this.maNCC = maNCC;
        this.tenNCC = tenNCC;
    }

    // Getter & Setter
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
    public String getDiaChi() { 
        return diaChi; 
    }
    public void setDiaChi(String diaChi) { 
        this.diaChi = diaChi; 
    }
    public String getSdt() { 
        return sdt; 
    }
    public void setSdt(String sdt) { 
        this.sdt = sdt; 
    }

    @Override
    public String toString() {
        return tenNCC; // Để hiển thị tên trong JComboBox
    }
}

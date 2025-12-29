package domain;

import java.util.Date;

/**
 * Domain class cho Đơn hàng
 * @author nguye
 */
public class DonHang {
    private String maDonHang;
    private String maKH;           // Added to match database
    private Date ngayTao;
    private double tongTien;
    private double tienGiam;
    private Integer voucherID;
    private String trangThai;    // MOI_TAO, DA_XAC_NHAN, HUY
    private String maNV;

    public DonHang() {
    }

    public DonHang(String maDonHang, String maKH, Date ngayTao, double tongTien, double tienGiam, 
                   Integer voucherID, String trangThai, String maNV) {
        this.maDonHang = maDonHang;
        this.maKH = maKH;
        this.ngayTao = ngayTao;
        this.tongTien = tongTien;
        this.tienGiam = tienGiam;
        this.voucherID = voucherID;
        this.trangThai = trangThai;
        this.maNV = maNV;
    }

    // Getter và Setter
    public String getMaDonHang() { return maDonHang; }
    public void setMaDonHang(String maDonHang) { this.maDonHang = maDonHang; }

    public String getMaKH() { return maKH; }
    public void setMaKH(String maKH) { this.maKH = maKH; }

    public Date getNgayTao() { return ngayTao; }
    public void setNgayTao(Date ngayTao) { this.ngayTao = ngayTao; }

    public double getTongTien() { return tongTien; }
    public void setTongTien(double tongTien) { this.tongTien = tongTien; }

    public double getTienGiam() { return tienGiam; }
    public void setTienGiam(double tienGiam) { this.tienGiam = tienGiam; }

    public Integer getVoucherID() { return voucherID; }
    public void setVoucherID(Integer voucherID) { this.voucherID = voucherID; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public String getMaNV() { return maNV; }
    public void setMaNV(String maNV) { this.maNV = maNV; }

    @Override
    public String toString() {
        return "DonHang{" +
                "maDonHang='" + maDonHang + '\'' +
                ", maKH='" + maKH + '\'' +
                ", ngayTao=" + ngayTao +
                ", tongTien=" + tongTien +
                ", tienGiam=" + tienGiam +
                ", voucherID=" + voucherID +
                ", trangThai='" + trangThai + '\'' +
                ", maNV='" + maNV + '\'' +
                '}';
    }
}
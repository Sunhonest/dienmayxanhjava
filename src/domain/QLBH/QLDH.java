package domain.QLBH;

import java.util.Date;

public class QLDH {
    private String maKH;
    private String maDonHang;
    private Date ngayTao;
    private float tongTien;
    private float tienGiam;
    private int voucherID;
    private String trangThai;
    private String maNV;

    public QLDH() {}

    public String getMaKH() { return maKH; }
    public void setMaKH(String maKH) { this.maKH = maKH; }

    public String getMaDonHang() { return maDonHang; }
    public void setMaDonHang(String maDonHang) { this.maDonHang = maDonHang; }

    public Date getNgayTao() { return ngayTao; }
    public void setNgayTao(Date ngayTao) { this.ngayTao = ngayTao; }

    public float getTongTien() { return tongTien; }
    public void setTongTien(float tongTien) { this.tongTien = tongTien; }

    public float getTienGiam() { return tienGiam; }
    public void setTienGiam(float tienGiam) { this.tienGiam = tienGiam; }

    public int getVoucherID() { return voucherID; }
    public void setVoucherID(int voucherID) { this.voucherID = voucherID; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public String getMaNV() { return maNV; }
    public void setMaNV(String maNV) { this.maNV = maNV; }
}

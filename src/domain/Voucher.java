package domain;

import java.util.Date;

public class Voucher {
    private int voucherID;
    private String maVoucher;
    private String tenVoucher;
    private String loaiGiam;        // PHAN_TRAM, TIEN_MAT
    private double giaTriGiam;
    private double giamToiDa;
    private double donHangToiThieu;
    private Date ngayBatDau;
    private Date ngayKetThuc;
    private int soLuong;
    private String trangThai;       // NHAP, KICH_HOAT, NGUNG
    private String maNV_Tao;

    public Voucher() {
    }

    public Voucher(int voucherID, String maVoucher, String tenVoucher, String loaiGiam, 
                   double giaTriGiam, double giamToiDa, double donHangToiThieu, 
                   Date ngayBatDau, Date ngayKetThuc, int soLuong, String trangThai, String maNV_Tao) {
        this.voucherID = voucherID;
        this.maVoucher = maVoucher;
        this.tenVoucher = tenVoucher;
        this.loaiGiam = loaiGiam;
        this.giaTriGiam = giaTriGiam;
        this.giamToiDa = giamToiDa;
        this.donHangToiThieu = donHangToiThieu;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.soLuong = soLuong;
        this.trangThai = trangThai;
        this.maNV_Tao = maNV_Tao;
    }

    // Getter và Setter
    public int getVoucherID() { return voucherID; }
    public void setVoucherID(int voucherID) { this.voucherID = voucherID; }

    public String getMaVoucher() { return maVoucher; }
    public void setMaVoucher(String maVoucher) { this.maVoucher = maVoucher; }

    public String getTenVoucher() { return tenVoucher; }
    public void setTenVoucher(String tenVoucher) { this.tenVoucher = tenVoucher; }

    public String getLoaiGiam() { return loaiGiam; }
    public void setLoaiGiam(String loaiGiam) { this.loaiGiam = loaiGiam; }

    public double getGiaTriGiam() { return giaTriGiam; }
    public void setGiaTriGiam(double giaTriGiam) { this.giaTriGiam = giaTriGiam; }

    public double getGiamToiDa() { return giamToiDa; }
    public void setGiamToiDa(double giamToiDa) { this.giamToiDa = giamToiDa; }

    public double getDonHangToiThieu() { return donHangToiThieu; }
    public void setDonHangToiThieu(double donHangToiThieu) { this.donHangToiThieu = donHangToiThieu; }

    public Date getNgayBatDau() { return ngayBatDau; }
    public void setNgayBatDau(Date ngayBatDau) { this.ngayBatDau = ngayBatDau; }

    public Date getNgayKetThuc() { return ngayKetThuc; }
    public void setNgayKetThuc(Date ngayKetThuc) { this.ngayKetThuc = ngayKetThuc; }

    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public String getMaNV_Tao() { return maNV_Tao; }
    public void setMaNV_Tao(String maNV_Tao) { this.maNV_Tao = maNV_Tao; }

    @Override
    public String toString() {
        return "Voucher{" +
                "voucherID=" + voucherID +
                ", maVoucher='" + maVoucher + '\'' +
                ", tenVoucher='" + tenVoucher + '\'' +
                ", loaiGiam='" + loaiGiam + '\'' +
                ", giaTriGiam=" + giaTriGiam +
                ", giamToiDa=" + giamToiDa +
                ", donHangToiThieu=" + donHangToiThieu +
                ", ngayBatDau=" + ngayBatDau +
                ", ngayKetThuc=" + ngayKetThuc +
                ", soLuong=" + soLuong +
                ", trangThai='" + trangThai + '\'' +
                ", maNV_Tao='" + maNV_Tao + '\'' +
                '}';
    }
}
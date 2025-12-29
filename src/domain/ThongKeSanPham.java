package domain;

public class ThongKeSanPham {
    private String maSP;
    private String tenSP;
    private String danhMuc;
    private int soLuongBan;
    private double doanhThu;
    private double giaBan;
    private int tonKho;
    private String trangThai;

    public ThongKeSanPham() {
    }

    public ThongKeSanPham(String maSP, String tenSP, String danhMuc, int soLuongBan, 
                         double doanhThu, double giaBan, int tonKho, String trangThai) {
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.danhMuc = danhMuc;
        this.soLuongBan = soLuongBan;
        this.doanhThu = doanhThu;
        this.giaBan = giaBan;
        this.tonKho = tonKho;
        this.trangThai = trangThai;
    }

    // Getter và Setter
    public String getMaSP() { return maSP; }
    public void setMaSP(String maSP) { this.maSP = maSP; }

    public String getTenSP() { return tenSP; }
    public void setTenSP(String tenSP) { this.tenSP = tenSP; }

    public String getDanhMuc() { return danhMuc; }
    public void setDanhMuc(String danhMuc) { this.danhMuc = danhMuc; }

    public int getSoLuongBan() { return soLuongBan; }
    public void setSoLuongBan(int soLuongBan) { this.soLuongBan = soLuongBan; }

    public double getDoanhThu() { return doanhThu; }
    public void setDoanhThu(double doanhThu) { this.doanhThu = doanhThu; }

    public double getGiaBan() { return giaBan; }
    public void setGiaBan(double giaBan) { this.giaBan = giaBan; }

    public int getTonKho() { return tonKho; }
    public void setTonKho(int tonKho) { this.tonKho = tonKho; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    @Override
    public String toString() {
        return "ThongKeSanPham{" +
                "maSP='" + maSP + '\'' +
                ", tenSP='" + tenSP + '\'' +
                ", danhMuc='" + danhMuc + '\'' +
                ", soLuongBan=" + soLuongBan +
                ", doanhThu=" + doanhThu +
                ", giaBan=" + giaBan +
                ", tonKho=" + tonKho +
                ", trangThai='" + trangThai + '\'' +
                '}';
    }
}
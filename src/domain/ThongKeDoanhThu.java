package domain;

/**
 * Domain class cho thống kê doanh thu
 * @author nguye
 */
public class ThongKeDoanhThu {
    private String khoanThoiGian;    // Ngày, tháng, năm
    private double tongDoanhThu;
    private double tongGiamGia;
    private double doanhThuThucTe;
    private int soDonHang;
    private int soDonHangHuy;

    public ThongKeDoanhThu() {
    }

    public ThongKeDoanhThu(String khoanThoiGian, double tongDoanhThu, double tongGiamGia, 
                          double doanhThuThucTe, int soDonHang, int soDonHangHuy) {
        this.khoanThoiGian = khoanThoiGian;
        this.tongDoanhThu = tongDoanhThu;
        this.tongGiamGia = tongGiamGia;
        this.doanhThuThucTe = doanhThuThucTe;
        this.soDonHang = soDonHang;
        this.soDonHangHuy = soDonHangHuy;
    }

    // Getter và Setter
    public String getKhoanThoiGian() { return khoanThoiGian; }
    public void setKhoanThoiGian(String khoanThoiGian) { this.khoanThoiGian = khoanThoiGian; }

    public double getTongDoanhThu() { return tongDoanhThu; }
    public void setTongDoanhThu(double tongDoanhThu) { this.tongDoanhThu = tongDoanhThu; }

    public double getTongGiamGia() { return tongGiamGia; }
    public void setTongGiamGia(double tongGiamGia) { this.tongGiamGia = tongGiamGia; }

    public double getDoanhThuThucTe() { return doanhThuThucTe; }
    public void setDoanhThuThucTe(double doanhThuThucTe) { this.doanhThuThucTe = doanhThuThucTe; }

    public int getSoDonHang() { return soDonHang; }
    public void setSoDonHang(int soDonHang) { this.soDonHang = soDonHang; }

    public int getSoDonHangHuy() { return soDonHangHuy; }
    public void setSoDonHangHuy(int soDonHangHuy) { this.soDonHangHuy = soDonHangHuy; }

    @Override
    public String toString() {
        return "ThongKeDoanhThu{" +
                "khoanThoiGian='" + khoanThoiGian + '\'' +
                ", tongDoanhThu=" + tongDoanhThu +
                ", tongGiamGia=" + tongGiamGia +
                ", doanhThuThucTe=" + doanhThuThucTe +
                ", soDonHang=" + soDonHang +
                ", soDonHangHuy=" + soDonHangHuy +
                '}';
    }
}
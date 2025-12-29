package domain;

public class DanhMuc {
    private String maDanhMuc;
    private String tenDanhMuc;

    public DanhMuc() {}

    public DanhMuc(String maDanhMuc, String tenDanhMuc) {
        this.maDanhMuc = maDanhMuc;
        this.tenDanhMuc = tenDanhMuc;
    }

    public String getMaDanhMuc() { return maDanhMuc; }
    public void setMaDanhMuc(String maDanhMuc) { this.maDanhMuc = maDanhMuc; }
    public String getTenDanhMuc() { return tenDanhMuc; }
    public void setTenDanhMuc(String tenDanhMuc) { this.tenDanhMuc = tenDanhMuc; }
    
    // Backward compatibility methods
    public String getMaDM() { return maDanhMuc; }
    public void setMaDM(String maDM) { this.maDanhMuc = maDM; }
    public String getTenDM() { return tenDanhMuc; }
    public void setTenDM(String tenDM) { this.tenDanhMuc = tenDM; }

    @Override
    public String toString() {
        return tenDanhMuc; // Để hiển thị tên trong ComboBox
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof DanhMuc) {
            return this.maDanhMuc.equals(((DanhMuc)obj).maDanhMuc);
        }
        return false;
    }
}
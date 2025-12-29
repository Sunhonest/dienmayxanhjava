package controller.BaoHanh;

import domain.PhieuBaoHanh;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import model.BaoHanh.BaoHanhDAO;
import view.viewBaoHanh.TraMay;

public class TraMayController implements ActionListener {
    
    private TraMay view;
    private BaoHanhDAO dao;
    private PhieuBaoHanh phieuHienTai; 

    public TraMayController(TraMay view) {
        this.view = view;
        this.dao = new BaoHanhDAO();
        
        view.getBtnCheck().addActionListener(this);
        view.getBtnXacNhan().addActionListener(this);
        
        view.getBtnCheck().setActionCommand("CHECK");
        view.getBtnXacNhan().setActionCommand("TRA_MAY");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd.equals("CHECK")) {
            xuLyCheck();
        } else if (cmd.equals("TRA_MAY")) {
            xuLyTraMay();
        }
    }

    private void xuLyCheck() {
        String ma = view.getMaPhieuInput();
        if (ma.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Vui lòng nhập Mã phiếu!");
            return;
        }

        // Lấy thông tin từ DB
        this.phieuHienTai = dao.getByID(ma);
        
        if (this.phieuHienTai != null) {
            // 1. Đổ dữ liệu vào các ô Form bên trái (Thay vì in ra giấy)
            view.fillFormChiTiet(phieuHienTai);
            
            // Xóa trắng tờ giấy bên phải để chuẩn bị in hóa đơn
            view.setThongTinHienThi(""); 

            // 2. Kiểm tra điều kiện trả máy
            String trangThai = phieuHienTai.getTrangThai();
            
            if (trangThai.equals("DA_XONG")) {
                view.setEnableButtonTra(true);
                JOptionPane.showMessageDialog(view, "Máy đã sửa xong! Hãy kiểm tra thông tin và ấn 'Xác Nhận Trả Khách'.");
            } else if (trangThai.equals("DA_TRA")) {
                view.setEnableButtonTra(false);
                JOptionPane.showMessageDialog(view, "Phiếu này đã hoàn tất trả khách rồi!");
                // Nếu đã trả rồi thì hiện luôn biên bản cũ ra cho xem
                hienThiPhieuTraKhach();
            } else {
                view.setEnableButtonTra(false);
                JOptionPane.showMessageDialog(view, "Máy đang sửa (Trạng thái: " + trangThai + "). Chưa thể trả!");
            }
        } else {
            view.clearForm();
            view.setThongTinHienThi("Không tìm thấy dữ liệu!");
            view.setEnableButtonTra(false);
            JOptionPane.showMessageDialog(view, "Không tìm thấy mã phiếu: " + ma);
        }
    }

    private void xuLyTraMay() {
        if (phieuHienTai == null) return;

        int confirm = JOptionPane.showConfirmDialog(view, 
                "Xác nhận bàn giao thiết bị cho khách hàng?", 
                "Xác nhận trả máy", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            // 1. Cập nhật trạng thái
            phieuHienTai.setTrangThai("DA_TRA");
            
            if (dao.update(phieuHienTai) > 0) {
                // 2. Cập nhật lại Form để thấy trạng thái mới
                view.fillFormChiTiet(phieuHienTai);
                
                // 3. In biên bản bàn giao ra khung bên phải
                hienThiPhieuTraKhach();
                
                JOptionPane.showMessageDialog(view, "Đã trả máy thành công!");
                view.setEnableButtonTra(false);
            } else {
                JOptionPane.showMessageDialog(view, "Lỗi kết nối CSDL!");
            }
        }
    }

    private void hienThiPhieuTraKhach() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String now = sdf.format(new Date());
        
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append("           CỘNG HÒA XÃ HỘI CHỦ NGHĨA VIỆT NAM\n");
        sb.append("              Độc lập - Tự do - Hạnh phúc\n");
        sb.append("              ---------------------------\n\n");
        sb.append("               BIÊN BẢN BÀN GIAO THIẾT BỊ\n\n");
        
        sb.append("  Ngày giờ:      ").append(now).append("\n");
        sb.append("  Mã phiếu:      ").append(phieuHienTai.getMaPhieu()).append("\n");
        sb.append("  Đơn vị:        TRUNG TÂM BẢO HÀNH ĐIỆN MÁY XANH\n\n");
        
        sb.append("  THÔNG TIN KHÁCH HÀNG:\n");
        sb.append("  - Họ tên:      ").append(phieuHienTai.getTenKhachHang()).append("\n");
        sb.append("  - SĐT:         ").append(phieuHienTai.getSoDienThoai()).append("\n\n");
        
        sb.append("  THIẾT BỊ ĐƯỢC BÀN GIAO:\n");
        sb.append("  - Tên máy:     ").append(phieuHienTai.getTenSP()).append("\n");
        sb.append("  - Serial/IMEI: ").append(phieuHienTai.getSoSerial()).append("\n");
        sb.append("  - Lỗi đã sửa:  ").append(phieuHienTai.getMoTaLoi()).append("\n\n");
        
        sb.append("  TÌNH TRẠNG KHI TRẢ: Thiết bị hoạt động tốt.\n");
        sb.append("  CAM KẾT: Khách hàng đã kiểm tra và nhận lại thiết bị.\n\n");
        
        sb.append("  .......................         .......................\n");
        sb.append("      KHÁCH HÀNG KÝ TÊN               NHÂN VIÊN KỸ THUẬT\n");
        
        view.setThongTinHienThi(sb.toString());
    }
}
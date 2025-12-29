package controller.BaoHanh;

import domain.PhieuBaoHanh;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JButton; // Import JButton
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.BaoHanh.BaoHanhDAO;
import view.viewBaoHanh.TraCuuBaoHanh;

public class TraCuuController implements ActionListener {
    
    private TraCuuBaoHanh view;
    private BaoHanhDAO dao;

    public TraCuuController(TraCuuBaoHanh view) {
        this.view = view;
        this.dao = new BaoHanhDAO();
        
        // 1. Gắn sự kiện nút
        this.view.addBtnListener(this);
        
        // 2. Gắn sự kiện click bảng
        this.view.getTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                fillChiTiet();
            }
        });
        
        // 3. Load dữ liệu ban đầu
        loadData(null); 
    }

    // --- HÀM NÀY ĐÃ ĐƯỢC SỬA LẠI ĐỂ HIỆN ĐỦ 8 CỘT ---
    private void loadData(String keyword) {
        DefaultTableModel model = view.getModel();
        model.setRowCount(0); // Xóa dữ liệu cũ
        
        List<PhieuBaoHanh> list;
        if (keyword == null || keyword.isEmpty()) {
            list = dao.getAll(); 
        } else {
            list = dao.search(keyword); 
        }
        
        for (PhieuBaoHanh p : list) {
            // CẦN KHỚP 100% THỨ TỰ CỘT BÊN VIEW:
            // [0]Mã, [1]MãSP, [2]TênSP, [3]Serial, [4]Khách, [5]SĐT, [6]Ngày, [7]Trạng thái
            model.addRow(new Object[]{
                p.getMaPhieu(),
                p.getMaSP(),
                p.getTenSP(),           // <-- (Mới thêm) Tên sản phẩm
                p.getSoSerial(),
                p.getTenKhachHang(),
                p.getSoDienThoai(),
                p.getNgayTiepNhan(),    // <-- (Đã sửa) Ngày nhận sẽ hiện ở đây
                p.getTrangThai()        // <-- (Đã sửa) Trạng thái sẽ hiện ở đây
            });
        }
    }

    private void fillChiTiet() {
        // Nếu đang sửa dở (nút là "Lưu") thì không cho click lung tung
        if (view.getBtnSua().getText().equals("Lưu")) return;

        int row = view.getTable().getSelectedRow();
        if (row >= 0) {
            String maPhieu = view.getTable().getValueAt(row, 0).toString();
            // Gọi lại DB lấy full thông tin để hiển thị chi tiết
            PhieuBaoHanh p = dao.getByID(maPhieu); 
            if (p != null) {
                // Fix lỗi nếu DAO chưa lấy được tên SP
                if (p.getTenSP() == null || p.getTenSP().isEmpty()) {
                     Object objTen = view.getTable().getValueAt(row, 2);
                     p.setTenSP(objTen != null ? objTen.toString() : "");
                }
                view.setThongTinChiTiet(p);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        switch(cmd) {
            case "Tìm kiếm": 
                String tuKhoa = view.getTuKhoa();
                loadData(tuKhoa);
                break;
                
            case "Thêm": 
                JOptionPane.showMessageDialog(view, "Vui lòng sang tab 'Tiếp nhận' để thêm mới!"); 
                break;
                
            case "Sửa": 
            case "Lưu": 
                xuLySuaTrucTiep(); // Gọi hàm sửa trực tiếp
                break;
                
            case "Xóa": 
                xuLyXoa(); 
                break;
        }
    }
    
    // --- LOGIC SỬA TRỰC TIẾP (Biến nút Sửa -> Lưu) ---
    private void xuLySuaTrucTiep() {
        if (view.getTxtMaPhieu().getText().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn phiếu cần sửa!");
            return;
        }

        JButton btn = view.getBtnSua();
        
        if (btn.getText().equals("Sửa")) {
            // Chế độ Sửa: Mở khóa ô nhập
            view.batCheDoSua(true); 
            view.setBtnSuaText("Lưu"); 
        } else {
            // Chế độ Lưu: Cập nhật vào DB
            PhieuBaoHanh p = view.getPhieuCapNhat();
            if (dao.update(p) > 0) {
                JOptionPane.showMessageDialog(view, "Cập nhật thành công!");
                view.batCheDoSua(false); // Khóa lại
                view.setBtnSuaText("Sửa"); 
                
                // Load lại bảng để thấy thay đổi (Ngày, Trạng thái...)
                loadData(view.getTuKhoa()); 
                
                // Cập nhật lại chi tiết
                PhieuBaoHanh pNew = dao.getByID(p.getMaPhieu());
                if(pNew != null) view.setThongTinChiTiet(pNew);
            } else {
                JOptionPane.showMessageDialog(view, "Cập nhật thất bại!");
            }
        }
    }

    private void xuLyXoa() {
        int row = view.getTable().getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(view, "Chọn phiếu cần xóa!");
            return;
        }
        String maPhieu = view.getTable().getValueAt(row, 0).toString();
        if(JOptionPane.showConfirmDialog(view, "Xóa phiếu " + maPhieu + "?", "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
            if(dao.delete(maPhieu) > 0) {
                JOptionPane.showMessageDialog(view, "Đã xóa!");
                loadData(null);
                // Clear form
                view.getTxtMaPhieu().setText("");
            }
        }
    }
}
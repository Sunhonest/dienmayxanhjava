package controller.Kho;

import domain.Kho.DanhMuc;
import model.Kho.DanhMucDAO;
import view.viewKho.QuanLyDanhMuc;
import java.awt.event.*;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class DanhMucController implements ActionListener {
    private QuanLyDanhMuc view;
    private DanhMucDAO dmDao;
    private List<DanhMuc> listCache;

    public DanhMucController(QuanLyDanhMuc view) {
        this.view = view;
        this.dmDao = new DanhMucDAO();
        
        view.addActionListener(this);
        
        // Sự kiện click vào bảng
        view.getTable().addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = view.getTable().getSelectedRow();
                if(row >= 0) {
                    String maDM = view.getTable().getValueAt(row, 0).toString();
                    // Tìm trong cache để fill lại form chính xác
                    for(DanhMuc dm : listCache) {
                        if(dm.getMaDanhMuc().equals(maDM)) {
                            view.fillForm(dm); break;
                        }
                    }
                }
            }
        });
        
        loadData();
    }

    private void loadData() {
        listCache = dmDao.getAll();
        DefaultTableModel model = view.getModel();
        model.setRowCount(0);
        for(DanhMuc dm : listCache) {
            model.addRow(new Object[]{ dm.getMaDanhMuc(), dm.getTenDanhMuc() });
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        try {
            switch(cmd) {
                case "Them": 
                    DanhMuc dmNew = view.getDanhMucInput();
                    if(dmNew == null) {
                        JOptionPane.showMessageDialog(view, "Vui lòng nhập đầy đủ thông tin!"); return;
                    }
                    if(dmDao.checkTrungMa(dmNew.getMaDanhMuc())) {
                        JOptionPane.showMessageDialog(view, "Mã danh mục đã tồn tại!"); return;
                    }
                    if(dmDao.insert(dmNew) > 0) {
                        JOptionPane.showMessageDialog(view, "Thêm thành công!");
                        loadData(); view.resetForm();
                    }
                    break;
                    
                case "Sua": 
                    DanhMuc dmEdit = view.getDanhMucInput();
                    if(dmEdit == null) return;
                    if(dmDao.update(dmEdit) > 0) {
                        JOptionPane.showMessageDialog(view, "Cập nhật thành công!");
                        loadData(); view.resetForm();
                    }
                    break;
                    
                case "Xoa": 
                    String maXoa = view.getMaDMChon();
                    if (maXoa == null) {
                        JOptionPane.showMessageDialog(view, "Chưa chọn danh mục để xóa!");
                        return;
                    }
                    // Cảnh báo kỹ vì nếu xóa danh mục đang có sản phẩm sẽ lỗi FK
                    if(JOptionPane.showConfirmDialog(view, 
                        "Xóa danh mục này có thể ảnh hưởng đến các sản phẩm thuộc về nó.\nBạn chắc chắn chứ?", 
                        "Cảnh báo", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        
                        int result = dmDao.delete(maXoa);
                        if(result > 0) {
                            JOptionPane.showMessageDialog(view, "Đã xóa thành công!");
                            loadData(); 
                            view.resetForm();
                        } else {
                            JOptionPane.showMessageDialog(view, "Xóa thất bại! Có thể danh mục này đang chứa sản phẩm.");
                        }
                    }
                    break;
                    
                case "LamMoi": 
                    view.resetForm(); loadData(); 
                    break;
            }
        } catch (Exception ex) { ex.printStackTrace(); }
    }
}
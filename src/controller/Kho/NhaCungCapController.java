package controller.Kho;

import domain.Kho.NhaCungCap;
import model.Kho.NhaCungCapDAO;
import view.viewKho.QuanLyNhaCungCap;
import java.awt.event.*;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class NhaCungCapController implements ActionListener {
    private QuanLyNhaCungCap view;
    private NhaCungCapDAO nccDao;
    private List<NhaCungCap> listCache;

    public NhaCungCapController(QuanLyNhaCungCap view) {
        this.view = view;
        this.nccDao = new NhaCungCapDAO();
        
        view.addActionListener(this);
        
        // Sự kiện click vào bảng
        view.getTable().addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = view.getTable().getSelectedRow();
                if(row >= 0) {
                    String maNCC = view.getTable().getValueAt(row, 0).toString();
                    // Tìm trong cache để fill lại form
                    for(NhaCungCap ncc : listCache) {
                        if(ncc.getMaNCC().equals(maNCC)) {
                            view.fillForm(ncc); break;
                        }
                    }
                }
            }
        });
        
        loadData();
    }

    private void loadData() {
        listCache = nccDao.getAll();
        DefaultTableModel model = view.getModel();
        model.setRowCount(0);
        for(NhaCungCap ncc : listCache) {
            model.addRow(new Object[]{ 
                ncc.getMaNCC(), 
                ncc.getTenNCC(),
                ncc.getDiaChi(),
                ncc.getSdt()
            });
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        try {
            switch(cmd) {
                case "Them": 
                    NhaCungCap nccNew = view.getNhaCungCapInput();
                    if(nccNew == null) {
                        JOptionPane.showMessageDialog(view, "Vui lòng nhập Mã và Tên NCC!"); return;
                    }
                    if(nccDao.checkTrungMa(nccNew.getMaNCC())) {
                        JOptionPane.showMessageDialog(view, "Mã nhà cung cấp đã tồn tại!"); return;
                    }
                    if(nccDao.insert(nccNew) > 0) {
                        JOptionPane.showMessageDialog(view, "Thêm thành công!");
                        loadData(); view.resetForm();
                    }
                    break;
                    
                case "Sua": 
                    NhaCungCap nccEdit = view.getNhaCungCapInput();
                    if(nccEdit == null) return;
                    if(nccDao.update(nccEdit) > 0) {
                        JOptionPane.showMessageDialog(view, "Cập nhật thành công!");
                        loadData(); view.resetForm();
                    }
                    break;
                    
                case "Xoa": 
                    String maXoa = view.getMaNCCChon();
                    if (maXoa == null) {
                        JOptionPane.showMessageDialog(view, "Chưa chọn nhà cung cấp để xóa!");
                        return;
                    }
                    if(JOptionPane.showConfirmDialog(view, 
                        "Xóa nhà cung cấp này có thể ảnh hưởng đến phiếu nhập/sản phẩm.\nBạn chắc chắn chứ?", 
                        "Cảnh báo", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        
                        int result = nccDao.delete(maXoa);
                        if(result > 0) {
                            JOptionPane.showMessageDialog(view, "Đã xóa thành công!");
                            loadData(); 
                            view.resetForm();
                        } else {
                            JOptionPane.showMessageDialog(view, "Xóa thất bại! Có thể NCC này đang có dữ liệu liên kết.");
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
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller.Kho;

/**
 *
 * @author nguye
 */
import domain.DanhMuc;
import domain.SanPham;
import model.Kho.DanhMucDAO;
import model.Kho.SanPhamDAO;
import view.viewKho.QuanLySanPham;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class SanPhamController implements ActionListener {
    private QuanLySanPham view;
    private SanPhamDAO spDao;
    private DanhMucDAO dmDao;
    private List<SanPham> listCache;

    public SanPhamController(QuanLySanPham view) {
        this.view = view;
        this.spDao = new SanPhamDAO();
        this.dmDao = new DanhMucDAO();
        
        view.addActionListener(this);
        view.getTable().addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = view.getTable().getSelectedRow();
                if(row >= 0) {
                    String maSP = view.getTable().getValueAt(row, 0).toString();
                    for(SanPham sp : listCache) {
                        if(sp.getMaSP().equals(maSP)) {
                            view.fillForm(sp); break;
                        }
                    }
                }
            }
        });
        
        loadDanhMuc();
        loadData();
    }
    
    private void loadDanhMuc() {
        List<DanhMuc> listDM = dmDao.getAll();
        view.setDuLieuDanhMuc(listDM);
    }

    private void loadData() {
        listCache = spDao.getAll();
        DefaultTableModel model = view.getModel();
        model.setRowCount(0);
        DecimalFormat df = new DecimalFormat("#,###");
        
        for(SanPham sp : listCache) {
            String tenDM = sp.getMaDanhMuc(); // Tạm thời hiện mã, nếu muốn hiện tên phải map từ listDM
            model.addRow(new Object[]{
                sp.getMaSP(), sp.getTenSP(), tenDM, sp.getThuongHieu(),
                sp.getTonKho(), sp.getDonViTinh(), df.format(sp.getGiaBan())
            });
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        try {
            switch(cmd) {
                case "Them": 
                    SanPham sp = view.getSanPhamInput();
                    if(spDao.checkTrungMa(sp.getMaSP())) {
                        JOptionPane.showMessageDialog(view, "Mã SP đã tồn tại!"); return;
                    }
                    if(spDao.insert(sp) > 0) {
                        JOptionPane.showMessageDialog(view, "Thêm thành công!");
                        loadData(); view.resetForm();
                    }
                    break;
                case "Sua": 
                    SanPham spSua = view.getSanPhamInput();
                    if(spDao.update(spSua) > 0) {
                        JOptionPane.showMessageDialog(view, "Cập nhật thành công!");
                        loadData(); view.resetForm();
                    }
                    break;
                case "Xoa": 
                    String maXoa = view.getMaSPChon();
                    if(JOptionPane.showConfirmDialog(view, "Xóa sản phẩm này?") == JOptionPane.YES_OPTION) {
                        if(spDao.delete(maXoa) > 0) {
                            JOptionPane.showMessageDialog(view, "Đã xóa!");
                            loadData(); view.resetForm();
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

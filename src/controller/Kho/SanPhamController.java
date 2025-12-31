package controller.Kho;

import domain.Kho.DanhMuc;
import domain.Kho.SanPham;
import model.Kho.DanhMucDAO;
import model.Kho.SanPhamDAO;
import view.viewKho.QuanLySanPham;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class SanPhamController implements ActionListener, KeyListener {
    private QuanLySanPham view;
    private SanPhamDAO spDao;
    private DanhMucDAO dmDao;
    private List<SanPham> listCache; // Danh sách gốc từ DB
    private List<SanPham> listDisplay; // Danh sách đang hiển thị (sau khi lọc)

    public SanPhamController(QuanLySanPham view) {
        this.view = view;
        this.spDao = new SanPhamDAO();
        this.dmDao = new DanhMucDAO();
        
        // Đăng ký listener
        view.addActionListener(this);
        view.addKeyListener(this); // Lắng nghe gõ phím tìm kiếm
        
        view.getTable().addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = view.getTable().getSelectedRow();
                if(row >= 0) {
                    // Lấy mã SP từ bảng (cần lưu ý khi bảng đã bị lọc thì row index vẫn đúng với dữ liệu trên bảng)
                    String maSP = view.getTable().getValueAt(row, 0).toString();
                    // Tìm trong listCache để fill form (chính xác hơn tìm trong bảng)
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

    // Tải dữ liệu gốc từ Database
    private void loadData() {
        listCache = spDao.getAll();
        filterAndDisplay(); // Gọi hàm lọc để hiển thị
    }

    // Hàm lọc dữ liệu và hiển thị lên bảng
    private void filterAndDisplay() {
        String keyword = view.getKeyword();
        Object selectedDM = view.getDanhMucFilter();
        
        listDisplay = new ArrayList<>();
        DefaultTableModel model = view.getModel();
        model.setRowCount(0);
        DecimalFormat df = new DecimalFormat("#,###");

        for(SanPham sp : listCache) {
            boolean matchKeyword = false;
            boolean matchDanhMuc = false;

            // 1. Kiểm tra Keyword (Tìm theo Mã hoặc Tên)
            if(keyword.isEmpty() || sp.getMaSP().toLowerCase().contains(keyword) || sp.getTenSP().toLowerCase().contains(keyword)) {
                matchKeyword = true;
            }

            // 2. Kiểm tra Danh mục
            if(selectedDM == null || selectedDM.toString().equals("Tất cả")) {
                matchDanhMuc = true;
            } else if (selectedDM instanceof DanhMuc) {
                DanhMuc dm = (DanhMuc) selectedDM;
                if(sp.getMaDanhMuc().equals(dm.getMaDM())) {
                    matchDanhMuc = true;
                }
            }

            // Nếu thỏa cả 2 điều kiện -> Thêm vào bảng
            if(matchKeyword && matchDanhMuc) {
                listDisplay.add(sp);
                model.addRow(new Object[]{
                    sp.getMaSP(), 
                    sp.getHinhAnh(), 
                    sp.getTenSP(), 
                    sp.getMaDanhMuc(), 
                    sp.getThuongHieu(),
                    sp.getTonKho(), 
                    sp.getDonViTinh(), 
                    df.format(sp.getGiaBan())
                });
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        // Xử lý sự kiện từ combobox lọc (khi chọn item thì lọc luôn)
        if(e.getSource() == view.getDanhMucFilter() || cmd.equals("TimKiem")) {
             filterAndDisplay();
             return;
        }

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
                    if (maXoa == null) {
                        JOptionPane.showMessageDialog(view, "Chưa chọn sản phẩm để xóa!");
                        return;
                    }
                    if(JOptionPane.showConfirmDialog(view, "Bạn có chắc muốn xóa vĩnh viễn sản phẩm này khỏi CSDL?") == JOptionPane.YES_OPTION) {
                        int result = spDao.delete(maXoa);
                        if(result > 0) {
                            JOptionPane.showMessageDialog(view, "Đã xóa thành công!");
                            loadData(); 
                            view.resetForm();
                        } else {
                            JOptionPane.showMessageDialog(view, "Xóa thất bại! Có thể do ràng buộc dữ liệu.");
                        }
                    }
                    break;
                case "LamMoi": 
                    view.resetForm(); 
                    loadDanhMuc();
                    loadData(); 
                    break;
            }
        } catch (Exception ex) { ex.printStackTrace(); }
    }

    // --- KeyListener cho ô tìm kiếm ---
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {
        // Tìm kiếm ngay khi nhả phím (Real-time search)
        filterAndDisplay();
    }
}
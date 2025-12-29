package controller.KhuyenMai;

import view.viewKhuyenMai.QuanLyVoucher;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * UI Controller cho QuanLyVoucher - Gắn sự kiện nút bấm
 */
public class VoucherUIController implements ActionListener {
    private QuanLyVoucher view;
    private VoucherController voucherController;
    
    public VoucherUIController(QuanLyVoucher view) {
        this.view = view;
        this.voucherController = new VoucherController();
        attachEventListeners();
    }
    
    private void attachEventListeners() {
        view.addActionListener(this);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        
        switch (command) {
            case "Them":
                view.enterThemMode();
                break;
            case "Sua":
                view.enterSuaMode();
                break;
            case "Xoa":
                view.xoa();
                break;
            case "LamMoi":
                view.resetForm();
                view.loadData();
                break;
            case "KichHoat":
                view.kichHoat();
                break;
            case "Luu":
                view.luu();
                break;
            case "Huy":
                view.cancelEdit();
                break;
            default:
                break;
        }
    }
}

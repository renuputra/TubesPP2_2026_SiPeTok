package id.ac.unpas.sipetok.view;

import id.ac.unpas.sipetok.controller.PelangganController;
import id.ac.unpas.sipetok.model.Pelanggan;
import id.ac.unpas.sipetok.view.MenuUtama;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class FormPelanggan extends JFrame {

    JTextField txtNama = new JTextField();
    JTextField txtNoHp = new JTextField();
    JTextField txtAlamat = new JTextField();

    JTable table = new JTable();
    PelangganController controller = new PelangganController();
    int selectedId = -1;

    public FormPelanggan() {
        setTitle("Data Pelanggan");
        setSize(600, 400);
        setLocationRelativeTo(null);

        JPanel panelForm = new JPanel(new GridLayout(6, 2, 5, 5));
        panelForm.setBorder(BorderFactory.createTitledBorder("Input Data Pelanggan"));

        panelForm.add(new JLabel("Nama"));
        panelForm.add(txtNama);
        panelForm.add(new JLabel("No Hp"));
        panelForm.add(txtNoHp);
        panelForm.add(new JLabel("Alamat"));
        panelForm.add(txtAlamat);

        JButton btnSimpan = new JButton("Simpan");
        JButton btnUpdate = new JButton("Update");
        JButton btnHapus = new JButton("Hapus");

        btnSimpan.addActionListener(e -> simpan());
        btnUpdate.addActionListener(e -> update());
        btnHapus.addActionListener(e -> hapus());
        
        JButton btnKembali = new JButton("Kembali");

        btnKembali.addActionListener(e -> {
            dispose();                       // tutup form barang
            new MenuUtama().setVisible(true); // kembali ke menu utama
        });


        panelForm.add(btnSimpan);
        panelForm.add(btnUpdate);
        panelForm.add(btnHapus);
        panelForm.add(btnKembali);


        JScrollPane sp = new JScrollPane(table);
        tampilData();

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                selectedId = Integer.parseInt(table.getValueAt(row, 0).toString());
                txtNama.setText(table.getValueAt(row, 1).toString());
                txtNoHp.setText(table.getValueAt(row, 2).toString());
                txtAlamat.setText(table.getValueAt(row, 3).toString());
            }
        });

        setLayout(new BorderLayout(5, 5));
        add(panelForm, BorderLayout.NORTH);
        add(sp, BorderLayout.CENTER);
    }

    private void tampilData() {
        DefaultTableModel model = new DefaultTableModel(
                new String[]{"ID", "Nama", "No Hp", "Alamat"}, 0
        );
        for (Pelanggan p : controller.tampil()) {
            model.addRow(new Object[]{
                p.getIdPelanggan(),
                p.getNamaPelanggan(),
                p.getAlamat(),
                p.getNoHp()
            });
        }
        table.setModel(model);
    }

    private void simpan() {
        if (!validasiInput()) return;   // ⬅️ VALIDASI DI SINI

        Pelanggan p = new Pelanggan();
        p.setNamaPelanggan(txtNama.getText());
        p.setAlamat(txtNoHp.getText());
        p.setNoHp(txtAlamat.getText());

        controller.simpan(p);
        reset();
        tampilData();
    }

    private void update() {
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data terlebih dahulu");
            return;
        }

        if (!validasiInput()) return;   // ⬅️ VALIDASI DI SINI

        Pelanggan p = new Pelanggan();
        p.setIdPelanggan(selectedId);
        p.setNamaPelanggan(txtNama.getText());
        p.setAlamat(txtNoHp.getText());
        p.setNoHp(txtAlamat.getText());

        controller.update(p);
        reset();
        tampilData();
    }


    private void hapus() {
    if (selectedId == -1) {
        JOptionPane.showMessageDialog(this, "Pilih data terlebih dahulu!");
        return;
    }

    int confirm = JOptionPane.showConfirmDialog(
            this,
            "Yakin ingin menghapus data?",
            "Konfirmasi",
            JOptionPane.YES_NO_OPTION
    );

    if (confirm == JOptionPane.YES_OPTION) {
        controller.hapus(selectedId);
        reset();
        tampilData();
        JOptionPane.showMessageDialog(this, "Data berhasil dihapus");
    }
    }
    
    private boolean validasiInput() {
    if (txtNama.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Nama pelanggan tidak boleh kosong");
        txtNama.requestFocus();
        return false;
    }

    if (txtAlamat.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Alamat tidak boleh kosong");
        txtAlamat.requestFocus();
        return false;
    }

    if (txtNoHp.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "No HP tidak boleh kosong");
        txtNoHp.requestFocus();
        return false;
    }

    if (!txtNoHp.getText().matches("\\d+")) {
        JOptionPane.showMessageDialog(this, "No HP harus berupa angka");
        txtNoHp.requestFocus();
        return false;
    }

    if (txtNoHp.getText().length() < 10 || txtNoHp.getText().length() > 13) {
        JOptionPane.showMessageDialog(this, "No HP harus 10–13 digit");
        txtNoHp.requestFocus();
        return false;
    }

    return true;
}



    private void reset() {
        txtNama.setText("");
        txtAlamat.setText("");
        txtNoHp.setText("");
        selectedId = -1;
    }
}

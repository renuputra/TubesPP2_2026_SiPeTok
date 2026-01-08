package id.ac.unpas.sipetok.view;

import id.ac.unpas.sipetok.controller.BarangController;
import id.ac.unpas.sipetok.model.Barang;
import id.ac.unpas.sipetok.view.MenuUtama;
import java.sql.ResultSet;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class FormBarang extends JFrame {

    JTextField txtNama = new JTextField();
    JTextField txtHarga = new JTextField();
    JTextField txtStok = new JTextField();

    JTable table = new JTable();
    BarangController controller = new BarangController();
    
    int selectedId = -1;


    public FormBarang() {
        setTitle("Data Barang");
        setSize(600, 400);
        setLocationRelativeTo(null);

        // ===== PANEL FORM =====
        JPanel panelForm = new JPanel(new GridLayout(7, 2, 5, 5));
        panelForm.setBorder(BorderFactory.createTitledBorder("Input Data Barang"));

        panelForm.add(new JLabel("Nama Barang"));
        panelForm.add(txtNama);

        panelForm.add(new JLabel("Harga"));
        panelForm.add(txtHarga);

        panelForm.add(new JLabel("Stok"));
        panelForm.add(txtStok);

        JButton btnSimpan = new JButton("Simpan");
        JButton btnUpdate = new JButton("Update");
        JButton btnHapus = new JButton("Hapus");
        JButton btnKembali = new JButton("Kembali");

        btnKembali.addActionListener(e -> {
            dispose();                       // tutup form barang
            new MenuUtama().setVisible(true); // kembali ke menu utama
        });

        
        btnSimpan.addActionListener(e -> simpanData());
        btnUpdate.addActionListener(e -> updateData());
        btnHapus.addActionListener(e -> hapusData());

        panelForm.add(new JLabel(""));
        panelForm.add(btnSimpan);
        panelForm.add(btnUpdate);
        panelForm.add(btnHapus);
        panelForm.add(btnKembali);


        // ===== TABEL =====
        JScrollPane sp = new JScrollPane(table);
        tampilData();
        
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                selectedId = Integer.parseInt(table.getValueAt(row, 0).toString());
                txtNama.setText(table.getValueAt(row, 1).toString());
                txtHarga.setText(table.getValueAt(row, 2).toString());
                txtStok.setText(table.getValueAt(row, 3).toString());
            }
        });

        // ===== LAYOUT =====
        setLayout(new BorderLayout(5, 5));
        add(panelForm, BorderLayout.NORTH);
        add(sp, BorderLayout.CENTER);
    }

    private void simpanData() {
        try {
            Barang b = new Barang();
            b.setNamaBarang(txtNama.getText());
            b.setHarga(Integer.parseInt(txtHarga.getText()));
            b.setStok(Integer.parseInt(txtStok.getText()));

            controller.simpan(b);

            txtNama.setText("");
            txtHarga.setText("");
            txtStok.setText("");

            tampilData();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Harga dan stok harus berupa angka");
        }
    }
    
        private void updateData() {
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data terlebih dahulu");
            return;
        }

        try {
            Barang b = new Barang();
            b.setIdBarang(selectedId);
            b.setNamaBarang(txtNama.getText());
            b.setHarga(Integer.parseInt(txtHarga.getText()));
            b.setStok(Integer.parseInt(txtStok.getText()));

            controller.update(b);
            resetForm();
            tampilData();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Harga dan stok harus angka");
        }
    }
        
        private void hapusData() {
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data terlebih dahulu");
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
            resetForm();
            tampilData();
        }
    }
        
        private void resetForm() {
        txtNama.setText("");
        txtHarga.setText("");
        txtStok.setText("");
        selectedId = -1;
    }





    private void tampilData() {
        DefaultTableModel model = new DefaultTableModel(
                new String[]{"ID", "Nama", "Harga", "Stok"}, 0);

        try {
            ResultSet rs = controller.tampil();
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id_barang"),
                    rs.getString("nama_barang"),
                    rs.getInt("harga"),
                    rs.getInt("stok")
                });
            }
            table.setModel(model);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
    
    
    
    
}

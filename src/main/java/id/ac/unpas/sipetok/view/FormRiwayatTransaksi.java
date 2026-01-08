package id.ac.unpas.sipetok.view;

import id.ac.unpas.sipetok.controller.TransaksiController;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;

public class FormRiwayatTransaksi extends JFrame {

    private JTable table;
    private TransaksiController controller;

    public FormRiwayatTransaksi() {
        controller = new TransaksiController();
        table = new JTable();

        setTitle("Riwayat Transaksi");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // ===== TABLE =====
        JScrollPane sp = new JScrollPane(table);
        tampilData();

        // ===== BUTTON PANEL =====
        JButton btnUbah = new JButton("Ubah");
        JButton btnHapus = new JButton("Hapus");

        JPanel panelButton = new JPanel();
        panelButton.add(btnUbah);
        panelButton.add(btnHapus);

        add(sp, BorderLayout.CENTER);
        add(panelButton, BorderLayout.SOUTH);

        // ===== EVENT =====
        btnHapus.addActionListener(e -> hapusData());
        btnUbah.addActionListener(e -> ubahData());
    }

    // =======================
    // TAMPIL DATA KE TABLE
    // =======================
    private void tampilData() {
        DefaultTableModel model = new DefaultTableModel(
            new String[]{"ID Transaksi", "Pelanggan", "Tanggal", "Total"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // tabel readonly
            }
        };

        try {
            ResultSet rs = controller.getRiwayatTransaksi();
            while (rs != null && rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id_transaksi"),
                    rs.getString("nama_pelanggan"),
                    rs.getDate("tanggal"),
                    rs.getInt("total")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        table.setModel(model);
        table.setRowHeight(25);
        table.getTableHeader().setReorderingAllowed(false);
    }

    // =======================
    // HAPUS DATA
    // =======================
    private void hapusData() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data dulu!");
            return;
        }

        int id = Integer.parseInt(
            table.getValueAt(row, 0).toString()
        );

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Yakin ingin menghapus transaksi ini?",
            "Konfirmasi",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            controller.hapusTransaksi(id);
            tampilData(); // refresh table
        }
    }

    // =======================
    // UBAH DATA
    // =======================
    private void ubahData() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data dulu!");
            return;
        }

        int id = Integer.parseInt(
            table.getValueAt(row, 0).toString()
        );

        // buka form transaksi dalam mode edit
        FormTransaksi form = new FormTransaksi(id);
        form.setVisible(true);
    }
}

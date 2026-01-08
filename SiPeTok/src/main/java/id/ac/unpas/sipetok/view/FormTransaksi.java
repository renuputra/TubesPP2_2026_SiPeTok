package id.ac.unpas.sipetok.view;

import id.ac.unpas.sipetok.controller.TransaksiController;
import id.ac.unpas.sipetok.model.DetailTransaksi;
import id.ac.unpas.sipetok.model.Transaksi;
import id.ac.unpas.sipetok.view.MenuUtama;
import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.util.Map;

public class FormTransaksi extends JFrame {

    // ===== COMPONENT =====
    private JComboBox<String> cmbPelanggan;
    private JComboBox<String> cmbBarang;
    private JTextField txtHarga;
    private JTextField txtQty;
    private JTextField txtTotal;
    private JButton btnHitung;
    private JButton btnSimpan;

    // ===== CONTROLLER =====
    private TransaksiController controller = new TransaksiController();

    // ===== DATA MAP =====
    private Map<Integer, String> pelangganMap;
    private Map<Integer, String> barangMap;

    // ===== EDIT MODE =====
    private boolean isEdit = false;
    private int idTransaksiEdit = 0;

    // =============================
    // CONSTRUCTOR TAMBAH TRANSAKSI
    // =============================
    public FormTransaksi() {
        initUI();
        loadCombo();
    }

    // =============================
    // CONSTRUCTOR EDIT TRANSAKSI
    // =============================
    public FormTransaksi(int idTransaksi) {
        initUI();
        loadCombo();
        loadDataEdit(idTransaksi);
        isEdit = true;
        idTransaksiEdit = idTransaksi;
    }

    // =============================
    // UI
    // =============================
    private void initUI() {
        setTitle("Form Transaksi");
        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        setLayout(new GridLayout(7, 2, 10, 10));

        cmbPelanggan = new JComboBox<>();
        cmbBarang = new JComboBox<>();
        txtHarga = new JTextField();
        txtQty = new JTextField();
        txtTotal = new JTextField();
        txtTotal.setEditable(false);

        btnHitung = new JButton("Hitung Total");
        btnSimpan = new JButton("Simpan Transaksi");
        
        JButton btnKembali = new JButton("Kembali");

        btnKembali.addActionListener(e -> {
            dispose();                       // tutup form barang
            new MenuUtama().setVisible(true); // kembali ke menu utama
        });

        add(new JLabel("Pelanggan"));
        add(cmbPelanggan);
        add(new JLabel("Barang"));
        add(cmbBarang);
        add(new JLabel("Harga"));
        add(txtHarga);
        add(new JLabel("Qty"));
        add(txtQty);
        add(new JLabel("Total"));
        add(txtTotal);
        add(btnHitung);
        add(btnSimpan);
        add(btnKembali);

        // ===== EVENT =====
        cmbBarang.addActionListener(e -> loadHarga());
        btnHitung.addActionListener(e -> hitungTotal());
        btnSimpan.addActionListener(e -> simpan());
    }

    // =============================
    // LOAD COMBOBOX
    // =============================
    private void loadCombo() {
        pelangganMap = controller.getPelanggan();
        barangMap = controller.getBarang();

        for (String nama : pelangganMap.values()) {
            cmbPelanggan.addItem(nama);
        }

        for (String nama : barangMap.values()) {
            cmbBarang.addItem(nama);
        }
    }

    // =============================
    // LOAD HARGA BARANG
    // =============================
    private void loadHarga() {
        int index = cmbBarang.getSelectedIndex();
        if (index == -1) return;

        int idBarang = (int) barangMap.keySet().toArray()[index];
        int harga = controller.getHargaBarang(idBarang);
        txtHarga.setText(String.valueOf(harga));
    }

    // =============================
    // HITUNG TOTAL
    // =============================
    private void hitungTotal() {
        try {
            int harga = Integer.parseInt(txtHarga.getText());
            int qty = Integer.parseInt(txtQty.getText());
            txtTotal.setText(String.valueOf(harga * qty));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Qty / Harga tidak valid");
        }
    }

    // =============================
    // SIMPAN / UPDATE TRANSAKSI
    // =============================
    private void simpan() {
        try {
            int idPelanggan =
                (int) pelangganMap.keySet().toArray()[cmbPelanggan.getSelectedIndex()];
            int idBarang =
                (int) barangMap.keySet().toArray()[cmbBarang.getSelectedIndex()];
            int harga = Integer.parseInt(txtHarga.getText());
            int qty = Integer.parseInt(txtQty.getText());
            int total = Integer.parseInt(txtTotal.getText());

            Transaksi t = new Transaksi();
            t.setIdPelanggan(idPelanggan);
            t.setTanggal(new Date(System.currentTimeMillis()));
            t.setTotal(total);

            DetailTransaksi d = new DetailTransaksi();
            d.setIdBarang(idBarang);
            d.setQty(qty);
            d.setHarga(harga);
            d.setSubtotal(total);

            if (!isEdit) {
                controller.simpanTransaksi(t, d);
                JOptionPane.showMessageDialog(this, "Transaksi berhasil disimpan");
            } else {
                JOptionPane.showMessageDialog(this,
                    "Mode edit aktif (updateTransaksi bisa ditambahkan)");
            }

            dispose();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Data belum lengkap");
            e.printStackTrace();
        }
    }

    // =============================
    // LOAD DATA SAAT EDIT
    // =============================
    private void loadDataEdit(int idTransaksi) {
        Transaksi t = controller.getTransaksiById(idTransaksi);
        DetailTransaksi d = controller.getDetailByTransaksiId(idTransaksi);

        if (t == null || d == null) return;

        cmbPelanggan.setSelectedIndex(
            new java.util.ArrayList<>(pelangganMap.keySet())
                .indexOf(t.getIdPelanggan())
        );

        cmbBarang.setSelectedIndex(
            new java.util.ArrayList<>(barangMap.keySet())
                .indexOf(d.getIdBarang())
        );

        txtHarga.setText(String.valueOf(d.getHarga()));
        txtQty.setText(String.valueOf(d.getQty()));
        txtTotal.setText(String.valueOf(t.getTotal()));
    }
}

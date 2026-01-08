package id.ac.unpas.sipetok.view;

import javax.swing.*;
import java.awt.*;

public class MenuUtama extends JFrame {

    public MenuUtama() {
        setTitle("SiPeTok - Sistem Penjualan Toko");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLabel lblTitle = new JLabel("MENU UTAMA", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));

        JButton btnBarang = new JButton("Data Barang");
        JButton btnPelanggan = new JButton("Data Pelanggan");
        JButton btnTransaksi = new JButton("Transaksi Penjualan");
        JButton btnRiwayat = new JButton("Riwayat Transaksi");
        JButton btnKeluar = new JButton("Keluar");

        btnBarang.setFont(new Font("Arial", Font.PLAIN, 14));
        btnPelanggan.setFont(new Font("Arial", Font.PLAIN, 14));
        btnKeluar.setFont(new Font("Arial", Font.PLAIN, 14));

        btnBarang.addActionListener(e -> {
            new FormBarang().setVisible(true);
        });

        btnPelanggan.addActionListener(e -> {
            new FormPelanggan().setVisible(true);
        });
        
        btnTransaksi.addActionListener(e -> {
            new FormTransaksi().setVisible(true);
        });
        
        btnRiwayat.addActionListener(e -> {
            new FormRiwayatTransaksi().setVisible(true);
        });

        btnKeluar.addActionListener(e -> {
            System.exit(0);
        });

        JPanel panelButton = new JPanel(new GridLayout(3, 1, 10, 10));
        panelButton.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        panelButton.add(btnBarang);
        panelButton.add(btnPelanggan);
        panelButton.add(btnTransaksi);
        panelButton.add(btnRiwayat);
        panelButton.add(btnKeluar);

        setLayout(new BorderLayout());
        add(lblTitle, BorderLayout.NORTH);
        add(panelButton, BorderLayout.CENTER);
    }
}

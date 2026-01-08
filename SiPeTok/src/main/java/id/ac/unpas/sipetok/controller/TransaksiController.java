package id.ac.unpas.sipetok.controller;

import id.ac.unpas.sipetok.database.KoneksiDB;
import id.ac.unpas.sipetok.model.DetailTransaksi;
import id.ac.unpas.sipetok.model.Transaksi;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class TransaksiController {

    private Connection conn = KoneksiDB.getConnection();

    // =================== COMBOBOX ===================

    public Map<Integer, String> getPelanggan() {
        Map<Integer, String> data = new HashMap<>();
        try {
            String sql = "SELECT id_pelanggan, nama_pelanggan FROM pelanggan";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                data.put(rs.getInt("id_pelanggan"),
                         rs.getString("nama_pelanggan"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public Map<Integer, String> getBarang() {
        Map<Integer, String> data = new HashMap<>();
        try {
            String sql = "SELECT id_barang, nama_barang FROM barang";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                data.put(rs.getInt("id_barang"),
                         rs.getString("nama_barang"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public int getHargaBarang(int idBarang) {
        try {
            String sql = "SELECT harga FROM barang WHERE id_barang=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idBarang);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("harga");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // =================== SIMPAN TRANSAKSI ===================

    public boolean simpanTransaksi(Transaksi t, DetailTransaksi d) {
        try {
            conn.setAutoCommit(false);

            // INSERT TRANSAKSI
            String sqlTransaksi =
                "INSERT INTO transaksi (id_pelanggan, tanggal, total) VALUES (?, ?, ?)";
            PreparedStatement psT =
                conn.prepareStatement(sqlTransaksi, Statement.RETURN_GENERATED_KEYS);

            psT.setInt(1, t.getIdPelanggan());
            psT.setDate(2, t.getTanggal());
            psT.setInt(3, t.getTotal());
            psT.executeUpdate();

            ResultSet rs = psT.getGeneratedKeys();
            if (!rs.next()) {
                throw new SQLException("Gagal mendapatkan ID transaksi");
            }
            int idTransaksi = rs.getInt(1);

            // INSERT DETAIL
            String sqlDetail =
                "INSERT INTO detail_transaksi (id_transaksi, id_barang, qty, harga, subtotal) " +
                "VALUES (?, ?, ?, ?, ?)";
            PreparedStatement psD = conn.prepareStatement(sqlDetail);
            psD.setInt(1, idTransaksi);
            psD.setInt(2, d.getIdBarang());
            psD.setInt(3, d.getQty());
            psD.setInt(4, d.getHarga());
            psD.setInt(5, d.getSubtotal());
            psD.executeUpdate();

            conn.commit();
            conn.setAutoCommit(true);
            return true;

        } catch (Exception e) {
            try {
                conn.rollback();
                conn.setAutoCommit(true);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        }
    }

    // =================== RIWAYAT TRANSAKSI ===================

    public ResultSet getRiwayatTransaksi() {
        try {
            String sql =
                "SELECT t.id_transaksi, p.nama_pelanggan, t.tanggal, t.total " +
                "FROM transaksi t " +
                "JOIN pelanggan p ON t.id_pelanggan = p.id_pelanggan " +
                "ORDER BY t.id_transaksi DESC";

            Statement st = conn.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY
            );
            return st.executeQuery(sql);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // =================== HAPUS TRANSAKSI ===================

    public void hapusTransaksi(int idTransaksi) {
        try {
            conn.setAutoCommit(false);

            String sqlDetail =
                "DELETE FROM detail_transaksi WHERE id_transaksi=?";
            PreparedStatement psDetail = conn.prepareStatement(sqlDetail);
            psDetail.setInt(1, idTransaksi);
            psDetail.executeUpdate();

            String sqlTransaksi =
                "DELETE FROM transaksi WHERE id_transaksi=?";
            PreparedStatement psTransaksi = conn.prepareStatement(sqlTransaksi);
            psTransaksi.setInt(1, idTransaksi);
            psTransaksi.executeUpdate();

            conn.commit();
            conn.setAutoCommit(true);

        } catch (Exception e) {
            try {
                conn.rollback();
                conn.setAutoCommit(true);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    // =================== AMBIL TRANSAKSI BY ID (UBAH) ===================

    public Transaksi getTransaksiById(int idTransaksi) {
        try {
            String sql = "SELECT * FROM transaksi WHERE id_transaksi=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idTransaksi);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Transaksi t = new Transaksi();
                t.setIdTransaksi(rs.getInt("id_transaksi"));
                t.setIdPelanggan(rs.getInt("id_pelanggan"));
                t.setTanggal(rs.getDate("tanggal"));
                t.setTotal(rs.getInt("total"));
                return t;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public DetailTransaksi getDetailByTransaksiId(int idTransaksi) {
        try {
            String sql =
                "SELECT * FROM detail_transaksi WHERE id_transaksi=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idTransaksi);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                DetailTransaksi d = new DetailTransaksi();
                d.setIdBarang(rs.getInt("id_barang"));
                d.setQty(rs.getInt("qty"));
                d.setHarga(rs.getInt("harga"));
                d.setSubtotal(rs.getInt("subtotal"));
                return d;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

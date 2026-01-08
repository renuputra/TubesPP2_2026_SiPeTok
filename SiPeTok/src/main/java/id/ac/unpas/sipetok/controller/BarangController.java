package id.ac.unpas.sipetok.controller;

import id.ac.unpas.sipetok.database.KoneksiDB;
import id.ac.unpas.sipetok.model.Barang;
import java.sql.*;
import javax.swing.JOptionPane;

public class BarangController {

    Connection conn = KoneksiDB.getConnection();

    public void simpan(Barang b) {
        if (b.getNamaBarang().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nama barang wajib diisi");
            return;
        }

        try {
            String sql = "INSERT INTO barang(nama_barang, harga, stok) VALUES (?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, b.getNamaBarang());
            ps.setInt(2, b.getHarga());
            ps.setInt(3, b.getStok());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data berhasil disimpan");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public void update(Barang b) {
        try {
            String sql = "UPDATE barang SET nama_barang=?, harga=?, stok=? WHERE id_barang=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, b.getNamaBarang());
            ps.setInt(2, b.getHarga());
            ps.setInt(3, b.getStok());
            ps.setInt(4, b.getIdBarang());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data berhasil diupdate");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public void hapus(int id) {
        try {
            String sql = "DELETE FROM barang WHERE id_barang=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data berhasil dihapus");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public ResultSet tampil() {
        try {
            Statement st = conn.createStatement();
            return st.executeQuery("SELECT * FROM barang");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            return null;
        }
    }
}

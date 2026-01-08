/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package id.ac.unpas.sipetok.controller;


import id.ac.unpas.sipetok.database.KoneksiDB;
import id.ac.unpas.sipetok.model.Pelanggan;
import java.sql.*;
import java.util.*;

/**
 *
 * @author acer
 */
public class PelangganController {
    Connection conn = KoneksiDB.getConnection();

    public void simpan(Pelanggan p) {
        try {
            String sql = "INSERT INTO pelanggan VALUES (NULL, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, p.getNamaPelanggan());
            ps.setString(2, p.getAlamat());
            ps.setString(3, p.getNoHp());
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(Pelanggan p) {
        try {
            String sql = "UPDATE pelanggan SET nama_pelanggan=?, alamat=?, no_hp=? WHERE id_pelanggan=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, p.getNamaPelanggan());
            ps.setString(2, p.getAlamat());
            ps.setString(3, p.getNoHp());
            ps.setInt(4, p.getIdPelanggan());
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hapus(int id) {
        try {
            String sql = "DELETE FROM pelanggan WHERE id_pelanggan=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Pelanggan> tampil() {
        List<Pelanggan> list = new ArrayList<>();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM pelanggan");
            while (rs.next()) {
                Pelanggan p = new Pelanggan();
                p.setIdPelanggan(rs.getInt("id_pelanggan"));
                p.setNamaPelanggan(rs.getString("nama_pelanggan"));
                p.setAlamat(rs.getString("alamat"));
                p.setNoHp(rs.getString("no_hp"));
                list.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}

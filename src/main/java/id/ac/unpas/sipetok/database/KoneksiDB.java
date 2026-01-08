/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package id.ac.unpas.sipetok.database;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author acer
 */
public class KoneksiDB {
     public static Connection getConnection() {
        Connection conn = null;
        try {
            String url = "jdbc:mysql://localhost:3306/sipetok_db";
            String user = "root";
            String pass = "";

            conn = DriverManager.getConnection(url, user, pass);
            System.out.println("Koneksi berhasil");
        } catch (Exception e) {
            System.out.println("Koneksi gagal: " + e.getMessage());
        }
        return conn;
    }
}

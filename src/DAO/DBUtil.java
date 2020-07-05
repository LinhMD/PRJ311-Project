/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author DELL
 */
public class DBUtil {

    //Ham nay de check login
    //input: email, password
    //output ten nhan vien
    public static String checkLogin(String email, String password) {
        Connection cn = null;
        try {
            //b1:tao connection
            cn = MyConnection.makeConnection();
            if (cn != null) {
                //b2: viet lenh sql can lam
                String sql = "select fullname\n"
                        + "from Staff\n"
                        + "where email =? and password = ?";//day la cau chua hoan chinh
                //dung khi viet lenh sql da fix cung du lieu
                PreparedStatement pst = cn.prepareStatement(sql);//dung khi muon do nhieu du lieu khac nhau vao cau lenh sql
                pst.setString(1, email);
                pst.setString(2, password);
                ResultSet rs = pst.executeQuery();
                //b4 su li ket qua
                if (rs.next()) return rs.getString("fullname");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public static int insertSupplier(int id, String name) throws SQLException {
        Connection connection = MyConnection.makeConnection();
        if(connection != null){
            String sql = "insert supplier value(?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(0, id);
            statement.setString(1, name);
            return statement.executeUpdate();
        }
        return 0;
    }
    public static int updateSupplier(int id, String name) throws SQLException {
        Connection connection = MyConnection.makeConnection();
        if(connection != null){
            String sql = "update name = '?' where id = '?'";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(0, id);
            statement.setString(1, name);
            return statement.executeUpdate();
        }
        return 0;
    }
}

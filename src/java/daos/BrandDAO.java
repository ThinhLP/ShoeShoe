/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daos;

import dtos.BrandDto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.DBUtils;

/**
 *
 * @author ThinhLPSE61759
 */
public class BrandDAO {

    public void insert(BrandDto dto) {
        Connection con = null;
        PreparedStatement stm = null;
        try {
            con = DBUtils.makeConnection();
            if (findByName(dto.getBrandName()) != null) {
                return;
            }
            
            String sql = "INSERT INTO brand(name)"
                    + " VALUES(?)";
            stm = con.prepareStatement(sql);
            stm.setString(1, dto.getBrandName());
            
            stm.executeUpdate();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (stm != null) {
                    stm.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private BrandDto convertToBrandDto(ResultSet rs) {
        try {
            BrandDto dto = new BrandDto();
            dto.setBrandId(rs.getInt(1));
            dto.setBrandName(rs.getString(2));
            return dto;
        } catch (SQLException ex) {
            Logger.getLogger(BrandDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public BrandDto findByName(String name) {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            con = DBUtils.makeConnection();
            String sql = "SELECT * FROM brand WHERE name = ?";
            stm = con.prepareStatement(sql);
            stm.setString(1, name);

            rs = stm.executeQuery();

            if (rs.next()) {
                return convertToBrandDto(rs);
            }

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stm != null) {
                    stm.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
    
    public BrandDto findById(int id) {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            con = DBUtils.makeConnection();
            String sql = "SELECT * FROM brand WHERE id = ?";
            stm = con.prepareStatement(sql);
            stm.setInt(1, id);

            rs = stm.executeQuery();

            if (rs.next()) {
                return convertToBrandDto(rs);
            }

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stm != null) {
                    stm.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
    
}

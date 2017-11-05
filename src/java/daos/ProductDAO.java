/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daos;

import commons.Const;
import dtos.BrandDto;
import dtos.ProductDto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.DBUtils;
import utils.Utils;

/**
 *
 * @author ThinhLPSE61759
 */
public class ProductDAO {

    public boolean insert(ProductDto dto) {
        Connection con = null;
        PreparedStatement stm = null;
        try {
            con = DBUtils.makeConnection();
            if (findByProName(dto.getProName()) != null) {
                return update(dto);
            }
            String sql = "INSERT INTO product(name, imageUrl, discountedPrice, originalPrice, createdDate, inStock, brand_id)"
                    + " VALUES(?, ?, ?, ?, ?, ?, ?)";
            stm = con.prepareStatement(sql);
            stm.setString(1, dto.getProName());
            stm.setString(2, dto.getImageUrl());
            stm.setLong(3, dto.getDiscountedPrice());
            stm.setLong(4, dto.getOriginalPrice());
            stm.setString(5, Utils.formatDate(dto.getCreatedDate()));
            stm.setBoolean(6, dto.getInStock());
            BrandDAO brandDao = new BrandDAO();
            int brandId = brandDao.findByName(dto.getBrand().getBrandName()).getBrandId();
            stm.setInt(7, brandId);

            int result = stm.executeUpdate();

            return result > 0;

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
        return false;

    }

    public boolean update(ProductDto dto) {
        Connection con = null;
        PreparedStatement stm = null;
        try {
            con = DBUtils.makeConnection();
            String sql = "UPDATE product"
                    + " SET imageUrl = ?, discountedPrice = ?, originalPrice = ?, updatedDate = ?, inStock = ?, brand_id = ? "
                    + " WHERE name = ?";
            stm = con.prepareStatement(sql);
            stm.setString(1, dto.getImageUrl());
            stm.setLong(2, dto.getDiscountedPrice());
            stm.setLong(3, dto.getOriginalPrice());
            Random rand = new Random();
            int number = rand.nextInt(9999) + 1000;
            stm.setString(4, Utils.formatDate(new Date(new Date().getTime() + number)));
            stm.setBoolean(5, dto.getInStock());
            BrandDAO brandDao = new BrandDAO();
            int brandId = brandDao.findByName(dto.getBrand().getBrandName()).getBrandId();
            stm.setInt(6, brandId);
            stm.setString(7, dto.getProName());

            int result = stm.executeUpdate();
            return result > 0;

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

        return false;

    }

    public ProductDto findByProName(String proName) {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            con = DBUtils.makeConnection();
            String sql = "SELECT * FROM product WHERE name = ?";
            stm = con.prepareStatement(sql);
            stm.setString(1, proName);

            rs = stm.executeQuery();

            if (rs.next()) {
                return convertToProductDto(rs);
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

    public List<ProductDto> getProductList(int pageNo) {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            List<ProductDto> result = new ArrayList<>();
            con = DBUtils.makeConnection();
            String sql = "SELECT * FROM product "
                    + "ORDER BY inStock desc, name, updatedDate desc "
                    + "LIMIT ?,?";
            stm = con.prepareStatement(sql);
            stm.setInt(1, Const.NO_OF_PRODUCT_PER_PAGE * (pageNo - 1));
            stm.setInt(2, Const.NO_OF_PRODUCT_PER_PAGE);
            rs = stm.executeQuery();

            while (rs.next()) {
                result.add(convertToProductDto(rs));
            }
            return result;

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

    public int getTotalNumberOfProducts() {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            List<ProductDto> result = new ArrayList<>();
            con = DBUtils.makeConnection();
            String sql = "SELECT COUNT(id) FROM product";
            stm = con.prepareStatement(sql);
            rs = stm.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
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
        return 0;
    }

    private ProductDto convertToProductDto(ResultSet rs) {
        try {
            ProductDto dto = new ProductDto();
            dto.setProId(rs.getInt("id"));
            dto.setProName(rs.getString("name"));
            dto.setDiscountedPrice(rs.getLong("discountedPrice"));
            dto.setOriginalPrice(rs.getLong("originalPrice"));
            dto.setCreatedDate(rs.getDate("createdDate"));
            dto.setInStock(rs.getBoolean("inStock"));
            dto.setImageUrl(rs.getString("imageUrl"));
            BrandDAO brandDAO = new BrandDAO();
            BrandDto brand = brandDAO.findById(rs.getInt("brand_id"));
            dto.setBrand(brand);

            Date updateDate = rs.getDate("updatedDate");
            if (updateDate == null) {
                updateDate = new Date();
            }
            dto.setUpdatedDate(updateDate);
            return dto;
        } catch (SQLException ex) {
            Logger.getLogger(BrandDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}

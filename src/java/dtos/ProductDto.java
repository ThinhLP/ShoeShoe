/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtos;

import java.io.Serializable;
import java.util.Date;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author ThinhLPSE61759
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {
    "proName",
    "brand",
    "discountedPrice",
    "originalPrice",
    "createdDate",
    "imageUrl",
    "updatedDate"
})
public class ProductDto implements Serializable {

    @XmlAttribute(required = true)
    private int proId;

    @XmlElement(required = true, namespace = "http://www.shoeshoe.vn/productList")
    private String proName;

    @XmlElement(required = true, namespace = "http://www.shoeshoe.vn/productList")
    private BrandDto brand;

    @XmlElement(required = true, namespace = "http://www.shoeshoe.vn/productList")
    private long discountedPrice;

    @XmlElement(required = true, namespace = "http://www.shoeshoe.vn/productList")
    private long originalPrice;

    @XmlElement(required = true, namespace = "http://www.shoeshoe.vn/productList")
    private Date createdDate;

    @XmlElement(required = true, namespace = "http://www.shoeshoe.vn/productList")
    private String imageUrl;

    @XmlAttribute(required = true)
    private Boolean inStock;

    @XmlElement(namespace = "http://www.shoeshoe.vn/productList")
    private Date updatedDate;

    public ProductDto() {
    }

    public ProductDto(int proId, String proName, BrandDto brand, long discountedPrice, long originalPrice, Date createdDate, String imageUrl, boolean inStock, Date updatedDate) {
        this.proId = proId;
        this.proName = proName;
        this.brand = brand;
        this.discountedPrice = discountedPrice;
        this.originalPrice = originalPrice;
        this.createdDate = createdDate;
        this.imageUrl = imageUrl;
        this.inStock = inStock;
        this.updatedDate = updatedDate;
    }

    public ProductDto(int proId, String proName, long discountedPrice, long originalPrice, Date createdDate, String imageUrl, boolean inStock) {
        this.proId = proId;
        this.proName = proName;
        this.discountedPrice = discountedPrice;
        this.originalPrice = originalPrice;
        this.createdDate = createdDate;
        this.imageUrl = imageUrl;
        this.inStock = inStock;
    }

    public ProductDto(int proId, String proName, BrandDto brand, long discountedPrice, long originalPrice, Date createdDate, String imageUrl, boolean inStock) {
        this.proId = proId;
        this.proName = proName;
        this.brand = brand;
        this.discountedPrice = discountedPrice;
        this.originalPrice = originalPrice;
        this.createdDate = createdDate;
        this.imageUrl = imageUrl;
        this.inStock = inStock;
    }

    /**
     * @return the proId
     */
    public int getProId() {
        return proId;
    }

    /**
     * @param proId the proId to set
     */
    public void setProId(int proId) {
        this.proId = proId;
    }

    /**
     * @return the proName
     */
    public String getProName() {
        return proName;
    }

    /**
     * @param proName the proName to set
     */
    public void setProName(String proName) {
        this.proName = proName;
    }

    /**
     * @return the brand
     */
    public BrandDto getBrand() {
        return brand;
    }

    /**
     * @param brand the brand to set
     */
    public void setBrand(BrandDto brand) {
        this.brand = brand;
    }

    /**
     * @return the discountedPrice
     */
    public long getDiscountedPrice() {
        return discountedPrice;
    }

    /**
     * @param discountedPrice the discountedPrice to set
     */
    public void setDiscountedPrice(long discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    /**
     * @return the originalPrice
     */
    public long getOriginalPrice() {
        return originalPrice;
    }

    /**
     * @param originalPrice the originalPrice to set
     */
    public void setOriginalPrice(long originalPrice) {
        this.originalPrice = originalPrice;
    }

    /**
     * @return the createdDate
     */
    public Date getCreatedDate() {
        return createdDate;
    }

    /**
     * @param createdDate the createdDate to set
     */
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * @return the imageUrl
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * @param imageUrl the imageUrl to set
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * @return the inStock
     */
    public Boolean getInStock() {
        return inStock;
    }

    /**
     * @param inStock the inStock to set
     */
    public void setInStock(Boolean inStock) {
        this.inStock = inStock;
    }
    
    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    @Override
    public String toString() {
        return "ID: " + proId + "\n"
                + "Name: " + proName + "\n"
                + "Brand: " + brand.getBrandName() + "\n"
                + "Discounted Price: " + discountedPrice + "\n"
                + "Original Price: " + originalPrice + "\n"
                + "In stock: " + inStock + "\n"
                + "Create date: " + createdDate + "\n"
                + "Img url: " + imageUrl + "\n";
    }

}

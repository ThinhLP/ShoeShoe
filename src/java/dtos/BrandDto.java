/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ThinhLPSE61759
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "brand")
public class BrandDto {
    @XmlAttribute(required = true)
    private int brandId;
    
    @XmlElement(required = true)
    private String brandName;

    public BrandDto() {
    }

    public BrandDto(int brandId, String brandName) {
        this.brandId = brandId;
        this.brandName = brandName;
    }
    
    /**
     * @return the brandId
     */
    public int getBrandId() {
        return brandId;
    }

    /**
     * @param brandId the brandId to set
     */
    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    /**
     * @return the brandName
     */
    public String getBrandName() {
        return brandName;
    }

    /**
     * @param brandName the brandName to set
     */
    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }
    
    
}

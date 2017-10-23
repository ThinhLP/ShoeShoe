/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtos;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ThinhLPSE61759
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "products")
public class ProductListDto {
    
    @XmlElement(required = true)
    private List<ProductDto> productList;

    public ProductListDto() {
    }

    public ProductListDto(List<ProductDto> productList) {
        this.productList = productList;
    }

    /**
     * @return the productList
     */
    public List<ProductDto> getProductList() {
        return productList;
    }

    /**
     * @param productList the productList to set
     */
    public void setProductList(List<ProductDto> productList) {
        this.productList = productList;
    }
    
    
    
}

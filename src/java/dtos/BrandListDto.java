/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtos;

import java.util.List;

/**
 *
 * @author ThinhLPSE61759
 */
public class BrandListDto {
    
    private List<BrandDto> brandList;

    public BrandListDto() {
    }

    public BrandListDto(List<BrandDto> brandList) {
        this.brandList = brandList;
    }

    public List<BrandDto> getBrandList() {
        return brandList;
    }

    public void setBrandList(List<BrandDto> brandList) {
        this.brandList = brandList;
    }
    
    
}

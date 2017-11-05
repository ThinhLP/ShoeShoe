<?xml version="1.0" encoding="UTF-8"?>

<!--
    Document   : product.xsl
    Created on : October 29, 2017, 2:39 PM
    Author     : ThinhLPSE61759
    Description:
        Purpose of transformation follows.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" 
                xmlns:p="http://www.shoeshoe.vn/productList">
    <xsl:output method="html" encoding="UTF-8"/>
    <xsl:param name="searchValue" />
    <xsl:variable name="lower" select="'abcdefghijklmnopqrstuvwxyzáàảạãăắằặẵâấầậẫđéèẻẹẽêềếểễệòóỏọõôồốỗổộơờớởỡợưừứửữựìíịĩỉ'" />
    <xsl:variable name="upper" select="'ABCDEFGHIJKLMNOPQRSTUVWXYZÁÀẢẠÃĂẮẰẶẴÂẤẦẬẪĐÉÈẺẸẼÊỀẾỂỄỆÒÓỎỌÕÔỒỐỖỔỘƠỜỚỞỠỢƯỪỨỬỮỰÌÍỊĨỈ'" />
    <xsl:variable name="singleQuoteMark">'</xsl:variable>
    <xsl:variable name="doubleQuoteMark">"</xsl:variable>
    <!-- TODO customize transformation rules 
         syntax recommendation http://www.w3.org/TR/xslt 
    -->
    <xsl:decimal-format name="vnd" decimal-separator="," grouping-separator="." />

    <xsl:template match="/">
        
        <html>
            <body>
                <xsl:for-each select="//p:product[contains(translate(p:proName, $lower, $upper), $searchValue)]">
                    
                    <div class="card-wrapper" value="{@proId}">
                        <div class="card-header">
                            <div class="card-picture" style="background-image:url({p:imageUrl})"></div>
                        </div>
                        <div class="card-info">
                            <div class="card-title">
                                <xsl:value-of select="p:proName"/>
                            </div>
                            <div class="brand-name" value="{p:brand/p:brandName/@brandId}">
                                <xsl:value-of select="p:brand/p:brandName"/>
                            </div>
                            <div class="card-details fixed-height">
                                <div class="card-price"> 
                                    <xsl:if test="@inStock='true'">
                                        <xsl:if test="p:discountedPrice != p:originalPrice">
                                            <span class="price discounted-price">
                                                <xsl:value-of select="format-number(p:discountedPrice, '#.###', 'vnd')" /> đ
                                            </span> 
                                            <span class="price original-price">
                                                <xsl:value-of select="format-number(p:originalPrice, '#.###', 'vnd')" /> đ
                                            </span> 
                                        </xsl:if>
                                        <xsl:if test="not(p:discountedPrice != p:originalPrice)">
                                            <span class="price normal-price">
                                                <xsl:value-of select="format-number(p:discountedPrice, '#.###', 'vnd')" /> đ
                                            </span> 
                                        </xsl:if>
                                        <div class="add-cart" 
                                             onclick='addToCart({@proId},"{translate(p:proName,$doubleQuoteMark,$singleQuoteMark)}","{p:imageUrl}",{p:discountedPrice})'>
                                            <i class="fa fa-cart-plus" aria-hidden="true"></i>
                                        </div>
                                    </xsl:if>
                                    <xsl:if test="not(@inStock='true')">
                                        <span class="price normal-price" style="color: #ccc">Hết hàng</span> 
                                    </xsl:if>
                                </div>
                            </div>
                        </div>
                    </div>
                </xsl:for-each>
            </body>

        </html>
    </xsl:template>
    
</xsl:stylesheet>

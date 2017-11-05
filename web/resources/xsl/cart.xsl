<?xml version="1.0" encoding="UTF-8"?>

<!--
    Document   : cart.xsl
    Created on : November 5, 2017, 8:06 PM
    Author     : ThinhLPSE61759
    Description:
        Purpose of transformation follows.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" 
                xmlns:c="http://www.shoeshoe.vn/cartList">
    <xsl:output method="html"/>

    <!-- TODO customize transformation rules 
         syntax recommendation http://www.w3.org/TR/xslt 
    -->
    <xsl:decimal-format name="vnd" decimal-separator="," grouping-separator="." />
    <xsl:variable name="maxItems" select="10" />
    <xsl:variable name="sequence" select="any-sequence"/>

    <xsl:template match="/">
        <html>
            <body>
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>STT</th>
                            <th>Tên sản phẩm</th>
                            <th>Hình ảnh</th>
                            <th>Size</th>
                            <th>Giá</th>
                            <th>Số lượng</th>
                            <th> </th>
                        </tr>
                    </thead>
                    <tbody id="cart-tbody">
                        <xsl:for-each select="//c:item">
                            <tr value="{c:proId}">
                                <td>
                                    <xsl:number level="single" count="//c:item" />
                                </td>
                                <td>
                                    <xsl:value-of select="c:proName"/>
                                </td>
                                <td>
                                    <img src="{c:imgUrl}" />
                                </td>
                                <td>
                                    <select onchange="onChangeSize(this,{c:proId})">
                                        <xsl:if test="c:size = 36">
                                            <option selected="selected">36</option>
                                        </xsl:if>
                                        <xsl:if test="c:size != 36">
                                            <option>36</option>
                                        </xsl:if> 
                                        
                                        <xsl:if test="c:size = 37">
                                            <option selected="selected">37</option>
                                        </xsl:if>
                                        <xsl:if test="c:size != 37">
                                            <option>37</option>
                                        </xsl:if>
                                        
                                        <xsl:if test="c:size = 38">
                                            <option selected="selected">38</option>
                                        </xsl:if>
                                        <xsl:if test="c:size != 38">
                                            <option>38</option>
                                        </xsl:if>
                                        
                                        <xsl:if test="c:size = 39">
                                            <option selected="selected">39</option>
                                        </xsl:if>
                                        <xsl:if test="c:size != 39">
                                            <option>39</option>
                                        </xsl:if>
                                        
                                        <xsl:if test="c:size = 40">
                                            <option selected="selected">40</option>
                                        </xsl:if>
                                        <xsl:if test="c:size != 40">
                                            <option>40</option>
                                        </xsl:if>
                                        
                                        <xsl:if test="c:size = 41">
                                            <option selected="selected">41</option>
                                        </xsl:if>
                                        <xsl:if test="c:size != 41">
                                            <option>41</option>
                                        </xsl:if>
                                    </select>
                                </td>
                                <td>
                                    <xsl:value-of select="format-number(c:price,'#.###','vnd')"/> đ
                                </td>
                                <td>
                                    <input type="number" value="{c:quantity}" style="width: 50px" min="1" max="10"  onchange="onChangeQuantity(this,{c:proId})" />
                                </td>
                                <td><i class="fa fa-trash" aria-hidden="true" onclick="removeItem({c:proId})"></i></td>
                            </tr>
                        </xsl:for-each>
                    </tbody>
                </table>
            </body>

        </html>
    </xsl:template>

</xsl:stylesheet>
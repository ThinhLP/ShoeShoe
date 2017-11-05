<%-- 
    Document   : products
    Created on : Oct 28, 2017, 10:20:24 PM
    Author     : ThinhLPSE61759
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sản phẩm | ShoeShoe.vn</title>
        <link href="https://fonts.googleapis.com/css?family=Open+Sans" rel="stylesheet">
        <link rel="stylesheet" href="./resources/css/font-awesome-4.7.0/css/font-awesome.min.css">
        <link rel="stylesheet" href="./resources/css/main.css">
    </head>
    <body>
        <a href="ProcessServlet?btAction=ViewCart" class="shopping-cart"> <i class="fa fa-shopping-cart" aria-hidden="true"></i> <span id="noOfItems">0</span> </a>
        <div id="promo-banner">Phone: 0909.909.090 - MIỄN PHÍ GIAO HÀNG TOÀN QUỐC CHO ĐƠN HÀNG TRÊN 500.000đ (Không áp dụng với các sản phẩm SALE)</div>
        <header>
            <c:set var="fullname" value="${sessionScope.USER_INFO.fullname}" />
            <div id="main-header"><a href="ProcessServlet"><img src="./resources/img/logo.png" class="logo" /></a>
                <div class="right-header">
                    <c:if test="${fullname != null}">
                        <span class="username">${fullname}</span>
                        <a href="ProcessServlet?btAction=logout">(Đăng xuất)</a>
                    </c:if>
                    <c:if test="${fullname == null}">
                        <div class="login-link"><a href="login.html">Đăng nhập</a> | <a href="signup.jsp">Đăng ký</a></div>
                    </c:if>
                </div>
            </div>
        </header>
        <section>
            <div class="main-section">
                <h2 class="main-section-header">Sản phẩm</h2>
                <div class="slogan">Những mẫu giày đẹp nhất quả đất</div>
                <div class="search-wrapper"> <span>Tìm kiếm: </span>
                    <input type="text" placeholder="Nhập tên giày..." value="" id="txtSearchValue" />
                    <button class="button" onclick="search()" >Tìm kiếm</button>
                </div>

                <%--<c:set var="productList" value="${requestScope.PRODUCTS}"/>--%>
                <%--<c:import var="xsl" url="/resources/xsl/product.xsl" charEncoding="utf-8"/>--%>
                <div id="zero-product">Không có sản phẩm nào</div>
                <div class="products" id="products">
                    <%--<x:transform doc="${productList}" xslt="${xsl}"/>--%> 
                </div>

            </div>
        </section>
        <footer> ShoeShoe.vn - Designed by ThinhLP </footer>

        <script src="./resources/js/XmlUtils.js"></script>
        <script src="./resources/js/cart.js"></script>
        <script>
                        var xmlUtils = new XMLUtils();
                        var searchValue = "";
                        var heightPerPage = 0;
                        var startPage = 0;


                        var loadedPage = [];

                        var applyXLS = function (xml, xsl, searchValue) {
                            var result;
                            if (window.ActiveXObject) {
                                var xslt = new ActiveXObject("Msxml2.XSLTemplate");
                                var xsltProcessor = xslt.createProcessor();
                                xsltProcessor.input = xml;
                                xsltProcessor.stylesheet = xsl;
                                xsltProcessor.transform();
                                xsltProcessor.addParameter("searchValue", searchValue);
                                result = xsltProcessor.output;
                            } else if (document.implementation && document.implementation.createDocument) {
                                var xsltProcessor = new XSLTProcessor();
                                xsltProcessor.importStylesheet(xsl);
                                xsltProcessor.setParameter(null, "searchValue", searchValue);
                                result = xsltProcessor.transformToFragment(xml, document);
                            }
                            return result;
                        };

                        var showElement = function (element) {
                            element.style.opacity = 1;
                        };
                        var hideElement = function (element) {
                            element.style.opacity = 0;
                        };

                        var loadProduct = function (pageNo) {
                            var xhr = xmlUtils.getXmlHttpRequestObject();
                            if (!xhr) {
                                alert("Trình duyệt của bạn không hỗ trợ Ajax");
                                return;
                            }
                            xhr.open('GET', 'GetProductsServlet?pageNo=' + pageNo, false);
                            xhr.onreadystatechange = function () {
                                if (this.readyState === 4 && this.status === 200) {
                                    var result = xhr.responseText;
                                    var xml = xmlUtils.parseXML(result);

                                    var xmlWithXSL = applyXLS(xml, productXSL, searchValue);
                                    var productsContainer = document.getElementById('products');
                                    var zeroProduct = document.getElementById('zero-product');

                                    var numOfProducts = xmlWithXSL.children.length;
                                    if (numOfProducts == 0) {
                                        if (pageNo == 1) {
                                            showElement(zeroProduct);
                                            hideElement(productsContainer);
                                        }
                                        return;
                                    }

                                    showElement(productsContainer);
                                    hideElement(zeroProduct);
                                    productsContainer.insertAdjacentHTML('beforeend', xmlUtils.convertDocToString(xmlWithXSL));

                                    heightPerPage = productsContainer.scrollHeight / (pageNo + 1);
                                }
                            };
                            xhr.send();
                        };

                        // window scroll handler
                        var scrollWindowHandler = function () {
                            var scrollPos = document.documentElement.scrollTop + screen.height + 100;
                            if (scrollPos >= startPage * heightPerPage) {
                                if (!loadedPage[startPage]) {
                                    loadedPage[startPage] = true;
                                    // Load page
                                    loadProduct(startPage + 1, searchValue);
                                }
                                startPage++;
                            }
                        };
                        window.addEventListener('scroll', function (e) {
                            scrollWindowHandler();
                        });

//                        window.addEventListener('resize', function (e) {
//                            heightPerPage = document.getElementById('products').scrollHeight / startPage;
//                        });

                        // Window onload proccessor
                        var productXSLFilePath = 'resources/xsl/product2.xsl';
                        var productXSL;
                        window.onload = function () {
                            productXSL = xmlUtils.loadXMLFile(productXSLFilePath);
                            scrollWindowHandler();
                            getCurrentCart();
                        };

                        // Onclick search button handler
                        function search() {
                            searchValue = document.getElementById("txtSearchValue").value.trim().toUpperCase();

                            //reset
                            startPage = 0;
                            loadedPage = [];
                            heightPerPage = 0;
                            document.getElementById('products').innerHTML = "";
                            var noOfPagePreload = 2;
                            for (var i = 0; i < noOfPagePreload; i++) {
                                scrollWindowHandler();
                                window.scrollTo(0, 20 * i);
                            }

                        }

                        setInterval(function () {
                            document.getElementById('products').innerHTML = "";
                            for (var i = 0; i < startPage; i++) {
                                loadProduct(i + 1);
                            }
                        }, 20000);


                        // Cart handler 
                        var cart;
                        var cartHandler;

                        var getCurrentCart = function () {
                            var cartStr = localStorage.getItem("CART");
                            if (cartStr == null) {
                                cartStr = "<cart xmlns='http://www.shoeshoe.vn/cartList'></cart>";
                            }
                            cart = xmlUtils.parseXML(cartStr);
                            cartHandler = new CartHandler(cart);
                            cartHandler.setNumOfItems();
                        };

                        function addToCart(id, name, imageUrl, price) {
//                            var existedItem = cartHandler.checkExistedItem(id);
//
//                            if (existedItem != null) {
//                                cartHandler.updateItem(existedItem);
//                            } else {
                                cartHandler.addItem(id, name, imageUrl, price);
//                            }
                            cartHandler.setNumOfItems();
                            localStorage.setItem("CART", xmlUtils.convertDocToString(cartHandler.getCart()));
                            alert('Đã thêm sản phẩm vào giỏ hàng của bạn');
                        }

        </script>
    </body>
</html>

<%-- 
    Document   : cart
    Created on : Nov 5, 2017, 7:34:11 PM
    Author     : ThinhLPSE61759
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Giỏ hàng của bạn | ShoeShoe.vn</title>
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
                <h2 class="main-section-header">Giỏ hàng của bạn</h2>

                <div id="zero-product">Không có sản phẩm nào</div>
                <div class="cart" id="cart">

                </div>
                <div class="total-money">
                    Tổng tiền: <span id="total-money">1.500.000 đ</span>
                </div>
                <div class="checkout">
                    <button class="button" value="checkout" name="btAction">Thanh toán</button>
                </div>

            </div>
        </section>
        <footer> ShoeShoe.vn - Designed by ThinhLP </footer>

        <script src="./resources/js/XmlUtils.js"></script>
        <script src="./resources/js/cart.js"></script>
        <script>
            var xmlUtils = new XMLUtils();
            var cart;
            var cartHandler;

            var convertToVND = function (str) {
                str = str.toString();
                var price = '';
                var count = 0;

                for (var i = str.length - 1; i >= 0; i--) {
                    if (str[i] !== '.') {
                        count++;
                        price = str[i] + price;
                        if (count % 3 === 0) {
                            price = '.' + price;
                        }
                    }
                }
                if (price[0] === '.') {
                    price = price.substring(1);
                }
                return price + " đ";
            };

            var showElement = function (element) {
                element.style.opacity = 1;
            };
            var hideElement = function (element) {
                element.style.opacity = 0;
            };

            var setNumOfItems = function () {
                var noOfItems = 0;
                if (cart != null) {
                    noOfItems = cart.getElementsByTagName("item").length;
                }
                document.getElementById("noOfItems").innerHTML = noOfItems;

                var zeroProductElm = document.getElementById("zero-product");
                var cartElm = document.getElementById("cart");
                var totalMoneyElm = document.getElementsByClassName("total-money")[0];
                if (noOfItems == 0) {
                    hideElement(cartElm);
                    showElement(zeroProductElm);
                    hideElement(totalMoneyElm);
                } else {
                    showElement(cartElm);
                    showElement(totalMoneyElm);
                    hideElement(zeroProductElm);
                }
            };

            var getCurrentCart = function () {
                var cartStr = localStorage.getItem("CART");
                if (cartStr != null) {
                    cart = xmlUtils.parseXML(cartStr);
                    cartHandler = new CartHandler(cart);
                }
                setNumOfItems();
            };

            var loadCart = function (cartXML) {
                var cartTable = document.getElementById("cart");
                cartTable.innerHTML = "";
                cartTable.appendChild(cartXML);
            };

            // Window onload proccessor
            var cartXSLFilePath = 'resources/xsl/cart.xsl';
            var cartXSL;

            window.onload = function () {
                getCurrentCart();
                cartXSL = xmlUtils.loadXMLFile(cartXSLFilePath);
                var cartXML = xmlUtils.applyXLS(cart, cartXSL);
                loadCart(cartXML);
                setTotalMoney();
            };


            // Handle change size 
            function onChangeSize(obj, id) {
                var newSize = obj.value;
                if (!newSize || isNaN(newSize)) {
                    return;
                }
                var existedItem = cartHandler.checkExistedItem(id);
                if (existedItem != null) {
                    cartHandler.updateItem(existedItem, newSize);
                    localStorage.setItem('CART', xmlUtils.convertDocToString(cartHandler.getCart()));
                    setTotalMoney();
                }
            }

            // Handle change quantity
            function onChangeQuantity(obj, id) {
                var newQuantity = obj.value;
                if (!newQuantity || isNaN(newQuantity) || newQuantity <= 0 || newQuantity > 10) {
                    return;
                }
                var existedItem = cartHandler.checkExistedItem(id);
                if (existedItem != null) {
                    cartHandler.updateItem(existedItem, undefined, newQuantity);
                    saveCart();
                }
            }


            // Save cart to local storage
            var saveCart = function () {
                localStorage.setItem('CART', xmlUtils.convertDocToString(cartHandler.getCart()));
                setTotalMoney();
            };
            
            // Total
            var setTotalMoney = function () {
                var totalMoneyElm = document.getElementById("total-money");
                totalMoneyElm.textContent = convertToVND(cartHandler.getTotal());
            };

            // Remove item
            function removeItem(id) {
                cartHandler.removeItem(id);
                saveCart();
                var cartXML = xmlUtils.applyXLS(cartHandler.getCart(), cartXSL);
                loadCart(cartXML);
                setNumOfItems();
            }


        </script>
    </body>
</html>

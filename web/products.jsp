<%-- 
    Document   : products
    Created on : Oct 28, 2017, 10:20:24 PM
    Author     : ThinhLPSE61759
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Shoe Shoe - Sản phẩm</title>
        <link href="https://fonts.googleapis.com/css?family=Open+Sans" rel="stylesheet">
        <link rel="stylesheet" href="./resources/css/font-awesome-4.7.0/css/font-awesome.min.css">
        <link rel="stylesheet" href="./resources/css/main.css">
    </head>
    <body>
        <div id="promo-banner">Phone: 0909.909.090 - MIỄN PHÍ GIAO HÀNG TOÀN QUỐC CHO ĐƠN HÀNG TRÊN 500.000đ (Không áp dụng với các sản phẩm SALE)</div>
        <header>
            <div id="main-header"> <img src="./resources/img/logo.png" class="logo" />
                <div class="right-header"> <span class="username">ThinhLP</span>
                    <a href="#" class="shopping-cart"> <i class="fa fa-shopping-cart" aria-hidden="true"></i> <span class="noOfItems">10</span> </a>
                </div>
            </div>
        </header>
        <section>
            <div class="main-section">
                <h2 class="main-section-header">Sản phẩm</h2>
                <div class="slogan">Những mẫu giày đẹp nhất quả đất</div>
                <div class="search-wrapper"> <span>Tìm kiếm: </span>
                    <input type="text" placeholder="Nhập tên giày..." />
                    <button class="button">Tìm kiếm</button>
                </div>
                <div class="products" id="products">
                    <div class="card-wrapper">
                        <div class="card-header">
                            <div class="card-picture" style="background-image:url('resource/Photos/html.jpeg')"></div>
                        </div>
                        <div class="card-info">
                            <div class="card-title">Sneaker</div>
                            <div class="brand-name">Nike </div>
                            <div class="card-details fixed-height">
                                <div class="card-price"> <span class="price discounted-price">660.000đ</span> <span class="price original-price">850.000đ</span> </div>
                            </div>
                        </div>
                    </div>

                </div>
                <div class="pagination">
                    <div class="page-no active">1</div>
                    <div class="page-no">2</div>
                    <div class="page-no">3</div>
                </div>
            </div>
        </section>
        <footer> ShoeShoe.vn - Design by ThinhLP </footer>
        <script>
            var productListStr = '${requestScope.PRODUCTS}';

            var getXmlDoc = function (productList) {
                var parser = new DOMParser();
                return parser.parseFromString(productList, "text/xml");
            };

            var toVNMoney = function (rawMoney) {

            };

            var createCart = function (product) {
                var cardWrapper = document.createElement("div");
                cardWrapper.className = "card-wrapper";
                cardWrapper.setAttribute("value", product.id);

                var cardHeader = document.createElement("div");
                cardHeader.className = "card-header";

                var cardPicture = document.createElement("div");
                cardPicture.className = "card-picture";
                cardPicture.setAttribute("style", 'background-image:url("' + product.imageUrl + '")');

                var cardInfo = document.createElement("div");
                cardInfo.className = "card-info";

                var cardTitle = document.createElement("div");
                cardTitle.className = "card-title";

                var brandName = document.createElement("div");
                brandName.className = "brand-name";

                var cardDetails = document.createElement("div");
                cardDetails.className = "card-details fixed-height";

                var cardPrice = document.createElement("div");
                cardPrice.className = "card-price";

                var disPrice = document.createElement("span");
                if (product.discountedPrice !== product.originalPrice) {
                    disPrice.className = "price discounted-price";
                    var oriPrice = document.createElement("span");
                    oriPrice.className = "price original-price";
                    // Set price
                    disPrice.textContent = product.discountedPrice;
                    oriPrice.textContent = product.originalPrice;
                    cardPrice.appendChild(disPrice);
                    cardPrice.appendChild(oriPrice);
                } else {
                    disPrice.className = "price normal-price";
                    disPrice.textContent = product.discountedPrice;
                    cardPrice.appendChild(disPrice);
                }

                // Set brand and title
                cardTitle.textContent = product.proName;
                brandName.textContent = product.brandName;


                cardDetails.appendChild(cardPrice);

                cardInfo.appendChild(cardTitle);
                cardInfo.appendChild(brandName);
                cardInfo.appendChild(cardDetails);
                
                cardHeader.appendChild(cardPicture);

                cardWrapper.appendChild(cardHeader);
                cardWrapper.appendChild(cardInfo);

                return cardWrapper;
            };

            var loadListProductToPage = function (list) {
                var productsElm = document.getElementById("products");
                productsElm.innerHTML = "";
                for (var i = 0; i < list.length; i++) {
                    productsElm.appendChild(createCart(list[i]));
                }
            };

            var processXML = function (xmlDoc) {
                var productTags = xmlDoc.getElementsByTagName("product");
                var productList = [];
                for (var i = 0; i < productTags.length; i++) {
                    var productNode = productTags[i];
                    var product = {};
                    product.id = productNode.getAttribute("proId");
                    product.inStock = productNode.getAttribute("inStock");

                    var childNodes = productNode.childNodes;
                    for (var j = 0; j < childNodes.length; j++) {
                        var nodeName = childNodes[j].nodeName;
                        if (nodeName === "proName") {
                            product.proName = childNodes[j].textContent;
                        } else if (nodeName === "brand") {
                            product.brandId = childNodes[j].getAttribute("brandId");
                            product.brandName = childNodes[j].firstChild.textContent;
                        } else if (nodeName === "discountedPrice") {
                            product.discountedPrice = childNodes[j].textContent;
                        } else if (nodeName === "originalPrice") {
                            product.originalPrice = childNodes[j].textContent;
                        } else if (nodeName === "imageUrl") {
                            product.imageUrl = childNodes[j].textContent;
                        } else if (nodeName === "updatedDate") {
                            product.updatedDate = childNodes[j].textContent;
                        }
                    }
                    productList.push(product);
                }
                return productList;
            };

            // Window onload proccessor
            window.onload = function () {
                var productList = processXML(getXmlDoc(productListStr));
                loadListProductToPage(productList);
            };

        </script>
    </body>
</html>

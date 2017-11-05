"use strict";
function CartHandler(cart) {

    var setNumOfItems = function () {
        var noOfItems = 0;
        if (cart != null) {
            noOfItems = cart.getElementsByTagName("item").length;
        }
        document.getElementById("noOfItems").innerHTML = noOfItems;
    };

    var createItem = function (id, name, imgUrl, price) {
        return '<item value="' + id + '" xlmns="http://www.shoeshoe.vn/cartList"><proId>' + id + '</proId>' +
                '<proName>' + name + '</proName>' +
                '<imgUrl>' + imgUrl + '</imgUrl>' +
                '<price>' + price + '</price>' +
                '<quantity>1</quantity>' +
                '<size>36</size></item>'
                ;
    };

    var checkExistedItem = function (id) {
        var items = cart.getElementsByTagName("item");
        for (var item of items) {
            var itemId = item.getAttribute('value');
            if (itemId && itemId == id) {
                return item;
            }
        }
        return null;
    };

    var updateItem = function (item, size, quantity) {
        var quantityElm = item.getElementsByTagName('quantity');
        var sizeElm = item.getElementsByTagName('size');
        if (quantityElm.length > 0 && quantity) {
//            var inc = parseInt(quantityElm[0].textContent) + 1;
//            if (inc > 10) {
//                inc = 10;
//            }
            quantityElm[0].innerHTML = quantity;
        }
        if (size && sizeElm.length > 0) {
            sizeElm[0].textContent = size;
        }
    };

    var addItem = function (id, name, imageUrl, price) {
        cart.getElementsByTagName("cart")[0].insertAdjacentHTML('beforeend', createItem(id, name, imageUrl, price));
    };

    var getCart = function () {
        return cart;
    };

    var totalOfItem = function (item) {
        var childs = item.childNodes;
        var quantity, money;
        for (var i = 0; i < childs.length; i++) {
            var tagName = childs[i].tagName;
            if (tagName == 'quantity') {
                quantity = childs[i].textContent;
            } else if (tagName == 'price') {
                money = childs[i].textContent;
            }
        }
        return quantity * money;
    };

    var getTotal = function () {
        var items = cart.getElementsByTagName("item");
        var total = 0;
        for (var item of items) {
            total += totalOfItem(item);
        }
        return total;
    };

    var removeItem = function (id) {
        var item = checkExistedItem(id);
        if (item != null) {
            cart.getElementsByTagName("cart")[0].removeChild(item);
        }
    };


    return {
        createItem: createItem,
        checkExistedItem: checkExistedItem,
        updateItem: updateItem,
        setNumOfItems: setNumOfItems,
        addItem: addItem,
        getCart: getCart,
        getTotal: getTotal,
        removeItem: removeItem,
    };
}

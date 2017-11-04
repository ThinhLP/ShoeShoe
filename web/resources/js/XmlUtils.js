/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
"use strict";
function XMLUtils() {

    var parseXML = function (data) {
        var parser = new DOMParser();
        return parser.parseFromString(data, "text/xml");
    };

    var getXmlHttpRequestObject = function () {
        var xhr;
        if (window.XMLHttpRequest) {
            xhr = new XMLHttpRequest();
        } else {
            xhr = new ActiveXObject('Microsoft.XMLHTTP');
        }
        return xhr;
    };

    var loadXMLFile = function (url) {
        var xhr = getXmlHttpRequestObject();
        if (!xhr) {
            alert("Trình duyệt của bạn không hỗ trợ Ajax");
            return;
        }
        xhr.open('GET', url, false);
        xhr.send();
        return xhr.responseXML;
    };

    var convertDocToString = function (doc) {
        var serialize = new XMLSerializer();
        return serialize.serializeToString(doc);
    };

    return {
        parseXML: parseXML,
        getXmlHttpRequestObject: getXmlHttpRequestObject,
        loadXMLFile: loadXMLFile,
        convertDocToString: convertDocToString
    };
}



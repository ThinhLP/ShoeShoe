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

    var applyXLS = function (xml, xsl) {
        var result;
        if (window.ActiveXObject) {
            var xslt = new ActiveXObject("Msxml2.XSLTemplate");
            var xsltProcessor = xslt.createProcessor();
            xsltProcessor.input = xml;
            xsltProcessor.stylesheet = xsl;
            xsltProcessor.transform();
            result = xsltProcessor.output;
        } else if (document.implementation && document.implementation.createDocument) {
            var xsltProcessor = new XSLTProcessor();
            xsltProcessor.importStylesheet(xsl);
            result = xsltProcessor.transformToFragment(xml, document);
        }
        return result;
    };

    return {
        parseXML: parseXML,
        getXmlHttpRequestObject: getXmlHttpRequestObject,
        loadXMLFile: loadXMLFile,
        convertDocToString: convertDocToString,
        applyXLS: applyXLS
    };
}



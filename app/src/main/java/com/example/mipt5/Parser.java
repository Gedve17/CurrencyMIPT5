package com.example.mipt5;

import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class Parser {

    public static String parseCurrencyRates(InputStream xmlStream) {
        StringBuilder resultBuilder = new StringBuilder();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(xmlStream);

            NodeList items = document.getElementsByTagName("item");
            for (int i = 0; i < items.getLength(); i++) {
                Element item = (Element) items.item(i);
                String currency = getTagValue(item, "targetCurrency");
                if (currency != null) {
                    resultBuilder.append(currency).append("\n");
                }
            }
        } catch (Exception e) {
            Log.e("Parser", "Error parsing XML", e);
        }

        return resultBuilder.toString();
    }

    private static String getTagValue(Element element, String tagName) {
        NodeList nodes = element.getElementsByTagName(tagName);
        if (nodes.getLength() > 0) {
            return nodes.item(0).getTextContent();
        }
        return null;
    }
}

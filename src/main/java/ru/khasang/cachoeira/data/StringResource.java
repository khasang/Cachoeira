package ru.khasang.cachoeira.data;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by nadezhda on 07.10.2015.
 */
/*
* Служебный класс для хранения служебных строковых ресурсов:
*    заголовки графических элементов: полей, кнопок и т. п.;
*    сообщений об ошибках;
*    данных для инициализации "по умолчанию" строковых полей классов;
*    и т.д.
*
* */

public class StringResource implements IStringResource {

    private final String resourceFile = "res/strings.xml";
    private static IStringResource instance;

    private HashMap<String, String> resources;

    public static IStringResource getInstance(){
        if (instance == null){
            instance = new StringResource();
        }
        return instance;
    }

    private StringResource() {
        resources = new HashMap<>();
        initResources();
    }

    @Override
    public String getResource(String id) {
        return resources.get(id);
    }

    private void initResources(){
        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        f.setValidating(false);
        DocumentBuilder builder = null;
        try {
            builder = f.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        try {
            Document doc = builder.parse(new File(resourceFile));
            NodeList nodes = doc.getChildNodes().item(0).getChildNodes();
            String id = "";
            String value = "";
            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);
                if (node.getAttributes() != null){
                    for (int j = 0; j < node.getAttributes().getLength(); j++) {
                        if (node.getAttributes().item(j).getNodeName().equals("id")) {
                            id = node.getAttributes().item(j).getNodeValue();
                        }
                        if (node.getAttributes().item(j).getNodeName().equals("value")) {
                            value = node.getAttributes().item(j).getNodeValue();
                        }
                    }
                }
                resources.put(id, value);
                id = "";
                value = "";
            }
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

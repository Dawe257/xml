import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<Employee> list = parseXML("data.xml");

        String json = listToJson(list);

        writeString(json, "1.json");
    }

    public static List<Employee> parseXML(String filename) {
        Document doc;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.parse(new File(filename));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Node root = doc.getDocumentElement();
        NodeList childNodes = root.getChildNodes();

        List<Employee> result = new ArrayList<>();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node node = childNodes.item(i);
            if (Node.ELEMENT_NODE == node.getNodeType()) {
                Element element = (Element) node;
                NodeList elementChildNodes = element.getChildNodes();

                Employee employee = new Employee();
                for (int j = 0; j < elementChildNodes.getLength(); j++) {
                    Node item = elementChildNodes.item(j);
                    switch (item.getNodeName()) {
                        case "id" -> employee.setId(Long.parseLong(item.getTextContent()));
                        case "firstName" -> employee.setFirstName(item.getTextContent());
                        case "lastName" -> employee.setLastName(item.getTextContent());
                        case "country" -> employee.setCountry(item.getTextContent());
                        case "age" -> employee.setAge(Integer.parseInt(item.getTextContent()));
                    }
                }
                result.add(employee);
            }
        }

        return result;
    }

    private static <T> String listToJson(List<Employee> list) {
        Type listType = new TypeToken<List<T>>() {}.getType();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.toJson(list, listType);
    }

    public static void writeString(String data, String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

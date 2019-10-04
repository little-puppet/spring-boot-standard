package com.standard.utils;

import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.*;
import java.util.Map.Entry;

/**
 *
 * 提供Map<String,Object>转XML，XML转Map<String,Object>
 *
 * @author MOSHUNWEI
 * @since 2018-03-09
 * @version 5.0
 */

public class XmlMapUtil {
    
    private XmlMapUtil() {}

	/********************************** 标签格式  **********************************/

	/**
	 * 格式化xml,显示为容易看的XML格式
	 *
	 * @param inputXML
	 * @return
	 */
	public static String formatXML(String inputXML) {
		String requestXML = null;
		XMLWriter writer = null;
		Document document = null;
		try(StringWriter stringWriter = new StringWriter()) {
			SAXReader reader = new SAXReader();
			document = reader.read(new StringReader(inputXML));
			if (document != null) {
				OutputFormat format = new OutputFormat("	", true);// 格式化，每一级前的空格
				format.setNewLineAfterDeclaration(false); // xml声明与内容是否添加空行
				format.setSuppressDeclaration(false); // 是否设置xml声明头部
				format.setNewlines(true); // 设置分行
				writer = new XMLWriter(stringWriter, format);
				writer.write(document);
				writer.flush();
				requestXML = stringWriter.getBuffer().toString();
			}
			return requestXML;
		} catch (Exception e1) {

			return null;
		} finally {
			if (writer != null) {
				try {
                    writer.close();
                } catch (IOException e) {}
			}
		}
	}

	/**
	 * 将Map转换为XML,Map可以多层转
	 *
	 * @param map 需要转换的map。
	 * @param parentName 就是map的根key,如果map没有根key,就输入转换后的xml根节点。
	 * @return String-->XML
	 */
	@SuppressWarnings("unchecked")
	public static String mapToXml(Map<String, Object> map, String parentName) {
		// 获取map的key对应的value
		Map<String, Object> rootMap = (Map<String, Object>) map.get(parentName);
		if (rootMap == null) {
			rootMap = map;
		}
		Document doc = DocumentHelper.createDocument();
		// 设置根节点
        Element element = doc.addElement(parentName);
        iteratorXml(element, rootMap);
		return element.asXML();
	}

	/**
	 * 将Map转换为XML,Map可以多层转
	 *
	 * @param map 需要转换的map。
	 * @param parentName 就是map的根key,如果map没有根key,就输入转换后的xml根节点。
	 * @return String-->XML
	 */
	@SuppressWarnings("unchecked")
	public static String mapToCDATEXml(Map<String, Object> map, String parentName) {
		// 获取map的key对应的value
		Map<String, Object> rootMap = (Map<String, Object>) map.get(parentName);
		if (rootMap == null) {
			rootMap = map;
		}
		Document doc = DocumentHelper.createDocument();
		// 设置根节点
		Element element = doc.addElement(parentName);
		iteratorCDATAXml(element, rootMap);
		return element.asXML();
	}

    /**
     * 循环遍历params创建xml节点
     *
     * @param element 根节点
     * @param params map数据
     * @return String-->Xml
     */
    @SuppressWarnings("unchecked")
    private static void iteratorCDATAXml(Element element, Map<String, Object> params) {
        Set<Entry<String, Object>> entries = params.entrySet();
        Element child = null;
        for (Entry<String, Object> entry : entries) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof Map) {
                child = element.addElement(key);
				iteratorCDATAXml(child, (Map<String, Object>) entry.getValue());
            } else if (value instanceof List) {
                List<Object> list = (ArrayList<Object>) value;
                for (int i = 0; i < list.size(); i++) {
                    child = element.addElement(key);
					iteratorCDATAXml(child, (Map<String, Object>) list.get(i));
                }
            } else {
                child = element.addElement(key);
                child.addCDATA(value.toString());
            }

        }
    }


	/**
	 * 循环遍历params创建xml节点
	 *
	 * @param element 根节点
	 * @param params map数据
	 * @return String-->Xml
	 */
	@SuppressWarnings("unchecked")
	private static void iteratorXml(Element element, Map<String, Object> params) {
		Set<Entry<String, Object>> entries = params.entrySet();
		Element child = null;
		for (Entry<String, Object> entry : entries) {
			String key = entry.getKey();
			Object value = entry.getValue();
			if (value instanceof Map) {
				child = element.addElement(key);
				iteratorXml(child, (Map<String, Object>) entry.getValue());
			} else if (value instanceof List) {
				List<Object> list = (ArrayList<Object>) value;
				for (int i = 0; i < list.size(); i++) {
					child = element.addElement(key);
					iteratorXml(child, (Map<String, Object>) list.get(i));
				}
			} else {
				child = element.addElement(key);
				child.addText(value.toString());
			}

		}
	}


	/**
	 * xml转map
	 * @param xml xml
	 * @return map
	 */
	public static Map<String, Object> xmlToMap(String xml) {
		Document doc = null;
		try {
			doc = DocumentHelper.parseText(xml);
		} catch (DocumentException e) {
		    e.printStackTrace();
			return null;
		}
		Map<String, Object> map = new HashMap<>();
		elementToMap(map, doc.getRootElement());
		return map;
	}

	/**
	 * 使用递归调用将多层级xml转为map
	 *
	 * @param map
	 * @param element
	 */
	private static void elementToMap(Map<String, Object> map, Element element) {
		// 获得当前节点的子节点
		List<Element> childElements = element.elements();

        for (Element childElement : childElements) {

            // 判断有没有子元素
            if (!childElement.elements().isEmpty()){
                Map<String, Object> childMap = new HashMap<>();
                elementToMap(childMap, childElement);
                Object o = map.get(childElement.getName());
                if (o != null){
                    if ( o instanceof  List){
                        ((List<Map>)o).add(childMap);
                    } else {
                        List list = new ArrayList<Map>();
                        list.add(childMap);
                        list.add(o);
                        map.put(childElement.getName(), list);
                    }
                } else {
                    map.put(childElement.getName(), childMap);
                }

            } else {
                map.put(childElement.getName(),childElement.getText());
            }

        }
	}


	/********************************** 属性格式  **********************************/

	/**
	 * element属性转Map
	 * @param xml
	 * @return
	 */
	public static Map<String, Object> xmlPropToMap(String xml){
		try {
			Document document = DocumentHelper.parseText(xml);
			document.setXMLEncoding("UTF-8");
			Element rootElement = document.getRootElement();
			HashMap<String, Object> xmlPropToMap = new HashMap<>();
			elementPropToMap(rootElement,xmlPropToMap);
			return xmlPropToMap;
		} catch (DocumentException e) {
			return null;
		}
	}


	/**
	 * element属性转Map 递归调用
	 * @param element 元素
	 * @param propMap 存放结果的map
	 */
	@SuppressWarnings("unchecked")
	private static void elementPropToMap(Element element, Map<String, Object> propMap){
		List<Attribute> attributes = element.attributes();
		for (Attribute attribute : attributes) {
			propMap.put(attribute.getName(), attribute.getValue());
		}
		List<Element> elements = element.elements();
		for (Element child : elements) {
			String name = child.getName();
			Object object = propMap.get(name);
			HashMap<String, Object> childMap = new HashMap<>();
			elementPropToMap(child, childMap);

			if (object != null) {
				if (object instanceof List) {
					((ArrayList<Map<String, Object>>)object).add(childMap);
				} else {
					ArrayList<Map<String, Object>> arrayList = new ArrayList<>();
					arrayList.add((Map<String, Object>)object);
					arrayList.add(childMap);
					propMap.put(name, arrayList);
				}
			} else {
				propMap.put(name, childMap);
			}
		}
	}

    /**
     * map转element属性
     * @param map 参数map
     * @param rootName 根节点名称
     * @return xml字符串
     */
	public static String mapToXmlProp(Map<String, Object> map, String rootName) {

		// 新建一个document 设置输出字符集
		Document document = DocumentHelper.createDocument();
		document.setXMLEncoding("UTF-8");

		Element rootElement = document.addElement(rootName);
		mapToElementProp(map, rootElement);
		return rootElement.asXML();
	}

	/**
	 * map转属性element属性 递归调用
	 * @param map 参数map
	 * @param element 元素
	 */
	@SuppressWarnings("unchecked")
	private static void mapToElementProp(Map<String, Object> map, Element element) {
		Set<Entry<String,Object>> entrySet = map.entrySet();
		for (Entry<String, Object> entry : entrySet) {

			Object value = entry.getValue();

			if (value instanceof String) {
				element.addAttribute(entry.getKey(), value.toString());
			}

			if (value instanceof List<?>) {
				for (Map<String, Object> valueMap : (List<Map<String, Object>>)value) {
					Element ele = element.addElement(entry.getKey());
					ele.addText("");
					mapToElementProp(valueMap, ele);
				}
			}

			if (value instanceof Map<?, ?>) {
				Element ele = element.addElement(entry.getKey());
				ele.addText("");
				mapToElementProp((Map<String, Object>)entry.getValue(), ele);
			}
		}
	}

	public static void main(String[] args) {
		String aString = "<persons id=\"1\">\r\n" +
				"    <person id=\"1\" sex=\"女\">\r\n" +
				"        <name>person1</name>\r\n" +
				"    </person>\r\n" +
				"    <person id=\"2\" sex=\"女\">\r\n" +
				"        <name>person2</name>\r\n" +
				"    </person>\r\n" +
				"    <person id=\"3\" sex=\"女\">\r\n" +
				"        <name>person3</name>\r\n" +
				"    </person>\r\n" +
				"    <person id=\"4\" sex=\"女\">\r\n" +
				"        <name>person4</name>\r\n" +
				"    </person>\r\n" +
				"    <person id=\"5\" sex=\"女\">\r\n" +
				"        <name>person5</name>\r\n" +
				"    </person>\r\n" +
				"</persons>";
		Map<String,Object> xmlPropToMap = xmlPropToMap(aString);
		System.out.println(xmlPropToMap);
		String mapToXmlProp = mapToXmlProp(xmlPropToMap, "persons");
		System.out.println(mapToXmlProp);

	}
}
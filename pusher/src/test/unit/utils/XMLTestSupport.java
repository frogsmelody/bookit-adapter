package utils;

import com.alibaba.fastjson.JSONObject;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.reflection.ReflectionConverter;
import com.thoughtworks.xstream.converters.reflection.SerializableConverter;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.Validate;
import org.dom4j.Document;
import org.dom4j.io.DocumentSource;
import org.dom4j.io.SAXReader;
import org.junit.Assert;
import org.junit.Ignore;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.xml.transform.StringResult;

import javax.xml.bind.helpers.AbstractMarshallerImpl;
import javax.xml.transform.Source;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@Ignore
@SuppressWarnings("ALL")
public abstract class XMLTestSupport extends JsonTestSupport {
    private static final XStream xStream = createXStream();

    private static Map<String, Jaxb2Marshaller> marshallerMap = new HashMap<String, Jaxb2Marshaller>();


    private static XStream createXStream() {
        XStream xstream = new XStream();
        xstream.setMode(XStream.NO_REFERENCES);
        xstream.registerConverter(new SerializableConverter(xstream.getMapper(), xstream.getReflectionProvider(), null), XStream.PRIORITY_LOW + 2);
        xstream.registerConverter(new ReflectionConverter(xstream.getMapper(), xstream.getReflectionProvider()), XStream.PRIORITY_LOW + 1);
        return xstream;
    }

    protected static String sortResultMap(String resultJsonText) {
        TreeMap<String, Object> result = new TreeMap<String, Object>();
        result.put("result", JSONObject.parseObject(resultJsonText).get("result"));
        result.put("message", JSONObject.parseObject(resultJsonText).get("message"));
        return JSONObject.toJSONString(result);
    }

    protected String toXml(Object object) {

        return xStream.toXML(object);
    }

    protected <T> T fromXml(String file, Class<T> clazz) {
        return (T) xStream.fromXML(getResourceAsStream(file));
    }

    protected static String readXml(InputStream inputStream) {
        try {
            return IOUtils.toString(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected String readXml(String file) {
        try {
            return IOUtils.toString(getResourceAsStream(file));
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException(e.getMessage());
        }
    }

    protected String marshal(Object source) {
        Validate.notNull(source);
        Jaxb2Marshaller marshaller = createMarshaller(source.getClass());

        StringResult stringResult = new StringResult();
        marshaller.marshal(source, stringResult);
        return stringResult.toString();
    }

    protected <T> T unmarshal(String file, Class<T> clazz, Jaxb2Marshaller marshaller) {
        try {
            Validate.notNull(file);
            Validate.notNull(marshaller);
            SAXReader reader = new SAXReader();
            Document document = reader.read(getResourceAsStream(file));
            Source source = new DocumentSource(document);

            return (T) marshaller.unmarshal(source);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e.getMessage());
        }
    }

    protected <T> T unmarshal(String file, Class<T> clazz) {
        Jaxb2Marshaller marshaller = createMarshaller(clazz);

        return unmarshal(file, clazz, marshaller);
    }

    private Jaxb2Marshaller createMarshaller(Class clazz) {
        String contextPath = clazz.getPackage().getName();

        if (marshallerMap.containsKey(contextPath)) {
            return marshallerMap.get(contextPath);
        }

        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        HashMap<String, Object> map = new HashMap<String, Object>();

        map.put(AbstractMarshallerImpl.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setMarshallerProperties(map);
        marshaller.setContextPath(contextPath);
        try {
            marshaller.afterPropertiesSet();
        } catch (Exception e) {
            e.printStackTrace();
        }
        marshallerMap.put(contextPath, marshaller);
        return marshaller;
    }

    protected static void assertEquals(String expected, String actual) {
        Assert.assertNotNull(expected);
        Assert.assertNotNull(actual);
        Assert.assertEquals(expected.replace(" ", "").replace("\r", "").replace("\n", ""),
                actual.replace(" ", "").replace("\r", "").replace("\n", ""));
    }

    private InputStream getResourceAsStream(String file) {
        InputStream resourceAsStream = getClass().getResourceAsStream(file);
        if (resourceAsStream == null) {
            throw new IllegalArgumentException(String.format("File not found [%s] in package [%s]",
                    file, getClass().getPackage().getName()));
        }
        return resourceAsStream;
    }
}

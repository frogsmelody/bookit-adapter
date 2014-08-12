package com.derbysoft.bookit.common.commons;
import com.derbysoft.bookit.dto.ObjectFactory;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;
import com.thoughtworks.xstream.converters.reflection.ReflectionConverter;
import com.thoughtworks.xstream.converters.reflection.SerializableConverter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.oxm.XmlMappingException;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.xml.transform.StringResult;
import org.springframework.xml.transform.StringSource;

import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.regex.Pattern;

public abstract class XMLUtils {
    private static final Pattern CARD_NUMBER_PATTERN = Pattern.compile("(?<=CardNumber=\")[\\d|\\w]*(?=\")");
    private static final Pattern EXPIRE_DATE_PATTERN = Pattern.compile("(?<=ExpireDate=\")[\\d|\\w]*(?=\")");

    private static final Logger LOGGER = LoggerFactory.getLogger(XMLUtils.class);
    private static XStream xStream = createXStream();

    private static Jaxb2Marshaller marshaller = createMarshaller();

    private static Jaxb2Marshaller createMarshaller() {
        Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
        jaxb2Marshaller.setContextPath(ObjectFactory.class.getPackage().getName());
        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        jaxb2Marshaller.setMarshallerProperties(properties);
        try {
            jaxb2Marshaller.afterPropertiesSet();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

        return jaxb2Marshaller;
    }

    private static XStream createXStream() {
        XStream xstream = new XStream();
        xstream.setMode(XStream.NO_REFERENCES);

        xstream.registerConverter(new XMLGregorianCalendarConvert(), XStream.PRIORITY_VERY_HIGH);
        xstream.registerConverter(new SerializableConverter(xstream.getMapper(), xstream.getReflectionProvider(), null), XStream.PRIORITY_LOW + 2);
        xstream.registerConverter(new ReflectionConverter(xstream.getMapper(), xstream.getReflectionProvider()), XStream.PRIORITY_LOW + 1);
        return xstream;
    }

    public static String toXML(Object value) {
        try {
            if (String.class.isInstance(value)) {
                return value.toString();
            }
            if (value != null) {
                XmlRootElement annotation = value.getClass().getAnnotation(XmlRootElement.class);
                if (annotation != null) {
                    return marshal(value);
                } else {
                    return xStream.toXML(value);
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return "";
    }

    public static String marshal(Object value) {
        StringResult result = new StringResult();
        marshaller.marshal(value, result);
        String response = result.toString();
        if (containsPaymentCard(response)) {
            return hideExpireDate(hideCardNumber(response));
        }
        return response;
    }

    private static String hideCardNumber(String input) {
        return CARD_NUMBER_PATTERN.matcher(input).replaceAll("xxxxxxxxxxxxxxxx");
    }

    private static String hideExpireDate(String input) {
        return EXPIRE_DATE_PATTERN.matcher(input).replaceAll("xxxx");
    }

    private static boolean containsPaymentCard(String response) {
        return StringUtils.containsIgnoreCase(response, "Booking") && StringUtils.containsIgnoreCase(response, "PaymentCard");
    }

    @SuppressWarnings("unchecked")
    public static <T> T unMarshal(String xml) {
        return (T) marshaller.unmarshal(new StringSource(xml));
    }

    public static String fromXMLWithString(String logDetail) {
        try {
            InputStream inputStream = new ByteArrayInputStream(logDetail.getBytes("UTF-8"));
            Object object = marshaller.unmarshal(new StreamSource(inputStream));
            return toXML(object);
        } catch (XmlMappingException e) {
            return logDetail;
        } catch (UnsupportedEncodingException e) {
            return logDetail;
        }
    }

    private static class XMLGregorianCalendarConvert extends AbstractSingleValueConverter {

        @Override
        public boolean canConvert(Class type) {
            return type.getGenericSuperclass() == XMLGregorianCalendar.class;
        }

        @Override
        public Object fromString(String str) {
            return null;
        }

        @Override
        public String toString(Object obj) {
            return obj.toString();
        }
    }
}

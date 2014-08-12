package utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import org.apache.commons.lang.Validate;
import org.junit.Assert;
import org.junit.Ignore;

import java.io.*;
import java.util.List;

/**
 * User: jason
 * Date: 2013-02-28
 */
@Ignore
@SuppressWarnings("ALL")
public abstract class JsonTestSupport {

    protected static void assertEquals(String expected, String actual) {
        Assert.assertNotNull(expected);
        Assert.assertNotNull(actual);
        Assert.assertEquals(expected.replace(" ", "").replace("\r", "").replace("\n", ""),
                actual.replace(" ", "").replace("\r", "").replace("\n", ""));
    }

    protected <T> T parseJsonObject(String jsonObject, Class<T> clazz) {
        return JSONObject.parseObject(jsonObject, clazz);
    }

    protected <T> List<T> parseJsonArray(String jsonArray, Class<T> clazz) {
        return JSONArray.parseArray(jsonArray, clazz);
    }

    protected String formatArrayToJson(Object array) {
        return JSONArray.toJSONString(array,SerializerFeature.DisableCircularReferenceDetect);
    }

    protected String formatArrayToJson(Object array, String... excludes) {
        SimplePropertyPreFilter simplePropertyPreFilter = new SimplePropertyPreFilter();
        if (excludes != null && excludes.length > 0) {
            for (String exclude : excludes) {
                simplePropertyPreFilter.getExcludes().add(exclude);
            }
        }
        return JSONArray.toJSONString(array, simplePropertyPreFilter);
    }

    protected String formatObjectToJson(Object object, String... excludes) {
        SimplePropertyPreFilter simplePropertyPreFilter = new SimplePropertyPreFilter();
        if (excludes != null && excludes.length > 0) {
            for (String exclude : excludes) {
                simplePropertyPreFilter.getExcludes().add(exclude);
            }
        }
        return JSONObject.toJSONString(object, simplePropertyPreFilter, SerializerFeature.DisableCircularReferenceDetect);
    }

    protected String formatJsonFile(String fileName) throws IOException {
        return JSONObject.parseObject(readJsonFile(fileName)).toJSONString();
    }

    private String readJsonFile(String fileName) throws IOException {
        String resource = this.getClass().getPackage().getName().replace(".", "//") + File.separator + fileName;
        StringBuilder builder = new StringBuilder();
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(resource);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while (reader.ready() && (line = reader.readLine()) != null) {
            builder.append(line);
        }
        return builder.toString();
    }

    public String readFile(String file) {
        try {
            Validate.notNull(file);
            InputStream inputStream = getResourceAsStream(file);
            BufferedInputStream stream = new BufferedInputStream(inputStream);
            StringBuilder builder = new StringBuilder();
            byte[] collection = new byte[1024];
            int read;
            while ((read = stream.read(collection)) != -1) {
                builder.append(new String(collection, 0, read));
            }
            return builder.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    private InputStream getResourceAsStream(String file) {
        InputStream resourceAsStream = this.getClass().getResourceAsStream(file);
        if (resourceAsStream == null) {
            throw new IllegalArgumentException(String.format("File not found [%s] in package [%s]",
                    file, this.getClass().getPackage().getName()));
        }
        return resourceAsStream;
    }
}

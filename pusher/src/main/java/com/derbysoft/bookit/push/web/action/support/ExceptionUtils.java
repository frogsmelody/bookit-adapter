package com.derbysoft.bookit.push.web.action.support;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;

public abstract class ExceptionUtils {
    private static Log logger = LogFactory.getLog(ExceptionUtils.class);
    private static final  String EOL = "\n";
    private static final String PROPERTY_KEY = "com.derbysoft.core.utils.ExceptionUtils.keyWords";
    private static final String [] DEFAULT_KEY_WORDS = {"com.derbysoft"};

    public static String toString(Throwable t) {
        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        String[] keyWords = getKeyWords();
        if (keyWords.length == 0) {
            return sw.toString();
        }
        BufferedReader reader = new BufferedReader(new StringReader(sw.toString()));
        StringBuilder sb = new StringBuilder();
        try {
            sb.append(reader.readLine());

            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }

                if (contains(line, keyWords)) {
                    sb.append(EOL).append(line);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return sb.toString();
    }

    private static boolean contains(String line, String[] keyWords) {
        for (String keyWord : keyWords) {
            if (line.contains(keyWord)) {
                return true;
            }
        }
        return false;
    }

    private static String [] getKeyWords() {
        if (System.getProperties().containsKey(PROPERTY_KEY)) {
            return StringUtils.split(System.getProperty(PROPERTY_KEY), ",");
        } else {
            return DEFAULT_KEY_WORDS;
        }
    }
}
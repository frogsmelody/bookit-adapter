package com.derbysoft.bookit.push.ws.impl;

import com.derbysoft.bookit.common.ccs.SystemConfig;
import com.derbysoft.bookit.common.model.SystemConfigKeys;
import com.derbysoft.bookit.common.repository.SystemConfigRepository;
import com.derbysoft.bookit.push.web.action.support.ExceptionUtils;
import com.derbysoft.bookit.push.ws.WebService;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.CloseShieldInputStream;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.FastDateFormat;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

@Service("webService")
public class WebServiceImpl implements WebService {
    private static final String TIME_STAMP_PATTERN = "yyyyMMddHHmmss";

    private static final int TEN_MINUTES = 10 * 60 * 1000;

    @Value("${bookit.update.rate}")
    private String pushRateChangeURL;

    @Value("${require.proxy}")
    private String requireProxy;

    @Autowired
    private SystemConfigRepository systemConfigRepository;

    public String send(String request) {
        try {
            DefaultHttpClient httpClient = createHttpClient();
            HttpPost httpPost = new HttpPost(pushRateChangeURL);
            httpPost.setHeader("Content-type", "text/xml; charset=UTF-8");
            httpPost.setEntity(new StringEntity(buildSOAPMessageBody(request), ContentType.create("text/xml", "UTF-8")));
            HttpResponse httpResponse = httpClient.execute(httpPost);
            InputStream inputStream = httpResponse.getEntity().getContent();
            String response = IOUtils.toString(new CloseShieldInputStream(inputStream), "UTF-8");
            EntityUtils.consume(httpResponse.getEntity());
            System.out.println(response);
            return response;
        } catch (Exception ex) {
            return ExceptionUtils.toString(ex);
        }
    }

    private static String buildSOAPMessageBody(String request) {
        StringBuilder builder = new StringBuilder();
        builder.append("<soap-env:Envelope xmlns:soap-env=\"http://schemas.xmlsoap.org/soap/envelope/\">");
        builder.append("<soap-env:Header>");
        builder.append("<Interface ChannelIdentifierId=\"BookIt.com XML4\" Version=\"2011A\" Interface=\"SourceSys XML\"");
        builder.append("xmlns=\"http://www.bookit.com/api/Documentation/XML/OTA/4/2011A/\">");
        builder.append("<ComponentInfo Id=\"1727782\" User=\"Hy4TpT3ri\" Pwd=\"4lFAwE5t\" ComponentType=\"Hotel\"/>");
        builder.append("</Interface>");
        builder.append("</soap-env:Header>");
        builder.append(String.format("<soap-env:Body RequestId=\"%s\" Transaction=\"HotelRateAmountNotifRQ\">", UUID.randomUUID().toString()));
        builder.append(StringUtils.remove(request, "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"));
        builder.append("</soap-env:Body>");
        builder.append("</soap-env:Envelope>");
        return builder.toString();
    }

    private DefaultHttpClient createHttpClient() {
        DefaultHttpClient client = new DefaultHttpClient();
        client.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, getTimeOut(SystemConfigKeys.CONNECTION_TIMEOUT_SECONDS.getKey()));
        client.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT, getTimeOut(SystemConfigKeys.SOCKET_TIMEOUT_SECONDS.getKey()));
        client.getParams().setParameter(CookiePolicy.IGNORE_COOKIES, true);
        if (Boolean.valueOf(requireProxy)) {
            client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, new HttpHost("54.245.251.221", 65443, "http"));
            client.getCredentialsProvider().setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("derby", "JfQY-gvllGsF-NbB8N2M"));
        }
        client.getParams().setParameter(HttpHeaders.AUTHORIZATION, String.format("Basic %s", getAuthorization()));
        return client;
    }

    private String getAuthorization() {
        SystemConfig systemConfig = systemConfigRepository.load(SystemConfigKeys.USERNAME_PASSWORD.getKey());
        if (systemConfig == null) {
            throw new IllegalStateException(String.format("System config not found,key:%s", SystemConfigKeys.USERNAME_PASSWORD.getKey()));
        }
        String[] fields = StringUtils.split(systemConfig.getValue(), com.derbysoft.common.util.Constants.COLON);
        return Base64.encodeBase64String(String.format("%s:%s", fields[0], fields[1]).getBytes());
    }

    private int getTimeOut(String connectionTimeout) {
        SystemConfig systemConfig = systemConfigRepository.load(connectionTimeout);
        if (systemConfig == null) {
            return TEN_MINUTES;
        }
        return Integer.parseInt(systemConfig.getValue()) * 1000;
    }

    public void setPushRateChangeURL(String pushRateChangeURL) {
        this.pushRateChangeURL = pushRateChangeURL;
    }

    public void setSystemConfigRepository(SystemConfigRepository systemConfigRepository) {
        this.systemConfigRepository = systemConfigRepository;
    }

    public void setRequireProxy(String requireProxy) {
        this.requireProxy = requireProxy;
    }
}

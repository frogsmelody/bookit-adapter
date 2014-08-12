package protocaltest;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.CloseShieldInputStream;
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
import org.junit.Test;
import utils.XMLTestSupport;

import java.io.IOException;
import java.io.InputStream;

public class PushLOSRateTest extends XMLTestSupport {

    @Test
    public void testPush() throws Exception {
        send("http://api.bookitintegration.com/ota", readFile("PushLOSRateRQ2.xml"));
    }

    private void send(String url, String request) throws IOException {
        DefaultHttpClient httpClient = createHttpClient();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-type", "text/xml; charset=UTF-8");
        httpPost.setEntity(new StringEntity(request, ContentType.create("text/xml", "UTF-8")));
        HttpResponse httpResponse = httpClient.execute(httpPost);
        InputStream inputStream = httpResponse.getEntity().getContent();
        String response = IOUtils.toString(new CloseShieldInputStream(inputStream), "UTF-8");
        EntityUtils.consume(httpResponse.getEntity());
        System.out.println(response);
    }

    private DefaultHttpClient createHttpClient() {
        DefaultHttpClient client = new DefaultHttpClient();
        client.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 60000);
        client.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 60000);
        client.getParams().setParameter(CookiePolicy.IGNORE_COOKIES, true);
        client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, new HttpHost("54.245.251.221", 65443, "http"));
        client.getCredentialsProvider().setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("derby", "JfQY-gvllGsF-NbB8N2M"));
        String authorization = Base64.encodeBase64String(String.format("%s:%s", "Hy4TpT3ri", "4lFAwE5t").getBytes());
        client.getParams().setParameter(HttpHeaders.AUTHORIZATION, String.format("Basic %s", authorization));
        return client;
    }
}

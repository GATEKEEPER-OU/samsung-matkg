package org.fhir;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author Riccardo Pala (riccardo.pala@open.ac.uk)
 * @todo descriptions
 * */
public class FHIRClient implements AutoCloseable {

  /**
   * @todo description
   * */
  public static FHIRClient connect(Properties config) throws IOException {
    String host = config.getProperty("fhir_host");
    FHIRClient instance = new FHIRClient(host);
    instance.authenticate(config);
    return instance;
  }

  /**
   * @todo description
   * */
  public JSONArray getPatients() throws IOException {
    // @todo
    return new JSONArray();
  }

  /**
   * @todo description
   * */
  public JSONArray getObservations(String from, String to) throws IOException {
    String url = httpGetObservation(from, to);
    HttpGet httpGet = new HttpGet(url);
    httpGet.addHeader("Authorization", "JWT " + session.getString("token"));
    JSONArray results;
    try (CloseableHttpResponse httpResponse = HTTP_CLIENT.execute(httpGet)) {
      String response = EntityUtils.toString(httpResponse.getEntity());
      results = new JSONArray(response);
    }
    return results;
  }

  /**
   * @throws IOException
   */
  public void close() throws IOException {
    HTTP_CLIENT.close();
  }

  //--------------------------------------------------------------------------//
  // Class definition
  //--------------------------------------------------------------------------//

  //  private static final Logger LOGGER = LoggerFactory.getLogger(FHIRClient.class); // @todo

  /** @todo */
  protected final String BASEURL_TEMPLATE;

  /** @todo */
  protected final String AUTHURL_TEMPLATE;

  /** @todo */
  protected final String GETURL_TEMPLATE;

  /** @todo */
  protected final CloseableHttpClient HTTP_CLIENT;

  /** @todo */
  private JSONObject session;

  /**
   * @todo description
   * */
  protected FHIRClient(String host) {
    BASEURL_TEMPLATE = "https://" + host;
    AUTHURL_TEMPLATE = BASEURL_TEMPLATE + "/api/auth";
    GETURL_TEMPLATE  = BASEURL_TEMPLATE + "/api/__SESSID__/shealth/fhir";
    HTTP_CLIENT = HttpClients.createDefault();
  }

  /**
   * @todo description
   * */
  protected String getSessionId() {
    // @todo check session ?
    return session
      .getJSONObject("user")
      .getString("_id");
  }

  /**
   * @todo description
   * */
  protected String httpGetObservation(String from, String to) {
    List<NameValuePair> params = new ArrayList<>(2);
    if (from != null )
      params.add(new BasicNameValuePair("from", from));
    if (to != null )
      params.add(new BasicNameValuePair("to", to));

    String urlByUser = GETURL_TEMPLATE.replace("__SESSID__", getSessionId());
    StringBuilder url = new StringBuilder(urlByUser);
    if (!params.isEmpty()) {
      String encodedParams = URLEncodedUtils.format(params, StandardCharsets.UTF_8);
      url.append("?" + encodedParams);
    }
    return url.toString();
  }

  /**
   * @todo description
   * */
  protected void authenticate(Properties config) throws IOException {
    List<NameValuePair> params = new ArrayList<>(2);
    params.add(new BasicNameValuePair("email", config.getProperty("fhir_email")));
    params.add(new BasicNameValuePair("password", config.getProperty("fhir_password")));

    HttpPost httpPost = new HttpPost(AUTHURL_TEMPLATE);
    httpPost.setEntity(new UrlEncodedFormEntity(params));

    try (CloseableHttpResponse httpResponse = HTTP_CLIENT.execute(httpPost)) {
      String response = EntityUtils.toString(httpResponse.getEntity());
      session = new JSONObject(response);

      if (!session.has("token")) {
        // @todo handle auth error
        System.out.println(" status >>> " + httpResponse.getStatusLine()); // DEBUG
      }
    }
  }

}
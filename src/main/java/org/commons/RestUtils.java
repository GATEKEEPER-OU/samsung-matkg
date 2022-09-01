package org.commons;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @todo
 * @author Riccardo Pala (riccardo.pala@open.ac.uk)
 * */
public class RestUtils {

  /**
   * @param httpClient
   * @param postUrl
   * @param content
   * @throws IOException
   */
  public static void makePost(CloseableHttpClient httpClient, String postUrl, File content) throws IOException {
    // Set request headers
    InputStreamEntity reqEntity = new InputStreamEntity(
        new FileInputStream(content),
        -1,
        ContentType.TEXT_PLAIN
    );
    reqEntity.setChunked(true);

    // Init post request
    HttpPost httpPost = new HttpPost(postUrl);
    httpPost.setEntity(reqEntity);

//    System.out.println("Executing request: " + httpPost.getRequestLine()); // DEBUG
    CloseableHttpResponse response = httpClient.execute(httpPost);

//    System.out.println(response.getStatusLine()); // DEBUG
//    System.out.println(EntityUtils.toString(response.getEntity())); // DEBUG
    response.close();
  }

  /**
   * @todo description
   */
  private RestUtils() {
  }

}

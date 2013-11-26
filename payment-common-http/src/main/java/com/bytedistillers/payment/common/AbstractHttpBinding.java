package com.bytedistillers.payment.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.auth.params.AuthPNames;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.AuthPolicy;
import org.apache.http.impl.client.SystemDefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractHttpBinding {
  private static final Logger logger = LoggerFactory.getLogger(AbstractHttpBinding.class);

  private HttpClient httpClient;
  private DocumentBuilder docBuilder;

  protected AbstractHttpBinding() throws ParserConfigurationException {
    this(new SystemDefaultHttpClient());
    httpClient.getParams().setParameter(AuthPNames.TARGET_AUTH_PREF, Collections.singletonList(AuthPolicy.BASIC));
  }

  protected AbstractHttpBinding(HttpClient httpClient) throws ParserConfigurationException {
    this.httpClient = httpClient;
    docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
  }

  protected HttpClient getHttpClient() {
    return httpClient;
  }

  protected String executeHttpRequest(HttpPost httpPost) {
    HttpClient httpClient = getHttpClient();

    InputStream contentStream = null;
    try {
      HttpResponse response = httpClient.execute(httpPost);

      HttpEntity entity = response.getEntity();
      String content;
      if (entity != null) {
        content = EntityUtils.toString(entity);
      } else {
        content = null;
      }
      return content;
    } catch (ClientProtocolException e) {
      logger.error("ClientProtocolException while executing request: " + e.getMessage(), e);
      throw new RuntimeException(e);
    } catch (IOException e) {
      logger.error("IOException while executing request: " + e.getMessage(), e);
      throw new RuntimeException(e);
    } finally {
      if (contentStream != null) {
        try {
          contentStream.close();
        } catch (IOException e) {
          logger.error("Exception while closing contentStream: " + e.getMessage(), e);
        }
      }
    }
  }

  protected DocumentBuilder getDocBuilder() {
    return docBuilder;
  }

  protected void setHttpClientCredentials(String username, String password) {
    SystemDefaultHttpClient systemClient = (SystemDefaultHttpClient) getHttpClient();
    systemClient.getCredentialsProvider().setCredentials(new AuthScope(null, -1, null), new UsernamePasswordCredentials(username, password));
  }
}
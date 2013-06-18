package com.bytedistillers.payment.sofort.gateway;

import java.io.StringReader;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.bytedistillers.payment.common.AbstractHttpBinding;
import com.bytedistillers.payment.sofort.gateway.dto.ErrorData;
import com.bytedistillers.payment.sofort.gateway.dto.SofortTransactionParameters;
import com.bytedistillers.payment.sofort.gateway.dto.TransactionData;
import com.bytedistillers.payment.sofort.gateway.dto.TransactionErrors;
import com.bytedistillers.payment.sofort.gateway.dto.TransactionResponse;
import com.bytedistillers.payment.sofort.gateway.dto.TransactionStatus;
import com.bytedistillers.payment.sofort.gateway.xml.Multipay;

public class SofortTemplate extends AbstractHttpBinding {

  private static final Logger logger = LoggerFactory.getLogger(SofortTemplate.class);
  private static final SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");

  private String host = "https://api.sofort.com";

  private SofortTransactionParameters transactionParameters;

  public TransactionResponse startTransaction() {
    HttpPost httpPost = generatePost(createRequestXml(transactionParameters));

    String responseXml = executeHttpRequest(httpPost);
    logger.info("responseXml: {}", responseXml);
    TransactionResponse response = parseResponseXml(responseXml);

    return response;
  }

  private HttpPost generatePost(String requestXml) {
    HttpPost httpPost = new HttpPost(host + "/api/xml");
    String headerValue = "application/xml; charset=UTF-8";
    httpPost.setHeader("Content-Type", headerValue);
    httpPost.setHeader("Accept", headerValue);

    StringEntity entity;
    entity = new StringEntity(requestXml, ContentType.APPLICATION_XML);
    httpPost.setEntity(entity);
    return httpPost;
  }

  private String createRequestXml(SofortTransactionParameters transactionData) {
    Multipay data = new Multipay(transactionParameters.getProjectId(), transactionData.getAmount(), transactionData.getCurrencyCode());
    data.setReasons(transactionData.getReasons());

    Document result = createMultipayDocument(data);
    String xmlString = stringFromXml(result);

    logger.info("requestXml: {}", xmlString);

    return xmlString;
  }

  private Document createMultipayDocument(Multipay data) {
    Document result = createDocument();

    Element rootNode = result.createElement("multipay");
    result.appendChild(rootNode);

    rootNode.appendChild(createNode(result, "project_id", data.getProjectId() + ""));
    //    rootNode.appendChild(createNode(result, "language_code", "de"));
    //    rootNode.appendChild(createNode(result, "interface_version", "pn_test_1"));
    rootNode.appendChild(createNode(result, "amount", data.getAmount().toPlainString()));
    rootNode.appendChild(createNode(result, "currency_code", data.getCurrencyCode()));

    rootNode.appendChild(generateReasonsNode(data, result));

    rootNode.appendChild(createNode(result, "success_url", transactionParameters.getSuccessUrl()));
    rootNode.appendChild(createNode(result, "abort_url", transactionParameters.getCancelUrl()));

    Element suNode = result.createElement("su");
    //    suNode.appendChild(createNode(result, "amount", "345.45"));

    rootNode.appendChild(suNode);

    return result;
  }

  protected Document createDocument() {
    return getDocBuilder().newDocument();
  }

  private Element generateReasonsNode(Multipay data, Document result) {
    List<String> reasons;
    if (data.getReasons() == null || data.getReasons().size() == 0) {
      reasons = transactionParameters.getReasons();
    } else {
      reasons = data.getReasons();
    }
    Element reasonsNode = result.createElement("reasons");
    for (String reason : reasons) {
      String compiledReason = reason.replace("[date]", df.format(new Date()));
      Node reasonNode = createNode(result, "reason", compiledReason);

      reasonsNode.appendChild(reasonNode);
    }
    return reasonsNode;
  }

  private Node createNode(Document doc, String tagName, String tagValue) {
    Element result = doc.createElement(tagName);
    CDATASection cdataSection = doc.createCDATASection(tagValue);
    result.appendChild(cdataSection);
    return result;
  }

  private String stringFromXml(Document doc) {
    TransformerFactory tf = TransformerFactory.newInstance();
    Transformer transformer;
    try {
      transformer = tf.newTransformer();
      transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
      StringWriter writer = new StringWriter();
      transformer.transform(new DOMSource(doc), new StreamResult(writer));
      String output = writer.getBuffer().toString().replaceAll("\n|\r", "");
      return output;
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      throw new RuntimeException(e.getMessage(), e);
    }
  }

  private TransactionResponse parseResponseXml(String responseXml) {
    Document doc = generateDocumentFromString(responseXml);
    Node rootNode = doc.getFirstChild();

    TransactionResponse result = new TransactionResponse();

    if (rootNode.getNodeName().equals("errors")) {
      TransactionErrors transactionErrors = extractErrorData(doc);
      result.setTransactionErrors(transactionErrors);
    } else {
      TransactionData transactionData = extractTransactionData(doc);
      result.setTransactionData(transactionData);
    }

    return result;

    /*
    } catch (SAXException e) {
    logger.error("SAXException while trying to parse new_transaction document: " + e.getMessage(), e);
    } catch (IOException e) {
    logger.error("IOException while trying to parse new_transaction document: " + e.getMessage(), e);
    }
    */
  }

  private TransactionErrors extractErrorData(Document doc) {
    TransactionErrors result = new TransactionErrors();

    NodeList errorNodes = doc.getElementsByTagName("error");
    int nrNodes = errorNodes.getLength();
    for (int idx = 0; idx < nrNodes; ++idx) {
      Node node = errorNodes.item(idx);
      ErrorData errorData = new ErrorData();

      NodeList childNodes = node.getChildNodes();
      int nrChildren = childNodes.getLength();
      for (int childIdx = 0; childIdx < nrChildren; ++childIdx) {
        Node child = childNodes.item(childIdx);
        String nodeName = child.getNodeName();
        String nodeValue = child.getTextContent();
        if ("code".equals(nodeName)) {
          errorData.setCode(Integer.parseInt(nodeValue));
        } else if ("message".equals(nodeName)) {
          errorData.setMessage(nodeValue);
        } else if ("field".equals(nodeName)) {
          errorData.setField(nodeValue);
        } else {
          logger.warn("Unknown node: {} with value {}", nodeName, nodeValue);
        }
      }

      result.addErrorData(errorData);
    }

    return result;
  }

  private TransactionData extractTransactionData(Document doc) {
    Node transactionNode = doc.getElementsByTagName("transaction").item(0);
    Node paymentUrlNode = doc.getElementsByTagName("payment_url").item(0);

    TransactionData transactionData = new TransactionData(transactionNode.getTextContent(), paymentUrlNode.getTextContent());
    return transactionData;
  }

  private Document generateDocumentFromString(String responseXml) {
    StringReader xmlReader = new StringReader(responseXml);
    Document doc;
    try {
      doc = getDocBuilder().parse(new InputSource(xmlReader));
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage(), e);
    }
    return doc;
  }

  public TransactionStatus queryTransactionStatus(String transactionId) {
    String requestXml = createTransactionStatusRequestXml(transactionId);

    HttpPost httpPost = generatePost(requestXml);
    String responseXml = executeHttpRequest(httpPost);
    TransactionStatus result = parseTransactionStatus(responseXml);

    return result;
  }

  private String createTransactionStatusRequestXml(String transactionId) {
    Document result = getDocBuilder().newDocument();

    Element rootNode = result.createElement("transaction_request");
    rootNode.setAttribute("version", "2");
    result.appendChild(rootNode);

    Node transNode = createNode(result, "transaction", transactionId);
    rootNode.appendChild(transNode);

    return stringFromXml(result);
  }

  private TransactionStatus parseTransactionStatus(String responseXml) {
    TransactionStatus result = new TransactionStatus();
    Document doc = generateDocumentFromString(responseXml);

    return result;
  }

  public SofortTransactionParameters getTransactionParameters() {
    return transactionParameters;
  }

  public void setTransactionParameters(SofortTransactionParameters transactionParameters) {
    this.transactionParameters = transactionParameters;

    setHttpClientCredentials(transactionParameters.getCustomerId(), transactionParameters.getApiKey());
  }

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

}

package com.jamesward.census2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class SendCensusResult
{
  public static void sendResult(String sendCensusResultURL, String clientId, String testId, String resultType, long resultData, Boolean gzip, Integer numRows) throws ClientProtocolException, IOException
  {
    DefaultHttpClient httpclient = new DefaultHttpClient();
    
    List<NameValuePair> qparams = new ArrayList<NameValuePair>();
    qparams.add(new BasicNameValuePair("clientId", clientId));
    qparams.add(new BasicNameValuePair("testId", testId));
    qparams.add(new BasicNameValuePair("resultType", resultType));
    qparams.add(new BasicNameValuePair("resultData", (new Long(resultData)).toString()));
    qparams.add(new BasicNameValuePair("gzip", gzip.toString()));
    qparams.add(new BasicNameValuePair("numRows", numRows.toString()));

    HttpGet httpget = new HttpGet(sendCensusResultURL + "?" + URLEncodedUtils.format(qparams, "UTF-8"));
    
    httpclient.execute(httpget);
    httpclient.getConnectionManager().shutdown();
  }
}
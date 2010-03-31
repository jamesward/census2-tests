package com.jamesward.census2;

import java.io.IOException;

import javax.servlet.ServletException;

import org.apache.catalina.Valve;
import org.apache.catalina.valves.ValveBase;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.coyote.http11.OutputFilter;
import org.apache.coyote.http11.filters.GzipOutputFilter;

public class CensusValve extends ValveBase implements Valve
{
  public void invoke(Request request, Response response) throws IOException, ServletException
  {
    long contentLength = 0;
    
    String sendCensusResultURL = request.getParameter("sendCensusResultURL");
    String clientId = request.getParameter("clientId");
    String testId = request.getParameter("testId");
    Boolean gzip = false;

    if ((request.getParameter("gzip") != null) && (request.getParameter("gzip").equals("true")))
    {
      gzip = true;
    }

    if ( !gzip || (request.getParameter("resultType") != null) )
    {
      request.getCoyoteRequest().getMimeHeaders().removeHeader("accept-encoding");
    }

    long startTime = System.currentTimeMillis();

    getNext().invoke(request, response);

    if ( (request.getParameter("rows") != null) && (clientId != null) && 
        (testId != null) && (sendCensusResultURL != null) )
    {
      
      if (gzip)
      {
        ((org.apache.coyote.http11.InternalOutputBuffer)response.getCoyoteResponse().getOutputBuffer()).flush();
        
        OutputFilter[] filters = ((org.apache.coyote.http11.InternalOutputBuffer)response.getCoyoteResponse().getOutputBuffer()).getFilters();
        
        for (int i = 0; i < filters.length; i++)
        {
          if (filters[i] instanceof GzipOutputFilter)
          {
            contentLength = ((GzipOutputFilter)filters[i]).getBytesWritten();
          }
        }
      }
      else
      {
        contentLength = response.getContentCountLong();
      }
      
      long execTime = System.currentTimeMillis() - startTime;
      
      try
      {
        Integer numRows = Integer.parseInt(request.getParameter("rows"));

        SendCensusResult.sendResult(sendCensusResultURL, clientId, testId, "totalServerTime", execTime, gzip, numRows);
        SendCensusResult.sendResult(sendCensusResultURL, clientId, testId, "contentLength", contentLength, gzip, numRows);
      }
      catch (Exception e)
      {
        System.out.println("Error: " + e.getMessage());
        e.printStackTrace();
      }

    }
  }
}
/*
Copyright (C) 2007 James Ward
http://www.jamesward.org

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License along
with this program; if not, write to the Free Software Foundation, Inc.,
51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
*/

package com.jamesward.census2;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;


public class ByteArrayTrafficFilter implements Filter
{

  public void destroy() {}

  public void doFilter(ServletRequest request, ServletResponse response,
    FilterChain chain) throws IOException, ServletException
  {
    long startTime = System.currentTimeMillis();

    int contentLength;
    long execTime;

    try
    {
      ByteArrayResponseWrapper wrapper = new ByteArrayResponseWrapper(
        (HttpServletResponse) response); 
      chain.doFilter(request, wrapper);
      contentLength = wrapper.getOutputAsByteArray().length;
      execTime = System.currentTimeMillis() - startTime;
      response.getOutputStream().write(wrapper.getOutputAsByteArray());
    }
    catch (Exception e)
    {
      throw new ServletException(e);
    }
    finally
    {
    }

    if (request.getParameter("wsdl") == null)
    {
      try
      {
        String sendCensusResultURL = request.getParameter("sendCensusResultURL");
        String clientId = request.getParameter("clientId");
        String testId = request.getParameter("testId");
        
        SendCensusResult.sendResult(sendCensusResultURL, clientId, testId, "totalServerTime", execTime);
        SendCensusResult.sendResult(sendCensusResultURL, clientId, testId, "contentLength", contentLength);
      }
      catch (Exception e)
      {
        System.out.println("Error: " + e.getMessage());
        e.printStackTrace();
      }
    }

  }

  public void init(FilterConfig filterConfig) throws ServletException
  {
    
  }

}
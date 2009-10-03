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
import java.util.Calendar;
import java.util.Hashtable;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import flex.messaging.MessageBroker;
import flex.messaging.messages.AsyncMessage;
import flex.messaging.util.UUIDUtils;


public class ByteArrayTrafficFilter implements Filter
{

  public void destroy() {}

  public void doFilter(ServletRequest request, ServletResponse response,
    FilterChain chain) throws IOException, ServletException
  {
    Calendar startTime = Calendar.getInstance();

    int contentLength;
    long execTime;

    try
    {
      ByteArrayResponseWrapper wrapper = new ByteArrayResponseWrapper(
        (HttpServletResponse) response); 
      chain.doFilter(request, wrapper);
      contentLength = wrapper.getOutputAsByteArray().length;
      execTime = Calendar.getInstance().getTimeInMillis() - startTime.getTimeInMillis();
      response.getOutputStream().write(wrapper.getOutputAsByteArray());
    }
    catch (Exception e)
    {
      throw new ServletException(e);
    }
    finally
    {
    }

    try
    {
      // send the time it took to the right place
      /*
      
      String id = request.getParameter("id");
      if (id == null)
      {
        String referer = ((HttpServletRequest)request).getHeader("referer");
        if (referer != null && referer.indexOf("DYNAMIC") == -1) // filter out polling requests
        {
          System.out.println("referer = " + referer);
          id = referer.substring(referer.lastIndexOf("/")+1, referer.length() - 4);
          System.out.println("id = " + id);
          if (id.indexOf(".") > -1)
          {
            id = id.substring(0, id.indexOf("."));
          }
        }
      }
    
      System.out.println("id = " + id);
      System.out.println("contentLength = " + contentLength);
      System.out.println("execTime = " + execTime);

      if ((id != null) && (!id.equals("index")))
      {
        Hashtable<String, Object> body = new Hashtable<String, Object>();
        body.put("id", id);
        body.put("contentLength", contentLength);
        body.put("execTime", execTime);

        AsyncMessage msg = new AsyncMessage();

        String clientId = UUIDUtils.createUUID(false);
        //System.out.println("clientId = " + clientId);
        msg.setClientId(clientId);

        String messageId = UUIDUtils.createUUID(false);
        //System.out.println("messageId = " + messageId);
        msg.setMessageId(messageId);

        String sessionId = session.getId();
        //System.out.println("sessionId = " + sessionId);
        msg.setHeader(AsyncMessage.SUBTOPIC_HEADER_NAME, sessionId);

        msg.setDestination("accessLogDestination");
        msg.setTimestamp(System.currentTimeMillis());
        msg.setBody(body);

        //System.out.println("getting the message broker");

        MessageBroker mb = MessageBroker.getMessageBroker(null);

        //System.out.println("got the message broker, sending message");

        mb.routeMessageToService(msg, null);

        //System.out.println("message sent");
         
      }
       */
    }
    catch (Exception e)
    {
      System.out.println("Error: " + e.getMessage());
      e.printStackTrace();
    }

  }

  public void init(FilterConfig filterConfig) throws ServletException
  {
    
  }

}
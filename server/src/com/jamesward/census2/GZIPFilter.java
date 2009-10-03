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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class GZIPFilter implements Filter
{

  public void destroy() {}

  public void doFilter(ServletRequest req, ServletResponse res,
    FilterChain chain) throws IOException, ServletException
  {
    HttpServletRequest request = (HttpServletRequest) req;
    HttpServletResponse response = (HttpServletResponse) res;

    String gzip = request.getParameter("gzip");
    String ae = request.getHeader("accept-encoding");
    if ((ae != null) && (ae.indexOf("gzip") != -1) && (gzip != null))
    {
      GZIPResponseWrapper wrappedResponse = new GZIPResponseWrapper(response);
      chain.doFilter(req, wrappedResponse);
      wrappedResponse.finishResponse();
    }
    else
    {
      chain.doFilter(req, res);
    }

  }

  public void init(FilterConfig config) throws ServletException
  {

  }

}

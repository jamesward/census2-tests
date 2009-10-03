package com.jamesward.census2;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Wraps Response for GZIPFilter
 * 
 * @author Matt Raible
 * @version $Revision: 1.1 $ $Date: 2007/08/05 20:37:41 $
 */
public class GZIPResponseWrapper extends HttpServletResponseWrapper {
  private final static Log log = LogFactory.getLog(GZIPResponseWrapper.class);

  protected HttpServletResponse origResponse = null;
  protected ServletOutputStream stream = null;
  protected PrintWriter writer = null;
  protected int error = 0;

  public GZIPResponseWrapper(HttpServletResponse response) {
    super(response);
    origResponse = response;
  }

  public ServletOutputStream createOutputStream() throws IOException {
    return (new GZIPResponseStream(origResponse));
  }

  public void finishResponse() {
    try {
      if (writer != null) {
        writer.close();
      } else {
        if (stream != null) {
          stream.close();
        }
      }
    } catch (IOException e) {
      log.info("finishResponse", e);
    }
  }

  public void flushBuffer() throws IOException {
    stream.flush();
  }

  public ServletOutputStream getOutputStream() throws IOException {
    if (writer != null) {
      throw new IllegalStateException("getWriter() has already been called!");
    }

    if (stream == null) {
      stream = createOutputStream();
    }

    return (stream);
  }

  public PrintWriter getWriter() throws IOException {
    // If access denied, don't create new stream or write because
    // it causes the web.xml's 403 page to not render
    if (this.error == HttpServletResponse.SC_FORBIDDEN) {
      return super.getWriter();
    }

    if (writer != null) {
      return (writer);
    }

    if (stream != null) {
      throw new IllegalStateException("getOutputStream() has already been called!");
    }

    stream = createOutputStream();
    writer = new PrintWriter(new OutputStreamWriter(stream, origResponse.getCharacterEncoding()));

    return (writer);
  }

  public void setContentLength(int length) {
    //no action here
  }

  /**
   * @see javax.servlet.http.HttpServletResponse#sendError(int,
   *      java.lang.String)
   */
  public void sendError(int err, String message) throws IOException {
    super.sendError(err, message);
    this.error = err;

    if (log.isDebugEnabled()) {
      log.debug("sending error: " + err + " [" + message + "]");
    }
  }
}

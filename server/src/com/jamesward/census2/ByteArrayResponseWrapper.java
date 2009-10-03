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

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

public class ByteArrayResponseWrapper extends HttpServletResponseWrapper { 
private ByteArrayServletOutputStream basos;
private PrintWriter pw;
private String contentType;

public ByteArrayResponseWrapper(HttpServletResponse response) {
super(response);
basos = new ByteArrayServletOutputStream();
pw = new PrintWriter(basos);
}

    public void setContentType(String contentType) {
        this.contentType = contentType;
        super.setContentType(contentType);
    }
    
    public String getContentType() {
        return contentType;
    }

    public String getOutputAsString() {
        return basos.toString();
    }

    public byte[] getOutputAsByteArray() {
        return basos.getByteArrayOutputStream().toByteArray();
    }

    public void flushBuffer() throws IOException {
        pw.flush();
    }

    public int getBufferSize() {
        return basos.getSize();
    }

    public ServletOutputStream getOutputStream() {
        return basos;
    }

    public PrintWriter getWriter() throws IOException {
        return pw;
    }

    public void reset() {
        resetBuffer();
        super.reset();
    }

    public void resetBuffer() {
        basos.reset();
    }

    public void setBufferSize(int size) throws IllegalStateException {
        try {
            basos.setSize(size);
        } catch (IOException ioe) {
            throw new IllegalStateException(ioe.getMessage());
        }
    }

}

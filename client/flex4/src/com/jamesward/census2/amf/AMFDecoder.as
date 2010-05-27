////////////////////////////////////////////////////////////////////////////////
//
//  ADOBE SYSTEMS INCORPORATED
//  Copyright 2008 Adobe Systems Incorporated
//  All Rights Reserved.
//
//  NOTICE: Adobe permits you to use, modify, and distribute this file
//  in accordance with the terms of the license agreement accompanying it.
//
////////////////////////////////////////////////////////////////////////////////
/*
Written by Pete Farland (pfarland@adobe.com)
*/
package com.jamesward.census2.amf
{
  import flash.net.ObjectEncoding;
  import flash.utils.ByteArray;
  
  public class AMFDecoder
  {
    private static const AVMPLUS_TYPE_MARKER:uint = 17;

    public static function decodeResponse(bytes:ByteArray):AMFPacket
    {
        // AMF Version
        var version:int = bytes.readUnsignedShort();

        // The response must always start in AMF0
        bytes.objectEncoding = ObjectEncoding.AMF0;
        var response:AMFPacket = new AMFPacket(); 

        var remainingBytes:ByteArray;

        // Headers
        var headerCount:uint = bytes.readUnsignedShort();
        for (var h:uint = 0; h < headerCount; h++)
        {
            var headerName:String = bytes.readUTF();
            var mustUnderstand:Boolean = bytes.readBoolean();
            bytes.readInt(); // Consume header length...

            // Handle AVM+ type marker
            if (version == ObjectEncoding.AMF3)
            {
                var typeMarker:int = bytes.readByte(); 
                if (typeMarker == AVMPLUS_TYPE_MARKER)
                    bytes.objectEncoding = ObjectEncoding.AMF3;
                else
                    bytes.position = bytes.position - 1;
            }

            var headerValue:* = bytes.readObject();

            // Read off the remaining bytes to account for the reset of 
            // the by-reference index on each header value
            remainingBytes = new ByteArray();
            remainingBytes.objectEncoding = bytes.objectEncoding;
            bytes.readBytes(remainingBytes, 0, bytes.length - bytes.position);
            bytes = remainingBytes;
            remainingBytes = null;

            var header:AMFHeader = new AMFHeader(headerName, mustUnderstand, headerValue);
            response.headers.push(header);

            // Reset to AMF0 for next header
            bytes.objectEncoding = ObjectEncoding.AMF0;
        }

        // Message Bodies
        var messageCount:uint = bytes.readUnsignedShort();
        for (var m:uint = 0; m < messageCount; m++)
        {
            var targetURI:String = bytes.readUTF();
            var responseURI:String = bytes.readUTF();
            bytes.readInt(); // Consume message body length...

            // Handle AVM+ type marker
            if (version == ObjectEncoding.AMF3)
            {
                if (bytes.readByte() == AVMPLUS_TYPE_MARKER)
                    bytes.objectEncoding = ObjectEncoding.AMF3;
                else
                    bytes.position = bytes.position - 1;
            }

            var messageBody:* = bytes.readObject();

            // Read off the remaining bytes to account for the reset of 
            // the by-reference index on each message body
            remainingBytes = new ByteArray();
            remainingBytes.objectEncoding = bytes.objectEncoding;
            bytes.readBytes(remainingBytes, 0, bytes.length - bytes.position);
            bytes = remainingBytes;
            remainingBytes = null;

            var message:AMFMessage = new AMFMessage(targetURI, responseURI, messageBody);
            response.messages.push(message);

            // Reset to AMF0 for next message
            bytes.objectEncoding = ObjectEncoding.AMF0;
        }

        return response;
    }

  }
}
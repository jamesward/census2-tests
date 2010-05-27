package com.jamesward.census2.amf
{
  public class AMFMessage
  {
    public function AMFMessage(targetURI:String=null, responseURI:String=null, body:*=undefined)
    {
        this.targetURI = targetURI;
        this.responseURI = responseURI;
        this.body = body;
    }

    public var targetURI:String;
    public var responseURI:String;
    public var body:*;
  }
}
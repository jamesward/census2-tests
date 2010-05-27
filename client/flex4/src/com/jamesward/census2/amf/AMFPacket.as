package com.jamesward.census2.amf
{
  public class AMFPacket
  {
    public function AMFPacket(version:uint=0)
    {
        this.version = version;
    }

    public var version:uint;
    public var headers:Array = []; // Array of AMFHeader
    public var messages:Array = []; // Array of AMFMessage
  }
}
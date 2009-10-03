package com.jamesward.census2.amf
{
  public class AMFHeader
  {
    public function AMFHeader(name:String, mustUnderstand:Boolean=false, data:*=undefined)
    {
        this.name = name;
        this.mustUnderstand = mustUnderstand;
        this.data = data;
    }

    public var name:String;
    public var mustUnderstand:Boolean;
    public var data:*;
  }
}
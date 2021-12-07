package com.skipservices.utils;




public class EncryptData {

	public String doEncrypt(String key,String value)
	{
		String encodedstring=base64Encode(xorOperation(key.getBytes(), value.getBytes()));
		return encodedstring;
	}
	public String doDecrypt(String key,String value)
	{
		return new String(new String(xorOperation(key.getBytes(),base64Decode(value))));
	}
	public byte[] xorOperation(byte[] key,byte[] value)
	{
		byte[] output=new byte[value.length];
		for(int i=0;i<value.length;i++)
		{
			output[i]=(byte)(value[i]^key[i%key.length]);
		}
		return output;
	}
	public String base64Encode(byte[] data)
	{
		String encodedString=Base64.encodeBytes(data,Base64.ENCODE);
		return encodedString;
	}
	public byte[] base64Decode(String encodedstring)
	{
		byte[] decodedbytes=Base64.decode(encodedstring);
		return decodedbytes;
	}
}

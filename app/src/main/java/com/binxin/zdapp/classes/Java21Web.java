package com.binxin.zdapp.classes;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.ksoap2.SoapEnvelope;

public class Java21Web
{
	private final String WDSL = "http://vbea.wicp.net/Java21web.asmx?WSDL";
	private final String NAMESPASE = "http://vbes.org/";
	private final String CreateKey = "CreateKey";
	private final String GetKey = "GetKey";
	private final String GetKeyInfo = "GetKeyInfo";
	private final String RegistKey = "RegistKey";
	private int Timeout = 5000;
	
	public Java21Web(){}
	public Java21Web(int timeout)
	{
		Timeout = timeout;
	}
	
	public Java21Web(int timeout, String name, String url)
	{
		Timeout = timeout;
	}
	
	public boolean CreateKey(String key, String version, String max, String mark, String password) throws Exception
	{
		SoapObject request = new SoapObject(NAMESPASE, CreateKey);
		request.addProperty("key", key);
		request.addProperty("version", version);
		request.addProperty("max", max);
		request.addProperty("mark", mark);
		request.addProperty("password", password);
		SoapObject response = getResponse(request, NAMESPASE+CreateKey);
		return response.getPropertyAsString(0).equals("True");
	}
	
	public String GetKey() throws Exception
	{
		SoapObject request = new SoapObject(NAMESPASE, GetKey);
		SoapObject response = getResponse(request, NAMESPASE+GetKey);
		return response.getPropertyAsString(0);
	}
	
	public String GetKeyInfo(String key) throws Exception
	{
		SoapObject request = new SoapObject(NAMESPASE, GetKeyInfo);
		request.addProperty("key", key);
		SoapObject response = getResponse(request, NAMESPASE+GetKeyInfo);
		return response.getPropertyAsString(0);
	}
	
	public boolean RegistKey(String key) throws Exception
	{
		SoapObject request = new SoapObject(NAMESPASE, RegistKey);
		request.addProperty("key", key);
		SoapObject response = getResponse(request, NAMESPASE+RegistKey);
		return response.getPropertyAsString(0).equals("True");
	}
	
	public SoapObject getResponse(SoapObject request, String soapAction) throws Exception
	{
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
		envelope.bodyOut = request;
		envelope.dotNet = true;
		HttpTransportSE httptse = new HttpTransportSE(WDSL, Timeout);
		httptse.call(soapAction, envelope);
		return (SoapObject) envelope.bodyIn;
	}
}

package lrf.docx;

import java.util.Enumeration;
import java.util.Hashtable;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SHStyles extends DefaultHandler {
	boolean allow=false;
	String currentStyleName=null;
	String defaultStyleName=null;
	String justify=null;
	int fontSize=0;
	Hashtable<String,String> estilos=new Hashtable<String, String>();
	@Override
	public void startElement(String uri, String localName, String name, Attributes at) 
	throws SAXException {
		super.startElement(uri, localName, name, at);
		if(localName.equals("style")){
			String type=at.getValue("w:type");
			if(type!=null && type.equals("paragraph")){
				allow=true;
				currentStyleName=at.getValue("w:styleId");
				String dv=at.getValue(uri, "default");
				if(dv!=null && dv.equals("1")){
					defaultStyleName=currentStyleName;
				}
			}
		}else if(allow){
			if(localName.equals("jc")){
				justify=at.getValue("w:val");
			}else if(localName.equals("sz")){
				fontSize=Integer.parseInt(at.getValue("w:val"));
			}
		}
	}
	@Override
	public void endElement(String uri, String localName, String name)
			throws SAXException {
		// TODO Auto-generated method stub
		super.endElement(uri, localName, name);
		if(localName.equals("style") & currentStyleName!=null){
			allow=false;
			estilos.put(
					currentStyleName,
					 (justify==null?"":" text-align:"+justify+";")
					+(fontSize==0?  "":" font-size:"+(fontSize)+";")
					);
			justify=null;
			fontSize=0;
			currentStyleName=null;
		}
	}

	public String getCSS(){
		if(estilos.size()==0)
			return null;
		String ret="";
		for(Enumeration<String> sne=estilos.keys();sne.hasMoreElements();){
			String sname=sne.nextElement();
			ret+="."+sname+" {"+estilos.get(sname)+"}\n";
		}
		return ret;
	}
	
	public String getDefaultStyleName(){
		return defaultStyleName;
	}
}

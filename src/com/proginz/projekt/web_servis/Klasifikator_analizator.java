package com.proginz.projekt.web_servis;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

public class Klasifikator_analizator {
	
	Web_sucelje sucelje;
	
	public void postaviSucelje(String URL) throws MalformedURLException {
		URL url = new URL(URL);  
		QName qname = new QName("http://web_servis.projekt.proginz.com/", "Implementacija_suceljaService");  
		Service service = Service.create(url, qname);  
		sucelje = service.getPort(Web_sucelje.class);
	}
	
	public Klasifikator_analizator(String put, String URL) throws IOException {
		
		postaviSucelje(URL);

		String podaci = procitajCSV(put);
		sucelje.uveziPodatkeString(podaci);
	}
	
	public Klasifikator_analizator(int brojTesta, String URL) throws IOException {
		postaviSucelje(URL);
		sucelje.uveziPodatkeInt(brojTesta);
	}
	
	static String procitajCSV(String put) throws IOException {
		byte[] znakovi = Files.readAllBytes(Paths.get(put));
		return new String(znakovi, StandardCharsets.UTF_8);
	}
	
	public void obaviSve(String klasifikator) throws Exception {
		sucelje.podijeliPaEvaluiraj();
		sucelje.postaviKlasifikator(klasifikator);
	}

	/*public void vratiTocke()  {
		String y = sucelje.vratiProsjecniGM();
		String x = sucelje.vratiPragove();
		x = x.substring(1, (x.length()-1));
		y = y.substring(1, (y.length()-1));
		//ovo treba splitati po zarezu pa pretvoriti u niz tocaka ili u onaj format s # i &, ovisi sto bude zgodnije
		System.out.println(x);
		System.out.println(y); 
	}*/
	
	public ArrayList<Tocka> vratiTocke()  {
		ArrayList<Tocka> arr = new ArrayList<Tocka>();
		String y = sucelje.vratiProsjecniGM();
		String x = sucelje.vratiPragove();
		x = x.substring(1, (x.length()-1));
		y = y.substring(1, (y.length()-1));
		String[] xparts = x.split(", ");
		String[] yparts = y.split(", ");
		for (int i = 0; i < xparts.length; i++) {
			//System.out.println(Integer.parseInt(xparts[i]));
			//System.out.println(Double.parseDouble(yparts[i]));
			arr.add(new Tocka(Double.parseDouble(xparts[i]), Double.parseDouble(yparts[i]) ) );
		}
			
		//System.out.println(arr.toString());
		return arr; 
	}
}

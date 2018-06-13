package com.proginz.projekt.web_servis;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoints;

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
		sucelje.podijeliPaEvaluiraj(klasifikator);
	}
	
	public ArrayList<Tocka> vratiTocke()  {
		ArrayList<Tocka> arr = new ArrayList<Tocka>();
		String y = sucelje.vratiProsjecniGM();
		String x = sucelje.vratiPostotakManjinskeKlase();
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
	
	public double[] vratiRegresiju() {
		ArrayList<Tocka> arr = vratiTocke();
		final WeightedObservedPoints obs = new WeightedObservedPoints();
		for (int i=0; i<arr.size(); i++) {
			obs.add(arr.get(i).X(), arr.get(i).Y());
		}
	    final PolynomialCurveFitter fitter = PolynomialCurveFitter.create(6);
	    final double[] coeff = fitter.fit(obs.toList());
		return coeff;
	}
	
	private double[] deriviraj(double[] arr) {
		double[] derivacija = new double [(arr.length - 1)];
		for (int i = 1; i < arr.length; i++) {
			derivacija[i - 1] = arr[i];
		}
		return derivacija;	
	}
	
	double uvrsti(double[] coef, double x) {
		double pow = 1, sol = 0;
		for (int i = 0; i < coef.length; i++) {
			sol += coef[i] * pow;
			pow *= x;
		}
		return sol;
	}
	
	double calcArrowPratt(double[] deriv1, double[] deriv2, double SNP) {
		return (-1) * uvrsti(deriv2, SNP) / uvrsti(deriv1, SNP);
	}
	
	public double granicnaRazinaNeujednacenosti(double[] arr) {
		double[] deriv1 = deriviraj(arr);
		double[] deriv2 = deriviraj(deriv1);
		double maxA = 0;
		double bestSNP = 0;
		String x = sucelje.vratiPostotakManjinskeKlase();
		x = x.substring(1, (x.length()-1));
		String[] xparts = x.split(", ");
		for (int i = 0; i < xparts.length; i++) {
			double SNP = Double.parseDouble(xparts[i]);
			if (calcArrowPratt(deriv1, deriv2, SNP) > maxA) {
				maxA = calcArrowPratt(deriv1, deriv2, SNP);
				bestSNP = SNP;
			}
		}
		return bestSNP;
	}
	
	public Tocka vratiTockuGranicneRazineNeujednacenosti() {
		double[] coef = vratiRegresiju();
		double x = granicnaRazinaNeujednacenosti(coef);
		double y = uvrsti(coef, x);
		return new Tocka(x, y);
	}
}

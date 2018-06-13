package com.proginz.projekt.web_servis;

import java.io.IOException;

import javax.jws.WebMethod;  
import javax.jws.WebService;  
import javax.jws.soap.SOAPBinding;  
import javax.jws.soap.SOAPBinding.Style;  

@WebService  
@SOAPBinding(style = Style.RPC)  

public interface Web_sucelje {
	@WebMethod void uveziPodatkeString(String podaci) throws NumberFormatException, IOException;
	@WebMethod void uveziPodatkeInt(int brojTesta) throws NumberFormatException, IOException;
	@WebMethod void podijeliPaEvaluiraj(String imeModela) throws Exception;

	@WebMethod String vratiProsjecniGM();
	@WebMethod String vratiPostotakManjinskeKlase();
}

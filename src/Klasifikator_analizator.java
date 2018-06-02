import java.util.ArrayList;

public class Klasifikator_analizator {
	
	private ArrayList<Tocka> tocke;
	private Tocka kriticnaTocka;
	
	public Klasifikator_analizator() {
		tocke = new ArrayList<Tocka>();
	}
	
	public void obaviSve(String klasifikator) {
		
		tocke.clear();
		
		String dummyTocke = "1,2 3,4 5,6 7,8 9,10 11,12 13,14 15,16 17,18 19,20";
		String dummyKriticnaTocka = "3,4";
		
		String[] razdvojeneTocke = dummyTocke.split(" ");
		String[] pomocnaTocka;
		for (int i = 0; i < razdvojeneTocke.length; i++) {
			pomocnaTocka = razdvojeneTocke[i].split(",");
			tocke.add(new Tocka(Float.parseFloat(pomocnaTocka[0]), Float.parseFloat(pomocnaTocka[1])));
		}
		
		pomocnaTocka = dummyKriticnaTocka.split(",");
		kriticnaTocka = new Tocka(Float.parseFloat(pomocnaTocka[0]), Float.parseFloat(pomocnaTocka[1]));
	}
	
	public Tocka vratiKriticnuTocku() {
		return kriticnaTocka;
	}
	
	public ArrayList<Tocka> vratiTocke() {
		return tocke;
	}

}

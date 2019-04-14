package it.polito.tdp.meteo;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import it.polito.tdp.meteo.bean.SimpleCity;
import it.polito.tdp.meteo.db.MeteoDAO;

public class Model {

	private final static int COST = 100;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;

	private MeteoDAO dao;
	
	public Model() {

		dao = new MeteoDAO();
		
	}

	public String getUmiditaMedia(int mese) {

        Map<String, Double> medieUmidita = dao.getAvgRilevamentiMese(mese);
		
      String risultato= String.format("Nel mese %02d le seguenti città presentano in media tale umidità :\n", mese);
        
        for(Entry<String, Double> loc : medieUmidita.entrySet()) {
        	risultato+=loc.getKey()+":   "+loc.getValue()+"\n"; 
        }
        
		return risultato;
	}

	public String trovaSequenza(int mese) {

		return "TODO!";
	}

	private Double punteggioSoluzione(List<SimpleCity> soluzioneCandidata) {

		double score = 0.0;
		return score;
	}

	private boolean controllaParziale(List<SimpleCity> parziale) {

		return true;
	}

}

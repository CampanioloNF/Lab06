package it.polito.tdp.meteo;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import it.polito.tdp.meteo.bean.Citta;
import it.polito.tdp.meteo.bean.SimpleCity;
import it.polito.tdp.meteo.db.MeteoDAO;

public class Model {

	
	private final static int COST = 100;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;

	private MeteoDAO dao;
	private List<Citta> listaCitta;
	//private List<Rilevamento> listaRilevamenti;
	/*
	 * Preferisco evitare di memorizzare tutti i rilevamenti
	 * e cheidere al DAO esclusivamente quelli necessari
	 */
	
	// Questi dati servono alla gestione della ricorsione
	private Double score_best;
	private List <SimpleCity> best = new LinkedList<SimpleCity>() ;
	
	public Model() {
 
		dao = new MeteoDAO();
		this.listaCitta = dao.getCitta();
		//this.listaRilevamenti = dao.getAllRilevamenti();
	}
	
	
	/**
	 * Questo metodo lavora attraverso una classe DAO.
	 * Riceve come parametro il mese e restituisce la l'umidità media che, in tale mese, 
	 * era presente nelle città
	 * @param mese
	 * @return una stringa contenente l'umidità media presente nelle città presenti nel DB
	 */
	public String getUmiditaMedia(int mese) {

    //Chiede al DAO la media di umidita presente in ogni citta e le memorizza in una mappa
		Map<String, Double> medieUmidita = dao.getAvgRilevamentiMese(mese);
		
      String risultato= "";
        
        for(Entry<String, Double> loc : medieUmidita.entrySet()) {
        	risultato+=loc.getKey()+":   "+loc.getValue()+"\n"; 
        }
        
		return risultato;
	}
	
	/*
	 * 
	 * In alternativa si possono scindere le ricerche delle possibili combinazioni e delle migliori combinazioni
	 * 
	 * In questo caso:
	 * 
	 * -Creo una soluzione
	 * -Vedo se è accettabile
	 * -Valuto se è migliore delle precedenti
	 * 
	 * La classe SimpleCity semplicemente contiene il nome della citta ed il relativo costo 
	 *  (a partire dal Rilevamento effettuato nella Citta con il nome della citta)
	 * 
	 */

	public String trovaSequenza(int mese) {

		score_best = 0.0;
		
		
		//Prendo i rilevamenti che mi interessano
		for(Citta citta : this.listaCitta) 
			citta.setRilevamenti(dao.getRilevamentiLocalitaMese15Giorni(mese, citta.getNome()));
			
		List <SimpleCity> parziale = new ArrayList<SimpleCity>();
		
		cerca(parziale, 0, mese);
		
		String ris_best = String.format(" \n(con un costo di %2.2f)\n", score_best);
		
		for(int i = 0; i<best.size();i++) 
			ris_best+=String.format(" - Giorno %d :   %s  (%d)\n", (i+1), best.get(i).toString(), best.get(i).getCosto());
		
		return ris_best;
	}
	
	
	/*
	 * Questo metodo è stato implementato senza il concetto di data, 
	 * partendo dal presupposto che tutti i primi 15 giorni del mese siano presenti sul DB
	 * La gestione delle dati mediante le opportune classi verrà effettuata nei successivi laboratori, dove opportuno
	 */

	
	private void cerca(List<SimpleCity> parziale, int L, int mese) {
		
		
		//Casi terminali
		
		//termino quando raggiungo i 15 giorni 
		if(L==NUMERO_GIORNI_TOTALI) {
		   
		     //dunque controllo i vincoli della soluzione parziale 
			if(this.controllaParziale(parziale)) {
			
				//ne calcolo i punteggi e in caso modifico il best
				
			    if(score_best==0.0) {
		        
		        	score_best=this.punteggioSoluzione(parziale);
					this.best=new ArrayList<SimpleCity>(parziale);
					//System.out.println(parziale);	
		        }
			
		        else if(this.punteggioSoluzione(parziale)<score_best) {
					score_best=this.punteggioSoluzione(parziale);
					this.best=new ArrayList<SimpleCity>(parziale);
					//System.out.println(parziale);	
				}
			
			}
					
			return;
			
	
			
		}
		
		//Caso generale
		

		//Considero tutte le citta
		for(int i=0; i<this.listaCitta.size();i++) {
			

			/*
			 * Pongo i vincoli nella condizione terminale, dal momento che
			 * alcuni di essi possono essere valuati solo quando si ha una soluzione completa
			 * 
			 * Pongo solo dei vincoli solo sulle citta visitate più di 6 volte
			 */
			
			if(this.controlloGiorniMax(parziale, L)) {
			
			Citta c = this.listaCitta.get(i);

			//Costruisco un oggetto SimpleCty
			
			SimpleCity sc;
			
			//Se sono all'inizio o non cambio citta non spedo i soldi del trasporto
			 if(parziale.isEmpty() || parziale.get(L-1).getNome().equals(c.getNome())) 
				sc = new SimpleCity(c.getNome(),c.getRilevamenti().get(L).getUmidita());
			else
				sc = new SimpleCity(c.getNome(), (COST+c.getRilevamenti().get(L).getUmidita()));
		    
			
			  //aggiungo il SimpleCity al parziale
			  parziale.add(sc);
			
			  //ricorsione
			  cerca(parziale, L+1, mese);
			
			  //si torna indietro (o invece di L parziale.size-1)
		      parziale.remove(L);
			
			}
		    
		  }	
			
		}
		
	
	
	//questi metodi sono da usare all'interno del trova sequenza
	//Uno serve a verificare i vincoli ed un secondo a calcolarne il punteggio
	
	private Double punteggioSoluzione(List<SimpleCity> soluzioneCandidata) {

		double score = 0.0;
		
		for(SimpleCity cs : soluzioneCandidata) {
			score+=cs.getCosto();		}
		
		return score;
	}

	private boolean controllaParziale(List<SimpleCity> parziale) {

		//per ogni citta
	  for(Citta c : this.listaCitta) {
		int cont= 0;
		int contM=0;
		   //conto quante ce ne sono nel parziale
		  for(SimpleCity sc: parziale) {
			  if(sc.getNome().equals(c.getNome())) {
				  cont++;
				  contM++;
			  }
			  //se sono meno di tre di fila esco dal ciclo e aspetto di ritrovare la stessa citta
			  else if(cont<NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN) {
				  cont=0;    //ricomincia a contare
				  continue; //mi mantiene nel ciclo
			  }
		  }
		  
		  if(cont<NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN || contM>NUMERO_GIORNI_CITTA_MAX) {
			  return false;
		  }
		  
	  }
	  
	  
		return true;
	}

	/*
	 * Si potrebbero dividire in due i controlli o meglio fare un ulteriore controllo in fase ricorsiva 
	 * per ottimizzare la ricerca, abbandonando dei rami ricorsivi dopo almeno 6 passaggi
	 */

	private boolean controlloGiorniMax(List<SimpleCity> parziale, int L) {
		
	if(L>=NUMERO_GIORNI_CITTA_MAX) {	
		
		for(Citta c : this.listaCitta) {
			int cont = 0;
			for(SimpleCity sc : parziale) {
				if(sc.getNome().equals(c.getNome())) 
					  cont++;
				
			}
			
			 if(cont>NUMERO_GIORNI_CITTA_MAX) 
				  return false;
					
		}
		
	}
		
		return true;
	}
}

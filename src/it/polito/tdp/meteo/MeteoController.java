package it.polito.tdp.meteo;

import java.net.URL;
import java.time.Month;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

/*
 * Modifica del progetto in base alla soluzione proposta in Aula
 */

public class MeteoController {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML     
	private ChoiceBox<Month> boxMese;

	@FXML
	private Button btnCalcola;

	@FXML
	private Button btnUmidita;

	@FXML
	private TextArea txtResult;

	private Model model;
	
	private ObservableList<Month> comboItems;
	
	
	
	public void caricaBox() {
		
		List<Month> mesi = new LinkedList <Month> ();
		
		for(int i=1; i<13; i++) {
			mesi.add(Month.of(i));
		}
	
		comboItems = FXCollections.observableList(mesi);
		this.boxMese.setItems(comboItems);
		
			}

	@FXML
	void doCalcolaSequenza(ActionEvent event) {

		int mese = this.boxMese.getValue().getValue();
		
		try {
		String  sequenza = this.model.trovaSequenza(mese);
		sequenza =  String.format("Il percorso da seguire, nel mese %s,  prevede le seguenti tappe: ",this.boxMese.getValue())+sequenza;
		this.txtResult.setText(sequenza);
		}catch(NumberFormatException nfe) {
			this.txtResult.setText("ERRORE! Si prega di selezionare un mese..");
		}
	}

	@FXML
	void doCalcolaUmidita(ActionEvent event) {


		int mese = this.boxMese.getValue().getValue();
		
		try {
		String umiditaAVG = this.model.getUmiditaMedia(mese);
		umiditaAVG = String.format("Nel mese %s le seguenti città presentano in media tale umidità :\n",  this.boxMese.getValue()) + umiditaAVG;
	    this.txtResult.setText(umiditaAVG);
		}catch(NumberFormatException nfe) {
			this.txtResult.setText("ERRORE! Si prega di selezionare un mese..");
		}
	}

	@FXML
	void initialize() {
		assert boxMese != null : "fx:id=\"boxMese\" was not injected: check your FXML file 'Meteo.fxml'.";
		assert btnCalcola != null : "fx:id=\"btnCalcola\" was not injected: check your FXML file 'Meteo.fxml'.";
		assert btnUmidita != null : "fx:id=\"btnUmidita\" was not injected: check your FXML file 'Meteo.fxml'.";
		assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Meteo.fxml'.";
		
		this.caricaBox();
		
	}

	public void setModel(Model model) {
		// TODO Auto-generated method stub
		this.model=model;
	}

}

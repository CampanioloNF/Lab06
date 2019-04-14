package it.polito.tdp.meteo;

import java.net.URL;
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

public class MeteoController {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private ChoiceBox<String> boxMese;

	@FXML
	private Button btnCalcola;

	@FXML
	private Button btnUmidita;

	@FXML
	private TextArea txtResult;

	private Model model;
	
	private ObservableList<String> comboItems;
	
	
	
	public void caricaBox() {
		
		List<String> mesi = new LinkedList <String> ();
		
		for(int i=1; i<13; i++) {
			mesi.add(""+i);
		}
	
		comboItems = FXCollections.observableList(mesi);
		this.boxMese.setItems(comboItems);
		
			}

	@FXML
	void doCalcolaSequenza(ActionEvent event) {

	}

	@FXML
	void doCalcolaUmidita(ActionEvent event) {

		//String mese = String.format("%02d", Integer.parseInt(this.boxMese.getValue()));
		//System.out.println(mese);
	    
		String umiditaAVG = this.model.getUmiditaMedia(Integer.parseInt(this.boxMese.getValue()));
	
	    this.txtResult.setText(umiditaAVG);
		
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

package it.polito.tdp.meteo.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.tdp.meteo.bean.Rilevamento;

public class MeteoDAO {

	public List<Rilevamento> getAllRilevamenti() {

		final String sql = "SELECT Localita, Data, Umidita FROM situazione ORDER BY data ASC";

		List<Rilevamento> rilevamenti = new ArrayList<Rilevamento>();

		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Rilevamento r = new Rilevamento(rs.getString("Localita"), rs.getDate("Data"), rs.getInt("Umidita"));
				rilevamenti.add(r);
			}

			conn.close();
			return rilevamenti;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public List<Rilevamento> getAllRilevamentiLocalitaMese(int mese, String localita) {

		return null;
	}

	public Map <String, Double> getAvgRilevamentiMese(int mese) {


		final String sql = "SELECT Localita, AVG(umidita) AS MediaUmidita FROM situazione WHERE YEAR(DATA) = 2013 AND MONTH(DATA) = ? GROUP BY localita";

		Map <String, Double> mediaPerLocalita = new HashMap <String, Double> ();
		

		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			
			st.setInt(1, mese);
			

			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				mediaPerLocalita.put(rs.getString("Localita"), rs.getDouble("MediaUmidita"));
				
			}

			conn.close();
			return mediaPerLocalita;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}

		
		
	}
	
	

}

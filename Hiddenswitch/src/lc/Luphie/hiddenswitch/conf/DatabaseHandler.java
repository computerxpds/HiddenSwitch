package lc.Luphie.hiddenswitch.conf;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import lc.Luphie.hiddenswitch.HiddenSwitch;

public class DatabaseHandler {
	/** Preloaded key blocks */
	public HashMap<String, KeyBlock> keyblocks = new HashMap<String, KeyBlock>();
	private Connection connection;
	private Statement statement;
	private HiddenSwitch me;

	public DatabaseHandler(HiddenSwitch instance) throws Exception {
		me = instance;
		
		Class.forName("org.sqlite.JDBC");
		connection = DriverManager.getConnection("jbdc:sqlite:data.db");
		statement = connection.createStatement();
		statement.executeUpdate("CREATE TABLE IF NOT EXISTS blocks (idstring text, world text, x integer, y integer, z integer, user text, key text");
	}
	public boolean load(){
		try {
			ResultSet result = statement.executeQuery("SELECT * FROM blocks");
			while(result.next()) {
				KeyBlock kb = new KeyBlock(
						result.getString("idstring"),
						result.getString("world"),
						result.getInt("x"),
						result.getInt("y"),
						result.getInt("z"),
						result.getString("user"),
						result.getString("key")
					);
				me.confV.keyblocks.put(kb.id,kb);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	public boolean port(){
		portMLtoSL();
		// or
		portSLtoML();
		return false;
	}
	private boolean portMLtoSL(){return false;}
	private boolean portSLtoML(){return false;}
	public boolean save() {return false;}

}

/* *

 * HiddenSwitch - Hidden switches and buttons for Bukkit 
 * Copyright (C) 2011-2012  Luphie (devLuphie) luphie@lumpcraft.com

 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.

 * */package lc.Luphie.hiddenswitch.conf;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import lc.Luphie.hiddenswitch.HiddenSwitch;
import lc.Luphie.hiddenswitch.utilities.KeyBlock;

/**
 * @author Luphie
 *
 */
public class DatabaseHandler {

	private Connection connection;
	private Statement statement;
	private HiddenSwitch me;
	private PreparedStatement prepIns;
	private PreparedStatement prepDel;
	private int updatesI = 0;

	public DatabaseHandler() {

		me = HiddenSwitch.instance;
		
		try {

			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:"+me.getDataFolder().getPath()+"\\data.db");
			statement = connection.createStatement();
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS blocks (idstring TEXT, world TEXT, x INTEGER, y INTEGER, z INTEGER, user TEXT, key TEXT, owner TEXT)");

			connection.setAutoCommit(false);
			
			prepIns = connection.prepareStatement("INSERT INTO blocks VALUES (?,?,?,?,?,?,?,?);");
			prepDel = connection.prepareStatement("DELETE FROM blocks WHERE idstring=?;");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		load();
		
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
						result.getString("key"),
						result.getString("owner"),
						true
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
	public void dropRecord(String stringid) {

		try {
		
			statement.executeUpdate("DELETE FROM blocks WHERE idstring='"+stringid+"'");
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Insert the values from an instance of KeyBlock into a new row in the
	 * database.
	 * 
	 * @param KeyBlock
	 *            The KeyBlock instance to pull the data from
	 */
	public void newRecord(KeyBlock key) {

		newRecord(
				key.id,
				key.world,
				key.x,
				key.y,
				key.z,
				key.users,
				key.key,
				key.owner);

	}
	
	/**
	 * Insert a new record into the database with the provided info.
	 * 
	 * @param String
	 *            idstring - The IDSTRING for the KeyBlock
	 * @param String
	 *            world - The world the KeyBlock resides in
	 * @param String
	 *            x - The x coordinate for the KeyBlock
	 * @param String
	 *            y - The y coordinate for the KeyBlock
	 * @param String
	 *            z - The z coordinate for the KeyBlock
	 * @param String
	 *            user - The user(s) that the KeyBlock is bound to.
	 * @param String
	 *            key - The item required to use the KeyBlock
	 * @param String
	 *            owner - The player who created the KeyBlock
	 */
	public void newRecord(String idstring, String world, int x, int y, int z,
			String user, String key, String owner) {

			try {
				
				prepIns.setString(1, idstring);
				prepIns.setString(2, world);
				prepIns.setInt(3, x);
				prepIns.setInt(4, y);
				prepIns.setInt(5, z);
				prepIns.setString(6, user);
				prepIns.setString(7, key);
				prepIns.setString(8, owner);
				
				prepIns.executeUpdate();

				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/*statement.executeUpdate("INSERT INTO blocks VALUES ('" + idstring
					+ "','" + world + "','" + Integer.toString(x) + "','"
					+ Integer.toString(y) + "','" + Integer.toString(z) + "','"
					+ user + "','" + key + "',NULL)");*/

			updatesI++;
			updates();
	}
	
	private void updates() {

		if(this.updatesI >= 5) {
			
			try {
				connection.commit();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

	}

}

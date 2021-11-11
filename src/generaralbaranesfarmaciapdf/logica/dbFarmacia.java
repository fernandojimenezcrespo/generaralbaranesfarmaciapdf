/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package generaralbaranesfarmaciapdf.logica;

/**
 *
 * @author fernando
 */
 

import generaralbaranesfarmaciapdf.utils.Desencriptar;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import generaralbaranesfarmaciapdf.LeerXML;

public class dbFarmacia {

	LeerXML leerxml= new LeerXML();
        Desencriptar desencripta=new Desencriptar();
	private  String CPDSuser=desencripta.descifraClave( leerxml.devuelveValor("CDPSUSER"));
	private  String CPDSpass =desencripta.descifraClave( leerxml.devuelveValor("CPDSPASS"));
	private String url = leerxml.devuelveValor("URL");        
        
 
	

	private static Connection con = null;

	private static dbFarmacia instancia = null;

	public dbFarmacia() {
		con = null;
		if (con == null) {
			try {
				Class.forName("oracle.jdbc.driver.OracleDriver");
                                
				// System.out.println(url);
				// System.out.println(CPDSuser);
				// System.out.println(CPDSpass);
				con = DriverManager.getConnection(url, CPDSuser, CPDSpass);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();
		if (con != null)
			con.close();
	}

	public static dbFarmacia getInstance() {
		instancia=null;
		if (instancia == null) {
			instancia = new dbFarmacia();
		}
		return instancia;
	}

	/**
	 * Prepara una orden SQL
	 * 
	 * @param conn
	 *            conexion utilizada
	 * @param SQL
	 *            orden a preparar
	 * @return el statement preparado o null si error
	 */
	public PreparedStatement prepareSQL(String SQL) {

		if (con == null)
			return null;
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement(SQL);
                        
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return stmt;
	}

	/**
	 * Crea un resultset para un SELECT preparado
	 * 
	 * @param stmt
	 *            statement preparado a utilizar
	 * @return el statement preparado o null si error
	 */
	public ResultSet executeSELECT(PreparedStatement stmt) {
		ResultSet rs = null;
		try {
			rs = stmt.executeQuery();
			
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return rs;
	}

	/**
	 * Ejecuta una orden INSERT, UPDATE, o DELETE preparada
	 * 
	 * @param stmt
	 *            statement preparado a utilizar
	 * @return el numero de filas afectadas, o -1 si error
	 */
	public int executeORDER(PreparedStatement stmt) {
		int n = -1;
		try {
			n = stmt.executeUpdate();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return n;
	}

	/**
	 * Cierra un ResultSet, inhibe excepciones
	 * 
	 * @param rs
	 *            ResultSet a cerrar
	 */
	public void close(ResultSet rs) {
		if (rs == null)
			return;
		try {
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Cierra un Statement, inhibe excepciones
	 * 
	 * @param stmt
	 *            Statement a cerrar
	 */
	public void close(Statement stmt) {
		if (stmt == null)
			return;
		try {
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Cierra un Statement, inhibe excepciones
	 * 
	 * @param stmt
	 *            Statement a cerrar
	 */
	public void close() {
		if (con == null)
			return;
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}

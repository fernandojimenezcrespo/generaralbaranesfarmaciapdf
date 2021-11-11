
/**
 *
 * @author fernando
 */
 
 
package generaralbaranesfarmaciapdf.dao;


 

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.informix.asf.Connection;
import com.itextpdf.text.List;

import  generaralbaranesfarmaciapdf.beans.proveedoresBE;
import generaralbaranesfarmaciapdf.logica.dbFarmacia;

 

public class ProveedoresDAO {
//	SincronizacionJimenaLogica sincronizaJimena = new SincronizacionJimenaLogica();
	dbFarmacia logicaFarmacia = new dbFarmacia();
	

	//static Includes include=new Includes();
	
	public ArrayList<proveedoresBE> getProveedor(String codigo){
		ArrayList<proveedoresBE> lista= new ArrayList<proveedoresBE>();
		dbFarmacia dbfarmacia=new dbFarmacia();
		
		PreparedStatement stmSelectProveedor = null;
		ResultSet rsSelectProveedor=null;
		proveedoresBE beanProveedor=new proveedoresBE();
		String sql="select codigo, nombre,direccion,telefono,fax,e_mail,cod_postal from proveedor where codigo=?";
		dbFarmacia conn1= dbfarmacia.getInstance();
		try {stmSelectProveedor = conn1.prepareSQL(sql);
			stmSelectProveedor.setString(1, codigo);
			rsSelectProveedor=dbfarmacia.executeSELECT(stmSelectProveedor);
			 
			 
			 while (rsSelectProveedor.next()) {
				 // System.out.println(rsSelectProveedor.getString("codigo"));
				 // System.out.println(rsSelectProveedor.getString("nombre"));
			 beanProveedor.setCodigo(rsSelectProveedor.getString("codigo"));
			 beanProveedor.setNombre(rsSelectProveedor.getString("nombre"));
			 beanProveedor.setDireccion(rsSelectProveedor.getString("direccion"));
			 beanProveedor.setTelefono(rsSelectProveedor.getString("telefono"));
			 beanProveedor.setFax(rsSelectProveedor.getString("fax"));
			 beanProveedor.setE_mail(rsSelectProveedor.getString("e_mail"));
			 beanProveedor.setCod_postal(rsSelectProveedor.getString("cod_postal"));
			 
			 lista.add(beanProveedor);
			 }
			 //dbfarmacia.close(stmSelectProveedor);
			 //dbfarmacia.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lista;
	}
}

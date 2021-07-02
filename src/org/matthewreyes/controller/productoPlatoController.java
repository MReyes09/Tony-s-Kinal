
package org.matthewreyes.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.matthewreyes.beam.Plato;
import org.matthewreyes.beam.Producto;
import org.matthewreyes.beam.productoHasPlato;
import org.matthewreyes.db.Conexion;
import org.matthewreyes.system.Principal;

public class productoPlatoController implements Initializable {
	 private Principal escenarioPrincipal;
	 private ObservableList<productoHasPlato> listaProductoPlato;
	 private ObservableList<Plato> listaPlato;
	 private ObservableList<Producto> listaProducto;
	 
	 @FXML private TextField txtCodigo;
	 @FXML private ComboBox cmbCodigoProductos;
	 @FXML private ComboBox cmbCodigoPlatos;
	 @FXML private TableView tblProductoPlatos;
	 @FXML private TableColumn colCodigo;
	 @FXML private TableColumn colCodigoProductos;
	 @FXML private TableColumn colCodPlatos;
	 
	 @Override
	 public void initialize(URL location, ResourceBundle resources) {
		  cargarDatos();
		  cmbCodigoProductos.setItems(getCodigoProducto());
		  cmbCodigoPlatos.setItems(getPlatos());
	 }
	 
	 public void cargarDatos(){
		  tblProductoPlatos.setItems(getProductoPlatos());
		  colCodigo.setCellValueFactory(new PropertyValueFactory<productoHasPlato, Integer>("codigoProductosHasPlatos"));
		  colCodigoProductos.setCellValueFactory(new PropertyValueFactory<productoHasPlato, Integer>("codigoProducto"));
		  colCodPlatos.setCellValueFactory(new PropertyValueFactory<productoHasPlato, Integer>("codigoPlato"));
	 }
	 
	 public void seleccionarElemento(){
		  txtCodigo.setText(String.valueOf(((productoHasPlato)tblProductoPlatos.getSelectionModel().getSelectedItem()).getCodigoProductosHasPlatos()));
		  cmbCodigoProductos.getSelectionModel().select(buscarProducto(((productoHasPlato)tblProductoPlatos.getSelectionModel().getSelectedItem()).getCodigoProducto()));
		  cmbCodigoPlatos.getSelectionModel().select(buscarServicioPlato(((productoHasPlato)tblProductoPlatos.getSelectionModel().getSelectedItem()).getCodigoPlato()));
	 }	 
	 
	 public Producto buscarProducto(int codigoProducto){
		  Producto resultado = null;
		  try{
			   PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarProdcutos(?)}");
			   procedimiento.setInt(1, codigoProducto);
			   ResultSet registro = procedimiento.executeQuery();
			   while(registro.next()){
					resultado = new Producto(registro.getInt("codigoProducto"),
											 registro.getString("nombreProducto"),
											 registro.getInt("cantidad"));
			   }
		  }catch(Exception e){
			   e.printStackTrace();
		  }
		  return resultado;
	 }
	 
	 public Plato buscarServicioPlato(int codigoPlato){
		  Plato resultado = null;
		  try{
			   PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarPlatos(?)}");
			   procedimiento.setInt(1, codigoPlato);
			   ResultSet registro = procedimiento.executeQuery();
			   while(registro.next()){
					resultado = new Plato(registro.getInt("codigoPlato"),
											 registro.getInt("cantidad"),
											 registro.getString("nombrePlato"),
											 registro.getString("descripcionPlato"),
											 registro.getDouble("precioPlato"),
											 registro.getInt("codigoTipoPlato")); 					
			   }
		  }catch(Exception e){
			   e.printStackTrace();
		  }
		  return resultado;
	 }

	 public ObservableList<Plato> getPlatos(){
		  ArrayList<Plato> lista = new ArrayList<Plato>();
		  try{
			   PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarPlatos}");
			   ResultSet resultado = procedimiento.executeQuery();
			   while(resultado.next()){
					lista.add(new Plato(resultado.getInt("codigoPlato"),
						 resultado.getInt("cantidad"),
						 resultado.getString("nombrePlato"),
						 resultado.getString("descripcionPlato"),
						 resultado.getDouble("precioPlato"),
						 resultado.getInt("codigoTipoPlato")));
			   }
		  }catch(Exception e){
			   e.printStackTrace();
		  }
		  return listaPlato = FXCollections.observableArrayList(lista);
	 }
	 
	 public ObservableList<Producto> getCodigoProducto(){
		  ArrayList<Producto> lista = new ArrayList<Producto>(); 
		  try{
			   PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarProductos}");
			   ResultSet resultado = procedimiento.executeQuery();
			   while(resultado.next()){
					lista.add(new Producto(resultado.getInt("codigoProducto"),
											 resultado.getString("nombreProducto"),
											 resultado.getInt("cantidad")));			   }
		  }catch(Exception e){
			   e.printStackTrace();
		  }		  
		  return listaProducto = FXCollections.observableArrayList(lista);
	 }

	 public ObservableList<productoHasPlato> getProductoPlatos(){
		  ArrayList<productoHasPlato> lista = new ArrayList<productoHasPlato>();
		  try{
			   PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarProductosHasPlatos}");
			   ResultSet resultado = procedimiento.executeQuery();
			   while(resultado.next()){
					lista.add(new productoHasPlato(resultado.getInt("codigoProductosHasPlatos"),
													   resultado.getInt("codigoProducto"),
													   resultado.getInt("codigoPlato")));
			   }
		  }catch(Exception e){
			   e.printStackTrace();
		  }
		  return listaProductoPlato = FXCollections.observableArrayList(lista);
	 }
	 
	 public Principal getEscenarioPrincipal() {
		  return escenarioPrincipal;
	 }

	 public void setEscenarioPrincipal(Principal escenarioPrincipal) {
		  this.escenarioPrincipal = escenarioPrincipal;
	 }
	 
	 public void MenuPrincipal(){
		  escenarioPrincipal.MenuPrincipal();
	 }
}

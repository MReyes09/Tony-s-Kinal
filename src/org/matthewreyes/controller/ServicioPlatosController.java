
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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.matthewreyes.beam.Plato;
import org.matthewreyes.beam.Servicio;
import org.matthewreyes.beam.ServicioPlato;
import org.matthewreyes.db.Conexion;
import org.matthewreyes.system.Principal;

public class ServicioPlatosController implements Initializable{
	 private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
	 private operaciones tipoDeOperacion = operaciones.NINGUNO;
	 private Principal escenarioPrincipal;
	 private ObservableList<ServicioPlato> listaServicioPlato;
	 private ObservableList<Servicio> listaServicio;
	 private ObservableList<Plato> listaPlatos;
	 
	 @FXML private TextField txtCodigo;
	 @FXML private ComboBox cmbCodigoServicio;
	 @FXML private ComboBox cmbCodigoPlato;
	 @FXML private TableView tblServiciosPlatos;
	 @FXML private TableColumn colCodigo;
	 @FXML private TableColumn colCodigoServicio;
	 @FXML private TableColumn colCodigoPlato;

	 @Override
	 public void initialize(URL location, ResourceBundle resources) {
		  cargarDatos();
		  cmbCodigoServicio.setItems(getServicios());
		  cmbCodigoPlato.setItems(getPlatos());
	 }
	 
	 public void cargarDatos(){
		  tblServiciosPlatos.setItems(getServicioPlato());
		  colCodigo.setCellValueFactory(new PropertyValueFactory<ServicioPlato, Integer>("codigoServiciosHasPlatos"));
		  colCodigoServicio.setCellValueFactory(new PropertyValueFactory<ServicioPlato, Integer>("codigoServicio"));
		  colCodigoPlato.setCellValueFactory(new PropertyValueFactory<ServicioPlato, Integer>("codigoPlato"));
	 }
	 
	 public void seleccionarElemento(){
		  txtCodigo.setText(String.valueOf(((ServicioPlato)tblServiciosPlatos.getSelectionModel().getSelectedItem()).getCodigoServiciosHasPlatos()));
		  cmbCodigoServicio.getSelectionModel().select(buscarServicio(((ServicioPlato)tblServiciosPlatos.getSelectionModel().getSelectedItem()).getCodigoServicio()));
		  cmbCodigoPlato.getSelectionModel().select(buscarServicioPlato(((ServicioPlato)tblServiciosPlatos.getSelectionModel().getSelectedItem()).getCodigoPlato()));
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
	 
	 public Servicio buscarServicio(int codigoServicio){
		  Servicio resultado = null;
		  try{
			   PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarServicios(?)}");
			   procedimiento.setInt(1, codigoServicio);
			   ResultSet registro = procedimiento.executeQuery();
			   while(registro.next()){
					resultado = new Servicio(registro.getInt("codigoServicio"),
										registro.getDate("fechaServicio"),
										registro.getString("tipoServicio"),
										registro.getString("horaServicio"),
										registro.getString("lugarServicio"),
										registro.getString("telefonoContacto"),
										registro.getInt("codigoEmpresa"));												  
			   }
		  }catch(Exception e){
			   e.printStackTrace();
		  }
		  return resultado;
	 }
	 
	 public ObservableList<ServicioPlato> getServicioPlato(){
		  ArrayList<ServicioPlato> lista = new ArrayList<ServicioPlato>();
		  try{
			   PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarServiciosHasPlatos}");
			   ResultSet resultado = procedimiento.executeQuery();
			   while(resultado.next()){
					lista.add(new ServicioPlato(resultado.getInt("codigoServiciosHasPlatos"),
												resultado.getInt("codigoServicio"),
												  resultado.getInt("codigoPlato")));
			   }
		  }catch(Exception e){
			   e.printStackTrace();
		  }
		  return listaServicioPlato = FXCollections.observableArrayList(lista);
	 }
	 
	 public ObservableList<Servicio> getServicios(){
		  ArrayList<Servicio> lista = new ArrayList<Servicio>();
		  try{
			   PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarServicios}");
			   ResultSet resultado = procedimiento.executeQuery();
			   while(resultado.next()){
					lista.add(new Servicio(resultado.getInt("codigoServicio"),
					resultado.getDate("fechaServicio"),
					resultado.getString("tipoServicio"),
					resultado.getString("horaServicio"),
					resultado.getString("lugarServicio"),
					resultado.getString("telefonoContacto"),
					resultado.getInt("codigoEmpresa")));
			   }
		  }catch(Exception e){
			   e.printStackTrace();
		  }
		  return listaServicio = FXCollections.observableArrayList(lista);
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
		  return listaPlatos = FXCollections.observableArrayList(lista);
	 }	 
	 
	 public void MenuPrincipal(){
		  escenarioPrincipal.MenuPrincipal();		  
	 }

	 public Principal getEscenarioPrincipal() {
		  return escenarioPrincipal;
	 }

	 public void setEscenarioPrincipal(Principal escenarioPrincipal) {
		  this.escenarioPrincipal = escenarioPrincipal;
	 } 
}

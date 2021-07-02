
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.matthewreyes.beam.Empresa;
import org.matthewreyes.beam.TipoPlato;
import org.matthewreyes.db.Conexion;
import org.matthewreyes.system.Principal;

public class TipoPlatoController implements Initializable {
	 private enum operaciones{NINGUNO,ACTUALIZAR, ELIMINAR, EDITAR, CANCELAR, GUARDAR, NUEVO};
	 private operaciones tipoDeOperacion = operaciones.NINGUNO;
	 private ObservableList<TipoPlato> listaTipoPlato;
	 	 
	 @FXML private TextField txtcodigoTipoPlato;
	 @FXML private TextField txtDescripcion;
	 @FXML private TableView tblTipoPlatos;
	 @FXML private TableColumn colCodigo;
	 @FXML private TableColumn colDescripcion;
	 @FXML private Button btnNuevo;
	 @FXML private Button btnEliminar;
	 @FXML private Button btnEditar;
	 @FXML private Button btnReporte;
	 
	 
	 private Principal escenarioPrincipal;
	 
	 @Override
	 public void initialize(URL location, ResourceBundle resources) {
		  cargarDatos();
	 }

	 public void cargarDatos(){
		  tblTipoPlatos.setItems(getTipoPlatos());
		  colCodigo.setCellValueFactory(new PropertyValueFactory<TipoPlato, Integer>("codigoTipoPlato"));
		  colDescripcion.setCellValueFactory(new PropertyValueFactory<TipoPlato, String>("descripcionTipo"));
	 }
	 
	 public ObservableList<TipoPlato> getTipoPlatos(){
		  ArrayList<TipoPlato> lista = new ArrayList<TipoPlato>();
		  try{
			   PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarTipoPlato}");
			   ResultSet resultado = procedimiento.executeQuery();
			   while(resultado.next()){
					lista.add(new TipoPlato(resultado.getInt("codigoTipoPlato"),
								   resultado.getString("descripcionTipo")));
			   }
		  }catch(Exception e){
			   e.printStackTrace();
		  }
		  return listaTipoPlato = FXCollections.observableArrayList(lista);
	 }	  
	 
	 public void seleccionarElemento(){
		  txtcodigoTipoPlato.setText(String.valueOf(((TipoPlato)tblTipoPlatos.getSelectionModel().getSelectedItem()).getCodigoTipoPlato()));
		  txtDescripcion.setText(((TipoPlato)tblTipoPlatos.getSelectionModel().getSelectedItem()).getDescripcionTipo());
	 }
	 
	 public void limpiarControles(){
		  txtcodigoTipoPlato.setText("");
		  txtDescripcion.setText("");
	 }
	 
	 public void bloquearControles(){
		  txtcodigoTipoPlato.setEditable(false);
		  txtDescripcion.setEditable(false);
	 }
	 
	 public void activarControles(){
		  txtcodigoTipoPlato.setEditable(false);
		  txtDescripcion.setEditable(true);
	 }
	 
	 public void nuevo(){
		  switch(tipoDeOperacion){
			   case NINGUNO:
					cargarDatos();
					activarControles();
					limpiarControles();
					btnNuevo.setText("Agregar");
					btnEliminar.setText("Cancelar");
					btnEditar.setDisable(true);
					btnReporte.setDisable(true);
					tipoDeOperacion = operaciones.GUARDAR;
			   break;
			   case GUARDAR:
					guardar();
					bloquearControles();
					limpiarControles();
					cargarDatos();
					btnNuevo.setText("Nuevo");
					btnEliminar.setText("Eliminar");
					btnEditar.setDisable(false);
					btnReporte.setDisable(false);
					tipoDeOperacion = operaciones.NINGUNO;
			   break;			   
		  }
	 }
	 
	 public void guardar(){
		  TipoPlato registro = new TipoPlato();
		  registro.setDescripcionTipo(txtDescripcion.getText());
		  try{
			   PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarTipoPlato(?)}");
			   procedimiento.setString(1, registro.getDescripcionTipo());
			   procedimiento.executeQuery();
			   listaTipoPlato.add(registro);
		  }catch(Exception e){
			   e.printStackTrace();
		  }
	 }
	 
	 public void eliminar(){
		  switch(tipoDeOperacion){
			   case GUARDAR:
					bloquearControles();
					limpiarControles();
					cargarDatos();
					btnNuevo.setText("Nuevo");
					btnEliminar.setText("Eliminar");
					btnEditar.setDisable(false);
					btnReporte.setDisable(false);
					tipoDeOperacion = operaciones.NINGUNO;
			   break;
			   default:
					if(tblTipoPlatos.getSelectionModel().getSelectedItem() != null){
						 int respuesta = JOptionPane.showConfirmDialog(null,"¿Estas seguro de eliminar el registro?","Eliminar Tipo De Plato",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
						 if (respuesta == JOptionPane.YES_OPTION)
							  try{
								   PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarTipoPlato(?)}");
								   procedimiento.setInt(1, ((TipoPlato)tblTipoPlatos.getSelectionModel().getSelectedItem()).getCodigoTipoPlato());
								   procedimiento.executeQuery();
								   listaTipoPlato.remove(tblTipoPlatos.getSelectionModel().getSelectedIndex());
								   cargarDatos();
								   limpiarControles();
								   bloquearControles();
							  }catch(Exception e){
								   e.printStackTrace();
							  }
						 }else 
							  JOptionPane.showMessageDialog(null,"Debe seleccionar un elemento antes de eliminar");
			   break;
		  }
	 }
	 
	 public void editar(){
		  switch(tipoDeOperacion){
			   case NINGUNO:
					if (tblTipoPlatos.getSelectionModel().getSelectedItem() != null){
						 btnEditar.setText("Actualizar");
						 btnReporte.setText("Cancelar");
						 btnNuevo.setDisable(true);
						 btnEliminar.setDisable(true);
						 activarControles();
						 tipoDeOperacion = operaciones.ACTUALIZAR;						 
					}else{
						 JOptionPane.showMessageDialog(null, "Debe Seleccionar un elemento antes de actualizar");
					}
			   break;
			   case ACTUALIZAR:
					actualizarDatos();
					limpiarControles();
					bloquearControles();
					cargarDatos();
					btnEditar.setText("Editar");
					btnReporte.setText("Cancelar");
					btnNuevo.setDisable(false);
					btnEliminar.setDisable(false);
					tipoDeOperacion = operaciones.NINGUNO;
			   break;
		  }
	 } 
	 
	 public void actualizarDatos(){
		  try{
			   TipoPlato registro = (TipoPlato)tblTipoPlatos.getSelectionModel().getSelectedItem();
			   registro.setCodigoTipoPlato(Integer.valueOf(txtcodigoTipoPlato.getText()));
			   registro.setDescripcionTipo(txtDescripcion.getText());
			   PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarTipoPlato(?,?)}");
			   procedimiento.setInt(1, registro.getCodigoTipoPlato());
			   procedimiento.setString(2, registro.getDescripcionTipo());
			   procedimiento.executeQuery();
		  }catch(Exception e){
			   e.printStackTrace();			   
		  }
	 }
	 
	 public void reporte(){
		  limpiarControles();
		  bloquearControles();
		  cargarDatos();
		  btnEditar.setText("Editar");
		  btnReporte.setText("Cancelar");
		  btnNuevo.setDisable(false);
		  btnEliminar.setDisable(false);	
		  tipoDeOperacion = operaciones.NINGUNO;
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
	 
	 public void VentanaPlatos(){
		  escenarioPrincipal.VentanaPlatos();
	 }	 
}

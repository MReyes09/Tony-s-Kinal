
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
import javax.swing.JOptionPane;
import org.matthewreyes.beam.Plato;
import org.matthewreyes.beam.TipoPlato;
import org.matthewreyes.db.Conexion;
import org.matthewreyes.system.Principal;

public class PlatosController implements Initializable{
	 
	 private Principal escenarioPrincipal;
	 private enum operaciones{NINGUNO,NUEVO,GUARDAR,EDITAR,ELIMINAR,CANCELAR,ACTUALIZAR};
	 private operaciones tipoDeOperacion = operaciones.NINGUNO;
	 private ObservableList<Plato> listaPlatos;
	 private ObservableList<TipoPlato> listaTipoPlatos;
	 
	 @FXML private TextField txtCodigo;
	 @FXML private TextField txtCantidad;
	 @FXML private TextField txtNombrePlato;
	 @FXML private TextField txtDescripcion;
	 @FXML private TextField txtPrecioPlato;
	 @FXML private ComboBox cmbCodigoTipoPlato;
	 @FXML private TableView tblPlatos; 
	 @FXML private TableColumn colCodigo;
	 @FXML private TableColumn colCantidad;
	 @FXML private TableColumn colNombre;
	 @FXML private TableColumn colDescripcion;
	 @FXML private TableColumn colPrecioPlato;
	 @FXML private TableColumn colCodigoTipoPlato;
	 @FXML private Button btnNuevo;
	 @FXML private Button btnEliminar;
	 @FXML private Button btnEditar;
	 @FXML private Button btnReporte;
	 
	 
	 @Override
	 public void initialize(URL location, ResourceBundle resources) {
		  cargarDatos();
		  cmbCodigoTipoPlato.setItems(getTipoPlatos());
	 }
	 
	 public void Nuevo(){
		  switch(tipoDeOperacion){
			   case NINGUNO:
					cargarDatos();
					limpiarControles();
					activarControles();
					btnNuevo.setText("Agregar");
					btnEliminar.setText("Cancelar");
					btnEditar.setDisable(true);
					btnReporte.setDisable(true);
					tipoDeOperacion = operaciones.GUARDAR;
			   break;
			   case GUARDAR:
					guardar();
					desactivarControles();
					limpiarControles();
					btnNuevo.setText("Nuevo");
					btnEliminar.setText("Eliminar");
					btnEditar.setDisable(false);
					btnReporte.setDisable(false);
					tipoDeOperacion = operaciones.NINGUNO;
			   break;
		  }
	 }
	 
	 public void Eliminar(){
		  switch(tipoDeOperacion){
			   case GUARDAR:
					desactivarControles();
					limpiarControles();
					btnNuevo.setText("Nuevo");
					btnEliminar.setText("Eliminar");
					btnEditar.setDisable(false);
					btnReporte.setDisable(false);
					tipoDeOperacion = operaciones.NINGUNO;
			   break;
			   default:
					if(tblPlatos.getSelectionModel().getSelectedItem() != null){
						 int respuesta = JOptionPane.showConfirmDialog(null,"¿Estas seguro de eliminar el registro?","Eliminar Platos",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
						 if(respuesta == JOptionPane.YES_OPTION)
							  try{
								   PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarPlatos(?)}");
								   procedimiento.setInt(1, Integer.valueOf(txtCodigo.getText()));
								   procedimiento.executeQuery();
								   listaPlatos.remove(tblPlatos.getSelectionModel().getSelectedIndex());
							  }catch(Exception e){
								   e.printStackTrace();
							  }
					}else
						 JOptionPane.showMessageDialog(null,"Debe seleccionar un elemento antes de eliminar");
			   break;
		  }
	 }
	 
	 public void Editar(){
		  switch(tipoDeOperacion){
			   case NINGUNO:
					if(tblPlatos.getSelectionModel().getSelectedItem() != null){
						 activarControles();
						 btnNuevo.setDisable(true);
						 btnEliminar.setDisable(true);
						 btnEditar.setText("Actualizar");
						 btnReporte.setText("Cancelar");
						 tipoDeOperacion = operaciones.ACTUALIZAR;
					}else
						 JOptionPane.showMessageDialog(null, "Debe Seleccionar un elemento antes de actualizar");
			   break;
			   case ACTUALIZAR:
					actualizar();
					desactivarControles();
					limpiarControles();
					cargarDatos();
					btnNuevo.setDisable(false);
					btnEliminar.setDisable(false);
					btnEditar.setText("Editar");
					btnReporte.setText("Reporte");
					tipoDeOperacion = operaciones.NINGUNO;
			   break;
		  }
	 }
	 
	 public void actualizar(){
		  try{
			   PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarPlatos(?,?,?,?,?,?)}");
			   procedimiento.setInt(1, Integer.parseInt(txtCodigo.getText()));
			   procedimiento.setInt(2, Integer.parseInt(txtCantidad.getText()));
			   procedimiento.setString(3, txtNombrePlato.getText());
			   procedimiento.setString(4, txtDescripcion.getText());
			   procedimiento.setDouble(5, Double.parseDouble(txtPrecioPlato.getText()));
			   procedimiento.setInt(6, ((TipoPlato)cmbCodigoTipoPlato.getSelectionModel().getSelectedItem()).getCodigoTipoPlato());
			   procedimiento.executeQuery();
		  }catch(Exception e){
			   e.printStackTrace();
		  }
	 }
	 
	 public void Reporte(){
		  switch(tipoDeOperacion){
			   case ACTUALIZAR:
					desactivarControles();
					limpiarControles();
					btnNuevo.setDisable(false);
					btnEliminar.setDisable(false);
					btnEditar.setText("Editar");
					btnReporte.setText("Reporte");
					tipoDeOperacion = operaciones.NINGUNO;
			   break;
		  }
	 }
	 
	 
	 public void limpiarControles(){
		  txtCodigo.setText("");
		  txtCantidad.setText("");
		  txtNombrePlato.setText("");
		  txtDescripcion.setText("");
		  txtPrecioPlato.setText("");
		  cmbCodigoTipoPlato.getSelectionModel().clearSelection();
	 }
	 
	 public void desactivarControles(){
		  txtCodigo.setEditable(false);
		  txtCantidad.setEditable(false);
		  txtNombrePlato.setEditable(false);
		  txtDescripcion.setEditable(false);
		  txtPrecioPlato.setEditable(false);
		  cmbCodigoTipoPlato.setDisable(true);
	 }
	 
	 public void activarControles(){
		  txtCodigo.setEditable(false);
		  txtCantidad.setEditable(true);
		  txtNombrePlato.setEditable(true);
		  txtDescripcion.setEditable(true);
		  txtPrecioPlato.setEditable(true);
		  cmbCodigoTipoPlato.setDisable(false);
	 }
	 
	 public void guardar(){
		  Plato registro = new Plato();
		  registro.setCantidad(Integer.parseInt(txtCantidad.getText()));
		  registro.setNombrePlato(txtNombrePlato.getText());
		  registro.setDescripcionPlato(txtDescripcion.getText());
		  registro.setPrecioPlato(Double.valueOf(txtPrecioPlato.getText()));
		  registro.setCodigoTipoPlato(((TipoPlato)cmbCodigoTipoPlato.getSelectionModel().getSelectedItem()).getCodigoTipoPlato());
		  try{
			   PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarPlatos(?,?,?,?,?)}");
			   procedimiento.setInt(1, registro.getCantidad());
			   procedimiento.setString(2, registro.getNombrePlato());
			   procedimiento.setString(3, registro.getDescripcionPlato());
			   procedimiento.setDouble(4, registro.getPrecioPlato());
			   procedimiento.setInt(5, registro.getCodigoTipoPlato());
			   procedimiento.executeQuery();
			   listaPlatos.add(registro);
		  }catch(Exception e){
			   e.printStackTrace();
		  }
	 }
	 
	 public void seleccionarElemento(){
		  txtCodigo.setText(String.valueOf(((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getCodigoPlato()));
		  txtCantidad.setText(String.valueOf(((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getCantidad()));
		  txtNombrePlato.setText(((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getNombrePlato());
		  txtDescripcion.setText(((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getDescripcionPlato());
		  txtPrecioPlato.setText(String.valueOf(((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getPrecioPlato()));
		  cmbCodigoTipoPlato.getSelectionModel().select(buscarTipoPlato(((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getCodigoTipoPlato()));
	 }
	 
	 public TipoPlato buscarTipoPlato(int codigoTipoPlato){
		  TipoPlato resultado = null;
		  try{
			   PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarTipoPlato(?)}");
			   procedimiento.setInt(1, codigoTipoPlato);
			   ResultSet registro = procedimiento.executeQuery();
			   while(registro.next()){
					resultado = new TipoPlato(registro.getInt("codigoTipoPlato"),
											 registro.getString("descripcionTipo"));
			   }
		  }catch(Exception e){
			   e.printStackTrace();
		  }
		  return resultado;
	 }
	 
	 public void cargarDatos(){
		  tblPlatos.setItems(getPlatos());
		  colCodigo.setCellValueFactory(new PropertyValueFactory<Plato, Integer>("codigoPlato"));
		  colCantidad.setCellValueFactory(new PropertyValueFactory<Plato, Integer>("cantidad"));
		  colNombre.setCellValueFactory(new PropertyValueFactory<Plato, String>("nombrePlato"));
		  colDescripcion.setCellValueFactory(new PropertyValueFactory<Plato, String>("descripcionPlato"));
		  colPrecioPlato.setCellValueFactory(new PropertyValueFactory<Plato, Double>("precioPlato"));
		  colCodigoTipoPlato.setCellValueFactory(new PropertyValueFactory<Plato, Integer>("codigoTipoPlato"));
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
		  return listaTipoPlatos = FXCollections.observableArrayList(lista);
	 }
	 
	 public void VentanaTipoDeplato(){
		  escenarioPrincipal.VentanaTipoPlatos();
	 }

	 public Principal getEscenarioPrincipal() {
		  return escenarioPrincipal;
	 }

	 public void setEscenarioPrincipal(Principal escenarioPrincipal) {
		  this.escenarioPrincipal = escenarioPrincipal;
	 }
	 
	 
	 
}

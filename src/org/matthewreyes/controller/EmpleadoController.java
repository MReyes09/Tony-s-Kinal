
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
import org.matthewreyes.beam.Empleado;
import org.matthewreyes.beam.TipoEmpleado;
import org.matthewreyes.db.Conexion;
import org.matthewreyes.system.Principal;

public class EmpleadoController implements Initializable{
	 
	 private Principal escenarioPrincipal;
	 private enum operaciones{NUEVO, GUARDAR, EDITAR, ELIMINAR, CANCELAR, ACTUALIZAR, NINGUNO};
	 private operaciones tipoDeOperacion = operaciones.NINGUNO;
	 private ObservableList<TipoEmpleado> listaTipoEmpleado;
	 private ObservableList<Empleado> listaEmpleado;
	 @FXML private TextField txtCodigoEmpleado;
	 @FXML private TextField txtNumeroEmpleado;
	 @FXML private TextField txtApellidoEmpleado;
	 @FXML private TextField txtNombreEmpleado;
	 @FXML private TextField txtDireccionEmpleado;
	 @FXML private TextField txtTelefonoContacto;
	 @FXML private TextField txtGradoCocinero;
	 @FXML private ComboBox cmbCodigoTipoEmpleado;
	 @FXML private TableView tblEmpleados;
	 @FXML private TableColumn colCodigoEmpleado;
	 @FXML private TableColumn colNoEmpleado;
	 @FXML private TableColumn colApellidos;
	 @FXML private TableColumn colNombres;
	 @FXML private TableColumn colDireccion;
	 @FXML private TableColumn colTelefono;
	 @FXML private TableColumn colGradoCocinero;
	 @FXML private TableColumn colCodTipoEmpleado;
	 @FXML private Button btnEliminar;
	 @FXML private Button btnEditar;
	 @FXML private Button btnReporte;
	 @FXML private Button btnNuevo;
	 
	 @Override
	 public void initialize(URL location, ResourceBundle resources) {
		  cargarDatos();
		  cmbCodigoTipoEmpleado.setItems(getTipoEmpleado());
	 }
	 
	 public void limpiarControles(){
		  txtCodigoEmpleado.setText("");
		  txtNumeroEmpleado.setText("");
		  txtApellidoEmpleado.setText("");
		  txtNombreEmpleado.setText("");
		  txtTelefonoContacto.setText("");
		  txtDireccionEmpleado.setText("");
		  txtGradoCocinero.setText("");
		  cmbCodigoTipoEmpleado.getSelectionModel().clearSelection();
	 } 
	 
	 public void desactivarControles(){
		  txtCodigoEmpleado.setEditable(false);
		  txtNumeroEmpleado.setEditable(false);
		  txtApellidoEmpleado.setEditable(false);
		  txtNombreEmpleado.setEditable(false);
		  txtTelefonoContacto.setEditable(false);
		  txtDireccionEmpleado.setEditable(false);
		  txtGradoCocinero.setEditable(false);
		  cmbCodigoTipoEmpleado.setDisable(false);
	 }
	 
	 public void activarControles(){
		  txtCodigoEmpleado.setEditable(false);
		  txtNumeroEmpleado.setEditable(true);
		  txtApellidoEmpleado.setEditable(true);
		  txtNombreEmpleado.setEditable(true);
		  txtTelefonoContacto.setEditable(true);
		  txtDireccionEmpleado.setEditable(true);
		  txtGradoCocinero.setEditable(true);
		  cmbCodigoTipoEmpleado.setDisable(false);
	 }
	 
	 public TipoEmpleado buscarTipoEmpleado(int codigoTipoEmpleado){
		  TipoEmpleado resultado = null;
		  try{
			   PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarTipoEmpleado(?)}");
			   procedimiento.setInt(1, codigoTipoEmpleado);
			   ResultSet registro = procedimiento.executeQuery();
			   while(registro.next()){
					resultado = new TipoEmpleado(registro.getInt("codigoTipoEmpleado"),
												  registro.getString("descripcion"));
			   }
		  }catch(Exception e){
			   e.printStackTrace();
		  }
		  return resultado;
	 }
	 
	 public void nuevo(){
		  switch (tipoDeOperacion){
			   case NINGUNO:
					limpiarControles();
					activarControles();
					btnNuevo.setText("Guardar");
					btnEliminar.setText("Cancelar");
					btnEliminar.setDisable(false);
					btnEditar.setDisable(true);
					btnReporte.setDisable(true);
					cargarDatos();
					tipoDeOperacion = operaciones.GUARDAR;
			   break;
			   case GUARDAR:
					guardar();
					desactivarControles();
					limpiarControles();
					btnNuevo.setText("Nuevo");
					btnEliminar.setText("Eliminar");
					btnEliminar.setDisable(false);
					btnEditar.setDisable(false);
					btnReporte.setDisable(false);
					tipoDeOperacion = operaciones.NINGUNO;
					cargarDatos();
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
					btnEliminar.setDisable(false);
					btnEditar.setDisable(false);
					btnReporte.setDisable(false);
					tipoDeOperacion = operaciones.NINGUNO;
					cargarDatos();
			   break;		
			   default:
					if(tblEmpleados.getSelectionModel().getSelectedItem() != null){
						 int respuesta = JOptionPane.showConfirmDialog(null,"¿Estas seguro de eliminar el registro?","Eliminar Empleado",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
						 if(respuesta == JOptionPane.YES_OPTION){
							  try{
								   PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarEmpleados(?)}");
								   procedimiento.setInt(1, (((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpleado()));								   
								   procedimiento.executeQuery();
								   listaEmpleado.remove(tblEmpleados.getSelectionModel().getSelectedIndex());
							  }catch(Exception e){
								   e.printStackTrace();
							  }
						 }
					}else
							  JOptionPane.showMessageDialog(null,"Debe seleccionar un elemento antes de eliminar");	 
			   break;
		  }
	 } 
	 
	 public void Editar(){
		  switch(tipoDeOperacion){
			   case NINGUNO:
					if (tblEmpleados.getSelectionModel().getSelectedItem() != null){
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
						 actualizar();
						 limpiarControles();
						 desactivarControles();
						 btnEditar.setText("Editar");
						 btnReporte.setText("Reporte");
						 btnNuevo.setDisable(false);
						 btnEliminar.setDisable(false);
						 tipoDeOperacion = operaciones.NINGUNO;
						 cargarDatos();						 
					break;
		  }
	 }	 
	 
	 public void reporte(){
		  switch(tipoDeOperacion){
			   case ACTUALIZAR:
					limpiarControles();
					desactivarControles();
					btnEditar.setText("Editar");
					btnReporte.setText("Reporte");
					btnNuevo.setDisable(false);
					btnEliminar.setDisable(false);
					tipoDeOperacion = operaciones.NINGUNO;
					cargarDatos();	
			   break;
		  }
	 }
	 
	 public void actualizar(){
		  try{
			   PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarEmpleados(?,?,?,?,?,?,?,?)}");
			   Empleado registro = (Empleado)tblEmpleados.getSelectionModel().getSelectedItem();
			   registro.setCodigoTipoEmpleado(((TipoEmpleado)cmbCodigoTipoEmpleado.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado());
			   registro.setNumeroEmpleado(Integer.parseInt(txtNumeroEmpleado.getText()));
			   registro.setApellidoEmpleado(txtApellidoEmpleado.getText());
			   registro.setNombreEmpleado(txtNombreEmpleado.getText());
			   registro.setDireccionEmpleado(txtDireccionEmpleado.getText());
			   registro.setTelefonoContacto(txtTelefonoContacto.getText());
			   registro.setGradoCocinero(txtGradoCocinero.getText());			   
			   procedimiento.setInt(1, registro.getCodigoEmpleado());
			   procedimiento.setInt(2, registro.getNumeroEmpleado());
			   procedimiento.setString(3, registro.getApellidoEmpleado());
			   procedimiento.setString(4, registro.getNombreEmpleado());
			   procedimiento.setString(5, registro.getDireccionEmpleado());
			   procedimiento.setString(6, registro.getTelefonoContacto());
			   procedimiento.setString(7, registro.getGradoCocinero());
			   procedimiento.setInt(8, registro.getCodigoTipoEmpleado());
			   procedimiento.executeQuery();
		  }catch(Exception e){
			   e.printStackTrace();
		  } 
	 }
	 
	 public void guardar(){
		  Empleado registro = new Empleado();
		  registro.setNumeroEmpleado(Integer.valueOf(txtNumeroEmpleado.getText()));
		  registro.setApellidoEmpleado(txtApellidoEmpleado.getText());
		  registro.setNombreEmpleado(txtNombreEmpleado.getText());
		  registro.setDireccionEmpleado(txtDireccionEmpleado.getText());
		  registro.setTelefonoContacto(txtTelefonoContacto.getText());
		  registro.setGradoCocinero(txtGradoCocinero.getText());
		  registro.setCodigoTipoEmpleado(((TipoEmpleado)cmbCodigoTipoEmpleado.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado());
		  try{
			   PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarEmpleados(?,?,?,?,?,?,?)}");
			   procedimiento.setInt(1, registro.getNumeroEmpleado());
			   procedimiento.setString(2, registro.getApellidoEmpleado());
			   procedimiento.setString(3, registro.getNombreEmpleado());
			   procedimiento.setString(4, registro.getDireccionEmpleado());
			   procedimiento.setString(5, registro.getTelefonoContacto());
			   procedimiento.setString(6, registro.getGradoCocinero());
			   procedimiento.setInt(7, registro.getCodigoTipoEmpleado());			   
			   procedimiento.executeQuery();
			   listaEmpleado.add(registro);
		  }catch(Exception e){
			   e.printStackTrace();
		  }
	 }
	 
	 public void seleccionarDatos(){
		  txtCodigoEmpleado.setText(String.valueOf(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpleado()));
		  txtNumeroEmpleado.setText(String.valueOf(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getNumeroEmpleado()));
		  txtApellidoEmpleado.setText(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getApellidoEmpleado());
		  txtNombreEmpleado.setText(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getNombreEmpleado());
		  txtDireccionEmpleado.setText(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getDireccionEmpleado());
		  txtTelefonoContacto.setText(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getTelefonoContacto());
		  txtGradoCocinero.setText(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getGradoCocinero());
		  cmbCodigoTipoEmpleado.getSelectionModel().select(buscarTipoEmpleado(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado()));
	 }
	 
	 public void cargarDatos(){
		  tblEmpleados.setItems(getEmpleado());
		  colCodigoEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, Integer>("codigoEmpleado"));
		  colNoEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, Integer>("numeroEmpleado"));
		  colApellidos.setCellValueFactory(new PropertyValueFactory<Empleado, String>("apellidoEmpleado"));
		  colNombres.setCellValueFactory(new PropertyValueFactory<Empleado, String>("nombreEmpleado"));
		  colDireccion.setCellValueFactory(new PropertyValueFactory<Empleado, String>("direccionEmpleado"));
		  colTelefono.setCellValueFactory(new PropertyValueFactory<Empleado, String>("telefonoContacto"));
		  colGradoCocinero.setCellValueFactory(new PropertyValueFactory<Empleado, String>("gradoCocinero"));
		  colCodTipoEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, Integer>("codigoTipoEmpleado"));
	 }
	 
	 public ObservableList<TipoEmpleado> getTipoEmpleado(){
		  ArrayList<TipoEmpleado> lista = new ArrayList<TipoEmpleado>();
		  try{
			   PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarTipoEmpleado}");
			   ResultSet resultado = procedimiento.executeQuery();
			   while(resultado.next()){
					lista.add(new TipoEmpleado(resultado.getInt("codigoTipoEmpleado"),
										resultado.getString("descripcion")
					));
			   } 
		  }catch(Exception e){
			   e.printStackTrace();
		  }
		  return listaTipoEmpleado = FXCollections.observableArrayList(lista);
	 }	 
	 
	 public ObservableList<Empleado>getEmpleado(){
		  ArrayList<Empleado> lista = new ArrayList<Empleado>();
		  try{
			   PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarEmpleados}");
			   ResultSet resultado = procedimiento.executeQuery();
			   while(resultado.next()){
					lista.add(new Empleado(resultado.getInt("codigoEmpleado"),
											 resultado.getInt("numeroEmpleado"),
											 resultado.getString("apellidoEmpleado"),
											 resultado.getString("nombreEmpleado"),
											 resultado.getString("direccionEmpleado"),
											 resultado.getString("telefonoContacto"),
											 resultado.getString("gradoCocinero"),
											 resultado.getInt("codigoTipoEmpleado")
					));
			   }
		  }catch(Exception e){
			   e.printStackTrace();
		  }
		  return listaEmpleado = FXCollections.observableArrayList(lista);
	 }	  
	 
	 public Principal getEscenarioPrincipal() {
		  return escenarioPrincipal;
	 }

	 public void setEscenarioPrincipal(Principal escenarioPrincipal) {
		  this.escenarioPrincipal = escenarioPrincipal;
	 }
	 
	 public void VentanaTipoEmpleados(){
		  escenarioPrincipal.VentanaTipoEmpleados();
	 }
}

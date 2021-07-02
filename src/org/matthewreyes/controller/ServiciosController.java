
package org.matthewreyes.controller;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
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
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.matthewreyes.beam.Empresa;
import org.matthewreyes.beam.Presupuesto;
import org.matthewreyes.beam.Servicio;
import org.matthewreyes.db.Conexion;
import org.matthewreyes.report.GenerarReporte;
import org.matthewreyes.system.Principal;

public class ServiciosController implements Initializable{

	 private Principal escenarioPrincipal;
	 private enum operaciones{NUEVO, ELIMINAR, CANCELAR, GUARDAR, EDITAR, ACTUALIZAR, NINGUNO};
	 private operaciones tipoDeOperaciones = operaciones.NINGUNO;
	 private ObservableList<Servicio> listaServicio;
	 private ObservableList<Empresa> listaEmpresa;
	 private DatePicker fecha;
	 
	 @FXML private TextField txtCodigoServicio;
	 @FXML private GridPane grpFechaServicio;
	 @FXML private TextField txtTipoServicio;
	 @FXML private TextField txtHoraServicio;
	 @FXML private TextField txtLugarEvento;
	 @FXML private TextField txtTelefono;
	 @FXML private ComboBox cmbCodigoEmpresa;
	 @FXML private TableView tblServicios;
	 @FXML private TableColumn colCodigo;
	 @FXML private TableColumn colFechaServicio;
	 @FXML private TableColumn colTipoServicio;
	 @FXML private TableColumn colHoraServicio;
	 @FXML private TableColumn colLugarEvento;
	 @FXML private TableColumn coloTelefono;
	 @FXML private TableColumn colCodigoEmpresa;
	 @FXML private Button btnNuevo;
	 @FXML private Button btnEliminar;
	 @FXML private Button btnEditar;
	 @FXML private Button btnReporte;
	 
	 @Override
	 public void initialize(URL location, ResourceBundle resources) {
		  cargarDatos();
		  fecha = new DatePicker(Locale.ENGLISH);
		  fecha.setDateFormat(new SimpleDateFormat("yyy-MM-dd"));
		  fecha.getCalendarView().todayButtonTextProperty().set("Today");
		  fecha.getCalendarView().setShowWeeks(false);
		  grpFechaServicio.add(fecha, 0, 0);
		  cmbCodigoEmpresa.setItems(getEmpresa());
	 }
	 
	 public void activarControles(){
		  txtCodigoServicio.setEditable(false);
		  txtTipoServicio.setEditable(true);
		  txtHoraServicio.setEditable(true);
		  txtLugarEvento.setEditable(true);
		  txtTelefono.setEditable(true);
		  grpFechaServicio.setDisable(false);
		  cmbCodigoEmpresa.setDisable(false);
	 }
	 
	 public void desactivarControles(){
		  txtCodigoServicio.setEditable(false);
		  txtTipoServicio.setEditable(false);
		  txtHoraServicio.setEditable(false);
		  txtLugarEvento.setEditable(false);
		  txtTelefono.setEditable(false);
		  grpFechaServicio.setDisable(true);
		  cmbCodigoEmpresa.setDisable(true);
	 }
	 
	 public void limpiarControles(){
		  txtCodigoServicio.setText("");
		  txtTipoServicio.setText("");
		  txtHoraServicio.setText("");
		  txtLugarEvento.setText("");
		  txtTelefono.setText("");
		  cmbCodigoEmpresa.getSelectionModel().clearSelection();
	 }
	 
	 public void cargarDatos(){
		  tblServicios.setItems(getServicios());
		  colCodigo.setCellValueFactory(new PropertyValueFactory<Servicio, Integer>("codigoServicio"));
		  colFechaServicio.setCellValueFactory(new PropertyValueFactory<Servicio, Date>("fechaServicio"));
		  colTipoServicio.setCellValueFactory(new PropertyValueFactory<Servicio, String>("tipoServicio"));
		  colHoraServicio.setCellValueFactory(new PropertyValueFactory<Servicio, String>("horaServicio"));
		  colLugarEvento.setCellValueFactory(new PropertyValueFactory<Servicio, String>("lugarServicio"));
		  coloTelefono.setCellValueFactory(new PropertyValueFactory<Servicio, String>("telefonoContacto"));
		  colCodigoEmpresa.setCellValueFactory(new PropertyValueFactory<Servicio, Integer>("codigoEmpresa"));
	 }
	 
	 public void seleccionarElemento(){
		  txtCodigoServicio.setText(String.valueOf(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getCodigoServicio()));
		  txtTipoServicio.setText(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getTipoServicio());
		  txtHoraServicio.setText(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getHoraServicio());
		  txtLugarEvento.setText(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getLugarServicio());
		  txtTelefono.setText(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getTelefonoContacto());
		  fecha.selectedDateProperty().set(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getFechaServicio());
		  cmbCodigoEmpresa.getSelectionModel().select(buscarEmpresa(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getCodigoEmpresa()));
	 }
	 
	 public void nuevo(){
		  switch(tipoDeOperaciones){
			   case NINGUNO:
					limpiarControles();
					activarControles();
					btnNuevo.setText("Agregar");
					btnEliminar.setText("Cancelar");
					btnEditar.setDisable(true);
					btnReporte.setDisable(true);
					cargarDatos();
					tipoDeOperaciones = operaciones.GUARDAR;
			   break;
			   case GUARDAR:
					guardarDatos();
					desactivarControles();
					limpiarControles();
					btnNuevo.setText("Nuevo");
					btnEliminar.setText("Eliminar");
					btnEditar.setDisable(false);
					btnReporte.setDisable(false);
					tipoDeOperaciones = operaciones.NINGUNO;
					cargarDatos();
			   break;
					
		  }
	 }
	 
	 public void eliminar(){
		  switch(tipoDeOperaciones){
			   case GUARDAR:
					desactivarControles();
					limpiarControles();
					btnNuevo.setText("Nuevo");
					btnEliminar.setText("Eliminar");
					btnEditar.setDisable(false);
					btnReporte.setDisable(false);
					tipoDeOperaciones = operaciones.NINGUNO;
					cargarDatos();					
			   break;
			   default:
					if (tblServicios.getSelectionModel().getSelectedItem() != null){
						 int respuesta = JOptionPane.showConfirmDialog(null,"¿Estas seguro de eliminar el registro?","Eliminar servicio",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
						 if (respuesta == JOptionPane.YES_OPTION)
							  try{
								   PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarServicios(?)}");
								   procedimiento.setInt(1, ((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getCodigoServicio());
								   procedimiento.executeQuery();
								   listaServicio.remove(tblServicios.getSelectionModel().getSelectedIndex());
								   desactivarControles();
								   cargarDatos();
								   limpiarControles();
								   tipoDeOperaciones = operaciones.NINGUNO;
							  }catch(Exception e){
								   e.printStackTrace();
							  }
					}else
						 JOptionPane.showMessageDialog(null,"Debe seleccionar un elemento antes de eliminar");
			   break;
		  }
	 }
	 
	 public void editar(){
		  switch(tipoDeOperaciones){
			   case NINGUNO:
					if(tblServicios.getSelectionModel().getSelectedItem() != null){
						 activarControles();
						 btnNuevo.setDisable(true);
						 btnEliminar.setDisable(true);
						 btnReporte.setText("Cancelar");
						 btnEditar.setText("Actualizar");
						 tipoDeOperaciones = operaciones.EDITAR;
					}else 
						 JOptionPane.showMessageDialog(null, "Debe Seleccionar un elemento antes de actualizar"); 					
			   break;
			   case EDITAR:
					actualizar();
					desactivarControles();
					limpiarControles();
					cargarDatos();
					btnNuevo.setDisable(false);
					btnEliminar.setDisable(false);
					btnEditar.setText("Editar");
					btnReporte.setText("Reporte");
					tipoDeOperaciones = operaciones.NINGUNO;  
			   break;
		  }
	 }
	 
	 public void reporte(){
		  switch(tipoDeOperaciones){
			   case EDITAR:
					desactivarControles();
					limpiarControles();
					cargarDatos();
					btnNuevo.setDisable(false);
					btnEliminar.setDisable(false);
					btnEditar.setText("Editar");
					btnReporte.setText("Reporte");
					tipoDeOperaciones = operaciones.NINGUNO; 
			   break;
			   case NINGUNO:
					if(tblServicios.getSelectionModel().getSelectedItem() != null){
						 imprimirReporte();
					} else 
						 JOptionPane.showMessageDialog(null, "Debe seleccionar un dato antes de crear el reporte");
			   break;
		  }
	 }
	 
	 public void imprimirReporte(){
		  Map parametros = new HashMap();
		  int codigoServicio = Integer.valueOf(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getCodigoServicio());
		  parametros.put("codigoServicio", codigoServicio);
		  GenerarReporte.mostrarReporte("ReporteServicios.jasper", "Reporte Servicios", parametros);
	 }
	 
	 public void actualizar(){
		  try{
			   Servicio resultado = new Servicio();
			   resultado.setCodigoServicio(Integer.valueOf(txtCodigoServicio.getText()));
			   resultado.setFechaServicio(fecha.getSelectedDate());
			   resultado.setTipoServicio(txtTipoServicio.getText());
			   resultado.setHoraServicio(txtHoraServicio.getText());
			   resultado.setLugarServicio(txtLugarEvento.getText());
			   resultado.setTelefonoContacto(txtTelefono.getText());
			   resultado.setCodigoEmpresa(((Empresa)cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
			   PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarServicios(?,?,?,?,?,?,?)}");
			   procedimiento.setInt(1, resultado.getCodigoServicio());
			   procedimiento.setDate(2, new java.sql.Date(resultado.getFechaServicio().getTime()));
			   procedimiento.setString(3, resultado.getTipoServicio());
			   procedimiento.setString(4, resultado.getHoraServicio());
			   procedimiento.setString(5, resultado.getLugarServicio());
			   procedimiento.setString(6, resultado.getTelefonoContacto());
			   procedimiento.setInt(7, resultado.getCodigoEmpresa());
			   procedimiento.executeQuery();
		  }catch(Exception e){
			   e.printStackTrace();
		  }
	 }
	 
	 public void guardarDatos(){
		  Servicio registro = new Servicio();
		  registro.setFechaServicio(fecha.getSelectedDate());
		  registro.setTipoServicio(txtTipoServicio.getText());
		  registro.setHoraServicio(txtHoraServicio.getText());
		  registro.setLugarServicio(txtLugarEvento.getText());
		  registro.setTelefonoContacto(txtTelefono.getText());
		  registro.setCodigoEmpresa(((Empresa)cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
		  try{
			   PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarServicios(?,?,?,?,?,?)}");
			   procedimiento.setDate(1, new java.sql.Date(registro.getFechaServicio().getTime()));
			   procedimiento.setString(2, registro.getTipoServicio());
			   procedimiento.setString(3, registro.getHoraServicio());
			   procedimiento.setString(4, registro.getLugarServicio());
			   procedimiento.setString(5, registro.getTelefonoContacto());
			   procedimiento.setInt(6, registro.getCodigoEmpresa());
			   procedimiento.executeQuery();
			   listaServicio.add(registro);
		  }catch(Exception e){
			   e.printStackTrace();
		  }
	 }
	 
	 public Empresa buscarEmpresa(int codigoEmpresa){
		  Empresa resultado = null;
		  try{
			   PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarEmpresas(?)}");
			   procedimiento.setInt(1, codigoEmpresa);
			   ResultSet registro = procedimiento.executeQuery();
			   while (registro.next()){
					resultado = new Empresa( registro.getInt("codigoEmpresa"),
							  registro.getString("nombreEmpresa"),
							  registro.getString("direccion"),
							  registro.getString("telefono")					
					);
			   }
		  }catch(Exception e){
			   e.printStackTrace();
		  }
		  return resultado;
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

	 public ObservableList<Empresa> getEmpresa(){
		  ArrayList<Empresa> lista = new ArrayList<Empresa>();
		  try{
			   PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarEmpresas}");
	 		   ResultSet resultado = procedimiento.executeQuery();
			   while(resultado.next()){
					lista.add(new Empresa(resultado.getInt("codigoEmpresa"),
										  resultado.getString("nombreEmpresa"),
										  resultado.getString("direccion"),
										  resultado.getString("telefono"))
					);
			   }
		  }catch(Exception e){
			   e.printStackTrace();
		  }
		  return listaEmpresa = FXCollections.observableArrayList(lista);
	 }	 
	 
	 public Principal getEscenarioPrincipal() {
		  return escenarioPrincipal;
	 }

	 public void setEscenarioPrincipal(Principal escenarioPrincipal) {
		  this.escenarioPrincipal = escenarioPrincipal;
	 } 
	 
	 public void VentanaEmpresas(){
		  escenarioPrincipal.VentanaEmpresas();
	 }
}

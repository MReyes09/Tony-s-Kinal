
package org.matthewreyes.controller;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
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
import org.matthewreyes.beam.Empleado;
import org.matthewreyes.beam.Servicio;
import org.matthewreyes.beam.ServiciosHasEmpleados;
import org.matthewreyes.db.Conexion;
import org.matthewreyes.system.Principal;

public class ServiciosEmpleadosController implements Initializable{
	 private Principal escenarioPrincipal;
	 private enum operaciones{NINGUNO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, NUEVO, CANCELAR}; 
	 private operaciones tipoDeOperacion = operaciones.NINGUNO;
	 private ObservableList<ServiciosHasEmpleados> listaServiciosEmpleados;
	 private ObservableList<Servicio> listaServicio;
	 private ObservableList<Empleado> listaEmpleado;
	 private DatePicker fecha;
	 private String x;
	 @FXML private TextField txtCodigo;
	 @FXML private TextField txtHoraEvento;
	 @FXML private TextField txtLugarEvento; 
	 @FXML private ComboBox cmbCodigoServicio;
	 @FXML private ComboBox cmbCodigoEmpleado;
	 @FXML private GridPane grpFechaEvento;
	 @FXML private TableView tblServicioEmpleados;
	 @FXML private TableColumn colCodigo;
	 @FXML private TableColumn colCodigoServicio;
	 @FXML private TableColumn colCodigoEmpleado;
	 @FXML private TableColumn colFechaEvento;
	 @FXML private TableColumn colHoraEvento;
	 @FXML private TableColumn colLugarEvento;
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
		  grpFechaEvento.add(fecha, 0, 0);
		  cmbCodigoServicio.setItems(getServicios());
		  cmbCodigoEmpleado.setItems(getEmpleado());
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
	 
	 public void cargarDatos(){
		  tblServicioEmpleados.setItems(getServicioEmpleados());
		  colCodigo.setCellValueFactory(new PropertyValueFactory<ServiciosHasEmpleados, Integer>("codigoServiciosHasEmpleados"));
		  colCodigoServicio.setCellValueFactory(new PropertyValueFactory<ServiciosHasEmpleados, Integer>("codigoServicio"));
		  colCodigoEmpleado.setCellValueFactory(new PropertyValueFactory<ServiciosHasEmpleados, Integer>("codigoEmpleado"));
		  colFechaEvento.setCellValueFactory(new PropertyValueFactory<ServiciosHasEmpleados, Date>("fechaEvento"));
		  colHoraEvento.setCellValueFactory(new PropertyValueFactory<ServiciosHasEmpleados, String>("horaEvento"));
		  colLugarEvento.setCellValueFactory(new PropertyValueFactory<ServiciosHasEmpleados, String>("lugarEvento"));
	 }
	 
	 public ObservableList<ServiciosHasEmpleados> getServicioEmpleados(){
		  ArrayList<ServiciosHasEmpleados> lista = new ArrayList<ServiciosHasEmpleados>();
		  try{
			   PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarServiciosHasEmpleados}");
			   ResultSet resultado = procedimiento.executeQuery();
			   while(resultado.next()){
					lista.add(new ServiciosHasEmpleados(resultado.getInt("codigoServiciosHasEmpleados"),
													   resultado.getInt("codigoServicio"),
													   resultado.getInt("codigoEmpleado"),
													   resultado.getDate("fechaEvento"),
													   resultado.getString("horaEvento"),
													   resultado.getString("lugarEvento")));
			   }
		  }catch(Exception e){
			   e.printStackTrace();
		  }
		  return listaServiciosEmpleados = FXCollections.observableArrayList(lista);
	 }
	 
	 public void seleccionarElemento(){
		  txtCodigo.setText(String.valueOf((((ServiciosHasEmpleados)tblServicioEmpleados.getSelectionModel().getSelectedItem()).getCodigoServiciosHasEmpleados())));
		  txtHoraEvento.setText(((ServiciosHasEmpleados)tblServicioEmpleados.getSelectionModel().getSelectedItem()).getHoraEvento());
		  txtLugarEvento.setText(((ServiciosHasEmpleados)tblServicioEmpleados.getSelectionModel().getSelectedItem()).getLugarEvento());
		  cmbCodigoServicio.getSelectionModel().select(buscarServicio(((ServiciosHasEmpleados)tblServicioEmpleados.getSelectionModel().getSelectedItem()).getCodigoServicio()));
		  cmbCodigoEmpleado.getSelectionModel().select(buscarEmpleado(((ServiciosHasEmpleados)tblServicioEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpleado()));
		  fecha.selectedDateProperty().set(((ServiciosHasEmpleados)tblServicioEmpleados.getSelectionModel().getSelectedItem()).getFechaEvento());
	 }
	 
	 public Empleado buscarEmpleado(int codigoEmpleado){
		  Empleado resultado = null;
		  try{
			   PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarEmpleados(?)}");
			   procedimiento.setInt(1, codigoEmpleado);
			   ResultSet registro = procedimiento.executeQuery();
			   while(registro.next()){
					resultado = new Empleado(registro.getInt("codigoEmpleado"),
											 registro.getInt("numeroEmpleado"),
											 registro.getString("apellidoEmpleado"),
											 registro.getString("nombreEmpleado"),
											 registro.getString("direccionEmpleado"),
											 registro.getString("telefonoContacto"),
											 registro.getString("gradoCocinero"),
											 registro.getInt("codigoTipoEmpleado"));
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
	 
	 public void Nuevo(){
		  switch(tipoDeOperacion){
			   case NINGUNO:
					limpiarControles();
					cargarDatos();
					activarControles();
					btnNuevo.setText("Guardar");
					btnEliminar.setText("Cancelar");
					btnEditar.setDisable(true);
					btnReporte.setDisable(true);
					tipoDeOperacion = operaciones.GUARDAR;
			   break;
			   case GUARDAR:
					guardarElementos();
					cargarDatos();
					limpiarControles();
					desactivarControles();
					btnNuevo.setText("Nuevo");
					btnEliminar.setText("Eliminar");
					btnEditar.setDisable(false);
					btnReporte.setDisable(false);
					tipoDeOperacion = operaciones.NINGUNO;
			   break;
		  }
	 }
	 
	 public void guardarElementos(){
		  ServiciosHasEmpleados registro = new ServiciosHasEmpleados();
		  registro.setCodigoServicio(((Servicio)cmbCodigoServicio.getSelectionModel().getSelectedItem()).getCodigoServicio());
		  registro.setCodigoEmpleado(((Empleado)cmbCodigoEmpleado.getSelectionModel().getSelectedItem()).getCodigoEmpleado());
		  registro.setFechaEvento(fecha.getSelectedDate());
		  registro.setHoraEvento(txtHoraEvento.getText());
		  registro.setLugarEvento(txtLugarEvento.getText());
		  try{
			   PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarServiciosHasEmpleados(?,?,?,?,?)}");
			   procedimiento.setInt(1, registro.getCodigoServicio());
			   procedimiento.setInt(2, registro.getCodigoEmpleado());
			   procedimiento.setString(3, registro.getHoraEvento());
			   procedimiento.setDate(4, new java.sql.Date(registro.getFechaEvento().getTime()));
			   procedimiento.setString(5, registro.getLugarEvento());
			   procedimiento.executeQuery();
			   listaServiciosEmpleados.add(registro);
		  }catch(Exception e){
			   e.printStackTrace();
		  }
	 }
	 
	 public void Eliminar(){
		  switch(tipoDeOperacion){
			   case GUARDAR:
					cargarDatos();
					limpiarControles();
					desactivarControles();
					btnNuevo.setText("Nuevo");
					btnEliminar.setText("Eliminar");
					btnEditar.setDisable(false);
					btnReporte.setDisable(false);
					tipoDeOperacion = operaciones.NINGUNO;
			   break;
			   default:
					if(tblServicioEmpleados.getSelectionModel().getSelectedItem() != null){
						 int respuesta = JOptionPane.showConfirmDialog(null,"¿Estas seguro de eliminar el registro?","Eliminar Servicios_De_Empleados",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
						 if(respuesta == JOptionPane.YES_OPTION)
							  try{
								   PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarServiciosHasEmpleados(?)}");
								   procedimiento.setInt(1, ((ServiciosHasEmpleados)tblServicioEmpleados.getSelectionModel().getSelectedItem()).getCodigoServiciosHasEmpleados());
								   procedimiento.executeQuery();
								   listaServiciosEmpleados.remove(tblServicioEmpleados.getSelectionModel().getSelectedIndex());
								   limpiarControles();
								   desactivarControles();
								   cargarDatos();								   
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
					if(tblServicioEmpleados.getSelectionModel().getSelectedItem() != null){
						 activarControles();
						 btnNuevo.setDisable(true);
						 btnEliminar.setDisable(true);
						 btnEditar.setText("Actualizar");
						 btnReporte.setText("Cancelar");
						 tipoDeOperacion = operaciones.ACTUALIZAR;
					}else{
						 JOptionPane.showMessageDialog(null, "Debe Seleccionar un elemento antes de actualizar");
					}						 
			   break;
			   case ACTUALIZAR:
					actualizar();
					desactivarControles();
					limpiarControles();
					btnNuevo.setDisable(false);
					btnEliminar.setDisable(false);
					btnEditar.setText("Editar");
					btnReporte.setText("Reporte");
					tipoDeOperacion = operaciones.NINGUNO;
					cargarDatos();
			   break;
		  }
	 }
	 
	 public void actualizar(){
		  try{
			   PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarServiciosHasEmpleados(?,?,?,?,?,?)}");
			   procedimiento.setInt(1, Integer.parseInt(txtCodigo.getText()));
			   procedimiento.setInt(2, (((Servicio)cmbCodigoServicio.getSelectionModel().getSelectedItem()).getCodigoServicio()));
			   procedimiento.setInt(3, (((Empleado)cmbCodigoEmpleado.getSelectionModel().getSelectedItem()).getCodigoEmpleado()));
			   procedimiento.setString(4, txtHoraEvento.getText());
			   procedimiento.setDate(5, new java.sql.Date(fecha.getSelectedDate().getTime()));
			   procedimiento.setString(6, txtLugarEvento.getText());
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
					cargarDatos();
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
		  txtHoraEvento.setText("");
		  txtLugarEvento.setText("");
		  cmbCodigoServicio.getSelectionModel().clearSelection();
		  cmbCodigoEmpleado.getSelectionModel().clearSelection();		  
	 }
	 
	 public void desactivarControles(){
		  txtCodigo.setEditable(false);
		  txtHoraEvento.setEditable(false);
		  txtLugarEvento.setEditable(false);
		  cmbCodigoServicio.setEditable(false);
		  cmbCodigoEmpleado.setDisable(false);
		  grpFechaEvento.setDisable(true);
	 }
	 
	 public void activarControles(){
		  txtCodigo.setEditable(false);
		  txtHoraEvento.setEditable(true);
		  txtLugarEvento.setEditable(true);
		  cmbCodigoServicio.setDisable(false);
		  cmbCodigoEmpleado.setDisable(false);
		  grpFechaEvento.setDisable(false);
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

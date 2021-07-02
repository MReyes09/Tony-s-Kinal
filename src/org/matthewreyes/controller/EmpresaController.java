
package org.matthewreyes.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
import org.matthewreyes.db.Conexion;
import org.matthewreyes.report.GenerarReporte;
import org.matthewreyes.system.Principal;

public class EmpresaController implements Initializable{
	 private Principal escenarioPrincipal;
	 private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};  
	 private operaciones tipoDeOperacion = operaciones.NINGUNO;
	 private ObservableList <Empresa> listaEmpresa;
	 @FXML private TextField txtCodigoEmpresa; 
	 @FXML private TextField txtNombreEmpresa;
	 @FXML private TextField txtDireccionEmpresa;
	 @FXML private TextField txtTelefonoEmpresa;
	 @FXML private TableView tblEmpresas;
	 @FXML private TableColumn colCodigoEmpresa;
	 @FXML private TableColumn colNombreEmpresa;
	 @FXML private TableColumn colDireccionEmpresa;
	 @FXML private TableColumn colTelefonoEmpresa;
	 @FXML private Button btnNuevo;
	 @FXML private Button btnEliminar;
	 @FXML private Button btnEditar;
	 @FXML private Button btnReporte;
	 	 
	 @Override
	 public void initialize(URL location, ResourceBundle resources) {
		  cargarDatos();
	 }
	 
	 public void cargarDatos(){
		  tblEmpresas.setItems(getEmpresa());
		  colCodigoEmpresa.setCellValueFactory(new PropertyValueFactory<Empresa, Integer>("codigoEmpresa")); 
		  colNombreEmpresa.setCellValueFactory(new PropertyValueFactory<Empresa, String>("nombreEmpresa"));
		  colDireccionEmpresa.setCellValueFactory(new PropertyValueFactory<Empresa, String>("direccion"));
		  colTelefonoEmpresa.setCellValueFactory(new PropertyValueFactory<Empresa, Integer>("telefono"));
		  
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
	 
	  public void desactivarControles(){
		  txtCodigoEmpresa.setEditable(false);
		  txtNombreEmpresa.setEditable(false);
		  txtDireccionEmpresa.setEditable(false);
		  txtTelefonoEmpresa.setEditable(false);
		  
	 }
	 
	 public void activarControles(){
		  txtCodigoEmpresa.setEditable(false);
		  txtNombreEmpresa.setEditable(true);
		  txtDireccionEmpresa.setEditable(true);
		  txtTelefonoEmpresa.setEditable(true);
	 }
	 
	 public void limpiarControles(){
		  txtCodigoEmpresa.setText("");
		  txtNombreEmpresa.setText("");
		  txtDireccionEmpresa.setText("");
		  txtTelefonoEmpresa.setText("");
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
	 
	 public void seleccionarElemento(){
		  txtCodigoEmpresa.setText(String.valueOf(((Empresa)tblEmpresas.getSelectionModel().getSelectedItem()).getCodigoEmpresa()));
		  txtNombreEmpresa.setText(((Empresa)tblEmpresas.getSelectionModel().getSelectedItem()).getNombreEmpresa());
		  txtDireccionEmpresa.setText(((Empresa)tblEmpresas.getSelectionModel().getSelectedItem()).getDireccion());
		  txtTelefonoEmpresa.setText(((Empresa)tblEmpresas.getSelectionModel().getSelectedItem()).getTelefono());
	 } 
	 
	 public Empresa buscarEmpresa(int codigoEmpresa){
		  Empresa resultado = null;
		  try{
			   PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarEmpresa(?)}");
			   ResultSet registro = procedimiento.executeQuery();
			   while (registro.next()){
					resultado = new Empresa(
							  registro.getInt("codigoEmpresa"),
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

	 public void Eliminar(){
		  switch (tipoDeOperacion){
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
					if(tblEmpresas.getSelectionModel().getSelectedItem() != null){
						 int respuesta = JOptionPane.showConfirmDialog(null,"¿Estas seguro de eliminar el registro?","Eliminar empresa",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
						 if (respuesta == JOptionPane.YES_OPTION)
							  try{
								   PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarEmpresas(?)}");
								   procedimiento.setInt(1, ((Empresa)tblEmpresas.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
								   procedimiento.execute();
								   listaEmpresa.remove(tblEmpresas.getSelectionModel().getSelectedIndex());
							  }catch(Exception e){
								   e.printStackTrace();
							  }
						 }else 
							  JOptionPane.showMessageDialog(null,"Debe seleccionar un elemento antes de eliminar");
			   break;
		  }
	 }
	 
	 public void actualizar(){
		  try{
			   PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarEmpresas(?,?,?,?)}");
			   Empresa registro = (Empresa)tblEmpresas.getSelectionModel().getSelectedItem();
			   registro.setNombreEmpresa(txtNombreEmpresa.getText());
			   registro.setDireccion(txtDireccionEmpresa.getText());
			   registro.setTelefono(txtTelefonoEmpresa.getText());
			   procedimiento.setInt(1,registro.getCodigoEmpresa());
			   procedimiento.setString(2,registro.getNombreEmpresa());
			   procedimiento.setString(3, registro.getDireccion());
			   procedimiento.setString(4, registro.getTelefono());
			   procedimiento.execute();
		  }catch(Exception e){
			   e.printStackTrace();
		  }
	 }
	 
	 public void Editar(){
		  switch(tipoDeOperacion){
			   case NINGUNO:
					if (tblEmpresas.getSelectionModel().getSelectedItem() != null){
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
					limpiarControles();
					desactivarControles();
					cargarDatos();					
			   break;			   
		  }
	 }
	 
	 public void Reporte(){
		  switch(tipoDeOperacion){
			   case ACTUALIZAR:
					limpiarControles();
					desactivarControles();
					btnEditar.setText("Editar");
					btnReporte.setText("Reporte");
					btnNuevo.setDisable(false);
					btnEliminar.setDisable(false);
					cargarDatos();
			   break;
			   case NINGUNO:
					imprimirReporte();
			   break;
		  }
	 }
	 
	 public void guardar(){
		  Empresa registro = new Empresa();
		  //registro.setCodigoEmpresa(Integer.parseInt(txtCodigoEmpresa.getText()));
		  registro.setNombreEmpresa(txtNombreEmpresa.getText());
		  registro.setDireccion(txtDireccionEmpresa.getText());
		  registro.setTelefono((txtTelefonoEmpresa.getText()));
		  try{
			   PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarEmpresas(?,?,?)}");
			   procedimiento.setString(1, registro.getNombreEmpresa());
			   procedimiento.setString(2, registro.getDireccion());
			   procedimiento.setString(3, registro.getTelefono());
			   procedimiento.execute();
			   listaEmpresa.add(registro);
		  }catch(Exception e){
			   e.printStackTrace();
		  }
	 }
	 
	 public void imprimirReporte(){
		  Map parametros = new HashMap();
		  parametros.put("codigoEmpresa", null);
		  GenerarReporte.mostrarReporte("ReporteEmpresas.jasper", "Reporte de Empresas", parametros);
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
	 
	 public void VentanaPresupuesto(){
		  escenarioPrincipal.ventanaPresupuestos();
	 }
	 
	 public void VentanaServicios(){
		  escenarioPrincipal.VentanaServicios();
	 }
	 
}

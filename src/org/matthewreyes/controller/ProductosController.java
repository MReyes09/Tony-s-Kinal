
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
import org.matthewreyes.beam.Producto;
import org.matthewreyes.db.Conexion;
import org.matthewreyes.system.Principal;

public class ProductosController implements Initializable {
	 
	 private Principal escenarioPrincipal;
	 private enum operaciones{NUEVO, GUARDAR, ACTUALIZAR, EDITAR, ELIMINAR, CANCELAR, NINGUNO};
	 private operaciones tipoDeOperacion = operaciones.NINGUNO;
	 private ObservableList<Producto> listaProducto; 
	 
	 @FXML private TextField txtCodigoProductos;
	 @FXML private TextField txtNombreProductos;
	 @FXML private TextField txtCantidadProductos;
	 @FXML private TableView tblProductos;
	 @FXML private TableColumn colCodigo;
	 @FXML private TableColumn colNombre;
	 @FXML private TableColumn colCantidad;
	 @FXML private Button btnNuevo;
	 @FXML private Button btnEliminar;
	 @FXML private Button btnEditar;
	 @FXML private Button btnReporte;
	 			   
	 @Override
	 public void initialize(URL location, ResourceBundle resources) {
		  cargarDatos();
	 }
	 
	 public void cargarDatos(){
		  tblProductos.setItems(getProductos());
		  colCodigo.setCellValueFactory(new PropertyValueFactory<Producto, Integer>("codigoProducto"));
		  colNombre.setCellValueFactory(new PropertyValueFactory<Producto, String>("nombreProducto"));
		  colCantidad.setCellValueFactory(new PropertyValueFactory<Producto, Integer>("cantidad"));
	 }
	 
	 public ObservableList<Producto> getProductos(){
		  ArrayList<Producto> lista = new ArrayList<Producto>();
		  try{
			   PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarProductos}");
			   ResultSet resultado = procedimiento.executeQuery();
			   while(resultado.next()){
					lista.add(new Producto(resultado.getInt("codigoProducto"),
											 resultado.getString("nombreProducto"),
											 resultado.getInt("cantidad")));					
			   }
		  }catch(Exception e){
			   e.printStackTrace();
		  }		  
		  return listaProducto = FXCollections.observableArrayList(lista);
	 }
	 
	 public void seleccionarDatos(){
		  txtCodigoProductos.setText(String.valueOf(((Producto)tblProductos.getSelectionModel().getSelectedItem()).getCodigoProducto()));
		  txtNombreProductos.setText(((Producto)tblProductos.getSelectionModel().getSelectedItem()).getNombreProducto());
		  txtCantidadProductos.setText(String.valueOf(((Producto)tblProductos.getSelectionModel().getSelectedItem()).getCantidad()));
	 }
	 
	 public void activarControles(){
		  txtCodigoProductos.setEditable(false);
		  txtNombreProductos.setEditable(true);
		  txtCantidadProductos.setEditable(true);
	 }
	 
	 public void limpiarControles(){
		  txtCodigoProductos.setText("");
		  txtNombreProductos.setText("");
		  txtCantidadProductos.setText("");
	 }
	 
	 public void desactivarControles(){
		  txtCodigoProductos.setEditable(false);
		  txtNombreProductos.setEditable(false);
		  txtCantidadProductos.setEditable(false);
	 }
	 
	 public void AccionNuevo(){
		  switch(tipoDeOperacion){
			   case NINGUNO:
					activarControles();
					limpiarControles();
					btnNuevo.setText("Agregar");
					btnEliminar.setText("Cancelar");
					btnEditar.setDisable(true);
					btnReporte.setDisable(true);
					cargarDatos();
					tipoDeOperacion = operaciones.GUARDAR;
			   break;
			   case GUARDAR:
					guardarDatos();
					limpiarControles();
					desactivarControles();
					btnNuevo.setText("Nuevo");
					btnEliminar.setText("Eliminar");
					btnEditar.setDisable(false);
					btnReporte.setDisable(false);
					cargarDatos();
					tipoDeOperacion = operaciones.NINGUNO;
			   break;
		  }
	 }
	 
	 public void guardarDatos(){
		  Producto registro = new Producto();
		  registro.setNombreProducto(txtNombreProductos.getText());
		  registro.setCantidad(Integer.parseInt(txtCantidadProductos.getText()));
		  try{
			   PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarProductos(?,?)}");
			   procedimiento.setString(1, registro.getNombreProducto());
			   procedimiento.setInt(2, registro.getCantidad());
			   procedimiento.executeQuery();
			   listaProducto.add(registro);
		  }catch(Exception e){
			   e.printStackTrace();
		  }
	 }
	 
	 public void accionEliminar(){
		  switch(tipoDeOperacion){
			   case GUARDAR:
					limpiarControles();
					desactivarControles();
					btnNuevo.setText("Nuevo");
					btnEliminar.setText("Eliminar");
					btnEditar.setDisable(false);
					btnReporte.setDisable(false);
					cargarDatos();
					tipoDeOperacion = operaciones.NINGUNO;	 
			   break;
			   default:
					if(tblProductos.getSelectionModel().getSelectedItem() != null){
						 int respuesta = JOptionPane.showConfirmDialog(null, "¿Estas seguro de eliminar el registro?","Eliminar Producto", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
						 if(respuesta == JOptionPane.YES_OPTION){
							  Producto registro = new Producto();
							  registro.setCodigoProducto(Integer.parseInt(txtCodigoProductos.getText()));
							  try{
								   PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarProductos(?)");
								   procedimiento.setInt(1, registro.getCodigoProducto());
								   procedimiento.executeQuery();
								   limpiarControles();
								   listaProducto.remove(tblProductos.getSelectionModel().getSelectedIndex());
							  }catch(Exception e){
								   e.printStackTrace();
							  }
						 }
					} else
						 JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento antes de eliminar");
			   break;
		  }
	 }
	 
	 public void accionEditar(){
		  switch(tipoDeOperacion){
			   case NINGUNO:
					if(tblProductos.getSelectionModel().getSelectedItem() != null){
						 activarControles();
						 btnNuevo.setDisable(true);
						 btnEliminar.setDisable(true);
						 btnEditar.setText("Actualizar");
						 btnReporte.setText("Cancelar");
						 tipoDeOperacion = operaciones.ACTUALIZAR;						 
					}else 
						 JOptionPane.showMessageDialog(null, "Selecciona un elemento antes de editar");
			   break;
			   case ACTUALIZAR:
					actualizar();
					limpiarControles();
					desactivarControles();
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
		  Producto registro = new Producto();
		  registro.setCodigoProducto(Integer.valueOf(txtCodigoProductos.getText()));
		  registro.setNombreProducto(txtNombreProductos.getText());
		  registro.setCantidad(Integer.valueOf(txtCantidadProductos.getText()));
		  try{
			   PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarProductos(?,?,?)}");
			   procedimiento.setInt(1, registro.getCodigoProducto());
			   procedimiento.setString(2, registro.getNombreProducto());
			   procedimiento.setInt(3, registro.getCantidad());
			   procedimiento.executeQuery();
		  }catch(Exception e){
			   e.printStackTrace();
		  }
	 }
	 
	 public void accionReporte(){
		  switch(tipoDeOperacion){
			   case ACTUALIZAR:
					limpiarControles();
					desactivarControles();
					btnNuevo.setDisable(false);
					btnEliminar.setDisable(false);
					btnEditar.setText("Editar");
					btnReporte.setText("Reporte");
					tipoDeOperacion = operaciones.NINGUNO;
					cargarDatos();
			   break;
		  }
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

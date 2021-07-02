
package org.matthewreyes.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import org.matthewreyes.system.Principal;

public class MenuPrincipalController implements Initializable{
	 private Principal escenarioPrincipal;

	 public Principal getEscenarioPrincipal() {
		  return escenarioPrincipal;
	 }

	 public void setEscenarioPrincipal(Principal escenarioPrincipal) {
		  this.escenarioPrincipal = escenarioPrincipal;
	 }

	 @Override
	 public void initialize(URL location, ResourceBundle resources) {
		  
	 }
	 
	 public void VentanaProgramador(){
		  escenarioPrincipal.VentanaProgramador();
	 }
	 
	 public void VentanaEmpresas(){
		  escenarioPrincipal.VentanaEmpresas();
	 }
	 
	 public void VentanaPresupuesto(){
		  escenarioPrincipal.ventanaPresupuestos();
	 }
	 
	 public void VentanaTipoEmpleados(){
		  escenarioPrincipal.VentanaTipoEmpleados();
	 }
	 
	 public void VentanaEmpleados(){
		  escenarioPrincipal.VentanaEmpleados();
	 }
	 
	 public void VentanaProductos(){
		  escenarioPrincipal.VentanaProductos();
	 }
	 
	 public void VentanaTipoPlatos(){
		  escenarioPrincipal.VentanaTipoPlatos();
	 }
	 
	 public void VentanaServicios(){
		  escenarioPrincipal.VentanaServicios();
	 }
	 
	 public void VentanaPlatos(){
		  escenarioPrincipal.VentanaPlatos();
	 }
	 
	 public void VentanaServiciosPlatos(){
		  escenarioPrincipal.VentanaServiciosPlatos();
	 }
	 
	 public void VentanaProductosHasPlatos(){
		  escenarioPrincipal.ventanaProductosHasPlatos();
	 }
	 
	 public void ventanaServicioHasEmpleado(){
		  escenarioPrincipal.ventanaServicioHasEmpleado();
	 }	 
}

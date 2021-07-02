
package org.matthewreyes.system;

import java.io.InputStream;
import java.util.HashSet;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.matthewreyes.controller.EmpresaController;
import org.matthewreyes.controller.DatosPersonalesController;
import org.matthewreyes.controller.EmpleadoController;
import org.matthewreyes.controller.MenuPrincipalController;
import org.matthewreyes.controller.PlatosController;
import org.matthewreyes.controller.PresupuestoController;
import org.matthewreyes.controller.ProductosController;
import org.matthewreyes.controller.ServicioPlatosController;
import org.matthewreyes.controller.ServiciosController;
import org.matthewreyes.controller.ServiciosEmpleadosController;
import org.matthewreyes.controller.TipoEmpleadoController;
import org.matthewreyes.controller.TipoPlatoController;
import org.matthewreyes.controller.productoPlatoController;

public class Principal extends Application {
	 private final String paquete_view = "/org/matthewreyes/view/" ;
	 private Stage escenarioPrincipal;
	 private Scene escena;
	 @Override
	 public void start(Stage escenarioPrincipal) throws Exception {
		  this.escenarioPrincipal = escenarioPrincipal;
		  this.escenarioPrincipal.setTitle("Tony's Kinal App");
		  escenarioPrincipal.getIcons().add(new Image("/org/matthewreyes/imagen/Icono.jpg"));
		  MenuPrincipal();
		  escenarioPrincipal.show();
	 }	  
	 
	 public void MenuPrincipal(){
		  try{
		       MenuPrincipalController menuPrincipal = (MenuPrincipalController)cambiarScena("MenuPrincipalView.fxml",443,411);
			   menuPrincipal.setEscenarioPrincipal(this);
		  }catch(Exception e){
			    e.printStackTrace();	
		  }
	 }
	 
	 public void VentanaProgramador(){
		  try{
			   DatosPersonalesController datosPersonales = (DatosPersonalesController)cambiarScena("ProgramadorView.fxml",531,263);
			   datosPersonales.setEscenarioPrincipal(this);
		  } catch(Exception e){
			   e.printStackTrace();
		  }
	 }

	 public void VentanaEmpresas(){
		  try{
			   EmpresaController empresaController = (EmpresaController)cambiarScena("EmpresasView.fxml",624,590);
			   empresaController.setEscenarioPrincipal(this);
		  } catch(Exception e){
			   e.printStackTrace();
		  }
	 }
	 
	 public void ventanaPresupuestos(){
		  try{
			   PresupuestoController presupuestoController = (PresupuestoController)cambiarScena("PresupuestoView.fxml",623,591);
			   presupuestoController.setEscenarioPrincipal(this);
		  }catch(Exception e){
			   e.printStackTrace();
		  }
	 }
	 
	 public void VentanaTipoEmpleados(){
		  try{
			   TipoEmpleadoController tipoEmpleadoController = (TipoEmpleadoController)cambiarScena("TipoEmpleadoView.fxml",624,590); 
			   tipoEmpleadoController.setEscenarioPrincipal(this);
		  }catch(Exception e){
			   e.printStackTrace();
		  }
	 } 
	 
	 public void VentanaEmpleados(){
		  try{
			   EmpleadoController empleadoController = (EmpleadoController)cambiarScena("EmpleadoView.fxml",1000,719);
			   empleadoController.setEscenarioPrincipal(this);
		  }catch(Exception e){
			   e.printStackTrace();
		  }
	 }
	 
	 public void VentanaProductos(){
		  try{
			  ProductosController productosController = (ProductosController)cambiarScena("ProductosView.fxml",603,570);
			  productosController.setEscenarioPrincipal(this);
		  }catch(Exception e){
			   e.printStackTrace();
		  }
	 }
	 
	 public void VentanaTipoPlatos(){
		  try{
			   TipoPlatoController tipoPlatoController = (TipoPlatoController)cambiarScena("TipoPlatoView.fxml",803,397);
			   tipoPlatoController.setEscenarioPrincipal(this);
		  }catch(Exception e){
			   e.printStackTrace();
		  }
	 }
	 
	 public void VentanaServicios(){
		  try{
			   ServiciosController serviciosController = (ServiciosController)cambiarScena("ServiciosView.fxml",950,608);
			   serviciosController.setEscenarioPrincipal(this);
		  }catch(Exception e){
			   e.printStackTrace();
		  }
	 }
	 
	 public void VentanaPlatos(){
		  try{
			   PlatosController platosController = (PlatosController)cambiarScena("PlatosView.fxml",1024,549);
			   platosController.setEscenarioPrincipal(this);
		  }catch(Exception e){
			   e.printStackTrace();
		  }
	 }
	 
	 public void VentanaServiciosPlatos(){
		  try{
			   ServicioPlatosController x = (ServicioPlatosController)cambiarScena("ServiciosHasPlatosView.fxml",723,460);
			   x.setEscenarioPrincipal(this);
		  }catch(Exception e){
			   e.printStackTrace();
		  }
	 }
	 	 
	 public void ventanaProductosHasPlatos(){
		  try{
			   productoPlatoController y = (productoPlatoController)cambiarScena("ProductosHasPlatosView.fxml",631,483);
			   y.setEscenarioPrincipal(this);
		  }catch(Exception e){
			   e.printStackTrace();
		  }		  
	 }
	 
	 public void ventanaServicioHasEmpleado(){
		  try{
			   ServiciosEmpleadosController k = (ServiciosEmpleadosController)cambiarScena("ServiciosHasEmpleadosView.fxml",932,586);
			   k.setEscenarioPrincipal(this);
		  }catch(Exception e){
			   e.printStackTrace();
		  }
	 }
	 
	 public static void main(String[] args) {
		  launch(args);
	 }
	 
	 public Initializable cambiarScena(String fxml,int ancho,int alto) throws Exception{
	      Initializable resultado = null;
	 	  FXMLLoader cargadorFXML = new FXMLLoader ();
		  InputStream archivo = Principal.class.getResourceAsStream(paquete_view+fxml);
		  cargadorFXML.setBuilderFactory(new JavaFXBuilderFactory());
		  cargadorFXML.setLocation(Principal.class.getResource(paquete_view+fxml));
		  escena = new Scene((AnchorPane)cargadorFXML.load(archivo),ancho,alto);
		  escenarioPrincipal.setScene(escena);
		  escenarioPrincipal.sizeToScene();
		  resultado = (Initializable)cargadorFXML.getController();
		  		  
		  return resultado; 
	 }
	
}

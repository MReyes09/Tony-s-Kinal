
package org.matthewreyes.beam;

public class ServicioPlato {
	 private int codigoServiciosHasPlatos;
	 private int codigoServicio;
	 private int codigoPlato;
	 
	 private ServicioPlato(){
		  
	 }

	 public ServicioPlato(int codigoServiciosHasPlatos, int codigoServicio, int codigoPlato) {
		  this.codigoServiciosHasPlatos = codigoServiciosHasPlatos;
		  this.codigoServicio = codigoServicio;
		  this.codigoPlato = codigoPlato;
	 }

	 public int getCodigoServiciosHasPlatos() {
		  return codigoServiciosHasPlatos;
	 }

	 public void setCodigoServiciosHasPlatos(int codigoServiciosHasPlatos) {
		  this.codigoServiciosHasPlatos = codigoServiciosHasPlatos;
	 }

	 public int getCodigoServicio() {
		  return codigoServicio;
	 }

	 public void setCodigoServicio(int codigoServicio) {
		  this.codigoServicio = codigoServicio;
	 }

	 public int getCodigoPlato() {
		  return codigoPlato;
	 }

	 public void setCodigoPlato(int codigoPlato) {
		  this.codigoPlato = codigoPlato;
	 }	 	 
}

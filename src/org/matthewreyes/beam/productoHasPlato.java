
package org.matthewreyes.beam;

public class productoHasPlato {
	 
	 private int codigoProductosHasPlatos;
	 private int codigoProducto;
	 private int codigoPlato;
	 
	 public productoHasPlato(){		  
	 }

	 public productoHasPlato(int codigoProductosHasPlatos, int codigoProducto, int codigoPlato) {
		  this.codigoProductosHasPlatos = codigoProductosHasPlatos;
		  this.codigoProducto = codigoProducto;
		  this.codigoPlato = codigoPlato;
	 }

	 public int getCodigoProductosHasPlatos() {
		  return codigoProductosHasPlatos;
	 }

	 public void setCodigoProductosHasPlatos(int codigoProductosHasPlatos) {
		  this.codigoProductosHasPlatos = codigoProductosHasPlatos;
	 }

	 public int getCodigoProducto() {
		  return codigoProducto;
	 }

	 public void setCodigoProducto(int codigoProducto) {
		  this.codigoProducto = codigoProducto;
	 }

	 public int getCodigoPlato() {
		  return codigoPlato;
	 }

	 public void setCodigoPlato(int codigoPlato) {
		  this.codigoPlato = codigoPlato;
	 }	 	 
}

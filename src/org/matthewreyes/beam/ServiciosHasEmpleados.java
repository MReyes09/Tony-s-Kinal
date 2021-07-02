
package org.matthewreyes.beam;

import java.util.Date;

public class ServiciosHasEmpleados {
	 private int codigoServiciosHasEmpleados;
	 private int codigoServicio;
	 private int codigoEmpleado; 
	 private Date fechaEvento; 
	 private String horaEvento;
	 private String lugarEvento;

	 public ServiciosHasEmpleados() {
	 }

	 public ServiciosHasEmpleados(int codigoServiciosHasEmpleados, int codigoServicio, int codigoEmpleado, Date fechaEvento, String horaEvento, String lugarEvento) {
		  this.codigoServiciosHasEmpleados = codigoServiciosHasEmpleados;
		  this.codigoServicio = codigoServicio;
		  this.codigoEmpleado = codigoEmpleado;
		  this.fechaEvento = fechaEvento;
		  this.horaEvento = horaEvento;
		  this.lugarEvento = lugarEvento;
	 }

	 public int getCodigoServiciosHasEmpleados() {
		  return codigoServiciosHasEmpleados;
	 }

	 public void setCodigoServiciosHasEmpleados(int codigoServiciosHasEmpleados) {
		  this.codigoServiciosHasEmpleados = codigoServiciosHasEmpleados;
	 }

	 public int getCodigoServicio() {
		  return codigoServicio;
	 }

	 public void setCodigoServicio(int codigoServicio) {
		  this.codigoServicio = codigoServicio;
	 }

	 public int getCodigoEmpleado() {
		  return codigoEmpleado;
	 }

	 public void setCodigoEmpleado(int codigoEmpleado) {
		  this.codigoEmpleado = codigoEmpleado;
	 }

	 public Date getFechaEvento() {
		  return fechaEvento;
	 }

	 public void setFechaEvento(Date fechaEvento) {
		  this.fechaEvento = fechaEvento;
	 }

	 public String getHoraEvento() {
		  return horaEvento;
	 }

	 public void setHoraEvento(String horaEvento) {
		  this.horaEvento = horaEvento;
	 }

	 public String getLugarEvento() {
		  return lugarEvento;
	 }

	 public void setLugarEvento(String lugarEvento) {
		  this.lugarEvento = lugarEvento;
	 }	 
}

package com.sistemaventas.sistema_venta_spring.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Proveedor que abastece los productos del inventario.
 * Se mapea a la tabla {@code proveedor}.
 */
@Entity
@Table(name = "proveedor")
public class Proveedor {

    /** Identificador generado por MySQL (AUTO_INCREMENT). */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_Proveedores")
    private Integer idProveedor;

    /** Nombre de la empresa proveedora. */
    @NotBlank(message = "El nombre de la empresa es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar 100 caracteres")
    @Column(name = "nombre_empresa", nullable = false, length = 100)
    private String nombreEmpresa;

    /** Telefono de contacto (opcional). */
    @Size(max = 20, message = "El telefono no puede superar 20 caracteres")
    @Column(name = "telefono", length = 20)
    private String telefono;

    /** Correo de contacto (opcional). */
    @Email(message = "El correo no tiene un formato valido")
    @Size(max = 100, message = "El correo no puede superar 100 caracteres")
    @Column(name = "correo", length = 100)
    private String correo;

    /** Direccion del proveedor (opcional). */
    @Size(max = 150, message = "La direccion no puede superar 150 caracteres")
    @Column(name = "direccion", length = 150)
    private String direccion;

        /** Constructor sin argumentos que exige JPA. */
    public Proveedor() {
    }

    public Integer getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(Integer idProveedor) {
        this.idProveedor = idProveedor;
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}
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
 * Cliente al que se le registran ventas.
 * Se mapea a la tabla {@code clientes}.
 */
@Entity
@Table(name = "clientes")
public class Cliente {

    /** Identificador generado por MySQL (AUTO_INCREMENT). */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_clientes")
    private Integer idCliente;

    /** Nombre completo del cliente. */
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar 100 caracteres")
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    /** Numero de documento de identidad. */
    @NotBlank(message = "El documento es obligatorio")
    @Size(max = 20, message = "El documento no puede superar 20 caracteres")
    @Column(name = "documento", nullable = false, length = 20)
    private String documento;

    /** Telefono de contacto (opcional). */
    @Size(max = 20, message = "El telefono no puede superar 20 caracteres")
    @Column(name = "telefono", length = 20)
    private String telefono;

    /** Correo de contacto (opcional). */
    @Email(message = "El correo no tiene un formato valido")
    @Size(max = 100, message = "El correo no puede superar 100 caracteres")
    @Column(name = "correo", length = 100)
    private String correo;

    /** Direccion del cliente (opcional). */
    @Size(max = 150, message = "La direccion no puede superar 150 caracteres")
    @Column(name = "direccion", length = 150)
    private String direccion;

    /** Constructor sin argumentos que exige JPA. */
    public Cliente() {
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
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

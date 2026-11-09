package com.sistemaventas.sistemaventaspring.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Categoria a la que pertenece un producto del inventario.
 * Se mapea a la tabla {@code categorias}.
 */
@Entity
@Table(name = "categorias")
public class Categoria {

    /** Identificador generado por MySQL (AUTO_INCREMENT). */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_Categoria")
    private Integer idCategoria;

    /** Nombre de la categoria; no se puede repetir. */
    @NotBlank(message = "El nombre de la categoria es obligatorio")
    @Size(max = 50, message = "El nombre no puede superar 50 caracteres")
    @Column(name = "nombre", nullable = false, length = 50, unique = true)
    private String nombre;

    /** Descripcion opcional de la categoria. */
    @Size(max = 200, message = "La descripcion no puede superar 200 caracteres")
    @Column(name = "descripcion", length = 200)
    private String descripcion;

    /** Constructor sin argumentos que exige JPA. */
    public Categoria() {
    }

    public Integer getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
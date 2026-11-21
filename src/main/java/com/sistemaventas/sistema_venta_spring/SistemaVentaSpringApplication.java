package com.sistemaventas.sistema_venta_spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal del sistema de ventas e inventario.
 * Arranca la aplicacion Spring Boot y su servidor web.
 */
@SpringBootApplication
public class SistemaVentaSpringApplication {

    /**
     * Punto de entrada de la aplicacion.
     *
     * @param args argumentos de la linea de comandos
     */
    public static void main(String[] args) {
        SpringApplication.run(SistemaVentaSpringApplication.class, args);
    }

}

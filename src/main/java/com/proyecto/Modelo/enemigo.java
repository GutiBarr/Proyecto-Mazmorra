package com.proyecto.Modelo;

import java.util.Random;

/**
 * @author Luis Gordillo
 * @author Roberto Borrallo
 * @author José María Gutierrez
 */

public class enemigo extends Personaje {
    private tipoEnemigo tipo;
    private int percepcion;
    private Mazmorra mazmorra;
    private int saludMax;

    public enemigo(int salud, int fuerza, int defensa, int velocidad, int percepcion, tipoEnemigo tipo) {
        super(salud, fuerza, defensa, velocidad);
        this.percepcion = percepcion;
        this.saludMax = salud;
        this.tipo = tipo;
    }

    /**
     * Establece la referencia a la mazmorra para poder acceder al estado del juego
     * 
     * @param mazmorra La mazmorra en la que se encuentra el enemigo
     */
    public void setMazmorra(Mazmorra mazmorra) {
        this.mazmorra = mazmorra;
    }

    /**
     * Obtiene el valor de percepción del objeto.
     *
     * @return el valor actual de percepción.
     */
    public int getPercepcion() {
        return this.percepcion;
    }

    /**
     * Establece un nuevo valor para la percepción.
     *
     * @param percepcion el nuevo valor de percepción.
     */
    public void setPercepcion(int percepcion) {
        this.percepcion = percepcion;
    }

    /**
     * Obtiene el valor de salud máxima.
     *
     * @return la salud máxima del objeto.
     */
    public int getSaludMax() {
        return this.saludMax;
    }

    /**
     * Establece el tipo de enemigo.
     *
     * @param tipo el nuevo tipo de enemigo.
     */
    public void setTipo(tipoEnemigo tipo) {
        this.tipo = tipo;
    }

    /**
     * Obtiene el tipo de enemigo.
     *
     * @return el tipo de enemigo asignado.
     */
    public tipoEnemigo getTipo() {
        return this.tipo;
    }

    /**
     * Implementación del método jugar para el enemigo.
     * El enemigo decide automáticamente su movimiento basado en la posición del
     * jugador y su atributo de percepción.
     */
    @Override
    public void jugar() {
        if (mazmorra == null || getSalud() <= 0) {
            System.out.println("Mazmorra no definida o el enemigo ya ha muerto");

        } else {

            Protagonista jugador = mazmorra.getJugador();
            if ((jugador.getSalud() <= 0) || jugador == null) {
                System.out.println("No hacer nada si el jugador ya está muerto");
            } else {
                // Calcular distancia al jugador
                int distX = Math.abs(getPosX() - jugador.getPosX());
                int distY = Math.abs(getPosY() - jugador.getPosY());
                int distancia = distX + distY; // Distancia Manhattan

                int nuevaX = getPosX();
                int nuevaY = getPosY();

                if (distancia == 1) {
                    mazmorra.procesarAtaque(this, jugador);
                }

                // Determinar movimiento basado en la percepción
                if (distancia <= percepcion) {
                    // Moverse hacia el jugador
                    if (distX > distY) {
                        // Moverse en X
                        nuevaX += (getPosX() < jugador.getPosX()) ? 1 : -1;
                    } else {
                        // Moverse en Y
                        nuevaY += (getPosY() < jugador.getPosY()) ? 1 : -1;
                    }
                } else {
                    // Moverse aleatoriamente
                    Random rand = new Random();
                    int direccion = rand.nextInt(4);

                    switch (direccion) {
                        case 0:
                            nuevaY--;
                            break; // Arriba
                        case 1:
                            nuevaY++;
                            break; // Abajo
                        case 2:
                            nuevaX--;
                            break; // Izquierda
                        case 3:
                            nuevaX++;
                            break; // Derecha
                    }
                }

                // Verificar si la nueva posición está dentro de los límites
                if (nuevaX < 0 || nuevaX >= mazmorra.getAncho() ||
                        nuevaY < 0 || nuevaY >= mazmorra.getAlto()) {
                    return; // No hacer nada si se sale de los límites
                }

                Celda nuevaCelda = mazmorra.getEscenario()[nuevaY][nuevaX];

                // Verificar si es una pared
                if (nuevaCelda.esPared()) {
                    System.out.println("El enemigo no puede moverse a una pared");
                }

                // Verificar si está ocupada
                else if (nuevaCelda.estaOcupada()) {
                    Personaje ocupante = nuevaCelda.getOcupante();

                    // Si es el jugador, atacar
                    if (ocupante instanceof Protagonista) {
                        mazmorra.procesarAtaque(this, ocupante);
                    }
                } else {
                    // Mover al enemigo
                    mazmorra.moverPersonaje(this, nuevaX, nuevaY);
                }
            }
        }
    }
}
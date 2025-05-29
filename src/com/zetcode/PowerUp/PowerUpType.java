package com.zetcode.powerup;

public enum PowerUpType {
    RAPID_FIRE("Disparo Rápido", 5000), // 5 segundos
    SHIELD("Escudo Temporal", 0),       // Dura hasta que recibe un impacto
    DOUBLE_SHOT("Disparo Doble", 8000), // 8 segundos
    AREA_BOMB("Bomba de Área", 0),      // Efecto instantáneo
    EXTRA_LIFE("Vida Extra", 0);        // Efecto instantáneo

    private final String name;
    private final int duration; // duración en milisegundos

    PowerUpType(String name, int duration) {
        this.name = name;
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public int getDuration() {
        return duration;
    }
}
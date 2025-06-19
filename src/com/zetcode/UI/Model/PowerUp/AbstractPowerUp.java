package com.zetcode.UI.Model.PowerUp;
import java.awt.Image;

public abstract class AbstractPowerUp implements PowerUpStrategy {
    protected boolean active;
    protected int x;
    protected int y;
    protected boolean visible;
    protected Image image;
    protected PowerUpType type;

    public AbstractPowerUp(PowerUpType type, int x, int y){
        this.type = type;
        this.x = x;
        this.y = y;
        this.active = false;
        this.visible = false;
        loadImage();
    }

    protected  abstract void loadImage();

    public void move(){
        y+=3; //caida del power-up
    }

    public  boolean isVisible(){
        System.out.println("Verificando visibilidad del power-up " + type.getName() + ": " + visible);
        return visible;
    }

    public void setVisible(boolean visible){
        System.out.println("Cambiando visibilidad del power-up " + type.getName() + " a: " + visible);
        this.visible = visible;
    }

    public Image getImage(){
        return image;
    }

    public int getX(){
        return x;
    }

    public int getY() {
        return y;
    }

    public PowerUpType getType() {
        return type;
    }

    @Override
    public int getDuration(){
        return type.getDuration();
    }
    @Override
    public boolean isActive(){
        return active;
    }

    @Override
    public void setActive(boolean active){
        this.active = active;
    }

}

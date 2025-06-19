package com.zetcode.UI.Model.PowerUp;

import com.zetcode.UI.Model.Player;

public interface PowerUpStrategy {
    void applyEffect(Player player);
    void removeEffect(Player player);
    int getDuration();
    boolean isActive();
    void setActive(boolean active);
}

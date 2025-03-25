package com.retronimia.pong4u;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Paddle {

    private float x, y;
    private float width, height;
    private Texture texture;
    private boolean isPlayer;
    private Rectangle bounds;

    public Paddle(float x, float y, float width, float height, Texture texture, boolean isPlayer) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.texture = texture;
        this.isPlayer = isPlayer;
        bounds = new Rectangle(x, y, width, height);
    }

    public void update(float delta, float velocity, float screenHeight) {
        if (isPlayer) {
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                y += velocity * delta;
            }

            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                y -= velocity * delta;
            }
        }
        // Garante que a raquete não saia dos limites verticais da tela
        if (y < 0) {
            y = 0;
        } else if (y > screenHeight - height) {
            y = screenHeight - height;
        }
        // Atualiza os limites do retângulo
        bounds.setPosition(x, y);
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, x, y, width, height);
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public boolean isPlayer() {
        return isPlayer;
    }
}

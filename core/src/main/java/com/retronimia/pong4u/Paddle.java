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

    // Método de atualização para o jogador (controle via teclado)
    public void update(float delta, float velocity, float screenHeight) {
        if (isPlayer) {
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                y += velocity * delta;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                y -= velocity * delta;
            }
        }
        // Garante que a raquete não saia dos limites da tela
        if (y < 0) {
            y = 0;
        } else if (y > screenHeight - height) {
            y = screenHeight - height;
        }
        bounds.setPosition(x, y);
    }

    // Novo método para controlar a raquete via IA
    public void updateAI(float delta, Ball ball, float screenHeight) {
        // Calcula o centro da raquete e da bola
        float paddleCenter = y + height / 2;
        float ballCenter = ball.getY() + ball.getHeight() / 2;
        float diff = ballCenter - paddleCenter;

        // Define uma zona morta para evitar microajustes
        float threshold = 10; // Se a diferença for menor que 10 pixels, a raquete não se move

        // Velocidade máxima da raquete controlada pela IA (valor ajustável conforme a dificuldade)
        float aiSpeed = 300;

        if (Math.abs(diff) > threshold) {
            if (diff > 0) {
                // Se a bola está abaixo, move a raquete para baixo
                y += aiSpeed * delta;
                if (y + height > screenHeight) {
                    y = screenHeight - height;
                }
            } else {
                // Se a bola está acima, move a raquete para cima
                y -= aiSpeed * delta;
                if (y < 0) {
                    y = 0;
                }
            }
        }
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

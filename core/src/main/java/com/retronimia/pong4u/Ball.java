package com.retronimia.pong4u;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Ball {

    private float x, y, width, height;
    private Texture texture;
    private float velocityX, velocityY;
    private float initialSpeed;
    private float currentSpeed;
    private Rectangle bounds;
    private int hitCount = 0;
    private final float maxVerticalSpeed = 350;

    public Ball(float x, float y, float width, float height, Texture texture, float initialSpeed) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.texture = texture;
        this.initialSpeed = initialSpeed;
        this.currentSpeed = initialSpeed;
        this.velocityX = currentSpeed * (Math.random() < 0.5 ? -1 : 1);
        this.velocityY = currentSpeed * (Math.random() < 0.5 ? -1 : 1);
        bounds = new Rectangle(x, y, width, height);
    }

    public void update(float delta, Paddle paddlePlayer, Paddle paddleComputer, float screenWidth, float screenHeight) {
        x += velocityX * delta;
        y += velocityY * delta;
        bounds.setPosition(x, y);

        if (y < 0) {
            y = 0;
            velocityY = -velocityY;
            bounds.setY(y);
        } else if (y > screenHeight - height) {
            y = screenHeight - height;
            velocityY = -velocityY;
            bounds.setY(y);
        }

        if (bounds.overlaps(paddlePlayer.getBounds())) {
            handlePaddleCollision(paddlePlayer);
        }
        if (bounds.overlaps(paddleComputer.getBounds())) {
            handlePaddleCollision(paddleComputer);
        }

        if (x < 0 || x > screenWidth) {
            reset(screenWidth, screenHeight);
        }
    }

    private void handlePaddleCollision(Paddle paddle) {
        float paddleCenterY = paddle.getBounds().y + paddle.getBounds().height / 2;
        float ballCenterY = this.y + this.height / 2;
        float offset = ballCenterY - paddleCenterY;
        float normalizedOffset = offset / (paddle.getBounds().height / 2);
        if (normalizedOffset < -1) normalizedOffset = -1;
        if (normalizedOffset > 1) normalizedOffset = 1;

        float ballCenterX = this.x + this.width / 2;
        float paddleCenterX = paddle.getBounds().x + paddle.getBounds().width / 2;

        boolean validHit;
        if (paddle.isPlayer()) {
            validHit = (ballCenterX < paddleCenterX);
        } else {
            validHit = (ballCenterX > paddleCenterX);
        }

        if (validHit) {
            if (paddle.isPlayer()) {
                x = paddle.getBounds().x - width;
            } else {
                x = paddle.getBounds().x + paddle.getBounds().width;
            }
            velocityX = -velocityX;
            velocityY = normalizedOffset * maxVerticalSpeed;
            hitCount++;
            if (hitCount % 5 == 0) {
                currentSpeed += 20;
                velocityX = (velocityX < 0 ? -1 : 1) * currentSpeed;
            }
        } else {
            // Para toque inválido, inverte a velocidade vertical e reposiciona a bola para fora do campo.
            velocityY = -velocityY;
            if (paddle.isPlayer()) {
                x = paddle.getBounds().x + paddle.getBounds().width;
            } else {
                x = paddle.getBounds().x - width;
            }
        }
        bounds.setPosition(x, y);
    }

    public void reset(float screenWidth, float screenHeight) {
        x = screenWidth / 2 - width / 2;
        y = screenHeight / 2 - height / 2;
        currentSpeed = initialSpeed;
        hitCount = 0;
        velocityX = currentSpeed * (Math.random() < 0.5 ? -1 : 1);
        velocityY = currentSpeed * (Math.random() < 0.5 ? -1 : 1);
        bounds.setPosition(x, y);
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, x, y, width, height);
    }

    public Rectangle getBounds() {
        return bounds;
    }

    // Novos getters para a posição vertical e altura da bola (usados pela IA)
    public float getY() {
        return y;
    }

    public float getHeight() {
        return height;
    }
}

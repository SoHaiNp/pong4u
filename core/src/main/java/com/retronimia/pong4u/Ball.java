package com.retronimia.pong4u;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Ball {

    private float x, y, width, height;
    private Texture texture;
    private float velocityX, velocityY;
    private float initialSpeed;
    private Rectangle bounds;

    public Ball(float x, float y, float width, float height, Texture texture, float initialSpeed) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.texture = texture;
        this.initialSpeed = initialSpeed;
        // Inicia a bola com uma direção aleatória
        velocityX = initialSpeed * (Math.random() < 0.5 ? -1 : 1);
        velocityY = initialSpeed * (Math.random() < 0.5 ? -1 : 1);
        bounds = new Rectangle(x, y, width, height);
    }

    public void update(float delta, Paddle paddlePlayer, Paddle paddleComputer, float screenWidth, float screenHeight) {
        // Atualiza a posição da bola
        x += velocityX * delta;
        y += velocityY * delta;
        bounds.setPosition(x, y);

        // Verifica colisão com os limites superior e inferior
        if (y < 0) {
            y = 0;
            velocityY = -velocityY;
            bounds.setY(y);
        } else if (y > screenHeight - height) {
            y = screenHeight - height;
            velocityY = -velocityY;
            bounds.setY(y);
        }

        // Verifica colisão com a raquete do jogador (lado direito)
        if (bounds.overlaps(paddlePlayer.getBounds())) {
            resolvePaddleCollision(paddlePlayer);
        }

        // Verifica colisão com a raquete do computador (lado esquerdo)
        if (bounds.overlaps(paddleComputer.getBounds())) {
            resolvePaddleCollision(paddleComputer);
        }

        // Se a bola sair dos limites laterais, reinicia sua posição
        if (x < 0 || x > screenWidth) {
            reset(screenWidth, screenHeight);
        }
    }

    private void resolvePaddleCollision(Paddle paddle) {
        // Obtemos os bounds da raquete
        Rectangle paddleBounds = paddle.getBounds();

        // Calcula os overlaps em X e Y
        float overlapLeft = (x + width) - paddleBounds.x; // Quando a bola colide do lado esquerdo da raquete
        float overlapRight = (paddleBounds.x + paddleBounds.width) - x; // Colisão pelo lado direito
        // Para o eixo vertical, calculamos os overlaps de cima e de baixo
        float overlapBottom = (y + height) - paddleBounds.y;
        float overlapTop = (paddleBounds.y + paddleBounds.height) - y;

        // Escolhemos os menores overlaps em cada direção
        float minOverlapX = Math.min(overlapLeft, overlapRight);
        float minOverlapY = Math.min(overlapTop, overlapBottom);

        // Se a colisão for predominantemente horizontal, invertemos velocityX e reposicionamos horizontalmente
        if (minOverlapX < minOverlapY) {
            velocityX = -velocityX;
            // Se for a raquete do jogador (lado direito), reposiciona a bola à esquerda da raquete;
            // Se for a raquete do computador (lado esquerdo), reposiciona a bola à direita da raquete.
            if (paddle.isPlayer()) {
                x = paddleBounds.x - width;
            } else {
                x = paddleBounds.x + paddleBounds.width;
            }
            bounds.setX(x);
        } else {
            // Caso contrário, é uma colisão vertical – invertemos velocityY e reposicionamos verticalmente
            velocityY = -velocityY;
            // Se a bola estiver acima do centro da raquete, reposiciona-a para cima; caso contrário, para baixo.
            float paddleCenterY = paddleBounds.y + paddleBounds.height / 2;
            float ballCenterY = y + height / 2;
            if (ballCenterY > paddleCenterY) {
                y = paddleBounds.y + paddleBounds.height;
            } else {
                y = paddleBounds.y - height;
            }
            bounds.setY(y);
        }
    }

    public void reset(float screenWidth, float screenHeight) {
        // Posiciona a bola no centro da tela
        x = screenWidth / 2 - width / 2;
        y = screenHeight / 2 - height / 2;
        velocityX = initialSpeed * (Math.random() < 0.5 ? -1 : 1);
        velocityY = initialSpeed * (Math.random() < 0.5 ? -1 : 1);
        bounds.setPosition(x, y);
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, x, y, width, height);
    }

    public Rectangle getBounds() {
        return bounds;
    }
}

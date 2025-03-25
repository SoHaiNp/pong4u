package com.retronimia.pong4u;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class DebugRenderer {

    private ShapeRenderer shapeRenderer;

    public DebugRenderer() {
        shapeRenderer = new ShapeRenderer();
    }

    public void render(OrthographicCamera camera, Ball ball, Paddle paddlePlayer, Paddle paddleComputer) {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        // Desenha os bounds da bola em vermelho
        Rectangle ballBounds = ball.getBounds();
        shapeRenderer.setColor(1, 0, 0, 1);
        shapeRenderer.rect(ballBounds.x, ballBounds.y, ballBounds.width, ballBounds.height);

        // Desenha os bounds da raquete do jogador em verde
        Rectangle playerBounds = paddlePlayer.getBounds();
        shapeRenderer.setColor(0, 1, 0, 1);
        shapeRenderer.rect(playerBounds.x, playerBounds.y, playerBounds.width, playerBounds.height);

        // Desenha os bounds da raquete do computador em azul
        Rectangle computerBounds = paddleComputer.getBounds();
        shapeRenderer.setColor(0, 0, 1, 1);
        shapeRenderer.rect(computerBounds.x, computerBounds.y, computerBounds.width, computerBounds.height);

        shapeRenderer.end();
    }

    public void dispose() {
        shapeRenderer.dispose();
    }
}

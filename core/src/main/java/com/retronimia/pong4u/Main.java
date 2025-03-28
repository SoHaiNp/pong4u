package com.retronimia.pong4u;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Main extends ApplicationAdapter {

    private SpriteBatch batch;
    private Texture background;
    private OrthographicCamera camera;
    private FitViewport viewport;

    private final float VIRTUAL_WIDTH = 800;
    private final float VIRTUAL_HEIGHT = 450;

    private Paddle paddlePlayer;
    private Paddle paddleComputer;

    private Texture paddlePlayerTexture;
    private Texture paddleComputerTexture;

    private final float paddleWidth = 35;
    private final float paddleHeight = 124;
    private final float velocity = 320;

    private Ball ball;
    private Texture ballTexture;
    private final float ballSize = 30;

    private DebugRenderer debugRenderer;
    private boolean debugEnabled = false;

    private ScoreRenderer scoreRenderer;
    private int score;

    @Override
    public void create() {
        batch = new SpriteBatch();
        scoreRenderer = new ScoreRenderer();

        background = new Texture("background_grid.png");

        paddlePlayerTexture = new Texture("paddle_1.png");
        paddleComputerTexture = new Texture("paddle_2.png");

        ballTexture = new Texture("ball.png");

        camera = new OrthographicCamera();
        viewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);
        viewport.apply();
        camera.position.set(VIRTUAL_WIDTH / 2, VIRTUAL_HEIGHT / 2, 0);

        paddlePlayer = new Paddle(
            VIRTUAL_WIDTH - 125,
            (VIRTUAL_HEIGHT - paddleHeight) / 2,
            paddleWidth, paddleHeight,
            paddlePlayerTexture,
            true);

        paddleComputer = new Paddle(
            90,
            (VIRTUAL_HEIGHT - paddleHeight) / 2,
            paddleWidth, paddleHeight,
            paddleComputerTexture,
            false);

        ball = new Ball(
            VIRTUAL_WIDTH / 2 - ballSize / 2,
            VIRTUAL_HEIGHT / 2 - ballSize / 2,
            ballSize, ballSize,
            ballTexture,
            350);

        debugRenderer = new DebugRenderer();
        //Gdx.app.setLogLevel(Application.LOG_DEBUG);
        score = 0; // valor inicial de teste
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        if (Gdx.input.isKeyJustPressed(Input.Keys.G)) {
            debugEnabled = !debugEnabled;
        }

        // Atualiza a raquete do jogador via input
        paddlePlayer.update(delta, velocity, VIRTUAL_HEIGHT);
        // Atualiza a raquete do computador via IA
        paddleComputer.updateAI(delta, ball, VIRTUAL_HEIGHT);

        // Atualiza a bola
        ball.update(delta, paddlePlayer, paddleComputer, VIRTUAL_WIDTH, VIRTUAL_HEIGHT);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();  // Início
        batch.draw(background, 0, 0, VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        paddlePlayer.draw(batch);
        paddleComputer.draw(batch);
        ball.draw(batch);
        scoreRenderer.renderScore(batch, score, 50, 400);
        batch.end();    // Fim

        if (debugEnabled) {
            debugRenderer.render(camera, ball, paddlePlayer, paddleComputer);
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        batch.dispose();
        background.dispose();
        paddlePlayerTexture.dispose();
        paddleComputerTexture.dispose();
        ballTexture.dispose();
        debugRenderer.dispose();
        scoreRenderer.dispose();
    }
}

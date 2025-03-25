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

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {

    private SpriteBatch batch;
    private Texture background;
    private OrthographicCamera camera;
    private FitViewport viewport;

    // Definindo uma resolução virtual com aspecto 16:9 (ex: 800x450)
    private final float VIRTUAL_WIDTH = 800;
    private final float VIRTUAL_HEIGHT = 450;

    // Instâncias das raquetes
    private Paddle paddlePlayer;
    private Paddle paddleComputer;

    // Assets das raquetes
    private Texture paddlePlayerTexture;
    private Texture paddleComputerTexture;

    // Dimensões e velocidade das raquetes
    private final float paddleWidth = 35;
    private final float paddleHeight = 124;
    private final float velocity = 320;

    // Declare a bola
    private Ball ball;
    private Texture ballTexture;
    private final float ballSize = 30;

    // Debug renderer
    private DebugRenderer debugRenderer;
    private boolean debugEnabled = false; // Flag para exibir ou não os gizmos

    @Override
    public void create() {
        batch = new SpriteBatch();

        // Carregando a asset do mapa diretamente
        background = new Texture("background_grid.png");

        // Carrega os assets das raquetes
        paddlePlayerTexture = new Texture("paddle_1.png");
        paddleComputerTexture = new Texture("paddle_2.png");

        // Carregando a asset da bola
        ballTexture = new Texture("ball.png");

        // Configurando a câmera e o viewport para manter a proporção
        camera = new OrthographicCamera();
        viewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);
        viewport.apply();
        camera.position.set(VIRTUAL_WIDTH / 2, VIRTUAL_HEIGHT / 2, 0);

        paddlePlayer = new Paddle(
        VIRTUAL_WIDTH - 125, // Posição próxima à borda direita
            (VIRTUAL_HEIGHT - paddleHeight) / 2, // Centralizada verticalmente
            paddleWidth, paddleHeight,
            paddlePlayerTexture,
            true);

        paddleComputer = new Paddle(
            90, // Posição próxima à borda esquerda
            (VIRTUAL_HEIGHT - paddleHeight) / 2,
            paddleWidth, paddleHeight,
            paddleComputerTexture,
            false); // Controlada pelo computador (por enquanto estática)

        ball = new Ball(
            VIRTUAL_WIDTH / 2 - ballSize / 2,
            VIRTUAL_HEIGHT / 2 - ballSize / 2,
            ballSize, ballSize,
            ballTexture,
            350); // ou 500, conforme desejado

        debugRenderer = new DebugRenderer();
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        // Verifica se a tecla G foi pressionada para alternar os gizmos
        if (Gdx.input.isKeyJustPressed(Input.Keys.G)) {
            debugEnabled = !debugEnabled;
        }

        // Atualiza a raquete do jogador com base no input; a do computador fica sem movimento
        paddlePlayer.update(delta, velocity, VIRTUAL_HEIGHT);
        // Em uma próxima etapa, podemos implementar a lógica para a raquete do computador

        // Atualiza a bola
        ball.update(delta, paddlePlayer, paddleComputer, VIRTUAL_WIDTH, VIRTUAL_HEIGHT);

        // Limpa a tela
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Atualiza a câmera e define a matriz de projeção do SpriteBatch
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        // Desenha a imagem de fundo ocupando toda a resolução virtual
        // Como o viewport já garante o aspecto 16:9, a imagem não será distorcida
        batch.draw(background, 0, 0, VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        paddlePlayer.draw(batch);
        paddleComputer.draw(batch);
        ball.draw(batch);
        batch.end();

        // Se o debug estiver ativado, renderiza os gizmos
        if (debugEnabled) {
            debugRenderer.render(camera, ball, paddlePlayer, paddleComputer);
        }
    }

    @Override
    public void resize(int width, int height) {
        // Atualiza o viewport sempre que houver mudança no tamanho da tela
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
    }
}

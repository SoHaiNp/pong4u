package com.retronimia.pong4u;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ScoreRenderer {
    private BitmapFont font;

    public ScoreRenderer() {
        // Carrega o arquivo FNT, que internamente vai carregar o PNG
        font = new BitmapFont(Gdx.files.internal("micro5.fnt"));
    }

    public void renderScore(SpriteBatch batch, int score, float x, float y) {
        font.draw(batch, "Score: " + score, x, y);
    }

    public void dispose() {
        font.dispose();
    }
}


package com.palevskinikola.coronaviruskiller.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class GameOverScreen implements Screen {

    CoronavirusKiller game;
    int score, highscore;

    Texture gameOver;
    BitmapFont scoreFont;

    public GameOverScreen(CoronavirusKiller game, int score){
        this.game = game;
        this.score = score;
        //GEt highscore
        Preferences preferences = Gdx.app.getPreferences("coronaviruskiller");
        this.highscore = preferences.getInteger("highscore",0);

        //check for highscore
        if(score > highscore){
            preferences.putInteger("highscore",score);
            preferences.flush();
        }

        //Load texture and font

        gameOver = new Texture("gameover.png");
        scoreFont = new BitmapFont(Gdx.files.internal("fonts.score.fnt"));


    }

    @Override
    public void show( ) {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();

        game.batch.draw(gameOver, Gdx.graphics.getWidth()/2 - gameOver.getWidth()/2, Gdx.graphics.getHeight()- gameOver.getHeight() - 15);

        game.batch.end();

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}

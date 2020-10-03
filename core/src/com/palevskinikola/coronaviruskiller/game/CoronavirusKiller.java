package com.palevskinikola.coronaviruskiller.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;


import java.util.ArrayList;
import java.util.Random;

public class CoronavirusKiller extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[] man;
	int manState = 0;
	int pause = 0;
	float gravity = 0.9f;
	float velocity = 0;
	int manY = 0;
	Rectangle manRectangle;

	BitmapFont font;
	Texture dizzy;

	int score = 0;
	int gameState = 0;

	ArrayList<Integer> sanitizerXs = new ArrayList<Integer>();
	ArrayList<Integer> sanitizerYs = new ArrayList<Integer>();
	ArrayList<Rectangle> sanitizerRectangles = new ArrayList<Rectangle>();
	Texture sanitizer;
	int sanitizerCount;
	Random random;
	ArrayList<Integer> virusXs = new ArrayList<Integer>();
	ArrayList<Integer> virusYs = new ArrayList<Integer>();
	ArrayList<Rectangle> virusRectangles = new ArrayList<Rectangle>();
	Texture virus;
	int virusCount;

    ArrayList<Integer> maskXs = new ArrayList<Integer>();
    ArrayList<Integer> maskYs = new ArrayList<Integer>();
    ArrayList<Rectangle> maskRectangles = new ArrayList<Rectangle>();
    Texture mask;
    int maskCount;


	Texture highScore;
	Texture gameOver;
	Texture playAgain;
	Texture tapStart;
	Texture getReady;
	Texture coronaVirusStart;
	Texture currScore;

	Sound coinSound;
	Sound gameOverSound;
	Music gameMusic;

	Stage stage;
	Texture myTexture;
	TextureRegion myTextureRegion;
	TextureRegionDrawable myTexRegionDrawable;
	ImageButton button;


	Preferences preferences;

	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		man = new Texture[4];
		man[0] = new Texture("prva.png");
		man[1] = new Texture("vtora.png");
		man[2] = new Texture("treta.png");
		man[3] = new Texture("vtora.png");

		manY = 0;

		sanitizer = new Texture("sanitizer.png");
		virus = new Texture("virus.png");
		mask = new Texture("facemask.png");
		random = new Random();

		dizzy = new Texture("prva.png");

		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);

		highScore = new Texture("high-score.png");
		gameOver = new Texture("gameover.png");
		currScore = new Texture("curr-score.png");
		playAgain = new Texture("playagain.png");
		tapStart = new Texture("taptostart.png");
		getReady = new Texture("getreadystart.png");
		coronaVirusStart = new Texture("coronarun.png");
		coinSound = Gdx.audio.newSound(Gdx.files.internal("coin-pickup-sound.wav"));
		gameOverSound = Gdx.audio.newSound(Gdx.files.internal("game-over-sound.wav"));
		gameMusic = Gdx.audio.newMusic(Gdx.files.internal("game-music.wav"));

		preferences = Gdx.app.getPreferences("game preferences");


		myTexture = new Texture("music.png");
		myTextureRegion = new TextureRegion(myTexture);
		myTexRegionDrawable = new TextureRegionDrawable(myTextureRegion);
		button = new ImageButton(myTexRegionDrawable);

		stage = new Stage(new ScreenViewport());
		stage.addActor(button);
		Gdx.input.setInputProcessor(stage);


	}

	public void makeBomb(){
		float height  = random.nextFloat() * Gdx.graphics.getHeight();
		virusYs.add((int)height);
		virusXs.add(Gdx.graphics.getWidth());
	}

	public void makeCoin(){
		float height  = random.nextFloat() * (Gdx.graphics.getHeight()-340);
		sanitizerYs.add((int)height);
		sanitizerXs.add(Gdx.graphics.getWidth());
	}

	public void makeMask(){
		float height  = random.nextFloat() * (Gdx.graphics.getHeight()-340);
		maskYs.add((int)height);
		maskXs.add(Gdx.graphics.getWidth());
	}

	@Override
	public void render () {
		batch.begin();
		batch.draw(background,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

		if(gameState == 0){
			//START SCREEN
		    batch.draw(coronaVirusStart, Gdx.graphics.getWidth()/2-coronaVirusStart.getWidth()/2, Gdx.graphics.getHeight()-coronaVirusStart.getHeight()-200);
			batch.draw(getReady, Gdx.graphics.getWidth()/2-getReady.getWidth()/2+20,Gdx.graphics.getHeight()/20);
			batch.draw(man[0],Gdx.graphics.getWidth()/2-man[0].getWidth()/2,Gdx.graphics.getHeight()/20+tapStart.getHeight()+man[0].getHeight()-55);
			batch.draw(tapStart,Gdx.graphics.getWidth()/2-tapStart.getWidth()/2+30,Gdx.graphics.getHeight()/20+tapStart.getHeight()+40);



		}



		if(gameState == 1){


			//GAME IS LIVE
			gameMusic.play();
			gameMusic.setVolume(0.2f);
			font.getData().setScale(10);
			//man

			batch.draw(man[manState], 100, manY);

			//SCORE
			if(score<10){
				font.draw(batch, String.valueOf(score),Gdx.graphics.getWidth()-(Gdx.graphics.getWidth()/2+50),Gdx.graphics.getHeight()-100);
			}else if(score>99){
				font.draw(batch, String.valueOf(score),Gdx.graphics.getWidth()-(Gdx.graphics.getWidth()/2+110),Gdx.graphics.getHeight()-100);
			}
			else {
				font.draw(batch, String.valueOf(score),Gdx.graphics.getWidth()-(Gdx.graphics.getWidth()/2+80),Gdx.graphics.getHeight()-100);
			}

			//BOMBS
			if(score<15){
				if(virusCount< 100){
					virusCount++;
				}else{
					virusCount = 0;
					makeBomb();
				}
				virusRectangles.clear();
				for (int i=0;i<virusXs.size();i++){
					batch.draw(virus,virusXs.get(i),virusYs.get(i));
					virusXs.set(i, virusXs.get(i) - 10);
					virusRectangles.add(new Rectangle(virusXs.get(i),virusYs.get(i),virus.getWidth(), virus.getHeight()));
				}
			}else if(score>=15 && score<30){
				if(virusCount< 100){
					virusCount++;
				}else{
					virusCount = 0;
					makeBomb();
				}
				virusRectangles.clear();
				for (int i=0;i<virusXs.size();i++){
					batch.draw(virus,virusXs.get(i),virusYs.get(i));
					virusXs.set(i, virusXs.get(i) - 12);
					virusRectangles.add(new Rectangle(virusXs.get(i),virusYs.get(i),virus.getWidth(), virus.getHeight()));
				}
			}else if(score>=30 && score<45){
				if(virusCount< 100){
					virusCount++;
				}else{
					virusCount = 0;
					makeBomb();
					makeBomb();
				}
				virusRectangles.clear();
				for (int i=0;i<virusXs.size();i++){
					batch.draw(virus,virusXs.get(i),virusYs.get(i));
					virusXs.set(i, virusXs.get(i) - 8);
					virusRectangles.add(new Rectangle(virusXs.get(i),virusYs.get(i),virus.getWidth(), virus.getHeight()));
				}
				for (int j=0;j<virusXs.size();j++){
					batch.draw(virus,virusXs.get(j),virusYs.get(j));
					virusXs.set(j, virusXs.get(j) - 8);
					virusRectangles.add(new Rectangle(virusXs.get(j),virusYs.get(j),virus.getWidth(), virus.getHeight()));
				}
			}else{
				if(virusCount< 100){
					virusCount++;
				}else{
					virusCount = 0;
					makeBomb();
					makeBomb();
					makeBomb();
				}
				virusRectangles.clear();
				for (int i=0;i<virusXs.size();i++){
					batch.draw(virus,virusXs.get(i),virusYs.get(i));
					virusXs.set(i, virusXs.get(i) - 8);
					virusRectangles.add(new Rectangle(virusXs.get(i),virusYs.get(i),virus.getWidth(), virus.getHeight()));
				}
				for (int j=0;j<virusXs.size();j = j+2){
					batch.draw(virus,virusXs.get(j),virusYs.get(j));
					virusXs.set(j, virusXs.get(j) - 8);
					virusRectangles.add(new Rectangle(virusXs.get(j),virusYs.get(j),virus.getWidth(), virus.getHeight()));
				}
				for (int z=0;z<virusXs.size();z++){
					batch.draw(virus,virusXs.get(z),virusYs.get(z));
					virusXs.set(z, virusXs.get(z) - 8);
					virusRectangles.add(new Rectangle(virusXs.get(z),virusYs.get(z),virus.getWidth(), virus.getHeight()));
				}

			}

			//SANITIZER
			if(sanitizerCount< 100){
				sanitizerCount++;
			}else{
				sanitizerCount = 0;
				makeCoin();
			}
			sanitizerRectangles.clear();
			for (int i=0;i<sanitizerXs.size();i++){
				batch.draw(sanitizer,sanitizerXs.get(i),sanitizerYs.get(i));
				sanitizerXs.set(i, sanitizerXs.get(i) - 4);
				sanitizerRectangles.add(new Rectangle(sanitizerXs.get(i),sanitizerYs.get(i),sanitizer.getWidth(), sanitizer.getHeight()));
			}

			//FACEMASK
			if(maskCount< 100){
				maskCount++;
			}else{
				maskCount = 0;
				makeMask();
			}
			maskRectangles.clear();
			for (int i=0;i<maskXs.size();i++){
				batch.draw(mask,maskXs.get(i),maskYs.get(i));
				maskXs.set(i, maskXs.get(i) - 8);
				maskRectangles.add(new Rectangle(maskXs.get(i),maskYs.get(i),mask.getWidth(), mask.getHeight()));
			}

			if(Gdx.input.justTouched()){
				velocity = -26;
			}

			if(pause < 8){
				pause++;
			}else{
				pause = 0;
				if(manState < 3){
					manState++;
				}else {
					manState=0;
				}
			}

			velocity += gravity;
			manY -= velocity;

			if(manY <= 0){
				manY = 0;
			}

		}else if(gameState == 0){
			//WAITING TO START
			if(Gdx.input.justTouched()){
				gameState = 1;
			}
		}else if(gameState == 2){
			//GAME OVER

			gameMusic.stop();

			if(Gdx.input.justTouched()){
				gameState = 1;
				manY = 0;
				score = 0;
				velocity = 0;
				sanitizerXs.clear();
				sanitizerYs.clear();
				sanitizerRectangles.clear();
				sanitizerCount = 0;
				virusXs.clear();
				virusYs.clear();
				virusRectangles.clear();
				virusCount = 0;
				maskXs.clear();
				maskYs.clear();
				maskRectangles.clear();
				maskCount = 0;


			}
			else {

				font.getData().setScale(7);

				batch.draw(gameOver, Gdx.graphics.getWidth()/2 - gameOver.getWidth()/2, Gdx.graphics.getHeight()-500);
				batch.draw(currScore,20,Gdx.graphics.getHeight()/2+150);
				batch.draw(playAgain,Gdx.graphics.getWidth()/2 - playAgain.getWidth()/2,130+playAgain.getHeight());
				batch.draw(highScore,20,Gdx.graphics.getHeight()/2-100);
				//SCORE


					int hScore = preferences.getInteger("High score",0);
					if(hScore>=score)
					{
						// display highscore

						if(hScore<10){
							font.draw(batch, String.valueOf(hScore),Gdx.graphics.getWidth()-150,Gdx.graphics.getHeight()/2+20);
						}else if(hScore>99){
							font.draw(batch, String.valueOf(hScore),Gdx.graphics.getWidth()-270,Gdx.graphics.getHeight()/2+20);
						}
						else {
							font.draw(batch, String.valueOf(hScore),Gdx.graphics.getWidth()-200,Gdx.graphics.getHeight()/2+20);
						}


					}
					else
					{
						// display CurrentScore

						if(score<10){
							font.draw(batch, String.valueOf(score),Gdx.graphics.getWidth()-150,Gdx.graphics.getHeight()/2+270);
							//+50 bese
						}else if(score>99){
							font.draw(batch, String.valueOf(score),Gdx.graphics.getWidth()-270,Gdx.graphics.getHeight()/2+270);
						}
						else {
							font.draw(batch, String.valueOf(score),Gdx.graphics.getWidth()-200,Gdx.graphics.getHeight()/2+270);
						}

						preferences.putInteger("High score", score);
						preferences.flush();
					}

				if(score<10){
					font.draw(batch, String.valueOf(score),Gdx.graphics.getWidth()-150,Gdx.graphics.getHeight()/2+270);
				}else if(score>99){
					font.draw(batch, String.valueOf(score),Gdx.graphics.getWidth()-270,Gdx.graphics.getHeight()/2+270);
				}
				else {
					font.draw(batch, String.valueOf(score),Gdx.graphics.getWidth()-200,Gdx.graphics.getHeight()/2+270);
				}


			}

		}



		 manRectangle = new Rectangle(100,manY,man[manState].getWidth()-40, man[manState].getHeight()-40);

		for(int i =0;i<sanitizerRectangles.size();i++){
			if(Intersector.overlaps(manRectangle,sanitizerRectangles.get(i))){
				score++;
				coinSound.play();
				coinSound.setVolume(0,2f);

				sanitizerRectangles.remove(i);
				sanitizerXs.remove(i);
				sanitizerYs.remove(i);
				break;
			}
		}

		for(int i =0;i<maskRectangles.size();i++){
			if(Intersector.overlaps(manRectangle,maskRectangles.get(i))){
				score=score+2;
				coinSound.play();
				coinSound.setVolume(0,2f);

				maskRectangles.remove(i);
				maskXs.remove(i);
				maskYs.remove(i);
				break;
			}
		}

		for(int i =0;i<virusRectangles.size();i++){
			if(Intersector.overlaps(manRectangle,virusRectangles.get(i))){
				Gdx.app.log("Bomb!","Collision");
				gameState = 2;
			}
		}



		batch.end();
	}



	@Override
	public void dispose () {
		batch.dispose();
	}
}

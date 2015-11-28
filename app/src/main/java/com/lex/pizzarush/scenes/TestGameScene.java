package com.lex.pizzarush.scenes;

import android.hardware.SensorManager;

import com.badlogic.gdx.math.Vector2;
import com.lex.gamelib.scenes.BaseScene;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.scene.background.AutoParallaxBackground;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.background.ParallaxBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITextureRegion;

/**
 * Created by Oleksiy on 11/28/2015.
 */
public class TestGameScene extends BaseScene {

    private PhysicsWorld world;
    private AutoParallaxBackground autoParallaxBackground;


    @Override
    public void createScene() {
        world = new PhysicsWorld(new Vector2(0, -SensorManager.GRAVITY_EARTH), false);
        //world.setContactListener(createContactListener());//no collision now
        registerUpdateHandler(new IUpdateHandler() {
            @Override
            public void onUpdate(float pSecondsElapsed) {
                world.onUpdate(pSecondsElapsed);
            }

            @Override
            public void reset() {

            }
        });
        float width = camera.getWidth();
        float height = camera.getHeight();
        setBackground(new Background(0.09804f, 0.0f, 0.8784f));
        autoParallaxBackground = new AutoParallaxBackground(0, 0, 0, 10);
        ITextureRegion bgBack = resourceManager.getTextureRegion("background_back.png");
        ITextureRegion bgFront = resourceManager.getTextureRegion("background_front.png");

        float scaledHeight = bgFront.getHeight() * width / bgFront.getWidth();
        Sprite sBgFront = new Sprite(width / 2, scaledHeight / 2, width, scaledHeight, bgFront, vertexBufferObjectManager);
        Sprite sBgBack = new Sprite(width / 2, height / 2, width, height, bgBack, vertexBufferObjectManager);


        ParallaxBackground.ParallaxEntity pBack = new ParallaxBackground.ParallaxEntity(-5.0f, sBgBack);
        autoParallaxBackground.attachParallaxEntity(pBack);
        ParallaxBackground.ParallaxEntity pFront = new ParallaxBackground.ParallaxEntity(-10.0f, sBgFront);
        autoParallaxBackground.attachParallaxEntity(pFront);
        setBackground(autoParallaxBackground);
    }

    @Override
    public void onBackKeyPressed() {

    }


    @Override
    public void resetScene() {

    }

    @Override
    public void disposeScene() {

    }
}

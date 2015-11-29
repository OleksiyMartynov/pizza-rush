package com.lex.pizzarush.scenes;

import android.os.Handler;
import android.os.Looper;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.lex.gamelib.manager.ResourceManager;
import com.lex.gamelib.objects.WorldEntity;
import com.lex.gamelib.scenes.BaseScene;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.scene.background.AutoParallaxBackground;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.background.ParallaxBackground;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oleksiy on 11/28/2015.
 */
public class TestGameScene extends BaseScene {

    private AutoParallaxBackground autoParallaxBackground;
    private WorldEntity player;
    private List<WorldEntity> entitiesToRemove = new ArrayList<>();
    private List<WorldEntity> entitiesToAdd = new ArrayList<>();
    @Override
    public void createScene() {
        world.setContactListener(createContactListener());
        registerUpdateHandler(createUpdateHandler());

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

        AnimatedSprite characterSprite = ResourceManager.getInstance().getAnimatedSprite("character_walk_r.png");
        characterSprite.setPosition(getCameraWidth() / 4, getCameraHeight() / 2);
        player = new WorldEntity("player", this, characterSprite, BodyDef.BodyType.DynamicBody, WorldEntity.BodyShape.rectangle, 0.0f, 0.0f, 0.0f);
        player.present();

        player.getSprite().registerUpdateHandler(new IUpdateHandler() {
            @Override
            public void onUpdate(float pSecondsElapsed) {
                player.getBody().setTransform(Vector2Pool.obtain(getCameraWidth() / 4 / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, player.getBody().getPosition().y), player.getBody().getAngle());
            }

            @Override
            public void reset() {

            }
        });

        Sprite groundSprite = ResourceManager.getInstance().getSprite("background_front.png");
        groundSprite.setZIndex(0);
        groundSprite.setAlpha(0);
        groundSprite.setPosition(getCameraWidth() / 2, scaledHeight / 2 - 15);
        groundSprite.setSize(getCameraWidth(), scaledHeight);
        WorldEntity ground = new WorldEntity("ground", this, groundSprite, BodyDef.BodyType.StaticBody, WorldEntity.BodyShape.rectangle, 0.5f, 0, 1);
        ground.present();


        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            public void run() {
                addEnemy();
                handler.postDelayed(this, (long) (Math.random() * 5000));
            }
        }, 500);
    }

    private IUpdateHandler createUpdateHandler() {
        return new IUpdateHandler() {
            @Override
            public void onUpdate(float pSecondsElapsed) {
                if (entitiesToRemove.size() > 0) {
                    for (WorldEntity entity : entitiesToRemove) {
                        entity.destroySelf();
                    }
                    entitiesToRemove.clear();
                }
                if (entitiesToAdd.size() > 0) {
                    for (WorldEntity entity : entitiesToAdd) {
                        entity.present();
                    }
                    entitiesToAdd.clear();
                }
            }

            @Override
            public void reset() {

            }
        };
    }

    private ContactListener createContactListener() {
        return new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                final Fixture x1 = contact.getFixtureA();
                final Fixture x2 = contact.getFixtureB();
                WorldEntity eX1 = (WorldEntity) x1.getBody().getUserData();
                WorldEntity eX2 = (WorldEntity) x2.getBody().getUserData();
                if (eX1.getSprite().getUserData().equals("enemy") && eX2.getSprite().getUserData().equals("ball")) {

                    entitiesToRemove.add(eX1);
                    entitiesToRemove.add(eX2);
                }
            }

            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        };
    }

    private void addBall(float x, float y) {
        final Sprite ballSprite = ResourceManager.getInstance().getSprite("cloud_ball.png");
        ballSprite.setPosition(x, y);
        final WorldEntity ball = new WorldEntity("ball", this, ballSprite, BodyDef.BodyType.DynamicBody, WorldEntity.BodyShape.circle, 0.9f, 0.9f, 0.2f);
        ball.present();
        ball.getBody().setLinearVelocity(5, -5);
    }

    private void addEnemy() {
        AnimatedSprite enemySprite = ResourceManager.getInstance().getAnimatedSprite("bug_walk_l.png");
        enemySprite.setPosition(getCameraWidth(), getCameraHeight() / 4);
        WorldEntity enemy = new WorldEntity("enemy", this, enemySprite, BodyDef.BodyType.DynamicBody, WorldEntity.BodyShape.rectangle, 0.0f, 0.6f, 0.0f);
        enemy.present();
        enemy.getBody().setLinearVelocity(-2, (float) Math.random() * 10);
    }

    private WorldEntity initSmoke(float x, float y) {
        AnimatedSprite smokeSprite = ResourceManager.getInstance().getAnimatedSprite("smoke.png");
        smokeSprite.setPosition(x, y);
        WorldEntity smoke = new WorldEntity("smoke", this, smokeSprite, BodyDef.BodyType.StaticBody, WorldEntity.BodyShape.rectangle, 0.0f, 0.6f, 0.0f);
        return smoke;
    }

    @Override
    public boolean onSceneTouchEvent(TouchEvent pSceneTouchEvent) {
        if (world != null && pSceneTouchEvent.isActionDown()) {
            if (pSceneTouchEvent.getX() < getCameraWidth() / 2) {//left half
                player.getBody().setLinearVelocity(0, 8.5f);
            } else { //right half
                Vector2 posi = Vector2Pool.obtain(player.getSprite().getX(), player.getSprite().getY());
                addBall(15 + posi.x + player.getSprite().getWidth() / 2, posi.y + player.getSprite().getHeight() / 2);
            }
            return true;
        }
        return false;
    }

    @Override
    public void onBackKeyPressed() {
        activity.onBackPressed();
    }


    @Override
    public void resetScene() {

    }

    @Override
    public void disposeScene() {

    }

}

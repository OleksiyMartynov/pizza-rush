package com.lex.pizzarush.scenes;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.lex.gamelib.objects.WorldEntity;
import com.lex.gamelib.scenes.BaseScene;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.scene.background.AutoParallaxBackground;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.background.ParallaxBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;


/**
 * Created by Oleksiy on 11/28/2015.
 */
public class TestGameScene extends BaseScene {

    private AutoParallaxBackground autoParallaxBackground;
    private WorldEntity player;
    private long nextEnemySpawnTime;
    private boolean canJump = true;

    @Override
    public void createScene() {


        setBackground(new Background(0.09804f, 0.0f, 0.8784f));
        autoParallaxBackground = new AutoParallaxBackground(0, 0, 0, 10);
        ITextureRegion bgBack = resourceManager.getTextureRegion("background_back.png");
        ITextureRegion bgFront = resourceManager.getTextureRegion("background_front.png");

        float scaledHeight = bgFront.getHeight() * getCameraWidth() / bgFront.getWidth();
        Sprite sBgFront = new Sprite(getCameraWidth() / 2, scaledHeight / 2, getCameraWidth(), scaledHeight, bgFront, vertexBufferObjectManager);
        Sprite sBgBack = new Sprite(getCameraWidth() / 2, getCameraHeight() / 2, getCameraWidth(), getCameraHeight(), bgBack, vertexBufferObjectManager);

        ParallaxBackground.ParallaxEntity pBack = new ParallaxBackground.ParallaxEntity(-5.0f, sBgBack);
        autoParallaxBackground.attachParallaxEntity(pBack);
        ParallaxBackground.ParallaxEntity pFront = new ParallaxBackground.ParallaxEntity(-10.0f, sBgFront);
        autoParallaxBackground.attachParallaxEntity(pFront);
        setBackground(autoParallaxBackground);

        //player
        player = new WorldEntity.WorldEntityBuilder("character_walk_r.png", this, BodyDef.BodyType.DynamicBody, "player")
                .setBodyShape(WorldEntity.BodyShape.rectangle)
                .setUpdateRotation(false)
                .setPosition(getCameraWidth() / 4, getCameraHeight() / 2)
                .build();
        player.present();

        //ground
        WorldEntity ground = new WorldEntity.WorldEntityBuilder("background_front.png", this, BodyDef.BodyType.StaticBody, "ground")
                .setBodyShape(WorldEntity.BodyShape.rectangle)
                .setPosition(getCameraWidth() / 2, scaledHeight / 2 - 40)
                .setFriction(0.1f)
                .build();
        ground.getSprite().setSize(getCameraWidth(), scaledHeight);
        ground.getSprite().setAlpha(0);
        ground.present();


        //handlers & listeners
        world.setContactListener(createContactListener());
        player.getSprite().registerUpdateHandler(getPlayerXPosCorrectionHandler());
        registerUpdateHandler(getEnemySpawnUpdateHandler());
    }




    private ContactListener createContactListener() {
        return new ContactListener() {
            @Override
            public void beginContact(Contact contact) {

                final Fixture x1 = contact.getFixtureA();
                final Fixture x2 = contact.getFixtureB();
                final WorldEntity eX1 = (WorldEntity) x1.getBody().getUserData();
                final WorldEntity eX2 = (WorldEntity) x2.getBody().getUserData();
                if (eX1 != null && eX2 != null) {
                    String str1 = (String) eX1.getSprite().getUserData();
                    String str2 = (String) eX2.getSprite().getUserData();
                    if (str1.equals("enemy") && str2.equals("ball")) {

                        removeEntity(eX1);
                        removeEntity(eX2);
                        runOnUpdate(new Runnable() {
                            @Override
                            public void run() {
                                addSmoke(eX1.getSprite().getX(), eX2.getSprite().getY());
                            }
                        });
                    }
                    if (str2.equals("player") || str1.equals("player")) {
                        canJump = true;
                    }
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
        WorldEntity ball = new WorldEntity.WorldEntityBuilder("cloud_ball.png", this, BodyDef.BodyType.DynamicBody, "ball")
                .setBodyShape(WorldEntity.BodyShape.circle)
                .setDensity(0.5f)
                .setElasticity(0.5f)
                .setFriction(1.0f)
                .setPosition(x, y)
                .setDestroyWhenOffscreen(true)
                .build();
        ball.present();
        ball.getBody().setLinearVelocity(5, -5);
    }

    private void addEnemy() {
        WorldEntity enemy = new WorldEntity.WorldEntityBuilder("bug_walk_l.png", this, BodyDef.BodyType.DynamicBody, "enemy")
                .setElasticity(0.6f)
                .setPosition(getCameraWidth(), getCameraHeight() / 4)
                .setDestroyWhenOffscreen(true)
                .build();
        enemy.present();
        enemy.getBody().setLinearVelocity(-2, (float) Math.random() * 10);
    }

    private void addSmoke(float x, float y) {
        final WorldEntity smoke = new WorldEntity.WorldEntityBuilder("smoke.png", this, BodyDef.BodyType.StaticBody, "smoke")
                .setDisableCollision(true)
                .setPosition(x, y)
                .build();
        smoke.present();
        smoke.setListener(new WorldEntity.WorldEntityEventListener() {
            @Override
            public void onEvent(WorldEntity.Event e, Object data) {
                if (e == WorldEntity.Event.animationLoopComplete) {
                    removeEntity(smoke);
                }
            }
        });
    }

    @Override
    public boolean onSceneTouchEvent(TouchEvent pSceneTouchEvent) {
        if (world != null && pSceneTouchEvent.isActionDown()) {
            if (pSceneTouchEvent.getX() < getCameraWidth() / 2) {//left half
                if (canJump) {
                    player.getBody().setLinearVelocity(0, 8.5f);
                    canJump = false;
                }
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


    public IUpdateHandler getEnemySpawnUpdateHandler() {
        return new IUpdateHandler() {
            @Override
            public void onUpdate(float pSecondsElapsed) {
                long t = System.currentTimeMillis();
                if (nextEnemySpawnTime < t) {
                    addEnemy();
                    nextEnemySpawnTime = t + (long) (Math.random() * 5000.0d);
                }
            }

            @Override
            public void reset() {

            }
        };
    }

    public IUpdateHandler getPlayerXPosCorrectionHandler() {
        return new IUpdateHandler() {
            @Override
            public void onUpdate(float pSecondsElapsed) {
                player.getBody().setTransform(Vector2Pool.obtain(getCameraWidth() / 4 / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, player.getBody().getPosition().y), player.getBody().getAngle());
            }

            @Override
            public void reset() {

            }
        };
    }
}

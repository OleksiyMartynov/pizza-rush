package com.lex.pizzarush.scenes;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.lex.gamelib.objects.WorldEntity;
import com.lex.gamelib.objects.WorldJoint;
import com.lex.gamelib.scenes.BaseScene;

import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.background.AutoParallaxBackground;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.background.ParallaxBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsFactory;
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

    private WorldEntity wheel, head, ballHandR, ballHandL;


    private boolean leftSidePressed, rightSidePressed;


    @Override
    public void createScene() {


        setBackground(new Background(0.9f, 0.9f, 0.9f));
        wheel = new WorldEntity.WorldEntityBuilder("wheel.png", this, BodyDef.BodyType.DynamicBody, "wheel")
                .setBodyShape(WorldEntity.BodyShape.circle)
                .setPosition(getCameraWidth() / 2, 50)
                .setDensity(1)
                .setElasticity(0.0f)
                .setFriction(1.0f)
                .build();


        WorldEntity butt = new WorldEntity.WorldEntityBuilder("butt.png", this, BodyDef.BodyType.DynamicBody, "butt")
                .setBodyShape(WorldEntity.BodyShape.circle)
                .setDensity(1)
                .setElasticity(0.3f)
                .setFriction(0.5f)
                .setPosition(getCameraWidth() / 2, 100)
                .setDisableCollision(true)
                .build();


        head = new WorldEntity.WorldEntityBuilder("head.png", this, BodyDef.BodyType.DynamicBody, "head")
                .setBodyShape(WorldEntity.BodyShape.circle)
                .setDensity(1)
                .setElasticity(0.3f)
                .setFriction(0.5f)
                .setPosition(getCameraWidth() / 2, 150)
                .build();


        WorldEntity ballKneeR = new WorldEntity.WorldEntityBuilder("cloud_ball.png", this, BodyDef.BodyType.DynamicBody, "knee_r")
                .setBodyShape(WorldEntity.BodyShape.rectangle)
                .setPosition(getCameraWidth() / 2 + 32, 100)
                .setDisableCollision(true)
                .build();
        ballKneeR.getSprite().setScale(0.5f);


        WorldEntity ballFootR = new WorldEntity.WorldEntityBuilder("cloud_ball.png", this, BodyDef.BodyType.DynamicBody, "knee_r")
                .setBodyShape(WorldEntity.BodyShape.rectangle)
                .setPosition(getCameraWidth() / 2 + 32, 132)
                .setDisableCollision(true)
                .build();
        ballFootR.getSprite().setScale(0.5f);

        WorldEntity ballKneeL = new WorldEntity.WorldEntityBuilder("cloud_ball.png", this, BodyDef.BodyType.DynamicBody, "knee_l")
                .setBodyShape(WorldEntity.BodyShape.rectangle)
                .setPosition(getCameraWidth() / 2 - 32, 100)
                .setDisableCollision(true)
                .build();
        ballKneeL.getSprite().setScale(0.5f);


        WorldEntity ballFootL = new WorldEntity.WorldEntityBuilder("cloud_ball.png", this, BodyDef.BodyType.DynamicBody, "knee_l")
                .setBodyShape(WorldEntity.BodyShape.rectangle)
                .setPosition(getCameraWidth() / 2 - 32, 132)
                .setDisableCollision(true)
                .build();
        ballFootL.getSprite().setScale(0.5f);

        WorldEntity ballElbowR = new WorldEntity.WorldEntityBuilder("cloud_ball.png", this, BodyDef.BodyType.DynamicBody, "elbow_r")
                .setBodyShape(WorldEntity.BodyShape.rectangle)
                .setPosition(getCameraWidth() / 2 + 32, 142)
                .setDisableCollision(true)
                .build();
        ballElbowR.getSprite().setScale(0.5f);

        WorldEntity ballElbowL = new WorldEntity.WorldEntityBuilder("cloud_ball.png", this, BodyDef.BodyType.DynamicBody, "elbow_L")
                .setBodyShape(WorldEntity.BodyShape.rectangle)
                .setPosition(getCameraWidth() / 2 - 32, 142)
                .setDisableCollision(true)
                .build();
        ballElbowL.getSprite().setScale(0.5f);

        ballHandR = new WorldEntity.WorldEntityBuilder("cloud_ball.png", this, BodyDef.BodyType.DynamicBody, "hand_r")
                .setBodyShape(WorldEntity.BodyShape.rectangle)
                .setPosition(getCameraWidth() / 2 + 35, 112)
                .setDisableCollision(true)
                .build();
        ballHandR.getSprite().setScale(0.5f);

        ballHandL = new WorldEntity.WorldEntityBuilder("cloud_ball.png", this, BodyDef.BodyType.DynamicBody, "hand_l")
                .setBodyShape(WorldEntity.BodyShape.rectangle)
                .setPosition(getCameraWidth() / 2 - 35, 112)
                .setDisableCollision(true)
                .build();
        ballHandL.getSprite().setScale(0.5f);


        WorldJoint seat = new WorldJoint(this, world, wheel, butt, "post.png", 0);
        WorldJoint torso = new WorldJoint(this, world, head, butt, "torso.png", 0);

        WorldJoint thighR = new WorldJoint(this, world, butt, ballKneeR, "thigh.png", 0);
        WorldJoint calfR = new WorldJoint(this, world, ballKneeR, ballFootR, "calf.png", 0);

        WorldJoint thighL = new WorldJoint(this, world, butt, ballKneeL, "thigh.png", 0);
        WorldJoint calfL = new WorldJoint(this, world, ballKneeL, ballFootL, "calf.png", 0);

        WorldJoint armR = new WorldJoint(this, world, head, ballElbowR, "arm.png", 0);
        WorldJoint armL = new WorldJoint(this, world, head, ballElbowL, "arm.png", 0);

        WorldJoint foreArmR = new WorldJoint(this, world, ballElbowR, ballHandR, "forearm.png", 0);
        WorldJoint foreArmL = new WorldJoint(this, world, ballElbowL, ballHandL, "forearm.png", 0);


        //left leg
        thighL.present();
        ballFootL.present();
        calfL.present();
        ballKneeL.present();

        //torso + bike
        wheel.present();
        seat.present();
        butt.present();
        torso.present();

        //right leg
        thighR.present();
        ballFootR.present();
        calfR.present();
        ballKneeR.present();


        //right arm
        armR.present();
        ballElbowR.present();
        foreArmR.present();
        ballHandR.present();

        //left arm
        armL.present();
        ballElbowL.present();
        foreArmL.present();
        ballHandL.present();

        head.present();
        createWorldBox();

        registerUpdateHandler(createControlsUpdater());
    }

    private IUpdateHandler createControlsUpdater() {
        return new IUpdateHandler() {
            @Override
            public void onUpdate(float pSecondsElapsed) {
                head.getBody().applyForce(Vector2Pool.obtain(0, 50), head.getBody().getWorldCenter());
                ballHandL.getBody().applyForce(Vector2Pool.obtain(-18, 25), head.getBody().getWorldCenter());
                ballHandR.getBody().applyForce(Vector2Pool.obtain(18, 25), head.getBody().getWorldCenter());
                if (leftSidePressed) {
                    wheel.getBody().applyTorque(50);
                } else if (rightSidePressed) {
                    wheel.getBody().applyTorque(-50);
                }
            }

            @Override
            public void reset() {

            }
        };
    }


    private void createWorldBox() {
        final Rectangle ground = new Rectangle(getCameraWidth() / 2, getCameraHeight() - 2, getCameraWidth(), 2, vertexBufferObjectManager);
        final Rectangle roof = new Rectangle(getCameraWidth() / 2, 0, getCameraWidth(), 2, vertexBufferObjectManager);
        final Rectangle left = new Rectangle(0, getCameraHeight() / 2, 2, getCameraHeight(), vertexBufferObjectManager);
        final Rectangle right = new Rectangle(getCameraWidth() - 2, getCameraHeight() / 2, 2, getCameraHeight(), vertexBufferObjectManager);

        final FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(0, 0.5f, 0.5f);
        PhysicsFactory.createBoxBody(world, ground, BodyDef.BodyType.StaticBody, wallFixtureDef);
        PhysicsFactory.createBoxBody(world, roof, BodyDef.BodyType.StaticBody, wallFixtureDef);
        PhysicsFactory.createBoxBody(world, left, BodyDef.BodyType.StaticBody, wallFixtureDef);
        PhysicsFactory.createBoxBody(world, right, BodyDef.BodyType.StaticBody, wallFixtureDef);

        attachChild(ground);
        attachChild(roof);
        attachChild(left);
        attachChild(right);
    }

    private void test() {
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

        //join test
        addJointTest(getCameraWidth() / 2, getCameraHeight() / 2);

        //bound camera test
        BoundCamera bc = (BoundCamera) camera;
        //bc.setChaseEntity(player.getSprite());

        //physics loader test

        //handlers & listeners
        world.setContactListener(createTestContactListener());
        player.getSprite().registerUpdateHandler(getPlayerXPosCorrectionHandler());
        //registerUpdateHandler(getEnemySpawnUpdateHandler());
    }

    private ContactListener createTestContactListener() {
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
                .setDensity(1)
                .setElasticity(0.5f)
                .setFriction(0.5f)
                .setPosition(x, y)
                .setDestroyWhenOffscreen(true)
                .build();
        ball.present();
        ball.getBody().setLinearVelocity(5, -5);
    }

    private void addEnemy() {
        WorldEntity enemy = new WorldEntity.WorldEntityBuilder("bug_walk_l.png", this, BodyDef.BodyType.DynamicBody, "enemy")
                .setDensity(0.5f)
                .setFriction(0.03f)
                .setElasticity(0.5f)
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

    private void addJointTest(float x, float y) {
        WorldEntity ball = new WorldEntity.WorldEntityBuilder("cloud_ball.png", this, BodyDef.BodyType.DynamicBody, "ball")
                .setBodyShape(WorldEntity.BodyShape.circle)
                .setDensity(1)
                .setElasticity(0.3f)
                .setFriction(0.5f)
                .setPosition(x, y)
                .build();


        WorldEntity ball2 = new WorldEntity.WorldEntityBuilder("cloud_ball.png", this, BodyDef.BodyType.DynamicBody, "ball")
                .setBodyShape(WorldEntity.BodyShape.circle)
                .setDensity(1)
                .setElasticity(0.7f)
                .setFriction(0.5f)
                .setPosition(x + 50, y)
                .build();

        WorldEntity ball3 = new WorldEntity.WorldEntityBuilder("cloud_ball.png", this, BodyDef.BodyType.DynamicBody, "ball")
                .setBodyShape(WorldEntity.BodyShape.circle)
                .setDensity(1)
                .setElasticity(0.4f)
                .setFriction(0.5f)
                .setPosition(x + 100, y)
                .build();

        float anchorFaceX = ball2.getSprite().getX();
        float anchorFaceY = ball2.getSprite().getY();
        float spriteWidth = ball2.getSprite().getWidth();
        float spriteHeight = ball2.getSprite().getHeight();

        Line line = new Line(anchorFaceX + spriteWidth / 2, anchorFaceY + spriteHeight / 2, anchorFaceX + spriteWidth / 2, anchorFaceY + spriteHeight / 2, vertexBufferObjectManager);
        line.setLineWidth(2);
        line.setColor(1, 1, 0);
        WorldJoint joint = new WorldJoint(this, world, ball, ball2, "background_back.png", 0);
        WorldJoint joint2 = new WorldJoint(this, world, ball2, ball3, line);
        ball.present();
        ball2.present();
        ball3.present();
        joint.present();
        joint2.present();
    }

    @Override
    public boolean onSceneTouchEvent(TouchEvent pSceneTouchEvent) {
        if (world != null && pSceneTouchEvent.isActionDown()) {
            if (pSceneTouchEvent.getX() < getCameraWidth() / 2) {//left half
                leftSidePressed = true;
                if (canJump) {
//                    player.getBody().setLinearVelocity(0, 8.5f);
//                    canJump = false;

                }
            } else { //right half
//                Vector2 posi = Vector2Pool.obtain(player.getSprite().getX(), player.getSprite().getY());
//                addBall(15 + posi.x + player.getSprite().getWidth() / 2, posi.y + player.getSprite().getHeight() / 2);
                //addBall(100,getCameraHeight()/2);
                rightSidePressed = true;
            }
            return true;
        } else if (pSceneTouchEvent.isActionUp() || pSceneTouchEvent.isActionCancel() || pSceneTouchEvent.isActionOutside()) {
            rightSidePressed = false;
            leftSidePressed = false;
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

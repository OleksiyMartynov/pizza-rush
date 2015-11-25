package com.lex.pizzarush;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.ui.IGameInterface;
import org.andengine.ui.activity.LayoutGameActivity;

import java.io.IOException;

public class MainActivity extends LayoutGameActivity {

    private Camera camera;
    public static final int CAMERA_WIDTH = 320;
    public static final int CAMERA_HEIGHT = 480;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected int getRenderSurfaceViewID() {
        return R.id.renderSurfaceView;
    }

    @Override
    public EngineOptions onCreateEngineOptions() {
        this.camera = new Camera(0,0, CAMERA_WIDTH, CAMERA_HEIGHT);
        EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED, new FillResolutionPolicy(), camera);
        engineOptions.getAudioOptions().setNeedsMusic(true).setNeedsSound(true);
        engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON);
        return engineOptions;
    }

    @Override
    public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) throws IOException {
        pOnCreateResourcesCallback.onCreateResourcesFinished();
    }

    @Override
    public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) throws IOException {
        Scene testScene = new Scene();
        testScene.setBackground(new Background(1,1,0));
        pOnCreateSceneCallback.onCreateSceneFinished(testScene);
    }

    @Override
    public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) throws IOException {
        pOnPopulateSceneCallback.onPopulateSceneFinished();
    }
}

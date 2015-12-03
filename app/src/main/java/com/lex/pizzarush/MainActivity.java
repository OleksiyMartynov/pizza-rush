package com.lex.pizzarush;

import com.lex.gamelib.manager.ResourceManager;
import com.lex.gamelib.manager.SceneManager;
import com.lex.pizzarush.scenes.SplashScene;
import com.lex.pizzarush.scenes.TestGameScene;

import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.LayoutGameActivity;
import org.json.JSONException;

import java.io.IOException;

public class MainActivity extends LayoutGameActivity {

    public static final int CAMERA_WIDTH = 480;
    public static final int CAMERA_HEIGHT = 320;
    private Camera camera;

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
        this.camera = new BoundCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT); //new Camera(0,0, CAMERA_WIDTH, CAMERA_HEIGHT);
        EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new FillResolutionPolicy(), camera);
        engineOptions.getAudioOptions().setNeedsMusic(true).setNeedsSound(true);
        engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON);
        return engineOptions;
    }

    @Override
    public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) throws IOException {
        try {
            ResourceManager.getInstance().init(this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        pOnCreateResourcesCallback.onCreateResourcesFinished();
    }

    @Override
    public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) throws IOException {
        SplashScene splashScene = new SplashScene();
        splashScene.setCompletionListener(new SplashScene.SplashLoadCompletionListener() {
            @Override
            public void onLoadCompletion() {
                try {
                    SceneManager.getInstance().setScene(TestGameScene.class);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }
            }
        });
        pOnCreateSceneCallback.onCreateSceneFinished(splashScene);
        try {
            if (!ResourceManager.getInstance().isLoaded()) {
                ResourceManager.getInstance().loadResources(splashScene);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) throws IOException {
        pOnPopulateSceneCallback.onPopulateSceneFinished();
    }
}

package com.lex.pizzarush.scenes;

import android.graphics.Color;

import com.lex.gamelib.manager.ResourceManager;
import com.lex.gamelib.scenes.BaseSplashScene;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.util.GLState;

/**
 * Created by Oleksiy on 11/28/2015.
 */
public class SplashScene extends BaseSplashScene {

    private BitmapTextureAtlas splashTextureAtlas;
    private TextureRegion splashTextureRegion;
    private SplashLoadCompletionListener completionListener;
    private BitmapTextureAtlas spinnerTextureAtlas;
    private TiledTextureRegion spinnerTextureRegion;

    public SplashScene() {
        super();
    }

    public void setCompletionListener(SplashLoadCompletionListener completionListener) {
        this.completionListener = completionListener;
    }

    @Override
    public void createScene() {
        setBackground(new Background(0.09804f, 0.6274f, 0.8784f));
        loadSplashResources();
        super.createScene();
    }

    public void loadSplashResources() {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath(ResourceManager.AssetFolders.Images + "/" + ResourceManager.ImageFolders.Textures + "/");
        splashTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 1024, 512);
        splashTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(splashTextureAtlas, activity, "splash_bg.png", 0, 0);
        splashTextureAtlas.load();
        backgroundSprite = new Sprite(0, 0, splashTextureRegion, vertexBufferObjectManager) {
            @Override
            protected void preDraw(GLState pGLState, Camera pCamera) {
                super.preDraw(pGLState, pCamera);
                pGLState.enableDither();
            }
        };
        float width = camera.getWidth();
        float height = camera.getHeight();
        backgroundSprite.setPosition(width / 2, height / 2);
        backgroundSprite.setSize(width, height);

        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath(ResourceManager.AssetFolders.Images + "/" + ResourceManager.ImageFolders.TiledTextures + "/");
        spinnerTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 1398, 128);
        spinnerTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(spinnerTextureAtlas, activity, "android_loading.png", 0, 0, 8, 1);
        spinnerTextureAtlas.load();
        spinner = new AnimatedSprite(width / 2, height / 2, spinnerTextureRegion, vertexBufferObjectManager);
        spinner.setZIndex(10);
        spinner.animate(200);


        FontFactory.setAssetBasePath(ResourceManager.AssetFolders.Fonts + "/");
        ITexture fontTexture2 = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR);
        textFont = FontFactory.createStrokeFromAsset(activity.getFontManager(), fontTexture2, activity.getAssets(),
                "OpenSans-Regular.ttf", 25, true, Color.WHITE, 1, Color.rgb(70, 70, 70));
        textFont.load();
    }

    @Override
    public void onComplete() {
        if (completionListener != null) {
            completionListener.onLoadCompletion();
        }
        splashTextureAtlas.unload();
        splashTextureRegion = null;
        spinnerTextureAtlas.unload();
        spinnerTextureRegion = null;
    }

    @Override
    public void onFailed() {
        //todo show failed msg
    }

    @Override
    public void resetScene() {

    }

    @Override
    public void disposeScene() {

    }

    public interface SplashLoadCompletionListener {
        void onLoadCompletion();
    }

}

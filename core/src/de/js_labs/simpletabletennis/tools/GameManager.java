package de.js_labs.simpletabletennis.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Disposable;

import de.js_labs.simpletabletennis.SimpleTableTennis;

/**
 * Created by Janik on 30.05.2016.
 */
public class GameManager implements Disposable {
    public static final String PREFERENCES_ID = "privatePref";
    public static final String LANGUAGE_PREF_ID = "language";
    public static final String REMOVE_ADS_PREF_ID = "removeAds";
    public static final String PREMIUM_PREF_ID = "premium";
    public static final String HIGHSCORE_PREF_ID = "highscore";
    public static final String COINS_PREF_ID = "coins";
    public static final String BLUE_COINS_PREF_ID = "blueCoins";

    public static final String REMOVEADS_SKU = "de.js_labs.simpletabletennis.removeads";
    public static final String PREMIUM_SKU = "de.js_labs.simpletabletennis.premium";
    public static final String PREMIUM_PLUS_SKU = "de.js_labs.simpletabletennis.premium_plus";
    public static final String PREMIUM_UPGRADE_SKU = "de.js_labs.simpletabletennis.premium_upgrade";

    public enum Premium {NONE, STANDARD, PLUS}
    public GameState CGS;

    public boolean ignoreInput;

    private Preferences prefs;
    private SimpleTableTennis game;
    public AssetManager assetManager;

    //Strings
    public static final int STRINGID_HOLD_TO_START = 0;
    public static final int STRINGID_1S = 1;
    public static final int STRINGID_HOLD_TO_RESUME = 2;
    public static final int STRINGID_SHARE_CHOOSE = 3;
    public static final int STRINGID_SHARE_TEXT = 4;
    public String[] strings;
    private String stringData;
    private FileHandle stringDataHandle;

    //Menu
    public Image menu_title_img;
    public Button menu_playgames_btn;
    public Button menu_share_btn;
    public Button menu_rate_btn;
    public Button menu_removeads_btn;
    public Button menu_language_btn;
    public Button menu_shop_btn;
    public BitmapFont font_80;
    public Image pmenu_title_img;
    public Button pmenu_remove_ads_btn;
    public Button pmenu_premium_btn;
    public Button pmenu_premium_plus_btn;

    //In-game
    public Button hud_hit_side;
    public Button hud_hit_side_down;
    public Image black_bg;
    public BitmapFont font_200;

    public Sound pinp_pong_sound;
    public Music bg_music;

    public GameManager(SimpleTableTennis game){
        this.game = game;
        assetManager = new AssetManager();

        ignoreInput = false;

        prefs = Gdx.app.getPreferences(PREFERENCES_ID);
        CGS = new GameState(prefs);
    }

    public void loadMenuAssetsAsyc(){

        assetManager.load("images/menu/shared/title_img.png", Texture.class);
        assetManager.load("images/menu/shared/shop_btn.png", Texture.class);
        assetManager.load("images/menu/shared/shop_down_btn.png", Texture.class);
        assetManager.load("images/menu/shared/playgames_btn.png", Texture.class);
        assetManager.load("images/menu/shared/playgames_down_btn.png", Texture.class);
        assetManager.load("images/menu/shared/share_btn.png", Texture.class);
        assetManager.load("images/menu/shared/share_down_btn.png", Texture.class);
        assetManager.load("images/menu/shared/rate_btn.png", Texture.class);
        assetManager.load("images/menu/shared/rate_down_btn.png", Texture.class);
        assetManager.load("images/menu/shared/premium_btn.png", Texture.class);
        assetManager.load("images/menu/shared/premium_down_btn.png", Texture.class);

        assetManager.load("fonts/font_80.fnt", BitmapFont.class);

        assetManager.load("images/menu/" + CGS.language[0] + "/language_btn.png", Texture.class);
        assetManager.load("images/menu/" + CGS.language[0] + "/language_down_btn.png", Texture.class);
        assetManager.load("images/menu/" + CGS.language[0] + "/removeads_btn.png", Texture.class);
        assetManager.load("images/menu/" + CGS.language[0] + "/removeads_down_btn.png", Texture.class);
        assetManager.load("images/menu/" + CGS.language[0] + "/pmenu_title_img.png", Texture.class);
        assetManager.load("images/menu/" + CGS.language[0] + "/pmenu_ra_btn.png", Texture.class);
        assetManager.load("images/menu/" + CGS.language[0] + "/pmenu_p_btn.png", Texture.class);
        assetManager.load("images/menu/" + CGS.language[0] + "/pmenu_pp_btn.png", Texture.class);

        assetManager.load("images/menu/" + CGS.language[1] + "/language_btn.png", Texture.class);
        assetManager.load("images/menu/" + CGS.language[1] + "/language_down_btn.png", Texture.class);
        assetManager.load("images/menu/" + CGS.language[1] + "/removeads_btn.png", Texture.class);
        assetManager.load("images/menu/" + CGS.language[1] + "/removeads_down_btn.png", Texture.class);
        assetManager.load("images/menu/" + CGS.language[1] + "/pmenu_title_img.png", Texture.class);
        assetManager.load("images/menu/" + CGS.language[1] + "/pmenu_ra_btn.png", Texture.class);
        assetManager.load("images/menu/" + CGS.language[1] + "/pmenu_p_btn.png", Texture.class);
        assetManager.load("images/menu/" + CGS.language[1] + "/pmenu_pp_btn.png", Texture.class);
    }

    public void finishLoadingMenu(){
        assetManager.finishLoading();

        menu_title_img = UiHelper.createImage(assetManager.get("images/menu/shared/title_img.png", Texture.class));
        menu_playgames_btn = UiHelper.createButton(assetManager.get("images/menu/shared/playgames_btn.png", Texture.class), assetManager.get("images/menu/shared/playgames_down_btn.png", Texture.class));
        menu_share_btn = UiHelper.createButton(assetManager.get("images/menu/shared/share_btn.png", Texture.class), assetManager.get("images/menu/shared/share_down_btn.png", Texture.class));
        menu_rate_btn = UiHelper.createButton(assetManager.get("images/menu/shared/rate_btn.png", Texture.class), assetManager.get("images/menu/shared/rate_down_btn.png", Texture.class));
        menu_shop_btn = UiHelper.createButton(assetManager.get("images/menu/shared/shop_btn.png", Texture.class), assetManager.get("images/menu/shared/shop_down_btn.png", Texture.class));
        menu_language_btn = UiHelper.createButton(assetManager.get("images/menu/" + CGS.language[CGS.languageId] + "/language_btn.png", Texture.class), assetManager.get("images/menu/" + CGS.language[CGS.languageId] + "/language_down_btn.png", Texture.class));
        menu_removeads_btn = UiHelper.createButton(assetManager.get("images/menu/" + CGS.language[CGS.languageId] + "/removeads_btn.png", Texture.class), assetManager.get("images/menu/" + CGS.language[CGS.languageId] + "/removeads_down_btn.png", Texture.class));
        font_80 = assetManager.get("fonts/font_80.fnt");
        pmenu_title_img = UiHelper.createImage(assetManager.get("images/menu/" + CGS.language[CGS.languageId] + "/pmenu_title_img.png", Texture.class));
        pmenu_remove_ads_btn = UiHelper.createButton(assetManager.get("images/menu/" + CGS.language[CGS.languageId] + "/pmenu_ra_btn.png", Texture.class));
        pmenu_premium_btn = UiHelper.createButton(assetManager.get("images/menu/" + CGS.language[CGS.languageId] + "/pmenu_p_btn.png", Texture.class));
        pmenu_premium_plus_btn = UiHelper.createButton(assetManager.get("images/menu/" + CGS.language[CGS.languageId] + "/pmenu_pp_btn.png", Texture.class));

        loadStrings();
    }

    public void loadStrings(){
        stringDataHandle = Gdx.files.internal("strings/" + CGS.language[CGS.languageId] + "/strings.txt");
        stringData = new String(stringDataHandle.readBytes());
        strings = stringData.split("\r\n");
        for(int i = 0; i < strings.length; i++){
            strings[i] = strings[i].replaceAll(":New:","\n");
            strings[i] = strings[i].replaceAll("ue","ü");
            strings[i] = strings[i].replaceAll("ae","ä");
            strings[i] = strings[i].replaceAll("oe","ö");
        }
    }

    public void loadInGameAssetsAsyc(){
        assetManager.load("images/in_game/hit_side.png", Texture.class);
        assetManager.load("images/in_game/hit_side_down.png", Texture.class);
        assetManager.load("images/in_game/black_bg.png", Texture.class);

        assetManager.load("fonts/font_200.fnt", BitmapFont.class);

        assetManager.load("sounds/pingpong.ogg", Sound.class);
        assetManager.load("sounds/Millennials-AnalogByNature.mp3", Music.class);
    }

    public void finishLoadingInGameAssets(){
        assetManager.finishLoading();

        hud_hit_side = UiHelper.createButton(assetManager.get("images/in_game/hit_side.png", Texture.class));
        hud_hit_side_down = UiHelper.createButton(assetManager.get("images/in_game/hit_side_down.png", Texture.class));
        black_bg = UiHelper.createImage(assetManager.get("images/in_game/black_bg.png", Texture.class));

        font_200 = assetManager.get("fonts/font_200.fnt");

        pinp_pong_sound = assetManager.get("sounds/pingpong.ogg", Sound.class);
        bg_music = assetManager.get("sounds/Millennials-AnalogByNature.mp3", Music.class);
    }

    public void changeLanguage(){
        if(CGS.languageId == 0){
            CGS.languageId = 1;
        }else {
            CGS.languageId = 0;
        }

        loadStrings();

        menu_language_btn = UiHelper.createButton(assetManager.get("images/menu/" + CGS.language[CGS.languageId] + "/language_btn.png", Texture.class), assetManager.get("images/menu/" + CGS.language[CGS.languageId] + "/language_down_btn.png", Texture.class));
        menu_removeads_btn = UiHelper.createButton(assetManager.get("images/menu/" + CGS.language[CGS.languageId] + "/removeads_btn.png", Texture.class), assetManager.get("images/menu/" + CGS.language[CGS.languageId] + "/removeads_down_btn.png", Texture.class));
        pmenu_title_img = UiHelper.createImage(assetManager.get("images/menu/" + CGS.language[CGS.languageId] + "/pmenu_title_img.png", Texture.class));
        pmenu_remove_ads_btn = UiHelper.createButton(assetManager.get("images/menu/" + CGS.language[CGS.languageId] + "/pmenu_ra_btn.png", Texture.class));
        pmenu_premium_btn = UiHelper.createButton(assetManager.get("images/menu/" + CGS.language[CGS.languageId] + "/pmenu_p_btn.png", Texture.class));
        pmenu_premium_plus_btn = UiHelper.createButton(assetManager.get("images/menu/" + CGS.language[CGS.languageId] + "/pmenu_pp_btn.png", Texture.class));

        prefs.putInteger(LANGUAGE_PREF_ID, CGS.languageId);
        prefs.flush();
    }

    public void setRemoveAds(boolean removeAds){
        CGS.removeAds = removeAds;
    }

    public void setPremium(Premium state){
        CGS.premium = state;
    }

    @Override
    public void dispose() {
        assetManager.dispose();
        pinp_pong_sound.dispose();
        bg_music.dispose();
    }

    public class GameState{
        public String[] language = {"en", "de"};
        public int languageId;
        public Premium premium;
        public boolean removeAds;
        public int highScore;
        public int coins;
        public int blueCoins;

        public GameState(Preferences prefs){
            languageId = prefs.getInteger(LANGUAGE_PREF_ID, 0);
            removeAds = prefs.getBoolean(REMOVE_ADS_PREF_ID, false);
            if(prefs.getString(PREMIUM_PREF_ID).equals("plus")){
                premium = Premium.PLUS;
            }else if(prefs.getString(PREMIUM_PREF_ID).equals("standard")){
                premium = Premium.STANDARD;
            }else {
                premium = Premium.NONE;
            }
            highScore = prefs.getInteger(HIGHSCORE_PREF_ID, 0);
            coins = prefs.getInteger(COINS_PREF_ID, 0);
            blueCoins = prefs.getInteger(BLUE_COINS_PREF_ID, 0);
        }

        public void add(GameResult gameResult){

        }
    }

    public class GameResult{
        private int score;
        private int coins;
        private int blueCoins;

        public GameResult(int score, int coins, int blueCoins){
            this.score = score;
            this.coins = coins;
            this.blueCoins = blueCoins;
        }
    }
}

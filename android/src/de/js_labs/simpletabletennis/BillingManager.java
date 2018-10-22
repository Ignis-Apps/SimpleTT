package de.js_labs.simpletabletennis;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;

import java.util.List;

import de.js_labs.simpletabletennis.tools.BillingHandler;

public class BillingManager implements BillingHandler, PurchasesUpdatedListener {
    public static final String BASE_64_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjuPdLBULN0wi9e/ayOz35elocRAKioFP5+5KD2W64dfLrnwdLzN+EQ9D7WndC5DE6d648DJwrlANZiAhePqRkSU9db25BgUyvIo4+s+vxSzLbRisqnkyeFeP8alIX2yDJJICbwtnZHmoNg21DtsslcqO1PJfb4pMYBvIdWFLPHxGBCFLzFTqkTLNRJRojDPrdxStg410QRmNQQZA7Z9A+30etShq4XhvMmmkovUw4/xQ3YtXq4lPJHG86utMRgrn5iqnUjUjiDqp9oRMhzPv1gxPE9yMb2svxci4Gldfo0jCeOt/cbhuNNdS9HYpiBzrKNNDQqdSkpgu4p9//Y1x6QIDAQAB";
    public static final String REMOVEADS_SKU = "de.js_labs.simpletabletennis.removeads";
    public static final String PREMIUM_SKU = "de.js_labs.simpletabletennis.premium";
    public static final String PREMIUM_PLUS_SKU = "de.js_labs.simpletabletennis.removeads_plus";
    public static final String PREMIUM_UPGRADE_SKU = "de.js_labs.simpletabletennis.premium_upgrade";

    private BillingClient billingClient;

    private Activity activity;
    private SimpleTableTennis game;

    public BillingManager(Activity activity, SimpleTableTennis game) {
        this.activity = activity;
        this.game = game;

        billingClient = BillingClient.newBuilder(activity).setListener(this).build();

        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@BillingClient.BillingResponse int billingResponseCode) {
                if (billingResponseCode == BillingClient.BillingResponse.OK) {
                    // The billing client is ready. You can query purchases here.
                }
            }
            @Override
            public void onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        });
    }

    @Override
    public void initPurchaseFlow(final String SKU) {
        if(billingClient.isReady()){
            launchPurchaseFlow(SKU);
        }else {
            billingClient.endConnection();
            billingClient.startConnection(new BillingClientStateListener() {
                @Override
                public void onBillingSetupFinished(int billingResponseCode) {
                    if (billingResponseCode == BillingClient.BillingResponse.OK) {
                        launchPurchaseFlow(SKU);
                    }else {
                        Toast.makeText(activity, "Unknown Error occured! Try again later.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onBillingServiceDisconnected() {
                    Toast.makeText(activity, "Unknown Error occured! Try again later.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void launchPurchaseFlow(String SKU) {
        BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                .setSku(SKU)
                .setType(BillingClient.SkuType.INAPP)
                .build();

        billingClient.launchBillingFlow(activity, flowParams);
    }

    @Override
    public void onPurchasesUpdated(int responseCode, @Nullable List<Purchase> purchases) {
        game.gameManager.ignoreInput = false;

        if (responseCode == BillingClient.BillingResponse.OK
                && purchases != null) {
            for (Purchase purchase : purchases) {
                //handlePurchase(purchase);
            }
        } else if (responseCode == BillingClient.BillingResponse.USER_CANCELED) {
            Toast.makeText(activity, "User canceled purchase :(", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(activity, "Unknown Error occured! Try again later.", Toast.LENGTH_SHORT).show();
        }
    }
}

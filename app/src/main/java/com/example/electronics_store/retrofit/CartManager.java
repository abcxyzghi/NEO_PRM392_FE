/*package com.example.electronics_store.retrofit;


import android.content.Context;
import android.content.SharedPreferences;
import com.example.electronics_store.retrofit.ProductResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static final String PREF_NAME = "cart_pref";
    private static final String CART_KEY = "cart_items";

    public static void addToCart(Context context, ProductResponse product) {
        List<ProductResponse> cart = getCart(context);
        cart.add(product);
        saveCart(context, cart);
    }

    public static void removeFromCart(Context context, ProductResponse product) {
        List<ProductResponse> cart = getCart(context);
        cart.removeIf(item -> item.getId() == product.getId());
        saveCart(context, cart);
    }

    public static List<ProductResponse> getCart(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(CART_KEY, null);
        if (json != null) {
            Type type = new TypeToken<List<ProductResponse>>() {}.getType();
            return new Gson().fromJson(json, type);
        }
        return new ArrayList<>();
    }

    private static void saveCart(Context context, List<ProductResponse> cart) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        String json = new Gson().toJson(cart);
        editor.putString(CART_KEY, json);
        editor.apply();
    }

    public static void clearCart(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().remove(CART_KEY).apply();
    }
}
*/
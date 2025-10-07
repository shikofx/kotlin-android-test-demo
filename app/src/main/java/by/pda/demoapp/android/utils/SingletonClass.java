package by.pda.demoapp.android.utils;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import by.pda.demoapp.android.model.CartItemModel;
import by.pda.demoapp.android.model.CheckoutInfo;

public class SingletonClass extends Methods {
    private static SingletonClass sSoleInstance;

    private Gson gson;
    public CheckoutInfo checkoutInfo = new CheckoutInfo();
    public CheckoutInfo billingInfo = new CheckoutInfo();
    public List<CartItemModel> cartItemList;
    public boolean isLogin = false;
    private boolean hasVisualChanges = false;

    private SingletonClass() {
        cartItemList = new ArrayList<>();
    }

    public static SingletonClass getInstance() {
        if (sSoleInstance == null) {
            sSoleInstance = new SingletonClass();
        }

        return sSoleInstance;
    }

    public Gson gson() {
        if (gson == null)
            gson = new Gson();
        return gson;
    }

    public boolean getHasVisualChanges() {
        return hasVisualChanges;
    }

    public void setHasVisualChanges(boolean hasVisualChanges) {
        this.hasVisualChanges = hasVisualChanges;
    }
}

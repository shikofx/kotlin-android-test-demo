package by.pda.demoapp.android.utils;

import android.app.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;

import by.pda.demoapp.android.R;
import by.pda.demoapp.android.databinding.DialogReviewBinding;
import by.pda.demoapp.android.interfaces.OnDialogCallBack;
import by.pda.demoapp.android.model.CartItemModel;
import by.pda.demoapp.android.view.activities.MainActivity;

import java.util.List;
import java.util.Objects;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class Methods extends Constants {

    public void startActivity(Activity mAc, Class aClass, int status) {
        if (mAc.isFinishing())
            return;
        switch (status) {
            case START_ACTIVITY -> mAc.startActivity(new Intent(mAc, aClass));
            case START_ACTIVITY_WITH_FINISH -> {
                mAc.startActivity(new Intent(mAc, aClass));
                mAc.finish();
            }
            case START_ACTIVITY_WITH_CLEAR_BACK_STACK ->
                    mAc.startActivity(new Intent(mAc, aClass).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
            case START_ACTIVITY_WITH_TOP ->
                    mAc.startActivity(new Intent(mAc, aClass).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }
    }

    public void startActivityWithDataBundle(Activity mAc, Class aClass, Bundle bundle, int status) {
        if (mAc.isFinishing())
            return;
        if (status == START_ACTIVITY) {
            mAc.startActivity(new Intent(mAc, aClass).putExtras(bundle));
        } else if (status == START_ACTIVITY_WITH_FINISH) {
            mAc.startActivity(new Intent(mAc, aClass).putExtras(bundle));
            mAc.finish();
        } else if (status == START_ACTIVITY_WITH_CLEAR_BACK_STACK) {
            mAc.startActivity(new Intent(mAc, aClass).putExtras(bundle).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
        } else if (status == START_ACTIVITY_WITH_TOP) {
            mAc.startActivity(new Intent(mAc, aClass).putExtras(bundle).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }
    }

    public void hideKeyboard(Activity mAc) {
        try {
            InputMethodManager imm = (InputMethodManager) mAc.getSystemService(INPUT_METHOD_SERVICE);
            if (imm == null) throw new AssertionError("assertion Error! Imm is null");
            imm.hideSoftInputFromWindow(Objects.requireNonNull(mAc.getCurrentFocus()).getWindowToken(), 0);
        } catch (Exception ignored) {
        }
    }

    public boolean isValidPassword(String passwordStr) {
        return passwordStr != null && passwordStr.length() >= 6;
    }

    public boolean isEqual(String str1, String str2) {
        return java.util.Objects.equals(str1, str2);
    }

    public void startMainActivity(Activity mAc, Bundle bundle) {

        mAc.startActivity(new Intent(mAc, MainActivity.class).putExtras(bundle));
        mAc.overridePendingTransition(0, 0);
    }

    public Bundle getBundle(int reqFrag, int selectedTab) {
        Bundle bundle = new Bundle();
        bundle.putInt(REQUEST_FRAGMENT, reqFrag);
        bundle.putInt(SELECTED_TAB, selectedTab);

        return bundle;
    }

    public int getTotalNum() {
        SingletonClass st = SingletonClass.getInstance();
        int totalNumOfProducts = 0;
        for (CartItemModel model : st.cartItemList) {
            totalNumOfProducts = totalNumOfProducts + model.getNumberOfProduct();
        }
        return totalNumOfProducts;
    }

    public void showDialog(final OnDialogCallBack onOkClickRCB,Activity mActivity, String title, String message, String okButtonText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity,R.style.MyDialogTheme);
        builder.setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(okButtonText, (dialog, id) -> {
                    if (onOkClickRCB != null)
                        onOkClickRCB.OnDialogCallBack(true);
                    dialog.dismiss();
                    hideKeyboard(mActivity);
                });
        AlertDialog alert = builder.create();
        try {
            alert.show();
        } catch (WindowManager.BadTokenException ex) {
            ex.printStackTrace();
        }
    }


    public double getTotalPrice(List<CartItemModel> cartItemList) {
        double totalPrice = 0;
        for (CartItemModel model : cartItemList) {
            totalPrice = totalPrice + (model.getNumberOfProduct() * model.getProductModel().getPrice());
        }

        return totalPrice;
    }

    public void showReviewDialog(Activity mAct) {
        final Dialog dialog = new Dialog(mAct);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        DialogReviewBinding reviewBinding = DataBindingUtil.inflate(LayoutInflater.from(mAct), R.layout.dialog_review, null, false);
        dialog.setContentView(reviewBinding.getRoot());

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.width = (int) (mAct.getResources().getDisplayMetrics().widthPixels * 0.80);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        dialog.getWindow().setAttributes(lp);

        reviewBinding.closeBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        dialog.show();
    }

}

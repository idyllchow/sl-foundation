package cc.seedland.foundation.component;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;


import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;

import cc.seedland.oa.foundation.R;

/**
 * author shibo
 * date 09/03/2018
 * description
 */

public class ChangelogDialog extends DialogFragment {
    protected MaterialDialog.SingleButtonCallback onPositiveCallback;
    protected MaterialDialog.SingleButtonCallback onNegativeCallback;

    public static ChangelogDialog create(boolean darkTheme, int accentColor, String data) {
        ChangelogDialog dialog = new ChangelogDialog();
        Bundle args = new Bundle();
        args.putString("data", data);
        args.putBoolean("dark_theme", darkTheme);
        args.putInt("accent_color", accentColor);
        dialog.setArguments(args);
        return dialog;
    }

    @SuppressLint("InflateParams")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final View customView;
        try {
            customView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_webview, null);
        } catch (InflateException e) {
            throw new IllegalStateException("This device does not support Web Views.");
        }
        MaterialDialog dialog =
                new MaterialDialog.Builder(getActivity())
                        .theme(getArguments().getBoolean("dark_theme") ? Theme.DARK : Theme.LIGHT)
                        .title(R.string.changelog)
                        .customView(customView, false)
                        .positiveText(R.string.upgrade)
                        .negativeText(R.string.cancel)
                        .onPositive(onPositiveCallback)
                        .onNegative(onNegativeCallback)
                        .canceledOnTouchOutside(false)
                        .cancelable(false)
                        .build();

        final WebView webView = (WebView) customView.findViewById(R.id.webview);
        final int accentColor = getArguments().getInt("accent_color");
        webView.loadData(getArguments().getString("data")
                        .replace(
                                "{style-placeholder}",
                                getArguments().getBoolean("dark_theme")
                                        ? "body { background-color: #444444; color: #fff; }"
                                        : "body { background-color: #fff; color: #000; }")
                        .replace("{link-color}", colorToHex(shiftColor(accentColor, true)))
                        .replace("{link-color-active}", colorToHex(accentColor))
                , "text/html; charset=UTF-8", null);
        return dialog;
    }

    private String colorToHex(int color) {
        return Integer.toHexString(color).substring(2);
    }

    private int shiftColor(int color, boolean up) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= (up ? 1.1f : 0.9f); // value component
        return Color.HSVToColor(hsv);
    }

    public ChangelogDialog onPositive(MaterialDialog.SingleButtonCallback callback) {
        this.onPositiveCallback = callback;
        return this;
    }

    public ChangelogDialog onNegative(MaterialDialog.SingleButtonCallback callback) {
        this.onNegativeCallback = callback;
        return this;
    }

}

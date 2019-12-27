package com.orlinskas.notebook.visual;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

public class IconTinter {
    public static Drawable tintIcon(Drawable icon, ColorStateList colorStateList) {
        if(icon!=null) {
            icon = DrawableCompat.wrap(icon).mutate();
            DrawableCompat.setTintList(icon, colorStateList);
            DrawableCompat.setTintMode(icon, PorterDuff.Mode.SRC_IN);
        }
        return icon;
    }

    public static void tintImageView(ImageView imageView, int colorStateListResId) {
        ColorStateList list = ContextCompat.getColorStateList(imageView.getContext(), colorStateListResId);
        if (list != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                imageView.setImageTintList(list);
            } else {
                imageView.setImageDrawable(tintIcon(imageView.getDrawable(), list));
            }
        }
    }
}

package com.example.ahmed.orgattendanceapp.common;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Toast;

/**
 * Created by ahmed on 20/12/17.
 */

public class Common {
    Context context = null;

    public Common(Context context) {
        this.context = context;
    }

    public final static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public void showToast(String message, int duration) {
        Toast.makeText(context, message, duration).show();
    }

    public void showAlertDialog(String message) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();

    }

    public Animation createFadeInAnimation() {

        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
        fadeIn.setDuration(1000);

        return fadeIn;
    }

    public Animation createFadeOutAnimation() {
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
        fadeOut.setStartOffset(1000);
        fadeOut.setDuration(1000);
        return fadeOut;
    }

    public Animation createSlideInLeftAnim(){
        Animation in = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
        return in;
    }

    public Animation createSlideOutRightAnim(){
        Animation out = AnimationUtils.loadAnimation(context, android.R.anim.slide_out_right);
        return out;
    }

    public TranslateAnimation slideToBottom(View view) {
        TranslateAnimation animate = new TranslateAnimation(0, 0, 0, view.getHeight());
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        view.setVisibility(View.GONE);
        return animate;
    }

    public TranslateAnimation slideToTop(View view) {
        TranslateAnimation animate = new TranslateAnimation(0, 0, view.getHeight(), 0);
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        view.setVisibility(View.VISIBLE);
        return animate;
    }

}

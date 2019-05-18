package cs134.miracosta.wastenot.utils;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

public class Animator {
    public static void animate(View view) {
        AnimatorSet mAnimatorSet = new AnimatorSet();
        mAnimatorSet.setDuration(500);
        mAnimatorSet.playTogether(ObjectAnimator.ofFloat(view, "translationX", 0, 25, -25, 25, -25, 15, -15, 6, -6, 0));
        mAnimatorSet.start();
    }
}

package com.voyager.ui;

import android.app.Service;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.voyager.menudemo.R;

/**
 * Created by wuhaojie on 2015/9/8.
 */
public class SlidingMenu extends HorizontalScrollView {

    private LinearLayout mWapper;
    private ViewGroup mMenu;
    private ViewGroup mContent;
    private int mScreenWidth;
    private int mMenuRightPadding;
    private boolean mOnce = false;
    private int mMenuWidth;
    private boolean isOpen;

    public SlidingMenu(Context context) {
        this(context, null);
    }

    public SlidingMenu(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SlidingMenu, 0, 0);
        int count = typedArray.getIndexCount();
        for(int i=0; i<count; i++){
            switch (typedArray.getIndex(i)){
                case R.styleable.SlidingMenu_menu_padding:
                    mMenuRightPadding = (int) typedArray.getDimension(i, 50);
                    break;
            }
        }

        WindowManager wm = (WindowManager) context.getSystemService(Service.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        mScreenWidth = displayMetrics.widthPixels;
//        mMenuRightPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dimension, context.getResources().getDisplayMetrics());

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!mOnce) {
            mWapper = (LinearLayout) getChildAt(0);
            mMenu = (ViewGroup) mWapper.getChildAt(0);
            mContent = (ViewGroup) mWapper.getChildAt(1);

            mMenuWidth = mMenu.getLayoutParams().width = mScreenWidth - mMenuRightPadding;
            mContent.getLayoutParams().width = mScreenWidth;
            mOnce = true;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            this.scrollTo(mMenuWidth, 0);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action){
            case MotionEvent.ACTION_UP:
                int scrollX = getScrollX();
                if(scrollX >= mMenuWidth/2){
                    this.smoothScrollTo(mMenuWidth, 0);
                    isOpen = false;
                }else {
                    this.smoothScrollTo(0, 0);
                    isOpen = true;
                }
                return true;
        }


        return super.onTouchEvent(ev);
    }

    @Override
    public boolean isHorizontalScrollBarEnabled() {
        return false;
    }

    public void btnMenu(){
        if(isOpen){
            this.smoothScrollTo(mMenuWidth, 0);
        }else {
            this.smoothScrollTo(0, 0);
        }
        isOpen = !isOpen;

    }


}

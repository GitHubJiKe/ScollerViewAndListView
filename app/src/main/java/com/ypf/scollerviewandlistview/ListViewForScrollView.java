package com.ypf.scollerviewandlistview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * Created by Administrator on 2016/4/15.
 */
public class ListViewForScrollView extends ListView {
    public ListViewForScrollView(Context context) {
        super(context);
    }

    public ListViewForScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListViewForScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int defaultsize = measureHight(Integer.MAX_VALUE >> 2, heightMeasureSpec);
        int expandSpec = MeasureSpec.makeMeasureSpec(defaultsize, MeasureSpec.AT_MOST);

        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    private int measureHight(int i, int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = i;//最小值是200px ，自己设定
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    /**
     * listview滑到底端了
     *
     * @return
     */
    public boolean isBottom() {
        int firstVisibleItem = getFirstVisiblePosition();//屏幕上显示的第一条是list中的第几条
        int childcount = getChildCount();//屏幕上显示多少条item
        int totalItemCount = getCount();//一共有多少条
        if ((firstVisibleItem + childcount) >= totalItemCount) {
            return true;
        }
        return false;
    }

    /**
     * listview在顶端
     *
     * @return
     */
    public boolean isTop() {
        int firstVisibleItem = getFirstVisiblePosition();
        if (firstVisibleItem == 0) {
            return true;
        }
        return false;
    }

    float down = 0;
    float y;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                down = ev.getRawY();

                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                y = ev.getRawY();
                if (isTop()) {
                    if (y - down > 1) {
                        //到顶端,向下滑动 把事件教给父类
                        getParent().requestDisallowInterceptTouchEvent(false);
                    } else {
                        //到顶端,向上滑动 把事件拦截 由自己处理
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                }

                if (isBottom()) {
                    if (y - down > 1) {
                        //到底端,向下滑动 把事件拦截 由自己处理
                        getParent().requestDisallowInterceptTouchEvent(true);
                    } else {
                        //到底端,向上滑动 把事件教给父类
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }
                }
                break;
            default:
                break;
        }

        return super.dispatchTouchEvent(ev);
    }

}

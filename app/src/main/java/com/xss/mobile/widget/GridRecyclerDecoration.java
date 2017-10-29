package com.xss.mobile.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;


/**
 * Created by xss on 2017/9/11.
 */

public class GridRecyclerDecoration extends RecyclerView.ItemDecoration {
    private int mColumn;
    private int mSpace;

    public GridRecyclerDecoration(int column, int space) {
        this.mColumn = column;
        this.mSpace = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        int position = parent.getChildAdapterPosition(view);
        int itemCount = parent.getAdapter().getItemCount();
        if (position >= 0 && position < itemCount) {
            //水平方向间距
            int horizontalPosition;
            horizontalPosition = position % mColumn; //水平上的位置，从0开始
            if (horizontalPosition == 0) {
                outRect.left = mSpace; //对每行第一个设置左边间距
            }
            outRect.right = mSpace;

            //垂直方向间距
            int verticalPosition;
            verticalPosition = position / mColumn; //垂直上的位置，从0开始
            if (verticalPosition == 0) {
                outRect.top = mSpace;  //对每列第一个设置顶部间距
            }
            outRect.bottom = mSpace;
        }
    }
}

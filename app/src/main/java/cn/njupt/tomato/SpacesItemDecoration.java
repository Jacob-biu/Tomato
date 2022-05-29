package cn.njupt.tomato;

import android.graphics.Rect;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;


public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    int mSpace;

    /**
     * 设置Item间隔
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.left = mSpace;
        outRect.right = mSpace;
        outRect.bottom = mSpace;
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = mSpace;
        }

    }

    public SpacesItemDecoration(int space) {
        this.mSpace = space;
    }
}


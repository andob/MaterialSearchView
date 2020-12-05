package com.miguelcatalan.materialsearchview;

import android.app.Activity;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import java.util.Stack;

public class MaterialSearchViewActivityHacks
{
    public static void onKeyboardClosed(Activity activity)
    {
        MaterialSearchView searchView=findMaterialSearchView(activity);
        if (searchView!=null&&searchView.isSearchOpen())
            searchView.closeSearch();
    }

    public interface SuperTouchEventDispatcher
    {
        boolean dispatchTouchEvent(MotionEvent event);
    }

    public static boolean dispatchTouchEvent(Activity activity, MotionEvent event, SuperTouchEventDispatcher superDispatcher)
    {
        MaterialSearchView searchView=findMaterialSearchView(activity);
        if (searchView!=null&&searchView.isSearchOpen())
        {
            int[] searchViewLocationOnScreen=new int[2];
            searchView.getLocationOnScreen(searchViewLocationOnScreen);
            int searchViewX=searchViewLocationOnScreen[0];
            int searchViewY=searchViewLocationOnScreen[1];

            RectF searchViewRect=new RectF(
                /*left*/ searchViewX,
                /*top*/ searchViewY,
                /*right*/ searchViewX+searchView.getWidth(),
                /*bottom*/ searchViewY+searchView.getHeight());

            if (!searchViewRect.contains(event.getRawX(), event.getRawY()))
            {
                searchView.closeSearch();
                return false;
            }
        }

        return superDispatcher.dispatchTouchEvent(event);
    }

    private static MaterialSearchView findMaterialSearchView(Activity activity)
    {
        Stack<ViewGroup> containersStack=new Stack<>();
        containersStack.push(activity.<ViewGroup>findViewById(android.R.id.content));

        while (!containersStack.isEmpty())
        {
            ViewGroup container=containersStack.pop();
            for (int i=0; i<container.getChildCount(); i++)
            {
                View child=container.getChildAt(i);
                if (child instanceof MaterialSearchView)
                    return (MaterialSearchView)child;

                if (child instanceof ViewGroup)
                    containersStack.push((ViewGroup)child);
            }
        }

        return null;
    }
}

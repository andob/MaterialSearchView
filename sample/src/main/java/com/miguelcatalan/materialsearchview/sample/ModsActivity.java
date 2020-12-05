package com.miguelcatalan.materialsearchview.sample;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.miguelcatalan.materialsearchview.MaterialSearchViewActivityHacks;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;
import net.yslibrary.android.keyboardvisibilityevent.Unregistrar;

public class ModsActivity extends AppCompatActivity
{
    private Unregistrar unregistrar = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mods);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.<Button>findViewById(R.id.someButton).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "test", Toast.LENGTH_SHORT).show();
            }
        });

        unregistrar=KeyboardVisibilityEvent.INSTANCE.registerEventListener(this, new KeyboardVisibilityEventListener() {
            public void onVisibilityChanged(boolean isOpened) {
                if (!isOpened)
                    MaterialSearchViewActivityHacks.onKeyboardClosed(ModsActivity.this);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MaterialSearchView searchView=findViewById(R.id.searchView);
        MenuItem searchButton=menu.findItem(R.id.action_search);
        searchView.setMenuItem(searchButton);

        return true;
    }

    @Override
    protected void onDestroy()
    {
        if (unregistrar!=null)
            unregistrar.unregister();

        super.onDestroy();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event)
    {
        return MaterialSearchViewActivityHacks.dispatchTouchEvent(this, event,
                //aka super::dispatchTouchEvent
                new MaterialSearchViewActivityHacks.SuperTouchEventDispatcher() {
                    public boolean dispatchTouchEvent(MotionEvent event) {
                        return ModsActivity.super.dispatchTouchEvent(event);
                    }
                });
    }
}

package net.zombie_sama.accountbook.base;

import android.app.Activity;
import android.os.Bundle;

import org.simple.eventbus.EventBus;

public abstract class BaseActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBefore();
        setContentView(getLayoutResource());
        initAfter();
    }

    protected abstract void initAfter();

    protected abstract int getLayoutResource();

    private void initBefore() {

    }

    private void registerEventBus(){
        EventBus.getDefault().register(this);
    }

    private void unregisterEventBus(){
        EventBus.getDefault().unregister(this);
    }
}

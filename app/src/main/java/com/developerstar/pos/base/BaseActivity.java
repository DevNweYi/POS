package com.developerstar.pos.base;

import android.app.Activity;

import com.developerstar.pos.util.NoEvent;
import com.developerstar.pos.util.ToastUtil;

import de.greenrobot.event.EventBus;


/**
 * Created by yefeng on 6/2/15.
 * github:yefengfreedom
 */
public class BaseActivity extends Activity {
    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(NoEvent msg) {
    }

    public void showToast(String message) {
        ToastUtil.showToast(this, message);
    }

    public void showToast(int message) {
        ToastUtil.showToast(this, message);
    }
}

package wang.imchao.plugin;

import android.util.Log;

import com.pgyersdk.feedback.PgyFeedback;
import com.pgyersdk.feedback.PgyFeedbackShakeManager;
import com.pgyersdk.update.PgyUpdateManager;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;

public class PgyerPlugin extends CordovaPlugin {
    private static String TAG = "PgyerPlugin";

    @Override
    public void initialize(final CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                PgyUpdateManager.register(cordova.getActivity());
                Log.i(TAG, "Pgyer update check registered");
            }
        });
    }

    @Override
    public void onResume(boolean multitasking) {
        super.onResume(multitasking);
        // 自定义摇一摇的灵敏度，默认为950，数值越小灵敏度越高。
        PgyFeedbackShakeManager.setShakingThreshold(1000);
        // 以对话框的形式弹出
        PgyFeedbackShakeManager.register(cordova.getActivity());
    }

    @Override
    public void onPause(boolean multitasking) {
        super.onPause(multitasking);
        PgyFeedbackShakeManager.unregister();
    }

    @Override
    public boolean execute(String action, JSONArray data, CallbackContext callbackContext) throws JSONException {
        if (action.equals("showFeedBackBox")) {
            PgyFeedback.getInstance().showDialog(this.cordova.getActivity());
            callbackContext.success("");
            return true;
        } else {
            return false;
        }
    }

}

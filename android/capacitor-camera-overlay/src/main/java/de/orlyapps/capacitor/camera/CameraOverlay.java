package de.orlyapps.capacitor.camera;

import com.getcapacitor.Bridge;
import com.getcapacitor.NativePlugin;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.PluginRequestCodes;

import android.Manifest;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.support.design.widget.CoordinatorLayout;

import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;

import android.webkit.WebView;
import android.widget.FrameLayout;

/**
 * @author https://github.com/cordova-plugin-camera-preview/cordova-plugin-camera-preview
 */
@NativePlugin(
        permissions={
                Manifest.permission.CAMERA
        },
        permissionRequestCode = CameraOverlay.REQUEST_IMAGE_CAPTURE
)
public class CameraOverlay extends Plugin implements CameraActivity.CameraPreviewListener {

    static final int REQUEST_IMAGE_CAPTURE = 1338;

    private CameraActivity fragment;
    private int containerViewId = 1339; //<- set to random number to prevent conflict with other plugins


    @PluginMethod()
    public void startCamera(PluginCall call) {

        if (!hasRequiredPermissions()) {
            saveCall(call);
            pluginRequestAllPermissions();

        } else {
            fragment = new CameraActivity();
            fragment.setEventListener(this);
            fragment.defaultCamera = "front";


            DisplayMetrics metrics = this.bridge.getActivity().getResources().getDisplayMetrics();
            // offset
            int computedX = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, metrics);
            int computedY = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, metrics);

            Display display =  this.bridge.getActivity().getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);

            // size
            int computedWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size.x, metrics);
            int computedHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size.y, metrics);

            fragment.setRect(computedX, computedY, computedWidth, computedHeight);

            final WebView webView = this.bridge.getWebView();
            final Bridge bridge = this.bridge;


            this.bridge.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {


                    //create or update the layout params for the container view
                    FrameLayout containerView = (FrameLayout)bridge.getActivity().findViewById(containerViewId);
                    if(containerView == null){
                        containerView = new FrameLayout(bridge.getActivity().getApplicationContext());
                        containerView.setId(containerViewId);

                        FrameLayout.LayoutParams containerLayoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
                        bridge.getActivity().addContentView(containerView, containerLayoutParams);
                    }
                    CoordinatorLayout layout = (CoordinatorLayout)webView.getParent();
                    layout.bringToFront();
                    webView.setBackgroundColor(Color.TRANSPARENT);


                    //add the fragment to the container
                    FragmentManager fragmentManager = bridge.getActivity().getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.add(containerView.getId(), fragment);
                    fragmentTransaction.commit();
                }
            });

            call.success();
        }





    }
    @PluginMethod()
    public void stopCamera(PluginCall call) {

        FragmentManager fragmentManager = bridge.getActivity().getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(fragment);
        fragmentTransaction.commit();
        fragment = null;

        call.success();
    }

    public void onCameraStarted() {

    }

    @Override
    protected void handleRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.handleRequestPermissionsResult(requestCode, permissions, grantResults);

        PluginCall savedCall = getSavedCall();
        if (savedCall == null) {
            return;
        }

        for(int result : grantResults) {
            if (result == PackageManager.PERMISSION_DENIED) {
                savedCall.error("User denied permission");
                return;
            }
        }

        startCamera(savedCall);
    }

}

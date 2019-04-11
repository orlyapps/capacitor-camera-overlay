package de.orlyapps.capacitor.camera;

import android.app.Activity;
import android.app.Fragment;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

/**
 * @author https://github.com/cordova-plugin-camera-preview/cordova-plugin-camera-preview
 */
public class CameraActivity extends Fragment {

    public interface CameraPreviewListener {

        void onCameraStarted();
    }

    public String defaultCamera;
    public FrameLayout mainLayout;
    public FrameLayout frameContainerLayout;
    public int width;
    public int height;
    public int x;
    public int y;

    public void setEventListener(CameraPreviewListener listener){
        eventListener = listener;
    }

    private String appResourcesPackage;
    private CameraPreviewListener eventListener;
    private static final String TAG = "CameraActivity";
    private Preview mPreview;
    private View view;
    private Camera mCamera;
    private int cameraCurrentlyLocked;
    // The first rear facing camera
    private int defaultCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        appResourcesPackage = getActivity().getPackageName();

        // Inflate the layout for this fragment
        view = inflater.inflate(getResources().getIdentifier("camera_activity", "layout", appResourcesPackage), container, false);
        createCameraPreview();
        return view;
    }

    public void setRect(int x, int y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    private void createCameraPreview(){
        if(mPreview == null) {

            //set box position and size
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(width, height);
            layoutParams.setMargins(x, y, 0, 0);
            frameContainerLayout = view.findViewById(getResources().getIdentifier("frame_container", "id", appResourcesPackage));
            frameContainerLayout.setLayoutParams(layoutParams);

            //video view
            mPreview = new Preview(getActivity());
            mainLayout = view.findViewById(getResources().getIdentifier("video_view", "id", appResourcesPackage));
            mainLayout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            mainLayout.addView(mPreview);
            mainLayout.setEnabled(false);

        }
    }



    @Override
    public void onResume() {
        super.onResume();

        setDefaultCameraId();

        mCamera = Camera.open(defaultCameraId);
        cameraCurrentlyLocked = defaultCameraId;

        if(mPreview.mPreviewSize == null){
            mPreview.setCamera(mCamera, cameraCurrentlyLocked);
            eventListener.onCameraStarted();
        } else {
            mPreview.switchCamera(mCamera, cameraCurrentlyLocked);
            mCamera.startPreview();
        }

        Log.d(TAG, "cameraCurrentlyLocked:" + cameraCurrentlyLocked);

        final FrameLayout frameContainerLayout = view.findViewById(getResources().getIdentifier("frame_container", "id", appResourcesPackage));

        ViewTreeObserver viewTreeObserver = frameContainerLayout.getViewTreeObserver();

        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    frameContainerLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    frameContainerLayout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                    Activity activity = getActivity();
                    if (isAdded() && activity != null) {
                        final RelativeLayout frameCamContainerLayout = (RelativeLayout) view.findViewById(getResources().getIdentifier("frame_camera_cont", "id", appResourcesPackage));

                        FrameLayout.LayoutParams camViewLayout = new FrameLayout.LayoutParams(frameContainerLayout.getWidth(), frameContainerLayout.getHeight());
                        camViewLayout.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;
                        frameCamContainerLayout.setLayoutParams(camViewLayout);
                    }
                }
            });
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        // Because the Camera object is a shared resource, it's very important to release it when the activity is paused.
        if (mCamera != null) {
            mPreview.setCamera(null, -1);
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }

    public Camera getCamera() {
        return mCamera;
    }

    private void setDefaultCameraId(){
        // Find the total number of cameras available
        int numberOfCameras = Camera.getNumberOfCameras();
        int facing = Camera.CameraInfo.CAMERA_FACING_FRONT;

        // Find the ID of the default camera
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == facing) {
                defaultCameraId = i;
                break;
            }
        }
    }








}

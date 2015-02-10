package arthurpeka.eu.openflashlight;

import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {
    Button btnSwitch;
    TextView txtFlashlightState;
    Camera camera;
    Camera.Parameters cameraParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boolean hasFlash = getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        btnSwitch = (Button)findViewById(R.id.btnToggleFlashlight);
        txtFlashlightState = (TextView)findViewById(R.id.txtStatus);
        initCamera();

        btnSwitch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                toggleFlash(!isFlashlightOn());
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(camera != null){
            camera.release();
            camera = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        toggleFlash(false);
    }

    private boolean isFlashlightOn(){
        cameraParams = camera.getParameters();

        return cameraParams.getFlashMode().equals(Camera.Parameters.FLASH_MODE_TORCH);
    }

    private void initCamera(){
        try {
            camera = Camera.open();
            cameraParams = camera.getParameters();
        }
        catch (RuntimeException e) {
            Log.e("Error initializing camera:",e.getMessage());
        }
    }

    private void toggleFlash(boolean turnOn){
        cameraParams = camera.getParameters();

        if(turnOn)
            cameraParams.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        else
            cameraParams.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);

        camera.setParameters(cameraParams);
        camera.startPreview();

        Toast toast = Toast.makeText(getApplicationContext(), cameraParams.getFlashMode(), Toast.LENGTH_SHORT);
        toast.show();

        String state = isFlashlightOn() ? "ON" : "OFF";
        txtFlashlightState.setText(state);
    }
}

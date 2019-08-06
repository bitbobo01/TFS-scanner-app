package com.example.tfsscanner;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.example.tfsscanner.Utils.Controller;
import com.example.tfsscanner.Utils.HistoryFile;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_CAMERA_CODE = 1;
    HistoryFile historyFile = new HistoryFile(this);
    Button scan_barcode,
            view_history;

    private ActionBar nav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //check Permission camera and internet
        checkPermission();
        historyFile.loadHistory();

        scan_barcode = findViewById(R.id.scan_barcode_button);
        view_history = findViewById(R.id.history_button);
        scan_barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanBarcode(v);
            }
        });
        view_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setView_history(v);
            }
        });
    }

    public void scanBarcode(View view) {
        Intent intent = new Intent(this, ScanBarcode.class);
        startActivity(intent);
    }
    public void setView_history(View view) {
        Intent intent = new Intent(this, History.class);
        startActivity(intent);
    }
    private void checkPermission(){
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            //if permission is Granted
        } else {
            //if not then ask for permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        PERMISSION_CAMERA_CODE);
        }
    }
}

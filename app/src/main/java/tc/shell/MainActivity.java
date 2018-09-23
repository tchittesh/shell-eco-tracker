package tc.shell;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.content.Context;
import android.widget.ToggleButton;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;

import com.jjoe64.graphview.series.LineGraphSeries;


public class MainActivity extends AppCompatActivity {

    public LocationManager locationManager;
    public LineGraphSeries<DataPoint> seriesalt = new LineGraphSeries<>();
    public LineGraphSeries<DataPoint> seriesspeed = new LineGraphSeries<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationlistener = new LocationListener() {
            long starttime=0;
            double startalt = 0;
            @Override
            public void onLocationChanged(Location location) {
                seriesalt.setTitle("Altitude");
                seriesalt.setColor(Color.BLUE);
                seriesalt.setDrawDataPoints(true);
                seriesalt.setDataPointsRadius(5);
                seriesspeed.setTitle("Speed");
                seriesspeed.setColor(Color.RED);
                seriesspeed.setDrawDataPoints(true);
                seriesspeed.setDataPointsRadius(5);
                GraphView graph = (GraphView) findViewById(R.id.graph);
                ToggleButton toggleButton = (ToggleButton) findViewById(R.id.toggleButton);
                if(starttime==0){
                    starttime=location.getTime();
                }
                if(startalt==0){
                    startalt=location.getAltitude();
                }
                if (toggleButton.isChecked()) {
                    seriesalt.appendData(new DataPoint((location.getTime()-starttime)/1000,location.getAltitude()-startalt+10), true, 10000);
                    seriesspeed.appendData(new DataPoint((location.getTime()-starttime)/1000,location.getSpeed()*2.23694), true, 10000);
                    graph.getViewport().setScalable(true);
                    graph.getViewport().setScalableY(true);

                    graph.addSeries(seriesalt);
                    graph.addSeries(seriesspeed);

                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationlistener);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void startstop(View view){
        TextView textView = (TextView) findViewById(R.id.textView);
        ToggleButton togglebutton = (ToggleButton) findViewById(R.id.toggleButton);
        if(togglebutton.isChecked()){
            textView.setText("recording...");
        }else{
            textView.setText("not recording");
        }
    }
}

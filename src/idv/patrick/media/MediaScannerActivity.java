package idv.patrick.media;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.BroadcastReceiver;
import android.net.Uri;
import android.os.Environment;
import android.widget.TextView;

public class MediaScannerActivity extends Activity
{
    public MediaScannerActivity() {
    }
 
    /** Called when the activity is first created or resumed. */
    @Override
    public void onResume() {
        super.onResume();
        
        setContentView(R.layout.media_scanner_activity);
        
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_MEDIA_SCANNER_STARTED);
        intentFilter.addAction(Intent.ACTION_MEDIA_SCANNER_FINISHED);
        intentFilter.addDataScheme("file");
        registerReceiver(mReceiver, intentFilter);
        
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"
                + Environment.getExternalStorageDirectory())));
            
        mTitle = (TextView) findViewById(R.id.title);
        mTitle.setText("Sent ACTION_MEDIA_MOUNTED to trigger the Media Scanner.");
    }

    /** Called when the activity going into the background or being destroyed. */
    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }
    
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_MEDIA_SCANNER_STARTED)) {
                mTitle.setText("Media Scanner started scanning " + intent.getData().getPath());     
            }
            else if (intent.getAction().equals(Intent.ACTION_MEDIA_SCANNER_FINISHED)) {
                mTitle.setText("Media Scanner finished scanning " + intent.getData().getPath());
                
                MediaScannerActivity.this.setResult(0);
                finish();
            }
        }
    };

    private TextView mTitle;
}

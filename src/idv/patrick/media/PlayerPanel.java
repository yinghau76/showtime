package idv.patrick.media;

import java.io.IOException;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Video;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class PlayerPanel extends Activity implements OnCompletionListener, OnErrorListener {

    private Showtime mApp;
    private VideoView mVideo;
    private View mProgressView;
    private Uri mUri;
    
    /**
     * Starts the PreferencesActivity for the specified user.
     *
     * @param context The application's environment.
     */
    static void show(Context context, Uri uri) {
        final Intent intent = new Intent(context, PlayerPanel.class);
        intent.setData(uri);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mApp = (Showtime) getApplication();
        
        setContentView(R.layout.player);
        
        mUri = getIntent().getData();
        
        mVideo = (VideoView) findViewById(R.id.surface);
        mVideo.setOnCompletionListener(this);
        mVideo.setOnErrorListener(this);
        mVideo.setMediaController(new MediaController(this));
        mVideo.setVideoURI(mUri);
        
        mProgressView = findViewById(R.id.progress_indicator);
        mProgressView.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() 
    {
        super.onStop();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            if (mVideo.isPlaying()) {
                mVideo.pause();
            }
            else {
                mVideo.start();
            }
            break;
        }
        
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onTrackballEvent(MotionEvent event) {
        return super.onTrackballEvent(event);
    }

    // OnCompletionListener
    
    @Override
    public void onCompletion(MediaPlayer mp) {
        finish();
    }

    // OnErrorListener
    
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Toast.makeText(this, R.string.media_on_error, Toast.LENGTH_SHORT).show();
        return false;
    }
}

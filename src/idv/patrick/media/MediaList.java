package idv.patrick.media;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class MediaList extends ListActivity {

    // Initialization
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main);
        getListView().setEmptyView(findViewById(R.id.empty));

        refreshList();
        registerForContextMenu(getListView());
    }

    @Override
    protected void onResume() {
        // reset bookmark
        Showtime app = (Showtime) getApplication();
        app.setBookmark(0);
        
        super.onResume();
    }

    // Video list

    private void refreshList() {
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null, null);

        setListAdapter(new SimpleCursorAdapter(
            this,
            android.R.layout.simple_list_item_1, 
            cur, 
            new String[] {MediaStore.Video.Media.DISPLAY_NAME}, 
            new int[] {android.R.id.text1}));
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        playMedia(id);
    }
    
    // Context menu
    
    private static final int ID_REMOVE = 1;

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, ID_REMOVE, 0, "Remove").setIcon(R.drawable.icon_delete);
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        
        switch (item.getItemId()) {
        case ID_REMOVE:
            removeMedia(info.id);
            return true;
        default:
            return super.onContextItemSelected(item);
        }
    }

    private void removeMedia(long id) {
        // Delete selected item
        Uri uri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id);
        getContentResolver().delete(uri, null, null);
        
        // Refresh list
        SimpleCursorAdapter adapter = (SimpleCursorAdapter) getListAdapter();
        adapter.notifyDataSetChanged();
    }
    
    private void playMedia(long id) {
        Uri uri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id);
        PlayerPanel.show(this, uri);
    }

    // Option menu

    private static final int ID_ABOUT = 1;
    private static final int ID_REFRESH = 2;
    private static final int ID_PREFERENCES = 3;
    private static final int ID_HELP = 4;
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, ID_ABOUT, 0, "About").setIcon(R.drawable.icon_notifications);
        menu.add(0, ID_REFRESH, 1, "Refresh").setIcon(R.drawable.icon_sync);
        menu.add(0, ID_PREFERENCES, 2, "Preferences").setIcon(R.drawable.icon_preferences);
        menu.add(0, ID_HELP, 3, "Help");
        
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case ID_ABOUT:
            new AlertDialog.Builder(this)
                .setTitle(R.string.about_title)
                .setMessage(R.string.about_message)
                .setPositiveButton(android.R.string.ok, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // empty
                    }})
                .show();
            return true;
            
        case ID_REFRESH:
            
            startActivityForResult(new Intent(this, MediaScannerActivity.class), 0);
            return true;
            
        case ID_PREFERENCES:
            SettingsActivity.show(this);
            return true;
            
        case ID_HELP:
            return true;
            
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) 
    {
        switch (requestCode)
        {
        case 0:
            refreshList();
            break;
        }
    }    
}
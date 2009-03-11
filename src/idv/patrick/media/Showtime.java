package idv.patrick.media;


import android.app.Application;

public class Showtime extends Application {
    
    public static final String TAG = "showtime";
    private int mBookmark;


    public void setBookmark(int bookmark) {
        mBookmark = bookmark;
    }
    
    public int getBookmark() {
        return mBookmark;
    }
}

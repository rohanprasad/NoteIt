package mdg.iitr.noteit;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class MainActivity extends Activity {

	Note notes;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent i = getIntent();
		Uri data = i.getData();
		if(data != null)
		{
			if(i.getType().indexOf("image") != -1)
			Globals.uris = data.toString();
			Globals.editing = true;
		}
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		notes = new Note(this);
		notes.setKeepScreenOn(true);
		setContentView(notes);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		notes.invalidate();
	}
	
	
}

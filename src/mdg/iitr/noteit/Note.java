package mdg.iitr.noteit;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

public class Note extends View {

	private Bitmap b_clear;
	private Bitmap b_save;
	private Bitmap temp_image;
	private Paint ink_color;
	private Paint line_color;
	private Paint bg_color;
	static private List<Integer> X_list;
	static private List<Integer> Y_list;
	private Boolean saving = false;
	private List<Integer> moving;
	private int X_pos;
	private int Y_pos;
	private int scr_h;
	private int scr_w;
	private Context app_c = getContext();

	public Note(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		
		Globals.ink_c = -16777216;
		X_list = new ArrayList<Integer>();
		Y_list = new ArrayList<Integer>();
		moving = new ArrayList<Integer>();
		ink_color = new Paint();
		ink_color.setAntiAlias(true);

		line_color = new Paint();
		line_color.setAntiAlias(true);
		line_color.setStrokeWidth(12);
		
		bg_color = new Paint();
		bg_color.setAntiAlias(true);
		bg_color.setColor(Color.WHITE);
		
		b_clear = BitmapFactory.decodeResource(getResources(), R.drawable.clear);
		b_save = BitmapFactory.decodeResource(getResources(), R.drawable.save);
		
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		
		scr_h = h;
		scr_w = w;
	}
	

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		
		
		
		setDrawingCacheEnabled(true);
		
		canvas.drawRect(0, 0, scr_w , scr_h, bg_color);
		
		line_color.setColor(Globals.ink_c);
		ink_color.setColor(Globals.ink_c);

		
		if(temp_image != null)
		{
			canvas.drawBitmap(temp_image, 0, 0, null);
		}
		
		int max = X_list.size();
		if(max>1)
		{
			for(int i=1; i<max; i++)
			{
				int X_posH = X_list.get(i-1);
				int Y_posH = Y_list.get(i-1);
				X_pos = X_list.get(i);
				Y_pos = Y_list.get(i);
				canvas.drawCircle(X_pos, Y_pos, 6, ink_color);
				if(moving.get(i-1) == 0)
				canvas.drawLine(X_posH, Y_posH, X_pos, Y_pos, line_color);
			}
		}
		
		
		
		canvas.drawBitmap(b_clear, 10, 10, null);
		canvas.drawBitmap(b_save, 20+b_clear.getWidth(), 10, null);
		canvas.drawRect(300, 10, 450, 50 , line_color);
		if(saving)
		{
			canvas.drawRect(0, 0, scr_w, 60, bg_color);
		}
		
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		
		int action = event.getAction();
		int Xcord = (int)event.getX();
		int Ycord = (int)event.getY();
		
		if(action == MotionEvent.ACTION_DOWN)
		{
			setDrawingCacheEnabled(true);
			moving.add(1);
			X_list.add(Xcord);
			Y_list.add(Ycord);
			
		}
		else if(action == MotionEvent.ACTION_UP)
		{
			if(Xcord<b_clear.getWidth() && Ycord<b_clear.getHeight())
			{
				temp_image = null;
			}
			else if(Xcord>20+b_clear.getWidth() && Xcord<20+b_clear.getWidth()+b_save.getWidth() && Ycord>10 && Ycord<10+b_save.getHeight())
			{				
				
					File roots = Environment.getExternalStorageDirectory();
					saving = true;
					buildDrawingCache();
					temp_image = Bitmap.createBitmap(getDrawingCache());
					File my_dir = new File(roots+"/NoteIt");
					int n=0;
					if(my_dir.mkdir()==false)
					{
						n = my_dir.list().length;
					}
					String fname = "Image-"+ n +".jpg";
					File file = new File (my_dir, fname);
					if (file.exists ()) 
						file.delete (); 
					try {
					       FileOutputStream out = new FileOutputStream(file);
					       temp_image.compress(Bitmap.CompressFormat.PNG, 90, out);
					       out.flush();
					       out.close();
					} catch (Exception e) {
					       e.printStackTrace();
					}
					saving = false;
					String filepath = Environment.getExternalStorageDirectory().getAbsolutePath().toString();
					filepath += "/NoteIt/" + fname;
					MediaScannerConnection.scanFile(getContext(),new String[]{ filepath}, null, null);
					Toast.makeText(getContext(),"Saved", Toast.LENGTH_SHORT).show();
				
			}
			else if(Xcord>300 && Xcord<450 && Ycord>10 && Ycord<50)
			{
				Intent i = new Intent(app_c,Color_picker.class);
				app_c.startActivity(i);
			}
			
			
			
			buildDrawingCache();
			
			temp_image = Bitmap.createBitmap(getDrawingCache());
			setDrawingCacheEnabled(false);
			X_list.clear();
			Y_list.clear();
			moving.clear();
			moving.add(1);
			X_list.add(Xcord);
			Y_list.add(Ycord);
		}
		else if(action == MotionEvent.ACTION_MOVE)
		{
			moving.add(0);		
			X_list.add(Xcord);
			Y_list.add(Ycord);
		}
		
		invalidate();
		return true;
	}
	
}

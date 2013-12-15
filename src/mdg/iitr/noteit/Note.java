package mdg.iitr.noteit;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

public class Note extends View {

	private Bitmap b_clear;
	private Bitmap temp_image;
	private Paint ink_color;
	private Paint line_color;
	static private List<Integer> X_list;
	static private List<Integer> Y_list;
	private List<Integer> moving;
	private int X_pos;
	private int Y_pos;
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

		line_color.setStrokeWidth(20);
		b_clear = BitmapFactory.decodeResource(getResources(), R.drawable.clear);
		
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		
		setDrawingCacheEnabled(true);
		
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
				canvas.drawCircle(X_pos, Y_pos, 10, ink_color);
				if(moving.get(i-1) == 0)
				canvas.drawLine(X_posH, Y_posH, X_pos, Y_pos, line_color);
			}
		}
		
		
		
		canvas.drawBitmap(b_clear, 10, 10, null);
		canvas.drawRect(300, 10, 450, 50 , line_color);
		
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

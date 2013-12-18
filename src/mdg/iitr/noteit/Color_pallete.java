package mdg.iitr.noteit;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class Color_pallete extends View {
	
	private Bitmap c_pallete;
	private Bitmap bar;
	private Bitmap handles;
	private int scr_h;
	private int scr_w;
	private Paint selected_color;
	private int c_color;
	private View v = getRootView(); 
	

	public Color_pallete(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		selected_color = new Paint();
		selected_color.setAntiAlias(true);
		c_color = Globals.ink_c;

		c_pallete = BitmapFactory.decodeResource(getResources(), R.drawable.colormap);
		bar = BitmapFactory.decodeResource(getResources(), R.drawable.bar);
		handles = BitmapFactory.decodeResource(getResources(), R.drawable.handle);
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

			selected_color.setColor(c_color);
		
		
		canvas.drawBitmap(c_pallete, (float) (scr_w - c_pallete.getWidth())/2, (float) (0.5*scr_w), null);
		canvas.drawBitmap(bar, (float) (scr_w - bar.getWidth())/2, (float) (0.9*scr_h), null);
		canvas.drawBitmap(handles, (float) (scr_w/2), (float) (0.9*scr_h), null);
		canvas.drawRect((float) (0.1*scr_w), (float) (0.1*scr_h), (float) (0.9*scr_w),(float) (0.4*scr_w) , selected_color);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		
		int action = event.getAction();
		int X_pos = (int)event.getX();
		int Y_pos = (int)event.getY();
		
		if(action == MotionEvent.ACTION_UP)
		{
			if(X_pos > 0.1*scr_w && X_pos < 0.9*scr_w && Y_pos > 0.1*scr_h && Y_pos < 0.2*scr_h )
			{
				Toast.makeText(getContext(), "" + c_color, Toast.LENGTH_SHORT).show();
				Globals.ink_c = c_color;
				Activity ac = (Activity)getContext();
				ac.finish();
			}
			
			
			v.setDrawingCacheEnabled(true);
			v.buildDrawingCache();
			c_color = v.getDrawingCache().getPixel(X_pos, Y_pos);
			
		}
		
		invalidate();
		return true;
		
	}
	
	

}

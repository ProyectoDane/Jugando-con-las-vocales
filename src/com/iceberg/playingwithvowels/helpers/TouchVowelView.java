/**  
 *   Copyright (C) 2015  CADISA
 *   
 *   Check License.txt
 */

package com.iceberg.playingwithvowels.helpers;

import java.util.ArrayList;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposePathEffect;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.Point;
import android.graphics.PorterDuffXfermode;
import android.graphics.Bitmap.Config;
import android.graphics.BlurMaskFilter.Blur;
import android.graphics.PorterDuff.Mode;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout.LayoutParams;

import com.iceberg.playingwithvowels.R;
import com.iceberg.playingwithvowels.helpers.VowelsHelper.VowelsEnum;

public class TouchVowelView extends View {
		
	public interface TouchVowelListener{
		void onVowelCompleted();
	}
	
	private Context _context;
	private Bitmap _bitmapBackground;
	private Bitmap _overlayDefault;
	private Bitmap _overlay;
	private Paint _paint;
	private Canvas _canvas;
	private ArrayList<Point> _points = new ArrayList<Point>();
	ArrayList<PaintLine> _paintLines = new ArrayList<PaintLine>();
	private PaintLine _redLine = null;
	private static final int TOUCH_TOLERANCE_DP = 40;
	private int _touchTolerance;
	private ArrayList<HiddenPoint> _allPoints = new ArrayList<HiddenPoint>();
	private boolean _completed = false;
	private ArrayList<TextIndication> _indications = new ArrayList<TextIndication>();

	public TouchVowelView(Context context){
		super(context);
		
		_context = context;
		_touchTolerance = dp2px(TOUCH_TOLERANCE_DP);
		
		SetLayoutFor(VowelsEnum.A);
	}
	
	public TouchVowelView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		_context = context;		
		_touchTolerance = dp2px(TOUCH_TOLERANCE_DP);
		
		SetLayoutFor(VowelsEnum.E);
	}

	public TouchVowelView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		_context = context;		
		_touchTolerance = dp2px(TOUCH_TOLERANCE_DP);
		
		SetLayoutFor(VowelsEnum.A);
	}
	
	public TouchVowelView(Context context, LayoutParams params, VowelsEnum vowel) {
		super(context);
		
		setLayoutParams(params);
			
		_context = context;				
		_touchTolerance = dp2px(TOUCH_TOLERANCE_DP);
		
		SetLayoutFor(vowel);
	}
	
	public void SetLayoutFor(VowelsEnum vowel){
		LoadPoints(vowel);
		
		Paint pathPaint = new Paint();
		pathPaint.setAntiAlias(true);
		pathPaint.setDither(true);
		pathPaint.setColor(getResources().getColor(R.color.graphomotor_vowel_text_border));
		pathPaint.setStyle(Paint.Style.STROKE);
		pathPaint.setStrokeJoin(Paint.Join.ROUND);
		pathPaint.setStrokeCap(Paint.Cap.ROUND);
		pathPaint.setStrokeWidth(58);
		pathPaint.setXfermode(null);
		
		Path pathBackground = new Path();
		
		for (PaintLine line : _paintLines) {
			pathBackground.moveTo(line.getStartPoint().x, line.getStartPoint().y);
			
			if(line.isCurve()){	
				Point curvePoint = line.getControlPoint();
				pathBackground.quadTo(curvePoint.x, curvePoint.y, line.getEndPoint().x, line.getEndPoint().y);
			}
			
			pathBackground.lineTo(line.getEndPoint().x, line.getEndPoint().y);
		}
		
		_bitmapBackground = Bitmap.createBitmap(500, 500, Config.ARGB_8888);		
		Canvas canvasBitmapBackground = new Canvas(_bitmapBackground);	
		canvasBitmapBackground.drawPath(pathBackground, pathPaint);			
		pathPaint.setColor( getResources().getColor(com.iceberg.playingwithvowels.R.color.vowel_color));
		pathPaint.setStrokeWidth(54);
		canvasBitmapBackground.drawPath(pathBackground, pathPaint);
					
		_overlayDefault = Bitmap.createBitmap(500, 500, Config.ARGB_8888);
		Canvas canvasOverlayDefault = new Canvas(_overlayDefault);
		pathPaint.setColor(getResources().getColor(R.color.graphomotor_vowel_text_background));
		canvasOverlayDefault.drawPath(pathBackground, pathPaint);
		
		pathPaint.setColor(getResources().getColor(R.color.graphomotor_vowel_text_arrow));
		Path pathDash = new Path();
		pathDash.moveTo(4, 0);
		pathDash.lineTo(0, -4);
		pathDash.lineTo(4, -4);
		pathDash.lineTo(8, 0);
		pathDash.lineTo(4, 4);
		pathDash.lineTo(0, 4);
		pathPaint.setPathEffect(new ComposePathEffect(new PathDashPathEffect(pathDash, 15, 0, PathDashPathEffect.Style.MORPH), new CornerPathEffect(10)));
					
		canvasOverlayDefault.drawPath(pathBackground, pathPaint);
		

		_overlay = Bitmap.createBitmap(500, 500, Config.ARGB_8888);
		_canvas = new Canvas(_overlay);	
		_canvas.drawPath(pathBackground, pathPaint);
				
		_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		_paint.setXfermode(new PorterDuffXfermode(Mode.SRC_OUT));
		_paint.setColor(Color.TRANSPARENT);
		_paint.setMaskFilter(new BlurMaskFilter(10, Blur.SOLID));
		
		Paint startPointPaint = new Paint();
		startPointPaint.setStyle(Paint.Style.FILL);
		startPointPaint.setColor(Color.RED);
		canvasOverlayDefault.drawCircle(_allPoints.get(0).x, _allPoints.get(0).y, 10, startPointPaint);
		
		Paint indicaionsPaint = new Paint();		
		indicaionsPaint.setColor(Color.BLACK);
		indicaionsPaint.setTextSize(15);
		for (TextIndication indication : _indications) {
			canvasOverlayDefault.drawText(indication.text, indication.x, indication.y, indicaionsPaint);
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_MOVE:
		case MotionEvent.ACTION_DOWN: {			
			Point point = new Point();
			point.x = (int) ev.getX();
			point.y = (int) ev.getY();
			
			if(checkPoint(point.x, point.y)){				
				_points.add(point);
								
				invalidate();
				
				if(!_completed){
					boolean allPointsCompleted = true;
					for (HiddenPoint hiddenPoint : _allPoints) {
						if (!hiddenPoint._touched)
							allPointsCompleted = false;
					}

					if (allPointsCompleted) {
						try {
							_completed = true;
							TouchVowelListener listener = (TouchVowelListener) _context;
							listener.onVowelCompleted();
						} catch (ClassCastException ex) {
							Log.w("Jugando con las Vocales", ex.getMessage());
						}
					}
				}
			}

			break;
		}
		case MotionEvent.ACTION_UP:
			break;
		}

		return true;
	}

	@Override
	public void onDraw(Canvas canvas) {		
		super.onDraw(canvas);
		//Magic begins
		
		// draw background 
		canvas.drawBitmap(_bitmapBackground, 0, 0, null);
		
		// copy the default overlay into temporary overlay and punch a hole in it
		_canvas.drawBitmap(_overlayDefault, 0, 0, null);

		// draw next line for complete
		if(_redLine != null && !_completed){
			Paint pathPaint = new Paint();
			Path pathBackground = new Path();
					
			pathBackground.moveTo(_redLine.getStartPoint().x, _redLine.getStartPoint().y);
			pathBackground.lineTo(_redLine.getEndPoint().x, _redLine.getEndPoint().y);
			
			pathPaint.setColor(Color.RED);
			Path pathDash = new Path();
			pathDash.moveTo(4, 0);
			pathDash.lineTo(0, -4);
			pathDash.lineTo(4, -4);
			pathDash.lineTo(8, 0);
			pathDash.lineTo(4, 4);
			pathDash.lineTo(0, 4);
			pathPaint.setPathEffect(new ComposePathEffect(new PathDashPathEffect(pathDash, 15, 0, PathDashPathEffect.Style.MORPH), new CornerPathEffect(10)));					
			
			_canvas.drawPath(pathBackground, pathPaint);
		}
		
		// exclude this line to show all as you draw
		for (Point point : _points) {				
			_canvas.drawCircle(point.x, point.y, 25, _paint);
		}
		
		// draw the overlay over the background
		canvas.drawBitmap(_overlay, 0, 0, null);
	}
	
	private boolean checkPoint(float x, float y) {
		boolean _valid = false;
		ArrayList<HiddenPoint> auxAllPoints = (ArrayList<HiddenPoint>)_allPoints.clone();
		
		for (int i = 0; i < _allPoints.size(); i++) {
			int previousPoint = i - 1; 
			HiddenPoint point = _allPoints.get(i);
			
			if(checkPoint(x, y, point) && (previousPoint == -1 || (auxAllPoints.get(previousPoint)._touched && previousPoint != -1))){
				point._touched = true;
				_valid = true;
				if(point.nextLine != null){
					_redLine = point.nextLine;
				}
			}		
		}
		
		return _valid;
	}
	
	private boolean checkPoint(float x, float y, HiddenPoint point){
		if (x > (point.x - _touchTolerance) && x < (point.x + _touchTolerance)) {
			if (y > (point.y - _touchTolerance) && y < (point.y + _touchTolerance)) {
				return true;
			}
		}
		
		return false;
	}
	
	private int dp2px(int dp) {
		Resources r = getContext().getResources();
		float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
		return (int) px;
	}
		
	private void LoadPoints(VowelsEnum vowel) {

		switch (vowel) {
		case A: {
			Point p1 = new Point(200, 50);
			Point p2 = new Point(65, 350);
			Point p3 = new Point(335, 350);
			Point p4 = new Point(135, 230);
			Point p5 = new Point(265, 230);

			_paintLines.add(new PaintLine(p1, p2));
			_paintLines.add(new PaintLine(p1, p3));
			_paintLines.add(new PaintLine(p4, p5));			
			
			//Esto es la primera barra /
			_allPoints.add(new HiddenPoint(200, 50));
			_allPoints.add(new HiddenPoint(190, 70));
			_allPoints.add(new HiddenPoint(175, 95));
			_allPoints.add(new HiddenPoint(160, 120));
			_allPoints.add(new HiddenPoint(145, 150));
			_allPoints.add(new HiddenPoint(130, 180));
			_allPoints.add(new HiddenPoint(120, 200));
			_allPoints.add(new HiddenPoint(100, 233));
			_allPoints.add(new HiddenPoint(85, 266));
			_allPoints.add(new HiddenPoint(75, 300));
			_allPoints.add(new HiddenPoint(65, 350, new PaintLine(p1, p3)));				
			
			//Esto es la segunda barra \
			_allPoints.add(new HiddenPoint(212, 70));
			_allPoints.add(new HiddenPoint(223, 95));
			_allPoints.add(new HiddenPoint(238, 120));
			_allPoints.add(new HiddenPoint(253, 150));
			_allPoints.add(new HiddenPoint(268, 180));
			_allPoints.add(new HiddenPoint(280, 200));
			_allPoints.add(new HiddenPoint(298, 233));
			_allPoints.add(new HiddenPoint(318, 266));
			_allPoints.add(new HiddenPoint(325, 300));
			_allPoints.add(new HiddenPoint(335, 350, new PaintLine(p4, p5)));
			
			//Esto es -
			_allPoints.add(new HiddenPoint(150, 230));
			_allPoints.add(new HiddenPoint(160, 230, new PaintLine(p4, p5)));
			_allPoints.add(new HiddenPoint(180, 230, new PaintLine(p4, p5)));
			_allPoints.add(new HiddenPoint(200, 230, new PaintLine(p4, p5)));
			_allPoints.add(new HiddenPoint(220, 230, new PaintLine(p4, p5)));
			_allPoints.add(new HiddenPoint(240, 230, new PaintLine(p4, p5)));
			_allPoints.add(new HiddenPoint(250, 230));
			
			
			_indications.add(new TextIndication("1", 170, 70));
			_indications.add(new TextIndication("2", 220, 70));
			_indications.add(new TextIndication("3", 175, 218));

			break;
		}
		case E: {
			Point p1 = new Point(105, 50);
			Point p2 = new Point(105, 350);
			Point p3 = new Point(290, 50);
			Point p4 = new Point(105, 200);
			Point p5 = new Point(240, 200);
			Point p6 = new Point(290, 350);

			_paintLines.add(new PaintLine(p1, p2));
			_paintLines.add(new PaintLine(p1, p3));
			_paintLines.add(new PaintLine(p4, p5));
			_paintLines.add(new PaintLine(p2, p6));
			
			_allPoints.add(new HiddenPoint(105, 50));
			_allPoints.add(new HiddenPoint(105, 70));
			_allPoints.add(new HiddenPoint(105, 90));
			_allPoints.add(new HiddenPoint(105, 120));
			_allPoints.add(new HiddenPoint(105, 150));
			_allPoints.add(new HiddenPoint(105, 180));
			_allPoints.add(new HiddenPoint(105, 210));
			_allPoints.add(new HiddenPoint(105, 240));
			_allPoints.add(new HiddenPoint(105, 270));
			_allPoints.add(new HiddenPoint(105, 300));
			_allPoints.add(new HiddenPoint(105, 330));
			_allPoints.add(new HiddenPoint(105, 350, new PaintLine(p1, p3)));
			
			_allPoints.add(new HiddenPoint(145, 50));
			_allPoints.add(new HiddenPoint(165, 50));
			_allPoints.add(new HiddenPoint(185, 50));
			_allPoints.add(new HiddenPoint(225, 50));
			_allPoints.add(new HiddenPoint(255, 50));
			_allPoints.add(new HiddenPoint(280, 50));
			_allPoints.add(new HiddenPoint(290, 50, new PaintLine(p4, p5)));
							
			_allPoints.add(new HiddenPoint(145, 200));
			_allPoints.add(new HiddenPoint(135, 200));
			_allPoints.add(new HiddenPoint(165, 200));
			_allPoints.add(new HiddenPoint(195, 200));
			_allPoints.add(new HiddenPoint(225, 200));
			_allPoints.add(new HiddenPoint(240, 200, new PaintLine(p2, p6)));
			
			_allPoints.add(new HiddenPoint(145, 350, new PaintLine(p2, p6)));
			_allPoints.add(new HiddenPoint(165, 350, new PaintLine(p2, p6)));
			_allPoints.add(new HiddenPoint(195, 350, new PaintLine(p2, p6)));
			_allPoints.add(new HiddenPoint(225, 350, new PaintLine(p2, p6)));
			_allPoints.add(new HiddenPoint(255, 350, new PaintLine(p2, p6)));
			_allPoints.add(new HiddenPoint(280, 350, new PaintLine(p2, p6)));
			_allPoints.add(new HiddenPoint(290, 350, new PaintLine(p2, p6)));
			
			
			_indications.add(new TextIndication("1", 85, 75));
			_indications.add(new TextIndication("2", 140, 43));
			_indications.add(new TextIndication("3", 140, 193));
			_indications.add(new TextIndication("4", 140, 343));
			
			break;
		}
		case I: {
			Point p1 = new Point(200, 50);				
			Point p2 = new Point(200, 350);

			_paintLines.add(new PaintLine(p1, p2));				
			
			_allPoints.add(new HiddenPoint(200, 50));
			_allPoints.add(new HiddenPoint(200, 80));
			_allPoints.add(new HiddenPoint(200, 110));
			_allPoints.add(new HiddenPoint(200, 130));
			_allPoints.add(new HiddenPoint(200, 160));
			_allPoints.add(new HiddenPoint(200, 190));
			_allPoints.add(new HiddenPoint(200, 220));
			_allPoints.add(new HiddenPoint(200, 250));
			_allPoints.add(new HiddenPoint(200, 280));
			_allPoints.add(new HiddenPoint(200, 310));
			_allPoints.add(new HiddenPoint(200, 340));
			_allPoints.add(new HiddenPoint(200, 350));
			
			_indications.add(new TextIndication("1", 180, 75));
			break;
		}
		case O: {
			Point p1 = new Point(200, 50);
			Point p2 = new Point(100, 50);//0.0
			Point p3 = new Point(100, 200);
			Point p4 = new Point(100, 350);
			Point p5 = new Point(200, 350);
			Point p6 = new Point(300, 350);
			Point p7 = new Point(300, 200);
			Point p8 = new Point(300, 50);
			
			_paintLines.add(new PaintLine(p1, p3, p2));
			_paintLines.add(new PaintLine(p3, p5, p4));
			_paintLines.add(new PaintLine(p5, p7, p6));
			_paintLines.add(new PaintLine(p7, p1, p8));
			
			
			_allPoints.add(new HiddenPoint(200, 50));
			_allPoints.add(new HiddenPoint(170, 55));
			_allPoints.add(new HiddenPoint(140, 70));
			_allPoints.add(new HiddenPoint(115, 95));				
			_allPoints.add(new HiddenPoint(110, 115));
			_allPoints.add(new HiddenPoint(107, 135));
			_allPoints.add(new HiddenPoint(104, 155));
			_allPoints.add(new HiddenPoint(102, 175));
			_allPoints.add(new HiddenPoint(100, 200));
			
			_allPoints.add(new HiddenPoint(102, 225));
			_allPoints.add(new HiddenPoint(104, 245));
			_allPoints.add(new HiddenPoint(107, 255));
			_allPoints.add(new HiddenPoint(110, 275));
			_allPoints.add(new HiddenPoint(115, 295));
			_allPoints.add(new HiddenPoint(137, 320));
			_allPoints.add(new HiddenPoint(170, 340));				
			_allPoints.add(new HiddenPoint(200, 350));
							
			_allPoints.add(new HiddenPoint(230, 340));
			_allPoints.add(new HiddenPoint(263, 330));
			_allPoints.add(new HiddenPoint(285, 295));
			_allPoints.add(new HiddenPoint(290, 275));
			_allPoints.add(new HiddenPoint(293, 255));
			_allPoints.add(new HiddenPoint(295, 245));
			_allPoints.add(new HiddenPoint(298, 225));
			_allPoints.add(new HiddenPoint(300, 200));
			
			_allPoints.add(new HiddenPoint(298, 175));
			_allPoints.add(new HiddenPoint(295, 155));
			_allPoints.add(new HiddenPoint(293, 135));
			_allPoints.add(new HiddenPoint(290, 115));
			_allPoints.add(new HiddenPoint(285, 95));
			_allPoints.add(new HiddenPoint(263, 70));
			_allPoints.add(new HiddenPoint(230, 55));	
			
			_indications.add(new TextIndication("1", 180, 43));
			break;
		}
		case U: {
			Point p1 = new Point(100, 50);//0.0
			Point p2 = new Point(100, 200);
			Point p3 = new Point(100, 350);
			Point p4 = new Point(200, 350);
			Point p5 = new Point(300, 350);
			Point p6 = new Point(300, 200);
			Point p7 = new Point(300, 50);
			
			_paintLines.add(new PaintLine(p1, p2));
			_paintLines.add(new PaintLine(p2, p4, p3));
			_paintLines.add(new PaintLine(p4, p6, p5));
			_paintLines.add(new PaintLine(p6, p7));
			
			_allPoints.add(new HiddenPoint(100, 50));
			_allPoints.add(new HiddenPoint(100, 75));
			_allPoints.add(new HiddenPoint(100, 100));
			_allPoints.add(new HiddenPoint(100, 125));
			_allPoints.add(new HiddenPoint(100, 150));
			_allPoints.add(new HiddenPoint(100, 175));
			_allPoints.add(new HiddenPoint(100, 200));				
			
			_allPoints.add(new HiddenPoint(100, 225));
			_allPoints.add(new HiddenPoint(102, 225));
			_allPoints.add(new HiddenPoint(104, 245));
			_allPoints.add(new HiddenPoint(107, 255));
			_allPoints.add(new HiddenPoint(110, 275));
			_allPoints.add(new HiddenPoint(115, 295));
			_allPoints.add(new HiddenPoint(137, 320));
			_allPoints.add(new HiddenPoint(170, 340));				
			_allPoints.add(new HiddenPoint(200, 350));
							
			_allPoints.add(new HiddenPoint(230, 350));
			_allPoints.add(new HiddenPoint(263, 320));
			_allPoints.add(new HiddenPoint(285, 295));
			_allPoints.add(new HiddenPoint(290, 275));
			_allPoints.add(new HiddenPoint(293, 255));
			_allPoints.add(new HiddenPoint(295, 245));
			_allPoints.add(new HiddenPoint(298, 225));
			_allPoints.add(new HiddenPoint(300, 200));
			
			_allPoints.add(new HiddenPoint(300, 175));
			_allPoints.add(new HiddenPoint(300, 150));
			_allPoints.add(new HiddenPoint(300, 125));
			_allPoints.add(new HiddenPoint(300, 100));
			_allPoints.add(new HiddenPoint(300, 75));
			_allPoints.add(new HiddenPoint(300, 50));
			
			_indications.add(new TextIndication("1", 80, 75));
			break;
		}

		default:
			break;
		}
	}
	
	class HiddenPoint{
		int x;
		int y;
		boolean _touched = false;
		int _previousPoint;
		PaintLine nextLine = null;
				
		public HiddenPoint(int x, int y){
			this.x = x;
			this.y = y;
		}
		
		public HiddenPoint(int x, int y, int previousPoint){
			this.x = x;
			this.y = y;
			_previousPoint = previousPoint;
		}
		
		public HiddenPoint(int x, int y, PaintLine nextLine){
			this.x = x;
			this.y = y;
			this.nextLine = nextLine;
		}
	}

	class TextIndication{
		
		public TextIndication(String t, float cx, float cy){
			text = t;
			x = cx;
			y = cy;
		}
		
		public String text;
		public float x;
		public float y;
	}
}
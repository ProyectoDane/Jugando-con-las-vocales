/**  
 *   Copyright (C) 2015  CADISA
 *   
 *   Check License.txt
 */

package com.iceberg.playingwithvowels.helpers;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.iceberg.playingwithvowels.helpers.VowelsHelper.VowelsEnum;

public class PaintView extends View {

	private Bitmap _bitmap;
	private Canvas _canvas;
	private Path _path;
	private Paint _paint;
	private static final int TOUCH_TOLERANCE_DP = 24;
	private static final int BACKGROUND = 0xFFDDDDDD;
	private List<PaintLine> _points = new ArrayList<PaintLine>();
	private List<Point> _allPoints = new ArrayList<Point>();
	private int _lastPointIndex = 0;
	private int _touchTolerance;
	private boolean _isPathStarted = false;

	public PaintView(Context context) {
		super(context);
		initDefaults();
	}

	public PaintView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initDefaults();
	}

	public PaintView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initDefaults();
	}

	@Override
	protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
		super.onSizeChanged(width, height, oldWidth, oldHeight);
		clear();

	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(BACKGROUND);
		canvas.drawBitmap(_bitmap, 0, 0, null);
		
		Path pathBackground = new Path();
					
		for (PaintLine line : _points) {
			pathBackground.moveTo(line.getStartPoint().x, line.getStartPoint().y);
			
			if(line.isCurve()){	
				Point curvePoint = line.getControlPoint();
				pathBackground.quadTo(curvePoint.x, curvePoint.y, line.getEndPoint().x, line.getEndPoint().y);
			}
			
			pathBackground.lineTo(line.getEndPoint().x, line.getEndPoint().y);
		}			
		
		_paint.setColor(Color.RED);
		_paint.setStrokeWidth(30);
		canvas.drawPath(pathBackground, _paint);
		
		_paint.setColor(Color.BLACK);
		_paint.setStrokeWidth(15);
		
		for (int i = 0; i < _lastPointIndex; i++) {
			Point startPoint = _points.get(i).getStartPoint();
			_path.moveTo(startPoint.x, startPoint.y);

			Point endPoint = _points.get(i).getEndPoint();
			
			if(_points.get(i).isCurve()){	
				Point controlPoint = _points.get(i).getControlPoint();
				_path.quadTo(controlPoint.x, controlPoint.y, endPoint.x, endPoint.y);
			}
							
			_path.lineTo(endPoint.x, endPoint.y);
		}
		
		canvas.drawPath(_path, _paint);
		
		_paint.setColor(Color.GREEN);
		for (PaintLine point : _points) {
			canvas.drawPoint(point.getStartPoint().x, point.getStartPoint().y, _paint);
			canvas.drawPoint(point.getEndPoint().x, point.getEndPoint().y, _paint);
			if(point.isCurve()){
				canvas.drawPoint(point.getControlPoint().x, point.getControlPoint().y, _paint);
			}
		}
		
		_paint.setColor(Color.GRAY);
		for (Point point : _allPoints) {
			canvas.drawPoint(point.x, point.y, _paint);
			
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			touch_start(x, y);
			invalidate();
			break;
		case MotionEvent.ACTION_MOVE:
			touch_move(x, y);
			invalidate();
			break;
		case MotionEvent.ACTION_UP:
			touch_up(x, y);
			invalidate();
			break;
		}
		return true;
	}

	private void initDefaults() {
		_canvas = new Canvas();			
		_path = new Path();
		_paint = new Paint();
		_paint.setAntiAlias(true);
		_paint.setDither(true);
		_paint.setColor(Color.BLACK);
		_paint.setStyle(Paint.Style.STROKE);
		_paint.setStrokeJoin(Paint.Join.ROUND);
		_paint.setStrokeCap(Paint.Cap.ROUND);
		_paint.setStrokeWidth(15);
		_touchTolerance = dp2px(TOUCH_TOLERANCE_DP);
		
		LoadPoints(VowelsEnum.U);			
	}

	private void touch_start(float x, float y) {

		if (checkStartPoint(x, y, _lastPointIndex)) {
			_path.reset();
			// user starts from given point so path can be is started
			_isPathStarted = true;
		} else {
			// user starts move from point which doen's belongs to _points list
			_isPathStarted = false;
		}

	}

	private void touch_move(float x, float y) {
		// draw line with finger move
		if (_isPathStarted) {
			_path.reset();
			PaintLine p = _points.get(_lastPointIndex);
			_path.moveTo(p.getStartPoint().x, p.getStartPoint().y);
			
			if(p.isCurve()){	
				Point curvePoint = p.getControlPoint();
				_path.quadTo(curvePoint.x, curvePoint.y, x, y);
			}
			
			_path.lineTo(x, y);
		}
	}
	
	private void touch_up(float x, float y) {
		_path.reset();
		if (checkEndPoint(x, y, _lastPointIndex) && _isPathStarted) {

			Point startPoint = _points.get(_lastPointIndex).getStartPoint();
			_path.moveTo(startPoint.x, startPoint.y);

			Point endPoint = _points.get(_lastPointIndex).getEndPoint();
			
			if(_points.get(_lastPointIndex).isCurve()){	
				Point controlPoint = _points.get(_lastPointIndex).getControlPoint();
				_path.quadTo(controlPoint.x, controlPoint.y, endPoint.x, endPoint.y);
			}
							
			_path.lineTo(endPoint.x, endPoint.y);
			
			_canvas.drawPath(_path, _paint);
			_path.reset();				

			++_lastPointIndex;
			_isPathStarted = false;
		}

	}

	public void setPaint(Paint paint) {
		this._paint = paint;
	}

	public Bitmap getBitmap() {
		return _bitmap;
	}

	public void clear() {
		_bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
		_bitmap.eraseColor(BACKGROUND);
		_canvas.setBitmap(_bitmap);
		invalidate();
	}

	private boolean checkStartPoint(float x, float y, int pointIndex) {
		if (pointIndex == _points.size()) {
			// out of bounds
			return false;
		}

		return checkPoint(x, y, _points.get(pointIndex).getStartPoint());
	}

	private boolean checkEndPoint(float x, float y, int pointIndex) {
		if (pointIndex == _points.size()) {
			// out of bounds
			return false;
		}

		return checkPoint(x, y, _points.get(pointIndex).getEndPoint());
	}

	private boolean checkPoint(float x, float y, Point point) {
		if (x > (point.x - _touchTolerance)
				&& x < (point.x + _touchTolerance)) {
			if (y > (point.y - _touchTolerance)
					&& y < (point.y + _touchTolerance)) {
				return true;
			}
		}
		return false;
	}

	public List<PaintLine> getPoints() {
		return _points;
	}

	public void setPoints(List<PaintLine> points) {
		this._points = points;
	}

	private int dp2px(int dp) {
		Resources r = getContext().getResources();
		float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
		return (int) px;
	}
	
	private Point getPointAtDistanceX(Point p1, Point p2, float dis)	{
	   //get vector   
	   float x3 = p2.x - p1.x;
	   float y3 = p2.y - p1.y;
	   
	   //normalize vector
	   float length = (float) Math.sqrt(x3 * x3 + y3 * y3 );
	   x3 /= length;
	   y3 /= length;

	   //scale vector
	   x3 *= dis;
	   y3 *= dis;

	   //add vector back onto initial point and return
	   Point point = new Point();
	   point.x = Math.round(p1.x + x3);
	   point.y = Math.round(p1.y + y3);
	   
	   return point;

	}
	

	private void LoadPoints(VowelsEnum vowel) {

		switch (vowel) {
		case A: {
			Point p1 = new Point(65, 300);
			Point p2 = new Point(200, 50);				
			Point p3 = new Point(335, 300);
			Point p4 = new Point(120, 200);
			Point p5 = new Point(280, 200);

			_points.add(new PaintLine(p1, p2));
			_points.add(new PaintLine(p2, p3));
			_points.add(new PaintLine(p4, p5));
			
			
			_allPoints.add(p1);
			_allPoints.add(new Point(85, 266));
			_allPoints.add(new Point(100, 233));
			_allPoints.add(p4);
			_allPoints.add(new Point(130, 180));
			_allPoints.add(new Point(145, 150));
			_allPoints.add(new Point(160, 120));				
			_allPoints.add(new Point(175, 95));
			_allPoints.add(new Point(190, 70));
			_allPoints.add(p2);				
			
			_allPoints.add(p3);
			_allPoints.add(new Point(318, 266));
			_allPoints.add(new Point(298, 233));
			_allPoints.add(p5);
			_allPoints.add(new Point(268, 180));
			_allPoints.add(new Point(253, 150));
			_allPoints.add(new Point(238, 120));				
			_allPoints.add(new Point(223, 95));
			_allPoints.add(new Point(212, 70));
			
			
			_allPoints.add(new Point(140, 200));
			_allPoints.add(new Point(160, 200));
			_allPoints.add(new Point(180, 200));
			_allPoints.add(new Point(200, 200));
			_allPoints.add(new Point(220, 200));
			_allPoints.add(new Point(240, 200));
			_allPoints.add(new Point(260, 200));

			break;
		}
		case E: {
			Point p1 = new Point(65, 50);
			Point p2 = new Point(65, 350);
			Point p3 = new Point(250, 50);
			Point p4 = new Point(65, 200);
			Point p5 = new Point(200, 200);
			Point p6 = new Point(250, 350);

			_points.add(new PaintLine(p1, p2));
			_points.add(new PaintLine(p1, p3));
			_points.add(new PaintLine(p4, p5));
			_points.add(new PaintLine(p2, p6));
			
			
			_allPoints.add(new Point(65, 50));
			_allPoints.add(new Point(65, 70));
			_allPoints.add(new Point(65, 90));
			_allPoints.add(new Point(65, 120));
			_allPoints.add(new Point(65, 150));
			_allPoints.add(new Point(65, 180));
			_allPoints.add(new Point(65, 210));
			_allPoints.add(new Point(65, 240));
			_allPoints.add(new Point(65, 270));
			_allPoints.add(new Point(65, 300));
			_allPoints.add(new Point(65, 330));
			_allPoints.add(new Point(65, 350));
			
			_allPoints.add(new Point(95, 50));
			_allPoints.add(new Point(125, 50));
			_allPoints.add(new Point(155, 50));
			_allPoints.add(new Point(185, 50));
			_allPoints.add(new Point(215, 50));
			_allPoints.add(new Point(240, 50));
			_allPoints.add(new Point(250, 50));
							
			_allPoints.add(new Point(65, 200));
			_allPoints.add(new Point(95, 200));
			_allPoints.add(new Point(125, 200));
			_allPoints.add(new Point(155, 200));
			_allPoints.add(new Point(185, 200));
			_allPoints.add(new Point(200, 200));
			
			_allPoints.add(new Point(95, 350));
			_allPoints.add(new Point(125, 350));
			_allPoints.add(new Point(155, 350));
			_allPoints.add(new Point(185, 350));
			_allPoints.add(new Point(215, 350));
			_allPoints.add(new Point(240, 350));
			_allPoints.add(new Point(250, 350));
			
			break;
		}
		case I: {
			
			Point p1 = new Point(80, 50);				
			Point p2 = new Point(80, 350);
			

			_points.add(new PaintLine(p1, p2));				
			
			_allPoints.add(new Point(80, 50));
			_allPoints.add(new Point(80, 80));
			_allPoints.add(new Point(80, 110));
			_allPoints.add(new Point(80, 130));
			_allPoints.add(new Point(80, 160));
			_allPoints.add(new Point(80, 190));
			_allPoints.add(new Point(80, 220));
			_allPoints.add(new Point(80, 250));
			_allPoints.add(new Point(80, 280));
			_allPoints.add(new Point(80, 310));
			_allPoints.add(new Point(80, 340));
			_allPoints.add(new Point(80, 350));
			break;
		}
		case O: {
			Point p1 = new Point(200, 100);
			Point p2 = new Point(100, 100);//0.0
			Point p3 = new Point(100, 250);
			Point p4 = new Point(100, 400);
			Point p5 = new Point(200, 400);
			Point p6 = new Point(300, 400);
			Point p7 = new Point(300, 250);
			Point p8 = new Point(300, 100);
			
			_points.add(new PaintLine(p1, p3, p2));
			_points.add(new PaintLine(p3, p5, p4));
			_points.add(new PaintLine(p5, p7, p6));
			_points.add(new PaintLine(p7, p1, p8));
			
			
			_allPoints.add(new Point(200, 100));
			_allPoints.add(new Point(170, 105));
			_allPoints.add(new Point(140, 120));
			_allPoints.add(new Point(115, 145));				
			_allPoints.add(new Point(110, 165));
			_allPoints.add(new Point(107, 185));
			_allPoints.add(new Point(104, 205));
			_allPoints.add(new Point(102, 225));
			_allPoints.add(new Point(100, 250));
			
			_allPoints.add(new Point(102, 275));
			_allPoints.add(new Point(104, 295));
			_allPoints.add(new Point(107, 305));
			_allPoints.add(new Point(110, 325));
			_allPoints.add(new Point(115, 345));
			_allPoints.add(new Point(137, 370));
			_allPoints.add(new Point(170, 390));				
			_allPoints.add(new Point(200, 400));
							
			_allPoints.add(new Point(230, 390));
			_allPoints.add(new Point(263, 370));
			_allPoints.add(new Point(285, 345));
			_allPoints.add(new Point(290, 325));
			_allPoints.add(new Point(293, 305));
			_allPoints.add(new Point(295, 295));
			_allPoints.add(new Point(298, 275));
			_allPoints.add(new Point(300, 250));
							
			_allPoints.add(new Point(230, 105));
			_allPoints.add(new Point(263, 120));
			_allPoints.add(new Point(285, 145));				
			_allPoints.add(new Point(290, 165));
			_allPoints.add(new Point(293, 185));
			_allPoints.add(new Point(295, 205));
			_allPoints.add(new Point(298, 225));
			
			break;
		}
		case U: {
			/*Point p1 = new Point(60, 50);
			Point p2 = new Point(60, 220);
			Point p3 = new Point(120, 300);
			Point p4 = new Point(180, 220);
			Point p5 = new Point(180, 50);
			
			_points.add(new PaintLine(p1, p2));
			_points.add(new PaintLine(p2, p4, p3));				
			_points.add(new PaintLine(p4, p5));*/
			
			//Point p1 = new Point(200, 100);
			Point p1 = new Point(100, 100);//0.0
			Point p2 = new Point(100, 250);
			Point p3 = new Point(100, 400);
			Point p4 = new Point(200, 400);
			Point p5 = new Point(300, 400);
			Point p6 = new Point(300, 250);
			Point p7 = new Point(300, 100);
			
			_points.add(new PaintLine(p1, p2));
			_points.add(new PaintLine(p2, p4, p3));
			_points.add(new PaintLine(p4, p6, p5));
			_points.add(new PaintLine(p6, p7));
			
			_allPoints.add(new Point(100, 100));
			_allPoints.add(new Point(100, 125));
			_allPoints.add(new Point(100, 150));
			_allPoints.add(new Point(100, 175));
			_allPoints.add(new Point(100, 200));
			_allPoints.add(new Point(100, 225));
			_allPoints.add(new Point(100, 250));				
			
			_allPoints.add(new Point(100, 275));
			_allPoints.add(new Point(102, 275));
			_allPoints.add(new Point(104, 295));
			_allPoints.add(new Point(107, 305));
			_allPoints.add(new Point(110, 325));
			_allPoints.add(new Point(115, 345));
			_allPoints.add(new Point(137, 370));
			_allPoints.add(new Point(170, 390));				
			_allPoints.add(new Point(200, 400));
							
			_allPoints.add(new Point(230, 390));
			_allPoints.add(new Point(263, 370));
			_allPoints.add(new Point(285, 345));
			_allPoints.add(new Point(290, 325));
			_allPoints.add(new Point(293, 305));
			_allPoints.add(new Point(295, 295));
			_allPoints.add(new Point(298, 275));
			_allPoints.add(new Point(300, 250));
			
			_allPoints.add(new Point(300, 225));
			_allPoints.add(new Point(300, 200));
			_allPoints.add(new Point(300, 175));
			_allPoints.add(new Point(300, 150));
			_allPoints.add(new Point(300, 125));
			_allPoints.add(new Point(300, 100));
			
			break;
		}

		default:
			break;
		}
	}		
}

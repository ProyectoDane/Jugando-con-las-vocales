/**  
 *   Copyright (C) 2015  CADISA
 *   
 *   Check License.txt
 */

package com.iceberg.playingwithvowels.helpers;

import android.graphics.Point;

public class PaintLine {
	private Point _startPoint;
	private Point _endPoint;
	private boolean _curve = false;
	private Point _controlPoint;
	
	public PaintLine(Point start, Point end, Point control) {
		_startPoint = start;
		_endPoint = end;
		_curve = true;
		_controlPoint = control;
	}

	public PaintLine(Point start, Point end) {
		_startPoint = start;
		_endPoint = end;
	}

	public Point getStartPoint() {
		return _startPoint;
	}

	public Point getEndPoint() {
		return _endPoint;
	}
	
	public Point getControlPoint() {
		return _controlPoint;
	}
	
	public boolean isCurve() {
		return _curve;
	}
}

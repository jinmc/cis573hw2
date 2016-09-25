package edu.upenn.cis573.hwk2;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class GameView extends View {

    private int size;
    private double xSpace;
    private double ySpace;
    private Bitmap dot;
    private Point[][] points;
    private ArrayList<Line> p1Lines = new ArrayList<Line>();
    private Paint p1LinesPaint = new Paint();
    private ArrayList<Line> p2Lines = new ArrayList<Line>();
    private Paint p2LinesPaint = new Paint();
    private boolean drawLine = false;
    private Point startPoint;
    private Point currentEnd;
    private ArrayList<Rect> p1Rectangles = new ArrayList<Rect>();
    private Paint p1RectPaint = new Paint();
    private ArrayList<Rect> p2Rectangles = new ArrayList<Rect>();
    private Paint p2RectPaint = new Paint();
    private boolean isP1Turn = true;
    private Line lastLine;
    private Rect lastRect;
    private boolean isP1LastTurn;

    public GameView(Context context) {
        super(context);
        init();
    }

    public GameView(Context context, AttributeSet as) {
        super(context, as);
        init();
    }

    public void reset(){
        p1Lines = new ArrayList<Line>();
        p2Lines = new ArrayList<Line>();
        p1Rectangles = new ArrayList<Rect>();
        p2Rectangles = new ArrayList<Rect>();
        drawLine = false;
        isP1Turn = true;
        lastLine = null;
        lastRect = null;
        init();
        invalidate();
    }

    public void undo(){
        if(lastLine == null) return;
        if(isP1LastTurn) {
            p1Lines.remove(lastLine);
            if(lastRect != null) p1Rectangles.remove(lastRect);
        }
        else {
            p2Lines.remove(lastLine);
            if(lastRect != null) p2Rectangles.remove(lastRect);
        }
        isP1Turn = isP1LastTurn;
        invalidate();
    }

    /*
     * Sets initial variables for size, dot, paints, etc
     */
    private void init(){
        this.size = Integer.parseInt(GameActivity.boardSize);

        dot = BitmapFactory.decodeResource(getResources(), R.drawable.dot);
        points = new Point[size][size];

        p1LinesPaint.setColor(Color.RED);
        p1LinesPaint.setStrokeWidth(5);

        p1RectPaint.setColor(Color.RED);
        p1RectPaint.setStrokeWidth(5);
        p1RectPaint.setAlpha(50);

        p2LinesPaint.setColor(Color.BLUE);
        p2LinesPaint.setStrokeWidth(5);

        p2RectPaint.setColor(Color.BLUE);
        p2RectPaint.setStrokeWidth(5);
        p2RectPaint.setAlpha(50);
    }

    /*
     * Find how far apart to space points after screen width and
     * height have been set
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        int width = this.getWidth();
        int height = this.getHeight();
        xSpace = width/size;
        ySpace = height/size;
    }

    public void onDraw(Canvas c){
        for(int i = 0; i < size; i++){
            int x = (int) (xSpace*(i + 0.5));
            for(int j = 0; j < size; j++) {
                int y = (int) (ySpace * (j + 0.5));
                Point p = new Point(x, y);
                points[i][j] = p;
                c.drawBitmap(dot, x, y, null);
            }
        }

        //draw existing lines
        for(Line l : p1Lines){
            Point start = l.getStart();
            Point end = l.getEnd();
            c.drawLine(start.getX(), start.getY(), end.getX(), end.getY(), p1LinesPaint);
        }

        //draw existing lines
        for(Line l : p2Lines){
            Point start = l.getStart();
            Point end = l.getEnd();
            c.drawLine(start.getX(), start.getY(), end.getX(), end.getY(), p2LinesPaint);
        }

        //draw existing rectangles
        for(Rect r: p1Rectangles){
            c.drawRect(r, p1RectPaint);
        }

        for(Rect r: p2Rectangles){
            c.drawRect(r, p2RectPaint);
        }

        //draw moving line
        if(drawLine){
            if(isP1Turn) {
                c.drawLine(startPoint.getX(), startPoint.getY(), currentEnd.getX(), currentEnd.getY(), p1LinesPaint);
            } else {
                c.drawLine(startPoint.getX(), startPoint.getY(), currentEnd.getX(), currentEnd.getY(), p2LinesPaint);
            }
        }
    }

    /*
     * Returns a dot in the grid if there is one within 20 pixels of this Point
     */
    private Point closestDot(Point p){
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                Point dotCenter = getDotCenter(points[i][j]);
                if(p.distance(dotCenter) < 20){
                    return dotCenter;
                }
            }
        }
        return null;
    }

    /*
     * Returns true if two points are 1 space apart (either vertical or horizontal)
     */
    private boolean adjacentDots(Point start, Point end){
        int xDist = Math.abs(start.getX()-end.getX());
        int yDist = Math.abs(start.getY()-end.getY());
        boolean isXAdj = xDist < xSpace + 3;
        boolean isYAdj = yDist < ySpace + 3;
        return (isXAdj && isYAdj) && ((xDist < 3) ^ (yDist < 3));
    }

    /*
     * Returns a Point that is at the center of the dot in the grid
     */
    private Point getDotCenter(Point p) {
        int x = p.getX() + dot.getWidth()/2;
        int y = p.getY() + dot.getHeight()/2;
        return new Point(x,y);
    }

    /*
     * Takes in a line and returns true if that line creates
     * a box
     */
    private boolean isNewSquare(Line newLine){
        boolean isNewSquare = false;
        ArrayList<Line> lines = new ArrayList<>();
        lines.addAll(p2Lines);
        lines.addAll(p1Lines);
        for(Line l: lines){
            if(newLine.equals(l)) continue;
            int left = 0, right = 0, top = 0, bottom = 0;
            // See if there is a parallel line to the new line
            if (newLine.getStart().equalX(l.getStart()) && newLine.getEnd().equalX(l.getEnd()) && (newLine.getStart().getX() - newLine.getEnd().getX()) != 0) {
                left = newLine.getStart().getX();
                right = newLine.getEnd().getX();
                top = Math.min(newLine.getStart().getY(), l.getStart().getY());
                bottom = Math.max(newLine.getStart().getY(), l.getStart().getY());
            }
            else if (newLine.getStart().equalY(l.getStart()) && newLine.getEnd().equalY(l.getEnd()) && (newLine.getStart().getY() - newLine.getEnd().getY()) != 0) {
                top = newLine.getStart().getY();
                bottom = newLine.getEnd().getY();
                left = Math.min(newLine.getStart().getX(), l.getStart().getX());
                right = Math.max(newLine.getStart().getX(), l.getStart().getX());
            }
            else continue;
            Line side1 = new Line(l.getStart(), newLine.getStart());
            Line side2 = new Line(l.getEnd(), newLine.getEnd());
            //If the sides are drawn (i.e. there is a full box)
            if(
                    lines.contains(side1) && lines.contains(side2)) {
//                    side1.isContained(lines) && side2.isContained(lines)){
                Rect r = new Rect(left, top, right, bottom);
                if(p1Rectangles.contains(r) || p2Rectangles.contains(r)) continue;
                if(isP1Turn){
                    p1Rectangles.add(r);
                } else p2Rectangles.add(r);
                lastRect = r;
                invalidate();
                isNewSquare = true;
            }
        }
        return isNewSquare;
    }

    /*
    This method is called when the user interacts with the View
     */
    public boolean onTouchEvent(MotionEvent e){
        //Begin drawing line
        if (e.getAction() == MotionEvent.ACTION_DOWN){
            Point p = new Point((int)(e.getX()), (int)(e.getY()));
            p = closestDot(p);
            if(p != null){
                startPoint = p;
                currentEnd = p;
                drawLine = true;
            }
        } //Done drawing line
        else if (e.getAction() == MotionEvent.ACTION_UP && drawLine){
            Point endPoint = new Point((int)(e.getX()), (int)(e.getY()));
            endPoint = closestDot(endPoint);
            if(endPoint != null){
                //Check that line endpoint is valid
                if(adjacentDots(startPoint,endPoint)){
                    Line l = new Line(endPoint, startPoint);
                    if (startPoint.getX() < endPoint.getX() || startPoint.getY() < endPoint.getY()) {
                        l = new Line(startPoint, endPoint);
                    }
                    if(
                            !p1Lines.contains(l) && !p2Lines.contains(l)) {
//                            !l.isContained(p1Lines)&&!l.isContained(p2Lines)){
                        if(isP1Turn) p1Lines.add(l);
                        else p2Lines.add(l);
                        lastLine = l;
                        isP1LastTurn =  isP1Turn;
                        if(!isNewSquare(l)){
                            isP1Turn = !isP1Turn;
                            lastRect = null;
                        }
                    }
                }
            }
            drawLine = false;
        }
        //Moving mouse
        else{
            currentEnd = new Point((int)(e.getX()), (int)(e.getY()));
        }
        invalidate();
        if(isGameOver()){
            displayScore();
        }
        return true;
    }

    private boolean isGameOver(){
        return (p1Rectangles.size() + p2Rectangles.size()) == Math.pow((size-1),2);
    }

    /*
     * Displays the score in a toast (for once the game is over)
     */
    private void displayScore(){
        int p1Score = p1Rectangles.size();
        int p2Score = p2Rectangles.size();
        String winner = "Red";
        if (p2Score > p1Score) {
            winner = "Blue";

            int winnerScore = (p1Score > p2Score ? p1Score : p2Score);
            int loserScore = (p1Score < p2Score ? p1Score : p2Score);

            Toast.makeText(this.getContext(), winner + " wins by a score of " + winnerScore + " to " + loserScore, Toast.LENGTH_LONG).show();
        } else if (p2Score == p1Score) {
            Toast.makeText(this.getContext(), "The game ends in a tie", Toast.LENGTH_LONG).show();
        }
    }
}
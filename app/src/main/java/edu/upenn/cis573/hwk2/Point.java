package edu.upenn.cis573.hwk2;

public class Point {
    private int x;
    private int y;
    public Point(int x, int y){
        this.x = x;
        this.y = y;
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public double distance(Point p){
        double xDist = p.getX() - x;
        double yDist = p.getY() - y;
        return Math.sqrt(Math.pow(xDist,2) + Math.pow(yDist,2));
    }
    @Override
    public String toString(){
        return "X: " + x + " Y: " + y;
    }

    public boolean equalX(Point p){
        return p.x == x;
    }

    public boolean equalY(Point p){
        return p.y == y;
    }

    public boolean equals(Point p){
        return equalX(p) && equalY(p);
    }
}

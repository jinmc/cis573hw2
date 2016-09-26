package edu.upenn.cis573.hwk2;

import java.util.ArrayList;

public class Line {
    private Point start;
    private Point end;
    public Line(Point start, Point end){
        this.start = start;
        this.end = end;
    }
    public Point getStart(){
        return start;
    }
    public Point getEnd(){
        return end;
    }

    public boolean isParallel(Line l) {
        if (l == null || this == null) {
            return false;
        }
        // if they are parallel by vertically
        if (isXStartEqual(l.getStart()) && isXEndEqual(l.getEnd()) && isXVertical()) {
            return true;
        // if they are parallel by horizontally
        } else if (isYStartEqual(l.getStart()) && isYEndEqual(l.getEnd()) && isYHorizontal()) {
            return true;
        } else {
            return false;
        }

    }

    public boolean isXStartEqual(Point p) {
        if (this.getStart().equalX(p)) {
            return true;
        }
        return false;
    }

    public boolean isYStartEqual(Point p) {
        if (this.getStart().equalY(p)) {
            return true;
        }
        return false;
    }

    public boolean isXEndEqual(Point p) {
        if (this.getEnd().equalX(p)) {
            return true;
        }
        return false;
    }

    public boolean isYEndEqual(Point p) {
        if (this.getEnd().equalY(p)) {
            return true;
        }
        return false;
    }

    public boolean isYHorizontal() {
        if (this.getStart().getY() - this.getEnd().getY() != 0) {
            return true;
        }
        return false;
    }

    public boolean isXVertical() {
        if (this.getStart().getX() - this.getEnd().getX() != 0) {
            return true;
        }
        return false;
    }

    public int getXStart() {
        return this.getStart().getX();
    }

    public int getXEnd() {
        return this.getEnd().getX();
    }

    public int getYStart() {
        return this.getStart().getY();
    }

    public int getYEnd() {
        return this.getEnd().getY();
    }

    public boolean equals(Line l){
        return (start.equals(l.getStart()) && end.equals(l.getEnd()))
                || (end.equals(l.getStart()) && start.equals(l.getEnd()));
    }

//    Returns true if an ArrayList of lines contains this line
//    public boolean isContained(ArrayList<Line> lines){
//        for(Line l: lines){
//            if (this.equals(l)) return true;
//        }
//        return false;
//    }


    @Override
    public boolean equals(Object object)
    {
        boolean sameSame = false;

        if (object != null && object instanceof Line)
        {
            sameSame = this.equals(((Line) object));
        }

        return sameSame;
    }

    @Override
    public String toString(){
        return "start: " + start.toString() + " end: " + end.toString();
    }


}
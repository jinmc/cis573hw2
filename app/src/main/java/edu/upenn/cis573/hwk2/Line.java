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

    public boolean isParallel(Line l) {
        return true;
    }

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
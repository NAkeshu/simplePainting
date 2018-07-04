import java.awt.*;
import java.util.Vector;

class shape{
    public static final int ERROR_PARAMENT_NUM = 1;
    double x=-1,y=-1;
    double dx=0,dy=0;
    double p3,p4,p5;
    Color color;
    double penRadius;
    boolean firstTime = true;
    boolean isVisible = true;

    shape(){}

    public double getPenRadius() {
        return penRadius;
    }
    public Color getColor() {
        return color;
    }
    public double getX() {
        return x+dx;
    }
    public double getY() {
        return y+dy;
    }
    public double getP3() {
        return p3;
    }
    public double getP4() {
        return p4;
    }
    public double getP5() {
        return p5;
    }

    public void setPenRadius(double penRadius) {
        this.penRadius = penRadius;
    }
    public void setColor(Color color) {
        this.color = color;
    }
    public void addDeltaX(double deltax){
        dx+=deltax;
    }
    public void addDeltaY(double deltay){
        dy+=deltay;
    }
    public void addhistory() {
        single.num_Shape++;
        single.historyName.add("shape " + String.valueOf(single.num_Shape));
    }
    /*
    public void setX(double x) {
        this.x = x;
    }
    public void setY(double y) {
        this.y = y;
    }*/
    public void setP3(double p3) {
        this.p3 = p3;
    }
    public void setP4(double p4) {
        this.p4 = p4;
    }
    public void setP5(double p5) {
        this.p5 = p5;
    }

    public void deFirstTime(){firstTime = false;}

    public void drawWithDelta(){

    }

    public int draw(){return ERROR_PARAMENT_NUM;};
    public int draw(double x, double y){
        return ERROR_PARAMENT_NUM;
    };
    public int draw(double x,double y,double parament3){
        return ERROR_PARAMENT_NUM;
    };
    public int draw(double x, double y, double parament3, double parament4){
        return ERROR_PARAMENT_NUM;
    };
    public int draw(double x, double y, double parament3, double parament4, double parament5){
        return ERROR_PARAMENT_NUM;
    };
}
class Dpoint extends shape{
    public void drawWithDelta(){
        draw(x+dx,y+dy);
    }
    public int draw(double x, double y) {
        if(firstTime) {
            this.x = x;
            this.y = y;
            firstTime = false;
        }
        StdDrawD.point(x, y);
        return 0;
    };
}
class Dline extends shape{
    public void drawWithDelta(){
        draw(x+dx,y+dy,p3+dx,p4+dy);;
    }
    public int draw(double x, double y, double x1, double y1) {
        if(firstTime) {
            this.x = x;
            this.y = y;
            this.p3= x1;
            this.p4= y1;
        }
        StdDrawD.line(x,y,x1,y1);
        return 0;
    }
    protected int drawD(double deltax, double deltay) {
        draw(x+deltax,y+deltay,p3+deltax,p4+deltay);
        return 0;
    }
    public void addhistory() {
        single.num_Line++;
        single.historyName.add("Line " + String.valueOf(single.num_Line));
    }
}
class Dpencil extends shape{
    Vector<Dline> dlineVector = new Vector<Dline>();
    public void add(Dline dline){
        dline.deFirstTime();
        dlineVector.add(dline);
    }
    public void drawWithDelta() {
        int n=dlineVector.size();
        for(int i=0;i<n;i++){
            dlineVector.get(i).drawD(dx,dy);
        }
    }
    public void addhistory() {
        single.num_Pencil++;
        single.historyName.add("pencil " + String.valueOf(single.num_Pencil));
    }
}
class Dcircle extends shape{
    public void drawWithDelta(){
        draw(x+dx,y+dy,p3);
    }
    public int draw(double x, double y, double radius) {
        if(firstTime) {
            this.x = x;
            this.y = y;
            this.p3 = radius;
        }
        StdDrawD.circle(x,y,radius);
        return 0;
    }
    public void addhistory() {
        single.num_Circle++;
        single.historyName.add("Circle " + String.valueOf(single.num_Circle));
    }
}
class Dellipse extends shape{
    public void drawWithDelta(){
        draw(x+dx,y+dy,p3,p4);;
    }
    public int draw(double x, double y, double semiMajorAxis, double semiMinorAxis){
        if(firstTime) {
            this.x = x;
            this.y = y;
            this.p3 = semiMajorAxis;
            this.p4 = semiMinorAxis;
        }
        StdDrawD.ellipse(x,y,semiMajorAxis,semiMinorAxis);
        return 0;
    }
    public void addhistory() {
        single.num_Ellipse++;
        single.historyName.add("Ellipse " + String.valueOf(single.num_Ellipse));
    }
}
class Dsquare extends shape{
    public void drawWithDelta(){
        draw(x+dx,y+dy,p3);;
    }
    public int draw(double x, double y, double halfLength){
        if(firstTime) {
            this.x = x;
            this.y = y;
            this.p3 = halfLength;
        }
        StdDrawD.square(x,y,halfLength);
        return 0;
    }
    public void addhistory() {
        single.num_Square++;
        single.historyName.add("Square " + String.valueOf(single.num_Square));
    }
}
class Drectangle extends shape {
    public void drawWithDelta() {
        draw(x + dx, y + dy, p3, p4);
        ;
    }

    public int draw(double x, double y, double halfWidth, double halfHeight) {
        if (firstTime) {
            this.x = x;
            this.y = y;
            this.p3 = halfWidth;
            this.p4 = halfHeight;
        }
        StdDrawD.rectangle(x, y, halfWidth, halfHeight);
        return 0;
    }

    public void addhistory() {
        single.num_Rectangle++;
        single.historyName.add("Rectangle " + String.valueOf(single.num_Rectangle));
    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class single {

    static JFrame frame = new JFrame();
    //右侧的颜色选择器
    static JColorChooser chooser=new JColorChooser();

    static JButton btnFColorChoose = new JButton("选择前景色");
    static JPanel  panelFColorView = new JPanel();
    static JButton btnBColorChoose = new JButton("选择背景色");
    static JPanel  panelBColorView = new JPanel();
    static JButton btnClear        = new JButton("清空为背景色");

    static JLabel  lbPenStatue     = new JLabel("铅笔",SwingConstants.CENTER);
    static JLabel  lbPenRadius     = new JLabel("画笔粗细:0.0020",SwingConstants.CENTER);
    static JLabel  lbShapesNum     = new JLabel("0",SwingConstants.CENTER);

    static JButton btnPencil       = new JButton("铅笔");
    static JButton btnLine         = new JButton("直线");
    static JButton btnCircle       = new JButton("正圆");
    static JButton btnEllips       = new JButton("椭圆");
    static JButton btnSquare       = new JButton("正矩");
    static JButton btnRectangle    = new JButton("矩形");
    static JButton btnRadiusPlus   = new JButton("粗细+");
    static JButton btnRadiusDecr   = new JButton("粗细-");

    static JButton test1           = new JButton("重画");
    static JButton test2           = new JButton("清空");

    static JButton btnVisible      = new JButton("显示/隐藏");
    static JButton btnToRight      = new JButton("右移");
    static JButton btnToLeft       = new JButton("左移");
    static JButton btnToUp         = new JButton("上移");
    static JButton btnToDown       = new JButton("下移");

    //画笔状态常量
    public static final int pen_Pencil = 0;
    public static final int pen_Line = 1;
    public static final int pen_Circle = 2;
    public static final int pen_Ellipse = 3;
    public static final int pen_Square = 4;
    public static final int pen_Rectangle = 5;

    //记录shape画出的数量
    static int num_Shape = 0;
    static int num_Pencil = 0;
    static int num_Line = 0;
    static int num_Circle = 0;
    static int num_Ellipse = 0;
    static int num_Square = 0;
    static int num_Rectangle = 0;
    //初始选择的shape,用于JComboBox history
    static int chooseNum = 0;
    //选中会变色，变成红色这里存的是原本的颜色
    static Color historyColor = Color.black;
    //用来存当前可重画的shape的 向量组
    static Vector<shape> shapes = new Vector<shape>();
    //用来存JComboBox history的下拉栏的元素
    static Vector<String> historyName = new Vector<String>();
    //用来选择要操作的shape，从Vector shapes里选择
    static JComboBox history = new JComboBox(historyName);
    //当前正在画的shape在Vector shapes里的索引值
    static int indexUsing=0;
    //当前的画笔状态，默认为铅笔
    static int penStatue = pen_Pencil;
    //存的是前景色和背景色，前景色用来画图，背景色目前只能用来清空
    static Color frontColor,backgroundColor;
    //没用存在感的single
    single(){
        init();
    }
    //记录按下时鼠标的位置
    static double prePointX,prePointY;

    static void clear(Color color){
        StdDrawD.clear(color);
        //shapes.clear();
    }
    static void mouseMovedCall(double mouseX,double mouseY){
        /*switch (penStatue) {
            default:
                break;
        }*/
    }
    static void mousePressedCall(double mouseX,double mouseY){
        prePointX=mouseX;prePointY=mouseY;
        indexUsing = shapes.size();
        if(penStatue != pen_Pencil) {StdDrawD.showsave();}

        switch (penStatue){
            case pen_Pencil:/*----------------------*/
                StdDrawD.point(mouseX,mouseY);
                shapes.add(new Dpencil());break;
            case pen_Line:
                shapes.add(new Dline());break;
            case pen_Circle:
                shapes.add(new Dcircle());break;
            case pen_Ellipse:
                shapes.add(new Dellipse());break;
            case pen_Square:
                shapes.add(new Dsquare());break;
            case pen_Rectangle:
                shapes.add(new Drectangle());break;
            default:
                JOptionPane.showMessageDialog
                        (null,"错误：未知的画笔类型！"
                                ,"报错！",JOptionPane.ERROR_MESSAGE);
                System.exit(1);
        }//switch Over
        //为新添加的
        shapes.lastElement().setColor(StdDrawD.getPenColor());
        shapes.lastElement().setPenRadius(StdDrawD.getPenRadius());
        shapes.lastElement().addhistory();

    }

    static void mouseReleasedCall(){
        lbShapesNum.setText(Integer.toString(shapes.size()));
        shapes.get(indexUsing).deFirstTime();
        history.updateUI();
    }

    static void mouseDragCall(double preX,double preY,double mouseX,double mouseY){
        switch (penStatue){
            case pen_Pencil:
                Dpencil dpencil = (Dpencil)shapes.get(indexUsing);
                Dline dline = new Dline();
                dline.draw(preX,preY,mouseX,mouseY);
                dpencil.add(dline);
                break;
            case pen_Line:
                StdDrawD.showload();
                shapes.get(indexUsing).draw(prePointX,prePointY,mouseX,mouseY);
                break;
            case pen_Circle:
                StdDrawD.showload();
                shapes.get(indexUsing).draw(prePointX,prePointY
                        ,distance(prePointX,prePointY,mouseX,mouseY));
                break;
            case pen_Ellipse: // 椭圆 长短半轴
                StdDrawD.showload();
                shapes.get(indexUsing).draw((prePointX+mouseX)/2,(prePointY+mouseY)/2
                        ,Math.abs((prePointX-mouseX)/2)
                        ,Math.abs((prePointY-mouseY)/2));
                break;
            case pen_Square:
                StdDrawD.showload();
                shapes.get(indexUsing).draw((prePointX+mouseX)/2,(prePointY+mouseY)/2
                        ,Math.abs((prePointX-mouseX)/2));
                break;
            case pen_Rectangle:
                StdDrawD.showload();
                shapes.get(indexUsing).draw((prePointX+mouseX)/2,(prePointY+mouseY)/2
                        ,Math.abs((prePointX-mouseX)/2)
                        ,Math.abs((prePointY-mouseY)/2));
                break;
            default:
                StdDrawD.line(preX,preY,mouseX,mouseY);
                break;
        }
    }

    static double distance(double x0,double y0,double x1,double y1){
        double dx2 = x0-x1;dx2 *= dx2;
        double dy2 = y0-y1;dy2 *= dy2;
        return Math.sqrt(dx2+dy2);
    }

    static void redraw(){
        StdDrawD.showbackground();
        Color c = StdDrawD.getPenColor();
        double d = StdDrawD.getPenRadius();
        StdDrawD.enableDoubleBuffering();
        for (shape s:shapes) {
            if (s.isVisible == true) {
                StdDrawD.setPenColor(s.getColor());
                StdDrawD.setPenRadius(s.getPenRadius());
                s.drawWithDelta();
            }
        }
        StdDrawD.disableDoubleBuffering();
        StdDrawD.show();
        StdDrawD.setPenColor(c);
        StdDrawD.setPenRadius(d);
    }

    private static void changePenStatue(int statue) {
        penStatue = statue;
        String str;
        switch (statue){
            case pen_Pencil:str = "铅笔";break;
            case pen_Line:str = "直线";break;
            case pen_Circle:str = "正圆";break;
            case pen_Ellipse:str = "椭圆";break;
            case pen_Square:str = "正矩";break;
            case pen_Rectangle:str = "矩形";break;
            default: str = "铅笔";break;
        }
        lbPenStatue.setText(str);
    }

    private void initLabel(){
        lbPenStatue.setBounds(5,245,170,30);
        lbPenRadius.setBounds(5,285,170,30);
        lbShapesNum.setBounds(5,385,170,30);
        /*
        Font f1 = new Font("黑体",Font.BOLD,18);
        lbPenStatue.setFont(f1);
        lbPenRadius.setFont(f1);
        */
    }

    private void clearShapesNum(){
        num_Shape = 0;
        num_Pencil = 0;
        num_Line = 0;
        num_Circle = 0;
        num_Ellipse = 0;
        num_Square = 0;
        num_Rectangle = 0;

    }

    private void initButton(){
        btnFColorChoose.setBounds(5,5,100,30);
        btnFColorChoose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color color=chooser.showDialog(frame,"选取前景色",Color.lightGray );
                if (color==null)  //如果未选取
                    color=Color.black;  //则设置颜色为hei色
                panelFColorView.setBackground(color);  //改变面板的背景色
                StdDrawD.setPenColor(color);
            }
        });

        btnBColorChoose.setBounds(5,45,100,30);
        btnBColorChoose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color color=chooser.showDialog(frame,"选取背景色",Color.lightGray );
                if (color==null)  //如果未选取
                    color=Color.white;  //则设置颜色为bai色
                panelBColorView.setBackground(color);  //改变面板的背景色
                backgroundColor = color;
            }
        });

        btnClear.setBounds(5,85,175,30);
        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clear(backgroundColor);
            }
        });

        btnPencil.setBounds(5,125,80,30);
        btnPencil.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changePenStatue(pen_Pencil);
            }
        });

        btnLine.setBounds(100,125,80,30);
        btnLine.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changePenStatue(pen_Line);
            }
        });

        btnCircle.setBounds(100,165,80,30);
        btnCircle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changePenStatue(pen_Circle);
            }
        });

        btnEllips.setBounds(5,165,80,30);
        btnEllips.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changePenStatue(pen_Ellipse);
            }
        });

        btnSquare.setBounds(100,205,80,30);
        btnSquare.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changePenStatue(pen_Square);
            }
        });

        btnRectangle.setBounds(5,205,80,30);
        btnRectangle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changePenStatue(pen_Rectangle);
            }
        });

        btnRadiusPlus.setBounds(5,325,80,30);
        btnRadiusPlus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double d = StdDrawD.getPenRadius()+0.005;
                if(d>1)d=1;if(d<0.001)d=0.001;
                StdDrawD.setPenRadius(d);
                lbPenRadius.setText("画笔粗细:"+String.format("%.4f",d));
            }
        });

        btnRadiusDecr.setBounds(100,325,80,30);
        btnRadiusDecr.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double d = StdDrawD.getPenRadius()-0.005;
                if(d>1)d=1;if(d<0.001)d=0.001;
                StdDrawD.setPenRadius(d);
                lbPenRadius.setText("画笔粗细:"+String.format("%.4f",d));
            }
        });

        test1.setBounds(5,470,80,30);
        test1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //StdDrawD.showsave();
                redraw();
            }
        });

        test2.setBounds(100,470,80,30);
        test2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //StdDrawD.showload();
                shapes.clear();
                lbShapesNum.setText(Integer.toString(shapes.size()));
                historyName.clear();
                clearShapesNum();
                chooseNum = 0;
                historyColor = Color.black;
                //history.setSelectedIndex(0);
            }
        });

        btnVisible.setBounds(5, 640, 180, 30);
        btnVisible.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (shapes.get(chooseNum).isVisible == true) {
                    shapes.get(chooseNum).isVisible = false;
                }
                else {
                    shapes.get(chooseNum).isVisible = true;
                }
                redraw();
            }
        });

        btnToUp.setBounds(65, 570, 60, 30);
        btnToUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                shapes.get(chooseNum).addDeltaY(5);
                redraw();
            }
        });

        btnToLeft.setBounds(5, 605, 60, 30);
        btnToLeft.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                shapes.get(chooseNum).addDeltaX(-5);
                redraw();
            }
        });

        btnToDown.setBounds(65, 605, 60, 30);
        btnToDown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                shapes.get(chooseNum).addDeltaY(-5);
                redraw();
            }
        });

        btnToRight.setBounds(125, 605, 60, 30);
        btnToRight.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                shapes.get(chooseNum).addDeltaX(5);
                redraw();
            }
        });

    }

    private void initPanel(){
        panelFColorView.setBounds(115,5,65,30);
        panelFColorView.setBackground(frontColor);

        panelBColorView.setBounds(115,45,65,30);
        panelBColorView.setBackground(backgroundColor);
    }

    private void initComboBox() {
        history.setBounds(5,525,175,30);
        history.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                history.updateUI();//
                int historyNum = ((JComboBox)e.getSource()).getSelectedIndex();
                if (chooseNum != historyNum) { // 选了新的shape
                    shapes.get(chooseNum).setColor(historyColor);
                    historyColor = shapes.get(historyNum).getColor();
                    shapes.get(historyNum).setColor(Color.red);
                    chooseNum = historyNum;
                    redraw();
                }
                else { // 选择重复的shape
                    shapes.get(chooseNum).setColor(new Color(255, 0, 0));
                    redraw();
                }
            }
        });
    }

    private void init()  {
        shapes.clear();
        frontColor = StdDrawD.getPenColor();
        backgroundColor = Color.WHITE;

        StdDrawD.setPenRadius(0.002);

        initButton();
        frame.add(btnFColorChoose);
        frame.add(btnBColorChoose);
        frame.add(btnClear);
        frame.add(btnPencil);
        frame.add(btnLine);
        frame.add(btnEllips);
        frame.add(btnCircle);
        frame.add(btnSquare);
        frame.add(btnRectangle);
        frame.add(btnRadiusDecr);
        frame.add(btnRadiusPlus);
        frame.add(test1);
        frame.add(test2);
        frame.add(btnVisible);
        frame.add(btnToUp);
        frame.add(btnToLeft);
        frame.add(btnToRight);
        frame.add(btnToDown);

        initComboBox();
        frame.add(history);

        initPanel();
        frame.add(panelFColorView);
        frame.add(panelBColorView);

        initLabel();
        frame.add(lbPenStatue);
        frame.add(lbPenRadius);
        frame.add(lbShapesNum);

        frame.setLayout(null);
        frame.setBounds(512,0,200,512+56);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setVisible(true);
    }

    public static void main(String args[]) {
        StdDrawD.setCanvasSize(800,800);
        StdDrawD.setXscale(0.0,800.0);
        StdDrawD.setYscale(0.0,800.0);
        new single();
        frame.setBounds(800+10,0,200,800+64);

        StdDrawD.clear();


    }
}

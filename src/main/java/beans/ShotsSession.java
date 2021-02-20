package beans;

import lombok.Getter;
import lombok.Setter;

import javax.annotation.ManagedBean;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@ManagedBean
@SessionScoped
public class ShotsSession implements Serializable {


    private int listNumber = 1;
    private int maxAvailablePage = 1;
    private int listSize = 12;

    private ArrayList<Shot> shotsList = new ArrayList<Shot>();

    private ArrayList<Shot> shotsViewList = new ArrayList<Shot>(listSize);

    private double x;
    private double y;
    private double r = 1.0d;

    @Getter
    @Setter
    private boolean y1, y2, y3, y4, y5, y6;

    @Getter
    @Setter
    private boolean r1 = true, r2, r3, r4, r5;

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getR() {
        return r;
    }

    public void setR(double r) {
        r1 = r2 = r3 = r4 = r5 = false;

        if(r == 1d) r1 = true;
        if(r == 1.5d) r2 = true;
        if(r == 2d) r3 = true;
        if(r == 2.5d) r4 = true;
        if(r == 3d) r5 = true;

        this.r = r;
    }



    public int getListNumber() {
        return listNumber;
    }

    public void setListNumber(int listNumber) {

        System.out.println("Setting: " + listNumber);

        if (listNumber > maxAvailablePage) {
            System.out.println("Out range list number!");
            return;
        }

        if (listNumber < 1) {
            listNumber = 1;
        }

        int size = listSize * (listNumber - 1);
        shotsViewList.clear();
        for (int i = 0; i < listSize; i++) {

            if (i + size < shotsList.size()) {
                shotsViewList.add(shotsList.get(i + size));
            } else {
                break;
            }
        }
        this.listNumber = listNumber;
    }

    public ArrayList<Shot> getShotsViewList() {
        return shotsViewList;
    }

    public void setShotsViewList(ArrayList<Shot> shotsList) {
        this.shotsViewList = shotsList;
    }

    public ArrayList<Shot> getShotsList() {
        return shotsList;
    }

    public void addShotGraph(String id) {
        Shot shot = new Shot();

        if(validateValue()) {
            shot.setX(x);
            shot.setY(y);
            shot.setR(r);

            shot.calculateShot();
            if (!saveShot(shot, id)) {
                System.out.println("Exception with saving in data base with shot: " + "\n" +
                        "   X: " + shot.getX() + "\n" +
                        "   Y: " + shot.getY() + "\n" +
                        "   R: " + shot.getR() + "\n" +
                        "   GR: " + shot.isGR() + "\n" +
                        "   Start: " + shot.getStart() + "\n" +
                        "   Script time: " + shot.getScriptTime() + "\n"
                );
            }

        }


    }

    private boolean validateValue() {
        boolean xCheck = x >= -3d && x <= 3d;
        boolean yCheck = y >= -2d && y <= 0.5d;

        return xCheck && yCheck;
    }


    private boolean saveShot(Shot shot, String id) {
        shotsList.add(0, shot);
        if (shotsList.size() % listSize == 1 && shotsList.size() != 1) {
            maxAvailablePage++;
        }
        setListNumber(getListNumber());

        //return true;
        return ShotsStorage.addShot(shot, id);
    }




    private static String head = "<div><svg id=\"image-coordinates\"  xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 300 300\" stroke=\"black\" fill=\"none\" strokeWidth=\"1\" strokeLinecap=\"round\" strokeLinejoin=\"round\">\n" +
            "<path d=\"M150 150 v120 h120 v-120 h-60 m0 0 l-60 -60 v-60 A120,120,0,0,0, 30 150 h120 z\"/>\n" +
            "<path d=\"M0,150 h30 v3,-6,3 h60 v3,-6,3 h60 v-60 h3,-6,3 v-60 h3,-6,3 v-30 m0,0 l3,6,-3,-6,-3,6,3,-6 v150 h60 v3,-6,3 h60 v3,-6,3 h30 m0,0 l-6,3,6,-3,-6,-3,6,3 h-150 v60 h3,-6,3 v60 h3,-6,3 v30 v-150 h-150 z\"/>\n" +
            "\n" +
            "<text x=\"355\" y=\"220\" strokeWidth=\"0\" fill=\"#757692\">R</text>\n";

    private static String foot = "</svg>\n" +
            "</div>";

    public String printShots() {
        StringBuilder strBuild = new StringBuilder();
        ArrayList<Shot> shots = shotsList;
        double r = getR();

        strBuild.append(head);

        if (shots != null && shots.size() != 0) {
            shots.stream().map(shot -> {
                double x = shot.getX();
                double y = shot.getY();

                if (shot.isGR()) {
                    return (
                            "<circle cx=\"" +
                                    (150 + 120 * (x / r)) +
                                    "\" cy=\"" +
                                    (150 - 120 * (y / r)) +
                                    "\" r=\"3\" fill=\"rgb(0,255,0)\" stroke-width=\"1\"\n stroke=\"rgb(0,0,0)\"/>"
                    );
                } else {
                    return (
                            "<circle cx=\"" +
                                    (150 + 120 * (x / r)) +
                                    "\" cy=\"" +
                                    (150 - 120 * (y / r)) +
                                    "\" r=\"3\" fill=\"rgb(255,0,0)\" stroke-width=\"1\"\n stroke=\"rgb(0,0,0)\"/>"
                    );
                }

            }).forEachOrdered(str -> strBuild.append(str));
        }

        strBuild.append(foot);

        return strBuild.toString();
    }

}
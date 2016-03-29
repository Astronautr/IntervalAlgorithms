package ru.nsc.globalopt.kenai;

import com.kenai.jinterval.ClassicRealInterval;
import com.kenai.jinterval.ClassicRealIntervalFactory;
import java.util.Comparator;
import java.util.PriorityQueue;

public class IAOGO_int {
    PriorityQueue<ListElemInt> wList = new PriorityQueue<ListElemInt>(new AssessmentCompInt()); //is used for storage objects of ListElem

    private ClassicRealIntervalFactory factory;
    private ClassicRealInterval c_1, c_2;
    
    private void initConstants(ClassicRealIntervalFactory factory) {
        this.factory = factory;
        c_1 = factory.valueOf(1, 1);
        c_2 = factory.valueOf(2, 2);
    }
    
    public ClassicRealInterval objectiveFunction(ClassicRealInterval[] origin) { // origin = [a b theta]
        // returning value is a^2 + (b+1)^2 - 2*a * (b+1) * sin(theta)
        //if you want to find max of function - just use .neg() to whole expression, and take off minus from result
        ClassicRealInterval a = origin[0];
        ClassicRealInterval b = origin[1];
        ClassicRealInterval theta = origin[2];
        return a.sqr()
                .add(b.add(c_1).sqr())
                .subtract(c_2.multiply(a).multiply(b.add(c_1)).multiply(theta.sin()));
    }

    public void IntFunc() {
        ClassicRealInterval[] first = wList.peek().getData().clone();
        ClassicRealInterval[] second = first.clone();
        //we initialize two clones of box, which stores in leading element of the list
        //next step - find the widest component of box
        int j = 0;
        double max = first[0].doubleWid();
        for (int i = 1; i < first.length; i++)
            if (first[i].doubleWid() > max) {
                max = first[i].doubleWid();
                j = i;
            }
        //now we bisect each clone by the widest component, first - from lower boundary to middle, second - from middle to upper boundary
        first[j] = factory.valueOf(first[j].doubleInf(),first[j].doubleMid());
        second[j] = factory.valueOf(second[j].doubleMid(),second[j].doubleSup());
        //here we create two elements of list with corresponding ranges of objective function and mins
        ListElemInt firstel = new ListElemInt(first, objectiveFunction(first).doubleInf());
        ListElemInt secondel = new ListElemInt(second, objectiveFunction(second).doubleInf());
        //further - remove leading element
        wList.poll();
        //and add new two with automatic collation, provided by PriorityQueue collection
        wList.add(firstel);
        wList.add(secondel);
    }

    public void start(ClassicRealInterval[] box, double tolerance) {
        initConstants(box[0].getFactory_());
        ClassicRealInterval intArea = objectiveFunction(box); //now we have defined range of objective function
        double intwid = intArea.doubleWid(); //width of range will be used for stopping criterion
        ListElemInt wrk = new ListElemInt(box,intArea.doubleInf());
        //at this step we initialized new element of Our List, it's consist of domain of objective function and the min of range of objective function
        wList.add(wrk);
        // when new element adds to the list, it's automatically compare with other elements of list by second field (min), and sorts in ascending order
        while (intwid >= tolerance) {
            IntFunc(); // check IntFunc() for detailed description
            intwid = objectiveFunction(wList.peek().getData()).doubleWid(); //here we take the min of leading element of the List
        }
    }
}

class AssessmentCompInt implements Comparator<ListElemInt> {
    public int compare(ListElemInt el1, ListElemInt el2) {
        return Double.valueOf(el1.getAssessment()).compareTo(el2.getAssessment());
    }
}

class ListElemInt implements Comparable<ListElemInt> {

    private ClassicRealInterval[] data; //here we store domain of objective function
    private double assessment; //this field represents infumum of range of objective function for ListElem

    ListElemInt() {
        data = null;//new DoubleInterval[];
        assessment = 0;
    }

    ListElemInt(ClassicRealInterval[] data, double assessment) {
        this.data = data;
        this.assessment = assessment;
    }

    public double getAssessment() {
        return assessment;
    }
    public void setAssessment(double assessment) {
        this.assessment = assessment;
    }
    public ClassicRealInterval[] getData() {
        return this.data;
    }
    public void setData(ClassicRealInterval[] data) {
        this.data = data;
    }
    public int compareTo(ListElemInt el) { //at this place and at strings 55-59 we redefined comparator for comparison ListElem objects by "assessment" field
        return Double.valueOf(this.assessment).compareTo(Double.valueOf(el.assessment));
    }
}

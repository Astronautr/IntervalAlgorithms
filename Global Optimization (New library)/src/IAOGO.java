import net.java.jinterval.interval.set.SetInterval;
import net.java.jinterval.interval.set.SetIntervalContext;

import java.util.Comparator;
import java.util.PriorityQueue;

public class IAOGO {
    PriorityQueue<ListElem> wList = new PriorityQueue<ListElem>(new AssessmentComp()); //is used for storage objects of ListElem
    private SetIntervalContext ic;

    private SetInterval c_1, c_2;
    
    private void initConstants(SetIntervalContext ic) {
        this.ic = ic;
        c_1 = ic.numsToInterval(1, 1);
        c_2 = ic.numsToInterval(2, 2);
    }
    
    public SetInterval objectiveFunction(SetInterval[] origin) { // origin = [a b theta]
        // returning value is a^2 + (b+1)^2 - 2*a * (b+1) * sin(theta)
        //if you want to find max of function - just use ic.neg() to whole expression, and take off minus from result
        SetInterval a = origin[0];
        SetInterval b = origin[1];
        SetInterval theta = origin[2];
        return ic.sub(ic.add(ic.sqr(a), ic.sqr(ic.add(b, c_1))),
                ic.mul(ic.mul(ic.mul(c_2, a), ic.add(b, c_1)), ic.sin(theta)));
    }

    public void IntFunc() {
        SetInterval[] first = wList.peek().getData().clone();
        SetInterval[] second = first.clone();
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
        first[j] = ic.numsToInterval(first[j].doubleInf(),first[j].doubleMid());
        second[j] = ic.numsToInterval(second[j].doubleMid(),second[j].doubleSup());
        //here we create two elements of list with corresponding ranges of objective function and mins
        ListElem firstel = new ListElem(first, objectiveFunction(first).doubleInf());
        ListElem secondel = new ListElem(second, objectiveFunction(second).doubleInf());
        //further - remove leading element
        wList.poll();
        //and add new two with automatic collation, provided by PriorityQueue collection
        wList.add(firstel);
        wList.add(secondel);
    }

    public void start(SetInterval[] box, double tolerance, SetIntervalContext ic) {
        initConstants(ic); //define the SetIntervalContext for following computing
        SetInterval intArea = objectiveFunction(box); //now we have defined range of objective function
        double intwid = intArea.doubleWid(); //width of range will be used for stopping criterion
        ListElem wrk = new ListElem(box,intArea.doubleInf());
        //at this step we initialized new element of Our List, it's consist of domain of objective function and the min of range of objective function
        wList.add(wrk);
        // when new element adds to the list, it's automatically compare with other elements of list by second field (min), and sorts in ascending order
        while (intwid >= tolerance) {
            IntFunc(); // check IntFunc() for detailed description
            intwid = objectiveFunction(wList.peek().getData()).doubleWid(); //here we take the min of leading element of the List
        }
    }
}

class AssessmentComp implements Comparator<ListElem> {
    public int compare(ListElem el1, ListElem el2) {
        return Double.valueOf(el1.getAssessment()).compareTo(el2.getAssessment());
    }
}

class ListElem implements Comparable<ListElem> {

    private SetInterval[] data; //here we store domain of objective function
    private double assessment; //this field represents infumum of range of objective function for ListElem

    ListElem() {
        data = null;
        assessment = 0;
    }

    ListElem(SetInterval[] data, double assessment) {
        this.data = data;
        this.assessment = assessment;
    }

    public double getAssessment() {
        return assessment;
    }
    public void setAssessment(double assessment) {
        this.assessment = assessment;
    }
    public SetInterval[] getData() {
        return this.data;
    }
    public void setData(SetInterval[] data) {
        this.data = data;
    }
    public int compareTo(ListElem el) { //at this place and at strings 58-62 we redefined comparator for comparison ListElem objects by "assessment" field
        return Double.valueOf(this.assessment).compareTo(Double.valueOf(el.assessment));
    }
}

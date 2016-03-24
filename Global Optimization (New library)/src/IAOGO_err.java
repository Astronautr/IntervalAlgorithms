import net.java.jinterval.interval.set.SetInterval;
import net.java.jinterval.interval.set.SetIntervalContext;

import java.util.Comparator;
import java.util.PriorityQueue;
import net.java.jinterval.rational.BinaryValueSet;
import net.java.jinterval.rational.Rational;

public class IAOGO_err {
    PriorityQueue<ListElem> wList = new PriorityQueue<ListElem>(new AssessmentComp()); //is used for storage objects of ListElem
    private SetIntervalContext ic;
    private double compensationInf, compensationSup;
    private long boxCount;
    private long maxQueueSize = 0;

    private static String p(SetInterval s) {
        return "[" + s.doubleInf() + "," + s.doubleSup() + "]";
    }
    
    private void initConstants(SetIntervalContext ic, SetInterval[] box) {
        this.ic = ic;
        ErrorBound.Factory factory = ErrorBound.createFactory(ic, BinaryValueSet.BINARY64, box);
        
        ErrorBound c_1 = factory.num(Rational.valueOf(1));
        ErrorBound c_2 = factory.num(Rational.valueOf(2));
        ErrorBound a = factory.getInp(0);
        ErrorBound b = factory.getInp(1);
        ErrorBound thetaB = factory.getInp(2);
        ErrorBound err = a.sqr()
                .add(b.add(c_1).sqr())
                .add(c_2.mul(a).mul(b.add(c_1)).mul(thetaB.sin()));
        SetInterval compensation = err.getCompensation();
        compensationInf = compensation.doubleInf();
        compensationSup = compensation.doubleSup();
        boxCount = 0;
        maxQueueSize = 0;
    }
    
    public SetInterval objectiveFunction(SetInterval[] origin) { // origin = [a b theta]
        boxCount++;
        maxQueueSize = Math.max(maxQueueSize, wList.size());
        // returning value is a^2 + (b+1)^2 - 2*a * (b+1) * sin(theta)
        //if you want to find max of function - just use ic.neg() to whole expression, and take off minus from result
        double ai = origin[0].doubleInf();
        double as = origin[0].doubleSup();
        double bi = origin[1].doubleInf();
        double bs = origin[1].doubleSup();
        double thetai = origin[2].doubleInf();
        double thetas = origin[2].doubleSup();
        double bi1 = bi + 1;
        double bs1 = bs + 1;
        double inf = ai*ai + bi1*bi1 - 2*as*bs1*Math.sin(thetas) + compensationInf;
        double sup = as*as + bs1*bs1 - 2*ai*bi1*Math.sin(thetai) + compensationSup;
        return ic.numsToInterval(inf, sup);
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
        initConstants(ic, box); //define the SetIntervalContext for following computing
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
//        System.out.println("boxCount=" + boxCount+ " maxQueueSize=" + maxQueueSize);
    }
}

class AssessmentCompErr implements Comparator<ListElemErr> {
    public int compare(ListElemErr el1, ListElemErr el2) {
        return Double.valueOf(el1.getAssessment()).compareTo(el2.getAssessment());
    }
}

class ListElemErr implements Comparable<ListElemErr> {

    private SetInterval[] data; //here we store domain of objective function
    private double assessment; //this field represents infumum of range of objective function for ListElem

    ListElemErr() {
        data = null;
        assessment = 0;
    }

    ListElemErr(SetInterval[] data, double assessment) {
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
    public int compareTo(ListElemErr el) { //at this place and at strings 58-62 we redefined comparator for comparison ListElem objects by "assessment" field
        return Double.valueOf(this.assessment).compareTo(Double.valueOf(el.assessment));
    }
}

package ru.nsc.interval.globalopt.v2;

import net.java.jinterval.interval.set.SetInterval;
import net.java.jinterval.interval.set.SetIntervalContext;
import net.java.jinterval.rational.ExtendedRational;
import net.java.jinterval.rational.ExtendedRationalContext;
import net.java.jinterval.rational.ExtendedRationalContexts;

import java.util.Comparator;
import java.util.PriorityQueue;


public class IAOGO {
    static PriorityQueue<ListElem> wList = new PriorityQueue<ListElem>(new AssessmentComp());
    private static SetIntervalContext ic;

    static SetInterval scMul(SetInterval[] X, SetInterval[] Y) {
        SetInterval result = ic.numsToInterval(0, 0);
        for (int i = 0; i < X.length; i++) {
            result = ic.add(result, ic.mul(X[i], Y[i]));
        }
        return result;
    }

    static void initElem(Gradient[] box) throws Exception {
        {
            Gradient[] dependentBox = new Gradient[Functions.class.getDeclaredMethods().length];
            for (int i = 0; i < dependentBox.length; i++) {
                String str = String.format("area%s", i);
                dependentBox[i] = (Gradient) Functions.class.getDeclaredMethod(str, new Class[]{Gradient[].class, Gradient[].class}).
                        invoke(null, new Object[]{box, dependentBox});
            }
            Gradient intvalArea = ObjectiveFunction.areaValues(box, dependentBox);
            SetInterval[] xc = new SetInterval[box.length];
            for (int i = 0; i < xc.length; i++) {
                xc[i] = ic.numsToInterval(box[i].getX().mid(), box[i].getX().mid());
            }
            Gradient[] grXC = Gradient.init(xc, ic);

            Gradient[] dependentGrXC = new Gradient[dependentBox.length];
            for (int i = 0; i < dependentGrXC.length; i++) {
                String str = String.format("area%s", i);
                dependentGrXC[i] = (Gradient) Functions.class.getDeclaredMethod(str, new Class[]{Gradient[].class, Gradient[].class}).
                        invoke(null, new Object[]{grXC, dependentGrXC});
            }

            Gradient intvalXCArea = ObjectiveFunction.areaValues(grXC, dependentGrXC);

            SetInterval[] xBiasC = new SetInterval[box.length];
            for (int i = 0; i < xBiasC.length; i++) {
                xBiasC[i] = ic.sub(box[i].getX(), xc[i]);
            }
            SetInterval fmv = ic.add(intvalXCArea.getX(), scMul(intvalArea.getDX(), xBiasC));
            fmv = ic.intersection(fmv, intvalArea.getX());
            for (int i = 0; i < box.length; i++) {
                if (intvalArea.getDX()[i].inf().ge(ExtendedRational.valueOf(0))) {
                    box[i].intersectionX(ic.numsToInterval(box[i].getX().inf(),box[i].getX().inf()));
                }
                else
                    if (intvalArea.getDX()[i].sup().le(ExtendedRational.valueOf(0))) {
                        box[i].intersectionX(ic.numsToInterval(box[i].getX().sup(),box[i].getX().sup()));
                    }
                    else
                        if (ic.intersection(intvalArea.getDX()[i],ic.numsToInterval(0,0)).isEmpty()) {
                            return;
                        }
            }
            wList.add(new ListElem(box, fmv.inf(), fmv.wid()));
        }
    }

    ExtendedRational start(Gradient[] box, ExtendedRational tolerance, SetIntervalContext ic) throws Exception {
        {
            IAOGO.ic = ic;
            initElem(box);

            while (wList.peek().getWid().ge(tolerance)) {
                int max = 0;
                box = wList.poll().getData();
                for (int i = 1; i < box.length; i++) {
                    if (box[max].getX().wid().lt(box[i].getX().wid()))
                        max = i;
                }
                Gradient[] first = box.clone();
                Gradient[] second = box.clone();
                first[max] = first[max].intersectionX(ic.numsToInterval(first[max].getX().inf(),first[max].getX().mid()));
                second[max] = second[max].intersectionX(ic.numsToInterval(second[max].getX().mid(),second[max].getX().sup()));
                initElem(first);
                initElem(second);
            }
            return wList.peek().getAssessment();

            //System out.....
            /*ExtendedRationalContext erc = ExtendedRationalContexts.exact();
            ExtendedRational min = wList.peek().getAssessment();
            System.out.printf("%.18f\t",min.doubleValue());
            Gradient[] wrkBox = wList.poll().getData();
            for (int i = 0; i < wrkBox.length; i++) {
                System.out.printf("[%.18f, %.18f]\t",wrkBox[i].getX().doubleInf(), wrkBox[i].getX().doubleSup());
            }
            System.out.println();
            if (wList.isEmpty())
                return min;
            else {
                ExtendedRational wrkMin= wList.peek().getAssessment();
                while (erc.sub(wrkMin,min).le(tolerance)) {
                    System.out.printf("%.18f\t",wrkMin.doubleValue());
                    wrkBox = wList.poll().getData();
                    for (int i = 0; i < wrkBox.length; i++) {
                        System.out.printf("[%.18f, %.18f]\t",wrkBox[i].getX().doubleInf(), wrkBox[i].getX().doubleSup());
                    }
                    System.out.println();
                    if (wList.isEmpty())
                        return min;
                    else
                        wrkMin= wList.peek().getAssessment();
                }
                return min;*/
            //}
        }
    }
}

class AssessmentComp implements Comparator<ListElem> {
    public int compare(ListElem el1, ListElem el2) {
        return ExtendedRational.valueOf(el1.getAssessment()).compareTo(el2.getAssessment());
    }
}

class ListElem implements Comparable<ListElem> {

    private Gradient[] data;
    private ExtendedRational assessment;
    private ExtendedRational wid;

    ListElem() {
        data = null;
        assessment = ExtendedRational.valueOf(0.0);
        wid = ExtendedRational.valueOf(0.0);
    }

    ListElem(Gradient[] data, ExtendedRational assessment, ExtendedRational wid) {
        this.data = data;
        this.assessment = assessment;
        this.wid = wid;
    }

    public ExtendedRational getAssessment() {
        return assessment;
    }
    public void setAssessment(ExtendedRational assessment) {
        this.assessment = assessment;
    }
    public ExtendedRational getWid() {
        return wid;
    }
    public Gradient[] getData() {
        return this.data;
    }
    public void setData(Gradient[] data) {
        this.data = data;
    }
    public int compareTo(ListElem el) {
        return ExtendedRational.valueOf(this.assessment).compareTo(ExtendedRational.valueOf(el.assessment));
    }
}

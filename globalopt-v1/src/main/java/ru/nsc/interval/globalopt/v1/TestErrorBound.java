package ru.nsc.interval.globalopt.v1;

import net.java.jinterval.interval.set.SetInterval;
import net.java.jinterval.interval.set.SetIntervalContext;
import net.java.jinterval.interval.set.SetIntervalContexts;
import net.java.jinterval.rational.BinaryValueSet;
import net.java.jinterval.rational.Rational;

/**
 *
 */
public class TestErrorBound {

    private static String p(SetInterval s) {
        return "[" + s.doubleInf() + "," + s.doubleSup() + "]";
    }
    
    private static void test(String msg, BinaryValueSet valueSet) {
        SetIntervalContext ic = SetIntervalContexts.getInfSup(BinaryValueSet.BINARY128);
        SetInterval d = ic.numsToInterval(15.2, 34.8);
        SetInterval X = ic.numsToInterval(100, 100);
        SetInterval L = ic.numsToInterval(19.1, 55.2);
        SetInterval theta = ic.numsToInterval(0, 1.18);
        //Here we wanted to initialize interval box = [ a b theta]
        //, where a = 2*L/d, b = 2*X/d
        SetInterval box[] = {ic.div(ic.mul(ic.numsToInterval(2, 2), L), d), ic.div(ic.numsToInterval(200, 200), d), theta};
        ErrorBound.Factory factory = ErrorBound.createFactory(ic, valueSet, box);
        
        ErrorBound c_1 = factory.num(Rational.valueOf(1));
        ErrorBound c_2 = factory.num(Rational.valueOf(2));
        ErrorBound a = factory.getInp(0);
        ErrorBound b = factory.getInp(1);
        ErrorBound thetaB = factory.getInp(2);
        ErrorBound errB = a.sqr()
                .add(b.add(c_1).sqr())
                .add(c_2.mul(a).mul(b.add(c_1)).mul(thetaB.sin()));
        SetInterval compensation = errB.getCompensation();
        System.out.println(msg + " range=" + p(errB.getRange()) + " err=" + p(errB.getErr())
                + " comp=" + p(compensation));
    }

    public static void main(String[] args) {
        test("b16", BinaryValueSet.BINARY16);
        test("b32", BinaryValueSet.BINARY32);
        test("b64", BinaryValueSet.BINARY64);
        test("b80", BinaryValueSet.BINARY80);
        test("b128", BinaryValueSet.BINARY128);
    }

}

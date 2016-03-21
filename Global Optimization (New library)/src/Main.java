
import net.java.jinterval.interval.set.SetInterval;
import net.java.jinterval.interval.set.SetIntervalContext;
import net.java.jinterval.interval.set.SetIntervalContexts;
import net.java.jinterval.rational.BinaryValueSet;

/**
 *
 */
public class Main {

    private static void test(String msg, SetIntervalContext ic, double... tolerances) {
        System.out.println(msg);
        for (double tolerance : tolerances) {
            long startTime = System.currentTimeMillis();
            SetInterval d = ic.numsToInterval(15.2, 34.8);
            SetInterval X = ic.numsToInterval(100, 100);
            SetInterval L = ic.numsToInterval(19.1, 55.2);
            SetInterval theta = ic.numsToInterval(0, 1.18);
            //Here we wanted to initialize interval box = [ a b theta]
            //, where a = 2*L/d, b = 2*X/d
            SetInterval box[] = {ic.div(ic.mul(ic.numsToInterval(2, 2), L), d), ic.div(ic.numsToInterval(200, 200), d), theta};
            IAOGO algorithm = new IAOGO();
            //Now we'll launch basic version of interval algorithm of global optimization for chosen objective function,
            //interval box, tolerance and Context (ic)
            algorithm.start(box, tolerance, ic);
            long stopTime = System.currentTimeMillis();
            System.out.println("tol=" + tolerance + " res=" + algorithm.wList.peek().getAssessment() + " time=" + (stopTime - startTime) / 1E3 + "s");
        }
    }

    public static void main(String[] args) {
        switch (7) {
            case 1:
                test("getPlain()", SetIntervalContexts.getPlain(), 1E-3, 1E-4, 1E-5, 1E-6, 1E-7);
                break;
            case 2:
                test("getAccur64()", SetIntervalContexts.getAccur64(), 1E-3, 1E-4, 1E-5, 1E-6, 1E-7);
                break;
            case 3:
                test("getDoubleNearest()", SetIntervalContexts.getDoubleNearest(), 1E-3, 1E-4, 1E-5);
                break;
            case 4:
                test("getInfSup(BinaryValueSet.BINARY32)", SetIntervalContexts.getInfSup(BinaryValueSet.BINARY32), 1E-3, 1E-4);
                break;
            case 5:
                test("getInfSup(BinaryValueSet.BINARY64)", SetIntervalContexts.getInfSup(BinaryValueSet.BINARY64), 1E-3, 1E-4, 1E-5);
                break;
            case 6:
                test("getInfSup(BinaryValueSet.BINARY80)", SetIntervalContexts.getInfSup(BinaryValueSet.BINARY80), 1E-3, 1E-4, 1E-5);
                break;
            case 7:
                test("getInfSup(BinaryValueSet.BINARY128)", SetIntervalContexts.getInfSup(BinaryValueSet.BINARY128), 1E-3, 1E-4, 1E-5);
                break;
        }
    }
}
/*
getPlain()
tol=0.001 res=6.605350712421185 time=0.348s
tol=1.0E-4 res=6.605631967925461 time=0.97s
tol=1.0E-5 res=6.605658079647924 time=3.391s
tol=1.0E-6 res=6.605660197332313 time=13.963s
tol=1.0E-7 res=6.605660471995435 time=59.798s

getAccur64()
tol=0.001 res=6.605350712421142 time=0.452s
tol=1.0E-4 res=6.605631967925433 time=1.316s
tol=1.0E-5 res=6.605658079647881 time=5.193s
tol=1.0E-6 res=6.605660197332298 time=12.198s
tol=1.0E-7 res=6.605660471995392 time=64.552s

getDoubleNearest()
tol=0.001 res=6.605350712421185 time=7.383s
tol=1.0E-4 res=6.605631967925461 time=22.374s
tol=1.0E-5 res=6.605658079647924 time=78.629s

getInfSup(BinaryValueSet.BINARY32)
tol=0.001 res=6.605316162109375 time=13.715s
tol=1.0E-4 res=6.605613708496094 time=49.812s

getInfSup(BinaryValueSet.BINARY64)
tol=0.001 res=6.605350712421142 time=13.848s
tol=1.0E-4 res=6.605631967925433 time=43.487s
tol=1.0E-5 res=6.605658079647881 time=148.704s

getInfSup(BinaryValueSet.BINARY80)
tol=0.001 res=6.605350712421182 time=19.449s
tol=1.0E-4 res=6.605631967925464 time=63.268s
tol=1.0E-5 res=6.605658079647916 time=216.264s

getInfSup(BinaryValueSet.BINARY128)
tol=0.001 res=6.605350712421182 time=25.426s
tol=1.0E-4 res=6.605631967925464 time=78.118s
tol=1.0E-5 res=6.605658079647917 time=264.028s
 */

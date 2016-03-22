
import net.java.jinterval.interval.set.SetInterval;
import net.java.jinterval.interval.set.SetIntervalContext;
import net.java.jinterval.interval.set.SetIntervalContexts;

/**
 *
 */
public class Main_err {

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
            IAOGO_err algorithm = new IAOGO_err();
            //Now we'll launch basic version of interval algorithm of global optimization for chosen objective function,
            //interval box, tolerance and Context (ic)
            algorithm.start(box, tolerance, ic);
            long stopTime = System.currentTimeMillis();
            System.out.println("tol=" + tolerance + " res=" + algorithm.wList.peek().getAssessment() + " time=" + (stopTime - startTime) / 1E3 + "s");
        }
    }

    public static void main(String[] args) {
        test("plain_err", SetIntervalContexts.getAccur64(), 1E-3, 1E-4, 1E-5, 1E-6, 1E-7);
    }
}
/*
plain_err
tol=0.001 res=6.605350712420997 time=0.317s
tol=1.0E-4 res=6.605631967925274 time=0.848s
tol=1.0E-5 res=6.605658079647722 time=2.891s
tol=1.0E-6 res=6.605660197332125 time=11.263s
tol=1.0E-7 res=6.605660471995233 time=42.512s

plain_err
tol=0.001 res=6.605350712420997 time=0.308s
tol=1.0E-4 res=6.605631967925274 time=0.849s
tol=1.0E-5 res=6.605658079647722 time=2.834s
tol=1.0E-6 res=6.605660197332125 time=11.614s
tol=1.0E-7 res=6.605660471995233 time=57.177s

plain_err
tol=0.001 res=6.605350712420997 time=0.306s
tol=1.0E-4 res=6.605631967925274 time=0.828s
tol=1.0E-5 res=6.605658079647722 time=2.932s
tol=1.0E-6 res=6.605660197332125 time=10.942s
tol=1.0E-7 res=6.605660471995233 time=39.985s
 */

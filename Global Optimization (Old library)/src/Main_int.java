
import com.kenai.jinterval.ClassicRealInterval;
import com.kenai.jinterval.ClassicRealIntervalFactory;
import com.kenai.jinterval.rational_bounds.RealInterval;

/**
 *
 */
public class Main_int {

    private static void test(String msg, ClassicRealIntervalFactory factory, double... tolerances) {
        System.out.println(msg);
        for (double tolerance : tolerances) {
            long startTime = System.currentTimeMillis();
            ClassicRealInterval d = factory.valueOf(15.2, 34.8);
            ClassicRealInterval X = factory.valueOf(100, 100);
            ClassicRealInterval L = factory.valueOf(19.1, 55.2);
            ClassicRealInterval theta = factory.valueOf(0, 1.18);
            //Here we wanted to initialize interval box = [ a b theta]
            //, where a = 2*L/d, b = 2*X/d
            ClassicRealInterval box[] = {L.multiply(factory.valueOf(2, 2)).divide(d), X.multiply(factory.valueOf(2, 2)).divide(d), theta};
            IAOGO_int algorithm = new IAOGO_int();
            //Now we'll launch basic version of interval algorithm of global optimization for chosen objective function,
            //interval box, tolerance and Context (ic)
            algorithm.start(box, tolerance);
            long stopTime = System.currentTimeMillis();
            System.out.println("tol=" + tolerance + " res=" + algorithm.wList.peek().getAssessment() + " time=" + (stopTime - startTime) / 1E3 + "s");
        }
    }

    public static void main(String[] args) {
        switch (3) {
            case 1:
                test("fast_nearest_round", com.kenai.jinterval.fast_nearest_round.DoubleInterval.getFactory(), 1E-3, 1E-4, 1E-5, 1E-6, 1E-7);
                break;
            case 2:
                test("rational_bounds default", RealInterval.getFactory(), 1E-3, 1E-4, 1E-5, 1E-6, 1E-7);
                break;
            case 3:
                RealInterval.enterFastContext();
                try {
                    test("rational_bounds fast", RealInterval.getFactory(), 1E-3, 1E-4, 1E-5, 1E-6, 1E-7);
                } finally {
                    RealInterval.exitContext();
                }
                break;
        }
    }
}
/*
fast_nearest_round
tol=0.001 res=6.605350712421185 time=0.445s
tol=1.0E-4 res=6.605631967925461 time=1.124s
tol=1.0E-5 res=6.605658079647924 time=3.562s
tol=1.0E-6 res=6.605660197332313 time=13.905s
tol=1.0E-7 res=6.605660471995435 time=70.122s

rational_bounds default
tol=0.001 res=6.605350712421084 time=0.371s
tol=1.0E-4 res=6.605631967925347 time=0.994s
tol=1.0E-5 res=6.6056580796478235 time=4.59s
tol=1.0E-6 res=6.605660197332212 time=11.956s
tol=1.0E-7 res=6.605660471995335 time=62.989s

rational_bounds fast
tol=0.001 res=6.605350712421185 time=0.299s
tol=1.0E-4 res=6.605631967925461 time=0.927s
tol=1.0E-5 res=6.605658079647924 time=3.036s
tol=1.0E-6 res=6.605660197332313 time=13.185s
tol=1.0E-7 res=6.605660471995435 time=68.265s
 */

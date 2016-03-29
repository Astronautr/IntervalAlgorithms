package ru.nsc.globalopt.kenai;

import com.kenai.jinterval.rational_bounds.RealInterval;

/**
 *
 */
public class Main_rat {

    private static void test(String msg, double... tolerances) {
        System.out.println(msg);
        for (double tolerance : tolerances) {
            long startTime = System.currentTimeMillis();
            RealInterval d = RealInterval.valueOf(15.2, 34.8);
            RealInterval X = RealInterval.valueOf(100, 100);
            RealInterval L = RealInterval.valueOf(19.1, 55.2);
            RealInterval theta = RealInterval.valueOf(0, 1.18);
            //Here we wanted to initialize interval box = [ a b theta]
            //, where a = 2*L/d, b = 2*X/d
            RealInterval box[] = {L.multiply(RealInterval.valueOf(2, 2)).divide(d), X.multiply(RealInterval.valueOf(2, 2)).divide(d), theta};
            IAOGO_rat algorithm = new IAOGO_rat();
            //Now we'll launch basic version of interval algorithm of global optimization for chosen objective function,
            //interval box, tolerance and Context (ic)
            algorithm.start(box, tolerance);
            long stopTime = System.currentTimeMillis();
            System.out.println("tol=" + tolerance + " res=" + algorithm.wList.peek().getAssessment() + " time=" + (stopTime - startTime) / 1E3 + "s");
        }
    }

    public static void main(String[] args) {
        switch (2) {
            case 1:
                test("default", 1E-3, 1E-4, 1E-5, 1E-6, 1E-7);
                break;
            case 2:
                RealInterval.enterFastContext();
                try {
                    test("fast", 1E-3, 1E-4, 1E-5, 1E-6, 1E-7);
                } finally {
                    RealInterval.exitContext();
                }
        }
    }
}
/*
default
tol=0.001 res=6.605350712421084 time=0.413s
tol=1.0E-4 res=6.605631967925347 time=0.97s
tol=1.0E-5 res=6.6056580796478235 time=4.575s
tol=1.0E-6 res=6.605660197332212 time=11.708s
tol=1.0E-7 res=6.605660471995335 time=62.925s

fast
tol=0.001 res=6.605350712421185 time=0.294s
tol=1.0E-4 res=6.605631967925461 time=0.929s
tol=1.0E-5 res=6.605658079647924 time=3.03s
tol=1.0E-6 res=6.605660197332313 time=13.054s
tol=1.0E-7 res=6.605660471995435 time=68.139s
 */

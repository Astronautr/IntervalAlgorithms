package ru.nsc.globalopt.kenai;

import com.kenai.jinterval.fast_nearest_round.DoubleInterval;

/**
 *
 */
public class Main {

    private static void test(String msg, double... tolerances) {
        System.out.println(msg);
        for (double tolerance : tolerances) {
            long startTime = System.currentTimeMillis();
            DoubleInterval d = DoubleInterval.valueOf(15.2, 34.8);
            DoubleInterval X = DoubleInterval.valueOf(100, 100);
            DoubleInterval L = DoubleInterval.valueOf(19.1, 55.2);
            DoubleInterval theta = DoubleInterval.valueOf(0, 1.18);
            //Here we wanted to initialize interval box = [ a b theta]
            //, where a = 2*L/d, b = 2*X/d
            DoubleInterval box[] = {L.multiply(DoubleInterval.valueOf(2, 2)).divide(d), X.multiply(DoubleInterval.valueOf(2, 2)).divide(d), theta};
            IAOGO algorithm = new IAOGO();
            //Now we'll launch basic version of interval algorithm of global optimization for chosen objective function,
            //interval box, tolerance and Context (ic)
            algorithm.start(box, tolerance);
            long stopTime = System.currentTimeMillis();
            System.out.println("tol=" + tolerance + " res=" + algorithm.wList.peek().getAssessment() + " time=" + (stopTime - startTime) / 1E3 + "s");
        }
    }

    public static void main(String[] args) {
        test("DoubleInterval", 1E-3, 1E-4, 1E-5, 1E-6, 1E-7);
    }
}
/*
DoubleInterval
tol=0.001 res=6.605350712421185 time=0.46s
tol=1.0E-4 res=6.605631967925461 time=1.102s
tol=1.0E-5 res=6.605658079647924 time=3.56s
tol=1.0E-6 res=6.605660197332313 time=13.786s
tol=1.0E-7 res=6.605660471995435 time=69.417s
 */

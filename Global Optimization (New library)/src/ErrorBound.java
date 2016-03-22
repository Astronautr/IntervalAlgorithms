
import net.java.jinterval.interval.set.SetInterval;
import net.java.jinterval.interval.set.SetIntervalContext;
import net.java.jinterval.interval.set.SetIntervalContexts;
import net.java.jinterval.rational.BinaryValueSet;
import net.java.jinterval.rational.ExtendedRational;
import net.java.jinterval.rational.ExtendedRationalContext;
import net.java.jinterval.rational.ExtendedRationalContexts;
import net.java.jinterval.rational.Rational;

/**
 *
 */
public class ErrorBound {

    private final Factory factory;
    private final SetInterval range;
    private final SetInterval err;

    private static final ExtendedRationalContext exact = ExtendedRationalContexts.exact();
    private static final Rational HALF = Rational.valueOf(0.5);
    
    public static class Factory {

        private final ErrorBound[] origin;
        private final SetIntervalContext ic; //This field determine accuracy of computing.
        private final BinaryValueSet valueSet;

        private Factory(SetIntervalContext ic, BinaryValueSet valueSet, SetInterval[] x) {
            int dim = x.length;
            origin = new ErrorBound[dim];
            this.valueSet = valueSet;
            for (int i = 0; i < dim; i++) {
                origin[i] = new ErrorBound(this, ic.hull(x[i]), ic.numsToInterval(0, 0));
            }
            this.ic = ic;
        }

        public int getDim() {
            return origin.length;
        }

        public ErrorBound getInp(int i) {
            return origin[i];
        }

        public ErrorBound num(Rational x) {
            SetInterval xi = ic.numsToInterval(x, x);
            ExtendedRational x_rounded = ExtendedRationalContexts.mkNearest(valueSet).rnd(x);
            SetInterval x_range;
            if (x_rounded.isFinite()) {
                x_range = ic.convexHull(xi, ic.numsToInterval(x_rounded, x_rounded));
            } else if (x_rounded.signum() < 0) {
                x_range = ic.numsToInterval(ExtendedRational.NEGATIVE_INFINITY, xi.sup());
            } else {
                x_range = ic.numsToInterval(xi.inf(), ExtendedRational.POSITIVE_INFINITY);
            }
            ExtendedRational err = exact.sub(x_rounded, x);
            return new ErrorBound(this, x_range, ic.numsToInterval(err, err));
        }
        
        private SetInterval rndErr(SetInterval range) {
            return rndErr(range, HALF);
        }

        private SetInterval rndErr(SetInterval range, Rational ulps) {
            ExtendedRational err = range.isCommonInterval() ? exact.mul(valueSet.ulp(range.mag()), ulps) : ExtendedRational.POSITIVE_INFINITY;
            return ic.numsToInterval(exact.neg(err), err);
        }

        private void check(ErrorBound that) {
            if (that.factory != this) {
                throw new IllegalArgumentException();
            }
        }
    }

    public static Factory createFactory(SetIntervalContext ic, BinaryValueSet valueSet, SetInterval... x) {
        return new Factory(ic, valueSet, x);
    }

    private ErrorBound(Factory factory, SetInterval range, SetInterval err) {
        this.factory = factory;
        this.range = range;
        this.err = err;
    }
    
    public SetInterval getRange() {
        return range;
    }
    
    public SetInterval getErr() {
        return err;
    }
    
    public SetInterval getCompensation() {
        SetIntervalContext ic1 = SetIntervalContexts.getInfSup(factory.valueSet);
        SetInterval compensation = ic1.neg(err);
        for (;;) {
            ErrorBound err1 = add(factory.num((Rational) compensation.inf()));
            ErrorBound err2 = add(factory.num((Rational) compensation.sup()));
            SetInterval newCompensation = ic1.convexHull(compensation,
                    ic1.convexHull(ic1.neg(err1.getErr()), ic1.neg(err2.getErr())));
            if (newCompensation.equals(compensation)) {
                break;
            } else {
                compensation = newCompensation;
            }
        }
        return compensation;
    }
    
    public ErrorBound neg() {
        SetIntervalContext ic = factory.ic;
        return new ErrorBound(factory, ic.neg(range), ic.neg(err));
    }

    public ErrorBound add(ErrorBound y) {
        factory.check(y);
        SetIntervalContext ic = factory.ic;
        SetInterval range = ic.add(this.range, y.range);
        SetInterval gX = ic.numsToInterval(1, 1);
        SetInterval gY = ic.numsToInterval(1, 1);
        SetInterval err = ic.add(ic.mul(this.err, gX), ic.mul(y.err, gY));
        SetInterval rndErr = factory.rndErr(range);
        return new ErrorBound(factory, ic.add(range, rndErr), ic.add(err, rndErr));
    }
    
    public ErrorBound sub(ErrorBound y) {
        factory.check(y);
        SetIntervalContext ic = factory.ic;
        SetInterval range = ic.sub(this.range, y.range);
        SetInterval gX = ic.numsToInterval(1, 1);
        SetInterval gY = ic.numsToInterval(-1, -1);
        SetInterval err = ic.add(ic.mul(this.err, gX), ic.mul(y.err, gY));
        SetInterval rndErr = factory.rndErr(range);
        return new ErrorBound(factory, ic.add(range, rndErr), ic.add(err, rndErr));
    }
    
    public ErrorBound mul(ErrorBound y) {
        factory.check(y);
        SetIntervalContext ic = factory.ic;
        SetInterval range = ic.mul(this.range, y.range);
        SetInterval gX = y.range;
        SetInterval gY = this.range;
        SetInterval err = ic.add(ic.mul(this.err, gX), ic.mul(y.err, gY));
        SetInterval rndErr = factory.rndErr(range);
        return new ErrorBound(factory, ic.add(range, rndErr), ic.add(err, rndErr));
    }
    
    public ErrorBound sqr() {
        SetIntervalContext ic = factory.ic;
        SetInterval range = ic.sqr(this.range);
        SetInterval gX = ic.mul(ic.numsToInterval(2, 2), this.range);
        SetInterval err = ic.mul(this.err, gX);
        SetInterval rndErr = factory.rndErr(range);
        return new ErrorBound(factory, ic.add(range, rndErr), ic.add(err, rndErr));
    }
    
    public ErrorBound sin() {
        SetIntervalContext ic = factory.ic;
        SetInterval range = ic.sin(this.range);
        SetInterval gX = ic.cos(this.range);
        SetInterval err = ic.mul(this.err, gX);
        SetInterval rndErr = factory.rndErr(range, Rational.one());
        return new ErrorBound(factory, ic.add(range, rndErr), ic.add(err, rndErr));
    }
}

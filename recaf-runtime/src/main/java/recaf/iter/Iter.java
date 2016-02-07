package recaf.iter;

import java.util.Iterator;

import recaf.core.AbstractJavaImpl;
import recaf.core.Cont;
import recaf.core.functional.K0;

public class Iter<R> extends AbstractJavaImpl<R> {

	@SuppressWarnings("serial")
	private static final class Yield extends RuntimeException {
		final Object value;
		final K0 k;

		public Yield(Object value, K0 k) {
			this.value = value;
			this.k = k;
		}
	}

	public Iterable<R> Method(Cont<R> body) {
		return new Iterable<R>() {

			boolean exhausted = false;
			R current = null;
			K0 k = () -> {
				body.statementDenotation.accept(r -> {
					exhausted = true;
				} , () -> {
					exhausted = true;
				},  (s) -> {
					exhausted = true;
				},  () -> {
					exhausted = true;
				} , exc -> {
					throw new RuntimeException(exc);
				});
			};

			@Override
			public Iterator<R> iterator() {
				return new Iterator<R>() {

					@SuppressWarnings("unchecked")
					@Override
					public boolean hasNext() {
						if (exhausted) {
							return false;
						}
						try {
							k.call();
							return false;
						} catch (Yield y) {
							current = (R) y.value;
							k = y.k;
							return true;
						}
					}

					@Override
					public R next() {
						return current;
					}

				};
			}
		};
	}

	@Override
	public Cont<R> Return(Cont<R> e) {
		throw new AssertionError("Cannot return value from iterator.");
	}

	public <U> Cont<R> Yield(Cont<U> exp) {
		return Cont.fromSD((rho, sigma, brk, contin, err) -> {
			exp.expressionDenotation.accept(v -> {
				throw new Yield(v, sigma);
			} , err);
		});
	}

	public <U> Cont<R> YieldFrom(Cont<Iterable<U>> exp) {
		return For(exp, e -> Yield(Exp(() -> e)));
	}
}

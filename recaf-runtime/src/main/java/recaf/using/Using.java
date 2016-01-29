package recaf.using;

import java.io.IOException;
import java.util.function.Function;

import recaf.core.AbstractJavaCPS;
import recaf.core.ED;
import recaf.core.SD;

public class Using<R> extends AbstractJavaCPS<R> {
	
	public R Method(Cont<R> body) {
		return typePreserving(body);
	}
	
	public <U extends AutoCloseable> Cont<R> Using(Cont<U> resource, Function<U, Cont<R>> body) {
		return Cont.fromSD((rho, sigma, err) -> {
			resource.expressionDenotation.accept(t -> {
				body.apply(t).statementDenotation.accept(r -> {
					try {
						t.close();
					} catch (Exception e) {
						err.accept(e);
					}
					rho.accept(r);
				}, () -> {
					try {
						t.close();
					} catch (Exception e) {
						err.accept(e);
					}
					sigma.call();
				}, exc -> {
					try {
						t.close();
					} catch (Exception e) {
						err.accept(e);
					}
					err.accept(exc);
				});
			}, err);
		});
	}
}

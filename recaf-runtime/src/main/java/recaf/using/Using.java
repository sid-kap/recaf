package recaf.using;

import java.io.IOException;
import java.util.function.Function;

import recaf.core.AbstractJavaCPS;
import recaf.core.ED;
import recaf.core.SD;

public class Using<R> extends AbstractJavaCPS<R> {
	
	public R Method(SD<R> body) {
		return typePreserving(body);
	}
	
	public <U extends AutoCloseable> SD<R> Using(ED<U> resource, Function<U, SD<R>> body) {
		return (rho, sigma, err) -> {
			resource.accept(t -> {
				body.apply(t).accept(r -> {
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
		};
	}
}
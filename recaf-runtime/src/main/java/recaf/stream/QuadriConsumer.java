package recaf.stream;

public interface QuadriConsumer<A, B, C, D> {
	void accept(A a, B b, C c, D d);
}
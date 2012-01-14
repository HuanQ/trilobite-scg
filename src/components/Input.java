package components;

public abstract class Input {
	protected final int											me;
	
	protected Input( int m ) {
		me = m;
	}
	
	public abstract void Update();
}

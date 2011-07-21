package sequences;

import javax.vecmath.Vector2f;

public interface Sequence {
	public void Spawn( final long time, final float rotStart, final Vector2f spawnPoint );
}

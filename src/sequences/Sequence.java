package sequences;

import geometry.Angle;
import geometry.Vec2;

public interface Sequence {
	public void Spawn( final Angle rotStart, final Vec2 spawnPoint, float rotSpeed );
}

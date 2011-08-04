package sequences;

import geometry.Angle;
import geometry.Vec2;

public interface Sequence {
	//TODO: Fer prefabs per a posar shapes
	public abstract boolean Spawn( final Angle direction, final Vec2 spawnPoint );
	public abstract String getName();
}

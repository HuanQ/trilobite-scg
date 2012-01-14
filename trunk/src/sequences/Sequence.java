package sequences;

import geometry.Angle;
import geometry.Vec2;

public interface Sequence {
	//TODO: Fer prefabs per a posar shapes
	//TODO: Fer una bona classe abstracta i implementar les coses comunes aqui (les fases)
	public abstract boolean Spawn( final Angle direction, final Vec2 spawnPoint );
	public abstract String getName();
}

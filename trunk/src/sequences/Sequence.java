package sequences;

import geometry.Angle;
import geometry.Vec2;

public interface Sequence {
	//TODO NOW Remake dels prefabs per a que no vagin linkats a les Sequencies, cal una carpeta de spawners
	//TODO Fer una bona classe abstracta i implementar les coses comunes aqui (les fases)
	public abstract boolean Spawn( final Angle direction, final Vec2 spawnPoint );
	public abstract String getName();
	public abstract Sequence getCopy();
}

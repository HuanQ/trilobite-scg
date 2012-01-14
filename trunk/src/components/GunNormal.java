package components;

import geometry.Vec2;

import managers.Component;
import managers.Level;
import managers.Sound;


public class GunNormal extends Gun {
	private final float											bulletSpeed;
	private final float											exitSeparation;
	private final int											gunNumber;
	// Internal data
	
	public GunNormal( int m, float gd, float bs, float gsep, int gnum ) {
		super(m, gd);
		bulletSpeed = bs;
		exitSeparation = gsep;
		gunNumber = gnum;
	}
	
	public final void Shoot() {
		super.Shoot();
		
		Vec2 bulletDirection = new Vec2( Vec2.down );
		bulletDirection.scale( bulletSpeed );
		
		Vec2 exitPoint = new Vec2( Vec2.down );
		exitPoint.scale(gunDist);
		exitPoint.add(Component.placement.get(me).position);
		
		float exitWidth = exitSeparation * gunNumber;
		
		exitPoint.x -= exitWidth/2;
		
		for(int i=0;i<gunNumber;++i) {
			exitPoint.x += exitWidth / (gunNumber+1);
			Level.AddBullet(new Vec2(bulletDirection), new Vec2(exitPoint), Killable.playerBulletTeam);
		}
		Sound.Play(Sound.shoot);
		
	}
	
}

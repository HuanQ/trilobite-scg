package components;

import geometry.Angle;
import geometry.Vec2;

import managers.Component;
import managers.Level;
import managers.Sound;


public class GunAimed extends Gun {
	private float												bulletSpeed;
	
	public GunAimed( int m, float gd, float bs ) {
		super(m, gd);
		bulletSpeed = bs;
	}
	
	public final void Shoot() {
		super.Shoot();
		
		Angle myRot = new Angle(Component.placement.get(me).angle);
		myRot.add((float) -Math.PI / 2);
		Vec2 bulletDirection = myRot.getDirection();
		bulletDirection.scale( bulletSpeed );
		
		Vec2 exitPoint = myRot.getDirection();
		exitPoint.scale(gunDist);
		exitPoint.add(Component.placement.get(me).position);
		
		Level.AddBullet(bulletDirection, exitPoint, Killable.playerBulletTeam);
		Sound.Play(Sound.shoot);
	}

}

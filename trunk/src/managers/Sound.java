package managers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;

public class Sound {
	static public final Integer								click = 0;
	static public final Integer								shoot = 1;
	static public final Integer								explosion = 2;
	static public final Integer								phase = 3;
	static public final Integer								start = 4;
	static public final Integer								win = 5;
	static private Map<Integer,Audio>						sounds = new HashMap<Integer,Audio>();
	
	static {
		Load(click, "click.wav");
		Load(shoot, "laser.wav");
		Load(explosion, "boom.wav");
		Load(phase, "phase.wav");
		Load(start, "start.wav");
		Load(win, "win.wav");		
	}

	static public final void Play( int num ) {
		sounds.get(num).playAsSoundEffect(1.0f, 1.0f, false);
	}
	
	static private final void Load( Integer num, final String fileName ) {
		try {
			Audio eff = AudioLoader.getAudio( "WAV", ResourceLoader.getResourceAsStream( "resources/sounds/" + fileName) );
			sounds.put(num, eff);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	static private final void LoadStream( Integer num, final String fileName ) {
		try {
			Audio eff = AudioLoader.getStreamingAudio( "WAV", ResourceLoader.getResource("resources/sounds/" + fileName) );
			sounds.put(num, eff);
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
}

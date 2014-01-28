package application.audio;

import static processing.core.PApplet.println;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import processing.core.PApplet;
import ddf.minim.AudioMetaData;
import ddf.minim.AudioOutput;
import ddf.minim.AudioPlayer;
import ddf.minim.Controller;
import ddf.minim.Minim;
import framework.audio.IAudioPlayer;
import framework.audio.IAudioView;
import framework.data.MusicEntry;
import framework.events.PlayTrackEvent;
import gesturedraw.GestureDraw;

public class MinimAudioPlayer extends Observable implements IAudioPlayer {

	private Map<MusicEntry, AudioPlayer> _tracks;

	private PApplet parent;
	private Minim _minim;
	private AudioPlayer _audioPlayer;
	private AudioOutput _output;

	private float _volume;
	private MusicEntry _currentEntry;

	public MinimAudioPlayer() {
		parent = GestureDraw.instance;
		_minim = new Minim(parent);
		
		_output = _minim.getLineOut();
		// testControls(_output);

		set_volume(0.1f);
	}

	@Override
	public void setEntries(ArrayList<MusicEntry> entries) {
		_tracks = new HashMap<MusicEntry, AudioPlayer>();

		for (MusicEntry entry : entries) {
			println("-->>load file :" + entry.filePath);
			_tracks.put(entry, _minim.loadFile(entry.filePath));
		}
		changed();
	}

	public ArrayList<MusicEntry> get_entries() {
		return new ArrayList<MusicEntry>(_tracks.keySet());
	}

	public Boolean isPlaying() {
		return _audioPlayer != null ? _audioPlayer.isPlaying() : false;
	}

	@Override
	public void play(MusicEntry entry) {
		if (_currentEntry != entry) {
			_currentEntry = entry;

			if (_audioPlayer != null) {
				_audioPlayer.pause();
				_audioPlayer.rewind();
			}

			_audioPlayer = _tracks.get(_currentEntry);
			set_volume(_volume);
		}

		_audioPlayer.play();
		changed();

	}

	@Override
	public void resume() {
		_audioPlayer.play();
		changed();
	}

	@Override
	public void stop() {
		if (_audioPlayer != null)
			_audioPlayer.close();

		_minim.stop();

		_currentEntry = null;
		changed();
	}

	@Override
	public void pause() {
		if (_audioPlayer != null)
			_audioPlayer.pause();

		changed();
	}

	@Override
	public void set_volume(float value) {
		_volume = value;
		float decibels = PApplet.map(value, 0, 1, 6, -48);

		if (_audioPlayer != null && _audioPlayer.hasControl(Controller.GAIN)) {
			_audioPlayer.setGain(decibels);
		}
		changed();
	}

	@Override
	public void set_view(IAudioView view) {
		this.addObserver(view);
		changed();
	}

	private void changed() {
		if (_audioPlayer != null)
			println("changed : " + " is playing : " + _audioPlayer.isPlaying());
		
		setChanged();
		notifyObservers(_currentEntry);
	}
	

	private void testControls(Controller controller) {
		if (controller.hasControl(Controller.PAN)) {
			println("The output has a pan control.");// , 15);
		} else {
			println("The output doesn'tcontrollerve a pan control.");// , 15);
		}

		if (controller.hasControl(Controller.VOLUME)) {
			println("The output has a volume control.");// , 30);
		} else {
			println("The output doesn't have a volume control.");// , 30);
		}

		if (controller.hasControl(Controller.SAMPLE_RATE)) {
			println("The output has a sample rate control.");// , 45);
		} else {
			println("The output doesn't have a sample rate control.");// , 45);
		}

		if (controller.hasControl(Controller.BALANCE)) {
			println("The output has a balance control.");// , 60);
		} else {
			println("The output doesn't have a balance control.");// , 60);
		}

		if (controller.hasControl(Controller.MUTE)) {
			println("The output has a mute control.");// , 75);
		} else {
			println("The output doesn't have a mute control.");// , 75);
		}

		if (controller.hasControl(Controller.GAIN)) {
			println("The output has a gain control.");// , 90);
		} else {
			println("The output doesn't have a gain control.");// , 105);
		}
	}

}

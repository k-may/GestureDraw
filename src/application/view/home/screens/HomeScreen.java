package application.view.home.screens;

import processing.core.PApplet;
import application.view.MainView;
import application.view.PView;

public class HomeScreen extends PView {

	private int padding = 5;
	
	public HomeScreen(){
		_width = MainView.SCREEN_WIDTH;
		_height = MainView.SCREEN_HEIGHT;
		_x = 0;
		_y = 0;

		
	}
	@Override
	public void draw(PApplet p) {

		p.noStroke();
		p.fill(0,200);
		p.rect(padding, padding, _width - padding*2, _height - padding*2);
		super.draw(p);
	}
}

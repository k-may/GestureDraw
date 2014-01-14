package application.view.home;

import framework.view.View;
import application.content.ContentManager;
import application.view.MainView;
import application.view.labels.LabelView;
import processing.core.PApplet;

public class WelcomeLabel extends View {

	private LabelView _titleLabel;
	private LabelView _paraLabel;

	public WelcomeLabel() {

	}

	@Override
	public void draw(PApplet p) {

		if (_titleLabel == null) {

			_titleLabel = new LabelView("Welcome", MainView.TEXT_COLOR, ContentManager.GetFont("large"));
			addChild(_titleLabel);

			_paraLabel = new LabelView("Wave your hand to start the experience", MainView.TEXT_COLOR, ContentManager.GetFont("small"));
			addChild(_paraLabel);
		}

		_titleLabel.set_x((_width - _titleLabel.get_width()) / 2);
		_titleLabel.set_y(100);

		_paraLabel.set_x((_width - _paraLabel.get_width()) / 2);
		_paraLabel.set_y(150);

		super.draw(p);

	}

	@Override
	public float get_width() {
		// TODO Auto-generated method stub
		if (_titleLabel == null)
			return 0;

		_width = Math.max(_titleLabel.get_width(), _paraLabel.get_width());
		return _width;
	}

	@Override
	public float get_height() {
		return _paraLabel.get_y() + _paraLabel.get_height();
	}
}

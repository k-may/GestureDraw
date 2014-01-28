package framework.data;

import framework.interaction.Types.HandType;

public class HandData {
	
	private int _id;
	private HandType _type;
	private int _useCount = 0;
	public Boolean updated = false;

	public HandData(int id, HandType type) {
		_id = id;
		_type = type;
	}

	public void updateCount() {
		updated = true;
		_useCount++;
	}
	
	public HandType getType(){
		return _type;
	}

	public int getUseCount() {
		return _useCount;
	}
	
	public int get_id(){
		return _id;
	}
}

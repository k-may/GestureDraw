package framework;

public enum ErrorType {
	KinectError("Kinect Error"), AssetError("Asset Error"), MusicError("Audio Error"), XMLPath("XML PATH"), INIT("Initiation"), Decode("URL_DECODE"), Save("SAVE"), XMLParsing("XML_PARSING"), SimpleOpenNI("SimpleOpenNI"), PathError("PathError");
	
	private String _name;
	private ErrorType(String name){
		_name = name;
	}
	
	@Override
	public String toString() {
		return _name;
	}
}

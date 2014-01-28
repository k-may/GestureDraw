package framework.data;

import processing.opengl.PShader;

public class ShaderEntry {
	
	//private PShader _shader;
	private String _name;
private String _filePath;
	
	public ShaderEntry(String name, String filePath) {
//		_shader = shader;
		_name = name;
		_filePath = filePath;
	}
	/*
	public PShader get_shader() {
		return _shader;
	}*/

	public String get_filePath(){
		return _filePath;
	}
	
	public String get_name() {
		return _name;
	}

}

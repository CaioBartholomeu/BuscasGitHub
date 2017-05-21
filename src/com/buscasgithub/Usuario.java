package com.buscasgithub;

import java.io.Serializable;

public class Usuario implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String login;
	private String avatar_url;
	
	public String getlogin() {
		return login;
	}
	
	public void setlogin(String login) {
		this.login = login;
	}
	
	public String getavatar_url() {
		return avatar_url;
	}
	
	public void setavatar_url(String avatar_url) {
		this.avatar_url = avatar_url;
	}
	
	@Override
	public String toString() {
		return  login;
	}
}

package com.buscasgithub;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;


public class TelaBuscasUsuarios extends Activity {

	private TextView txtLogin;
	private TextView txtavatar_url;
	private Usuario usuario;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_informacoes);
		setContentView(R.layout.buscas_usuarios);
		
		usuario = (Usuario) getIntent().getSerializableExtra("usuario");
		
		txtLogin      = (TextView) findViewById(R.id.txtLogin);
		txtavatar_url = (TextView) findViewById(R.id.txtAvatar);
		
		txtLogin.setText(usuario.getlogin());
		txtavatar_url.setText(usuario.getavatar_url());

	}

}

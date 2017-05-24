package com.buscasgithub;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.Date;
import java.text.BreakIterator;
import java.text.SimpleDateFormat;

import com.buscasgithub.BuscasUsuariosActivity.ConsumirJsonUsuarios;

import android.R.string;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class HomeActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);

		final Button button1 = (Button) findViewById(R.id.button1);
		final Button button2 = (Button) findViewById(R.id.button2);
		
		button1.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View v) {
	        	
	        	//Chamada activity BuscasUsuariosActivity
	        	startActivity(new Intent(HomeActivity.this, BuscasUsuariosActivity.class));
				overridePendingTransition(R.anim.fade, R.anim.hold);
	            }
	        });	
		
		button2.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View v) {
	        	
	        	//Chamada activity BuscasRepositoriosActivity
	        	startActivity(new Intent(HomeActivity.this, BuscasRepositoriosActivity.class));
				overridePendingTransition(R.anim.fade, R.anim.hold);
	            }
	        });	
	}
}
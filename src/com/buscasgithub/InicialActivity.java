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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class InicialActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inicial);

		try {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Thread tempo = new Thread() {
			public void run() {

				try {
					sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				finally {
					
					//Chamada activity HomeActivity
					startActivity(new Intent(InicialActivity.this, HomeActivity.class));
					overridePendingTransition(R.anim.fade, R.anim.hold);
				}
			}
		};
        //Inicia a Thread para aguardar o tempo e chamar outro activity  
		tempo.start();
	}

}
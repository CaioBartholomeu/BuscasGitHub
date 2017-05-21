package com.buscasgithub;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;


//public class ConsumirJson extends ListActivity {
	//static final String LOG_TAG = null;
	
	/*
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
Log.d(ConsumirJson.LOG_TAG,"PASSO 3");	
		new DownloadJsonAsyncTask()
				.execute("https://api.github.com/search/users?q=CaioBartholomeu");
	}
*/
	/*
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
Log.d(ConsumirJson.LOG_TAG,"PASSO 4");			
		Usuario usuario = (Usuario) l.getAdapter().getItem(position);

		Intent intent = new Intent(this, TelaBuscasUsuarios.class);
		intent.putExtra("usuario", usuario);
		startActivity(intent);
	}
	*/

	
	//class DownloadJsonAsyncTask extends AsyncTask<String, Void, List<Usuario>> {
	class ConsumirJson extends AsyncTask<String, Void, List<Usuario>> {	
		static final String LOG_TAG = null;
		ProgressDialog dialog;
		String jsonStringPrincipal;
		
		//Exibe pop-up indicando que est� sendo feito o download do JSON
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
			/*
			  dialog = ProgressDialog.show(ConsumirJson.this, "Aguarde",
              "Fazendo download do JSON");
			*/
		}

		//Acessa o servi�o do JSON e retorna a lista de usuario
		@Override
		protected List<Usuario> doInBackground(String... params) {
			String urlString = params[0];
			HttpClient httpclient = new DefaultHttpClient();
			HttpGet httpget = new HttpGet(urlString);
			try {
				HttpResponse response = httpclient.execute(httpget);
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					InputStream instream = entity.getContent();
					String json = getStringFromInputStream(instream);
					instream.close();
					//List<Usuario> usuario = getUsuario(json);					
					jsonStringPrincipal=json;
					//return usuario;
				}
			} catch (Exception e) {
				Log.e("Erro", "Falha ao acessar Web service", e);
			}
			return null;
		}


		//Depois de executada a chamada do servi�o 
		@Override
		protected void onPostExecute(List<Usuario> result) {
			super.onPostExecute(result);
			//dialog.dismiss();
			/*
			if (result.size() > 0) {
				ArrayAdapter<Usuario> adapter = new ArrayAdapter<Usuario>(
						ConsumirJson.this,
						android.R.layout.simple_list_item_1, result);
				setListAdapter(adapter);
			} else {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						ConsumirJson.this)
						.setTitle("Erro")
						.setMessage("N�o foi poss�vel acessar as informa��es!!")
						.setPositiveButton("OK", null);
				builder.create().show();
			}
			*/
		}
		
 	//Retorna uma lista de pessoas com as informa��es retornadas do JSON
		private List<Usuario> getUsuario(String jsonString) {
			List<Usuario> usuarios = new ArrayList<Usuario>();
			try {
Log.d(ConsumirJson.LOG_TAG,"PASSO 5");					
				JSONObject jsonObj = new JSONObject(jsonString);
                JSONArray contacts = jsonObj.getJSONArray("items");
				
				for (int i = 0; i < contacts.length(); i++) {
					
					JSONObject c = contacts.getJSONObject(i); 
					
Log.d(ConsumirJson.LOG_TAG,"LOGIN= "  + c.getString("login"));	
Log.d(ConsumirJson.LOG_TAG,"AVATAR= " + c.getString("avatar_url"));	

				Usuario objetoPessoa = new Usuario();
				objetoPessoa.setlogin(c.getString("login"));
				objetoPessoa.setavatar_url(c.getString("avatar_url"));
				usuarios.add(objetoPessoa);
				jsonStringPrincipal=jsonString;
				}

			} catch (JSONException e) {
				Log.e("Erro", "Erro no parsing do JSON", e);
			}
			return usuarios;
		}
		

		//Converte objeto InputStream para String
		private String getStringFromInputStream(InputStream is) {

			BufferedReader br = null;
			StringBuilder sb = new StringBuilder();

			String line;
			try {

				br = new BufferedReader(new InputStreamReader(is));
				while ((line = br.readLine()) != null) {
					sb.append(line);
				}

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (br != null) {
					try {
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			return sb.toString();

		}

	}
//}

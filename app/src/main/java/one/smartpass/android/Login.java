package one.smartpass.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import com.mixxamm.smartpassalpha.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by maxim on 19/12/2017.
 * Deze klasse zorgt ervoor dat inloggen mogelijk is. Werkt in de achtergrond. De klasse LoginActivity en LoginTest bevatten de lay-out.
 * LoginTest heeft een simpelere lay-out, omdat dat makkelijker is om mee te werken. LoginActivity heeft een ingewikkeldere lay-out
 * die waarschijnlijk niet meer gaat gebruikt worden aangezien LoginTest er ondertussen al beter uitziet, en volledig zelf is gemaakt,
 * waardoor het makkelijker is om mee te werken.
 */


public class Login extends AsyncTask<String, Void, String> {
    static String leerlingID, leerlingNaam, naarBuiten, tekst, type1, leerkrachtNaam, login, type2, klas;
    static int aantalTotaal, aantalTrimester, aantalTotNablijven;
    Context context;
    AlertDialog alertDialog;

    Login(Context context1) {
        context = context1;
    }

    @Override
    public String doInBackground(String... params) {
        String type = params[0];
        type1 = type;
        String login_url;
        if (type.equals("login")) {
            try {
                login_url = "https://smartpass.one/connect/login.php";
                String tabel = "tblleerlingen";
                String gebruikersnaam = params[1];
                String wachtwoord = params[2];
                URL url = new URL(login_url);
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
                httpsURLConnection.setRequestMethod("POST");
                httpsURLConnection.setDoOutput(true);
                httpsURLConnection.setDoInput(true);
                OutputStream outputStream = httpsURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("tabel", "UTF-8") + "=" + URLEncoder.encode(tabel, "UTF-8") + "&"
                        + URLEncoder.encode("gebruikersnaam", "UTF-8") + "=" + URLEncoder.encode(gebruikersnaam, "UTF-8") + "&"
                        + URLEncoder.encode("wachtwoord", "UTF-8") + "=" + URLEncoder.encode(wachtwoord, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpsURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }


                JSONObject jsonobj = new JSONObject(result);
                leerlingID = jsonobj.getString("leerlingID");
                leerlingNaam = jsonobj.getString("naam");
                naarBuiten = jsonobj.getString("buiten");
                klas = jsonobj.getString("klas");
                stelLeerlingIdIn(context);

                bufferedReader.close();
                inputStream.close();
                httpsURLConnection.disconnect();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if (type.equals("loginLeerkracht")) {
            try {
                login_url = "https://smartpass.one/connect/login.php";
                String tabel1 = "tblleerkrachten";
                String gebruikersnaam = params[1];
                String wachtwoord = params[2];
                URL url = new URL(login_url);
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
                httpsURLConnection.setRequestMethod("POST");
                httpsURLConnection.setDoOutput(true);
                httpsURLConnection.setDoInput(true);
                OutputStream outputStream = httpsURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("tabel", "UTF-8") + "=" + URLEncoder.encode(tabel1, "UTF-8") + "&"
                        + URLEncoder.encode("gebruikersnaam", "UTF-8") + "=" + URLEncoder.encode(gebruikersnaam, "UTF-8") + "&"
                        + URLEncoder.encode("wachtwoord", "UTF-8") + "=" + URLEncoder.encode(wachtwoord, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpsURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";
                while((line = bufferedReader.readLine()) !=null){
                    result += line;
                }
                JSONObject jsonObject = new JSONObject(result);
                leerkrachtNaam = jsonObject.getString("naamLeerkracht");
                login = jsonObject.getString("login");

                bufferedReader.close();
                inputStream.close();
                httpsURLConnection.disconnect();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


        } else if(type.equals("dashboard")){
            login_url = "https://smartpass.one/connect/dashboard.php";
            String id = params[1];
            try{
                URL url = new URL(login_url);
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
                httpsURLConnection.setRequestMethod("POST");
                httpsURLConnection.setDoOutput(true);
                httpsURLConnection.setDoInput(true);
                OutputStream outputStream = httpsURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpsURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                JSONObject jsonobj = new JSONObject(result);
                aantalTotaal = jsonobj.getInt("totaal");
                aantalTrimester = jsonobj.getInt("trimester");
                aantalTotNablijven = jsonobj.getInt("totnablijven");

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            }





        else if (type.equals("zetTeLaat")) {
            String id = params[1];
            String wachtwoord = params[2];
            String leerkrachtNaam = params[3];
            type2 = params[4];
            login_url = "https://smartpass.one/connect/telaat.php";
            try {
                URL url = new URL(login_url);
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
                httpsURLConnection.setRequestMethod("POST");
                httpsURLConnection.setDoOutput(true);
                httpsURLConnection.setDoInput(true);
                OutputStream outputStream = httpsURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8")
                        + "&" + URLEncoder.encode("wachtwoord", "UTF-8") + "=" + URLEncoder.encode(wachtwoord, "UTF-8")
                        + "&" + URLEncoder.encode("naam", "UTF-8") + "=" + URLEncoder.encode(leerkrachtNaam, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpsURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                JSONObject jsonobj = new JSONObject(result);
                tekst = jsonobj.getString("tekst");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
        return null;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    public void onPostExecute(String naam) {

        if(type1.equals("login")){
            LeerlingenKaartFragment.naam = leerlingNaam;
            LeerlingenKaartFragment.fotoURL = "https://smartpass.one/foto/" + leerlingID + ".png";
            LeerlingenKaartFragment.buiten = naarBuiten;
            LeerlingenKaartFragment.klas = klas;

            if(leerlingNaam.equals("Leerling niet gevonden")){
                Toast.makeText(context, "Naam of wachtwoord fout", Toast.LENGTH_LONG).show();
                resetLeerling();
                LoginTest loginTest = new LoginTest();
                loginTest.type = "login";
                Intent loginTest1 = new Intent(context, LoginTest.class);
                context.startActivity(loginTest1);
                ((Activity) context).finish();
                /*LoginTest loginTest = new LoginTest();
                LoginTest.progressBar.setVisibility(View.INVISIBLE);*/
            }
            else{
                Intent leerlingenkaart = new Intent(context, LeerlingActivity.class);
                context.startActivity(leerlingenkaart);
                ((Activity) context).finish();
            }
        }

        else if (type1.equals("zetTeLaat") && type2.equals("tli")) {
            if(tekst.contains("Leerling niet te laat gezet") || tekst.equals("Er is iets fout gegaan")){
                Toast.makeText(context, tekst, Toast.LENGTH_SHORT).show();
            }
            else{
                ToonLeerlingInfo.magBuiten.setImageResource(R.drawable.ic_cancel_black_48dp);
            }


        }
        else if(type1.equals("zetTeLaat") && type2.equals("sa")){
            if(tekst.contains("Leerling niet te laat gezet") || tekst.equals("Er is iets fout gegaan")){
                Toast.makeText(context, tekst, Toast.LENGTH_SHORT).show();
            }
            else{
                ScanFragment.buiten = "0";
                ScanFragment scanFragment = new ScanFragment();
                scanFragment.laden();
            }
        }
        else if(type1.equals("loginLeerkracht")){
                if(login.equals("1")){
                    Intent leerkrachtenActivity = new Intent(context, LeerkrachtenActivity.class);
                    leerkrachtenActivity.putExtra("type", "normal");
                    context.startActivity(leerkrachtenActivity);
                    ((Activity) context).finish();
                }
                else{
                    Toast.makeText(context, "Inloggen als leerkracht mislukt, kijk gegevens na", Toast.LENGTH_SHORT).show();
                    resetLeerkracht();
                    LoginTest loginTest = new LoginTest();
                    loginTest.type = "loginLeerkracht";
                    Intent loginTest1 = new Intent(context, LoginTest.class);
                    context.startActivity(loginTest1);
                    ((Activity) context).finish();
                    /*LoginTest loginTest = new LoginTest();
                    loginTest.progressOnzichtbaar();*/
                }

        }
        else if(type1.equals("dashboard")){
            DashboardFragment.aantalTeLaat = aantalTotaal;
            DashboardFragment.aantalTeLaatTrimester = aantalTrimester;
            DashboardFragment.intAantalTotNablijven = aantalTotNablijven;
            DashboardFragment dashboardFragment = new DashboardFragment();
            dashboardFragment.laden();
        }

    }


    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    public void stelLeerlingIdIn(Context c) {
        SharedPreferences account = c.getSharedPreferences(MainActivity.ACCOUNT, 0);
        SharedPreferences.Editor editor = account.edit();
        editor.putString("id", leerlingID);
        editor.putString("klas", klas);
        editor.commit();
    }

    public void resetLeerkracht(){
        SharedPreferences account = context.getSharedPreferences(MainActivity.ACCOUNT, 0);
        SharedPreferences.Editor editor = account.edit();
        editor.putString("naamLeerkracht", "");
        editor.commit();
    }

    public void resetLeerling(){
        SharedPreferences account = context.getSharedPreferences(MainActivity.ACCOUNT, 0);
        SharedPreferences.Editor editor = account.edit();
        editor.putString("naamGebruiker", "");
        editor.commit();
    }


}


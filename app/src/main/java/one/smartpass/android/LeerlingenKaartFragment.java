package one.smartpass.android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.mixxamm.smartpassalpha.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


import static android.view.WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_FULL;
import static android.view.WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;

import static one.smartpass.android.MainActivity.ACCOUNT;

public class LeerlingenKaartFragment extends Fragment {

    private int color1;
    public static String id, naam, fotoURL, buiten, klas;
    public static boolean internet;
    public static View v;
    public ImageView imageView;
    private TextView naamLeerling;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        v = inflater.inflate(R.layout.fragment_leerlingenkaart, container, false);
        setScreenBrightnessTo(BRIGHTNESS_OVERRIDE_FULL);

        BottomNavigationView navigation = getActivity().findViewById(R.id.navigation);

        //Kleurlijsten maken
        int[][] states = new int[][] {
                new int[] {-android.R.attr.state_checked}, // unchecked
                new int[] { android.R.attr.state_checked}, // checked

        };

        int[] colors = new int[] {
                Color.parseColor("#757575"),
                Color.parseColor("#455A64")
        };

        int[][] states1 = new int[][] {
                new int[] {-android.R.attr.state_checked}, // unchecked
                new int[] { android.R.attr.state_checked}, // checked

        };

        int[] colors2 = new int[] {
                Color.parseColor("#BDBDBD"),
                Color.parseColor("#455A64")
        };

        final ColorStateList normaalLijst = new ColorStateList(states, colors);
        final ColorStateList lichtLijst = new ColorStateList(states, colors);
        SharedPreferences account = getActivity().getSharedPreferences(ACCOUNT, 0);
        if(!account.getString("id", "").equals("")){
            id = account.getString("id", "");
        }

        if(!isNetworkAvailable() || !internet){
            naam = account.getString("naamGebruiker", "");
            klas = account.getString("klas", "");
            buiten = "4";
        }
        else if(naam.equals(null)){
            Intent mainActivity = new Intent(getContext(), MainActivity.class);
            startActivity(mainActivity);
        }

        else if("Leerling niet gevonden".equals(naam)){//log automatisch uit als account niet bestaat
            resetLeerlingNaam();
        }

        imageView = v.findViewById(R.id.qrcode1);
        CircleImageView profielFotoView = v.findViewById(R.id.profielFoto);
        if ("1".equals(buiten)) {
            setActivityBackgroundColor(Color.parseColor("#8BC34A"), Color.parseColor("#689F38"));//parseColor gebruiken aangezien kleuren van colors.xml pakken niet werkt om een vage reden


            navigation.setItemTextColor(normaalLijst);
            navigation.setItemIconTintList(normaalLijst);

            color1 = Color.parseColor("#8BC34A");
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getActivity().getWindow().setNavigationBarColor(Color.parseColor("#689F38"));
            }
        } else if ("0".equals(buiten)) {
            setActivityBackgroundColor(Color.parseColor("#F44336"), Color.parseColor("#D32F2F"));
            color1 = Color.parseColor("#F44336");


            navigation.setItemTextColor(lichtLijst);
            navigation.setItemIconTintList(lichtLijst);

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getActivity().getWindow().setNavigationBarColor(Color.parseColor("#D32F2F"));
            }
        } else if ("3".equals(buiten)) {
            profielFotoView.setImageResource(R.drawable.alert_circle);
            profielFotoView.setVisibility(View.VISIBLE);
            profielFotoView.setVisibility(View.INVISIBLE);
            color1 = Color.WHITE;
            navigation.setItemTextColor(normaalLijst);
            navigation.setItemIconTintList(normaalLijst);
        } else if("4".equals(buiten)){
            profielFotoView.setImageResource(R.drawable.sync_alert);
            profielFotoView.setVisibility(View.VISIBLE);
            color1 = Color.parseColor("#FAFAFA");
            setActivityBackgroundColor(Color.parseColor("#FAFAFA"), Color.parseColor("#bcbcbc"));
            navigation.setItemTextColor(normaalLijst);
            navigation.setItemIconTintList(normaalLijst);
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getActivity().getWindow().setNavigationBarColor(Color.parseColor("#bcbcbc"));
            }
        }


        naamLeerling = v.findViewById(R.id.leerlingNaam);
        naamLeerling.setText(naam + " | " + klas);
        if("1".equals(buiten) || "0".equals(buiten)){
            Picasso.with(getContext()).load(fotoURL).into(profielFotoView);
        }

        try {
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            BitMatrix bitMatrix = multiFormatWriter.encode(id + "," + buiten, BarcodeFormat.QR_CODE,600,600);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            ((ImageView) v.findViewById(R.id.qrcode1)).setImageBitmap(bitmap);
            ImageView qrcode = v.findViewById(R.id.qrcode1);
            qrcode.setColorFilter(color1, PorterDuff.Mode.MULTIPLY);
        } catch (WriterException e) {
            e.printStackTrace();
        }


        return v;
    }

    private boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void setActivityBackgroundColor(int color, int color2) {
        RelativeLayout relativeLayout = v.findViewById(R.id.fragmentleerlingenkaart);
        relativeLayout.setBackgroundColor(color);

        BottomNavigationView navigation = getActivity().findViewById(R.id.navigation);
        navigation.setBackgroundColor(color);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color2);
        }
    }
    public void resetLeerlingNaam(){
        SharedPreferences account = getActivity().getSharedPreferences(ACCOUNT, 0);
        SharedPreferences.Editor editor = account.edit();
        editor.putString("naamGebruiker", "");//Aangezien bij het inloggen enkel op de naam wordt gecontroleerd, hoeven we het wachtwoord niet te resetten
        editor.commit();//Dit voert de wijzigingen door
    }

    private void setScreenBrightnessTo(float brightness) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        if (lp.screenBrightness == brightness) {
            return;
        }
        lp.screenBrightness = brightness;
        getActivity().getWindow().setAttributes(lp);
    }
    public void onDestroy(){
        super.onDestroy();
        setScreenBrightnessTo(BRIGHTNESS_OVERRIDE_NONE);
        }
        public void onResume(){
        super.onResume();
        if(naamLeerling.equals(null)){
            Intent mainActivity = new Intent(getContext(), MainActivity.class);
            startActivity(mainActivity);
        }
        }
}

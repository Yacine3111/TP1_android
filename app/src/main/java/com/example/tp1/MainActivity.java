package com.example.tp1;

import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Dictionary;
import java.util.Hashtable;

public class MainActivity extends AppCompatActivity {
    Dictionary<String,Double> conversionsDictionary =new Hashtable<>();
    String[] currencies={"CAD","EUR","GBP","USD"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        conversionsDictionary.put("CADEUR",0.67);
        conversionsDictionary.put("CADGBP",0.56);
        conversionsDictionary.put("CADUSD",0.69);
        conversionsDictionary.put("EURCAD",1.5);
        conversionsDictionary.put("EURGBP",0.84);
        conversionsDictionary.put("EURUSD",1.04);
        conversionsDictionary.put("GBPCAD",1.79);
        conversionsDictionary.put("GBPEUR",1.19);
        conversionsDictionary.put("GBPUSD",1.24);
        conversionsDictionary.put("USDCAD",1.44);
        conversionsDictionary.put("USDEUR",0.96);
        conversionsDictionary.put("USDGBP",0.81);

        Spinner spinner1 = findViewById(R.id.spinner1);
        Spinner spinner2=findViewById(R.id.spinner2);
        EditText editTextValue=findViewById(R.id.editText1);
        EditText editTextResult=findViewById(R.id.editTextResult);
        Button convertButton=findViewById(R.id.converteButton);
        Button reverseButton=findViewById(R.id.reverseButton);
        Button eraseButton=findViewById(R.id.eraseButton);
        LinearLayout historyLinearLayout=findViewById(R.id.historyLayout);
        ArrayAdapter<String> spinnerArrayAdapter=new ArrayAdapter<>(this,R.layout.spinner_item,currencies);

        spinner1.setAdapter(spinnerArrayAdapter);
        spinner2.setAdapter(spinnerArrayAdapter);


        convertButton.setOnClickListener(View->{
            //verification que les deux devises sont différente
            if(spinner1.getSelectedItem()==spinner2.getSelectedItem()){
                alertMessage("Vous devez choisir deux devises différentes");
                //on vérifie si la conversion existe
            } else if (conversionsDictionary.get(spinner1.getSelectedItem().toString()+spinner2.getSelectedItem().toString())==null) {
                alertMessage("Taux non disponible pour cette conversion");
            } else{
                //on essaye de convertir ou bien on envoie un message pour demander à entrer une valeur
                try {
                    editTextResult.setText(convert(spinner1,spinner2,editTextValue));
                    setHistory(editTextValue,editTextResult,spinner1,spinner2,historyLinearLayout);
                }catch (NumberFormatException e){
                    alertMessage("Veuillez entrer une valeur.");
                }
            }
        });

        reverseButton.setOnClickListener(View->{
            //verification que les deux devises sont différente
            if(spinner1.getSelectedItem()==spinner2.getSelectedItem()){
                alertMessage("Vous devez choisir deux devises différentes");
            }else {
                //on prend les valeurs des deux spinner et on les met dans des String puis on assigne celui de l'un à l'autre
                String spinner1Srt = spinner1.getSelectedItem().toString();
                String spinner2Srt = spinner2.getSelectedItem().toString();
                spinner1.setSelection(spinnerArrayAdapter.getPosition(spinner2Srt));
                spinner2.setSelection(spinnerArrayAdapter.getPosition(spinner1Srt));
                editTextResult.setText("");
            }
        });

        eraseButton.setOnClickListener(View->{
            historyLinearLayout.removeAllViews();
        });
    }
    private void alertMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void setHistory(EditText editTextValue,EditText editTextResult, Spinner spinner1,Spinner spinner2,LinearLayout linearLayout){
        //on convertie en Double la valeur pour qu'on puisse la formater à 2 chiffres après la virgule
        Double EditTextValueDouble=Double.parseDouble(editTextValue.getText().toString());
        TextView textView;
        //on vérifie si le taux de conversion est plus grand ou égale à 1 pour donner la couleur
        if(conversionsDictionary.get(spinner1.getSelectedItem().toString()+spinner2.getSelectedItem().toString())>=1){
            textView =new TextView(new ContextThemeWrapper(this,R.style.HistoryStyleGreen));
        }
        else {
            textView =new TextView(new ContextThemeWrapper(this,R.style.HistoryStyleRed));
        }
        //on formate la valeur avec le String.format() à deux chiffre après la virgule
        textView.setText(String.format("%.2f",EditTextValueDouble)+" "+spinner1.getSelectedItem().toString()+" = "
                +editTextResult.getText()+" "+spinner2.getSelectedItem().toString());

        linearLayout.addView(textView,0);
    }
    private String convert(Spinner spinner1, Spinner spinner2, EditText text){
        double value,result;
        value=Double.parseDouble(text.getText().toString());
        result= value* conversionsDictionary.get(spinner1.getSelectedItem().toString()+spinner2.getSelectedItem().toString());

        //on formate la valeur avec le String.format() à deux chiffre après la virgule
        return String.format("%.2f",result);
    }
}
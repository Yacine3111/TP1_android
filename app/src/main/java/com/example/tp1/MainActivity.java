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
    Dictionary<String,Double> conversionsDictionary =new Hashtable<>()
    {{
        put("CADEUR",0.67);
        put("CADGBP",0.56);
        put("CADUSD",0.69);
        put("EURCAD",1.5);
        put("EURGBP",0.84);
        put("EURUSD",1.04);
        put("GBPCAD",1.79);
        put("GBPEUR",1.19);
        put("GBPUSD",1.24);
        put("USDCAD",1.44);
        put("USDEUR",0.96);
        put("USDGBP",0.81);
    }};
    String[] currencies={"CAD","EUR","GBP","USD"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


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
            String spinner1Value,spinner2Value,key;
            spinner1Value=spinner1.getSelectedItem().toString();
            spinner2Value=spinner2.getSelectedItem().toString();
            key=spinner1Value+spinner2Value;
            //verification que les deux devises sont différente
            if(spinner1Value.equals(spinner2Value)){
                alertMessage("Vous devez choisir deux devises différentes");
                //on vérifie si la conversion existe
            } else if (conversionsDictionary.get(key)==null) {
                alertMessage("Taux non disponible pour cette conversion");
            } else if (editTextValue.getText().toString().isBlank()) {
                alertMessage("Veuillez entrer une valeur.");
            } else{
                //on essaye de convertir ou bien on envoie un message pour demander à entrer une valeur
                    editTextResult.setText(convert(key,editTextValue));
                    setHistory(editTextValue,editTextResult,spinner1Value,spinner2Value,historyLinearLayout);
            }
        });

        reverseButton.setOnClickListener(View->{
            String spinner1Value = spinner1.getSelectedItem().toString();
            String spinner2Value = spinner2.getSelectedItem().toString();
            //verification que les deux devises sont différente
            if(spinner1Value.equals(spinner2Value)){
                alertMessage("Vous devez choisir deux devises différentes");
            }else {
                //on prend les valeurs des deux spinner et on les met dans des String puis on assigne celui de l'un à l'autre
                spinner1.setSelection(spinnerArrayAdapter.getPosition(spinner2Value));
                spinner2.setSelection(spinnerArrayAdapter.getPosition(spinner1Value));
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
    private String convert(String key, EditText text){
        double result;
        result=Double.parseDouble(text.getText().toString());
        result *= conversionsDictionary.get(key);
        //on formate la valeur avec le String.format() à deux chiffre après la virgule
        return String.format("%.2f",result);
    }
    private void setHistory(EditText editTextValue,EditText editTextResult, String spinner1Value,String spinner2Value,LinearLayout linearLayout){
        //on convertie en Double la valeur pour qu'on puisse la formater à 2 chiffres après la virgule
        Double EditTextValueDouble=0.0;
        EditTextValueDouble=Double.parseDouble(editTextValue.getText().toString());
        String key =spinner1Value+spinner2Value;
        TextView textView;
        //on vérifie si le taux de conversion est plus grand ou égale à 1 pour donner la couleur
        if(conversionsDictionary.get(key)>=1){
            textView =new TextView(new ContextThemeWrapper(this,R.style.HistoryStyleGreen));
        }
        else {
            textView =new TextView(new ContextThemeWrapper(this,R.style.HistoryStyleRed));
        }
        //on formate la valeur avec le String.format() à deux chiffre après la virgule
        textView.setText(String.format("%.2f",EditTextValueDouble)+" "+spinner1Value+" = "
                +editTextResult.getText()+" "+spinner2Value);

        linearLayout.addView(textView,0);
    }
}
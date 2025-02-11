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
    Dictionary<String,Double> conversionsTable=new Hashtable<>();
    String[] currencies={"CAD","EUR","GBP","USD"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        conversionsTable.put("CADEUR",0.67);
        conversionsTable.put("CADGBP",0.56);
        conversionsTable.put("CADUSD",0.69);
        conversionsTable.put("EURCAD",1.5);
        conversionsTable.put("EURGBP",0.84);
        conversionsTable.put("EURUSD",1.04);
        conversionsTable.put("GBPCAD",1.79);
        conversionsTable.put("GBPEUR",1.19);
        conversionsTable.put("GBPUSD",1.24);
        conversionsTable.put("USDCAD",1.44);
        conversionsTable.put("USDEUR",0.96);
        conversionsTable.put("USDGBP",0.81);

        Spinner spinner1 = findViewById(R.id.spinner1);
        Spinner spinner2=findViewById(R.id.spinner2);
        EditText editText1=findViewById(R.id.editText1);
        EditText editTextResult=findViewById(R.id.editTextResult);
        Button convertButton=findViewById(R.id.converteButton);
        Button reverseButton=findViewById(R.id.reverseButton);
        Button eraseButton=findViewById(R.id.eraseButton);
        LinearLayout historyLinearLayout=findViewById(R.id.historyLayout);
        ArrayAdapter<String> spinnerArrayAdapter=new ArrayAdapter<>(this,R.layout.spinner_item,currencies);

        spinner1.setAdapter(spinnerArrayAdapter);
        spinner2.setAdapter(spinnerArrayAdapter);


        convertButton.setOnClickListener(View->{
            if(spinner1.getSelectedItem()==spinner2.getSelectedItem()){
                alertMessage();
            }else{
                editTextResult.setText(convesion(spinner1,spinner2,editText1));
                setHistory(editText1,editTextResult,spinner1,spinner2,historyLinearLayout);
            }
        });

        reverseButton.setOnClickListener(View->{
            if(spinner1.getSelectedItem()==spinner2.getSelectedItem()){
                alertMessage();
            }else {
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
    private void alertMessage(){
        Toast.makeText(this, "Vous devez choisir deux devises différentes", Toast.LENGTH_SHORT).show();
    }

    private void setHistory(EditText editText1,EditText editTextResult, Spinner spinner1,Spinner spinner2,LinearLayout linearLayout){
        Double dbEditText1=Double.parseDouble(editText1.getText().toString());
        TextView textView;
        if(conversionsTable.get(spinner1.getSelectedItem().toString()+spinner2.getSelectedItem().toString())>=1){
            textView =new TextView(new ContextThemeWrapper(this,R.style.HistoryStyleGreen));
        }
        else {
            textView =new TextView(new ContextThemeWrapper(this,R.style.HistoryStyleRed));
        }
        textView.setText(String.format("%.2f",dbEditText1)+" "+spinner1.getSelectedItem().toString()+" = "
                +editTextResult.getText()+" "+spinner2.getSelectedItem().toString());

        linearLayout.addView(textView,0);
    }
    private String convesion(Spinner spinner1,Spinner spinner2,EditText text){
        Double value,result;
        value=Double.parseDouble(text.getText().toString());
        result= value*conversionsTable.get(spinner1.getSelectedItem().toString()+spinner2.getSelectedItem().toString());

        return String.format("%.2f",result);
    }
}
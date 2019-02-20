package com.example.translator;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    static final String URL = "https://translate.yandex.net/api/v1.5/tr.json/";
    static final String KEY= "trnsl.1.1.20190129T142501Z.c56ea5b0f3cec23b.fa9138765d38652df8554ebdfb1c3917cc466496";
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;
    RelativeLayout relativeLayout;
    ImageButton btn_ChangeLang;
    ImageButton btn_Clear;
    TextView textViewResult;
    EditText editTextForResult;
    Spinner spinnerForResult;
    Spinner spinnerResult;
    ArrayList<ItemsManager> itemList = new ArrayList<>();
    String lang ,text  ;
    boolean addItem = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        ((LinearLayoutManager) mLayoutManager).setReverseLayout(true);
        ((LinearLayoutManager) mLayoutManager).setStackFromEnd(true);
        mRecyclerView.setLayoutManager(mLayoutManager);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemCallBack);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
        loadItemData();

        relativeLayout = findViewById(R.id.relativeLayout);
        btn_Clear = findViewById(R.id.btn_Clear);
        textViewResult = findViewById(R.id.editTextResult);
        editTextForResult = findViewById(R.id.editTextForResult);
            editTextForResult.setImeOptions(EditorInfo.IME_ACTION_NEXT);
            editTextForResult.setRawInputType(InputType.TYPE_CLASS_TEXT);
            editTextForResult.setOnEditorActionListener(editorListener);
            editTextForResult.clearFocus();
        spinnerForResult = findViewById(R.id.spinnerForResult);
        spinnerResult = findViewById(R.id.spinnerResult);
        btn_ChangeLang = findViewById(R.id.btn_Change);


        ChangeLanguage();
        TextAutoTranslate();
     }

    @Override
    protected void onPause() {
        super.onPause();

            SharedPreferences sharedPreferences = getSharedPreferences("TranslatedItems",MODE_PRIVATE );
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Gson gson = new Gson();
            String json = gson.toJson(itemList);
            editor.putString("itemList",json);
            editor.apply();
    }

    private void loadItemData(){
        SharedPreferences sharedPreferences = getSharedPreferences("TranslatedItems",MODE_PRIVATE );
        Gson gson = new Gson();
        String json = sharedPreferences.getString("itemList",null);
        Type type = new TypeToken<ArrayList<ItemsManager>>() {}.getType();
        itemList = gson.fromJson(json,type);
        mAdapter = new AdapterRecyclerView(itemList,this);
        mRecyclerView.setAdapter(mAdapter);
        if (itemList == null){
            itemList = new ArrayList<>();
        }
    }

    ItemTouchHelper.SimpleCallback simpleItemCallBack = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT ) {
         @Override
         public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
             return false;
         }

         @Override
         public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                int position = viewHolder.getAdapterPosition();
                itemList.remove(position);
                mAdapter.notifyDataSetChanged();
         }
     };

    public void addRecyclerItem(String originalText,String translatedText){
        mAdapter = new AdapterRecyclerView(itemList,this);
        mRecyclerView.setAdapter(mAdapter);
        String saveLang = lang.substring(0,2);
        itemList.add(new ItemsManager(originalText,translatedText,saveLang));
    }
    public void btn_Copy(View view) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Translated Text: ",textViewResult.getText().toString());
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, "Text was copied", Toast.LENGTH_SHORT).show();
    }
    public void btn_Clear(View view) {
        if (editTextForResult.getText().toString().trim().isEmpty() && textViewResult.getText().toString().trim().isEmpty()){
            btn_Clear.setVisibility(View.INVISIBLE);
        }else{
            btn_Clear.setVisibility(View.VISIBLE);
        }
        closeKeyBoard();
        if(itemList.isEmpty()){
            addRecyclerItem(editTextForResult.getText().toString(), textViewResult.getText().toString());
        }

        for (int i = 0; i <= itemList.size() - 1 ; i++) {
            if ((itemList.get(i).getTranslatedText().contains(textViewResult.getText().toString().trim()) && itemList.get(i).getOriginalText().contains(editTextForResult.getText().toString().trim()))){
                addItem = false;
                break;
            }else{
                addItem = true;
            }
        }
        if (addItem){
            addRecyclerItem(editTextForResult.getText().toString(), textViewResult.getText().toString());
        }

        editTextForResult.setText("");
        relativeLayout.setVisibility(View.GONE);



    }

    private void TextAutoTranslate(){
        editTextForResult.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                onTranslate();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (editTextForResult.getText().toString().trim().length() == 0){
                    btn_Clear.setVisibility(View.INVISIBLE);
                    textViewResult.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    relativeLayout.setVisibility(View.GONE);

                }else{
                    textViewResult.setVisibility(View.VISIBLE);
                    btn_Clear.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

     }

    private void closeKeyBoard(){
        View view = this.getCurrentFocus();
        if (view != null){
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }

    private TextView.OnEditorActionListener editorListener = new TextView.OnEditorActionListener() {
         @Override
         public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
             if (actionId == EditorInfo.IME_ACTION_NEXT){
                 onTranslate();
                 closeKeyBoard();
             }
             return false;
         }
     };
    public void btn_Change(View view) {
        int PositionSpinnerForResult = spinnerForResult.getSelectedItemPosition();
        int PositionSpinnerResult= spinnerResult.getSelectedItemPosition();
        spinnerResult.setSelection(PositionSpinnerForResult,true);
        spinnerForResult.setSelection(PositionSpinnerResult,true);
    }
    public void onTranslate() {
        Log.d("TAG", "onTranslate " + lang);
        if (editTextForResult.getText().toString().trim().length() != 0){
            if (Languages.getBuferForAutoDetectedLang().isEmpty()){
                lang = Languages.getBufferLang2();
            }else{
                lang = Languages.getBuferForAutoDetectedLang();
            }
            text = editTextForResult.getText().toString();
            Translate(text,lang,KEY);
            Languages.setBuferForAutoDetectedLang("");
            relativeLayout.setVisibility(View.VISIBLE);
        }
    }
    private void ChangeLanguage(){
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, Languages.getLangs());
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerForResult.setAdapter(arrayAdapter);
        spinnerResult.setAdapter(arrayAdapter);
        AdapterView.OnItemSelectedListener OriginalLanguageSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    Languages.setBufferLang1(Languages.getLangCode(spinnerForResult.getSelectedItemPosition()));
                    Languages.setBufferLang2(Languages.getLangCode(spinnerResult.getSelectedItemPosition()));
                    onTranslate();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                onTranslate();
            }
        };
        spinnerResult.setOnItemSelectedListener(OriginalLanguageSelectedListener);
        spinnerForResult.setOnItemSelectedListener(OriginalLanguageSelectedListener);
    }

    private void onLangChangeAuto(String lang) {
            String mLang1 = lang.substring(0, 2);
            String mLang2 = lang.substring(3, 5);

            int position1 = Languages.getItemPosition(mLang1);
            int position2 = Languages.getItemPosition(mLang2);
            spinnerForResult.setSelection(position1);
            spinnerResult.setSelection(position2);
    }

    private void Translate(String text, String lang, String key){

            Retrofit retrofit   = new Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            RestApi restApi = retrofit.create(RestApi.class);
            Call<Adapter> callForTranslate= restApi.getText(key,lang,text,1);
            callForTranslate.enqueue(new Callback<Adapter>() {
                @Override
                public void onResponse(Call<Adapter> call, Response<Adapter> response) {
                    Adapter adapter = response.body();

                    if (response.isSuccessful()){

                        String result = adapter.getText().toString();
                        result  = result.substring(1,result.length()-1);
                        textViewResult.setText(result);
                        onLangChangeAuto(adapter.getLang());
                    }else{
                        editTextForResult.setHint("Enter Text");
                    }
                }

                @Override
                public void onFailure(Call<Adapter> call, Throwable t) {
                    textViewResult.setText(t.getMessage());
                }
            });
        }
}

package com.example.mybeacon2;

import android.app.Activity;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;

public class ModuleInfo extends Activity implements View.OnClickListener {
    Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_moduleinfo);
        super.onCreate(savedInstanceState);

        back=(Button)findViewById(R.id.back);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.back) {
            finish();
        }
    }
}
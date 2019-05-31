package com.example.mybeacon2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;
import android.widget.TextView;

public class ModuleInfo extends Activity implements View.OnClickListener {
    private Button back;
    private TextView uuid, major, minor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_moduleinfo);
        super.onCreate(savedInstanceState);

        uuid = (TextView)findViewById(R.id.uuid);
        major = (TextView)findViewById(R.id.major);
        minor = (TextView)findViewById(R.id.minor);

        back=(Button)findViewById(R.id.back);
        back.setOnClickListener(this);

        Intent intent = getIntent();
        uuid.setText(intent.getExtras().getString("uuid"));
        major.setText(intent.getExtras().getString("major"));
        minor.setText(intent.getExtras().getString("minor"));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.back) {
            onResume();
            finish();
        }
    }
}
package recruitment.minnovare.com.minnovare;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private static final int OPEN_CSV = 1;
    private Button importCSV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Button upload CSV
        importCSV = (Button) findViewById(R.id.importCSV);
        importCSV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChooser();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case OPEN_CSV: {
                if (resultCode == RESULT_OK) {
                    Intent intent = new Intent(this, TableViewActivity.class);
                    intent.setData(data.getData());
                    startActivity(intent);
                }
                break;
            }
        }
    }

    private void showChooser() {
        try {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("text/comma-separated-values");
            startActivityForResult(Intent.createChooser(intent, "Open CSV"), OPEN_CSV);
        } catch (ActivityNotFoundException e) {
            cancelAndFinish(e);
        }
    }


    private void cancelAndFinish(ActivityNotFoundException e) {
        Log.e("abc", e.getMessage());
        setResult(RESULT_CANCELED);
        finish();
    }
}
package recruitment.minnovare.com.minnovare;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.opencsv.CSVReader;

import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class TableViewActivity extends AppCompatActivity {
    private List<String[]> scoreList = new ArrayList<String[]>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.table);

        Intent callingActivityIntent = getIntent();
        if (callingActivityIntent != null) {
            Uri uri = callingActivityIntent.getData();
            readCSV(uri);

            if (!scoreList.isEmpty()) {
                populateUI();
            }
        }
    }

    private void populateUI() {
        TableRow.LayoutParams cellParam = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
        TableRow.LayoutParams rowParam = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);

        TableLayout tbl = (TableLayout) findViewById(R.id.tableLayout);

        for (final String[] score : scoreList) {
            boolean isTitleBar = score.equals(scoreList.get(0));
            TableRow row = new TableRow(this);
            row.setBackgroundResource(R.drawable.table_row_bg);
            row.setPadding(0, 40, 0, 40);

            for (int i = 0; i < score.length; i++) {

                if (i == score.length - 1 && !score[i].equals("Image File")) { // image item
                    String imageName = score[i];
                    Button showImage = new Button(this);
                    showImage.setBackgroundResource(R.drawable.button_oval_shape);
                    showImage.setTextColor(Color.WHITE);
                    showImage.setTextSize(12);

                    if (imageName == null || imageName.isEmpty()) {
                        showImage.setVisibility(View.GONE);
                    } else {
                        showImage.setText(R.string.show_image);
                        showImage.setVisibility(View.VISIBLE);
                        showImage.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                String imageName = score[score.length - 1].trim();
                                openImageInFullScreen(imageName);
                            }
                        });
                        showImage.setLayoutParams(cellParam);
                        row.addView(showImage);
                    }
                } else {    // Creating new tablerows and textviews
                    TextView txt1 = new TextView(this);

                    //setting the text
                    txt1.setPadding(16, 16, 16, 16);
                    txt1.setText(score[i]);
                    txt1.setGravity(Gravity.CENTER);
                    txt1.setBackgroundResource(R.drawable.table_cell_bg);
                    if (isTitleBar) {
                        txt1.setTypeface(null, Typeface.BOLD);
                    }
                    txt1.setLayoutParams(cellParam);
                    //the textviews have to be added to the row created
                    row.addView(txt1);
                }
            }

            row.setLayoutParams(rowParam);

            tbl.addView(row);
        }
    }

    private void openImageInFullScreen(String imageName) {
            try {
                String path =
                        Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +
                                Environment.DIRECTORY_PICTURES + "/" + imageName;
                File imageFile = new File(path);

                if (Build.VERSION.SDK_INT < 24) {
                    Uri data = Uri.fromFile(imageFile);
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
                    intent.setDataAndType(data, "image/*");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                } else {
                    Toast.makeText(this,"WIP for Image display in Nougat and above", Toast.LENGTH_LONG ).show();
                }
            } catch (Exception e) {
                Toast.makeText(this,imageName +" not found under Pictures", Toast.LENGTH_LONG ).show();
                e.printStackTrace();
            }
    }

    private void readCSV(Uri uri) {
        try {
            // reading CSV and writing table
            try (CSVReader dataRead = new CSVReader(new InputStreamReader(getContentResolver().openInputStream(uri)))) {
                Toast.makeText(this,
                        "Successfully uploaded file!", Toast.LENGTH_LONG).show();
                String[] vv = null;
                while ((vv = dataRead.readNext()) != null) {
                    scoreList.add(vv);
                }
                dataRead.close();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Failed to load CSV, Loading Sample CSV", Toast.LENGTH_LONG).show();
            try (CSVReader dataRead = new CSVReader(new InputStreamReader(getResources().openRawResource(R.raw.stopes)))) {
                String[] vv = null;
                while ((vv = dataRead.readNext()) != null) {
                    scoreList.add(vv);
                }
                dataRead.close();
            } catch (Exception ex) {
            }
        }
    }

}

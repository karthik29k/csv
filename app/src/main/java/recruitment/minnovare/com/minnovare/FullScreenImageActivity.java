package recruitment.minnovare.com.minnovare;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class FullScreenImageActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_screen_image);
        ImageView fullScreenImageView = (ImageView) findViewById(R.id.fullScreenImageView);
        Intent callingActivityIntent = getIntent();
        if (callingActivityIntent != null) {
            String drawableName = callingActivityIntent.getExtras().getString("image");
            int resID = getResources().getIdentifier(drawableName, "drawable", getPackageName());

            if (drawableName != null && fullScreenImageView != null) {
                Glide.with(this)
                        .load(resID)
                        .into(fullScreenImageView);
            }
        }
    }
}
package com.wheelchef.wheelchef.chefinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewDebug;
import android.widget.ImageView;
import android.widget.TextView;

import com.wheelchef.wheelchef.R;

/**
 * Created by lyk on 10/27/2015.
 */
public class ViewChefInfoActivity extends AppCompatActivity {
    private TextView chefNameText;
    private TextView chefDescText;
    private TextView chefDistText;
    private TextView chefTimeText;
    private ImageView chefPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_chef_info);
        chefNameText = (TextView) findViewById(R.id.chef_name_text);
        chefDescText = (TextView) findViewById(R.id.chef_description_text);
        chefDistText = (TextView) findViewById(R.id.chef_distance_text);
        chefTimeText = (TextView) findViewById(R.id.chef_time_text);
        chefPhoto = (ImageView) findViewById(R.id.chef_photo);
        Intent intent = getIntent();
        loadAccountInfo(intent);
    }

    private void loadAccountInfo(Intent intent){
        String chefName = intent.getExtras().getString(ViewChefInfoDialog.CHEF_NAME_KEY);
        String chefTime = intent.getExtras().getString(ViewChefInfoDialog.CHEF_TIME_KEY);
        String chefDist = intent.getExtras().getString(ViewChefInfoDialog.CHEF_DIST_KEY);
        chefNameText.setText(chefName);
        chefDescText.setText(chefName + " is a good chef! ");
        chefDistText.setText(chefDist);
        chefTimeText.setText(chefTime);
        if(chefName.equals("lyk")){
            chefPhoto.setImageDrawable(getResources().getDrawable(R.mipmap.ic_lyk));
        }
    }
}

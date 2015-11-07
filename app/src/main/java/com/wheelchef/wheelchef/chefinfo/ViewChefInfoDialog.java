package com.wheelchef.wheelchef.chefinfo;

import com.rey.material.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rey.material.widget.Button;
import com.wheelchef.wheelchef.R;

/**
 * Created by lyk on 10/28/2015.
 */
public class ViewChefInfoDialog extends Dialog {
    public static final String CHEF_NAME_KEY = "ChefNameKey";
    public static final String CHEF_TIME_KEY = "ChefTimeKey";
    public static final String CHEF_DIST_KEY = "ChefDistKey";
    private String chefName;
    private String chefTime;
    private String chefDist;
    private Button viewChefDetailBtn;
    private TextView chefCuisineCatText;
    private TextView chefNameText;
    private TextView chefDescText;
    private ImageView chefPhoto;

    public ViewChefInfoDialog(Context context, String chefName, String chefDist, String chefTime) {
        super(context);
        this.chefName = chefName;
        this.chefTime = chefTime;
        this.chefDist = chefDist;
        this.setContentView(R.layout.dialog_view_chef);
        viewChefDetailBtn = (Button) findViewById(R.id.view_chef_detail_btn);
        chefCuisineCatText = (TextView) findViewById(R.id.chef_cuisine_category);
        chefNameText = (TextView) findViewById(R.id.chef_name_text);
        chefDescText = (TextView) findViewById(R.id.chef_description_text);
        chefPhoto = (ImageView) findViewById(R.id.chef_photo);
        initialize();
        loadChefInfo();
    }

    private void initialize(){
        viewChefDetailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewChefInfoDialog.this.getContext(), ViewChefInfoActivity.class);
                intent.putExtra(CHEF_NAME_KEY, chefName);
                intent.putExtra(CHEF_TIME_KEY, chefTime);
                intent.putExtra(CHEF_DIST_KEY, chefDist);
                ViewChefInfoDialog.this.getContext().startActivity(intent);
                ViewChefInfoDialog.this.dismissImmediately();
            }
        });

        chefCuisineCatText.setText("Chinese Cuisine");
        chefNameText.setText(chefName);
        chefDescText.setText(chefName + " is a good chef! ");
    }
    private void loadChefInfo(){
        if(chefName.equals("lyk")){
            if(chefPhoto==null)
                Log.d("ViewChefInfoDialog","chefPhoto is null");
            chefPhoto.setImageResource(R.mipmap.ic_lyk);

        }
    }
}

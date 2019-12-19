package com.example.yunoi.cleaningmaster;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class TutorialPage3 extends Fragment {

    private ImageView tu3ImageView;
    View view;
    public static TutorialPage3 newInstance(){
        TutorialPage3 tutorialPage3=new TutorialPage3();
        return tutorialPage3;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tutorial_page_3,container,false);
        tu3ImageView=view.findViewById(R.id.tu3ImageView);

        Bitmap bitmapImag=BitmapFactory.decodeResource(view.getContext().getResources(),R.drawable.tu3);
        Bitmap bitmap=resizeBitmapImageFn(bitmapImag,800);
        tu3ImageView.setImageBitmap(bitmap);


        return view;
    }

    public Bitmap resizeBitmapImageFn(Bitmap bmpSource, int maxResolution){

        int iWidth = bmpSource.getWidth();      //비트맵이미지의 넓이

        int iHeight = bmpSource.getHeight();     //비트맵이미지의 높이

        int newWidth = iWidth ;

        int newHeight = iHeight ;

        float rate = 0.0f;



        //이미지의 가로 세로 비율에 맞게 조절

        if(iWidth > iHeight ){

            if(maxResolution < iWidth ){

                rate = maxResolution / (float) iWidth ;

                newHeight = (int) (iHeight * rate);

                newWidth = maxResolution;

            }

        }else{

            if(maxResolution < iHeight ){

                rate = maxResolution / (float) iHeight ;

                newWidth = (int) (iWidth * rate);

                newHeight = maxResolution;

            }

        }


        return Bitmap.createScaledBitmap(

                bmpSource, newWidth, newHeight, true);

    }
}

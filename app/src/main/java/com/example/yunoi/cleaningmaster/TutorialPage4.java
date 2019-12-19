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

public class TutorialPage4 extends Fragment {
    private ImageView tu4ImageView;
    View view;
    public static TutorialPage4 newInstance(){
        TutorialPage4 tutorialPage4=new TutorialPage4();
        return tutorialPage4;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tutorial_page_4,container,false);
        tu4ImageView=view.findViewById(R.id.tu4ImageView);

        Bitmap bitmapImag=BitmapFactory.decodeResource(view.getContext().getResources(),R.drawable.tu4);
        Bitmap bitmap=resizeBitmapImageFn(bitmapImag,800);
        tu4ImageView.setImageBitmap(bitmap);

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

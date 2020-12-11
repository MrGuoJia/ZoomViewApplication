package com.example.zoomviewapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.zoomviewapplication.bean.CirclePoint;
import com.example.zoomviewapplication.custom.PhotoViewAttacherZoom;
import com.example.zoomviewapplication.custom.PhotoViewZoom;
import com.example.zoomviewapplication.utils.PhoneUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private PhotoViewZoom zoomView;
    List<CirclePoint> circlePointList=new ArrayList<>();//标记手触摸点击的坐标点
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initZooView();
    }

    private void initZooView() {
        zoomView=findViewById(R.id.zoomView);
        zoomView.setOnPhotoTapListener(new PhotoViewAttacherZoom.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y, float xWidth, float yTop) {

                CirclePoint circlePoint=new CirclePoint();
                circlePoint.setRealX(x);
                circlePoint.setRealY(y);
                circlePoint.setDisWidth(xWidth);
                circlePoint.setDisHight(yTop);
                circlePointList= reMoveRepeatCircle(circlePoint);

                zoomView.setCirclePoints(circlePointList);
                view.invalidate();
            }
        });
    }

    /**
     * 判断圆是否相交 半径和等于圆心距 相切 半径和 小于圆心距 相离 半径和大于圆心距 相交
     * 去除圆相交,相切 半径 15dp
     * */
    private List<CirclePoint> reMoveRepeatCircle(CirclePoint circlePoint) {
        circlePointList.add(circlePoint);
        int diameter= PhoneUtil.dp2px(MainActivity.this,30);
        for(int i=0;i<circlePointList.size()-1;i++){

            for (int j=i+1;j<circlePointList.size();j++){
                CirclePoint point1=circlePointList.get(i);
                CirclePoint point2=circlePointList.get(j);
                double d = Math.sqrt(Math.pow(point1.getDisWidth()-point2.getDisWidth(),2) + Math.pow(point1.getDisHight()-point2.getDisHight(),2));
                if(d<diameter){
                    //相交，相离
                    circlePointList.get(i).setIfDouble(true);
                    circlePointList.get(j).setIfDouble(true);
                }

            }

        }
        for(int i=0;i<circlePointList.size();i++){
            if(circlePointList.get(i).isIfDouble()){

                circlePointList.remove(i);
                i--;
            }

        }

        finish();
        return circlePointList;

    }

}

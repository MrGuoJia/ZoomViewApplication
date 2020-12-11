package com.example.zoomviewapplication.bean;

public class CirclePoint {
    /**
     * 用于android端设计本地图片
     * */
    private  float x;
    private  float y;

    /**
     * x,y 坐标左边距和上边距的百分比
     * */
    private float realX;
    private float realY;

    public float getRealX() {
        return realX;
    }

    public void setRealX(float realX) {
        this.realX = realX;
    }

    public float getRealY() {
        return realY;
    }

    public void setRealY(float realY) {
        this.realY = realY;
    }

    private boolean ifDouble;

    public boolean isIfDouble() {
        return ifDouble;
    }

    public void setIfDouble(boolean ifDouble) {
        this.ifDouble = ifDouble;
    }

    private float disWidth;
    private float disHight;
    private float windowDisWidth;
    private float windowDisHeight;
    private float leftX;
    private float leftY;

    private float topDy;

    private float touchWidth;
    private float touchHeight;


    public float getTouchWidth() {
        return touchWidth;
    }

    public void setTouchWidth(float touchWidth) {
        this.touchWidth = touchWidth;
    }

    public float getTouchHeight() {
        return touchHeight;
    }

    public void setTouchHeight(float touchHeight) {
        this.touchHeight = touchHeight;
    }

    public CirclePoint() {
    }

    public float getLeftX() {
        return leftX;
    }

    public void setLeftX(float leftX) {
        this.leftX = leftX;
    }

    public float getLeftY() {
        return leftY;
    }

    public void setLeftY(float leftY) {
        this.leftY = leftY;
    }

    public float getDisWidth() {
        return disWidth;
    }

    public void setDisWidth(float disWidth) {
        this.disWidth = disWidth;
    }

    public float getDisHight() {
        return disHight;
    }

    public void setDisHight(float disHight) {
        this.disHight = disHight;
    }

    public float getWindowDisWidth() {
        return windowDisWidth;
    }

    public void setWindowDisWidth(float windowDisWidth) {
        this.windowDisWidth = windowDisWidth;
    }

    public float getWindowDisHeight() {
        return windowDisHeight;
    }

    public void setWindowDisHeight(float windowDisHeight) {
        this.windowDisHeight = windowDisHeight;
    }



    public float getTopDy() {
        return topDy;
    }

    public void setTopDy(float topDy) {
        this.topDy = topDy;
    }

    public CirclePoint(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }


    @Override
    public String toString() {
        return "CirclePoint{" +
                "realX=" + realX +
                ", realY=" + realY +
                ", disWidth=" + disWidth +
                ", disHight=" + disHight +
                '}';
    }
}

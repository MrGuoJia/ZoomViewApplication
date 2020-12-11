package com.example.zoomviewapplication.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.WindowManager;

import com.example.zoomviewapplication.R;
import com.example.zoomviewapplication.bean.CirclePoint;
import com.example.zoomviewapplication.utils.PhoneUtil;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class PhotoViewZoom extends androidx.appcompat.widget.AppCompatImageView implements IPhotoViewZoom {

    //该图的真实宽高
    private static final float mOriginalWidth = 1600f;
    private static final float mOriginalHeight = 1200f;
    private Boolean isTouch = true;//是否可以点击
    private final PhotoViewAttacherZoom mAttacher;
    private Bitmap mainBitmap;

    static final String LOG_TAG = "PhotoViewZoom";
    //控件的宽高
    private int mMeasureWidth;
    private int mMeasureHeight;
    //原始图片宽高
    // 图片显示的长宽
    private int mDisplayHeight;
    private int mDisplayWidth;
    //原始图片宽高
    private ScaleType mPendingScaleType;
    private int mBitWidth;//
    private int mBitHeight;



    private BitmapFactory.Options options;
    private float currentX = 0;
    private float currentY = 0;
    private float pointX;//手指触摸起点的位置
    private float pointY;
    private float moveX;//当前手指位置
    private float moveY;
    private float mScaleWidth = 0;
    private float mScalehight;
    private float mLeftX;
    private float mLeftY;

    boolean isDraw = true;
    boolean isWidthMoreHeight = false;
    private boolean mTwofingerTonch = false;
    private float onSelectedDrawX = 0f;
    private float onSelectedDrawY = 0f;
    private float mScaleWMode = 1.0f;
    private int mMeasuredHeight;
    private List<CirclePoint> circlePoints = new ArrayList<>();//记录点击了哪几个坐标

    private Paint paint;





    public void setCirclePoints(List<CirclePoint> circlePoints) {
        this.circlePoints = circlePoints;


    }

    public PhotoViewZoom(Context context) {
        this(context, null);
    }

    public PhotoViewZoom(Context context, AttributeSet attr) {
        this(context, attr, 0);
    }


    public PhotoViewZoom(Context context, AttributeSet attr, int defStyle) {
        super(context, attr, defStyle);
        super.setScaleType(ScaleType.MATRIX);

        paint = new Paint();
        paint.setAntiAlias(true);//抗锯齿
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE); //绘制空心圆
        paint.setStrokeWidth(PhoneUtil.dp2px(getContext(), 2));


        mainBitmap = getBitmap(R.mipmap.icon_illness);//人体图
        mAttacher = new PhotoViewAttacherZoom(this) {
            @Override
            protected void setImgPointF(float scaleWidth, float scalehight, float leftX, float leftY, float rightX, float rightY) {


                Log.w(LOG_TAG, mScaleWidth + " <--w*h--> " + mScalehight + "  mLeftX"
                        + mLeftX + "  mLeftY" + mLeftY + "  rightX" + rightX + "  righty" + rightY);
                mScaleWidth = scaleWidth;
                mScalehight = scalehight;
                mLeftX = leftX;
                mLeftY = leftY;

                postInvalidate();
            }

            @Override
            public void setDownXY(float x, float y) {
                //手点击屏幕

                pointX = x;
                pointY = y;
                moveX = pointX;
                moveY = pointY;
                currentX = pointX;
                currentY = pointY;
            }

            @Override
            public void setMoveXY(float x, float y) {
                //手拖动界面
                moveX = x;
                moveY = y;
                isDraw = false;
                if ((Math.sqrt(Math.abs(moveX - pointX) * Math.abs(moveX - pointX) +
                        Math.abs(moveY - pointY) * Math.abs(moveY - pointY))) > 2) {
                    isDraw = false;
                }
            }

            @Override
            public void setTwofingerTonch(boolean b) {
                //手拖动界面
                mTwofingerTonch = b;
            }

            @Override
            public void setpostInvalidate() {
                //刷新界面，刷新坐标点

                postInvalidate();
            }

            @Override
            public void extendedImg() {
                if (mBodyEnlargeListener != null) {
                    mBodyEnlargeListener.changeSelectBtn();
                }
            }

            @Override
            public void setUpXY(float x, float y) {
                //获取图片实际的长宽

                if ((Math.sqrt(Math.abs(x - pointX) * Math.abs(x - pointX) +
                        Math.abs(y - pointY) * Math.abs(y - pointY))) <= 2) {
                    isDraw = true;
                }
                currentX = currentX + (moveX - pointX);
                currentY = currentY + (moveY - pointY);
                pointX = currentX;
                pointY = currentY;
                if (null != mPendingScaleType) {
                    setScaleType(mPendingScaleType);
                    mPendingScaleType = null;
                }

                if (!isDraw) {
                    isDraw = true;
                    return;
                }

                options = new BitmapFactory.Options();
                options.inSampleSize = 1;

                Log.w(LOG_TAG, pointX + "*" + mLeftX + "*" + mBitWidth + "*" + mScaleWidth);
               // Log.w("3699xxyyyy", pointY + "*" + mLeftY + "*" + mBitHeight + "*" + mScalehight);
                boolean touchPointInTransparent;
                float touchWidth, touchHeight;
                //Log.v("3699isWidthMoreHeight", isWidthMoreHeight + "");
                CirclePoint circlePoint = new CirclePoint();
                circlePoint.setLeftX(mLeftX);
                circlePoint.setLeftY(mLeftY);

                if (!isWidthMoreHeight) {
                    //左边距
                    float dx = (mMeasureWidth * mScaleWidth * 1.00f / mBitWidth - (mScaleWidth * mScaleWMode)) / 2;
                    touchWidth = (pointX - mLeftX - dx) * mBitWidth / (mScaleWidth * mScaleWMode);
                    touchHeight = (pointY - mLeftY) * mBitHeight * 1.0000f / (mScalehight * mScaleWMode);

                } else {
                    //上边距
                    float dy = (mMeasureHeight * mScalehight * 1.00f / mBitHeight - (mScalehight * mScaleWMode)) / 2;
                    Log.v(LOG_TAG, dy + "");
                    touchWidth = (pointX - mLeftX) * mBitWidth * 1.0000f / (mScaleWidth * mScaleWMode);
                    touchHeight = (pointY - mLeftY - dy) * mBitHeight * 1.0000f / (mScalehight * mScaleWMode);

                }
                //判断是否超过边界，超过就不设置，不加入
                touchPointInTransparent = isTouchPointInTransparent(touchWidth, touchHeight);


                if (!touchPointInTransparent) {
                    if (isTouch && !mTwofingerTonch) {//如果是可以点击修改，则保存更新坐标


                        onSelectedDrawX = touchWidth * mOriginalWidth * 1.00f / mBitWidth;
                        onSelectedDrawY = touchHeight * mOriginalHeight * 1.00f / mBitHeight;





                        postInvalidate();
                    }
                }
            }
        };

        int paddingLeft = this.getPaddingLeft();
        Log.v(LOG_TAG,"图片距离左边距:"+paddingLeft + "");
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mMeasuredHeight = getMeasuredHeight();
        int measuredWidth = getMeasuredWidth();
        Log.v(LOG_TAG, getMeasuredWidth() + "*" + mMeasuredHeight);
        mDisplayHeight = mMeasuredHeight;
        mDisplayWidth = measuredWidth;
        //mDisplayWidth = (int) (mBitWidth * mDisplayHeight * 1.00f / mBitHeight + 0.5f);
        mMeasureWidth = getMeasureWidth();
        mMeasureHeight = getMeasureHeigh();
        Log.v(LOG_TAG, getMeasuredWidth() + "mMeasureWidth:" + mMeasureWidth);
        Log.v(LOG_TAG, mMeasuredHeight + "mMeasureHeight:" + mMeasureHeight);
        if (mBitHeight > mBitWidth) {
            mScaleWMode = mDisplayHeight * 1.000f / mBitHeight;
        } else {
            mScaleWMode = mDisplayWidth * 1.000f / mBitWidth;
        }
        resetBit();
        Log.w(LOG_TAG,  getHeight() + "***" + getMeasuredHeight());
        Log.w(LOG_TAG,  mScaleWMode + "***" + mDisplayWidth + "  " + mDisplayHeight);
    }


    public void resetBit() {
        RectF rect = getDisplayRect();
        if (null != rect) {
            mAttacher.zoomTo(1.0f, rect.centerX(), rect.centerY());
        }
    }

    public float getDisWidth() {
        return mScaleWidth * mScaleWMode;
    }

    public float getDisHight() {
        return mScalehight * mScaleWMode;
    }

    public float getWindowDisWidth() {
        return mMeasureWidth * mScaleWidth * 1.00f / mBitWidth;
    }

    public float getWindowDisHeight() {
        return mMeasureHeight * mScalehight * 1.00f / mBitHeight;
    }

    public float getLeftDx() {
        return (getWindowDisWidth() - getDisWidth()) * 1.00f / 2;
    }

    public float getTopDy() {
        return (getWindowDisHeight() - getDisHight()) * 1.00f / 2;
    }

    //图片当前缩放倍数
    public float getScaleMultiple() {
        return mScaleWidth * 1.00f / mBitWidth;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);



        if (!isDraw || mTwofingerTonch) {
            return;
        }



        /***
         * circleRadius 圆的半径
         * */
        int circleRadius = (int) (PhoneUtil.dp2px(getContext(), 15) * getScaleMultiple());
            for (int i = 0; i < circlePoints.size(); i++) {
                final RectF displayRect = getDisplayRect();
                float left = circlePoints.get(i).getRealX() * displayRect.width() + displayRect.left;
                float top = circlePoints.get(i).getRealY() * displayRect.height() + displayRect.top;

                circlePoints.get(i).setDisWidth(left);
                circlePoints.get(i).setDisHight(top);

                Log.e(LOG_TAG, "原点坐标 x：距离图片左上角百分比" + circlePoints.get(i).getRealX() + "   y :距离图片顶部百分比" + circlePoints.get(i).getRealY());
                Log.e(LOG_TAG, "图片距离左边和上边的距离 left：" + left + "   top:" + top);

                canvas.drawCircle(left, top, circleRadius, paint);
            }


    }





    /**
     * @param x
     * @param y
     * @return 判断点击区域是否在透明区域
     */

    private boolean isTouchPointInTransparent(float x, float y) {

        Drawable drawable = this.getDrawable();
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        Log.w(LOG_TAG, x + "*" + y + "--->");
        int pixel = 0;
        Log.w(LOG_TAG, ((x > 0 && x < mBitWidth && y > 0 && y < mBitHeight) ? "" : "不") + "在范围内");

        if (y > 0 && y < mBitHeight  && x<bitmap.getWidth()) {
            pixel = bitmap.getPixel((int) x, (int) y);//获取像素值
            Log.v(LOG_TAG, pixel + "");
        }
        Log.v(LOG_TAG, (pixel == 0) + "" + (bitmap.getPixel(291, 53) == 0));
        return pixel == 0;
    }

    private Bitmap getBitmap(int resId) {
        Bitmap bitmap = null;
        try {
            InputStream ins = this.getResources().openRawResource(resId);
            BitmapFactory.Options options = new BitmapFactory.Options();
            //inJustDecodeBounds为true，不返回bitmap，只返回这个bitmap的尺寸
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(getResources(), resId, options);
            //利用返回的原图片的宽高，我们就可以计算出缩放比inSampleSize，获取指定宽度为300像素，等长宽比的缩略图，减少图片的像素
            //使用RGB_565减少图片大小
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            //释放内存，共享引用（21版本后失效）
            options.inPurgeable = true;
            options.inInputShareable = true;
            //inJustDecodeBounds为false，返回bitmap
            options.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeStream(ins, null, options);
            mBitWidth = bitmap.getWidth();
            mBitHeight = bitmap.getHeight();
            mScaleWidth = mBitWidth;
            mScalehight = mBitHeight;

            Log.i(LOG_TAG, bitmap.getWidth() + "--" + bitmap.getHeight());
            isWidthMoreHeight = (mBitWidth > mBitHeight);
            Log.i(LOG_TAG, mBitWidth + "--" + mBitHeight);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } catch (ArithmeticException e) {
            e.printStackTrace();
        }
        if (bitmap == null) {
            // 如果实例化失败 返回默认的Bitmap对象
            return mainBitmap;
        }
        return bitmap;
    }


    public int getMeasureWidth() {
        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);

        return wm.getDefaultDisplay().getWidth();
    }

    public int getMeasureHeigh() {
        return mMeasuredHeight;
    }

    @Override
    public boolean canZoom() {
        return mAttacher.canZoom();
    }

    @Override
    public RectF getDisplayRect() {
        return mAttacher.getDisplayRect();
    }

    @Override
    public float getMinScale() {
        return mAttacher.getMinScale();
    }

    @Override
    public float getMidScale() {
        return mAttacher.getMidScale();
    }

    @Override
    public float getMaxScale() {
        return mAttacher.getMaxScale();
    }

    @Override
    public float getScale() {
        return mAttacher.getScale();
    }

    @Override
    public ScaleType getScaleType() {
        return mAttacher.getScaleType();
    }

    @Override
    public void setAllowParentInterceptOnEdge(boolean allow) {
        mAttacher.setAllowParentInterceptOnEdge(allow);
    }

    @Override
    public void setMinScale(float minScale) {
        mAttacher.setMinScale(minScale);
    }

    @Override
    public void setMidScale(float midScale) {
        mAttacher.setMidScale(midScale);
    }

    @Override
    public void setMaxScale(float maxScale) {
        mAttacher.setMaxScale(maxScale);
    }

    @Override
    // setImageBitmap calls through to this method
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        if (null != mAttacher) {
            mAttacher.update();
        }
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        if (null != mAttacher) {
            mAttacher.update();
        }
    }

    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        if (null != mAttacher) {
            mAttacher.update();
        }
    }

    @Override
    public void setOnMatrixChangeListener(PhotoViewAttacherZoom.OnMatrixChangedListener listener) {
        mAttacher.setOnMatrixChangeListener(listener);
    }

    @Override
    public void setOnLongClickListener(OnLongClickListener l) {
        mAttacher.setOnLongClickListener(l);
    }

    @Override
    public void setOnPhotoTapListener(PhotoViewAttacherZoom.OnPhotoTapListener listener) {
        mAttacher.setOnPhotoTapListener(listener);
    }

    @Override
    public void setOnViewTapListener(PhotoViewAttacherZoom.OnViewTapListener listener) {
        mAttacher.setOnViewTapListener(listener);
    }

    @Override
    public void setScaleType(ScaleType scaleType) {
        if (null != mAttacher) {
            mAttacher.setScaleType(scaleType);
        } else {
            mPendingScaleType = scaleType;
        }
    }


    @Override
    public void setZoomable(boolean zoomable) {
        mAttacher.setZoomable(zoomable);
    }

    @Override
    public void zoomTo(float scale, float focalX, float focalY) {
        mAttacher.zoomTo(scale, focalX, focalY);
    }

    @Override
    protected void onDetachedFromWindow() {
        mAttacher.cleanup();
        super.onDetachedFromWindow();
    }

    public interface IBodyEnlargeListener {
        void changeSelectBtn();
    }

    private IBodyEnlargeListener mBodyEnlargeListener;

    public void setBodyEnlargeListener(IBodyEnlargeListener listener) {
        this.mBodyEnlargeListener = listener;
    }

    public Boolean getTouch() {
        return isTouch;
    }

    public void setTouch(Boolean touch) {
        isTouch = touch;
    }

}
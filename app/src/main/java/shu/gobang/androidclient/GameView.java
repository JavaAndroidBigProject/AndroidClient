package shu.gobang.androidclient;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import Client.AndroidInterface;

/**
 * Created by Administrator on 2016/2/27.
 */
public class GameView extends View{
    int[][] board;
    boolean isBlack;
    boolean isMyTurn;
    boolean isPlaying;
    int x, y, padding, width;
    float d;
//    MyApplication myApplication;
    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        isMyTurn = false;
        board = new int[15][15];
    }

    @Override
    public void onMeasure(int width, int height){
        int specSize = MeasureSpec.getSize(width);
        setMeasuredDimension(specSize, specSize);
    }

    @Override
    public void onDraw(Canvas canvas){
        padding = 40;
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        width = getWidth() - padding*2;
        d = width / 14.0f;
        for(int i=0;i<15;i++){
            canvas.drawLine(padding+i*d, padding, padding+i*d, padding+width,paint);
        }
        for(int i=0;i<15;i++){
            canvas.drawLine(padding, padding+i*d, padding+width,padding+i*d,paint);
        }
        for(int i=0;i<15;i++) {
            for(int j=0;j<15;j++){
                if(board[i][j] == 1){
                    paint.setColor(Color.BLACK);
                    RectF rectF = new RectF(padding+i*d-d/2+4,padding+j*d-d/2+4,padding+i*d+d/2-4,padding+j*d+d/2-4);
                    canvas.drawOval(rectF,paint);
                }else if(board[i][j] == 2){
                    paint.setColor(Color.LTGRAY);
                    RectF rectF = new RectF(padding + i * d - d / 2 + 4, padding + j * d - d / 2 + 4, padding + i * d + d / 2 - 4, padding + j * d + d / 2 - 4);
                    canvas.drawOval(rectF, paint);
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(!isMyTurn || !isPlaying)
            return false;
        else {
            if(getnearestXY(event.getX(),event.getY())) {
                if (board[x][y] == 0) {
                    if (isBlack)
                        board[x][y] = 1;
                    else
                        board[x][y] = 2;
                    AndroidInterface.getInstance().move(x,y);
//                 board[x][y] = 1;invalidate();
                }
            }
        }
        return false;
    }

    private boolean getnearestXY(float rawX, float rawY){
        if(rawX > padding + width || rawX < padding || rawY > padding + width || rawY < padding)
            return false;
        int a = (int)((rawX - padding) / d);
        if(Math.abs(padding+a*d-rawX) > Math.abs(padding+(a+1)*d-rawX))
            x = a + 1;
        else
            x = a;
        int b = (int)((rawY - padding) / d);
        if(Math.abs(padding+b*d-rawY) > Math.abs(padding+(b+1)*d-rawY))
            y = b + 1;
        else
            y = b;
        return true;
    }
}

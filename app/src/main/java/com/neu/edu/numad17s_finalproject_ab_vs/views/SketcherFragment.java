package com.neu.edu.numad17s_finalproject_ab_vs.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.EmbossMaskFilter;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.neu.edu.numad17s_finalproject_ab_vs.Constants;
import com.neu.edu.numad17s_finalproject_ab_vs.R;
import com.neu.edu.numad17s_finalproject_ab_vs.model.Sketch;

import java.util.ArrayList;


public class SketcherFragment extends Fragment {

    private Paint       mPaint;
    private MaskFilter mEmboss;
    private MaskFilter  mBlur;
    MyView mv;
    private LinearLayout mContainerDrawer;
    private FirebaseDatabase mDatabase;
    private ListView mGuessList;
    private ArrayList<String> mGuesses = new ArrayList<>();
    private ArrayAdapter<String> mGuessListAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sketcher, container, false);
        mContainerDrawer = (LinearLayout) view.findViewById(R.id.drawing_board);
        mv= new MyView(getActivity());
        mv.setDrawingCacheEnabled(true);
        mGuessListAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, mGuesses);

        mGuessList = (ListView) view.findViewById(R.id.guesses_list);
        //mv.setBackgroundResource(R.drawable.afor);//set the back ground if you wish tois
        //setContentView(mv);
        mGuessList.setAdapter(mGuessListAdapter);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(0xFFFF0000);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(20);
        mEmboss = new EmbossMaskFilter(new float[] { 1, 1, 1 },
                0.4f, 6, 3.5f);
        mBlur = new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL);
        mContainerDrawer.addView(mv);
        listenGuesses();
        return view;
    }





    public class MyView extends View {

        private static final float MINP = 0.25f;
        private static final float MAXP = 0.75f;
        private Bitmap mBitmap;
        private Canvas mCanvas;
        private Path mPath;
        private Paint mBitmapPaint;
        Context context;

        public MyView(Context c) {
            super(c);
            context=c;
            mPath = new Path();
            mBitmapPaint = new Paint(Paint.DITHER_FLAG);

        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);

        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
            canvas.drawPath(mPath, mPaint);
        }

        private float mX, mY;
        private static final float TOUCH_TOLERANCE = 4;

        private void touch_start(float x, float y) {
            //showDialog();
            mPath.reset();
            mPath.moveTo(x, y);
            mX = x;
            mY = y;

        }
        private void touch_move(float x, float y) {
            float dx = Math.abs(x - mX);
            float dy = Math.abs(y - mY);
            if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
                mX = x;
                mY = y;
            }
        }

        private void touch_up() {
            mPath.lineTo(mX, mY);
            // commit the path to our offscreen
            mCanvas.drawPath(mPath, mPaint);
            // kill this so we don't double draw
            mPath.reset();
            mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SCREEN));
            //mPaint.setMaskFilter(null);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mv.touch_start(x, y);
                    mv.invalidate();
                    drawOnBoard(x,y,"start");
                    break;
                case MotionEvent.ACTION_MOVE:

                    mv.touch_move(x, y);
                    mv.invalidate();
                    drawOnBoard(x,y,"move");
                    break;
                case MotionEvent.ACTION_UP:
                    mv.touch_up();
                    mv.invalidate();
                    drawOnBoard(x,y,"done");
                    break;
            }
            return true;
        }
    }

    private void drawOnBoard(final float x, final float y , final String move) {
        mDatabase.getInstance().getReference()
                .child("Sketches")
                .child(Constants.SketchID)
                .runTransaction(new Transaction.Handler() {
                    @Override
                    public Transaction.Result doTransaction(MutableData mutableData) {
                       Sketch sketch = mutableData.getValue(Sketch.class);
                        sketch.setMouseLocation(String.valueOf(x)+ " "+String.valueOf(y));
                        sketch.setMouseAction(move);
                        mutableData.setValue(sketch);
                        return Transaction.success(mutableData);
                    }

                    @Override
                    public void onComplete(DatabaseError databaseError, boolean b,
                                           DataSnapshot dataSnapshot) {
                        // Transaction completed

                    }
                });
    }


    public void listenGuesses()
    {
        FirebaseDatabase.getInstance().getReference().child("Sketches").child(Constants.SketchID).child("guesses").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> guessList = new ArrayList<String>();
                Iterable<DataSnapshot> dataSnap  = dataSnapshot.getChildren();
                for (DataSnapshot key : dataSnap)
                {
                   guessList.add(key.getValue(String.class));

                }
                mGuesses.clear();
                mGuesses.addAll(guessList);
                mGuessListAdapter.notifyDataSetChanged();
               }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

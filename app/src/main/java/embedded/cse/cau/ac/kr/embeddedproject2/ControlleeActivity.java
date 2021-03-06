package embedded.cse.cau.ac.kr.embeddedproject2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.Socket;
import java.util.Locale;

/**
 * Created by caucse on 2017-11-29.
 */

public class ControlleeActivity extends Activity {

    private Button CancelButton = null;
    private Button OkButton = null;

    private MyAdapter adapter = null;
    private GridView myGridview = null;

    private int [] imageArray = {R.drawable.bulb,R.drawable.crane,
                    R.drawable.handshake, R.drawable.piston,
                    R.drawable.team, R.drawable.remote};

    private boolean [] clickedArray = new boolean[6];

    private TextView IpTextView = null;

    private int clickedIconPosition = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.controllee_activity);

        CancelButton = (Button) findViewById(R.id.controlleeCancelButton);
        OkButton = (Button) findViewById(R.id.controlleeOkButton);

        IpTextView = (TextView) findViewById(R.id.myipAddress);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket sock = new Socket("www.google.co.kr", 80);
                    final String myIP = String.format(Locale.US,"나의 IP 주소 - %s",sock.getLocalAddress());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            IpTextView.setText(myIP);
                        }
                    });
                }catch (Exception e){}
            }
        }).start();


        adapter = new MyAdapter(getApplicationContext(),R.layout.icongrid_view,imageArray,clickedArray);
        myGridview = (GridView) findViewById(R.id.iconGridView);
        myGridview.setAdapter(adapter);


        CancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        myGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Log.d("GridView Clicked","Clicked");
                InitClickedArr();
                clickedArray[position]= true;
                adapter.notifyDataSetChanged();
                clickedIconPosition = position;
            }
        });

        OkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Control Activity","Go to Control Activity");
                // 여기서, 디바이스 이름, 비밀번호, 아이콘 정보, 포트를 다음 Activity로 넘겨주어야한다.

                Intent i = new Intent(ControlleeActivity.this,ControlleeMainActivity.class);
                //i.putExtra("DEVICE_NAME",)
            }
        });

    }





    private void InitClickedArr()
    {
        for(int i=0;i<clickedArray.length;i++)
        {
            clickedArray[i] = false;
        }
    }


    class MyAdapter extends BaseAdapter
    {
        Context context;
        int layout;
        int img[];
        boolean isclicked[];
        LayoutInflater inf;

        public MyAdapter(Context context, int layout, int[] img, boolean[] isclicked)
        {
            this.context = context;
            this.layout = layout;
            this.img = img;
            this.isclicked = isclicked;
            inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount()
        {
            return img.length;
        }

        @Override
        public Object getItem(int position) {
            return img[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            if(view == null)
                view = inf.inflate(layout,null);
            ImageView iv = (ImageView) view.findViewById(R.id.gridImage);
            iv.setImageResource(img[position]);
            if(isclicked[position]==true)
                iv.setBackgroundResource(R.color.clickedIconColor);
            else
                iv.setBackgroundColor(Color.WHITE);

            return view;
        }
    }
}

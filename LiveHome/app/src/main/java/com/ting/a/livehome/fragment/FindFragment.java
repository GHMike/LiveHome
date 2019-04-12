package com.ting.a.livehome.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ting.a.livehome.R;
import com.ting.a.livehome.activity.AddOrderActivity;
import com.ting.a.livehome.unit.Toast;


/**
 * 发现页面（层）
 */
public class FindFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private View find_view1,find_view2,find_view3,find_view4;



    public FindFragment() {
    }

    // TODO: Rename and change types and number of parameters
    public static Fragment newInstance(String param1, String param2) {
        FindFragment fragment = new FindFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View con = inflater.inflate(R.layout.fragment_find, container, false);
        initW(con);
        return con;
    }

    //获取控件
    private void initW(View v) {

        find_view1=v.findViewById(R.id.find_view1);
        find_view2=v.findViewById(R.id.find_view2);
        find_view3=v.findViewById(R.id.find_view3);
        find_view4=v.findViewById(R.id.find_view4);

        find_view1.setOnClickListener(this);
        find_view2.setOnClickListener(this);
        find_view3.setOnClickListener(this);
        find_view4.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.find_view1:
                Intent intent=new Intent(getActivity(),AddOrderActivity.class);
                startActivity(intent);
                break;
            case  R.id.find_view2:
            case  R.id.find_view3:
            case  R.id.find_view4:
                Toast.show(getActivity(),"敬请期待!",Toast.LENGTH_LONG);
                break;
        }
    }
}

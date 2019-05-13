package com.abhishek360.dev.logicsynthesis;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;

import static com.abhishek360.dev.logicsynthesis.MainActivity.tosty;


public class AdderFragment extends Fragment
{


    private EditText input1_edittext,input2_edittext;
    private TextView output1_textview,output2_textview;
    private Button gen_output_button;
    private LogicGates myLogicGate;
    private ImageView gate_imageview;

    private OnFragmentInteractionListener mListener;

    public AdderFragment() {
        // Required empty public constructor
    }


    public static AdderFragment newInstance(String param1, String param2) {
        AdderFragment fragment = new AdderFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=  inflater.inflate(R.layout.fragment_adder, container, false);

        myLogicGate= new LogicGates(3);

        gen_output_button=v.findViewById(R.id.half_adder_button_gen_output);
        input1_edittext=  v.findViewById(R.id.half_adder_edittext_input_1);
        input2_edittext=  v.findViewById(R.id.half_adder_edittext_input_2);
        output1_textview= v.findViewById(R.id.half_adder_textview_output_1);
        output2_textview= v.findViewById(R.id.half_adder_textview_output_2);

        gate_imageview=   v.findViewById(R.id.half_adder_imageview);

        gen_output_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                boolean input1;
                boolean input2;

                if(Objects.equals(input2_edittext.getText().toString(),"0"))
                {
                    input2 = false;

                }
                else if(Objects.equals(input2_edittext.getText().toString(),"1"))
                {
                    input2 = true;

                }
                else
                {
                    tosty(getContext(),"Input 2 is Invalid!"+input2_edittext.getText().toString());
                    return;
                }

                if(Objects.equals(input1_edittext.getText().toString(),"0"))
                {
                    input1 = false;

                }
                else if(Objects.equals(input1_edittext.getText().toString(),"1"))
                {
                    input1 = true;

                }
                else
                {
                    tosty(getContext(),"Input 1 is Invalid!"+input1_edittext.getText().toString());
                    return;
                }

                //setImage(input1,input2);

                boolean[] result=myLogicGate.getHalfAdderOutput(input1,input2);


                output1_textview.setText(result[0]?"1":"0");
                output2_textview.setText(result[1]?"1":"0");


            }
        });

        return v;
    }


    private void setImage(boolean input1,boolean input2)
    {
        if(input1&&input2)
        {
            gate_imageview.setImageResource(R.drawable.and_11);

        }
        else if(input1&&!input2)
        {
            gate_imageview.setImageResource(R.drawable.and_10);

        }
        else if(!input1&&input2)
        {
            gate_imageview.setImageResource(R.drawable.and_01);

        }
        else if(!input1&&!input2)
        {
            gate_imageview.setImageResource(R.drawable.and_00);

        }
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

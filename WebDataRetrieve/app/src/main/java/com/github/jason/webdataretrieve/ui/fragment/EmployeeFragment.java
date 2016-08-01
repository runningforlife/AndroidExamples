package com.github.jason.webdataretrieve.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.jason.webdataretrieve.R;
import com.github.jason.webdataretrieve.parser.XmlParser;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * Created by JasonWang on 2016/8/1.
 */
public class EmployeeFragment extends BaseFragment{
    public static final String TAG = EmployeeFragment.class.getSimpleName();

    private TextView mTvId;
    private TextView mTvFirstName;
    private TextView mTvLastName;
    private TextView mTvNickName;
    private TextView mTvSalary;

    public EmployeeFragment(){

    }

    public static EmployeeFragment newInstance(String type){
        EmployeeFragment fragment = new EmployeeFragment();

        Bundle bundle = new Bundle();
        bundle.putString("type",type);
        fragment.setArguments(bundle);

        return fragment;
    }


    @Override
    public void onAttach(Context context){
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedState){
        View root = inflater.inflate(R.layout.fragment_employee,container,false);

        mTvId = (TextView)root.findViewById(R.id.staff_id);
        mTvFirstName = (TextView)root.findViewById(R.id.staff_first);
        mTvLastName = (TextView)root.findViewById(R.id.staff_last);
        mTvNickName = (TextView)root.findViewById(R.id.staff_nick);
        mTvSalary = (TextView)root.findViewById(R.id.staff_salary);

        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedState){
        super.onActivityCreated(savedState);

        parseXML();
    }

    private void parseXML(){
        try {
            File file = new File("../data/employee.xml");
            FileInputStream in = new FileInputStream(file);
            XmlParser parser = XmlParser.getInstance();
            updateView(parser.parserByXMLPull(in));
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }

    private void updateView(List<XmlParser.Employee> employees){
        XmlParser.Employee employee = employees.get(0);

        mTvId.setText(employee.getId());
        mTvFirstName.setText(employee.getFirstName());
        mTvLastName.setText(employee.getLastName());
        mTvNickName.setText(employee.getNickName());
        mTvSalary.setText(employee.getSalary());
    }

}

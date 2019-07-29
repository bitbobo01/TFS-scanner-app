package com.example.tfsscanner.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.tfsscanner.R;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;

public class ExpandableAdapter extends BaseExpandableListAdapter {
    Context context;
    List<String> headers;
    HashMap<String,List<String>> listChild;

    public ExpandableAdapter(Context context, List<String> headers, HashMap<String, List<String>> listChild) {
        this.context = context;
        this.headers = headers;
        this.listChild = listChild;
    }

    @Override
    public int getGroupCount() {
        return headers.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return listChild.get(headers.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return headers.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listChild.get(headers.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headTitle = (String) getGroup(groupPosition);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.group_view,null);
        TextView txtHeader = (TextView) convertView.findViewById(R.id.textViewHeader);
        txtHeader.setText(headTitle);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String item = (String) getChild(groupPosition,childPosition);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.child_view,null);
        TextView txtItem = (TextView) convertView.findViewById(R.id.textViewChild);
        txtItem.setText(item);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

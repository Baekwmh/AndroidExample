package edu.niit.android.sqlite.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import edu.niit.android.sqlite.R;
import edu.niit.android.sqlite.entity.OrmStudent;
import edu.niit.android.sqlite.entity.Student;

public class OrmCustomAdapter extends BaseAdapter {
    private List<OrmStudent> students;
    private Context context;

    public OrmCustomAdapter(Context context, List<OrmStudent> students) {
        this.context = context;
        this.students = students;
    }

    @Override
    public int getCount() {
        return students.size();
    }

    @Override
    public Object getItem(int position) {
        return students.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder holder;

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_student, parent, false);

            holder = new ViewHolder();
            holder.tvName = view.findViewById(R.id.tv_name);
            holder.tvClassmate = view.findViewById(R.id.tv_classmate);
            holder.tvAge = view.findViewById(R.id.tv_age);
//            holder.tvSchool = view.findViewById(R.id.tv_school);

            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }

        OrmStudent student = students.get(position);
        holder.tvAge.setText(String.valueOf(student.getAge()));
        holder.tvName.setText(student.getName());
        holder.tvClassmate.setText(student.getClassmate());
//        holder.tvSchool.setText(student.getSchool().getName());

        return view;
    }

    static class ViewHolder {
        TextView tvName;
        TextView tvClassmate;
        TextView tvAge;
//        TextView tvSchool;

    }
}

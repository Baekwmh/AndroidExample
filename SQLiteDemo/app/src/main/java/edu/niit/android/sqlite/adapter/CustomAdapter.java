package edu.niit.android.sqlite.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import edu.niit.android.sqlite.R;
import edu.niit.android.sqlite.entity.Student;

public class CustomAdapter extends BaseAdapter {
    private List<Student> students;
    private Context context;

    public CustomAdapter(Context context, List<Student> students) {
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

            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }

        Student student = students.get(position);
        holder.tvAge.setText(String.valueOf(student.getAge()));
        holder.tvName.setText(student.getName());
        holder.tvClassmate.setText(student.getClassmate());

        return view;
    }

    static class ViewHolder {
        TextView tvName;
        TextView tvClassmate;
        TextView tvAge;

    }
}

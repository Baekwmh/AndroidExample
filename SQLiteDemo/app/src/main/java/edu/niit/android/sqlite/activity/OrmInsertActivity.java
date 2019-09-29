package edu.niit.android.sqlite.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.List;

import edu.niit.android.sqlite.R;
import edu.niit.android.sqlite.dao.OrmStudentDao;
import edu.niit.android.sqlite.dao.OrmStudentSchoolDao;
import edu.niit.android.sqlite.dao.StudentDao;
import edu.niit.android.sqlite.entity.OrmStudent;
import edu.niit.android.sqlite.entity.Student;

public class OrmInsertActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etName;
    private Spinner spClassmate;
    private EditText etAge;
    private Button btnConfirm;

    private OrmStudentDao studentDao;
//    private OrmStudentSchoolDao studentDao;
    private OrmStudent currentStudent;
    private boolean isUpdate = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        initView();
        initData();
    }

    private void initData() {
        // 判断是否有数据需要加载
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            currentStudent = (OrmStudent) bundle.get("student");
        }


        if (currentStudent != null) {
            isUpdate = true;
            etName.setText(currentStudent.getName());
            etAge.setText(String.valueOf(currentStudent.getAge()));

            // 设置Spinner值
            List<String> classmates = Arrays.asList(getResources().getStringArray(R.array.classmate_arr));
            spClassmate.setSelection(classmates.indexOf(currentStudent.getClassmate()), true);
//            SpinnerAdapter spinnerAdapter = spClassmate.getAdapter();
//            for (int i = 0; i < spinnerAdapter.getCount(); i++) {
//                if (spinnerAdapter.getItem(i).toString().equals(currentStudent.getClassmate())) {
//                    spClassmate.setSelection(i);
//                    break;
//                }
//            }
        }
        studentDao = new OrmStudentDao(this);
    }

    private void initView() {
        etName = findViewById(R.id.et_name);
        spClassmate = findViewById(R.id.sp_classmate);
        etAge = findViewById(R.id.et_age);

        btnConfirm = findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(this);
    }

    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_confirm:
                // 将输入的内容封装成student对象
                OrmStudent student = new OrmStudent(etName.getText().toString(),
                        spClassmate.getSelectedItem().toString(),
                        Integer.valueOf(etAge.getText().toString()));
                if (isUpdate) {
                    // 更新
                    student.set_id(currentStudent.get_id());
                    studentDao.update(student);  // update table set name=name值 where _id=?
                } else {
                    // 插入数据库表
                    studentDao.insert(student);
                }
                // 返回MainActivity，刷新ListView
                setResult(RESULT_OK, new Intent());
                finish();
                break;
            case R.id.btn_cancel:
                finish();
                break;
        }
    }
}

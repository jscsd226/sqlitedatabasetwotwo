package jscsd.sqlitedatabasetwo;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddActivity extends AppCompatActivity {

    private MyDatabaseHelper dbOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        dbOpenHelper = new MyDatabaseHelper(AddActivity.this,"dict.db",null,1);
        final EditText etWord =(EditText) findViewById(R.id.add_word);
        final EditText etExplain =(EditText) findViewById(R.id.add_interpret);
        Button btn_Save =(Button)findViewById(R.id.save_btn);
        Button btn_Cancel =(Button)findViewById(R.id.cancel_btn1);

        btn_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String word =etWord.getText().toString();
                String explain =etExplain.getText().toString();
                if (word.equals("")||etExplain.equals("")){
                    Toast.makeText(AddActivity.this,"填写的联系人或者电话为空",Toast.LENGTH_SHORT).show();
                }
                else {
                    //调用insertData方法，实现插入数据
                    insertData(dbOpenHelper.getReadableDatabase(),word,explain);
                    //显示提示信息
                    Toast.makeText(AddActivity.this,"添加联系人成功！",Toast.LENGTH_LONG).show();
                }
            }
        });
        btn_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void insertData(SQLiteDatabase readableDatabase,String word,String explain){
        //生词ContentValues对象
        ContentValues values =new ContentValues();
        //向该对象当中插入键值对，其中键是列名，值是希望插入这一列的值，值必须和数据库的数据类型匹配
        values.put("word",word);//保存联系人
        values.put("detail",explain);//保存电话
        readableDatabase.insert("dict",null,values);//执行插入操作
    }

    //更新操作
    //第一个参数是要更新的表名  第二个是一个ContentValeus对象 第三个是where字句
    //dbOpenHelper.update("dict",values,"id=?",new String[]{"?"});

}

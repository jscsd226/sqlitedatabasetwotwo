package jscsd.sqlitedatabasetwo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends Activity {

    private   MyDatabaseHelper dbOpenHelper;//定义DBOpenHelper
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbOpenHelper = new MyDatabaseHelper(MainActivity.this, "dict.db", null, 1);
        final ListView listView = (ListView) findViewById(R.id.result_listView);
        final EditText etSearch = (EditText) findViewById(R.id.search_et);
        Button btnSearch = (Button) findViewById(R.id.search_btn);
        Button btn_add = (Button) findViewById(R.id.btn_add);
        Button btn_delete = (Button) findViewById(R.id.btn_delete);



        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
                String key = etSearch.getText().toString();
                db.delete("dict", "word=?", new String[] { "key" });
            }
        });






        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this,AddActivity.class);
                //通过Intent跳转添加生词界面
                startActivity(intent);

            }
        });


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = etSearch.getText().toString();//获取要查询的姓名
                //query("user",new String[]{"id","name"},"id=?",new String[]{"1"},null,null,null)
                Cursor cursor =dbOpenHelper.getReadableDatabase().query("dict",null,"word=?",new String[] {key},null,null,null);
                //创建ArrayList对象，用于保存查询输出的结果
                ArrayList<Map<String,String>> resultList =new ArrayList<Map<String, String>>();// 创建一个结果集 两个String类型
                while (cursor.moveToNext()){
                    //用while循环一直判断，当下一条为假时，我们的游标循环也就结束了
                    //cursor.movetonext   定义一个游标 它的作用是将游标向下挪动一位 判断当前游标位置的下一条还有没有数据 如果有 就返回真 如果无 就返回假
                    Map<String,String> map =new HashMap<>(); //新开辟一个map的集合空间
                    map.put("word",cursor.getString(1));//在第一行输出姓名
                    map.put("interpret",cursor.getString(2));//在第二行输出电话
                    resultList.add(map);//在XML文件resultList 显示
                }
                if(resultList == null||resultList.size()==0){
                    Toast.makeText(MainActivity.this,"没有相关记录",Toast.LENGTH_LONG).show();
                } else {
                    //定义一个simpleAdapter,供列表项使用
                    SimpleAdapter simpleAdapter =new SimpleAdapter(MainActivity.this,resultList,R.layout.result_main,
                            new String[]{"word","interpret"},new int[]{
                            R.id.result_word,R.id.result_interpret});
                    listView.setAdapter(simpleAdapter);
                }


            }
        });




    }

    //释放数据库连接
    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(dbOpenHelper!=null){
            dbOpenHelper.close();
        }
    }

    //双击退出  使用按下的方式。双击退出一共有三种方式 按下 弹起 定时

//记录用户首次点击返回键的时间
private long firstTime=0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK && event.getAction()==KeyEvent.ACTION_DOWN){
            if (System.currentTimeMillis()-firstTime>2000){
                Toast.makeText(MainActivity.this,"再按一次退出程序--->onKeyDown",Toast.LENGTH_SHORT).show();
                firstTime=System.currentTimeMillis();
            }else{
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
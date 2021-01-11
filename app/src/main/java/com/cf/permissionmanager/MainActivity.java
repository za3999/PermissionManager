package com.cf.permissionmanager;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.cf.common.ServiceManager;
import com.cf.common.permission.Constant;
import com.cf.common.permission.IPermissionService;
import com.cf.common.permission.PermissionCallBack;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button requestBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        requestBtn = (Button) findViewById(R.id.request_btn);
        requestBtn.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        IPermissionService permissionService = ServiceManager.getServices(IPermissionService.class);
        if (permissionService == null) {
            return;
        }
        permissionService.checkPermission(this, new PermissionCallBack() {
                    @Override
                    public void onGranted() {
                        Toast.makeText(MainActivity.this, "权限请求成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onDenied() {
                        Toast.makeText(MainActivity.this, "权限请求失败", Toast.LENGTH_SHORT).show();
                    }
                }, Constant.PERMISSION_CAMERA,
                Constant.PERMISSION_PHONE_STATE,
                Constant.PERMISSION_GET_ACCOUNTS,
                Constant.PERMISSION_SMS);
    }
}

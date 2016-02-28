package shu.gobang.androidclient;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Message;

import Client.AndroidInterface;

/**
 * Created by Administrator on 2016/2/28.
 */
public class ConfirmDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage(getTag())
                .setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Message msg = AndroidInterface.getInstance().gameHandle.obtainMessage();
                        Bundle bundle = new Bundle();
                        bundle.putBoolean("reaction", false);
                        msg.what = 6;
                        msg.setData(bundle);
                        AndroidInterface.getInstance().gameHandle.sendMessage(msg);
                    }
                }).setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Message msg = AndroidInterface.getInstance().gameHandle.obtainMessage();
                Bundle bundle = new Bundle();
                bundle.putBoolean("reaction",true);
                msg.what = 6;
                msg.setData(bundle);
                AndroidInterface.getInstance().gameHandle.sendMessage(msg);
            }
        }) .setTitle("提示");
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
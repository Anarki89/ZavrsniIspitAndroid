package dialog;


import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.example.android.beleske.R;

/**
 * Created by android on 26.11.16..
 */


public class Dialog extends AlertDialog.Builder{


public Dialog(Context context) {
        super(context);

        setTitle(R.string.dialog_about_title);
        setMessage("Pedja Nastasic");
        setCancelable(false);

        setPositiveButton(R.string.dialog_about_yes, new DialogInterface.OnClickListener() {
public void onClick(DialogInterface dialog, int id) {
        dialog.dismiss();
        }
        });

        setNegativeButton(R.string.dialog_about_no, new DialogInterface.OnClickListener() {
public void onClick(DialogInterface dialog, int id) {
        dialog.cancel();
        }
        });
        }


public AlertDialog prepareDialog(){
        AlertDialog dialog = create();
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
        }

        }

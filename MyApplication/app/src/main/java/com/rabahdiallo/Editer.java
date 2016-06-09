package com.rabahdiallo;

        import java.util.UUID;

        import android.graphics.Bitmap;
        import android.graphics.drawable.BitmapDrawable;
        import android.graphics.drawable.Drawable;
        import android.os.Bundle;
        import android.provider.MediaStore;
        import android.app.Activity;
        import android.app.AlertDialog;
        import android.app.Dialog;
        import android.content.DialogInterface;
        import android.view.Menu;
        import android.view.View;
        import android.view.View.OnClickListener;
        import android.widget.Button;
        import android.widget.ImageButton;
        import android.widget.LinearLayout;
        import android.widget.SeekBar;
        import android.widget.TextView;
        import android.widget.Toast;
        import android.widget.SeekBar.OnSeekBarChangeListener;


public class Editer extends Activity implements OnClickListener {

    //inviter l'editeur View
    private EditerView editView;
    //les boutons
    private ImageButton currPaint, drawBtn, eraseBtn, newBtn, saveBtn, opacityBtn;
    //Tailles
    private float smallBrush, mediumBrush, largeBrush;
    //image à editer
    private Bitmap bmp;
    private Drawable drawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editer);
        Bundle extras = getIntent().getExtras();
        bmp =  extras.getParcelable("imagebitmap");
        drawable= (Drawable)new BitmapDrawable(bmp);

        //obtenir l'editeur view
        editView = (EditerView)findViewById(R.id.drawing);

        editView.setBackground(drawable);
        //Obtenir la palette et le premier couleur
        LinearLayout paintLayout = (LinearLayout)findViewById(R.id.paint_colors);
        currPaint = (ImageButton)paintLayout.getChildAt(0);
        currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));

        //Tailles à partir des dimensions
        smallBrush = getResources().getInteger(R.integer.small_size);
        mediumBrush = getResources().getInteger(R.integer.medium_size);
        largeBrush = getResources().getInteger(R.integer.large_size);

        //bouton dessiner
        drawBtn = (ImageButton)findViewById(R.id.draw_btn);
        drawBtn.setOnClickListener(this);

        //taille initiale
        editView.setBrushSize(mediumBrush);

        //bouton effacer
        eraseBtn = (ImageButton)findViewById(R.id.erase_btn);
        eraseBtn.setOnClickListener(this);

        //bouton nouveau
        newBtn = (ImageButton)findViewById(R.id.new_btn);
        newBtn.setOnClickListener(this);

        //bouton enregistrer
        saveBtn = (ImageButton)findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(this);

        //bouton opacité
        opacityBtn = (ImageButton)findViewById(R.id.opacity_btn);
        opacityBtn.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    public void paintClicked(View view){

        editView.setErase(false);
        editView.setPaintAlpha(100);
        editView.setBrushSize(editView.getLastBrushSize());

        if(view!=currPaint){
            ImageButton imgView = (ImageButton)view;
            String color = view.getTag().toString();
            editView.setColor(color);
            //mise à jours
            imgView.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
            currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint));
            currPaint=(ImageButton)view;
        }
    }

    @Override
    public void onClick(View view){

        if(view.getId()==R.id.draw_btn){
            //outil pinceau
            final Dialog brushDialog = new Dialog(this);
            brushDialog.setTitle("choisissez la taille:");
            brushDialog.setContentView(R.layout.brush_chooser);
            //listner sur les boutons de la liste
            ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
            smallBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    editView.setErase(false);
                    editView.setBrushSize(smallBrush);
                    editView.setLastBrushSize(smallBrush);
                    brushDialog.dismiss();
                }
            });
            ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
            mediumBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    editView.setErase(false);
                    editView.setBrushSize(mediumBrush);
                    editView.setLastBrushSize(mediumBrush);
                    brushDialog.dismiss();
                }
            });
            ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
            largeBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    editView.setErase(false);
                    editView.setBrushSize(largeBrush);
                    editView.setLastBrushSize(largeBrush);
                    brushDialog.dismiss();
                }
            });
            //afficher la boite de dilogue
            brushDialog.show();
        }
        else if(view.getId()==R.id.erase_btn){
            //outil d'effacement
            final Dialog brushDialog = new Dialog(this);
            brushDialog.setTitle("Taille de la gomme:");
            brushDialog.setContentView(R.layout.brush_chooser);
            //taille des boutons
            ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
            smallBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    editView.setErase(true);
                    editView.setBrushSize(smallBrush);
                    brushDialog.dismiss();
                }
            });
            ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
            mediumBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    editView.setErase(true);
                    editView.setBrushSize(mediumBrush);
                    brushDialog.dismiss();
                }
            });
            ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
            largeBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    editView.setErase(true);
                    editView.setBrushSize(largeBrush);
                    brushDialog.dismiss();
                }
            });
            brushDialog.show();
        }
        else if(view.getId()==R.id.new_btn){
            //bouton nouveau
            AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
            newDialog.setTitle("Nouveau");
            newDialog.setMessage("Vous perderez le fichier en cours?");
            newDialog.setPositiveButton("Oui", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    editView.startNew();
                    dialog.dismiss();
                }
            });
            newDialog.setNegativeButton("Sortir", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    dialog.cancel();
                }
            });
            newDialog.show();
        }
        else if(view.getId()==R.id.save_btn){
            //Enregisterement
            AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
            saveDialog.setTitle("Enregister");
            saveDialog.setMessage("Enregistrer dans la gallerie?");
            saveDialog.setPositiveButton("Oui", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    //sauvegarder
                    editView.setDrawingCacheEnabled(true);

                    String imgSaved = MediaStore.Images.Media.insertImage(
                            getContentResolver(), editView.getDrawingCache(),
                            UUID.randomUUID().toString()+".png", "drawing");
                    //feedback
                    if(imgSaved!=null){
                        Toast savedToast = Toast.makeText(getApplicationContext(),
                                "Image sauvegardé!", Toast.LENGTH_SHORT);
                        savedToast.show();
                    }
                    else{
                        Toast unsavedToast = Toast.makeText(getApplicationContext(),
                                "Oops! l'image ne peut pas etre sauvegardé.", Toast.LENGTH_SHORT);
                        unsavedToast.show();
                    }
                    editView.destroyDrawingCache();
                }
            });
            saveDialog.setNegativeButton("Sortir", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    dialog.cancel();
                }
            });
            saveDialog.show();
        }
        else if(view.getId()==R.id.opacity_btn){
            //niveau d'opacité
            final Dialog seekDialog = new Dialog(this);
            seekDialog.setTitle("Niveau d'opacité:");
            seekDialog.setContentView(R.layout.opacity_chooser);
            //prendre la valeur
            final TextView seekTxt = (TextView)seekDialog.findViewById(R.id.opq_txt);
            final SeekBar seekOpq = (SeekBar)seekDialog.findViewById(R.id.opacity_seek);
            //set max
            seekOpq.setMax(100);
            //afficher la niveau actuel
            int currLevel = editView.getPaintAlpha();
            seekTxt.setText(currLevel+"%");
            seekOpq.setProgress(currLevel);
            //mise à jours en fur et à mesure
            seekOpq.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    seekTxt.setText(Integer.toString(progress)+"%");
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {}

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {}

            });
            //Bouton OK
            Button opqBtn = (Button)seekDialog.findViewById(R.id.opq_ok);
            opqBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    editView.setPaintAlpha(seekOpq.getProgress());
                    seekDialog.dismiss();
                }
            });
            //afficher le baladeur de l'opacité
            seekDialog.show();
        }
    }

}

package com.example.nazenani.palette;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int MENU_ID_FIRST = Menu.FIRST + 1;
    private static final int REQUEST_CODE = 10;

    private ImageView mImageView;

    private TextView mDarkVibrantSwitch;
    private TextView mVibrantSwatch;
    private TextView mLightVibrantSwatch;
    private TextView mDarkMutedSwatch;
    private TextView mMutedSwatch;
    private TextView mLightMutedSwatch;

    private HashMap<String,String> mHashMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // イメージビューインスタンスを生成
        this.mImageView = (ImageView) findViewById(R.id.imageView);

        // ハッシュマップインスタンスを生成
        this.mHashMap = new HashMap<>();

        // 各テキストビューインスタンスを生成
        this.mDarkVibrantSwitch = (TextView) findViewById(R.id.darkVibrantSwatch);
        this.mVibrantSwatch = (TextView) findViewById(R.id.vibrantSwatch);
        this.mLightVibrantSwatch = (TextView) findViewById(R.id.lightVibrantSwatch);
        this.mDarkMutedSwatch = (TextView) findViewById(R.id.darkMutedSwatch);
        this.mMutedSwatch = (TextView) findViewById(R.id.mutedSwatch);
        this.mLightMutedSwatch = (TextView) findViewById(R.id.lightMutedSwatch);

        // 各テキストビューのクリックイベントを設定
        this.mDarkVibrantSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!MainActivity.this.mHashMap.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "背景：" + MainActivity.this.mHashMap.get("darkVibrantBackground"), Toast.LENGTH_LONG).show();
                }
            }
        });
        this.mVibrantSwatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!MainActivity.this.mHashMap.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "背景：" + MainActivity.this.mHashMap.get("vibrantBackground"), Toast.LENGTH_LONG).show();
                }
            }
        });
        this.mLightVibrantSwatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!MainActivity.this.mHashMap.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "背景：" + MainActivity.this.mHashMap.get("lightVibrantBackground"), Toast.LENGTH_LONG).show();
                }
            }
        });
        this.mDarkMutedSwatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!MainActivity.this.mHashMap.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "背景：" + MainActivity.this.mHashMap.get("darkMutedBackground"), Toast.LENGTH_LONG).show();
                }
            }
        });
        this.mMutedSwatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!MainActivity.this.mHashMap.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "背景：" + MainActivity.this.mHashMap.get("mutedBackground"), Toast.LENGTH_LONG).show();
                }
            }
        });
        this.mLightMutedSwatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!MainActivity.this.mHashMap.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "背景：" + MainActivity.this.mHashMap.get("lightMutedBackground"), Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    /**
     * オプションメニューが最初に呼び出される時に1度だけ呼び出される
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, MENU_ID_FIRST, Menu.NONE, "追加");
        return super.onCreateOptionsMenu(menu);
    }


    /**
     * オプションメニューアイテムが選択された時に呼び出される
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == MENU_ID_FIRST) {
            // 画像ファイルがあるドキュメントプロバイダを検索
            Intent intent = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            } else {
                intent = new Intent(Intent.ACTION_PICK);
            }
            // 開くことができるファイルのみフィルタの結果で取得
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            // 画像のMIMEデータタイプを使用し画像のみを表示するフィルタ
            intent.setType("image/*");
            // アクティビティを開始
            startActivityForResult(intent, REQUEST_CODE);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * アクティビティからの戻り値を取得
     * @param requestCode
     * @param resultCode
     * @param intent
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Uri uri = null;
                if (intent != null) {
                    uri = intent.getData();
                    Log.d(TAG, "onActivityResult: " + uri.toString());

                    try {
                        Bitmap bmp = getBitmapFromUri(uri);
                        this.mImageView.setImageBitmap(bmp);
                        setBitmapInfo(bmp);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    /**
     * ビットマップ情報をセット
     * @param bitmap
     */
    private void setBitmapInfo(Bitmap bitmap) {
        // Bitmap画像のリサイズ
        //this.mBitmap = decodeSampledBitmapFromResource(getResources(), R.drawable.candy, 100, 100);

        // パレット処理
        //new Palette.Builder(this.mBitmap).generate(new Palette.PaletteAsyncListener() {
        new Palette.Builder(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                if (palette != null) {
                    // 鮮やかな色(暗い)
                    if (palette.getDarkVibrantSwatch() != null) {
                        MainActivity.this.mDarkVibrantSwitch.setBackgroundColor(palette.getDarkVibrantSwatch().getRgb());
                        MainActivity.this.mDarkVibrantSwitch.setTextColor(palette.getDarkVibrantSwatch().getTitleTextColor());
                        // rgbを変換
                        String backgroundRgb = changeRgb(palette.getDarkVibrantSwatch().getRgb());
                        // 変数に保持
                        MainActivity.this.mHashMap.put("darkVibrantBackground", backgroundRgb);
                    } else {
                        MainActivity.this.mDarkVibrantSwitch.setBackgroundColor(Color.WHITE);
                        MainActivity.this.mDarkVibrantSwitch.setTextColor(Color.BLACK);
                    }

                    // 鮮やかな色(中間)
                    if (palette.getVibrantSwatch() != null) {
                        MainActivity.this.mVibrantSwatch.setBackgroundColor(palette.getVibrantSwatch().getRgb());
                        MainActivity.this.mVibrantSwatch.setTextColor(palette.getVibrantSwatch().getTitleTextColor());
                        // rgbを変換
                        String backgroundRgb = changeRgb(palette.getVibrantSwatch().getRgb());
                        // 変数に保持
                        MainActivity.this.mHashMap.put("vibrantBackground", backgroundRgb);
                    } else {
                        MainActivity.this.mVibrantSwatch.setBackgroundColor(Color.WHITE);
                        MainActivity.this.mVibrantSwatch.setTextColor(Color.BLACK);
                    }

                    // 鮮やかな色(明るい)
                    if (palette.getLightVibrantSwatch() != null) {
                        MainActivity.this.mLightVibrantSwatch.setBackgroundColor(palette.getLightVibrantSwatch().getRgb());
                        MainActivity.this.mLightVibrantSwatch.setTextColor(palette.getLightVibrantSwatch().getTitleTextColor());
                        // rgbを変換
                        String backgroundRgb = changeRgb(palette.getLightVibrantSwatch().getRgb());
                        // 変数に保持
                        MainActivity.this.mHashMap.put("lightVibrantBackground", backgroundRgb);
                    } else {
                        MainActivity.this.mLightVibrantSwatch.setBackgroundColor(Color.WHITE);
                        MainActivity.this.mLightVibrantSwatch.setTextColor(Color.BLACK);
                    }

                    // 落ち着いた色(暗い)
                    if (palette.getDarkMutedSwatch() != null) {
                        MainActivity.this.mDarkMutedSwatch.setBackgroundColor(palette.getDarkMutedSwatch().getRgb());
                        MainActivity.this.mDarkMutedSwatch.setTextColor(palette.getDarkMutedSwatch().getTitleTextColor());
                        // rgbを変換
                        String backgroundRgb = changeRgb(palette.getDarkMutedSwatch().getRgb());
                        // 変数に保持
                        MainActivity.this.mHashMap.put("darkMutedBackground", backgroundRgb);
                    } else {
                        MainActivity.this.mDarkMutedSwatch.setBackgroundColor(Color.WHITE);
                        MainActivity.this.mDarkMutedSwatch.setTextColor(Color.BLACK);
                    }

                    // 落ち着いた色(中間)
                    if (palette.getMutedSwatch() != null) {
                        MainActivity.this.mMutedSwatch.setBackgroundColor(palette.getMutedSwatch().getRgb());
                        MainActivity.this.mMutedSwatch.setTextColor(palette.getMutedSwatch().getTitleTextColor());
                        // rgbを変換
                        String backgroundRgb = changeRgb(palette.getMutedSwatch().getRgb());
                        // 変数に保持
                        MainActivity.this.mHashMap.put("mutedBackground", backgroundRgb);
                    } else {
                        MainActivity.this.mMutedSwatch.setBackgroundColor(Color.WHITE);
                        MainActivity.this.mMutedSwatch.setTextColor(Color.BLACK);
                    }

                    // 落ち着いた色(明るい)
                    if (palette.getLightMutedSwatch() != null) {
                        MainActivity.this.mLightMutedSwatch.setBackgroundColor(palette.getLightMutedSwatch().getRgb());
                        MainActivity.this.mLightMutedSwatch.setTextColor(palette.getLightMutedSwatch().getTitleTextColor());
                        // rgbを変換
                        String backgroundRgb = changeRgb(palette.getLightMutedSwatch().getRgb());
                        // 変数に保持
                        MainActivity.this.mHashMap.put("lightMutedBackground", backgroundRgb);
                    } else {
                        MainActivity.this.mLightMutedSwatch.setBackgroundColor(Color.WHITE);
                        MainActivity.this.mLightMutedSwatch.setTextColor(Color.BLACK);
                    }
                }
            }
        });
    }


    /**
     * ビットマップ取得URIからビットマップを取得
     * @param uri
     * @return
     * @throws IOException
     */
    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        Bitmap bitmap = null;
        ParcelFileDescriptor parcelFileDescriptor = getContentResolver().openFileDescriptor(uri, "r");
        if (parcelFileDescriptor != null) {
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
            parcelFileDescriptor.close();
        }
        return bitmap;
    }


    /**
     * int型のカラーを16進数に変換しカラーコードとして返す
     * @param rgb
     * @return
     */
    private String changeRgb(int rgb) {
        Log.d(TAG, "changeRgb: " + rgb);
        int a = (rgb >> 24) & 0xff;
        int r = (rgb >> 16) & 0xff;
        int g = (rgb >> 8) & 0xff;
        int b = rgb & 0xff;

        return Integer.toHexString(r) + "," + Integer.toHexString(g) + "," + Integer.toHexString(b);
    }



    /**
     * サイズ比率を取得
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // オプションサイズを取得
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;

        // 実際のサイズが指定サイズより小さい場合はサイズ比率を設定
        if (height > reqHeight || width > reqWidth) {
            int halfHeight = height / 2;
            int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }


    /**
     * リサイズしたビットマップを取得
     * @param res
     * @param resId
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

}

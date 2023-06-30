package com.example.gearmobile.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gearmobile.R;
import com.example.gearmobile.models.Product;
import com.example.gearmobile.models.ProductDescription;

import java.util.List;

public class ProductDescriptionAdapter extends BaseAdapter {
    private Context context;
    private List<Product> products;
    private ProductDescription productDescription;

    public ProductDescriptionAdapter(Context context, ProductDescription productDescription) {
        this.context = context;
        this.productDescription=productDescription;
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public Object getItem(int position) {
        return productDescription;
    }

    @Override
    public long getItemId(int position) {
        return productDescription.getProductId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //ProductDescription product = getItem(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            //convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_detail_product, parent, false);
            convertView = inflater.inflate(R.layout.activity_detail_product, parent, false);
        }
        TableRow row = new TableRow(context);

        // Tạo và cấu hình các trường dữ liệu
        TextView productNameTextView = convertView.findViewById(R.id.titleTextView);
        TextView keyCapTextView = convertView.findViewById(R.id.pdKeycap);
        TextView switchKeyBoardTextView = convertView.findViewById(R.id.pdSwitchKeyBoard);
        TextView typeKeyBoardTextView = convertView.findViewById(R.id.pdTypeKeyBoard);
        TextView connectTextView = convertView.findViewById(R.id.pdConnect);
        TextView ledTextView = convertView.findViewById(R.id.pdLed);
        TextView freighTextView = convertView.findViewById(R.id.pdFreigh);
        TextView ItemDimensionTextView = convertView.findViewById(R.id.pdItemDimension);
        TextView cpuTextView = convertView.findViewById(R.id.pdCpu);
        TextView ramTextView = convertView.findViewById(R.id.pdRam);
        TextView operatingSystemTextView = convertView.findViewById(R.id.pdOperatingSystem);
        TextView batteryTextView = convertView.findViewById(R.id.pdBattery);
        TextView hardDiskTextView = convertView.findViewById(R.id.pdHardDisk);
        TextView graphicCardTextView = convertView.findViewById(R.id.pdGraphicCard);
        TextView keyBoardTextView = convertView.findViewById(R.id.pdKeyBoard);
        TextView audioTextView = convertView.findViewById(R.id.pdAudio);
        TextView wifiTextView = convertView.findViewById(R.id.pdWifi);
        TextView bluetoothTextView = convertView.findViewById(R.id.pdBluetooth);
        TextView colorTextView = convertView.findViewById(R.id.pdColor);
        TextView frameRateTextView = convertView.findViewById(R.id.pdFrameRate);
        TextView screenSizeTextView = convertView.findViewById(R.id.pdScreenSize);
        TextView screenTypeTextView = convertView.findViewById(R.id.pdScreenType);

        // Các TextView khác tương tự
        productNameTextView.setText(productDescription.getProduct().getProductName());
        keyCapTextView.setText(productDescription.getKeycap());
        switchKeyBoardTextView.setText(productDescription.getSwitchKeyBoard());
        typeKeyBoardTextView.setText(productDescription.getTypeKeyboard());
        connectTextView.setText(productDescription.getConnect());
        ledTextView.setText(productDescription.getLed());
        freighTextView.setText(productDescription.getFreigh());
        ItemDimensionTextView.setText(productDescription.getItemDimension());
        cpuTextView.setText(productDescription.getCpu());
        ramTextView.setText(productDescription.getRam());
        operatingSystemTextView.setText(productDescription.getOperatingSystem());
        batteryTextView.setText(productDescription.getBattery());
        hardDiskTextView.setText(productDescription.getHardDisk());
        graphicCardTextView.setText(productDescription.getGraphicCard());
        keyBoardTextView.setText(productDescription.getKeyBoard());
        audioTextView.setText(productDescription.getAudio());
        wifiTextView.setText(productDescription.getWifi());
        bluetoothTextView.setText(productDescription.getBluetooth());
        colorTextView.setText(productDescription.getColor());
        frameRateTextView.setText(productDescription.getFrameRate());
        screenSizeTextView.setText(productDescription.getScreenSize());
        screenTypeTextView.setText(productDescription.getScreenType());

        //addView cho từng row
        row.addView(productNameTextView);
        row.addView(keyCapTextView);
        row.addView(switchKeyBoardTextView);
        row.addView(typeKeyBoardTextView);
        row.addView(connectTextView);
        row.addView(ledTextView);
        row.addView(freighTextView);
        row.addView(ItemDimensionTextView);
        row.addView(cpuTextView);
        row.addView(ramTextView);
        row.addView(operatingSystemTextView);
        row.addView(batteryTextView);
        row.addView(hardDiskTextView);
        row.addView(graphicCardTextView);
        row.addView(keyBoardTextView);
        row.addView(audioTextView);
        row.addView(wifiTextView);
        row.addView(bluetoothTextView);
        row.addView(colorTextView);
        row.addView(frameRateTextView);
        row.addView(screenSizeTextView);
        row.addView(screenTypeTextView);


        // Thiết lập các TextView khác tương tự

        return convertView;
    }
}

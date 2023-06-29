package com.example.gearmobile.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
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
        keyCapTextView.setText("Keycap:"+productDescription.getKeycap());
        switchKeyBoardTextView.setText("SwitchKeyBoard"+productDescription.getSwitchKeyBoard());
        typeKeyBoardTextView.setText("TypeKeyBoard"+productDescription.getTypeKeyboard());
        connectTextView.setText("SwitchKeyBoard"+productDescription.getConnect());
        ledTextView.setText("SwitchKeyBoard"+productDescription.getLed());
        freighTextView.setText("SwitchKeyBoard"+productDescription.getFreigh());
        ItemDimensionTextView.setText("SwitchKeyBoard"+productDescription.getItemDimension());
        cpuTextView.setText("SwitchKeyBoard"+productDescription.getCpu());
        ramTextView.setText("SwitchKeyBoard"+productDescription.getRam());
        operatingSystemTextView.setText("SwitchKeyBoard"+productDescription.getOperatingSystem());
        batteryTextView.setText("SwitchKeyBoard"+productDescription.getBattery());
        hardDiskTextView.setText("SwitchKeyBoard"+productDescription.getHardDisk());
        graphicCardTextView.setText("SwitchKeyBoard"+productDescription.getGraphicCard());
        keyBoardTextView.setText("SwitchKeyBoard"+productDescription.getKeyBoard());
        audioTextView.setText("SwitchKeyBoard"+productDescription.getAudio());
        wifiTextView.setText("SwitchKeyBoard"+productDescription.getWifi());
        bluetoothTextView.setText("SwitchKeyBoard"+productDescription.getBluetooth());
        colorTextView.setText("SwitchKeyBoard"+productDescription.getColor());
        frameRateTextView.setText("SwitchKeyBoard"+productDescription.getFrameRate());
        screenSizeTextView.setText("SwitchKeyBoard"+productDescription.getScreenSize());
        screenTypeTextView.setText("SwitchKeyBoard"+productDescription.getScreenType());


        // Thiết lập các TextView khác tương tự

        return convertView;
    }
}

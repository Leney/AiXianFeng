package com.bjzcht.lovebeequick.gui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bjzcht.lovebeequick.R;
import com.bjzcht.lovebeequick.model.Address;
import com.bjzcht.lovebeequick.util.ParseUtil;
import com.bjzcht.lovebeequick.util.Util;

/**
 * 编辑收货地址
 * Created by lilijun on 2016/9/18.
 */
public class EditAddressActivity extends BaseActivity implements View.OnClickListener {
    public static final int UPDATE_SUCCESS_RESULT_CODE = 10009;
    /**
     * 获取省份、市、城镇信息
     */
    private final String GET_ADDRESS_NAME = "address/select";

    /**
     * 更新地址信息
     */
    private final String UPDATE_ADDRESS = "address/updateAddress";
    /**
     * 获取到的跳转过来的Address对象数据
     */
    private Address intentAddress;
    /**
     * 收货人，联系电话，详细地址，邮政编码
     */
    private EditText consigneeInput, telephoneInput, addressDetailInput, postcodeInput;
    /**
     * 省份，市，城镇
     */
    private Spinner provinceSpinner, citySpinner, townSpinner;
    private FrameLayout provinceLay, cityLay, townLay;
    /**
     * 保存按钮
     */
    private Button saveBtn;

    /**
     * 获取省份、市、城镇等信息的请求参数
     */
    private Map<String, Object> params = null;

    private ArrayAdapter provinceAdapter;
    private ArrayAdapter cityAdapter;
    private ArrayAdapter townAdapter;

    /**
     * 省级信息数据
     */
    private List<String> provinceValues;
    /**
     * 市级信息数据
     */
    private List<String> cityValues;
    /**
     * 城镇级信息数据
     */
    private List<String> townValues;

    /**
     * spinner选择完成之后最终的id数据集合
     */
    private List<Integer> locationIds;

    /**
     * 用户选择好spinner地址信息之后的地址id
     */
    private int curLocationId = -1;

    /**
     * 正在保存dialog
     */
    private ProgressDialog progressDialog;

//    /** 初始化获取区域信息的参数信息*/
//    private Map<String,Object> initParams;

    @Override
    protected void initView() {
        intentAddress = (Address) getIntent().getSerializableExtra("address");
        if (intentAddress == null) {
            showErrorView();
            return;
        }
        provinceValues = new ArrayList<>();
        cityValues = new ArrayList<>();
        townValues = new ArrayList<>();
        locationIds = new ArrayList<>();

        provinceValues.add(getResources().getString(R.string.choice_province));
        cityValues.add(getResources().getString(R.string.choice_city));
        townValues.add(getResources().getString(R.string.choice_town));
        locationIds.add(-1);

        setTitleName(getResources().getString(R.string.edit_address));
        setCenterView(R.layout.activity_add_new_adress);
        consigneeInput = (EditText) findViewById(R.id.add_address_consignee_input);
        telephoneInput = (EditText) findViewById(R.id.add_address_telephone_input);
        addressDetailInput = (EditText) findViewById(R.id.add_address_detail_input);
        postcodeInput = (EditText) findViewById(R.id.add_address_postcode_input);
        provinceSpinner = (Spinner) findViewById(R.id.add_address_area_province);
        citySpinner = (Spinner) findViewById(R.id.add_address_area_city);
        townSpinner = (Spinner) findViewById(R.id.add_address_area_town);
        provinceLay = (FrameLayout) findViewById(R.id.add_address_area_province_lay);
        cityLay = (FrameLayout) findViewById(R.id.add_address_area_city_lay);
        townLay = (FrameLayout) findViewById(R.id.add_address_area_town_lay);
        saveBtn = (Button) findViewById(R.id.add_address_save_btn);
        saveBtn.setOnClickListener(this);

        //适配器
        provinceAdapter = new ArrayAdapter<>(EditAddressActivity.this, R.layout
                .spinner_address_left_item_lay,
                provinceValues);
        cityAdapter = new ArrayAdapter<>(EditAddressActivity.this, R.layout
                .spinner_address_left_item_lay,
                cityValues);
        townAdapter = new ArrayAdapter<>(EditAddressActivity.this, R.layout
                .spinner_address_left_item_lay,
                townValues);

        provinceSpinner.setAdapter(provinceAdapter);
        provinceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    if (params == null) {
                        params = new HashMap<>();
                    } else {
                        params.clear();
                    }
                    params.put("province", provinceValues.get(i));
                    loadDataGet(GET_ADDRESS_NAME, params);
                }

                cityLay.setVisibility(View.GONE);
                townLay.setVisibility(View.GONE);

                locationIds.clear();
                locationIds.add(-1);
                curLocationId = -1;
                cityValues.clear();
                townValues.clear();

                cityAdapter.notifyDataSetChanged();
                townAdapter.notifyDataSetChanged();

                citySpinner.setSelection(0);
                townSpinner.setSelection(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        citySpinner.setAdapter(cityAdapter);
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    params.put("city", cityValues.get(i));
                    loadDataGet(GET_ADDRESS_NAME, params);
                }
                townLay.setVisibility(View.GONE);
                locationIds.clear();
                locationIds.add(-1);
                curLocationId = -1;
                townValues.clear();
                townAdapter.notifyDataSetChanged();
                townSpinner.setSelection(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        townSpinner.setAdapter(townAdapter);
        townSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                curLocationId = locationIds.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

//        initParams = new HashMap<>();
//        initParams.put("country",intentAddress.getCountry());
//        initParams.put("province",intentAddress.getProvince());
//        initParams.put("city",intentAddress.getCity());
        loadDataGet(GET_ADDRESS_NAME, null);
    }

    /**
     * 设置初始化的编辑信息
     */
    private void initData() {
        consigneeInput.setText(intentAddress.getName());
        telephoneInput.setText(intentAddress.getMobile());
        addressDetailInput.setText(intentAddress.getDetailAddress());
        postcodeInput.setText(intentAddress.getPostcode());
    }

    @Override
    protected void onLoadDataSuccess(String tag, JSONObject resultObject) {
        super.onLoadDataSuccess(tag, resultObject);
        if (TextUtils.equals(tag, GET_ADDRESS_NAME)) {
            if (params == null) {
                // 获取省份信息成功
                List<String> provinceList = ParseUtil.parseAddressInfos("province", resultObject);
                if (provinceList != null && !provinceList.isEmpty()) {
                    provinceValues.addAll(provinceList);
                    provinceAdapter.notifyDataSetChanged();
                    provinceLay.setVisibility(View.VISIBLE);
                    cityLay.setVisibility(View.GONE);
                    townLay.setVisibility(View.GONE);
                    initData();
                    showCenterView();
                } else {
                    // 没数据就是请求失败
                    showErrorView();
                }
            } else {
                if (params.containsKey("city")) {
                    // 获取城镇信息成功
                    locationIds.clear();
                    locationIds.add(-1);
                    curLocationId = -1;
                    List<String> townList = ParseUtil.parseAddressInfosAndIds(resultObject,
                            locationIds);
                    if (townList != null && !townList.isEmpty()) {
                        townValues.clear();
                        townValues.add(getResources().getString(R.string.choice_town));
                        townValues.addAll(townList);
                        townAdapter.notifyDataSetChanged();
                        townLay.setVisibility(View.VISIBLE);
                    }
                } else {
                    // 获取市级信息成功
                    locationIds.clear();
                    locationIds.add(-1);
                    curLocationId = -1;
                    List<String> cityList = ParseUtil.parseAddressInfos("city", resultObject);
                    if (cityList != null && !cityList.isEmpty()) {
                        cityValues.clear();
                        townValues.clear();

                        cityValues.add(getResources().getString(R.string.choice_city));
                        cityValues.addAll(cityList);
                        cityAdapter.notifyDataSetChanged();
                        cityLay.setVisibility(View.VISIBLE);
                        townLay.setVisibility(View.GONE);
                    }
                }
            }
        } else if (TextUtils.equals(tag, UPDATE_ADDRESS)) {
            // 更新收货收货地址成功
            progressDialog.dismiss();
            Intent data = new Intent();
//            Address address = new Address();
            intentAddress.setProvince((String) provinceSpinner.getSelectedItem());
            intentAddress.setCity((String) citySpinner.getSelectedItem());
            intentAddress.setTown((String) townSpinner.getSelectedItem());
            intentAddress.setMobile(telephoneInput.getText().toString().trim());
            intentAddress.setName(consigneeInput.getText().toString().trim());
            intentAddress.setDetailAddress(addressDetailInput.getText().toString().trim());
            intentAddress.setPostcode(postcodeInput.getText().toString().trim());
            intentAddress.setLocationId(curLocationId);
            data.putExtra("address", intentAddress);
            setResult(UPDATE_SUCCESS_RESULT_CODE, data);
//            EventBus.getDefault().post(Constants.UPDATE_ADDRESS_SUCCESS);
            EventBus.getDefault().post(intentAddress);
            Util.showToast(EditAddressActivity.this, getResources().getString(R.string
                    .update_address_success));
            finish();
        }
    }

    @Override
    protected void onLoadDataError(String tag, int errorCode, String msg) {
        super.onLoadDataError(tag, errorCode, msg);
        if (TextUtils.equals(tag, GET_ADDRESS_NAME)) {
            if (params == null) {
                // 获取省份信息失败
                showErrorView();
            }
        } else if (TextUtils.equals(tag, UPDATE_ADDRESS)) {
            // 添加新地址失败
            progressDialog.dismiss();
            if (TextUtils.isEmpty(msg.trim())) {
                Util.showToast(EditAddressActivity.this, getResources().getString(R.string
                        .add_address_failed));
            } else {
                Util.showToast(EditAddressActivity.this, msg);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_address_save_btn:
                // 保存按钮
                String consignee = consigneeInput.getText().toString().trim();
                if (TextUtils.isEmpty(consignee)) {
                    Util.showToast(EditAddressActivity.this, getResources().getString(R.string
                            .input_consignee));
                    return;
                }

                String telephone = telephoneInput.getText().toString().trim();
                if (TextUtils.isEmpty(telephone) || telephone.length() != 11) {
                    Util.showToast(EditAddressActivity.this, getResources().getString(R.string
                            .input_consignee_tel));
                    return;
                }
                if (curLocationId == -1) {
                    Util.showToast(EditAddressActivity.this, getResources().getString(R.string
                            .choice_area));
                    return;
                }
                String addressDetail = addressDetailInput.getText().toString().trim();
                if (TextUtils.isEmpty(addressDetail)) {
                    Util.showToast(EditAddressActivity.this, getResources().getString(R.string
                            .input_address_detail));
                    return;
                }

                String postcode = postcodeInput.getText().toString().trim();
                if (TextUtils.isEmpty(postcode) || postcode.length() != 6) {
                    Util.showToast(EditAddressActivity.this, getResources().getString(R.string
                            .input_postcode));
                    return;
                }
                Map<String, String> params = new HashMap<>();
                params.put("id", intentAddress.getId() + "");
                params.put("name", consignee);
                params.put("address", addressDetail);
                params.put("mobile", telephone);
                params.put("senderzip", postcode);
                params.put("locationId", curLocationId + "");
                params.put("isReceiveDefault", intentAddress.isReceiveDefault() + "");
                params.put("isSendDefault", "false");
                loadDataPost(UPDATE_ADDRESS, params);
                progressDialog = Util.showLoadingDialog(EditAddressActivity.this, progressDialog);
                break;
        }
    }

    @Override
    protected void tryAgain() {
        super.tryAgain();
//        loadDataGet(GET_ADDRESS_NAME, initParams);
        loadDataGet(GET_ADDRESS_NAME, null);
    }

    public static void startActivity(Context context, Address address) {
        Intent intent = new Intent(context, EditAddressActivity.class);
        intent.putExtra("address", address);
        context.startActivity(intent);
    }

    public static void startActivityForResult(Activity context, Address address, int requestCode) {
        Intent intent = new Intent(context, EditAddressActivity.class);
        intent.putExtra("address", address);
        context.startActivityForResult(intent, requestCode);
    }
}
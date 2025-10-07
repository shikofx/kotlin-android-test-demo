package by.pda.demoapp.android.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import by.pda.demoapp.android.R;
import by.pda.demoapp.android.database.AppDatabase;
import by.pda.demoapp.android.databinding.FragmentProductCatalogBinding;
import by.pda.demoapp.android.model.ProductModel;
import by.pda.demoapp.android.utils.Constants;
import by.pda.demoapp.android.utils.SingletonClass;
import by.pda.demoapp.android.utils.base.BaseFragment;
import by.pda.demoapp.android.view.activities.MainActivity;
import by.pda.demoapp.android.view.adapters.ProductsAdapter;
import by.pda.demoapp.android.viewModel.ProductCatalogViewModel;
import by.pda.demoapp.android.viewModel.ProductCatalogViewModelFactory;

import java.util.List;

public class ProductCatalogFragment extends BaseFragment implements View.OnClickListener {
    private FragmentProductCatalogBinding binding;
    private final boolean addVisualChanges;
    List<ProductModel> productList;
    ProductsAdapter adapter;
    ProductCatalogViewModel viewModel;

    public static ProductCatalogFragment newInstance(String param1, String param2, int param3) {
        boolean addVisualChanges = SingletonClass.getInstance().getHasVisualChanges();
        ProductCatalogFragment fragment = new ProductCatalogFragment(addVisualChanges);
        Bundle args = new Bundle();
        args.putString(Constants.ARG_PARAM1, param1);
        args.putString(Constants.ARG_PARAM2, param2);
        args.putInt(Constants.ARG_PARAM3, param3);
        fragment.setArguments(args);
        return fragment;
    }

    public ProductCatalogFragment(boolean addVisualChanges) {
        this.addVisualChanges = addVisualChanges;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAct = getActivity();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(Constants.ARG_PARAM1, "");
            mParam2 = getArguments().getString(Constants.ARG_PARAM2, "");
            mParam3 = getArguments().getInt(Constants.ARG_PARAM3, -1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_product_catalog, container, false);
        viewModel = new ViewModelProvider(this, new ProductCatalogViewModelFactory(app)).get(ProductCatalogViewModel.class);
        bindData();

        // Add visual changes
        if (this.addVisualChanges) {
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams)binding.productTV.getLayoutParams();
            params.setMarginStart(400);
            binding.productTV.requestLayout();;
        }

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void bindData() {
        mDb = AppDatabase.getInstance(getActivity());

        int spanCount = 2;
        binding.productRV.setLayoutManager(new GridLayoutManager(mAct, spanCount));

        observer();

        // Trigger the initial data load
        viewModel.getAllProducts(MainActivity.selectedSort);
    }

    private void observer() {
        viewModel.getAllProductsLiveData().observe(getViewLifecycleOwner(), new Observer<List<ProductModel>>() {
            @Override
            public void onChanged(List<ProductModel> productModels) {
                if (productModels != null) {
                    productList = productModels;
                    setAdapter();
                }
            }
        });
    }

    public void updateData() {
        viewModel.getAllProducts(MainActivity.selectedSort);
    }

    private void setAdapter() {

        final Integer[] meta = new Integer[] {
                0, null, 2, 3, 4, 5 // null is an intentionally introduced bug for demos
        };

        mAct.runOnUiThread(() -> {
            adapter = new ProductsAdapter(mAct, productList, (position, status) -> {

                Bundle bundle = ST.getBundle(MainActivity.FRAGMENT_PRODUCT_DETAIL, 1);
                bundle.putString("meta", "" + meta[position].intValue());
                bundle.putString(Constants.ARG_PARAM1, String.valueOf(productList.get(position).getId()));

                ST.startMainActivity(mAct, bundle);
            });
            binding.productRV.setAdapter(adapter);
        });
    }


    @Override
    public void onClick(View view) {

    }
}

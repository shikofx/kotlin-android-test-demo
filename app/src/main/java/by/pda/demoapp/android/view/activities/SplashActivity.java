package by.pda.demoapp.android.view.activities;

import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import by.pda.demoapp.android.R;
import by.pda.demoapp.android.database.AppDatabase;
import by.pda.demoapp.android.databinding.ActivitySplashBinding;
import by.pda.demoapp.android.model.ProductModel;
import by.pda.demoapp.android.utils.base.BaseActivity;
import by.pda.demoapp.android.viewModel.SplashViewModel;
import by.pda.demoapp.android.viewModel.SplashViewModelFactory;

import java.util.List;

public class SplashActivity extends BaseActivity {
	private ActivitySplashBinding binding;
	SplashViewModel viewModel;
	private AppDatabase mDb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);

		viewModel = new ViewModelProvider(this, new SplashViewModelFactory(this.getApplication())).get(SplashViewModel.class);
		initViews();
	}

	private void initViews() {
		mDb = AppDatabase.getInstance(getApplicationContext());
		checkObserver();
	}

	private void checkObserver() {
		viewModel.getAllProductsLiveData().observe(this, productModels -> {
            if (productModels != null && !productModels.isEmpty()) {
                ST.startActivity(mAct, MainActivity.class, ST.START_ACTIVITY_WITH_FINISH);
            } else {
                populateProductsDb(viewModel);
            }
        });

		viewModel.getProgressBarState().observe(this, integer -> {
            if (integer == View.GONE) {
                ST.startActivity(mAct, MainActivity.class, ST.START_ACTIVITY_WITH_FINISH);
            }
        });
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

}

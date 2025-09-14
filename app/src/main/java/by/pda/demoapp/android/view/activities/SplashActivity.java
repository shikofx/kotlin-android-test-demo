package by.pda.demoapp.android.view.activities;

import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import by.pda.demoapp.android.R;
import by.pda.demoapp.android.database.AppDao;
import by.pda.demoapp.android.database.AppDatabase;
import by.pda.demoapp.android.database.AppExecutors;
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
		viewModel.allProducts.observe(this, new Observer<List<ProductModel>>() {
			@Override
			public void onChanged(List<ProductModel> productModels) {
				if (productModels != null && productModels.size() > 0) {
					ST.startActivity(mAct, MainActivity.class, ST.START_ACTIVITY_WITH_FINISH);
				} else {
					populateProductsDb(viewModel);
				}
			}
		});

		viewModel.pb.observe(this, new Observer<Integer>() {
			@Override
			public void onChanged(Integer integer) {
				if (integer == View.GONE) {
					ST.startActivity(mAct, MainActivity.class, ST.START_ACTIVITY_WITH_FINISH);
				}
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	public void insertProducts(AppDao dao, List<ProductModel> list) {
		AppExecutors.getInstance().diskIO().execute(new Runnable() {
			@Override
			public void run() {
				dao.insertProduct(list);
				ST.startActivity(mAct, MainActivity.class, ST.START_ACTIVITY_WITH_FINISH);
			}
		});
	}

	private void checkLocalDataBase() {
		AppExecutors.getInstance().diskIO().execute(new Runnable() {
			@Override
			public void run() {
				final List<ProductModel> productList = mDb.personDao().getAllProducts();
				if (productList != null && productList.size() == 0) {
					populateProductsDb(viewModel);
				} else {
//                    myVieswModel.addDelays();
					ST.startActivity(mAct, MainActivity.class, ST.START_ACTIVITY_WITH_FINISH);
				}
			}
		});
	}
}
